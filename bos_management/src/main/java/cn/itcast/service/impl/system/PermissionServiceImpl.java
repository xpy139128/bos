package cn.itcast.service.impl.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.dao.system.PermissionRepository;
import cn.itcast.domain.system.Permission;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.PermissionService;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionRepository permissionRepository;
	@Override
	public List<Permission> findByUser(User user) {
		// TODO Auto-generated method stub
		return permissionRepository.findByUser(user.getId());
	}
	@Override
	public List<Permission> findAll() {
		// TODO Auto-generated method stub
		return permissionRepository.findAll();
	}
	@Override
	public void save(Permission t) {
		// TODO Auto-generated method stub
		permissionRepository.save(t);
	}

}
