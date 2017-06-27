package com.shaoqing.netty.protocol.codec;

import java.util.Map;

import com.shaoqing.netty.protocol.struct.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Netty消息编码类
 * @author lsq 
 */
public class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {

	MarshallingEncoder marshallingEncoder;
	
	public NettyMessageEncoder() throws Exception {
		this.marshallingEncoder = new MarshallingEncoder();
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf sendBuf) throws Exception {
		//System.out.println("start encoding");
		if (msg == null || msg.getHeader() == null){
			throw new Exception("The encode message is null");
		}
		//写入数据，按照NettyMessage的格式
		sendBuf.writeInt((msg.getHeader().getCrcCode()));
		sendBuf.writeInt((msg.getHeader().getLength()));
		sendBuf.writeLong((msg.getHeader().getSessionID()));
		sendBuf.writeByte((msg.getHeader().getType()));
		sendBuf.writeByte((msg.getHeader().getPriority()));
		sendBuf.writeInt((msg.getHeader().getAttachment().size()));
		String key = null;
		byte[] keyArray = null;
		Object value = null;
		//循环吸入attachment
		for (Map.Entry<String, Object> param : msg.getHeader().getAttachment().entrySet()) {
		    key = param.getKey();
		    keyArray = key.getBytes("UTF-8");
		    sendBuf.writeInt(keyArray.length);
		    sendBuf.writeBytes(keyArray);
		    value = param.getValue();
		    marshallingEncoder.encode(value, sendBuf);
		}
		key = null;
		keyArray = null;
		value = null;
		//处理NettyMessage body部分
		if(msg.getBody() != null){
			marshallingEncoder.encode(msg.getBody(), sendBuf);
		} else {
			sendBuf.writeInt(0);
		}
		sendBuf.setInt(4, sendBuf.readableBytes() - 8);
	}

}
