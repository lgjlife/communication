package com.communication.rocket.normal.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;


@Slf4j
public class RoccketConsumer {

    private DefaultMQPushConsumer consumer;

    public void start(String name){

        consumer = new DefaultMQPushConsumer(name);

        consumer.setNamesrvAddr("127.0.0.1:9876");

        try{
            consumer.subscribe("TopicTest","*");

            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext ctx) {

                    list.forEach((me)->{
                        log.info("{}-接收到数据:{}",consumer.getConsumerGroup(),new String(me.getBody()));
                    });

                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });


            consumer.start();
        }
        catch(Exception ex){
            log.error(ex.getMessage());
        }

    }
}
