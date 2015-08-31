package com.mosn.asyncmockwebserver.example.ui;

import android.support.annotation.NonNull;
import android.view.View;

public interface Presenter {
    void bindView(@NonNull View view);
    void unbindView();
    boolean isBinding();
}
