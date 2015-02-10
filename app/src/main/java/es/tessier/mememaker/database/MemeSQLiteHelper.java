package es.tessier.mememaker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    static final String ANNOTATIONS_TABLE="ANNOTATIONS";
    static final String COLUMN_ANNOTATIONS_MEME="meme_id";
    static final String COLUMN_ANNOTATIONS_TITLE="title";
    static final String COLUMN_ANNOTATIONS_ID="_id";
    static final String COLUMN_ANNOTATIONS_X="x";
    static final String COLUMN_ANNOTATIONS_Y="y";
    static final String COLUMN_ANNOTATIONS_COLOR="color";
    static final String CREATE_TABLE_ANNOTATIONS="CREATE TABLE "+ANNOTATIONS_TABLE+"("+COLUMN_ANNOTATIONS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_ANNOTATIONS_TITLE+" TEXT NOT NULL, "+COLUMN_ANNOTATIONS_MEME+" INTEGER NOT NULL,"+COLUMN_ANNOTATIONS_X+" INTEGER NOT NULL" +
            ","+COLUMN_ANNOTATIONS_Y+" INTEGER NOT NULL, "+COLUMN_ANNOTATIONS_COLOR+" INTEGER NOT NULL,FOREIGN KEY("+COLUMN_ANNOTATIONS_MEME+") REFERENCES MEMES("+COLUMN_MEMES_ID+"));";

    public MemeSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
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
