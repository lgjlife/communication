package com.communication.rocket.normal.producer;


import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import java.util.Date;

@Slf4j
public class RocketProducerDemo {

    public static void main(String args[]){

        RocketProducer producer = new RocketProducer();
        producer.start();

        try{
           for(int i = 0; i< 10; i++){

               producer.send(new Date().toString() + "---"+ i, new SendCallback() {
                   @Override
                   public void onSuccess(SendResult sendResult) {

                       log.info("发送数据成功");
                       log.info("sendResult = " + sendResult);
                   }

                   @Override
                   public void onException(Throwable throwable) {
                       log.error("发送数据失败");
                       throwable.printStackTrace();
                   }
               });
           }
        }
        catch(Exception ex){
            log.error(ex.getMessage());
        }
    }
}
