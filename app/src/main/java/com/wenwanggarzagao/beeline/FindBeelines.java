package com.wenwanggarzagao.beeline;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;

import android.view.View;
import android.support.design.widget.NavigationView;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.wenwanggarzagao.beeline.data.Beeline;
import com.wenwanggarzagao.beeline.data.DatabaseUtils;
import com.wenwanggarzagao.beeline.data.Date;
import com.wenwanggarzagao.beeline.data.Location;
import com.wenwanggarzagao.beeline.data.Time;
import com.wenwanggarzagao.beeline.io.ResponseHandler;

import java.util.ArrayList;
import java.util.List;

public class FindBeelines extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    //bee.setToFrom(origin, destination);

    private ArrayList<Beeline> beelines;
    private ArrayAdapter<Beeline> beelineArrayAdapter;

    private ListView beeList;
    private Context context; // For adaptor
    private Cursor curse; // Database Cursor

    static final int REQUEST_CODE = 1;

    //TODO: hardcoded
    int zip = 21231; //hardcoded user location
    Location origin = new Location("9E33", "Baltimore", "MD", (short) 21218);
    Location destination = new Location("Fells Point","Baltimore", "MD", (short) 21231);
    Date birthday = new Date("12/31/2019");
    Time weird_hour = new Time("12:31");
    Beeline bee = Beeline.builder().setFromTo(origin, destination).setDate(birthday).setTime(weird_hour).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_beelines);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton addTrip = (FloatingActionButton) findViewById(R.id.fab);
        addTrip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindBeelines.this, CreateBeeline.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        beeList = (ListView) findViewById(R.id.beeline_list);

        // create ArrayList of courses from database
        updateArray();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            /*Intent launcher = getIntent();
            String date = launcher.getStringExtra("date");
            String time = launcher.getStringExtra("time");
            String info = launcher.getStringExtra("info");
            String start = launcher.getStringExtra("start");
            String destination = launcher.getStringExtra("destination");
            */
            updateArray();
        }
    }

    public void updateArray() {
        DatabaseUtils.queryBeelinesNear(zip, new ResponseHandler<List<Beeline>>() {

            @Override
            public void handle(List<Beeline> bls) {
                beelines = new ArrayList<Beeline>();
                System.out.println("got returned list of size " + bls.size());
                for (Beeline bl: bls) {
                    System.out.println(bl.toString());
                    beelines.add(bl);
                }

                // make array adapter to bind arraylist to listview with new custom item layout
                beelineArrayAdapter = new BeelineAdaptor(FindBeelines.this, R.layout.beeline_layout, beelines);
                beeList.setAdapter(beelineArrayAdapter);

                registerForContextMenu(beeList);
            }
        });
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
        ImageView navProfImgView = (ImageView) findViewById(R.id.nav_profImg);
        navProfImgView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindBeelines.this, UserProfile.class);
                startActivity(intent);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
            Intent intent = new Intent(FindBeelines.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_find) {
            Intent intent = new Intent(FindBeelines.this, FindBeelines.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityIfNeeded(intent, 0);
        } else if (id == R.id.nav_buzz) {

        } else if (id == R.id.nav_settings) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return false;
    }

}
