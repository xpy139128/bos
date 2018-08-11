package cn.itcast.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import cn.itcast.domain.FixedArea;
@Repository
public interface FixedAreaRepository extends JpaRepository<FixedArea, String>,JpaSpecificationExecutor<FixedArea>{

}
