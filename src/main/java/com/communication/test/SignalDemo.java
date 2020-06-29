package com.communication.test;

import lombok.extern.slf4j.Slf4j;
import sun.misc.Signal;
import sun.misc.SignalHandler;

@Slf4j
public class SignalDemo {

    public static void main(String args[]){

        SignalDemo signalDemo = new  SignalDemo();
        signalDemo.init();

        Runtime.getRuntime().addShutdownHook(new Thread(){

            @Override
            public void run() {
                log.info("执行退出操作");
                super.run();
            }
        });


        while (true);
    }

    public void init(){
        Signal signal = new Signal(getOSSignalType());
        Signal.handle(signal,new MySignalHandler());

    }

    private String getOSSignalType()
    {
        return System.getProperties().getProperty("os.name").
                toLowerCase().startsWith("win") ? "INT" : "USR2";
    }


    class MySignalHandler implements SignalHandler {

        @Override
        public void handle(Signal signal) {

            log.info("signal = " + signal);

        }
    }
}




