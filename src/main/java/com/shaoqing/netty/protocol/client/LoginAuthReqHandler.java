package com.shaoqing.netty.protocol.client;

import com.shaoqing.netty.protocol.MessageType;
import com.shaoqing.netty.protocol.struct.Header;
import com.shaoqing.netty.protocol.struct.NettyMessage;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 握手认证客户端
 * @author lsq 
 */
public class LoginAuthReqHandler extends ChannelHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx){
		ctx.writeAndFlush(buildReq());
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		NettyMessage message = (NettyMessage) msg;
		//如果是握手认证回复消息则处理，需要判断认证是否成功
		if(message.getHeader()!=null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()){
			byte loginResult = (byte)message.getBody();
			if(loginResult == (byte) 0) {
				System.out.println("Login is ok : " + message);
				ctx.fireChannelRead(msg);
			} else {
				//握手失败，关闭链接
				ctx.close();
			}
		} 
		//如果不是，则透传
		else {
			ctx.fireChannelRead(msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	    ctx.fireExceptionCaught(cause);
	}
	
	private NettyMessage buildReq(){
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_REQ.value());
		message.setHeader(header);
		return message;
	}
}
