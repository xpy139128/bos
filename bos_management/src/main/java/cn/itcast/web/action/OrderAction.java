package cn.itcast.web.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.domain.take_delivery.Order;
import cn.itcast.service.OrderService;
import cn.itcast.web.action.base.BaseAction;

@Namespace("/")
@ParentPackage("json-default")
@Scope("prototype")
@Controller
public class OrderAction extends BaseAction<Order>{

	@Autowired
	private OrderService orderService;
	
	@Action(value="order_findByOrderNum",results= {@Result(name="success",type="json")})
	public String order_findByOrderNum() {
		
		Order order = orderService.findByOrderNum(t.getOrderNum());
		Map<String,Object> map = new HashMap<String,Object>();
		if(order==null) {
			map.put("success", false);
			
		}else {
			map.put("success", true);
			map.put("orderData", order);
		}
		ActionContext.getContext().getValueStack().push(map);
		
		return SUCCESS;
	}
}
