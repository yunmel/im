package com.yunmel.game.core.msg;

public class Message
{
  private int msgId;
  private byte[] data;
  public int getMsgId()
  {
    return msgId;
  }
  public void setMsgId(int msgId)
  {
    this.msgId = msgId;
  }
  public byte[] getData()
  {
    return data;
  }
  public void setData(byte[] data)
  {
    this.data = data;
  }
  
  
}
