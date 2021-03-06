package org.droidtv.aiot.dev;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tuya.smart.android.demo.R;

import org.droidtv.aiot.dev.login.activity.AccountInputActivity;

public class Errorclass extends AppCompatActivity {
    Button try_again,new_user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error);
        try_again=findViewById(R.id.try_again);
        new_user=findViewById(R.id.new_user);
        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
finish();
            }
        });
        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AccountInputActivity.class));

            }
        });

    }
}
