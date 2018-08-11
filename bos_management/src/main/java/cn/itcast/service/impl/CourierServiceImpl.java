package cn.itcast.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.dao.CourierRepository;
import cn.itcast.domain.Courier;
import cn.itcast.service.CourierService;
@Service
@Transactional
public class CourierServiceImpl implements CourierService {

	@Autowired
	private CourierRepository courierRepository;
	@Override
	public void save(Courier courier) {
		courierRepository.save(courier);
	}
	@Override
	public Page<Courier> findAll(Specification<Courier> spec,Pageable pageable) {
		
		return courierRepository.findAll(spec,pageable);
	}
	@Override
	public void updateDeltag(String[] idArray) {
		for(int i = 0;i<idArray.length;i++) {
			int id = Integer.parseInt(idArray[i]);
			courierRepository.updateDeltag(id);
		}
		
		
	}
	@Override
	public void restoreDeltag(String[] idArray) {
		for(int i = 0;i<idArray.length;i++) {
			int id = Integer.parseInt(idArray[i]);
			courierRepository.restoreDeltag(id);
		}
		
	}

}
