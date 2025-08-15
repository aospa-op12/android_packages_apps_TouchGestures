/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.touch.gestures;

import static android.content.Intent.ACTION_BOOT_COMPLETED;
import static android.content.Intent.ACTION_LOCKED_BOOT_COMPLETED;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.android.touch.gestures.TouchscreenGestureSettings;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";
    private static final String ONE_TIME_TUNABLE_RESTORE = "hardware_tunable_restored";
    private static final String KEY_MIGRATION_DONE = "migration_done_1";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final SharedPreferences dePrefs = TouchscreenGestureConstants.getDESharedPrefs(context);
        final boolean migrationDone = dePrefs.getBoolean(KEY_MIGRATION_DONE, false);

        if (intent.getAction().equals(ACTION_BOOT_COMPLETED) &&
                !dePrefs.getBoolean(KEY_MIGRATION_DONE, false)) {
            TouchscreenGestureSettings.MainSettingsFragment.migrateTouchscreenGestureStates(context);
            dePrefs.edit().putBoolean(KEY_MIGRATION_DONE, true).commit();
        }

        TouchscreenGestureSettings.MainSettingsFragment.restoreTouchscreenGestureStates(context);
    }

    private boolean hasRestoredTunable(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(ONE_TIME_TUNABLE_RESTORE, false);
    }

    private void setRestoredTunable(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(ONE_TIME_TUNABLE_RESTORE, true).apply();
    }
}
