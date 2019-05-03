package com.wenwanggarzagao.beeline;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v14.preference.MultiSelectListPreferenceDialogFragment;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.MultiSelectListPreferenceDialogFragmentCompat;
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

import java.util.HashSet;
import java.util.Set;

public class NotificationPreferenceFragment extends PreferenceFragmentCompat {

    SwitchPreference notifsSwitchPref;
    MultiSelectListPreference alertTimesMSLPref;
    CheckBoxPreference cb30mins, cb1hr, cb1day;


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
        final Storage storage = new Storage(getContext().getApplicationContext());

        notifsSwitchPref = (SwitchPreference)  getPreferenceManager().findPreference("prefs_switch_notifs");
        notifsSwitchPref.setChecked(Storage.SHOW_NOTIFICATIONS.get(MainActivity.preferences));
        notifsSwitchPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

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


        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        String list = Storage.NOTIFY_SETTINGS.get(storage);
        if (list == null) list = "";
        cb30mins = (CheckBoxPreference) getPreferenceManager().findPreference("pref_30mins");
        cb30mins.setChecked(sharedPreferences.getBoolean("pref_30mins", false));
//        cb30mins.setChecked(list.contains("30"));

        cb1hr = (CheckBoxPreference) getPreferenceManager().findPreference("pref_1hr");
        cb1hr.setChecked(sharedPreferences.getBoolean("pref_1hr", false));
//        cb1hr.setChecked(list.contains("60"));

        cb1day = (CheckBoxPreference) getPreferenceManager().findPreference("pref_1day");
//        cb1day.setChecked(list.contains(60*24 + ""));
        cb1day.setChecked(sharedPreferences.getBoolean("pref_1day", false));

//        Preference.OnPreferenceChangeListener preferenceChangeListener = new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object o) {
//                Storage storage = new Storage(getContext().getApplicationContext());
//                storage.store(Storage.NOTIFY_SETTINGS, updateAlertList()).commit();
//                return true;
//            }
//        };
        Preference.OnPreferenceClickListener preferenceClickListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Storage storage = new Storage(getContext().getApplicationContext());
                storage.store(Storage.NOTIFY_SETTINGS, updateAlertList()).commit();
                return true;
            }
        };

//        cb30mins.setOnPreferenceChangeListener(preferenceChangeListener);
//        cb1hr.setOnPreferenceChangeListener(preferenceChangeListener);
//        cb1day.setOnPreferenceChangeListener(preferenceChangeListener);
        cb30mins.setOnPreferenceClickListener(preferenceClickListener);
        cb1hr.setOnPreferenceClickListener(preferenceClickListener);
        cb1day.setOnPreferenceClickListener(preferenceClickListener);

//        alertTimesMSLPref = (MultiSelectListPreference) getPreferenceManager().findPreference("prefs_multilist_alertTimes");
//        alertTimesMSLPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                System.out.println(alertTimesMSLPref.getValues().toString());
//                return false;
//            }
//        });
//        alertTimesMSLPref.getLayoutResource();
//        //alertTimesMSLPref.setDialogLayoutResource();
//        alertTimesMSLPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            Storage storage = new Storage(getContext().getApplicationContext());
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object o) {
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
////                Set<String> selections = sharedPreferences.getStringSet("prefs_multilist_alertTimes", new HashSet<String>());
//                Set<String> selections = alertTimesMSLPref.getValues();
//                String[] selected = selections.toArray(new String[] {});
//                String selectedlist = "";
//                for (int i = 0; i < selected.length; i++){
//                    selectedlist += selected[i];
//                    if (i != selected.length - 1) selectedlist += ",";
//                }
//                Toast.makeText(getContext().getApplicationContext(), "changed", Toast.LENGTH_SHORT);
//                System.out.println(selectedlist);
//                storage.store(Storage.NOTIFY_SETTINGS, selectedlist).commit();
//                return true;
//            }
//        });

        final View view = inflater.inflate(R.layout.content_settings, container, false);
        final ViewGroup innerContainer = (ViewGroup) view.findViewById(R.id.layout_settings);
        final View innerView = super.onCreateView(inflater, innerContainer, savedInstanceState);
        if (innerView != null) {
            innerContainer.addView(innerView);
        }
        return view;
    }

    private String updateAlertList() {
        String list = "";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        Boolean s30m = sharedPreferences.getBoolean("pref_30mins", false);
        Boolean s1h = sharedPreferences.getBoolean("pref_1hr", false);
        Boolean s1d = sharedPreferences.getBoolean("pref_1day", false);
        if (s30m) list += 30 + ",";
        if (s1h) list += 60 + ",";
        if (s1d) list += 60*24;
        if (list.endsWith(",")) list = list.substring(0, list.length() - 1);
        return list;
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
