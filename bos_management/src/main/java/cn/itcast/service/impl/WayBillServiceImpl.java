package cn.itcast.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.dao.WayBillRepository;
import cn.itcast.domain.take_delivery.WayBill;
import cn.itcast.index.WayBillIndexRepository;
import cn.itcast.service.WayBillService;

@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {

	@Autowired
	private WayBillRepository wayBillRepository;
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;

	@Override
	public void save(WayBill wayBill) {
		System.err.println(wayBill);
		// 判断运单号是否存在
				WayBill persistWayBill = wayBillRepository.findByWayBillNum(wayBill
						.getWayBillNum());
				if (persistWayBill == null || persistWayBill.getId() == null) {
					// 运单不存在
					wayBillRepository.save(wayBill);
					// 保存索引
					wayBillIndexRepository.save(wayBill);
				} else {
					// 运单存在
					try {
						Integer id = persistWayBill.getId();
						BeanUtils.copyProperties(persistWayBill, wayBill);
						persistWayBill.setId(id);
						// 保存索引
						wayBillIndexRepository.save(persistWayBill);

					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e.getMessage());
					}
				}

	}

	@Override
	public Page<WayBill> findAll(WayBill t, Pageable pageable) {
		System.err.println(t);
		if (StringUtils.isBlank(t.getWayBillNum()) && StringUtils.isBlank(t.getSendAddress())
				&& StringUtils.isBlank(t.getRecAddress()) && StringUtils.isBlank(t.getSendProNum())
				&& (t.getSignStatus() == null || t.getSignStatus() == 0)) {
			System.err.println("from oracle");
			return wayBillRepository.findAll(pageable);
		} else {
			System.err.println("from es");
			BoolQueryBuilder query = new BoolQueryBuilder();
			if (StringUtils.isNotBlank(t.getWayBillNum())) {
				QueryBuilder builder = new TermQueryBuilder("wayBillNum", t.getWayBillNum()); 
				query.must(builder);
			}
			if (StringUtils.isNotBlank(t.getSendAddress())) {
				QueryBuilder builder1 = new WildcardQueryBuilder("sendAddress", "*" + t.getSendAddress() + "*");

				QueryBuilder builder2 = new QueryStringQueryBuilder(t.getSendAddress()).field("sendAddress")
						.defaultOperator(Operator.AND);

				BoolQueryBuilder builder = new BoolQueryBuilder();
				builder.should(builder1);
				builder.should(builder2);
				query.must(builder);

			}
			if (StringUtils.isNotBlank(t.getRecAddress())) {
				QueryBuilder builder = new WildcardQueryBuilder("recAddress", "*" + t.getRecAddress() + "*");
				query.must(builder);
			}
			if (StringUtils.isNotBlank(t.getSendProNum())) {
				QueryBuilder builder = new TermQueryBuilder("sendProNum", t.getSendProNum());
				query.must(builder);
			}
			if (t.getSignStatus() != null && t.getSignStatus() != 0) {
				QueryBuilder builder = new TermQueryBuilder("signStatus", t.getSignStatus());
				query.must(builder);
			}

			SearchQuery searchQuery = new NativeSearchQuery(query);
			searchQuery.setPageable(pageable);
			return wayBillIndexRepository.search(searchQuery);
		}

	}

	@Override
	public WayBill findByWayBillNum(String wayBillNum) {
		// TODO Auto-generated method stub
		return wayBillRepository.findByWayBillNum(wayBillNum);
	}

}
