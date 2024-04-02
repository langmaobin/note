package com.android.notes.sample.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class AutoModel implements Parcelable {
    @SerializedName("id")
    private String id;
    @SerializedName("content")
    private String content;
    @SerializedName("name")
    private String name;

    public AutoModel() {
    }

    protected AutoModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        content = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(content);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AutoModel> CREATOR = new Creator<AutoModel>() {
        @Override
        public AutoModel createFromParcel(Parcel in) {
            return new AutoModel(in);
        }

        @Override
        public AutoModel[] newArray(int size) {
            return new AutoModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
