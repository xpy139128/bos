package cn.itcast.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.itcast.domain.take_delivery.WayBill;


@Repository
public interface WayBillRepository extends JpaRepository<WayBill, Integer> {

	WayBill findByWayBillNum(String wayBillNum);

}
