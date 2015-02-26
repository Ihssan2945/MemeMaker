package es.tessier.mememaker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Date;


import es.tessier.mememaker.models.Meme;
import es.tessier.mememaker.models.MemeAnnotation;

/**
 * Created by Evan Anger on 8/17/14.
 */
public class MemeDatasource {

    private Context mContext;
    private MemeSQLiteHelper mMemeSqlLiteHelper;

    public MemeDatasource(Context context)
    {
        mContext = context;
        mMemeSqlLiteHelper= new MemeSQLiteHelper(context);
    }

    public SQLiteDatabase openWritable()
    {
        return mMemeSqlLiteHelper.getWritableDatabase();
    }

    public SQLiteDatabase openReadable()
    {
        return mMemeSqlLiteHelper.getReadableDatabase();
    }

    public void closeDataBase(SQLiteDatabase database)
    {
        database.close();
    }

    public ArrayList<Meme> read()
    {
        ArrayList<Meme> memes =readMemes();
        addMemeAnnotations(memes);

        return memes;
    }

    public ArrayList<Meme> readMemes()
    {
        SQLiteDatabase database=openReadable();

        Cursor c = database.query(MemeSQLiteHelper.MEMES_TABLE,new String[]{MemeSQLiteHelper.COLUMN_MEMES_NAME, BaseColumns._ID,
                MemeSQLiteHelper.COLUMN_MEMES_ASSET},null,null,null,null,MemeSQLiteHelper.COLUMN_MEMES_CREATE_DATE+" DESC;");

        ArrayList<Meme> memes=new ArrayList<Meme>();

        if(c.moveToFirst())
        {
            do
            {
                Meme meme = new Meme(getIntFromColumnName(c,MemeSQLiteHelper.COLUMN_MEMES_ID),getStringFromColumnName
                        (c,MemeSQLiteHelper.COLUMN_MEMES_ASSET),getStringFromColumnName(c,MemeSQLiteHelper.COLUMN_MEMES_NAME),null);
                memes.add(meme);
            }while(c.moveToNext());
        }
        c.close();
        database.close();

        return memes;
    }

    private void addMemeAnnotations(ArrayList<Meme> memes)
    {
        SQLiteDatabase database = openReadable();

        for(Meme meme:memes)
        {
            ArrayList<MemeAnnotation> annotations = new ArrayList<MemeAnnotation>();

            Cursor c=database.rawQuery("SELECT * FROM "+MemeSQLiteHelper.ANNOTATIONS_TABLE+" WHERE "
                    +MemeSQLiteHelper.COLUMN_ANNOTATIONS_MEME+" = "+meme.getId(),null);

            if(c.moveToFirst())
            {
                do
                {
                    MemeAnnotation annotation = new MemeAnnotation(getIntFromColumnName(c,MemeSQLiteHelper.COLUMN_ANNOTATIONS_ID),getStringFromColumnName(c,MemeSQLiteHelper.COLUMN_ANNOTATIONS_COLOR),getStringFromColumnName(c,MemeSQLiteHelper.COLUMN_ANNOTATIONS_TITLE),getIntFromColumnName(c,MemeSQLiteHelper.COLUMN_ANNOTATIONS_X),getIntFromColumnName(c,MemeSQLiteHelper.COLUMN_ANNOTATIONS_Y));
                    annotations.add(annotation);
                }while(c.moveToNext());


            }

            meme.setAnnotations(annotations);
            c.close();
        }
        database.close();
    }

    private int getIntFromColumnName(Cursor cursor,String columnName)
    {
        int columnIndex=cursor.getColumnIndex(columnName);
        return cursor.getInt(columnIndex);
    }

    private String getStringFromColumnName(Cursor cursor,String columnName)
    {
        int columnIndex=cursor.getColumnIndex(columnName);
        return cursor.getString(columnIndex);
    }

    public void create(Meme meme)
    {
        SQLiteDatabase database=openWritable();
        database.beginTransaction();

        ContentValues memeValues = new ContentValues();
        memeValues.put(MemeSQLiteHelper.COLUMN_MEMES_NAME,meme.getName());
        memeValues.put(MemeSQLiteHelper.COLUMN_MEMES_ASSET,meme.getAssetLocation());
        memeValues.put(MemeSQLiteHelper.COLUMN_MEMES_CREATE_DATE,new Date().getTime());

        long memeID=database.insert(MemeSQLiteHelper.MEMES_TABLE,null,memeValues);

        ArrayList<MemeAnnotation> annotations=meme.getAnnotations();

        for (MemeAnnotation annotation:annotations)
        {
            ContentValues annotationsValues = new ContentValues();
            annotationsValues.put(MemeSQLiteHelper.COLUMN_ANNOTATIONS_COLOR,annotation.getColor());
            annotationsValues.put(MemeSQLiteHelper.COLUMN_ANNOTATIONS_X,annotation.getLocationX());
            annotationsValues.put(MemeSQLiteHelper.COLUMN_ANNOTATIONS_Y,annotation.getLocationY());
            annotationsValues.put(MemeSQLiteHelper.COLUMN_ANNOTATIONS_TITLE,annotation.getTitle());
            annotationsValues.put(MemeSQLiteHelper.COLUMN_ANNOTATIONS_MEME,memeID);

            database.insert(MemeSQLiteHelper.ANNOTATIONS_TABLE,null,annotationsValues);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        closeDataBase(database);
    }

    public void update (Meme meme)
    {
        SQLiteDatabase database=openWritable();
        database.beginTransaction();

        ContentValues updateMemeValues = new ContentValues();
        updateMemeValues.put(MemeSQLiteHelper.COLUMN_MEMES_NAME,meme.getName());

        database.update(MemeSQLiteHelper.MEMES_TABLE,updateMemeValues,String.format("%s=%d",BaseColumns._ID,meme.getId()),null);

        ArrayList<MemeAnnotation> annotations=meme.getAnnotations();



        for (MemeAnnotation annotation:annotations)
        {
            ContentValues updateAnnotations = new ContentValues();

            updateAnnotations.put(MemeSQLiteHelper.COLUMN_ANNOTATIONS_COLOR,annotation.getColor());
            updateAnnotations.put(MemeSQLiteHelper.COLUMN_ANNOTATIONS_X,annotation.getLocationX());
            updateAnnotations.put(MemeSQLiteHelper.COLUMN_ANNOTATIONS_Y,annotation.getLocationY());
            updateAnnotations.put(MemeSQLiteHelper.COLUMN_ANNOTATIONS_TITLE,annotation.getTitle());
            updateAnnotations.put(MemeSQLiteHelper.COLUMN_ANNOTATIONS_MEME,meme.getId());

            if(annotation.hasBeenSaved())
            {
                database.update(MemeSQLiteHelper.ANNOTATIONS_TABLE,updateAnnotations,String.format("%s=%d",MemeSQLiteHelper.COLUMN_ANNOTATIONS_MEME,
                        annotation.getId()),null);
            }
            else
            {
                database.insert(MemeSQLiteHelper.ANNOTATIONS_TABLE,null,updateAnnotations);
            }
        }

        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    public void delete(int id)
    {
        SQLiteDatabase database=openWritable();
        database.beginTransaction();

        database.delete(MemeSQLiteHelper.ANNOTATIONS_TABLE,String.format("%s=%d",MemeSQLiteHelper.COLUMN_ANNOTATIONS_MEME,id),null);
        database.delete(MemeSQLiteHelper.MEMES_TABLE,String.format("%s=%d",MemeSQLiteHelper.COLUMN_MEMES_ID,id),null);


        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }
}