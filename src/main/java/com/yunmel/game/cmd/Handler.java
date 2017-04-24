package com.yunmel.game.cmd;

import org.apache.mina.core.session.IoSession;

import com.yunmel.game.core.msg.Message;

public abstract class Handler implements ICommand
{
  // 参数
  private Object data;
  // 创建时间
  private long createTime;
  private IoSession session;

  protected void sendMsg(Message message)
  {
    session.write(message);
  }

  public Object getData()
  {
    return data;
  }

  public void setData(Object data)
  {
    this.data = data;
  }


  public long getCreateTime()
  {
    return createTime;
  }

  public void setCreateTime(long createTime)
  {
    this.createTime = createTime;
  }

  public IoSession getSession()
  {
    return session;
  }

  public void setSession(IoSession session)
  {
    this.session = session;
  }

  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

}
