package com.example.olga.photoeditor.db;

import android.content.Context;

import com.example.olga.photoeditor.models.vkfriends.Friend;

import java.util.ArrayList;
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

    public void deleteAll() {
        try {
            mDbHelper.getFriemdDao().delete();
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

    public List<Friend> findFriends(String string) {
        try {
            List<Friend> friends = new ArrayList<>();
            friends.addAll(mDbHelper.getFriemdDao().queryForEq(Friend.Column.FIRST_NAME, string));
            friends.addAll(mDbHelper.getFriemdDao().queryForEq(Friend.Column.LAST_NAME, string));
            return friends;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
