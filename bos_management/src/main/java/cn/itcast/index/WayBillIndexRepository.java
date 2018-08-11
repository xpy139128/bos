package cn.itcast.index;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import cn.itcast.domain.take_delivery.WayBill;

public interface WayBillIndexRepository extends ElasticsearchRepository<WayBill, Integer>{

}
