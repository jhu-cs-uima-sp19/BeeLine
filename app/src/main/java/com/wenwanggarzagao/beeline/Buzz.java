package com.wenwanggarzagao.beeline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.wenwanggarzagao.beeline.data.DatabaseUtils;
import com.wenwanggarzagao.beeline.data.Notification;
import com.wenwanggarzagao.beeline.io.ResponseHandler;

import java.util.ArrayList;
import java.util.List;

public class Buzz extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView notifications;
    private ArrayList<Notification> itemlist;
    private static String[] randtext = {
            "Hello world",
            "This is a notification",
            "Blagh",
            "Cheese sandwich",
            "TEAM BEEEEEEEEEEEEEEEE"
    };
    private ChildEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.itemlist = new ArrayList<>();
        setContentView(R.layout.activity_buzz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton addNotif = (FloatingActionButton) findViewById(R.id.testnotif);
        addNotif.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Notification n = new Notification().withText(randtext[((int) (Math.random() * randtext.length))]).randomId().setTimeNow();
                DatabaseUtils.pushNotification(n);
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        notifications = (ListView) findViewById(R.id.beeline_buzz);
        // populate notifications
        populateNotifications();

        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Notification n = dataSnapshot.getValue(Notification.class);
                itemlist.add(n);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Notification n = dataSnapshot.getValue(Notification.class);
                itemlist.remove(n);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        DatabaseUtils.database.getReference("notifications").child("user_" + DatabaseUtils.me.getId()).addChildEventListener(listener);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void populateNotifications() {
        final ProgressDialog dialog = ProgressDialog.show(this, "Loading...", "Please bee patient!");
        DatabaseUtils.queryNotifications(new ResponseHandler<ArrayList<Notification>>() {
            @Override
            public void handle(ArrayList<Notification> response) {
                // make array adapter to bind arraylist to listview with new custom item layout
                itemlist.addAll(response);
                NotifAdapter adapter = new NotifAdapter(Buzz.this, R.layout.notification_layout, itemlist);
                notifications.setAdapter(adapter);

                registerForContextMenu(notifications);
                dialog.cancel();
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
        getMenuInflater().inflate(R.menu.buzz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityIfNeeded(intent, 0);
        } else if (id == R.id.nav_find) {
            Intent intent = new Intent(this, FindBeelines.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_buzz) {
            // we're already here, dont do anything
        } else if (id == R.id.nav_settings) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
