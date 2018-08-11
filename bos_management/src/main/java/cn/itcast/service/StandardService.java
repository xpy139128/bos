package cn.itcast.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.domain.Standard;

public interface StandardService {

	void save(Standard standard);

	Page<Standard> findAll(Pageable pageable);

	List<Standard> findAll();

}
