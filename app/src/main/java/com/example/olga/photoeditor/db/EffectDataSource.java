package com.example.olga.photoeditor.db;

import android.content.Context;

import com.example.olga.photoeditor.models.PhotoEffect;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Date: 25.06.2016
 * Time: 09:21
 *
 * @author Jeksor
 */
public class EffectDataSource implements Serializable {

    private OrmLiteDbHelper mDbHelper;
    protected ResultListener<PhotoEffect> mResultListener;

    public EffectDataSource(Context context, ResultListener<PhotoEffect> resultListener) {
        mDbHelper = OrmLiteDbHelper.getInstance(context);
        mResultListener = resultListener;
    }

    public void createEffect(PhotoEffect photoEffect) {
        try {
            mDbHelper.getPhotoEffectDao().createIfNotExists(photoEffect);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllEffects() {
        try {
            List<PhotoEffect> effects = mDbHelper.getPhotoEffectDao().queryForAll();
            mDbHelper.getPhotoEffectDao().delete(effects);
        } catch (
                java.sql.SQLException e
                )

        {
            e.printStackTrace();
        }
    }

    public void updateEffect(PhotoEffect photoEffect) {
        try {
            mDbHelper.getPhotoEffectDao().update(photoEffect);
            mResultListener.updateEffectValue(photoEffect);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public List<PhotoEffect> getAllEffects() {
        try {
            return mDbHelper.getPhotoEffectDao().queryForAll();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public float getEffectValue(String name) {
        try {
            QueryBuilder<PhotoEffect, Integer> effectQb = mDbHelper.getPhotoEffectDao().queryBuilder();
            effectQb.where().eq(PhotoEffect.Column.EFFECT_NAME, name);
            List<PhotoEffect> effects = mDbHelper.getPhotoEffectDao().queryBuilder().join(effectQb).query();
            return effects.get(0).getEffectValue();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return 5.0f;
        }
    }


    public interface ResultListener<T> {
        void updateEffectValue(PhotoEffect effect);
    }

}
