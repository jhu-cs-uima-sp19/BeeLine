package com.wenwanggarzagao.beeline;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wenwanggarzagao.beeline.data.DatabaseUtils;

public class BeelineDetails extends AppCompatActivity {


    private TextView locName;
    private TextView dateTime;


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

        String locationsTxt = DatabaseUtils.bl.from + " > " + DatabaseUtils.bl.to;
        locName.setText(locationsTxt);

        dateTime = findViewById(R.id.date_time_txt);

        String meetTxt = DatabaseUtils.bl.meet_date.toString() + " | " + DatabaseUtils.bl.meet_time.toString();

        dateTime.setText(meetTxt);

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
