package cn.itcast.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.crm.domain.Customer;
import cn.itcast.domain.Area;
import cn.itcast.domain.constants.Constants;
import cn.itcast.domain.take_delivery.Order;
import cn.itcast.web.action.base.BaseAction;

@Namespace("/")
@ParentPackage("json-default")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order>{

	private String sendAreaInfo;
	
	private String recAreaInfo;

	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}

	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}
	
	@Action(value="order_add",results= {@Result(name="success",type="redirect",location="index.html")})
	public String addOrder() {
		Area sendArea = new Area();
		String[] send = sendAreaInfo.split("/");
		sendArea.setProvince(send[0]);
		sendArea.setCity(send[1]);
		sendArea.setDistrict(send[2]);
		
		Area recArea = new Area();
		String[] rec = recAreaInfo.split("/");
		recArea.setProvince(rec[0]);
		recArea.setCity(rec[1]);
		recArea.setDistrict(rec[2]);
			
		t.setRecArea(recArea);
		t.setSendArea(sendArea);
		
		Customer customer = (Customer) ServletActionContext.getRequest().getSession().getAttribute("customer");
		t.setCustomer_id(customer.getId());
		
		WebClient.create(Constants.BOS_MANAGEMENT_URL+"/services/orderService/order/save").type(MediaType.APPLICATION_JSON).post(t);
		
		return SUCCESS;
	}
	
}
