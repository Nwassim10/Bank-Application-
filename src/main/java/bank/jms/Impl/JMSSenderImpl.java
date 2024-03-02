package bank.jms.Impl;

import bank.jms.JMSSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class JMSSenderImpl implements JMSSender {
	@Autowired
	JmsTemplate jmsTemplate;
	
	public void sendJMSMessage (String text){
		System.out.println("JMSSender: sending JMS message ="+text);
		jmsTemplate.convertAndSend("LargeDeposit", text);
	}



}
