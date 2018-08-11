package cn.itcast.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.crm.dao.CustomerRepository;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	// 查询所有未关联客户列表
	@Override
	public List<Customer> findNoAssociationCustomers() {
		return customerRepository.findByFixedAreaIdIsNull();
	}

	// 已经关联到指定定区的客户列表
	@Override
	public List<Customer> findHasAssociationFixedAreaCustomers(String FixedAreaId) {
		return customerRepository.findByFixedAreaId(FixedAreaId);
	}

	// 将客户关联到定区上 ， 将所有客户id 拼成字符串 1,2,3
	@Override
	public void associationCustomersToFixedArea(String FixedAreaid, String Customerids) {
		String[] ids = Customerids.split(",");
		for(String idstr:ids) {
			Integer id = Integer.parseInt(idstr);
			customerRepository.updateFixedAreaId(FixedAreaid,id);
		}
	}

	@Override
	public void register(Customer customer) {
		customerRepository.save(customer);
		
	}

	@Override
	public Customer findCustomerByTelephone(String telephone) {
		
		return customerRepository.findByTelephone(telephone);
	}

	@Override
	public void updateCustomerTypeByTelephone(String telephone) {
		
		customerRepository.updateCustomerTypeByTelephone(telephone);
	}

	@Override
	public Customer login(String telephone, String password) {
		return customerRepository.findByTelephoneAndPassword(telephone,password);
		
	}

	@Override
	public String findFixedAreaIdByAddress(String address) {
		return customerRepository.findFixedAreaIdByAddress(address);
	}

}
