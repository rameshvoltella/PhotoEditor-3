package com.example.olga.photoeditor.db;

import android.content.Context;

import com.example.olga.photoeditor.models.vkfriends.Friend;

import java.util.Collections;
import java.util.List;

/**
 * Date: 25.06.16
 * Time: 13:55
 *
 * @author Olga
 */
public class FriendDataSource {

    private OrmLiteDbHelper mDbHelper;

    public FriendDataSource(Context context) {
        mDbHelper = OrmLiteDbHelper.getInstance(context);
    }

    public void saveFriend(Friend friend) {
        try {
            mDbHelper.getFriemdDao().createOrUpdate(friend);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFriend(Friend friend) {
        try {
            mDbHelper.getFriemdDao().delete(friend);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Friend> getAllFriends() {
        try {
            return mDbHelper.getFriemdDao().queryForAll();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Friend> getOnlineFriends() {
        try {
            return mDbHelper.getFriemdDao().queryForEq(Friend.Column.ONLINE, "1");
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
