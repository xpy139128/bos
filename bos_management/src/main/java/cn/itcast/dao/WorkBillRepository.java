package cn.itcast.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.domain.take_delivery.WorkBill;



public interface WorkBillRepository extends JpaRepository<WorkBill, Integer> {

}
