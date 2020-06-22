package com.communication.test;

import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.*;

public class Test {

    public static void main(String args[]) throws  Exception{


        int someport = 1234;
        String somelocalport = "";

        SocketChannel sc = SocketChannel.open( );

        Socket socket = sc.socket();

        sc.connect (new InetSocketAddress("somehost", someport));
//nio server
        ServerSocketChannel ssc = ServerSocketChannel.open( );
        ssc.bind (new InetSocketAddress (someport));

        SocketChannel socketChannel = ssc.accept();
//        socketChannel.read();
//        socketChannel.write()
//        socketChannel.configureBlocking()
//        socketChannel.close();
//        socketChannel.shutdownInput()
//        socketChannel.isOpen()

//udp
        DatagramChannel dc = DatagramChannel.open( );

//file
        RandomAccessFile raf = new RandomAccessFile ("somefile", "r");
        FileChannel fc = raf.getChannel( );



//aio client
        AsynchronousSocketChannel asc =  AsynchronousSocketChannel.open();
        asc.connect (new InetSocketAddress ("somehost", someport));

//aio server
        AsynchronousServerSocketChannel assc = AsynchronousServerSocketChannel.open();
        assc.bind(new InetSocketAddress(someport));

        AsynchronousSocketChannel asynchronousSocketChannel =  assc.accept().get();

    }
}
