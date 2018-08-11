package cn.itcast.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.dao.FixedAreaRepository;
import cn.itcast.domain.FixedArea;
import cn.itcast.service.FixedAreaService;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {

		@Autowired
		private FixedAreaRepository fixedAreaRepository;

		@Override
		public void save(FixedArea fixedArea) {
			fixedAreaRepository.save(fixedArea);
			
		}

		@Override
		public Page<FixedArea> findAll(Specification<FixedArea> specification, Pageable pageable) {
			return fixedAreaRepository.findAll(specification, pageable);
			
		}
}
