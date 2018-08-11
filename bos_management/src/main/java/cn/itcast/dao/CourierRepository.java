package cn.itcast.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.itcast.domain.Courier;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Integer>, JpaSpecificationExecutor<Courier> {
	
	@Query("update from Courier set deltag='1' where id = ?")
	@Modifying
	public void updateDeltag(int id);

	@Query("update from Courier set deltag='' where id = ?")
	@Modifying
	public void restoreDeltag(int id);
		
}
