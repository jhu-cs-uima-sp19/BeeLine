package com.wenwanggarzagao.beeline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = findViewById(R.id.toolbar_uprof);
        setSupportActionBar(toolbar);

        TextView nameTextView = (TextView) findViewById(R.id.uprof_name);
        nameTextView.setText("Joe Ansel Ensky");
        TextView descripTextView = (TextView) findViewById(R.id.uprof_descrip);
        descripTextView.setText("Description Here");
    }



}
