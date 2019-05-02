package com.wenwanggarzagao.beeline.settings;

import android.content.SharedPreferences;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * A very jank abstraction to make data saving easier to write...
 */
public class Option<T> {

    private static Map<String, Option> byName = new HashMap<>();
    public static Object get(Storage stg, String key) {
        return byName.get(key).get(stg);
    }

    public Option(String key, T defValue) {
        this.key = key;
        this.defValue = defValue;
        byName.put(this.key, this);
    }

    private T defValue;
    private String key;
    private Method mGet;
    private Method mPut;

    public T getDefault() {
        return this.defValue;
    }

    public String getName() {
        return this.key;
    }

    public T get(Storage storage) {
        if (mGet == null) {
            try {
                mGet = SharedPreferences.class.getDeclaredMethod("get" + keyFor(this.defValue.getClass()), String.class, classFor(defValue.getClass()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            return (T) mGet.invoke(storage.getPrefs(), this.key, defValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defValue;
        }
    }

    public void put(Storage storage, T value) {
        if (mPut == null) {
            try {
                mPut = SharedPreferences.Editor.class.getDeclaredMethod("put" + keyFor(this.defValue.getClass()), String.class, classFor(this.defValue.getClass()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (storage.getEditor() == null)
            throw new IllegalStateException("Access put from Storage, not from Option.");

        try {
            mPut.invoke(storage.getEditor(), this.key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String keyFor(Class clazz) {
        if (clazz == Integer.class) return "Int";
        if (clazz == Boolean.class) return "Boolean";
        if (clazz == String.class) return "String";
        if (clazz == Float.class) return "Float";
        if (clazz == Long.class) return "Long";

        String str = clazz.getSimpleName();
        return str.substring(0, str.indexOf('.'));
    }

    private static Class classFor(Class clazz) {
        if (clazz == Integer.class) return int.class;
        if (clazz == Boolean.class) return boolean.class;
        if (clazz == Float.class) return float.class;
        if (clazz == Long.class) return long.class;
        return clazz;
    }

}
