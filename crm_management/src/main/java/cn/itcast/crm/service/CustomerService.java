package cn.itcast.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cn.itcast.crm.domain.Customer;

public interface CustomerService {
	// 查询所有未关联客户列表
	@Path("/noassociationcustomers")
	@Produces({"application/xml","application/json"})
	@GET
	public List<Customer> findNoAssociationCustomers();

	// 已经关联到指定定区的客户列表
	@Path("/associationfixedareacustomers/{fixedareaid}")
	@Produces({ "application/xml", "application/json" })
	@GET
	public List<Customer> findHasAssociationFixedAreaCustomers(@PathParam("fixedareaid") String FixedAreaId);

	// 将客户关联到定区上 ， 将所有客户id 拼成字符串 1,2,3
	@Path("/associationcustomerstofixedarea")
	@PUT
	public void associationCustomersToFixedArea(@QueryParam("fixedareaid") String FixedAreaid,
			@QueryParam("customerid") String Customerids);
	
	@Path("/customer")
	@POST
	@Consumes({ "application/xml", "application/json" })
	public void register(Customer customer) ;
	
	@Path("/findCustomerByTelephone/{telephone}")
	@GET
	@Produces({"application/xml","application/json"})
	@Consumes({ "application/xml", "application/json" })
	public Customer findCustomerByTelephone(@PathParam("telephone") String telephone);
	
	@Path("/updateCustomerTypeByTelephone/{telephone}")
	@PUT
	@Produces({"application/xml","application/json"})
	public void updateCustomerTypeByTelephone(@PathParam("telephone") String telephone);
	
	@Path("/customer/login")
	@GET
	@Consumes({"application/xml","application/json"})
	public Customer login(@QueryParam("telephone") String telephone,@QueryParam("password") String password);
	
	@Path("/customer/findFixedAreaIdByAddress")
	@GET
	@Consumes({"application/xml","application/json"})
	public String findFixedAreaIdByAddress(@QueryParam("address") String address);
	
}
