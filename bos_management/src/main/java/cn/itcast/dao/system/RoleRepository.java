package cn.itcast.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.itcast.domain.system.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

	@Query("from Role r inner join fetch r.users u where u.id= ?")
	List<Role> findByUser(int id);

}
