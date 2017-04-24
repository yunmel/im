package com.yunmel.code;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.yunmel.game.IServer;
import com.yunmel.game.core.codec.protoc.PbCodecFactory;
import com.yunmel.game.core.msg.Message;
import com.yunmel.game.handler.PbHandler;
import com.yunmel.game.msg.MsgId;
import com.yunmel.game.pb.LoginMsg;

public class Client implements IServer
{
  private NioSocketConnector connector = null;
  private Logger LOG = LoggerFactory.getLogger(Client.class);

  public void Init() throws InterruptedException
  {
    connector = new NioSocketConnector();
    connector.setConnectTimeoutMillis(30000);
    connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new PbCodecFactory()));
    connector.getFilterChain().addLast("threadPool",
        new ExecutorFilter(Executors.newCachedThreadPool()));

    int recsize = 512 * 1024;
    int sendsize = 1024 * 1024;
    SocketSessionConfig sc = connector.getSessionConfig();
    sc.setReceiveBufferSize(recsize);// 设置输入缓冲区的大小
    sc.setSendBufferSize(sendsize);// 设置输出缓冲区的大小
    // sc.setTcpNoDelay(true);// flush函数的调用 设置为非延迟发送，为true则不组装成大包发送，收到东西马上发出
    sc.setSoLinger(0);
    connector.setHandler(new PbHandler(this));
    ConnectFuture connect = connector.connect(new InetSocketAddress("127.0.0.1", 9002));
    connect.awaitUninterruptibly(60 * 1000);
    IoSession session = connect.getSession();

    Thread.sleep(1000);

    LoginMsg.LoginReq loginReq =
        LoginMsg.LoginReq.newBuilder().setUseranme("admin").setPassword("password").build();
    Message message = new Message();
    message.setMsgId(MsgId.LOGIN_REQ);
    message.setData(loginReq.toByteArray());
    
    for (int i = 0; i < 100; i++)
    {
      session.write(message);
      Thread.sleep(5);
    }
    

    Thread.sleep(5000);
  }

  @Override
  public void doCommand(IoSession iosession, IoBuffer buf)
  {
    int msgId = buf.getInt();
    byte[] data = new byte[buf.remaining()];
    buf.get(data);
    switch (msgId)
    {
      case MsgId.LOGIN_RES:
        try
        {
          LoginMsg.LoginRes loginRes = LoginMsg.LoginRes.parseFrom(data);
          LOG.info("client response success : [{}].", loginRes.toString());
          LOG.info("client response desc : {}" ,loginRes.getDescBytes().toStringUtf8());
        }
        catch (InvalidProtocolBufferException e)
        {
          LOG.error("client response error.", e);
        }
        break;

      default:
        break;
    }
  }

  public static void main(String[] args) throws InterruptedException
  {
    Client client = new Client();
    client.Init();
  }

  @Override
  public void sessionCreated(IoSession session) throws Exception
  {}

  @Override
  public void sessionOpened(IoSession session) throws Exception
  {}

  @Override
  public void sessionClosed(IoSession session) throws Exception
  {}

  @Override
  public void sessionIdle(IoSession session, IdleStatus status) throws Exception
  {}

  @Override
  public void exceptionCaught(IoSession session, Throwable cause) throws Exception
  {}

  @Override
  public void inputClosed(IoSession session) throws Exception
  {}

}
