package cn.itcast.service;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.domain.PageBean;
import cn.itcast.domain.take_delivery.Promotion;

public interface PromotionService {

	void save(Promotion t);

	Page<Promotion> findAll(Pageable pageable);
	
	
	@Produces({"application/xml","application/json"})
	@GET
	@Path("/pageQuery")
	PageBean<Promotion> findPageData(@QueryParam("page") int page,@QueryParam("rows") int rows);

	@Produces({"application/xml","application/json"})
	@GET
	@Path("/promotion/{id}")
	Promotion findById(@PathParam("id") Integer id);

	void updateStatus(Date date);
	
}
