package cn.itcast.service.system;

import java.util.List;

import cn.itcast.domain.system.Permission;
import cn.itcast.domain.system.User;

public interface PermissionService {

	List<Permission> findByUser(User user);

	List<Permission> findAll();

	void save(Permission t);

}
