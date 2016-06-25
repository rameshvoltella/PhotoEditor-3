package com.example.olga.photoeditor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.olga.photoeditor.models.vkfriends.Friend;

/**
 * Date: 25.06.16
 * Time: 13:55
 *
 * @author Olga
 */
public class DbHelper extends SQLiteOpenHelper {

    public  static  final  String  DB_NAME = "sample.db";
    public  static  final  int DB_VERSION = 1;

    public DbHelper (Context context){
        super(context, DB_NAME, null, DB_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Friend.Database.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Friend.Database.onUpgrade(db, oldVersion, newVersion);
    }
}
