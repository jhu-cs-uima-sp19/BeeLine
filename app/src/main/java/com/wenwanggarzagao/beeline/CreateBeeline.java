package com.wenwanggarzagao.beeline;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateBeeline extends AppCompatActivity {
    private static Button add_btn;
    private Toolbar toolbar;
    private static EditText meeting_time;
    private static EditText meeting_date;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_beeline);


        SharedPreferences mainPrefs = getSharedPreferences("MainActivityPrefs", 0);
        meeting_time = (EditText) findViewById(R.id.meeting_time);
        meeting_date = (EditText) findViewById(R.id.meeting_date);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateBeeline.this, FindBeelines.class);
                startActivity(intent);
            }
        });

        meeting_time.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String meet_time = meeting_time.getText().toString();

                    try {
                        checkTime(meet_time);
                    } catch (IOException e) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Invalid time format entered", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                return false;
            }
        });

        meeting_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Hide keyboard after button press
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(meeting_time.getWindowToken(), 0);
                }

            }
        });

        meeting_date.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String meet_date = meeting_date.getText().toString();

                    try {
                        checkDate(meet_date);
                    } catch (IOException e) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Invalid date format entered", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                return false;
            }
        });

        meeting_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Hide keyboard after button press
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(meeting_date.getWindowToken(), 0);
                }

            }
        });


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




    private void checkTime(String time) throws IOException{
        Pattern pattern;
        Matcher m;

        final String TIME_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

        pattern = Pattern.compile(TIME_PATTERN);

        m = pattern.matcher(time);

        if (!m.matches()) {
            throw new IOException("Invalid time");
        }
        else {
            String min_str = time.substring(time.length() - 2);
            String hr_str = time.substring(0, time.length() - 3);

            int hr = Integer.parseInt(hr_str);
            int min = Integer.parseInt(min_str);
        }
    }

    private void checkDate(String date) throws IOException {
        Pattern pattern;
        Matcher m;

        final String DATE_PATTERN = "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)";
        pattern = Pattern.compile(DATE_PATTERN);

        m = pattern.matcher(date);

        if (!m.matches()) {
            throw new IOException("Invalid date format");
        } else {
            m.reset();

            if (m.find()) {

                String month = m.group(1);
                String day = m.group(2);
                int year = Integer.parseInt(m.group(3));
                boolean val_date = true;

                if (day.equals("31") &&
                        (month.equals("4") || month.equals("6") || month.equals("9") ||
                                month.equals("11") || month.equals("04") || month.equals("06") ||
                                month.equals("09"))) {
                    val_date = false; // only 1,3,5,7,8,10,12 has 31 days
                } else if (month.equals("2") || month.equals("02")) {
                    //leap year
                    if (year % 4 == 0) {
                        if (day.equals("30") || day.equals("31")) {
                            val_date = false;
                        }
                    } else {
                        if (day.equals("29") || day.equals("30") || day.equals("31")) {
                            val_date = false;
                        }
                    }
                }
                if (!val_date) {
                    throw new IOException("Invalid date");
                }
            }

        }
    }
}
