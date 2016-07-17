package com.example.olga.photoeditor.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.olga.photoeditor.models.Filter;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Date: 13.07.16
 * Time: 21:07
 *
 * @author Olga
 */
public class OrmLiteHelper extends OrmLiteSqliteOpenHelper {
    public static final String DB_NAME = "Filters.db";
    public static final int DB_VERSION = 1;

    private static OrmLiteHelper sInstance;

    private Dao<Filter, Integer> mFilterDao;


    public OrmLiteHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static OrmLiteHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (OrmLiteHelper.class) {
                if (sInstance == null) {
                    sInstance = OpenHelperManager.getHelper(context, OrmLiteHelper.class);
                }
            }
        }

        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Filter.onCreate(database, connectionSource);
            for (Filter filter : Filter.getStandardFilters()) {
                getFilterDao().create(filter);
            }

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Filter.onUpgrade(database, connectionSource, oldVersion, newVersion);
    }

    public Dao<Filter, Integer> getFilterDao() throws SQLException, java.sql.SQLException {
        if (mFilterDao == null) {
            mFilterDao = getDao(Filter.class);
        }
        return mFilterDao;
    }
}
