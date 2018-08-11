package cn.itcast.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.service.PromotionService;

public class PromotionJob implements Job{
	
	@Autowired
	private PromotionService promotionService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		System.out.println("更新操作正在进行");
		promotionService.updateStatus(new Date());
		
	}

}
