package com.wenwanggarzagao.beeline;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.wenwanggarzagao.beeline.data.Beeline;
import com.wenwanggarzagao.beeline.SettingsActivity;

import java.util.ArrayList;
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

    // TODO: no more hard coded stuff :(
    int zip = 21218;
    /*Location origin = new Location("9E33", "Baltimore", "MD", (short) 21218);
    Location destination = new Location("Fells Point","Baltimore", "MD", (short) 21231);

    Date birthday = new Date("12/31/2019");
    Time weird_hour = new Time("12:31");
    Beeline bee = Beeline.builder().setFromTo(origin, destination).setDate(birthday).setTime(weird_hour).build();*/

    //Beeline bee;

    private ArrayList<Beeline> beelines;
    private ArrayAdapter<Beeline> beelineArrayAdapter;

    private ListView beeList;
    private Context context; // For adaptor
    private Cursor curse; // Database Cursor

    // Unused as of now
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        beeList = (ListView) findViewById(R.id.beeline_list);

        //TODO: change interest image
        //updateInterest();

        // create ArrayList of courses from database
        beelines = new ArrayList<Beeline>();
        updateArray();


        beelineArrayAdapter = new BeelineAdaptor(this, R.layout.beeline_layout, beelines);
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
        });






        /*
        navItems = getResources().getStringArray(R.array.nav_pane_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, navItems));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(this);
        */

        if (!DatabaseUtils.isLoggedin()) {
            DatabaseUtils.signIn(this, HARDCODED_USER, HARDCODED_PWD, false, new Runnable() {
                @Override
                public void run() {
                    // create ArrayList of courses from database
                    updateArray();
                }
            });
        }

    }

    /*@Override
    protected void onResume() {
        super.onResume();
    }*/

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
        ImageView navProfImgView = (ImageView) findViewById(R.id.nav_profImg);
        navProfImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserProfile.class);
                startActivity(intent);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

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
                System.out.println("got returned list of size " + bls.size());

                for (Beeline bl: bls) {
                    System.out.println(bl.toString());
                    beelines.add(bl);
                }

                // make array adapter to bind arraylist to listview with new custom item layout
                beelineArrayAdapter = new BeelineAdaptor(MainActivity.this, R.layout.beeline_layout, beelines);
                beeList.setAdapter(beelineArrayAdapter);

                registerForContextMenu(beeList);
            }
        });
        //beelines.add(bee);
    }

    /** Update interest flower */
    public void updateInterest() {
        final ImageView interestImg = findViewById(R.id.interest_icon);

        interestImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interestImg.setImageResource(R.drawable.target_flowers);
            }
        });
    }

}
