package com.example.budgetty.util;

import android.util.Log;
public class Logger {
    private static final String TAG = "Tag logger";
    public static void logIt(String message){ Log.d(TAG,message); } // custom logs
    public static void logError(String message){ Log.e(TAG,message); } // error logs
}
