package com.communication.io;

import lombok.extern.slf4j.Slf4j;

import java.nio.channels.*;
import java.util.Set;

@Slf4j
public class SocketOption {

    public static void main(String args[]) throws Exception{
        printOps(SocketChannel.open());
       // printOps(ServerSocketChannel.open());
        printOps(AsynchronousSocketChannel.open());
        printOps(AsynchronousServerSocketChannel.open());
        printOps(DatagramChannel.open());
    }


    public static void printOps(NetworkChannel channel) {

        System.out.println(channel);

        Set<java.net.SocketOption<?>> opsSet  = channel.supportedOptions();

        opsSet.forEach((socketOption)->{

            try{
                String name = socketOption.name();
                Class type = socketOption.type();
                Object ops =  channel.getOption(socketOption);
                System.out.println(name+":  " + type + "  " + ops);
               // System.out.println();
               // System.out.println(ops);
            }
            catch(Exception ex){
                log.info("-----------------------------");
            }

        });

        System.out.println();
    }
}
