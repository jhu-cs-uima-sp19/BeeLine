package com.wenwanggarzagao.beeline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setTitle(R.string.title_activity_profile);


        TextView nameTextView = (TextView) findViewById(R.id.uprof_name);
        nameTextView.setText("Joe Ansel Ensky");
        TextView descripTextView = (TextView) findViewById(R.id.uprof_descrip);
        descripTextView.setText("Description Here");
    }



}
