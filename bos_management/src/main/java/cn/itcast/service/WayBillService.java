package cn.itcast.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.domain.take_delivery.WayBill;

public interface WayBillService {

	void save(WayBill t);

	Page<WayBill> findAll(WayBill t, Pageable pageable);

	WayBill findByWayBillNum(String wayBillNum);

}
