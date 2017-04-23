package com.yunmel.game.handler;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunmel.game.IServer;

public class PbHandler extends IoHandlerAdapter 
{
  private Logger LOG = LoggerFactory.getLogger(PbHandler.class);

  private IServer server;
  
  public PbHandler(IServer server)
  {
      this.server = server;
      LOG.debug("init server handler.");
  }
  
  @Override
  public void sessionCreated(IoSession session) throws Exception
  {
    server.sessionCreated(session);
  }

  @Override
  public void sessionOpened(IoSession session) throws Exception
  {
    server.sessionOpened(session);
  }

  @Override
  public void sessionClosed(IoSession session) throws Exception
  {
    server.sessionClosed(session);
  }

  @Override
  public void sessionIdle(IoSession session, IdleStatus status) throws Exception
  {
    server.sessionIdle(session, status);
  }

  @Override
  public void exceptionCaught(IoSession session, Throwable cause) throws Exception
  {
    server.exceptionCaught(session, cause);
  }

  @Override
  public void messageReceived(IoSession session, Object message) throws Exception
  {
    byte[] bytes = (byte[])message;
    IoBuffer buf = IoBuffer.allocate(bytes.length);
    buf.put(bytes);
    buf.flip();
    //处理命令
    server.doCommand(session, buf);
  }

  @Override
  public void messageSent(IoSession session, Object message) throws Exception
  {
    
  }

  @Override
  public void inputClosed(IoSession session) throws Exception
  {
    server.inputClosed(session);
  }
  
  
}
