package com.example.olga.photoeditor.models;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * Date: 09.08.16
 * Time: 19:50
 *
 * @author Olga
 */

@DatabaseTable(tableName = PhotoEffect.TABLE)
public class PhotoEffect implements Serializable {
    public static final String TABLE = "PHOTO_EFFECT";

    public static class Column {
        public static final String ID = "id";
        public static final String EFFECT_NAME = "effect_name";
        public static final String EFFECT_VALUE = "effect_value";
        public static final String EFFECT_TYPE = "effect_type";
    }

    @DatabaseField(columnName = Column.ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = Column.EFFECT_NAME)
    private String mEffectName;

    @DatabaseField(columnName = Column.EFFECT_VALUE)
    private float mEffectValue;

    @DatabaseField(columnName = Column.EFFECT_TYPE)
    private String mEffectType;

    public PhotoEffect() {/**/}

    public PhotoEffect(String effectName, String effectType, float effectValue) {
        mEffectValue = effectValue;
        mEffectName = effectName;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getEffectName() {
        return mEffectName;
    }

    public void setEffectName(String effectName) {
        mEffectName = effectName;
    }

    public String getEffectType() {
        return mEffectType;
    }

    public void setEffectType(String effectType) {
        mEffectType = effectType;
    }

    public float getEffectValue() {
        return mEffectValue;
    }

    public void setEffectValue(float effectValue) {
        mEffectValue = effectValue;
    }

    public static void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) throws SQLException {
        TableUtils.createTable(connectionSource, PhotoEffect.class);
    }

    public static void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) throws SQLException {
        //use old
    }

}
