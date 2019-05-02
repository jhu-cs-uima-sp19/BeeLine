package com.wenwanggarzagao.beeline;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wenwanggarzagao.beeline.data.DatabaseUtils;

public class Landing extends AppCompatActivity {

    Button login;
    TextView signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseUtils.load();

        setContentView(R.layout.activity_landing);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }

        login = findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        signup = findViewById(R.id.text_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup.setTextColor(getResources().getColor(R.color.colorAccent));
                Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

    }
}
