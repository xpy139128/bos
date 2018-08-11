package cn.itcast.mq;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Service;

@Service("smsConsumer")
public class SmsConsumer implements MessageListener {

	@Override
	public void onMessage(Message message) {

		MapMessage mapMessage = (MapMessage) message;

		String result = "000/xxx";

		try {
			if (result.startsWith("000")) {
				System.out.println("发送短信成功,手机号：" + mapMessage.getString("telephone") + "，验证码：" + mapMessage.getString("mString"));
			} else {
				throw new RuntimeException("短信发送失败, 信息码：" + result);
			}

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
