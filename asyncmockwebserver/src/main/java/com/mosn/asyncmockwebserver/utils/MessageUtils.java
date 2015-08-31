package com.mosn.asyncmockwebserver.utils;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

public class MessageUtils {

    public static Message obtain(@NonNull Handler handler, Object obj, int what) {
        Message msg = handler.obtainMessage();
        msg.what = what;
        msg.obj = obj;
        return msg;
    }
}
