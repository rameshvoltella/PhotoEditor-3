package com.example.olga.photoeditor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.olga.photoeditor.models.PhotoEffect;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Date: 25.06.2016
 * Time: 08:39
 *
 * @author Jeksor
 */
public class OrmLiteDbHelper extends OrmLiteSqliteOpenHelper {
    public static final String DB_NAME = "effects.db";
    public static final int DB_VERSION = 1;

    private static OrmLiteDbHelper sInstance;

    private Dao<PhotoEffect, Integer> mPhotoEffectDao;

    public static OrmLiteDbHelper getInstance(Context context)
    {
        if (sInstance == null)
        {
            synchronized (OrmLiteDbHelper.class)
            {
                if (sInstance == null)
                {
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
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            PhotoEffect.onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            PhotoEffect.onUpgrade(database, connectionSource, oldVersion, newVersion);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<PhotoEffect, Integer> getPhotoEffectDao() throws SQLException {
        if (mPhotoEffectDao == null) {
            mPhotoEffectDao = getDao(PhotoEffect.class);
        }
        return mPhotoEffectDao;
    }
}
