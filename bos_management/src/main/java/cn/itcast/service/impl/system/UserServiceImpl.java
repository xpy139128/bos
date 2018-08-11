package cn.itcast.service.impl.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.dao.system.RoleRepository;
import cn.itcast.dao.system.UserRepository;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Override
	public User findByUsername(String username) {
		
		return userRepository.findByUsername(username);
	}
	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}
	@Override
	public void save(User t, String[] roleids) {
		userRepository.save(t);
		
		if(roleids!=null) {
			for (String string : roleids) {
				Role findOne = roleRepository.findOne(Integer.parseInt(string));
				t.getRoles().add(findOne);
			}
		}
		
	}

}
