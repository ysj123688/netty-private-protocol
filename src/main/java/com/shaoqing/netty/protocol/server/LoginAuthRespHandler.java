package com.shaoqing.netty.protocol.server;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.shaoqing.netty.protocol.MessageType;
import com.shaoqing.netty.protocol.struct.Header;
import com.shaoqing.netty.protocol.struct.NettyMessage;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 握手和安全认证 
 */
public class LoginAuthRespHandler extends ChannelHandlerAdapter{
	
	//存储已经连接的客户端
	private Map<String, Boolean> nodeCheck = new HashMap<String, Boolean>();
	//白名单
	private String[] whiteList = {"127.0.0.1", "192.168.1.104"};
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		NettyMessage message = (NettyMessage) msg;
		//如果是握手请求，则处理
		if(message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.value()){
			String nodeIndex = ctx.channel().remoteAddress().toString();
			NettyMessage loginResp = null;
			//如果已经登录，则拒绝
			if(nodeCheck.containsKey(nodeIndex)){
				loginResp = buildResponse((byte)-1);
			} else {
				InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
				String ip = address.getAddress().getHostAddress();
				boolean isOK = false;
				for(String WIP : whiteList){
					if(WIP.equals(ip)){
						System.out.println(ip + "通过白名单");
						isOK = true;
						break;
					}
				}
				loginResp = isOK ? buildResponse((byte) 0) : buildResponse((byte) -1);
				if(isOK)
					nodeCheck.put(nodeIndex, true);
			}
			System.out.println("The login response is : " + loginResp + " body [" + loginResp.getBody() + "]");
			ctx.writeAndFlush(loginResp);
		} 
		//如果不是握手消息，透传
		else {
			ctx.fireChannelRead(msg);
		}
	}
	
	private NettyMessage buildResponse(byte result){
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_RESP.value());
		message.setHeader(header);
		message.setBody(result);
		return message;
	}
}
