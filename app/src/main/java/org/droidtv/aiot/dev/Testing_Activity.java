package org.droidtv.aiot.dev;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.tuya.smart.android.demo.R;

import org.droidtv.aiot.dev.test_for_messaging.mq.MqConsumer;

import org.apache.pulsar.client.api.Message;


public class Testing_Activity extends AppCompatActivity {
//    String accessId="5vpadpjdykpj93jngge7";
//    String accessKey="qkcqraaqx9wa88wppd9rtvcq8vhnja4t";

    String accessId="qkcqraaqx9wa88wppd9rtvcq8vhnja4t";
    String accessKey="5vpadpjdykpj93jngge7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_);
        MqConsumer mqConsumer = MqConsumer.build()
                .serviceUrl("pulsar+ssl://mqe.tuyacn.com:7285/")
                .accessId(accessId)
                .accessKey(accessKey)
                .maxRedeliverCount(3)
                .messageListener(new MqConsumer.IMessageListener() {
                    @Override
                    public void onMessageArrived(Message message) throws Exception {
                        //write your own message processing logic
                        String mess=message.getTopicName();
                        Log.d("topic",mess);
                    }
                });
        try {
            mqConsumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
