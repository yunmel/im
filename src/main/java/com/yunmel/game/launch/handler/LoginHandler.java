package com.yunmel.game.launch.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunmel.game.cmd.Handler;
import com.yunmel.game.core.msg.Message;
import com.yunmel.game.msg.MsgId;
import com.yunmel.game.pb.LoginMsg;
import com.yunmel.game.pb.LoginMsg.LoginReq;

public class LoginHandler extends Handler
{
  private Logger LOG = LoggerFactory.getLogger(LoginHandler.class);
  @Override
  public void action()
  {
    LoginMsg.LoginRes loginRes =
        LoginMsg.LoginRes.newBuilder().setCode(1000).setDesc("登陆成功").build();
    LoginReq loginReq = (LoginReq) this.getData();
    LOG.info("accept username is [{}],password is [{}].",loginReq.getUseranme(),loginReq.getPassword());
    
    Message message = new Message();
    message.setMsgId(MsgId.LOGIN_RES);
    message.setData(loginRes.toByteArray());
    sendMsg(message);
  }

}
