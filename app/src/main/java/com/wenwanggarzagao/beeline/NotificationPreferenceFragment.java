package com.wenwanggarzagao.beeline;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v14.preference.MultiSelectListPreferenceDialogFragment;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.app.DialogFragment;
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
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment dialogFragment = null;
        if (preference instanceof MultiSelectListPreference) {
            System.out.println("starting?");
            dialogFragment = MultiSelectListPreferenceDialogFragmentCompat.newInstance(preference.getKey());
        }
        if (dialogFragment != null) {
            // The dialog was created (it was one of our custom Preferences), show the dialog for it
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getFragmentManager(), "android.support.v7.preference" +
                    ".PreferenceFragment.DIALOG");
        } else {
            // Dialog creation could not be handled here. Try with the super method.
            super.onDisplayPreferenceDialog(preference);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notifsSwitchPref = (SwitchPreference)  getPreferenceManager().findPreference("prefs_switch_notifs");
        notifsSwitchPref.setChecked(Storage.SHOW_NOTIFICATIONS.get(MainActivity.preferences));
        notifsSwitchPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            Storage storage = new Storage(getContext().getApplicationContext());
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
        alertTimesMSLPref.setPositiveButtonText("OK");
        alertTimesMSLPref.setNegativeButtonText("CANCEL");
        alertTimesMSLPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            Storage storage = new Storage(getContext().getApplicationContext());
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Toast.makeText(getContext().getApplicationContext(), o.toString(), Toast.LENGTH_SHORT);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
                Set<String> selections = sharedPreferences.getStringSet("prefs_multilist_alertTimes", new HashSet<String>());
                String[] selected = selections.toArray(new String[] {});
                String selectedlist = "";
                for (int i = 0; i < selected.length ; i++){
                    selectedlist += selected[i];
                    if (i != selected.length - 1) selectedlist += ",";
                }
                Toast.makeText(getContext().getApplicationContext(), "changed", Toast.LENGTH_SHORT);
                System.out.println(selectedlist);
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
}
