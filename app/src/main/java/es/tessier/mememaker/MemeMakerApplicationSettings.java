package es.tessier.mememaker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import es.tessier.mememaker.utils.StorageType;

/**
 * Created by Evan Anger on 8/13/14.
 */
public class MemeMakerApplicationSettings {

    static SharedPreferences mSharedPreferences;
    final static String KEY_STORAGE = "Storage";

    public MemeMakerApplicationSettings(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public static String getStoragePreference() {
        return StorageType.INTERNAL;
    }

    public static void setSharedPreference(String almacenamiento) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_STORAGE,getStoragePreference());
        editor.apply();


    }
}

