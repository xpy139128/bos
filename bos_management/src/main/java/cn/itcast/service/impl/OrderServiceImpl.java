package cn.itcast.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.activemq.Message;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.dao.AreaRepository;
import cn.itcast.dao.FixedAreaRepository;
import cn.itcast.dao.OrderRepository;
import cn.itcast.dao.WorkBillRepository;
import cn.itcast.domain.Area;
import cn.itcast.domain.Courier;
import cn.itcast.domain.FixedArea;
import cn.itcast.domain.SubArea;
import cn.itcast.domain.constants.Constants;
import cn.itcast.domain.take_delivery.Order;
import cn.itcast.domain.take_delivery.WorkBill;
import cn.itcast.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private WorkBillRepository workBillRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;

	@Override
	public void saveOrder(Order order) {

		order.setOrderNum(UUID.randomUUID().toString()); // 设置订单号
		order.setOrderTime(new Date());// 设置下单时间
		order.setStatus("1"); // 待取件

		// 寄件人 省市区
		Area area = order.getSendArea();
		Area persistSendArea = areaRepository.findByProvinceAndCityAndDistrict(area.getProvince(), area.getCity(),
				area.getDistrict());
		// 收件人 省市区
		Area recArea = order.getRecArea();
		Area persistRecArea = areaRepository.findByProvinceAndCityAndDistrict(recArea.getProvince(), recArea.getCity(),
				recArea.getDistrict());

		order.setSendArea(persistSendArea);
		order.setRecArea(persistRecArea);
		// 自动分单逻辑，基于CRM地址库完全匹配，获取定区，匹配快递员
		String fixedAreaId = WebClient.create(Constants.CRM_MANAGEMENT_URL
				+ "/service/customerService/customer/findFixedAreaIdByAddress?address=" + order.getSendAddress())
				.accept(MediaType.APPLICATION_JSON).get(String.class);

		if (fixedAreaId != null) {
			FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
			Iterator<Courier> iterator = fixedArea.getCouriers().iterator();
			if (iterator.hasNext()) {
				Courier courier = iterator.next();
				if (courier != null) {
					// 自动分单成功
					saveOrder(order, courier);

					// 生成工单 发送短信
					return;
				}
			}
		}
		// 自动分单 逻辑， 通过省市区 ，查询分区关键字，匹配地址，基于分区实现自动分单
		for (SubArea subarea : persistSendArea.getSubareas()) {
			// 当前客户的下单地址 是否包含分区 关键字
			if (order.getSendAddress().contains(subarea.getKeyWords())) {
				// 找打分区 ,找到定区、找到快递员
				Iterator<Courier> iterator = subarea.getFixedArea().getCouriers().iterator();
				if (iterator.hasNext()) {
					Courier courier = iterator.next();
					if (courier != null) {
						// 自动分单成功
						saveOrder(order, courier);
						return;
					}
				}
			}
		}

		for (SubArea subArea : persistSendArea.getSubareas()) {
			// 当前客户的下单地址 是否包含分区 辅助关键字
			if (order.getSendAddress().contains(subArea.getAssistKeyWords())) {
				// 找打分区 ,找到定区、找到快递员
				Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
				if (iterator.hasNext()) {
					Courier courier = iterator.next();
					if (courier != null) {
						// 自动分单成功
						saveOrder(order, courier);
						return;
					}
				}

			}
		}
		// 进入人工分单
		order.setOrderType("2");
		orderRepository.save(order);

	}
	
	
	// 生成工单，发送短信
		private void generateWorkBill(final Order order) {
			// 生成工单
			WorkBill workBill = new WorkBill();
			workBill.setType("新");
			workBill.setPickstate("新单");
			workBill.setBuildtime(new Date());
			workBill.setRemark(order.getRemark());
			final String smsNumber = RandomStringUtils.randomNumeric(4);
			workBill.setSmsNumber(smsNumber); // 短信序号
			workBill.setOrder(order);
			workBill.setCourier(order.getCourier());
			workBillRepository.save(workBill);

			// 发送短信
			// 调用MQ服务，发送一条消息
			jmsTemplate.send("bos_sms", new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					MapMessage mapMessage = session.createMapMessage();
					mapMessage.setString("telephone", order.getCourier()
							.getTelephone());
					mapMessage.setString(
							"msg",
							"短信序号：" + smsNumber + ",取件地址：" + order.getSendAddress()
									+ ",联系人:" + order.getSendName() + ",手机:"
									+ order.getSendMobile() + "，快递员捎话："
									+ order.getSendMobileMsg());
					return (Message) mapMessage;
				}
			});

			// 修改工单状态
			workBill.setPickstate("已通知");
		}

	private void saveOrder(Order order, Courier courier) {
		order.setCourier(courier);
		order.setOrderType("1");
		orderRepository.save(order);
	}


	@Override
	public Order findByOrderNum(String orderNum) {
		
		return orderRepository.findByOrderNum(orderNum);
	}
}
