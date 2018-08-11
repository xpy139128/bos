package cn.itcast.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import cn.itcast.domain.take_delivery.Order;

public interface OrderService {

	@Consumes({"application/xml","application/json"})
	@POST
	@Path("/order/save")
	void saveOrder(Order order);

	Order findByOrderNum(String orderNum);
	
}
