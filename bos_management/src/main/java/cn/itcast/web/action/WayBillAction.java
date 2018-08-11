package cn.itcast.web.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.domain.take_delivery.Order;
import cn.itcast.domain.take_delivery.WayBill;
import cn.itcast.service.OrderService;
import cn.itcast.service.WayBillService;
import cn.itcast.web.action.base.BaseAction;

@Namespace("/")
@ParentPackage("json-default")
@Scope("prototype")
@Controller
public class WayBillAction extends BaseAction<WayBill>{
	
	@Autowired
	private WayBillService wayBillService;
	@Autowired
	private OrderService orderService;
	
	private static final Logger logger = Logger.getLogger(WayBillAction.class);
	
	@Action(value = "waybill_save",results= {@Result(name="success",type="json")})
	public String quickSave() {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			if(t.getOrder()!=null&&(t.getOrder().getId()==null||t.getOrder().getId()==0)) {
				t.setOrder(null);
			}

			
			wayBillService.save(t);
			map.put("success", true);
			map.put("msg", "保存运单成功,单号为"+t.getWayBillNum());
			logger.info("保存运单成功,单号为"+t.getWayBillNum());
		} catch (Exception e) {
			map.put("success", false);
			map.put("msg", "保存运单失败,单号为"+t.getWayBillNum());
			logger.error("保存运单失败,单号为"+t.getWayBillNum()+e);
		}
		
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
	@Action(value="waybill_query",results= {@Result(name="success",type="json")})
	public String queryWayBill() {
		
		Pageable pageable= new PageRequest(page-1,rows);
		
		Page<WayBill> page = wayBillService.findAll(t,pageable);
		
		pushPageDataToValueStack(page);
		
		return SUCCESS;
	}
	
	
	
	@Action(value="wayBill_findByWayBillNum",results= {@Result(name="success",type="json")})
	public String wayBill_findByWayBillNum() {
		WayBill wayBill =  wayBillService.findByWayBillNum(t.getWayBillNum());
		Map<String,Object> map = new HashMap<String,Object>();
		if(wayBill==null) {
			map.put("success", false);
		}else {
			map.put("success", true);
			map.put("wayBillData",wayBill );
		}
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
	
	
}
