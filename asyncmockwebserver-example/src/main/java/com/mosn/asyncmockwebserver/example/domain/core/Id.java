package com.mosn.asyncmockwebserver.example.domain.core;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public final class Id implements Parcelable {

    @NonNull
    public final String id;

    public Id(@NonNull String id) {
        this.id = id;
    }

    protected Id(Parcel in) {
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }

        String targetId = ((Id) obj).id;
        return this.id.equals(targetId);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Id> CREATOR = new Creator<Id>() {
        @Override
        public Id createFromParcel(Parcel in) {
            return new Id(in);
        }

        @Override
        public Id[] newArray(int size) {
            return new Id[size];
        }
    };
}
