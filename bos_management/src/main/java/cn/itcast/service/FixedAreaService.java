package cn.itcast.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.domain.FixedArea;

public interface FixedAreaService {

	void save(FixedArea t);

	Page<FixedArea> findAll(Specification<FixedArea> specification, Pageable pageable);

}
