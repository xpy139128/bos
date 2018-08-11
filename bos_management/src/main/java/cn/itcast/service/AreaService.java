package cn.itcast.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.domain.Area;

public interface AreaService {

	void save(List<Area> list);

	Page<Area> findAll(Specification<Area> spec,Pageable pageable);

}
