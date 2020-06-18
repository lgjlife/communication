package com.communication.io;

import java.nio.channels.*;
import java.util.Set;

public class SocketOption {

    public static void main(String args[]) throws Exception{
        printOps(SocketChannel.open());
        printOps(ServerSocketChannel.open());
        printOps(AsynchronousSocketChannel.open());
        printOps(AsynchronousServerSocketChannel.open());
        printOps(DatagramChannel.open());
    }


    public static void printOps(NetworkChannel channel){

        System.out.println(channel);

        Set<java.net.SocketOption<?>> opsSet  = channel.supportedOptions();

        opsSet.forEach((socketOption)->{

            System.out.println(socketOption.name()+": " + socketOption.type());

        });

        System.out.println();
    }
}
