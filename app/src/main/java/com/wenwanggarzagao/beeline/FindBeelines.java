package com.wenwanggarzagao.beeline;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wenwanggarzagao.beeline.data.Beeline;
import com.wenwanggarzagao.beeline.data.DatabaseUtils;
import com.wenwanggarzagao.beeline.data.Updatable;
import com.wenwanggarzagao.beeline.io.ResponseHandler;
import com.wenwanggarzagao.beeline.CreateBeeline;
import java.io.IOException;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class FindBeelines extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener, Updatable {

    private ViewDialog dialog;

    private ArrayList<Beeline> beelines;
    private static final int VERTICAL_ITEM_SPACE = 48;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private RecyclerView beeListView;
    private Context context; // For adaptor
    private Cursor curse; // Database Cursor
    public static int currentZip;
    private int zip;
    static final int REQUEST_CODE = 1;
    public double latitude;
    public double longitude;
    public LocationManager locationManager;
    public ImageView interestFlower;

    public static boolean needsRefresh;

    @Override
    public void onRestart() {
        super.onRestart();
        System.out.println("======================ON RESTART FINDBEELINES");
        if (needsRefresh) {
            this.updateArray(currentZip);
            needsRefresh = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ViewDialog(this);

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

        beeListView = (RecyclerView) findViewById(R.id.beeline_list);
        beeListView.setLayoutManager(new LinearLayoutManager(this));

        beeListView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
        locationManager=(LocationManager) FindBeelines.this.getSystemService(Context.LOCATION_SERVICE);

        currentZip = getLocation();
        updateArray(currentZip);
        System.out.println(currentZip);




    }

    @Override
    public void update() {
        updateArray(currentZip);
    }

    public static boolean isLocationEnabled(Context context)
    {
        //...............
        return true;
    }

    protected int getLocation() {
        //LocationManager locationManager;
        Criteria criteria;
        if (MainActivity.locationPermissionGranted) {//criteria = new Criteria();
            //String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));

            try {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    try {
                        double longitude = ((Location) location).getLongitude();
                        double latitude = location.getLatitude();
                        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        zip = Integer.parseInt(addresses.get(0).getPostalCode());
                        return zip;
                    } catch (IOException e) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Not able to find your location", Toast.LENGTH_SHORT);
                        toast.show();
                        zip = 21218;
                        System.out.println("Not able to find your location");
                        return zip;
                    }
                } else {
                    //This is what you need:
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                    System.out.println("Not able to find location");
                    //zip = 21231;
                    //return zip;
                }
            }
                catch (SecurityException e) {
                    Toast toast = Toast.makeText(getApplicationContext(),"Enable location settings", Toast.LENGTH_SHORT);
                    toast.show();
                }



            }
            return zip == 0 ? 21218 : zip;
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
            updateArray(currentZip);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //Hey, a non null location! Sweet!

        //remove location callback:
        locationManager.removeUpdates(this);

        //open the map:
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            currentZip = Integer.parseInt(addresses.get(0).getPostalCode());
            if (currentZip == 0)
                currentZip = 21218;
        } catch (IOException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "no zip code update", Toast.LENGTH_SHORT);
            toast.show();
            currentZip = 21218;
        }
        //Toast.makeText(MainActivity.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
        //searchNearestPlace(voice2text);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void updateArray(int zip) {
        // TODO replace with user location
        dialog.showDialog();
        DatabaseUtils.queryBeelinesNear(zip, new ResponseHandler<List<Beeline>>() {

            @Override
            public void handle(List<Beeline> bls) {
                dialog.hideDialog();
                beelines = new ArrayList<Beeline>();
                System.out.println("got returned list of size " + bls.size());

                for (Beeline bl: bls) {
                    System.out.println(bl.toString());
                    beelines.add(bl);
                }

                beelines.sort(new Comparator<Beeline>() {
                    @Override
                    public int compare(Beeline o1, Beeline o2) {
                        return o1.timeValue() - o2.timeValue();
                    }
                });
                // make array adapter to bind arraylist to listview with new custom item layout
                /*beelineArrayAdapter = new BeelineAdaptor(beelines, listener);
                beeList.setAdapter(beelineArrayAdapter);

                registerForContextMenu(beeList);*/
                BeelineAdaptor adapter = new BeelineAdaptor(FindBeelines.this, beelines, new ClickListener() {
                    @Override
                    public void onPositionClicked(int position) {
                        DatabaseUtils.bl = (Beeline) beelines.get(position);
                        // callback performed on click
                    }

                    @Override public void onLongClicked(int position) {
                        // callback performed on click
                    }
                });
                beeListView.setAdapter(adapter);

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

        TextView navUserName = (TextView) findViewById(R.id.nav_profName);
        navUserName.setText(DatabaseUtils.me.saveData.username);

        ImageView navProfImgView = (ImageView) findViewById(R.id.nav_profImg);
        navProfImgView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindBeelines.this, UserProfile.class);
                intent.putExtra("userUID", DatabaseUtils.me.saveData.userId);
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
            finish();
        } else if (id == R.id.nav_find) {
            Intent intent = new Intent(FindBeelines.this, FindBeelines.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityIfNeeded(intent, 0);
        } else if (id == R.id.nav_buzz) {
            Intent intent = new Intent(FindBeelines.this, Buzz.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(FindBeelines.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return false;
    }

}
