package cn.itcast.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.domain.Courier;
import cn.itcast.service.CourierService; 

@Namespace("/")
@ParentPackage("json-default")
@Actions
@Controller
@Scope("prototype")
public class CourierAction extends ActionSupport implements ModelDriven<Courier>{

	private Courier courier = new Courier();
	@Override
	public Courier getModel() {
		return courier;
	}
	
	@Autowired
	private CourierService courierService;
	
	@Action(value="courier_save",results= {@Result(name="success",type="redirect",location="/pages/base/courier.html")})
	public String save() {
		courierService.save(courier);
		return SUCCESS;
	}
	
	private int page;
	private int rows;
	public void setPage(int page) {
		this.page = page;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	@Action(value="courier_query",results= {@Result(name="success",type="json")})
	public String findAll() {
		System.out.println(page);
		Pageable pageable = new PageRequest(page-1,rows);
		Specification specification = new Specification<Courier>() {

			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				if(StringUtils.isNotBlank(courier.getCourierNum())) {
					Predicate p1 = cb.like(root.get("courierNum").as(String.class), "%"+courier.getCourierNum()+"%");
					list.add(p1);
				}
				
				if(StringUtils.isNotBlank(courier.getCompany())) {
					Predicate p2 = cb.like(root.get("company").as(String.class), "%"+courier.getCompany()+"%");
					list.add(p2);
				}
				
				if(StringUtils.isNotBlank(courier.getType())) {
					Predicate p3 = cb.like(root.get("type").as(String.class), "%"+courier.getType()+"%");
					list.add(p3);
				}
				
				Join<Object, Object> join = root.join("standard", JoinType.INNER);
				if(courier.getStandard()!=null&&StringUtils.isNotBlank(courier.getStandard().getName())) {
					Predicate p4 = cb.like(join.get("name").as(String.class),"%"+courier.getStandard().getName()+"%" );
					list.add(p4);
				}
				
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		
		
		Page<Courier> page = courierService.findAll(specification,pageable);
		Map<String,Object> map = new HashMap<String,Object>(); 
		map.put("total", page.getTotalElements());
		map.put("rows", page.getContent());
		//System.out.println(map);
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
	private String ids;
	
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	@Action(value="courier_delete",results={@Result(name="success",location="/pages/base/courier.html",type="redirect")})
	public String deleteBeth() {
		
		String[] idArray = ids.split(",");
		//System.out.println(ids);
		
		
		courierService.updateDeltag(idArray);
		return SUCCESS;
	}
	@Action(value="courier_restore",results={@Result(name="success",location="/pages/base/courier.html",type="redirect")})
	public String restoreBeth() {
		
		String[] idArray = ids.split(",");
		//System.out.println(ids);
		
		
		courierService.restoreDeltag(idArray);
		return SUCCESS;
	}
	
	
}
