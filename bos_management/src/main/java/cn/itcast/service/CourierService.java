package cn.itcast.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.domain.Courier;

public interface CourierService {

	void save(Courier courier);

	Page<Courier> findAll(Specification<Courier> spec,Pageable pageable);

	void updateDeltag(String[] idArray);

	void restoreDeltag(String[] idArray);

}
