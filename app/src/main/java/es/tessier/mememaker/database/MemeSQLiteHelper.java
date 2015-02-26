package es.tessier.mememaker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Evan Anger on 8/17/14.
 */
public class MemeSQLiteHelper extends SQLiteOpenHelper{

    private static final String TAG= MemeSQLiteHelper.class.getSimpleName();
    private static final String DB_NAME="meme.db";
    private static final int DB_VERSION=2;



    //Meme Table functionality
    static final String MEMES_TABLE="MEMES";
    static final String COLUMN_MEMES_ASSET="asset";
    static final String COLUMN_MEMES_NAME="name";
    static final String COLUMN_MEMES_CREATE_DATE="create_date";
    static final String COLUMN_MEMES_ID="_id";

    static final String CREATE_TABLE_MEMES="CREATE TABLE "+MEMES_TABLE+" ( "+
            COLUMN_MEMES_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            COLUMN_MEMES_ASSET+" TEXT NOT NULL,"+
            COLUMN_MEMES_NAME+ " TEXT NOT NULL,"+
            COLUMN_MEMES_CREATE_DATE+" INTEGER);";

    //Meme Table Annotations functionality
    static final String ANNOTATIONS_TABLE="ANNOTATIONS";
    static final String COLUMN_ANNOTATIONS_MEME="meme_id";
    static final String COLUMN_ANNOTATIONS_TITLE="title";
    static final String COLUMN_ANNOTATIONS_ID="_id";
    static final String COLUMN_ANNOTATIONS_X="x";
    static final String COLUMN_ANNOTATIONS_Y="y";
    static final String COLUMN_ANNOTATIONS_COLOR="color";
    static final String CREATE_TABLE_ANOTATIONS="CREATE TABLE "+ANNOTATIONS_TABLE+"("+COLUMN_ANNOTATIONS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_ANNOTATIONS_TITLE+" TEXT NOT NULL, "+COLUMN_ANNOTATIONS_MEME+" INTEGER NOT NULL,"+COLUMN_ANNOTATIONS_X+" INTEGER NOT NULL" +
            ","+COLUMN_ANNOTATIONS_Y+" INTEGER NOT NULL, "+COLUMN_ANNOTATIONS_COLOR+" INTEGER NOT NULL,FOREIGN KEY("+COLUMN_ANNOTATIONS_MEME+") REFERENCES MEMES("+COLUMN_MEMES_ID+"));";

    //v2
    static final String ALTER_ADD_CREATE_DATE="ALTER TABLE "+ MEMES_TABLE +
            " ADD COLUMN "+ COLUMN_MEMES_CREATE_DATE+ " INTEGER ;";

    public MemeSQLiteHelper(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEMES);
        db.execSQL(CREATE_TABLE_ANOTATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        switch (oldVersion)
        {
            case 1: db.execSQL(ALTER_ADD_CREATE_DATE);
        }
    }





}