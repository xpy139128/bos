package cn.itcast.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.domain.Standard;
import cn.itcast.service.StandardService;

@Namespace("/")
@ParentPackage("json-default")
@Actions
@Controller
@Scope("prototype")
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {

	private Standard standard = new Standard();

	@Override
	public Standard getModel() {
		return standard;
	}

	@Autowired
	private StandardService standardService;

	@Action(value = "standard_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/standard.html") })
	// 增加收派标准
	public String save() {
		standardService.save(standard);
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

	@Action(value = "standard_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		Pageable pageable = new PageRequest(page-1,rows); 
		Page<Standard> standardPage = standardService.findAll(pageable);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("totla", standardPage.getTotalElements());
		map.put("rows", standardPage.getContent());
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
	@Action(value="standard_findAll",results= {@Result(name="success",type="json")})
	public String findAll() {
		List<Standard> list = standardService.findAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}

}
