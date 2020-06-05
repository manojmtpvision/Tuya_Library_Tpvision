package org.droidtv.aiot.dev.test_for_messaging.utils;




import org.droidtv.aiot.dev.test_for_messaging.mq.MqAuthentication;
import org.droidtv.aiot.dev.test_for_messaging.mq.MqConfigs;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

/**
 * @author: bilahepan
 * @date: 2019/4/28 下午5:40
 */
public class PulsarClientFactory {

    public static PulsarClient getPulsarClient() {
        return PulsarClientHolder.CLIENT;
    }

    public static class PulsarClientHolder {
        private static final PulsarClient CLIENT = getClient();

        private static PulsarClient getClient() {
            try {
                return PulsarClient.builder().serviceUrl(MqConfigs.serviceUrl).allowTlsInsecureConnection(true)
                        .authentication(new MqAuthentication(MqConfigs.accessId, MqConfigs.accessKey)).build();
            } catch (PulsarClientException e) {
                System.err.println("PulsarClientFactory init error! e=" + e);
                return null;
            }
        }
    }
}