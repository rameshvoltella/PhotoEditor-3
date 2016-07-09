package com.example.olga.photoeditor.models.vkfriends;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    @SerializedName("count")
    private int mCount;

    @SerializedName("items")
    private List<Friend> mFriends;

    @SuppressWarnings("unused")
    public int getCount() {
        return mCount;
    }

    @SuppressWarnings("unused")
    public void setCount(int count) {
        mCount = count;
    }

    public List<Friend>
    getFriends() {
        return mFriends;
    }

    @SuppressWarnings("unused")
    public void setFriends(List<Friend> friends) {
        mFriends = friends;
    }
}
