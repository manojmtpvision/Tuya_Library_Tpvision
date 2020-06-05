
package org.droidtv.aiot.dev.test_for_messaging.example;


import android.os.Build;


import org.droidtv.aiot.dev.test_for_messaging.mq.MessageHandlerTask;
import org.droidtv.aiot.dev.test_for_messaging.utils.PulsarConsumerPoolFactory;
import org.droidtv.aiot.dev.test_for_messaging.utils.ThreadPoolFactory;

import org.apache.pulsar.client.api.Consumer;

import java.util.function.BiConsumer;

/**
 *
 *@author: bilahepan
 *@date: 2019/4/28 下午9:27
 */
public class ConsumerExample2 {
    public static void main(String[] args) throws Exception {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            PulsarConsumerPoolFactory.getConsumerPool().forEach(new BiConsumer<Integer, Consumer>() {
                @Override
                public void accept(Integer consumerNum, Consumer consumer) {
                    ThreadPoolFactory.getCustomThreadPool().submit(new MessageHandlerTask(consumerNum, consumer));
                }
            });
        }
    }
}
