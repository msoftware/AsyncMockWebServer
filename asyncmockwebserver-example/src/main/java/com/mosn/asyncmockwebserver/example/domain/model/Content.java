package com.mosn.asyncmockwebserver.example.domain.model;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.mosn.asyncmockwebserver.example.domain.core.Entity;
import com.mosn.asyncmockwebserver.example.domain.core.Id;

public class Content extends Entity {

    public String wrapperType;

    public String kind;

    public int artistId;

    public int collectionId;

    public int trackId;

    public String artistName;

    public String collectionName;

    public String trackName;

    public String artworkUrl100;

    public String primaryGenreName;

    protected Content(@NonNull Id id) {
        super(id);
    }

    protected Content(Parcel in) {
        super(in);
        wrapperType = in.readString();
        kind = in.readString();
        artistId = in.readInt();
        collectionId = in.readInt();
        trackId = in.readInt();
        artistName = in.readString();
        collectionName = in.readString();
        trackName = in.readString();
        artworkUrl100 = in.readString();
        primaryGenreName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(wrapperType);
        dest.writeString(kind);
        dest.writeInt(artistId);
        dest.writeInt(collectionId);
        dest.writeInt(trackId);
        dest.writeString(artistName);
        dest.writeString(collectionName);
        dest.writeString(trackName);
        dest.writeString(artworkUrl100);
        dest.writeString(primaryGenreName);
    }

    public static Content create(int artistId, int collectionId, int trackId) {
        return new Content(new Id(artistId + "/" + collectionId + "/" + trackId));
    }

    public static final Creator<Content> CREATOR = new Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel in) {
            return new Content(in);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };
}
