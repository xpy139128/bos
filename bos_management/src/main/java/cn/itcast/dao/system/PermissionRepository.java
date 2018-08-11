package cn.itcast.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.itcast.domain.system.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Integer> {

	@Query("from Permission  p inner join fetch p.roles r inner join fetch r.users u where u.id = ?")
	List<Permission> findByUser(int id);

}
