package cn.itcast.web.action.system;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.domain.system.User;
import cn.itcast.service.system.UserService;
import cn.itcast.web.action.base.BaseAction;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User> {

	@Autowired
	private UserService userService;
	
	@Action(value = "user_login", results = { @Result(name = "login", type = "redirect", location = "/login.html"),
			@Result(name = "success", type = "redirect", location = "/index.html") })
	public String login() {

		Subject subject = SecurityUtils.getSubject();

		AuthenticationToken token = new UsernamePasswordToken(t.getUsername(), t.getPassword());

		try {
			subject.login(token);
			return SUCCESS;
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return LOGIN;
		}
	}

	@Action(value = "user_logout", results = { @Result(name = "success", type = "redirect", location = "/login.html") })
	public String logout() {

		Subject subject = SecurityUtils.getSubject();

		subject.logout();
		return SUCCESS;

		
	}
	
	@Action(value = "user_list",results= {@Result(name="success",type="json")})
	public String list() {
		List<User> list = userService.findAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	private String[] roleids;
	
	public void setRoleids(String[] roleids) {
		this.roleids = roleids;
	}

	@Action(value="user_save",results= {@Result(name="success",type="redirect",location="/pages/system/userlist.html")})
	public String save() {
		userService.save(t,roleids);
		
		return SUCCESS;
	}
}
