
package org.droidtv.aiot.dev.test_for_messaging.example;

import com.alibaba.fastjson.JSON;
import org.droidtv.aiot.dev.test_for_messaging.mq.AESBase64Utils;
import org.droidtv.aiot.dev.test_for_messaging.mq.MqConsumer;


public class ConsumerExample {

    public static void main(String[] args) throws Exception {
        String url = "pulsar+ssl://mqe.tuyacn.com:7285/";
        String accessId = "5vpadpjdykpj93jngge7";
        String accessKey = "qkcqraaqx9wa88wppd9rtvcq8vhnja4t";

        MqConsumer mqConsumer = MqConsumer.build().serviceUrl(url).accessId(accessId).accessKey(accessKey)
                .maxRedeliverCount(3).messageListener(message -> {
                            System.out.println("---------------------------------------------------");
                            System.out.println("Message received:" + new String(message.getData()) + ",seq="
                                    + message.getSequenceId() + ",time=" + message.getPublishTime() + ",consumed time="
                                    + System.currentTimeMillis());
                            String jsonMessage = new String(message.getData());
                            MessageVO vo = JSON.parseObject(jsonMessage, MessageVO.class);
                            System.out.println("the real message data:" + AESBase64Utils.decrypt(vo.getData(), accessKey.substring(8, 24)));
                        }

                );
        mqConsumer.start();
    }
}
