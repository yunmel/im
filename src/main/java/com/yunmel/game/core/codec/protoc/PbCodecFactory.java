package com.yunmel.game.core.codec.protoc;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class PbCodecFactory implements ProtocolCodecFactory
{

  @Override
  public ProtocolEncoder getEncoder(IoSession session) throws Exception
  {
    return new PbEncoder();
  }

  @Override
  public ProtocolDecoder getDecoder(IoSession session) throws Exception
  {
    return new PbDecoder();
  }

}
