package com.wenwanggarzagao.beeline;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenwanggarzagao.beeline.data.DatabaseUtils;
import com.wenwanggarzagao.beeline.data.SavedUserData;
import com.wenwanggarzagao.beeline.data.User;
import com.wenwanggarzagao.beeline.io.ResponseHandler;

public class UserProfile extends AppCompatActivity {

    private String userUID;
    private SavedUserData sud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = findViewById(R.id.toolbar_uprof);
        setSupportActionBar(toolbar);

        final ImageButton cancel = (ImageButton) findViewById(R.id.exitButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final TextView nameTextView = (TextView) findViewById(R.id.uprof_name);
        ImageView userImageView = (ImageView) findViewById(R.id.uprof_image);
        final TextView descripTextView = (TextView) findViewById(R.id.uprof_descrip);

        final EditText editNameField = (EditText) findViewById(R.id.editName);
        final EditText editBioField = (EditText) findViewById(R.id.editBio);

        final ImageButton checkBtn = (ImageButton) findViewById(R.id.checkButton);
        final ImageButton editBtn = (ImageButton) findViewById(R.id.editButton);

        userImageView.setImageResource(R.drawable.bee);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userUID = extras.getString("userUID", null);
            if (userUID != null) {
                DatabaseUtils.queryUserData(userUID, new ResponseHandler<SavedUserData>() {
                    @Override
                    public void handle(SavedUserData savedUserData) {
                        sud = savedUserData;
                        nameTextView.setText(savedUserData.username);
                        descripTextView.setText(savedUserData.bio);
                        if (!userUID.equals(DatabaseUtils.me.getId())) { //if not my profile, cannot edit
                            editBtn.setVisibility(View.GONE);
                        }
                    }
                });
            } // invalid user data
            else {
                descripTextView.setText("Could not load user data");
                editBtn.setVisibility(View.GONE);
            }
        } else {
            descripTextView.setText("Could not load user data");
            editBtn.setVisibility(View.GONE);
        }


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //view -> edit mode
                editBtn.setVisibility(View.GONE);
                nameTextView.setVisibility(View.GONE);
                descripTextView.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);

                checkBtn.setVisibility(View.VISIBLE);
                editNameField.setVisibility(View.VISIBLE);
                editNameField.setText(nameTextView.getText());
                editBioField.setVisibility(View.VISIBLE);
                editBioField.setText(descripTextView.getText());
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // edit -> view mode
                //save data
                final String newName = editNameField.getText().toString();
                final String newBio = editBioField.getText().toString();

                if (sud != null) {
                    sud.username = newName;
                    sud.bio = newBio;
                    DatabaseUtils.pushUserData(sud);
                }
                DatabaseUtils.me.setSaveData(sud);

                //reset visibilities
                editBtn.setVisibility(View.VISIBLE);
                nameTextView.setVisibility(View.VISIBLE);
                nameTextView.setText(newName);
                descripTextView.setVisibility(View.VISIBLE);
                descripTextView.setText(newBio);
                cancel.setVisibility(View.VISIBLE);

                checkBtn.setVisibility(View.GONE);
                editNameField.setVisibility(View.GONE);
                editBioField.setVisibility(View.GONE);
            }
        });

    }


}
