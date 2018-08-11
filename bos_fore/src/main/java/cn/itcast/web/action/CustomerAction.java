package cn.itcast.web.action;

import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.databinding.source.mime.CustomExtensionRegistry;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.aspectj.apache.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import cn.itcast.crm.domain.Customer;
import cn.itcast.domain.constants.Constants;
import cn.itcast.utils.MailUtils;
import cn.itcast.web.action.base.BaseAction;

@Namespace("/")
@ParentPackage("json-default")
@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer> {

	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;

	@Action(value = "customer_sendMsg")
	public String sendCheckMsg() throws Exception {
		String random = RandomStringUtils.randomNumeric(4);
		ServletActionContext.getRequest().getSession().setAttribute(t.getTelephone(), random);
		final String mString = "尊敬的用户你好,本次的验证码为" + random + ",如有有问题请联系123456789";

		jmsTemplate.send("bos_sms", new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage message = (MapMessage) session.createMapMessage();
				message.setString("telephone", t.getTelephone());
				message.setString("mString", mString);
				return message;
			}
		});

		return NONE;

	}

	private String checkCode;

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Action(value = "customer_register", results = {
			@Result(name = "success", type = "redirect", location = "/signup-success.html"),
			@Result(name = "error", type = "redirect", location = "/signup.html") })
	public String register() throws Exception {
		// ServletActionContext.getResponse().setContentType("text/html,charSet=utf-8");
		String code = (String) ServletActionContext.getRequest().getSession().getAttribute(t.getTelephone());
		if (checkCode.equals(code) && checkCode != null) {

			WebClient.create("http://localhost:9002/crm_management/service/customerService/customer")
					.type(MediaType.APPLICATION_JSON).post(t);

			String activeCode = RandomStringUtils.randomNumeric(32);
			redisTemplate.opsForValue().set(t.getTelephone(), activeCode, 2, TimeUnit.HOURS);
			String content = "尊敬的客户你好!请于2小时之内进行邮箱的激活动作,如超过2小时未激活请重新注册,点击下方的链接进行激活:</br><a href = '"
					+ MailUtils.activeUrl + "?telephone=" + t.getTelephone() + "&activeCode=" + activeCode
					+ "'>速运快递激活邮箱链接</a>";
			MailUtils.sendMail("速运快递", content, t.getEmail(), activeCode);
			return SUCCESS;
		} else {

			// ServletActionContext.getResponse().getWriter().write("输入的验证码不正确");
			return ERROR;
		}
	}

	private String activeCode;

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	@Action(value = "customer_activeMail")
	public String activeEmail() throws Exception {
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		String code = redisTemplate.opsForValue().get(t.getTelephone());
		if (code == null || !code.equals(activeCode)) {
			ServletActionContext.getResponse().getWriter().write("激活码无效，请登录系统，重新绑定邮箱！");
		} else {
			Customer customer = WebClient
					.create("http://localhost:9002/crm_management/service/customerService/findCustomerByTelephone/"
							+ t.getTelephone())
					.accept(MediaType.APPLICATION_JSON).get(Customer.class);
			if (customer.getType() == null || customer.getType() != 1) {

				WebClient.create(
						"http://localhost:9002/crm_management/service/customerService/updateCustomerTypeByTelephone/"
								+ t.getTelephone())
						.accept(MediaType.APPLICATION_JSON).put(t);
				ServletActionContext.getResponse().getWriter().println("邮箱绑定成功！");

			} else {
				ServletActionContext.getResponse().getWriter().println("邮箱已经绑定过，无需重复绑定！");
			}
		}
		return NONE;

	}

	@Action(value = "customer_login", results = {
			@Result(name = "success", type = "redirect", location = "index.html#/myhome"),
			@Result(name = "login", type = "redirect", location = "login.html") })
	public String login() {
		Customer customer = WebClient.create(Constants.CRM_MANAGEMENT_URL
				+"/service/customerService/customer/login?telephone="+t.getTelephone()+"&password="+t.getPassword()).
				accept(MediaType.APPLICATION_JSON).get(Customer.class);
		if (customer == null) {
			return LOGIN;
		} else {
			ServletActionContext.getRequest().getSession().setAttribute("customer", customer);
			return SUCCESS;
		}
	}
}
