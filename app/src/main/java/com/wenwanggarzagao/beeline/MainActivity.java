package com.wenwanggarzagao.beeline;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wenwanggarzagao.beeline.data.Beeline;
import com.wenwanggarzagao.beeline.SettingsActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.wenwanggarzagao.beeline.data.DatabaseUtils;
import com.wenwanggarzagao.beeline.data.Date;
import com.wenwanggarzagao.beeline.data.Location;
import com.wenwanggarzagao.beeline.data.Time;
import com.wenwanggarzagao.beeline.io.ResponseHandler;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String HARDCODED_USER = "person@place.com";
    private static final String HARDCODED_PWD = "password123";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static Boolean locationPermissionGranted = false;
    /*Location origin = new Location("9E33", "Baltimore", "MD", (short) 21218);
    Location destination = new Location("Fells Point","Baltimore", "MD", (short) 21231);

    Date birthday = new Date("12/31/2019");
    Time weird_hour = new Time("12:31");
    Beeline bee = Beeline.builder().setFromTo(origin, destination).setDate(birthday).setTime(weird_hour).build();*/

    //Beeline bee;

    private ArrayList<Beeline> beelines;
    private RecyclerView beeListView;
    private static final int VERTICAL_ITEM_SPACE = 48;

    private Context context; // For adaptor
    private Cursor curse; // Database Cursor

    // Unused as of now
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    public static boolean needsRefresh = false;

    @Override
    public void onRestart() {
        super.onRestart();
        System.out.println("======================ON RESTART");
        if (needsRefresh) {
            this.updateArray();
            needsRefresh = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("======================ON CREATE");
        setContentView(R.layout.activity_main);
        setTitle("My Beelines");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ////////////////////////////////////////
        /* Our Additions (Beeline) etc. */

        beeListView = (RecyclerView) findViewById(R.id.beeline_list);
        beeListView.setLayoutManager(new LinearLayoutManager(this));

        beeListView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));


        // create ArrayList of courses from database
        beelines = new ArrayList<Beeline>();
        getLocationPermission();

        /*beelineArrayAdapter = new BeelineAdaptor(this, R.layout.beeline_layout, beelines);
        beeList.setAdapter(beelineArrayAdapter);
        registerForContextMenu(beeList);

        beeList.setClickable(true);
        beeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {Bundle bundle = new Bundle();
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseUtils.bl = (Beeline) beeList.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, BeelineDetails.class);
                //based on item add info to intent

                startActivity(intent);
            }
        });*/

        System.out.println("got here mainactivity");
        updateArray();
        DatabaseUtils.attachNotificationListeners(getApplicationContext(), R.drawable.queen_bee);
    }
    private void getLocationPermission() {
        String [] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch(requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if(grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            locationPermissionGranted = false;
                            return;
                        }
                    }
                    locationPermissionGranted = true;
                    //initialize our map
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        TextView navUserName = (TextView) findViewById(R.id.nav_profName);
        navUserName.setText(DatabaseUtils.me.saveData.username);

        ImageView navProfImgView = (ImageView) findViewById(R.id.nav_profImg);
        navProfImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserProfile.class);
                intent.putExtra("userUID", DatabaseUtils.me.saveData.userId);
                startActivity(intent);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityIfNeeded(intent, 0);
        } else if (id == R.id.nav_find) {
            Intent intent = new Intent(MainActivity.this, FindBeelines.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_buzz) {
            Intent intent = new Intent(MainActivity.this, Buzz.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_settings) {
            DatabaseUtils.sendNotification(getApplicationContext(), "Title", "body", R.drawable.queen_bee);
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    /** Update the beelines listing */
    public void updateArray() {
        DatabaseUtils.queryMyBeelines(new ResponseHandler<List<Beeline>>() {

            @Override
            public void handle(List<Beeline> bls) {
                beelines = new ArrayList<Beeline>();
                System.out.println("querymybeelines got returned list of size " + bls.size());

                for (Beeline bl: bls) {
                    System.out.println("querymybeelines " + bl.toString());
                    beelines.add(bl);
                    DatabaseUtils.attachNotificationForUserJoinListener(getApplicationContext(), bl, R.drawable.queen_bee);
                }

                beelines.sort(new Comparator<Beeline>() {
                    @Override
                    public int compare(Beeline o1, Beeline o2) {
                        return o1.timeValue() - o2.timeValue();
                    }
                });
                // make array adapter to bind arraylist to listview with new custom item layout
                System.out.println("setting up beelinearrayadapter");
                /*beelineArrayAdapter = new BeelineAdaptor(MainActivity.this, R.layout.beeline_layout, beelines);
                beeList.setAdapter(beelineArrayAdapter);
                registerForContextMenu(beeList);*/
                BeelineAdaptor adapter = new BeelineAdaptor(beelines, new ClickListener() {
                    @Override public void onPositionClicked(int position) {
                        DatabaseUtils.bl = (Beeline) beelines.get(position);
                    }

                    @Override public void onLongClicked(int position) {
                        // callback performed on click
                    }
                });
                beeListView.setAdapter(adapter);
            }
        });
        //beelines.add(bee);
    }

    /** Update interest flower */
    /*public void updateInterest() {
        final ImageView interestImg = findViewById(R.id.interest_icon);

        interestImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interestImg.setImageResource(R.drawable.target_flowers);
            }
        });
    }*/

}
