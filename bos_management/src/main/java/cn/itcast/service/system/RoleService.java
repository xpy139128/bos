package cn.itcast.service.system;

import java.util.List;

import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;

public interface RoleService {

	List<Role> findByUser(User user);

	List<Role> findAll();

	void save(Role t, String[] permissionIds, String menuIds);

}
