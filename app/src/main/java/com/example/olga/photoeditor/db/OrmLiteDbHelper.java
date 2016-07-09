package com.example.olga.photoeditor.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.olga.photoeditor.models.Effects.Filter;
import com.example.olga.photoeditor.models.vkfriends.Friend;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Date: 25.06.16
 * Time: 13:55
 *
 * @author Olga
 */
public class OrmLiteDbHelper extends OrmLiteSqliteOpenHelper {

    public static final String DB_NAME = "sample.db";
    public static final int DB_VERSION = 1;

    private static OrmLiteDbHelper sInstance;

    private Dao<Friend, Integer> mFriendDao;
    private Dao<Filter, Integer> mFilterDao;

    public static OrmLiteDbHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (OrmLiteDbHelper.class) {
                if (sInstance == null) {
                    sInstance = OpenHelperManager.getHelper(context, OrmLiteDbHelper.class);
                }
            }
        }

        return sInstance;
    }

    public OrmLiteDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Friend.onCreate(db, connectionSource);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Friend.onUpgrade(db, connectionSource, oldVersion, newVersion);
    }

    public Dao<Filter, Integer> getFilterDao() throws SQLException, java.sql.SQLException {
        if (mFilterDao == null) {
            mFilterDao = getDao(Filter.class);
        }
        return mFilterDao;
    }

    public Dao<Friend, Integer> getFriendDao() throws SQLException, java.sql.SQLException {
        if (mFriendDao == null) {
            mFriendDao = getDao(Friend.class);
        }
        return mFriendDao;
    }

}
