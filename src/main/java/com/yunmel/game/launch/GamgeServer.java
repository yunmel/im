package com.yunmel.game.launch;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.yunmel.game.IServer;
import com.yunmel.game.core.codec.protoc.PbCodecFactory;
import com.yunmel.game.core.msg.Message;
import com.yunmel.game.handler.PbHandler;
import com.yunmel.game.msg.MsgId;
import com.yunmel.game.pb.LoginMsg;

public class GamgeServer implements IServer
{

  private Logger LOG = LoggerFactory.getLogger(GamgeServer.class);
  // 服务器数据监听端口
  private int port;
  private NioSocketAcceptor acceptor;

  @Override
  public void doCommand(IoSession iosession, IoBuffer buf)
  {
    int msgId = buf.getInt();
    byte[] data = new byte[buf.remaining()];
    buf.get(data);
    switch (msgId)
    {
      case MsgId.LOGIN_REQ:
        try
        {
          LoginMsg.LoginReq loginReq = LoginMsg.LoginReq.parseFrom(data);
          LOG.info("server request success : [{}].", loginReq.toString());

          LoginMsg.LoginRes loginRes =
              LoginMsg.LoginRes.newBuilder().setCode(1000).setDesc("登陆成功").build();
          
          Message message = new Message();
          message.setMsgId(MsgId.LOGIN_RES);
          message.setData(loginRes.toByteArray());
          iosession.write(message);
        }
        catch (InvalidProtocolBufferException e)
        {
          LOG.error("server request error.", e);
        }
        break;

      default:
        break;
    }
  }

  public static void main(String[] args)
  {
    GamgeServer gs = new GamgeServer();
    gs.init(9002);
  }

  public void init(int tport)
  {
    this.port = tport;
    acceptor = new NioSocketAcceptor();
    DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
    chain.addLast("codec", new ProtocolCodecFilter(new PbCodecFactory()));
    chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
    int recsize = 5120;
    int sendsize = 20480;
    int timeout = 30;
    acceptor.setReuseAddress(true);// 设置每一个非主监听连接的端口可以重用
    SocketSessionConfig sc = acceptor.getSessionConfig();
    sc.setReuseAddress(true);// 设置每一个非主监听连接的端口可以重用
    sc.setReceiveBufferSize(recsize);// 设置输入缓冲区的大小
    sc.setSendBufferSize(sendsize);// 设置输出缓冲区的大小
    sc.setTcpNoDelay(true);// flush函数的调用 设置为非延迟发送，为true则不组装成大包发送，收到东西马上发出
    sc.setSoLinger(0);
    sc.setIdleTime(IdleStatus.READER_IDLE, timeout);
    acceptor.setHandler(new PbHandler(this));
    try
    {
      // 绑定服务器数据监听端口，启动服务器
      acceptor.bind(new InetSocketAddress(port));
      LOG.info("Mina Server Start At Port " + port);
    }
    catch (IOException e)
    {
      LOG.error("Mina Server Port " + port + "Already Use:" + e.getMessage());
      System.exit(1);
    }
  }

  @Override
  public void sessionCreated(IoSession session) throws Exception
  {

  }

  @Override
  public void sessionOpened(IoSession session) throws Exception
  {

  }

  @Override
  public void sessionClosed(IoSession session) throws Exception
  {

  }

  @Override
  public void sessionIdle(IoSession session, IdleStatus status) throws Exception
  {

  }

  @Override
  public void exceptionCaught(IoSession session, Throwable cause) throws Exception
  {

  }

  @Override
  public void inputClosed(IoSession session) throws Exception
  {

  }

}
