package com.yunmel.game;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public interface IServer
{
  public void doCommand(IoSession iosession, IoBuffer buf);
  
  public void sessionCreated(IoSession session) throws Exception;

  public void sessionOpened(IoSession session) throws Exception;

  public void sessionClosed(IoSession session) throws Exception;

  public void sessionIdle(IoSession session, IdleStatus status) throws Exception;

  public void exceptionCaught(IoSession session, Throwable cause) throws Exception;

  public void inputClosed(IoSession session) throws Exception;
}
