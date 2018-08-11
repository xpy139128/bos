package cn.itcast.service.impl.system;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import cn.itcast.dao.system.MenuRepository;
import cn.itcast.dao.system.PermissionRepository;
import cn.itcast.dao.system.RoleRepository;
import cn.itcast.domain.system.Menu;
import cn.itcast.domain.system.Permission;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.PermissionService;
import cn.itcast.service.system.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public List<Role> findByUser(User user) {
		// TODO Auto-generated method stub
		return roleRepository.findByUser(user.getId());
	}

	@Override
	public List<Role> findAll() {
		// TODO Auto-generated method stub
		return roleRepository.findAll();
	}

	@Override
	public void save(Role t, String[] permissionIds, String menuIds) {

		if (permissionIds != null) {
			for (String string : permissionIds) {
				int id = Integer.parseInt(string);
				Permission findOne = permissionRepository.findOne(id);
				t.getPermissions().add(findOne);
			}
		}
		if(StringUtils.isNotBlank(menuIds)) {
		String[] split = menuIds.split(",");
		for (String string : split) {
			int parseInt = Integer.parseInt(string);
			Menu findOne = menuRepository.findOne(parseInt);
			t.getMenus().add(findOne);

		}}
		roleRepository.save(t);

	}

}
