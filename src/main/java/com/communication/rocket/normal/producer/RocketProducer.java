package com.communication.rocket.normal.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;


@Slf4j
public class RocketProducer {

    private DefaultMQProducer producer;

    public void  start(){


        try{
            producer = new  DefaultMQProducer("myname");
            producer = new  DefaultMQProducer("myname");
            producer.setNamesrvAddr("127.0.0.1:9876");
            producer.start();
            log.info("Rocket startup success..");
        }
        catch(Exception ex){
            log.error("Rocket startup fail..");
            log.error(ex.getMessage());
        }


    }

    public void send(String data , SendCallback sendCallback)throws Exception{

        Message msg = new Message("TopicTest" /* Topic */,
                "TagA" /* Tag */,
                data.getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
        );

        producer.send(msg,sendCallback);

    }
}
