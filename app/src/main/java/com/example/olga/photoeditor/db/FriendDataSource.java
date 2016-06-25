package com.example.olga.photoeditor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.olga.photoeditor.models.vkfriends.Friend;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 25.06.16
 * Time: 13:55
 *
 * @author Olga
 */
public class FriendDataSource {
    private SQLiteDatabase mDatabase;
    private DbHelper mDbHelper;

    public FriendDataSource(Context context) {
        mDbHelper = new DbHelper(context);
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public long saveFriend(Friend friend) {
        long id = friend.getFriendId();
        ContentValues values = new ContentValues();

        values.put(Friend.Database.Column.USER_ID, friend.getFriendId());
        values.put(Friend.Database.Column.FIRST_NAME, friend.getFirstName());
        values.put(Friend.Database.Column.LAST_NAME, friend.getLastName());
        values.put(Friend.Database.Column.ONLINE, friend.getOnline());
        values.put(Friend.Database.Column.PHOTO_100, friend.getPhotoUrl());
        if (mDatabase.query(Friend.Database.TABLE, null, "id = " + id, null, null, null, null) != null)
            return mDatabase.insert(Friend.Database.TABLE, null, values);
        else
            return mDatabase.update(Friend.Database.TABLE, values, Friend.Database.Column.USER_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteFriend(Friend friend) {
        long id = friend.getFriendId();
        if (mDatabase.query(Friend.Database.TABLE, null, "id = " + id, null, null, null, null) != null)
        mDatabase.delete(Friend.Database.TABLE, Friend.Database.Column.USER_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public List<Friend> getAllFriends() {
        List<Friend> friends = new ArrayList<>();

        Cursor cursor = mDatabase.query(Friend.Database.TABLE, null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Friend friend = cursorToFriend(cursor);
            friends.add(friend);
            cursor.moveToNext();
        }

        cursor.close();
        return friends;
    }

    public List<Friend> getOnlineFriends() {
        List<Friend> friends = new ArrayList<>();

        Cursor cursor = mDatabase.query(Friend.Database.TABLE, null, "online = 1", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Friend friend = cursorToFriend(cursor);
            friends.add(friend);
            cursor.moveToNext();
        }

        cursor.close();
        return friends;
    }

    public Friend cursorToFriend(Cursor cursor) {
        Friend friend = new Friend();

        int userIdIndex = cursor.getColumnIndex((Friend.Database.Column.USER_ID));
        int firstNameIndex = cursor.getColumnIndex((Friend.Database.Column.FIRST_NAME));
        int lastNameIndex = cursor.getColumnIndex((Friend.Database.Column.LAST_NAME));
        int onlainIndex = cursor.getColumnIndex((Friend.Database.Column.ONLINE));
        int photoUrlIndex = cursor.getColumnIndex((Friend.Database.Column.PHOTO_100));

        friend.setFriendId(cursor.getInt(userIdIndex));
        friend.setFirstName(cursor.getString(firstNameIndex));
        friend.setLastName(cursor.getString(lastNameIndex));
        friend.setOnline(cursor.getString(onlainIndex));
        friend.setPhotoUrl(cursor.getString(photoUrlIndex));

        return friend;
    }
}
