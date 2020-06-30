package com.communication.rocket.normal.consumer;

public class RoccketConsumerDemo {

    public static void main(String args[]){

        RoccketConsumer  consumer1 = new RoccketConsumer();
        consumer1.start("name1");


        RoccketConsumer  consumer2 = new RoccketConsumer();
        consumer2.start("name1");

    }
}
