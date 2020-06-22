package com.communication.netty.client;

import com.communication.netty.client.handler.*;
import com.communication.netty.exception.UnConnectServerException;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;


import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class NettyClient {

    private final String DEFAULT_HOST = "127.0.0.1";
    private final int DEFAULT_PORT = 8001;

    private String host = DEFAULT_HOST;
    private int port = DEFAULT_PORT;

    private ConcurrentHashMap<ServerDef,Channel> serverCache = new ConcurrentHashMap<>();

    //工作线程池,默认cpu * 2
    EventLoopGroup worker = new NioEventLoopGroup();

    public NettyClient() {

    }

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void init(){

        try{



            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    .channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer(){
                @Override
                protected void initChannel(Channel channel) throws Exception {

                    ChannelPipeline pipeline = channel.pipeline();

                    pipeline.addLast( new InboundHandlerA());

                    pipeline.addLast("2", new InboundHandlerB());
                    pipeline.addLast("3", new OutboundHandlerA());
                    pipeline.addLast("4", new OutboundHandlerB());
                   // pipeline.addLast("5", new InboundOutboundHandlerX());

                    //输入
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(1024,0,1,0,1));
                    pipeline.addLast(new StringDecoder());

                    pipeline.addLast("5",new ClientChannelInboundHandlerAdapter());
                    //输出
                    channel.pipeline().addLast("6",new ClientChannelOutboundHandlerAdapter());
                }
            });

            log.info("正在连接[{}:{}]...",host,port);
            ChannelFuture channelFuture =  bootstrap.connect(host,port).sync();
            Channel channel =  channelFuture.channel();

            serverCache.put(new ServerDef(host,port),channel);
            log.info("连接[{}:{}]成功！",host,port);


        }
        catch(Exception ex){
            ex.printStackTrace();
            log.error("连接[{}:{}]失败！",host,port);
        }



    }

    public Object send(String host ,int port ,Object data) throws UnConnectServerException{

        Channel channel = serverCache.get(new ServerDef(host, port));



        if(channel == null){
            throw  new UnConnectServerException(String.format("Unconnect to %s:%d",host,port));
        }

        log.info("发送数据....");
        try{
            ByteBuf message = Unpooled.buffer(((byte[])data).length+1);
            message.writeByte(5);
            message.writeBytes((byte[])data);

            channel.writeAndFlush(message).sync();
            log.info("数据发送完成....");

        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        return  channel.read();
    }

    class ServerDef{

        private String host;
        private int port;

        public ServerDef(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 37 * result + host.hashCode();
            result = 37 * result + port;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null ){
                return false;
            }
            if(!(obj instanceof ServerDef)){
                return false;
            }
            if( (this.host.equals(((ServerDef) obj).host)
                && (this.port == ((ServerDef) obj).port)))
            {
                return true;
            }
            return false;

        }
    }

    public static void main(String args[]){
        for(int i = 0; i< 10; i++){
            ServerDef serverDef = new NettyClient().new ServerDef("127.0.0.1"+i,8001+i);
            ServerDef serverDef1 = new NettyClient().new ServerDef("127.0.0.1"+i,8001+i);
            System.out.println(serverDef.equals(serverDef1));
        }
    }
}
