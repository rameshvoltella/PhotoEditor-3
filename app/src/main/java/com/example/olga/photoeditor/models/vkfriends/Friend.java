package com.example.olga.photoeditor.models.vkfriends;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused")
@DatabaseTable(tableName = Friend.TABLE)
public class Friend implements Serializable {
    public static final String TABLE = "Friend";

    public static class Column {
        public static final String ID = "id";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String ONLINE = "online";
        public static final String PHOTO_100 = "photo_100";
    }

    @DatabaseField(columnName = Column.ID, id = true)
    @SerializedName("id")
    private int mFriendId;

    @DatabaseField(columnName = Column.FIRST_NAME)
    @SerializedName("first_name")
    private String mFirstName;

    @DatabaseField(columnName = Column.LAST_NAME)
    @SerializedName("last_name")
    private String mLastName;

    @DatabaseField(columnName = Column.ONLINE)
    @SerializedName("online")
    private String mOnline;

    @DatabaseField(columnName = Column.PHOTO_100)
    @SerializedName("photo_100")
    private String mPhotoUrl;

    @SerializedName("hidden")
    private int mHidden;

    @SerializedName("lists")
    private List<Integer> mLists;

    public Friend() {/**/}

    public int getFriendId() {
        return mFriendId;
    }

    public void setFriendId(int friendId) {
        mFriendId = friendId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getOnline() {
        return mOnline;
    }

    public void setOnline(String online) {
        mOnline = online;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }

    @SuppressWarnings("unused")
    public int getHidden() {
        return mHidden;
    }

    @SuppressWarnings("unused")
    public void setHidden(int hidden) {
        mHidden = hidden;
    }

    @SuppressWarnings("unused")
    public List<Integer> getLists() {
        return mLists;
    }

    @SuppressWarnings("unused")
    public void setLists(List<Integer> lists) {
        mLists = lists;
    }

    public static void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) throws SQLException, java.sql.SQLException {
        TableUtils.createTable(connectionSource, Friend.class);
    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        //
    }

}
