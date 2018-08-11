package cn.itcast.crm.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.crm.service.CustomerService;
import cn.itcast.crm.service.impl.CustomerServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class CustomerTest extends CustomerServiceImpl {

	@Autowired
	private CustomerService customerService;
	
	@Test
	public void testFindNoAssociationCustomers() {
		System.out.println(customerService.findNoAssociationCustomers());
	}

	@Test
	public void testFindHasAssociationFixedAreaCustomers() {
		System.out.println(customerService.findHasAssociationFixedAreaCustomers("dq001"));
	}
	@Rollback(value=false)
	@Test
	public void testAssociationCustomersToFixedArea() {
		customerService.associationCustomersToFixedArea("dq001", "1,2");
	}

}
