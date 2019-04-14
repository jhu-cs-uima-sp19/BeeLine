package com.wenwanggarzagao.beeline;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenwanggarzagao.beeline.data.DatabaseUtils;
import com.wenwanggarzagao.beeline.data.User;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = findViewById(R.id.toolbar_uprof);
        setSupportActionBar(toolbar);

        ImageButton cancel = (ImageButton) findViewById(R.id.imageButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        User me = DatabaseUtils.me;

        TextView nameTextView = (TextView) findViewById(R.id.uprof_name);
        if (DatabaseUtils.isLoggedin() && me != null && me.name != null) {
            nameTextView.setText(me.name);
        }
        else {
            System.out.println("name null");
        }

        ImageView userImageView = (ImageView) findViewById(R.id.uprof_image);
        if (DatabaseUtils.isLoggedin() && me != null) {
            Uri photoURI = me.fbuser.getPhotoUrl();
            if (photoURI != null) {
                userImageView.setImageURI(photoURI);
            }
        }

        TextView descripTextView = (TextView) findViewById(R.id.uprof_descrip);
        if (DatabaseUtils.isLoggedin() && me != null && me.saveData != null) {
            descripTextView.setText(me.saveData.getBio());
        }
        else {
            System.out.println(DatabaseUtils.isLoggedin() + " " + (me != null) + " " + (me.saveData != null));
            descripTextView.setText("Could not load user data");
        }

    }

}
