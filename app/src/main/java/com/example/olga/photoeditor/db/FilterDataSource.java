package com.example.olga.photoeditor.db;

import android.content.Context;

import com.example.olga.photoeditor.models.Effects.Filter;

import java.util.Collections;
import java.util.List;

/**
 * Date: 08.07.16
 * Time: 14:47
 *
 * @author Olga
 */
public class FilterDataSource {

    private OrmLiteDbHelper mDbHelper;

    public FilterDataSource(Context context) {
        mDbHelper = OrmLiteDbHelper.getInstance(context);
    }

    public void saveFilter(Filter filter) {
        try {
            mDbHelper.getFilterDao().createOrUpdate(filter);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFilter(Filter filter) {
        try {
            mDbHelper.getFilterDao().delete(filter);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Filter> getAllFilters() {
        try {
            return mDbHelper.getFilterDao().queryForAll();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
