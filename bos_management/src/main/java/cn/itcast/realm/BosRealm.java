package cn.itcast.realm;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.domain.system.Permission;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.PermissionService;
import cn.itcast.service.system.RoleService;
import cn.itcast.service.system.UserService;

//@Service
public class BosRealm extends AuthorizingRealm{

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PermissionService permissionService;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		//授权
		
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		
		List<Role> list1 = roleService.findByUser(user);
		for (Role role : list1) {
			authorizationInfo.addRole(role.getKeyword());
		}
		
		List<Permission> list2= permissionService.findByUser(user);
		for (Permission permission : list2) {
			authorizationInfo.addStringPermission(permission.getKeyword());
		}
		
		
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//认证
		UsernamePasswordToken uptoken = (UsernamePasswordToken) token;
		User user = userService.findByUsername(uptoken.getUsername());
		if(user==null) {
			return null;
		}else{
			return  new SimpleAuthenticationInfo(user,user.getPassword(),getName());
		}
		
	}

	
}
