package com.yunmel.game.core.codec;

import org.apache.mina.core.buffer.IoBuffer;

public class ServerContext {

	//数据缓存
	private IoBuffer buff;
	
	public ServerContext(){
		buff = IoBuffer.allocate(256);
		buff.setAutoExpand(true);
		buff.setAutoShrink(true);
	}
	
	public void append(IoBuffer buff){
		this.buff.put(buff);
	}
	
	public IoBuffer getBuff(){
		return buff;
	}
	
}