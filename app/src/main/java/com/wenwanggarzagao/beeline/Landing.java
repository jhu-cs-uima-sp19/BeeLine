package com.wenwanggarzagao.beeline;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.wenwanggarzagao.beeline.data.DatabaseUtils;

public class Landing extends AppCompatActivity {

    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseUtils.load();

        setContentView(R.layout.activity_landing);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }

        login = findViewById(R.id.button_login);
    }

    public void setUser() {

    }

    public void onClickHandler(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
