package com.websocket;

import java.util.logging.Logger;

import listener.WebListener;
import net.sf.json.JSONObject;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * websocket事件处理
 * @author
 *
 */
public class CustomerChannelHandler extends SimpleChannelHandler{

	private int userid;
	private ChannelHandlerContext ctx;
	private int time;
	
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		System.out.println("进来一个 id:" + ctx.getChannel().getId());
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		System.out.println("断开链接");
		WebListener.websocket.removerClient(this);
		super.channelDisconnected(ctx, e);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		e.getCause().printStackTrace();
		Logger.getAnonymousLogger().info(e.getCause().getMessage());
		ctx.getChannel().close();
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		this.ctx = ctx;
		Object msg = e.getMessage();
		if (msg != null && msg instanceof WebSocketFrame) {
			TextWebSocketFrame t = (TextWebSocketFrame) msg;
			String txt = t.getText();
			if (txt != null) {
				if(txt.startsWith("{\"userid")){
					JSONObject json = JSONObject.fromObject(txt);
					userid = json.optInt("userid");
					txt = json.optString("value");
					sendMessage(userid+":"+txt);
					WebListener.websocket.addClient(this);
				}else{
					
				}
			}
		} else if (e.getMessage() instanceof PingWebSocketFrame) {
			ctx.getChannel().write(
					new PongWebSocketFrame(((WebSocketFrame) msg)
							.getBinaryData()));
		} else if (e.getMessage() instanceof CloseWebSocketFrame) {
			e.getChannel().close();
		}
	}
	
	public void sendMessage(String msg){
		if(ctx == null){
			return;
		}
		ctx.getChannel().write(new TextWebSocketFrame(msg));
	}
	
}
