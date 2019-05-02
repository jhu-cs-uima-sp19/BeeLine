package com.wenwanggarzagao.beeline;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.wenwanggarzagao.beeline.settings.Storage;

import java.util.Set;

public class NotificationPreferenceFragment extends PreferenceFragmentCompat {

    SwitchPreference notifsSwitchPref;
    MultiSelectListPreference alertTimesMSLPref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_settings, rootKey);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        notifsSwitchPref = (SwitchPreference)  getPreferenceManager().findPreference("prefs_switch_notifs");
        notifsSwitchPref.setChecked(Storage.SHOW_NOTIFICATIONS.get(MainActivity.preferences));
        notifsSwitchPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            Storage storage = new Storage(getContext());
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (notifsSwitchPref.isChecked()) {
                    Toast.makeText(getActivity(), "Unchecked", Toast.LENGTH_SHORT).show();
                    storage.store(Storage.SHOW_NOTIFICATIONS, false).commit();
                    notifsSwitchPref.setChecked(false);
                }
                else {
                    Toast.makeText(getActivity(), "Checked", Toast.LENGTH_SHORT).show();
                    storage.store(Storage.SHOW_NOTIFICATIONS, true).commit();
                    notifsSwitchPref.setChecked(true);
                }
                return false;
            }
        });

        alertTimesMSLPref = (MultiSelectListPreference) getPreferenceManager().findPreference("prefs_multilist_alertTimes");
        alertTimesMSLPref.setPositiveButtonText("Confirm");
        alertTimesMSLPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            Storage storage = new Storage(getContext());
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                CharSequence[] selected = alertTimesMSLPref.getEntries();
                Set<String> selections = sharedPreferences.getStringSet("prefs_multilist_alertTimes", null);
                //String[] selected = selections.toArray(new String[] {});
                String selectedlist = "";
                for (int i = 0; i < selected.length ; i++){
                    selectedlist += selected[i];
                    if (i != selected.length - 1) selectedlist += ",";
                    System.out.println("test" + i +" : " + selected[i]);
                }
                alertTimesMSLPref.setEntries(selected);
                Toast.makeText(getContext(), "changed", Toast.LENGTH_SHORT);
                storage.store(Storage.NOTIFY_SETTINGS, selectedlist).commit();
                return true;
            }
        });

        final View view = inflater.inflate(R.layout.content_settings, container, false);
        final ViewGroup innerContainer = (ViewGroup) view.findViewById(R.id.layout_settings);
        final View innerView = super.onCreateView(inflater, innerContainer, savedInstanceState);
        if (innerView != null) {
            innerContainer.addView(innerView);
        }
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
