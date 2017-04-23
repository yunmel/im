package com.yunmel.game.core.codec.protoc;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunmel.game.core.msg.Message;

public class PbEncoder implements ProtocolEncoder
{
  private Logger LOG = LoggerFactory.getLogger(PbEncoder.class);
  @Override
  public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception
  {
    IoBuffer buf = IoBuffer.allocate(100);
    buf.setAutoExpand(true);
    buf.setAutoShrink(true);
    
    if (message instanceof Message)
    {
      Message msg = (Message) message;
      buf.putInt(msg.getData().length + 4);
      buf.putInt(msg.getMsgId());
      buf.put(msg.getData());
      buf.flip();
      out.write(buf);
      out.flush();
    }
    else 
    {
      LOG.error("unknow object type . {}" , message.getClass().getSimpleName());
    }
    
  }

  @Override
  public void dispose(IoSession session) throws Exception
  {
    
  }
  
}
