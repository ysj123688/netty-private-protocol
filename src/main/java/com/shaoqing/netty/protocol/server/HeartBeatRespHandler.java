package com.shaoqing.netty.protocol.server;

import com.shaoqing.netty.protocol.MessageType;
import com.shaoqing.netty.protocol.struct.Header;
import com.shaoqing.netty.protocol.struct.NettyMessage;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 心跳包应答
 * @author lsq 
 */
public class HeartBeatRespHandler extends ChannelHandlerAdapter{
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		NettyMessage message = (NettyMessage) msg;
		//返回心跳应答消息
		if(message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()){
			System.out.println("Receive client heart beat message : " + message);
			NettyMessage heartBeatResp = buildHeartBeat();
			System.out.println("Send heart beat response message to client : " + heartBeatResp);
			ctx.writeAndFlush(heartBeatResp);
		} else {
			ctx.fireChannelRead(msg);
		}
	}
	
	public NettyMessage buildHeartBeat(){
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.HEARTBEAT_RESP.value());
		message.setHeader(header);
		return message;
	}
}
