package cn.itcast.service.impl;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.dao.StandardRepository;
import cn.itcast.domain.Standard;
import cn.itcast.service.StandardService;

@Service
@Transactional

public class StandardServiceImpl implements StandardService {

	@Autowired
	private StandardRepository standardRepository;
	
	@Override
	@CacheEvict(value="standard",allEntries=true)
	public void save(Standard standard) {
		standardRepository.save(standard);
	}

	@Override
	@CacheEvict(value="standard",key="#pageable.pageNumber+'_'+#pageable.pageSize")
	public Page<Standard> findAll(Pageable pageable) {
		
		return standardRepository.findAll(pageable);
	}

	@Override
	@Cacheable("standard")
	public List<Standard> findAll() {
		
		return standardRepository.findAll();
	}

}
