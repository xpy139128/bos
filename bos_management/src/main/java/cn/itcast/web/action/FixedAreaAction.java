package cn.itcast.web.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
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

import cn.itcast.crm.domain.Customer;
import cn.itcast.domain.FixedArea;
import cn.itcast.service.FixedAreaService;
import cn.itcast.web.action.base.BaseAction;

@Namespace("/")
@ParentPackage("json-default")
@Controller
@Scope("prototype")
@Actions
public class FixedAreaAction extends BaseAction<FixedArea> {

	@Autowired
	private FixedAreaService fixedAreaService;

	@Action(value = "fixed_save", results = {
			@Result(name = "success", type = "redirect", location = "/pages/base/fixed_area.html") })
	public String fixedAreaSave() {
		fixedAreaService.save(t);
		return SUCCESS;
	}

	@Action(value = "fixedArea_search", results = { @Result(name = "success", type = "json") })
	public String fixedAreaSearch() {
		Pageable pageable = new PageRequest(page - 1, rows);
		Specification<FixedArea> specification = new Specification<FixedArea>() {

			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (StringUtils.isNotBlank(t.getId())) {
					Predicate p1 = cb.like(root.get("id").as(String.class), "%" + t.getId() + "%");
					list.add(p1);
				}
				if (StringUtils.isNotBlank(t.getCompany())) {
					Predicate p2 = cb.like(root.get("company").as(String.class), "%" + t.getCompany() + "%");
					list.add(p2);
				}

				return cb.and(list.toArray(new Predicate[0]));
			}
		};

		Page<FixedArea> page = fixedAreaService.findAll(specification, pageable);
		//System.out.println(page);
		pushPageDataToValueStack(page);

		return SUCCESS;
	}
	
	@Action(value="fixedArea_findNoAssociationCustomers",results= {@Result(name="success",type="json")})
	public String findNoAssociationCustomers() {
		Collection<? extends Customer> collection = WebClient.create("http://localhost:9002/crm_management/service/customerService/noassociationcustomers")
		.accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}
	@Action(value="fixedArea_findHasAssociationFixedAreaCustomers",results= {@Result(name="success",type="json")})
	public String findHasAssociationFixedAreaCustomers() {
		Collection<? extends Customer> collection = WebClient.create("http://localhost:9002/crm_management/service/customerService/associationfixedareacustomers/"+t.getId())
		.accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}

}
