package cn.itcast.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.itcast.crm.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	List<Customer> findByFixedAreaIdIsNull();

	List<Customer> findByFixedAreaId(String fixedAreaId);

	@Query("update Customer set fixedAreaId = ? where id = ?")
	@Modifying
	void updateFixedAreaId(String fixedAreaid, int id);

	Customer findByTelephone(String telephone);

	@Query("update Customer set type = 1 where telephone = ?")
	@Modifying
	void updateCustomerTypeByTelephone(String telephone);

	Customer findByTelephoneAndPassword(String telephone, String password);

	@Query("select fixedAreaId from Customer where address = ?")
	String findFixedAreaIdByAddress(String address);

	

	
}
