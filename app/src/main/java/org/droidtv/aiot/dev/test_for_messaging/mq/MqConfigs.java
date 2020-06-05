package org.droidtv.aiot.dev.test_for_messaging.mq;

/**
 * @author: bilahepan
 * @date: 2019/4/28 下午9:06
 */
public class MqConfigs {

    public final static String serviceUrl = "pulsar+ssl://mqe.tuyacn.com:7285/";
    public final static String accessId = "";
    public final static String accessKey = "";

    public final static Integer MAX_REDELIVER_COUNT = 3;
    public final static Integer CONSUMER_NUM= 3;
}