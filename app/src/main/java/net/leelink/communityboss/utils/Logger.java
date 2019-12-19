package net.leelink.communityboss.utils;

import android.util.Log;

public class Logger {
    public static final int LOGGER_ALL = 100;
    public static final int LOGGER_NONE = 0;
    public static final int VERBOSE = 5;
    public static final int DEBUG = 4;
    public static final int INFO = 3;
    public static final int WARN = 2;
    public static final int ERROR = 1;

    private static int mLogLevel = 6;

    public static int getmLogLevel() {
        return mLogLevel;
    }

    public void setLogLevel(int level) {
        mLogLevel = level;
    }

    public static void v(String tag, String msg) {
        if (mLogLevel >= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (mLogLevel >= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (mLogLevel >= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (mLogLevel >= WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (mLogLevel >= ERROR) {
            Log.e(tag, msg);
        }
    }
}
