package cn.itcast.service.system;

import java.util.List;

import cn.itcast.domain.system.Menu;
import cn.itcast.domain.system.User;

public interface MenuService {

	List<Menu> findAll();

	void save(Menu t);

	List<Menu> findByUser(User user);

}
