package cn.itcast.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import cn.itcast.domain.Area;
@Repository
public interface AreaRepository extends JpaRepository<Area, String>,JpaSpecificationExecutor<Area>{
	Area findByProvinceAndCityAndDistrict(String province,String city,String district);
}
