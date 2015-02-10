package es.tessier.mememaker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import es.tessier.mememaker.models.Meme;
import es.tessier.mememaker.models.MemeAnnotation;

/**
 * Created by Evan Anger on 8/17/14.
 */
public class MemeDatasource {

    private Context mContext;
    private MemeSQLiteHelper mMemeSqlLiteHelper;


    public MemeDatasource(Context context) {
        mContext = context;
        mMemeSqlLiteHelper = new MemeSQLiteHelper(mContext);
        SQLiteDatabase database = mMemeSqlLiteHelper.getReadableDatabase();
    }
    public SQLiteDatabase openWritable(){
        SQLiteDatabase bd=mMemeSqlLiteHelper.getWritableDatabase();
        return bd;
    }
    public void closeWritable(SQLiteDatabase bd){
       bd.close();
    }

public void create(Meme meme){
    SQLiteDatabase database=openWritable();
    database.beginTransaction();
    ContentValues memeValues=new ContentValues();
    memeValues.put(MemeSQLiteHelper.COLUMN_MEMES_NAME,meme.getName());
    memeValues.put(MemeSQLiteHelper.COLUMN_MEMES_ASSET,meme.getAssetLocation());
    long memeID=database.insert(MemeSQLiteHelper.MEMES_TABLE, null, memeValues);
    ArrayList<MemeAnnotation> annotations=meme.getAnnotations();
    for (MemeAnnotation annotation:annotations)
    {
        ContentValues annotationsValues = new ContentValues();
        annotationsValues.put(MemeSQLiteHelper.COLUMN_ANNOTATIONS_COLOR,annotation.getColor());
        annotationsValues.put(MemeSQLiteHelper.COLUMN_ANNOTATIONS_X,annotation.getLocationX());
        annotationsValues.put(MemeSQLiteHelper.COLUMN_ANNOTATIONS_Y,annotation.getLocationY());
        annotationsValues.put(MemeSQLiteHelper.COLUMN_ANNOTATIONS_TITLE,annotation.getTitle());
        annotationsValues.put(MemeSQLiteHelper.COLUMN_ANNOTATIONS_MEME,meme.getId());

        database.insert(MemeSQLiteHelper.ANNOTATIONS_TABLE,null,annotationsValues);
    }
    database.endTransaction();
    closeWritable(database);
}
}