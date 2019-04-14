package com.wenwanggarzagao.beeline;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {

    private static ImageButton cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        Toolbar toolbar = findViewById(R.id.toolbar_uprof);
        setSupportActionBar(toolbar);

        cancel = (ImageButton) findViewById(R.id.imageButton);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView nameTextView = (TextView) findViewById(R.id.uprof_name);
        nameTextView.setText("Joe Ansel Ensky");
        TextView descripTextView = (TextView) findViewById(R.id.uprof_descrip);
        descripTextView.setText("Description Here");
    }



}
