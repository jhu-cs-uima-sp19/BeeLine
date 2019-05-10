package com.wenwanggarzagao.beeline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.wenwanggarzagao.beeline.data.DatabaseUtils;

public class SettingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Toolbar and Nav drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_settings, new NotificationPreferenceFragment())
                .commit();

        //Prefs
        PreferenceManager.setDefaultValues(this, R.xml.pref_settings, false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals("display_text")) {
//            setTextVisible(sharedPreferences.getBoolean("display_text",true));
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
        getMenuInflater().inflate(R.menu.settings_menu, menu);

        TextView navUserName = (TextView) findViewById(R.id.nav_profName);
        navUserName.setText(DatabaseUtils.me.saveData.username);

        ImageView navProfImgView = (ImageView) findViewById(R.id.nav_profImg);
        navProfImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, UserProfile.class);
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
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_find) {
            Intent intent = new Intent(SettingsActivity.this, FindBeelines.class);
            startActivity(intent);
            finish();
        } /*else if (id == R.id.nav_buzz) {
            Intent intent = new Intent(SettingsActivity.this, Buzz.class);
            startActivity(intent);
            finish();
        } */else if (id == R.id.nav_settings) {
            Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityIfNeeded(intent, 0);
        } else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), Landing.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
