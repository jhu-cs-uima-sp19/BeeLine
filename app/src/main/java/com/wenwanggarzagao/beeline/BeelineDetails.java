package com.wenwanggarzagao.beeline;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

//import com.firebase.ui.auth.data.model.User;
import com.wenwanggarzagao.beeline.data.Beeline;
import com.wenwanggarzagao.beeline.data.DatabaseUtils;
import com.wenwanggarzagao.beeline.data.User;

import java.util.ArrayList;
import java.util.List;

public class BeelineDetails extends AppCompatActivity {

    private ListView participantListView;
    private TextView locName;
    private TextView dateTime;
    private ToggleButton join_leave_btn;
    private ArrayAdapter<Beeline> ParticipantsAdapter;

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

        final Beeline selectedBeeline = DatabaseUtils.bl;
        locName = findViewById(R.id.origin_dest_txt);

        String locationsTxt = selectedBeeline.from + " > " + selectedBeeline.to;
        locName.setText(locationsTxt);

        dateTime = findViewById(R.id.date_time_txt);

        String meetTxt = selectedBeeline.meet_date.toString() + " | " + selectedBeeline.meet_time.toString();

        dateTime.setText(meetTxt);

        join_leave_btn = findViewById(R.id.join_leave_toggle);
        join_leave_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (join_leave_btn.isChecked()) {
                    selectedBeeline.join(DatabaseUtils.me);
                } else {
                    selectedBeeline.leave(DatabaseUtils.me);
                }
            }
        });

        participantListView = findViewById(R.id.participant_list);
        participantListView.setClickable(true);
        participantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {Bundle bundle = new Bundle();
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // DatabaseUtils.bl = (Beeline) beeList.getItemAtPosition(position);

                // Intent intent = new Intent(BeelineDetails.this, BeelineDetails.class);
                //based on item add info to intent

                // startActivity(intent);
            }
        });

        List<User> participantList= new ArrayList<User>();

        for (int i = 0; i < selectedBeeline.participants.size(); i++) {
            User u = selectedBeeline.participants.get(i);
            participantList.add(u);
        }

        ParticipantsAdaptor participantsAdaptor = new ParticipantsAdaptor(BeelineDetails.this, R.layout.participant_layout, participantList);
        participantListView.setAdapter(participantsAdaptor);

        registerForContextMenu(participantListView);

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
