package cn.itcast.web.action.system;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.domain.system.Menu;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.MenuService;
import cn.itcast.web.action.base.BaseAction;

@Namespace("/")
@ParentPackage("json-default")
@Controller
@Scope("prototype")
public class MenuAction extends BaseAction<Menu>{

	@Autowired
	private MenuService menuService;
	
	@Action(value="menu_list",results= {@Result(name="success",type="json")})
	public String list() {
		List<Menu> list = menuService.findAll();
		ActionContext.getContext().getValueStack().push(list);
		
		return SUCCESS;
	}
	@Action(value="menu_save",results= {@Result(name="success",type="redirect",location="/pages/system/menu.html")})
	public String save() {
		
		menuService.save(t);
		
		return SUCCESS;
	}
	@Action(value= "menu_showMenu",results= {@Result(name="success",type="json")})
	public String show() {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		List<Menu> list = menuService.findByUser(user);
		ActionContext.getContext().getValueStack().push(list);
		
		return SUCCESS;
	}
	
}
