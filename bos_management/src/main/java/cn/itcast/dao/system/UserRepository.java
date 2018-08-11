package cn.itcast.dao.system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.itcast.domain.system.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	User findByUsername(String username);

}
