package cn.itcast.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.itcast.domain.take_delivery.Promotion;
@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer>{

	@Query("update Promotion set status = '2' where endDate<? and status = '1'")
	@Modifying
	void update(Date date);

}
