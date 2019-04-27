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
import com.wenwanggarzagao.beeline.data.SavedUserData;
import com.wenwanggarzagao.beeline.data.User;
import com.wenwanggarzagao.beeline.io.ResponseHandler;

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

        final TextView nameTextView = (TextView) findViewById(R.id.uprof_name);
        ImageView userImageView = (ImageView) findViewById(R.id.uprof_image);
        final TextView descripTextView = (TextView) findViewById(R.id.uprof_descrip);

        String userUID = getIntent().getExtras().getString("userUID", null);
        if (userUID != null) {
            DatabaseUtils.queryUserData(userUID, new ResponseHandler<SavedUserData>() {
                @Override
                public void handle(SavedUserData savedUserData) {
                    nameTextView.setText(savedUserData.username);
                    descripTextView.setText(savedUserData.bio);
                }
            });
        } else {
            User me = DatabaseUtils.me;

            if (DatabaseUtils.isLoggedin() && me != null && me.getUsername() != null) {
                nameTextView.setText(me.getUsername());
            } else {
                System.out.println("name null");
            }

            if (DatabaseUtils.isLoggedin() && me != null) {
                Uri photoURI = me.fbuser.getPhotoUrl();
                if (photoURI != null) {
                    userImageView.setImageURI(photoURI);
                }
            }

            if (DatabaseUtils.isLoggedin() && me != null && me.saveData != null) {
                descripTextView.setText(me.saveData.bio);
            } else {
                //System.out.println(DatabaseUtils.isLoggedin() + " " + (me != null) + " " + (me.saveData != null));
                descripTextView.setText("Could not load user data");
            }

        }
    }


}
