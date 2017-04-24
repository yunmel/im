package com.yunmel.game.launch.pool;

import java.util.concurrent.ConcurrentHashMap;

import com.yunmel.game.cmd.Handler;
import com.yunmel.game.exception.HanderException;
import com.yunmel.game.launch.handler.LoginHandler;
import com.yunmel.game.msg.MsgId;

public class MsgPool
{
  private static ConcurrentHashMap<Integer, Class<?>> HANDLER_POOL = new ConcurrentHashMap<>();

  public Handler getHandler(Integer msgId) throws InstantiationException, IllegalAccessException, HanderException
  {
    if (!HANDLER_POOL.containsKey(msgId))
    {
      throw new HanderException();
    }
    else
    {
      return (Handler) HANDLER_POOL.get(msgId).newInstance();
    }
  }

  public void register()
  {
    HANDLER_POOL.put(MsgId.LOGIN_REQ, LoginHandler.class);
  }

  public static MsgPool getIt()
  {
    return Holder._instanse;
  }

  private MsgPool()
  {
    register();
  }

  private static final class Holder
  {
    private static final MsgPool _instanse = new MsgPool();
  }

}
