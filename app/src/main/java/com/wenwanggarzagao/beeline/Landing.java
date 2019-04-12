package com.wenwanggarzagao.beeline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wenwanggarzagao.beeline.data.DatabaseUtils;

public class Landing extends AppCompatActivity {

    Button login;
    private static final String HARDCODED_USER = "preson@place.com";
    private static final String HARDCODED_PWD = "password123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseUtils.load();
        DatabaseUtils.signIn(this, HARDCODED_USER, HARDCODED_PWD);

        setContentView(R.layout.activity_landing);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }

        login = findViewById(R.id.button_login);
    }

    public void setUser() {

    }

    public void onClickHandler(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
