package com.example.olga.photoeditor.db;

import android.content.Context;

import com.example.olga.photoeditor.models.PhotoEffect;

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
            mDbHelper.getPhotoEffectDao().create(photoEffect);
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

    public PhotoEffect findEffect(String name) {
        try {
            List<PhotoEffect> effects = mDbHelper.getPhotoEffectDao().queryBuilder().where().eq(PhotoEffect.Column.EFFECT_NAME, name).query();
            return effects.get(0);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface ResultListener<T> {
        void updateEffectValue(T effect);
    }

}
