package com.shaoqing.netty.protocol;

public enum MessageType {

	/**
	 * 0：业务请求消息
	 * 1：业务回复消息
	 * 2：即是请求也是业务消息
	 * 3：握手请求消息
	 * 4：握手回复消息
	 * 5：心跳请求消息
	 * 6：心跳应答消息
	 */
    SERVICE_REQ((byte) 0), SERVICE_RESP((byte) 1), ONE_WAY((byte) 2), LOGIN_REQ(
	    (byte) 3), LOGIN_RESP((byte) 4), HEARTBEAT_REQ((byte) 5), HEARTBEAT_RESP(
	    (byte) 6);

    private byte value;

    private MessageType(byte value) {
    	this.value = value;
    }

    public byte value() {
    	return this.value;
    }
}
