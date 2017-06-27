package com.shaoqing.netty.protocol.codec;

import java.io.IOException;

import org.jboss.marshalling.ByteInput;

import io.netty.buffer.ByteBuf;

/**
 *  ChannelBufferByteInput类
 *  @author lsq
 */
public class ChannelBufferByteInput implements ByteInput{

	private final ByteBuf buffer;
	
	public ChannelBufferByteInput(ByteBuf buffer) {
		this.buffer = buffer;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public int read() throws IOException {
		if(buffer.isReadable()){
			return buffer.readByte() & 0xff;
		}
		return -1;
	}

	@Override
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int available = available();
		if (available == 0){
			return -1;
		}
		len = Math.min(available, len);
		buffer.readBytes(b, off, len);
		return len;
	}

	@Override
	public int available() throws IOException {
		return buffer.readableBytes();
	}

	@Override
	public long skip(long bytes) throws IOException {
		int readable = buffer.readableBytes();
		if (readable < bytes){
			bytes = readable;
		}
		buffer.readerIndex((int)(buffer.readerIndex() + bytes));
		return bytes;
	}

}
