package cn.itcast.service.impl.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.dao.system.MenuRepository;
import cn.itcast.domain.system.Menu;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.MenuService;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuRepository menuRepository;
	
	@Override
	public List<Menu> findAll() {
	
		return menuRepository.findAll();
	}

	@Override
	public void save(Menu t) {
		//防止用户没有选父节点
		if(t.getParentMenu()==null||t.getParentMenu().getId()==0) {
			t.setParentMenu(null);
		}
		menuRepository.save(t);
		
	}

	@Override
	public List<Menu> findByUser(User user ) {
		
		if(user.getUsername().equals("admin")) {
			return menuRepository.findAll();
		}
		
		return menuRepository.findByUser(user.getId());
	}

}
