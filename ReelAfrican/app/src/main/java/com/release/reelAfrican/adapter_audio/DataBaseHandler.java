package com.release.reelAfrican.adapter_audio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.release.reelAfrican.utils.Util;


/**
 * Created by Muvi on 5/12/2017.
 */

public class DataBaseHandler extends SQLiteOpenHelper {
    public DataBaseHandler(Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + Util.USER_TABLE_NAME
                + " (Id INTEGER PRIMARY KEY AUTOINCREMENT ,ALBUM_ART_PATH TEXT,ALBUM_SONG_NAME TEXT);");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + Util.ADAPTOR_TABLE_NAME
                + " (Id INTEGER PRIMARY KEY AUTOINCREMENT ,ALBUM_URL TEXT,PERMALINK TEXT,ALBUM_ART TEXT,ALBUM_NAME TEXT,ALBUM_SONG_NAME TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            onCreate(sqLiteDatabase);
    }
}
