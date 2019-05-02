package com.wenwanggarzagao.beeline.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.wenwanggarzagao.beeline.MainActivity;
import com.wenwanggarzagao.beeline.data.Beeline;

public class Storage {

    public Storage(Context ctx) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    // settings
    public static final Option<Boolean> SHOW_NOTIFICATIONS = new Option("shownotifs", true);
    public static final Option<String> NOTIFY_TIMES = new Option("notifytimes", "");
    public static final Option<String> NOTIFY_VALUES = new Option("notifytimes", "");

    public <T> T get(Option<T> option) {
        return get(option.getName(), option.getDefault());
    }

    public <T> T get(String key, T def) {
        return (T) Option.get(this, key);
    }

    public SharedPreferences getPrefs() {
        return this.prefs;
    }

    public SharedPreferences.Editor getEditor() {
        return this.editor;
    }

    public <T> Storage store(Option<T> option, T value) {
        if (this.editor == null)
            this.editor = prefs.edit();
        option.put(this, value);
        return this;
    }

    public void commit() {
        if (editor == null)
            throw new IllegalStateException("Tried to commit without editor.");
        editor.commit();
        editor = null;
    }
}
