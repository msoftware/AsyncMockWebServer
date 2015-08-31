package com.mosn.asyncmockwebserver.example.domain.core;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public abstract class Entity implements Parcelable {

    @NonNull
    public final Id id;

    protected Entity(@NonNull Id id) {
        this.id = id;
    }

    protected Entity(Parcel in) {
        id = in.readParcelable(Id.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(id, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
