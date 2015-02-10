package es.tessier.mememaker.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import es.tessier.mememaker.MemeMakerApplicationSettings;

/**
 * Created by Evan Anger on 7/28/14.
 */
public class FileUtilities {
    final static String STORAGE_TYPE= MemeMakerApplicationSettings.getStoragePreference();
    final static String albumName="mememaker";

    public static void saveAssetImage(Context context, String assetName) {
        File fileDirectory = getFilesDirectory(context);
        File fileToWrite = new File(fileDirectory,assetName);
        AssetManager assetManager = context.getAssets();
        InputStream in = null;
        FileOutputStream out=null;
        try{
            in=assetManager.open(assetName);
            out = new FileOutputStream(fileToWrite);
            byte[]buffer=new byte[1024];
            int read=0;
                while((read=in.read(buffer))!=-1){
                    out.write(buffer,0,read);
                }

        }catch(FileNotFoundException e){
            Log.e("hola","no encontre el fichero");}
        catch(IOException e){
            Log.e("hola","salta IO XD");}
        finally {

            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static File getFilesDirectory(Context context) {
        if (STORAGE_TYPE.equals(StorageType.INTERNAL)) {
            return context.getFilesDir();
        } else {
            if (isExternalStorageAvailable()) {
                return getAlbumStorageDir(albumName);

            }
        }

    return null;
    }
    public static File getAlbumStorageDir (String albumName){
        File file= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        if(!file.mkdir()){Log.e("hola","Directory not created");}
        return file;
    }
    private static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }
        return false;
    }




    public static Uri saveImageForSharing(Context context, Bitmap bitmap,  String assetName) {
        File fileToWrite = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), assetName);

        try {
            FileOutputStream outputStream = new FileOutputStream(fileToWrite);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return Uri.fromFile(fileToWrite);
        }
    }


    public static void saveImage(Context context, Bitmap bitmap, String name) {
        File fileDirectory = getFilesDirectory(context);
        File fileToWrite = new File(fileDirectory, name);

        try {
            FileOutputStream outputStream = new FileOutputStream(fileToWrite);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static File[] hola(Context context){
        File fileDirectory = context.getFilesDir();
        File [] filteredFiles = fileDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {

                if (pathname.getAbsolutePath().contains(".jpg")||pathname.getAbsolutePath().contains(".png") ){
                    return true;
                }
                else
                return false;
            }
        });
        return filteredFiles;
    }

}
