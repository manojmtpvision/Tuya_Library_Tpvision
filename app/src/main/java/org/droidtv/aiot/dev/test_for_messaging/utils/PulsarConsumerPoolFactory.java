package org.droidtv.aiot.dev.test_for_messaging.utils;


import android.os.Build;


import org.droidtv.aiot.dev.test_for_messaging.mq.MqConfigs;
import org.droidtv.aiot.dev.test_for_messaging.mq.MqEnv;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.DeadLetterPolicy;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.SubscriptionType;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: bilahepan
 * @date: 2019/4/16 下午6:41
 */
public class PulsarConsumerPoolFactory {

    public static ConcurrentHashMap<Integer, Consumer> getConsumerPool() {
        return InitializingConsumerPool.pool;
    }


    private static class InitializingConsumerPool {
        private static final ConcurrentHashMap<Integer, Consumer> pool = new ConcurrentHashMap<Integer, Consumer>() {{
            for (int i = 1; i <= MqConfigs.CONSUMER_NUM; i++) {
                //
                int retryTimes = getRetryTimes();
                Consumer consumer = getConsumerInstance();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    while (Objects.isNull(consumer) && retryTimes > 0) {
                        retryTimes--;
                        consumer = getConsumerInstance();
                    }
                }
                //
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (Objects.isNull(consumer)) {
                        System.out.println("PulsarConsumerPoolFactory create consumer error! and have retry two times!");
                        continue;
                    }
                }
                put(i, consumer);
            }
        }};


        private static Consumer getConsumerInstance() {
            try {
                return PulsarClientFactory.getPulsarClient().newConsumer().topic(String.format("%s/out/%s", MqConfigs.accessId, MqEnv.PROD.getValue()))
                        .subscriptionName(String.format("%s-sub", MqConfigs.accessId)).subscriptionType(SubscriptionType.Failover)
                        .deadLetterPolicy(DeadLetterPolicy.builder().maxRedeliverCount(MqConfigs.MAX_REDELIVER_COUNT).build()).subscribe();
            } catch (PulsarClientException e) {
                System.err.println("PulsarConsumerPoolFactory init error! e=" + e);
                return null;
            }
        }


        public static int getRetryTimes() {
            return 2;
        }
    }

}