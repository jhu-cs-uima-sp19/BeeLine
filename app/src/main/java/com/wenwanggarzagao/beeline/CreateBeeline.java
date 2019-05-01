package com.wenwanggarzagao.beeline;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wenwanggarzagao.beeline.data.Beeline;
import com.wenwanggarzagao.beeline.data.DatabaseUtils;
import com.wenwanggarzagao.beeline.data.Location;
import com.wenwanggarzagao.beeline.data.Date;
import com.wenwanggarzagao.beeline.data.Time;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateBeeline extends AppCompatActivity {
    private static Button add_btn;
    private Toolbar toolbar;
    private static EditText start_loc;
    private static EditText end_loc;
    private static EditText addl_info;
    private static EditText meeting_time;
    private static EditText meeting_date;
    private ListView beeList;
    Beeline.Builder bee = new Beeline.Builder();

    private boolean clicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_beeline);


        meeting_time = (EditText) findViewById(R.id.meeting_time);
        meeting_date = (EditText) findViewById(R.id.meeting_date);
        start_loc = (EditText) findViewById(R.id.start_loc);
        end_loc = (EditText) findViewById(R.id.end_loc);
        addl_info = (EditText) findViewById(R.id.addl_info);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        beeList = (ListView) findViewById(R.id.beeline_list);


        add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked)
                    return;

                clicked = true;
                try {
                    String meet_time = meeting_time.getText().toString();
                    String meet_date = meeting_date.getText().toString();
                    String start = start_loc.getText().toString();
                    String end = end_loc.getText().toString();
                    String deets = addl_info.getText().toString();

                    checkDate(meet_date);
                    checkTime(meet_time);
                    Address origin_address = checkLocation(start_loc);
                    Address dest_address = checkLocation(end_loc);
                    //geoLocate(start_loc);
                    //geoLocate(end_loc);


                    Intent intent = new Intent(CreateBeeline.this, FindBeelines.class);
                    /*intent.putExtra("time", meet_time);
                    intent.putExtra("date", meet_date);
                    intent.putExtra("info", addl_info.getText().toString());
                    intent.putExtra("start", start);
                    intent.putExtra("destination", destination);*/
                    //TODO: fix NumberFormatException

                    if (!start.isEmpty() && !end.isEmpty()) {
                        try {
                            Location origin = new Location(start, origin_address.getLocality(), origin_address.getAdminArea(), Integer.parseInt(origin_address.getPostalCode()));
                            Location destination = new Location(end, dest_address.getLocality(), dest_address.getAdminArea(), Integer.parseInt(dest_address.getPostalCode()));
                            Beeline new_bline = Beeline.builder().setDate(new Date(meet_date)).setFromTo(origin, destination).setTime(new Time(meet_time)).setDetails(deets).build();
                            new_bline.join(DatabaseUtils.me);
                            DatabaseUtils.pushBeeline(new_bline);
                            DatabaseUtils.attachNotificationForUserJoinListener(getApplicationContext(), new_bline, R.drawable.queen_bee);
                            Toast.makeText(getApplicationContext(), "You've created a Beeline! Yeah!!", Toast.LENGTH_SHORT).show();

                            Intent notifyIntent = new Intent(CreateBeeline.this, MyReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast
                                    (getApplicationContext(), NOTIFICATION_REMINDER_NIGHT, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
                            alarmManager.set(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis() +
                                    1000 * 60 * 60 * 24, pendingIntent);

                            setResult(RESULT_OK, intent);
                            finish();
                        } catch (NullPointerException e) {
                            clicked = false;
                            Toast toast = Toast.makeText(getApplicationContext(), "Location does not exist", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else {
                        clicked = false;
                        Toast toast = Toast.makeText(getApplicationContext(), "Location cannot be empty", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (IOException e) {
                    clicked = false;
                    Context context = getApplicationContext();
                    CharSequence text = e.getMessage();
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }


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

    private Address checkLocation(EditText ed_loc) {
        String loc = ed_loc.getText().toString();
        if (loc.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Location cannot be empty", Toast.LENGTH_SHORT);
            toast.show();
            // throw new RuntimeException("Location cannot be empty");
            return null;
        } else {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> list = null;
            try {
                list = geocoder.getFromLocationName(loc, 1);

            } catch (IOException e) {
                Log.w("OCR", "unable to geoLocate. IOException:" + e.getMessage());
            } try {
                if (list.size() > 0) {
                    Address address = list.get(0);
                    Log.w("OCR", "Geolocation: " + address.toString());
                    return address;
                }
            } catch (NullPointerException e){
                return null;
            }
        }
        return null;
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
