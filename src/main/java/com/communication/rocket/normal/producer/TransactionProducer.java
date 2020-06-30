package com.communication.rocket.normal.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Date;
@Slf4j
public class TransactionProducer {

    public static void main(String[] args) throws MQClientException, InterruptedException {

        TransactionListener transactionListener = new TransactionListenerImpl();
        TransactionMQProducer producer = new TransactionMQProducer("Transaction");
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(new Thread(){

            int count = 0;

            @Override
            public void run() {

                count++;
                if(count%10 == 0){
                    log.info("时间＝{}s",count);
                }
                if(count== 60) count = 0;
            }
        },0,1,TimeUnit.SECONDS);

      //  producer.setExecutorService(executorService);
        producer.setTransactionListener(transactionListener);
        producer.start();

        try{
            Message msg = new Message("TopicTest", "TransactionTag", "KEY",
                    ("Hello RocketMQ ").getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.sendMessageInTransaction(msg, null);
        }
        catch(Exception ex){
            log.error(ex.getMessage());
        }

    }

    public static class TransactionListenerImpl implements TransactionListener {
        private AtomicInteger transactionIndex = new AtomicInteger(0);

        private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

        private int checkCount = 0;


        @Override
        public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {

            log.info("executeLocalTransaction 执行本地事务...");
            int value = transactionIndex.getAndIncrement();
            int status = value % 3;
            localTrans.put(msg.getTransactionId(), status);
            return LocalTransactionState.UNKNOW;
        }

        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt msg) {

            log.info("checkLocalTransaction　检查本地事务状态...");
            Integer status = localTrans.get(msg.getTransactionId());
            /*if (null != status) {
                switch (status) {
                    case 0:
                        return LocalTransactionState.UNKNOW;
                    case 1:
                        return LocalTransactionState.COMMIT_MESSAGE;
                    case 2:
                        return LocalTransactionState.ROLLBACK_MESSAGE;
                }
            }*/

            if(checkCount++ > 3){
                checkCount  = 0;

                log.info("提交消息");
                return LocalTransactionState.COMMIT_MESSAGE;
            }
            return LocalTransactionState.UNKNOW;
        }
    }
}


