package org.droidtv.aiot.dev.test_for_messaging.mq;

import com.alibaba.fastjson.JSON;
import org.droidtv.aiot.dev.test_for_messaging.example.MessageVO;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: bilahepan
 * @date: 2019/4/16 下午5:48
 */
public class MessageHandlerTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandlerTask.class);

    private Consumer consumer;
    private Integer consumerNum;

    public MessageHandlerTask(Integer consumerNum, Consumer consumer) {
        this.consumerNum = consumerNum;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        do {
            try {
                Message message = consumer.receive();
                Long start = System.currentTimeMillis();
                System.out.println("----------------consumerNum:" + consumerNum + ";consumerName:" + consumer.getConsumerName() + "--------------------");
                System.out.println("Message Received:" + new String(message.getData()) + ",seq="
                        + message.getSequenceId() + ",time=" + message.getPublishTime() + ",consumed time="
                        + System.currentTimeMillis());
                String jsonMessage = new String(message.getData());
                MessageVO vo = JSON.parseObject(jsonMessage, MessageVO.class);
                System.out.println("Decrypt Message Data:" + AESBase64Utils.decrypt(vo.getData(), MqConfigs.accessKey.substring(8, 24)));

                Long businessCost = System.currentTimeMillis() - start;
                Long ackStart = System.currentTimeMillis();
                consumer.acknowledge(message);
                System.out.println("Cost Time: messageKey=" + message.getKey() + "; business process message cost = " + businessCost + " ms; msg ack cost = " + (System.currentTimeMillis() - ackStart) + " ms");
            } catch (Throwable t) {
                System.out.println("error:" + t);
            }
        } while (true);
    }
}