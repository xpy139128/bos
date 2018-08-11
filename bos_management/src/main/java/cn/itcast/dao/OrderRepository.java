package cn.itcast.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.domain.take_delivery.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

	Order findByOrderNum(String orderNum);
	
}
