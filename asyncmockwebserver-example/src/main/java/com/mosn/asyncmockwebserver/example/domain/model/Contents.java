package com.mosn.asyncmockwebserver.example.domain.model;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.mosn.asyncmockwebserver.example.domain.core.Entity;
import com.mosn.asyncmockwebserver.example.domain.core.Id;

import java.util.ArrayList;
import java.util.List;

public class Contents extends Entity {

    @NonNull public final String searchKeyword;

    @NonNull public final List<Content> contents = new ArrayList<>();

    protected Contents(@NonNull Id id) {
        super(id);
        searchKeyword = id.id;
    }

    protected Contents(Parcel in) {
        super(in);
        searchKeyword = in.readString();
        in.readList(contents, Content.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(searchKeyword);
        dest.writeList(contents);
    }

    public static Contents create(String searchKeyword) {
        return new Contents(new Id(searchKeyword));
    }

    public static final Creator<Contents> CREATOR = new Creator<Contents>() {
        @Override
        public Contents createFromParcel(Parcel in) {
            return new Contents(in);
        }

        @Override
        public Contents[] newArray(int size) {
            return new Contents[size];
        }
    };
}
