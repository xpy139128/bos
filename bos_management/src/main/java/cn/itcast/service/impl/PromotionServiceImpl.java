package cn.itcast.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.dao.PromotionRepository;
import cn.itcast.domain.PageBean;
import cn.itcast.domain.take_delivery.Promotion;
import cn.itcast.service.PromotionService;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private PromotionRepository promotionRepository;
	
	@Override
	public void save(Promotion t) {
		
		promotionRepository.save(t);
	}

	@Override
	public Page<Promotion> findAll(Pageable pageable) {
		return promotionRepository.findAll(pageable);
	}

	@Override
	public PageBean<Promotion> findPageData(int page, int rows) {
		Pageable pageable = new PageRequest(page-1,rows);
		
		Page<Promotion> pageQuery = promotionRepository.findAll(pageable);
		
		PageBean<Promotion> pageBean = new PageBean<Promotion>();
		pageBean.setPageData(pageQuery.getContent());
		pageBean.setTotalCount(pageQuery.getNumberOfElements());
		System.out.println(pageBean);
		return pageBean;
	}

	@Override
	public Promotion findById(Integer id) {
		
		return promotionRepository.findOne(id);
	}

	@Override
	public void updateStatus(Date date) {
		promotionRepository.update(date);
		
	}

}
