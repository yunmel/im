package com.yunmel.code;

import java.io.IOException;

public class ProtocGenerator
{
  public static void main(String[] args)
  {
    //通过执行cmd命令调用protoc.exe程序  
    String protoFile = "person-entity.proto";//  
    String strCmd = "d:/dev/protobuf-master/src/protoc.exe -I=./proto --java_out=./src/main/java ./proto/"+ protoFile;  
    try {
        Runtime.getRuntime().exec(strCmd);
    } catch (IOException e) {
        e.printStackTrace();
    }
  }

}
