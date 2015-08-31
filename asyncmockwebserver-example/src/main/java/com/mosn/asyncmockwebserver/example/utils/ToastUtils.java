package com.mosn.asyncmockwebserver.example.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class ToastUtils {

    private ToastUtils() {}

    public static void show(@Nullable Context context, String text) {
        if (context == null) return;
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(@Nullable Context context, String text) {
        if (context == null) return;
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
