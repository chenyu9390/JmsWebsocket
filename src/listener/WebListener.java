package listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import vo.User;

import com.websocket.JmsServer;
import com.websocket.WebsocketServer;

/**
 * jms收到消息后发送给websocket
 * websocket和jms之间用指定的userid去,互相关联
 * @author 
 *
 */
public class WebListener implements ServletContextListener{

	public static WebsocketServer websocket = new WebsocketServer(9696);
	public static ApplicationContext cxt = new ClassPathXmlApplicationContext("applicationContext.xml");
	public static JmsServer jmsSender = new JmsServer(cxt);
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		websocket.stop();
		System.out.println("===server stop===");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		websocket.start();
		System.out.println("==server start===");
		jmsSender.sendString("{\"userid\":10,\"name\":\"chen\"}");
		User u = new User();
		u.setName("chen");
		u.setUserid(10);
		jmsSender.sendObject(u);
	}

}
