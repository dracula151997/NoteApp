package com.project.semicolon.noteapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.lang.ref.WeakReference;

public class SharedHelper {


    public static void saveToPref(Context ctx, String key, String value) {
        WeakReference<Context> contextWeakReference = new WeakReference<Context>(ctx);
        if (contextWeakReference.get() != null) {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(contextWeakReference.get());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(key, value);
            editor.apply();
        }

    }

    public static String getFromPref(Context ctx, String key, String defaultValue) {
        WeakReference<Context> contextWeakReference = new WeakReference<>(ctx);
        if (contextWeakReference.get() != null) {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(contextWeakReference.get());
            return prefs.getString(key, defaultValue);
        }

        return defaultValue;
    }

    public static void removeFromPref(Context ctx, String key) {
        WeakReference<Context> contextWeakReference = new WeakReference<>(ctx);
        if (contextWeakReference.get() != null) {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(contextWeakReference.get());
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    public static boolean hasKey(Context ctx, String key) {
        WeakReference<Context> contextWeakReference = new WeakReference<>(ctx);
        if (contextWeakReference.get() != null) {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(contextWeakReference.get());
            return prefs.contains(key);
        }

        return false;
    }

    public static void clearAll(Context ctx) {
        WeakReference<Context> contextWeakReference = new WeakReference<>(ctx);
        if (contextWeakReference.get() != null) {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(contextWeakReference.get());
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
        }
    }
}
