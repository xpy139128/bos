package cn.itcast.service.system;

import java.util.List;

import cn.itcast.domain.system.User;

public interface UserService {

	User findByUsername(String username);

	List<User> findAll();

	void save(User t, String[] roleids);

}
