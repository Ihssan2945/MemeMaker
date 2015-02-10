package es.tessier.mememaker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by Evan Anger on 8/17/14.
 */
public class MemeSQLiteHelper extends SQLiteOpenHelper{

    final static String DB_NAME="memes.db";
    final static int DB_VERSION=1;
    static final String TAG = MemeSQLiteHelper.class.getName();



    //Meme Table functionality

    static final String MEMES_TABLE="MEMES";
    static final String COLUMN_MEMES_ASSET="asset";
    static final String COLUMN_MEMES_NAME="name";
    static final String COLUMN_MEMES_ID="_id";

    static final String CREATE_TABLE_MEMES="CREATE TABLE" + MEMES_TABLE + " ( "+
            COLUMN_MEMES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " +
            COLUMN_MEMES_ASSET + " TEXT NOT NULL, " +
            COLUMN_MEMES_NAME + " TEXT NOT NULL );";
    static final String FK_MEME="FK_MEME_ID";
    static final String ANNOT="annotations";
    static final String CREATE_TABLE_ANNOTATIONS="CREATE TABLE" + "ANNOTATIONS" + " ( "+
            FK_MEME + " INTEGER" +
            ANNOT + " TEXT" +
            " FOREIGN KEY (FK_MEME) REFERENCES MEMES_TABLE(COLUMN_MEMES_ID) );";

    public MemeSQLiteHelper(Context context) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(CREATE_TABLE_MEMES);
            db.execSQL(CREATE_TABLE_ANNOTATIONS);
        } catch (Exception E) {
            Log.e(TAG, "Android Exception caught" + E);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Meme Table Annotations functionality



}
