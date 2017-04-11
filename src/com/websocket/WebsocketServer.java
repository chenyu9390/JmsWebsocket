package com.websocket;

import java.net.InetSocketAddress;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

import net.sf.json.JSONObject;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * ≥ı ºªØwebsocket
 * @author
 *
 */
public class WebsocketServer {
	public final int port;
	public ServerBootstrap bootstrap;
	public static List<CustomerChannelHandler> clientList = new ArrayList<CustomerChannelHandler>();
	
	public WebsocketServer(int port){
		this.port = port;
	}
	
	public void removerClient(CustomerChannelHandler ctx){
		synchronized (clientList) {
			clientList.remove(ctx);
		}
	}
	
	public void addClient(CustomerChannelHandler ctx){
		synchronized (clientList) {
			boolean isExit = false;
			int index = 0;
			for(CustomerChannelHandler client : clientList){
				if(client.getUserid()>0&&client.getUserid() == ctx.getUserid()){
					isExit = true;
					clientList.set(index, ctx);
				}
				index ++;
			}
			if(!isExit){
				clientList.add(ctx);
			}
		}
	}
	
	public void start(){
		ChannelFactory factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		bootstrap = new ServerBootstrap(factory);
		bootstrap.setPipelineFactory(new PieplelineFactory());
		bootstrap.bind(new InetSocketAddress(port));
	}
	
	public void notifyClient(String text){
		JSONObject json = JSONObject.fromObject(text);
		int userid = json.optInt("userid");
		if(WebsocketServer.clientList != null && !WebsocketServer.clientList.isEmpty()){
			for(CustomerChannelHandler channel : WebsocketServer.clientList){
				if(channel.getUserid() == userid){
					channel.getCtx().getChannel().write(new TextWebSocketFrame(json.optString("name")));
				}
			}
		}
	}
	
	/**
	 * server stop
	 */
	public void stop(){
		if(bootstrap != null){
			bootstrap.shutdown();
		}
		clientList.clear();
		clientList = null;
	}
}
