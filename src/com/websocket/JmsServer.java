package com.websocket;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.QueueSender;
import javax.jms.Session;
import javax.jms.TextMessage;

import listener.WebListener;

import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import vo.User;

public class JmsServer implements MessageListener {

	private JmsTemplate jmsTemplate;

	public JmsServer(){
		
	}
	
	public JmsServer(ApplicationContext cxt) {
		jmsTemplate = (JmsTemplate) cxt.getBean("jmsTemplate");
	}

	/**
	 * JMS发送消息
	 */
	public void sendString(final String message) {
		System.out.println("====jms send===");
		jmsTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session
						.createTextMessage(message);
			}
		});
	}
	
	public void sendObject(final User u){
		jmsTemplate.send(new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createObjectMessage(u);
			}
		});
	}

	public void stop() {

	}

	/**
	 * JMS接收消息
	 */
	@Override
	public void onMessage(Message message) {
		/*
		 * try { Thread.sleep(160000);
		 * 
		 * } catch (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		if (message instanceof TextMessage) {
			try {
				String txt = ((TextMessage) message).getText();
				System.out.println("=========receiver:" + txt);
				WebListener.websocket.notifyClient(txt);
			} catch (JMSException e) {
				e.printStackTrace();
			} finally {
				
			}
		} else if(message instanceof ObjectMessage){
			try {
				User u = (User) ((ObjectMessage)message).getObject();
				System.out.println("===========receive:" +u.getUserid());
			} catch (JMSException e) {
				e.printStackTrace();
			}
			System.out.println("==========Object message=======");
		}
	}
}
