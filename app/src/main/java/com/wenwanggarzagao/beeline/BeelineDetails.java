package com.wenwanggarzagao.beeline;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.ImageView;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

//import com.firebase.ui.auth.data.model.User;
import com.wenwanggarzagao.beeline.data.Beeline;
import com.wenwanggarzagao.beeline.data.DatabaseUtils;
import com.wenwanggarzagao.beeline.data.SavedUserData;
import com.wenwanggarzagao.beeline.data.User;
import com.wenwanggarzagao.beeline.io.ResponseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeelineDetails extends AppCompatActivity {

    private RecyclerView participantListView;
    private TextView locName;
    private TextView dateTime;
    private TextView detailsView;
    private EditText detailsEdit;
    private ImageButton editBtn;
    private ImageButton checkBtn;
    private ToggleButton join_leave_btn;

    private boolean hasJoined;
    private boolean originallyJoined;
    final Beeline selectedBeeline = DatabaseUtils.bl;

    private ImageView interestIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beeline_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        locName = findViewById(R.id.origin_dest_txt);
        String locationsTxt = selectedBeeline.from + " > " + selectedBeeline.to;
        locName.setText(locationsTxt);

        dateTime = findViewById(R.id.date_time_txt);
        String meetTxt = selectedBeeline.meet_date.toString() + " | " + selectedBeeline.meet_time.toString();
        dateTime.setText(meetTxt);

        //TODO: hardcoded for now
        detailsView = findViewById(R.id.addl_info_txt);
        if (selectedBeeline.details != null && !selectedBeeline.details.isEmpty()){
            detailsView.setText(selectedBeeline.details);
        }
        else {
            detailsView.setText("No additional info provided");
        }

        detailsEdit = findViewById(R.id.editDetails);
        detailsEdit.setVisibility(View.GONE);

        editBtn = findViewById(R.id.editButton);
        checkBtn = findViewById(R.id.checkButton);
        checkBtn.setVisibility(View.GONE);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBtn.setVisibility(View.GONE);
                detailsView.setVisibility(View.INVISIBLE);

                checkBtn.setVisibility(View.VISIBLE);
                detailsEdit.setVisibility(View.VISIBLE);
                detailsEdit.setText(detailsView.getText());
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBtn.setVisibility(View.VISIBLE);
                detailsView.setVisibility(View.VISIBLE);
                String newDeets = detailsEdit.getText().toString();
                detailsView.setText(newDeets);
                selectedBeeline.details = newDeets;
                DatabaseUtils.pushBeeline(selectedBeeline);

                checkBtn.setVisibility(View.GONE);
                detailsEdit.setVisibility(View.GONE);
            }
        });

        join_leave_btn = findViewById(R.id.join_leave_toggle);
        originallyJoined = hasJoined = searchBeelines();
        join_leave_btn.setChecked(hasJoined);
        join_leave_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!join_leave_btn.isChecked()) {
                    //join_leave_btn.setText("LEAVE");

                    System.out.println("currently joined, trying to leave");
                    selectedBeeline.leave(DatabaseUtils.me);
                    hasJoined = false;

                    System.out.println("left beeline");
                    //Informative Message
                    Toast.makeText(getApplicationContext(),"Left Beeline!",Toast.LENGTH_SHORT).show();

                } else {
                    //join_leave_btn.setText("JOIN");
                    System.out.println("currently not joined, trying to join");
                    selectedBeeline.join(DatabaseUtils.me);
                    hasJoined = true;

                    System.out.println("joined beeline");
                    DatabaseUtils.pushBeeline(selectedBeeline);
                    //Informative Message
                    Toast.makeText(getApplicationContext(),"Joined Beeline!",Toast.LENGTH_SHORT).show();
                }

                MainActivity.needsRefresh = originallyJoined != hasJoined;
                System.out.println("needs refresh? " + MainActivity.needsRefresh);
                join_leave_btn.setChecked(hasJoined);
            }
        });

        participantListView = findViewById(R.id.participant_list);
        participantListView.setLayoutManager(new LinearLayoutManager(this));
        //participantListView.setClickable(true);

        //participantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {Bundle bundle = new Bundle();
            //@Override
            //public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // DatabaseUtils.bl = (Beeline) beeList.getItemAtPosition(position);

                // Intent intent = new Intent(BeelineDetails.this, BeelineDetails.class);
                //based on item add info to intent

                // startActivity(intent);
            //}
       // });

        List<SavedUserData> participantList= new ArrayList<SavedUserData>();

        for (int i = 0; i < selectedBeeline.participants.size(); i++) {
            SavedUserData u = selectedBeeline.participants.get(i);
            participantList.add(u);
        }

        //ParticipantsAdaptor participantsAdaptor = new ParticipantsAdaptor(BeelineDetails.this, R.layout.participant_layout, participantList);
        //participantListView.setAdapter(participantsAdaptor);

        //registerForContextMenu(participantListView);


        ParticipantsAdaptor adapter = new ParticipantsAdaptor(participantList, new ClickListener() {
            @Override public void onPositionClicked(int position) {
                // callback performed on click
            }

            @Override public void onLongClicked(int position) {
                // callback performed on click
            }
        });

        participantListView.setAdapter(adapter);

    }


    /*@Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }*/

    public boolean searchBeelines() {

        // System.out.println(DatabaseUtils.me.saveData.myBeelines.size());
        System.out.println("Selected beeline id: " + selectedBeeline.id);
        try {
            for (Map.Entry<String, List<Long>> entry : DatabaseUtils.me.saveData.myBeelines.entrySet()) {

                System.out.println(entry.getValue());
                if (entry.getValue().contains(selectedBeeline.id)) {
                    System.out.println("FOUND; LEAVE");
                    return true;
                }
            }
        } catch (NullPointerException e) {
            System.err.println("No entry set");
        }

        System.out.println("DIDN'T FIND; JOIN");
        return false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
