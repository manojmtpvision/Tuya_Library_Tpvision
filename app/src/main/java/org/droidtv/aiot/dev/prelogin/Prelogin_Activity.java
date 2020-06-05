package org.droidtv.aiot.dev.prelogin;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tuya.smart.android.demo.R;

import org.droidtv.aiot.dev.login.activity.AccountInputActivity;
import org.droidtv.aiot.dev.login.activity.LoginActivity;

public class Prelogin_Activity extends AppCompatActivity {
    TextView sign_in;
    Button register;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prelogin);
        sign_in=findViewById(R.id.sign_in);
        register=findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountInputActivity.gotoAccountInputActivity(Prelogin_Activity.this, AccountInputActivity.MODE_REGISTER, 0);

            }
        });
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

    }
}
