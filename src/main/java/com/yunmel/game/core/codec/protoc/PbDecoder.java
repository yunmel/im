package com.yunmel.game.core.codec.protoc;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunmel.game.core.codec.ServerContext;

public class PbDecoder implements ProtocolDecoder {
	//属性名称
	private static final String CONTEXT = "context";
	protected Logger log = LoggerFactory.getLogger(PbDecoder.class);
	@Override
	public void decode(IoSession session, IoBuffer buff, ProtocolDecoderOutput out)
			throws Exception {
		//初始化服务器上下文
		ServerContext context = null;
    	if(session.getAttribute(CONTEXT) != null) context = (ServerContext)session.getAttribute(CONTEXT);
    	if(context == null){
    		context = new ServerContext();
    		session.setAttribute(CONTEXT, context);
    	}
    	//读取信息
    	IoBuffer io = context.getBuff();
    	//添加接收到的信息到服务器上下文
    	io.put(buff);
    	do{
    		io.flip();
    		if(io.remaining() < Integer.SIZE/Byte.SIZE){
    			//剩余字节长度不足，等待下次信息
    			io.compact();
    			break;
    		}
    		//获得信息长度
    		int length = io.getInt();
    		//log.error(session + ":message length " + length + " bytes, buff remain " + io.remaining() + " bytes");
    		if(io.remaining() < length){
    			io.rewind();
    			//剩余字节长度不足，等待下次信息
    			io.compact();
    			break;
    		}else{
    			//读取指定长度的字节数
    			byte[] bytes = new byte[length];
    			io.get(bytes);
    			//返回字节数组
    			out.write(bytes);
    			
    			if(io.remaining() == 0){
    				io.clear();
    				break;
    			}else{
    				io.compact();
    			}
    		}
    	}while(true);
	}

	@Override
	public void dispose(IoSession session) throws Exception {
		if(session.getAttribute(CONTEXT) != null) session.removeAttribute(CONTEXT);
	}

	@Override
	public void finishDecode(IoSession session, ProtocolDecoderOutput out)
			throws Exception {

	}
}