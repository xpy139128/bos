package cn.itcast.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.itcast.dao.AreaRepository;
import cn.itcast.domain.Area;
import cn.itcast.service.AreaService;

@Service

public class AreaServiceImpl implements AreaService {

	@Autowired
	private AreaRepository areaRepository;
	
	@Override
	public void save(List<Area> list) {
		areaRepository.save(list);
		
	}

	@Override
	public Page<Area> findAll(Specification<Area> spec,Pageable pageable) {
		
		return areaRepository.findAll(spec,pageable);
	}
	
	

}
