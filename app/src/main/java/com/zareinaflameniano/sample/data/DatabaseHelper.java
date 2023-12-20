package com.zareinaflameniano.sample.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    //Don't forget to put your namespace
    private static String DB_PATH = "/data/data/com.zareinaflameniano.sample/databases/";

    private static String DB_NAME = "Database.db";

    private SQLiteDatabase myDatabase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 4);
        this.myContext = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }


    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
            Log.i("DatabaseHelper", "DB is existing. NOT COPYING");
        } else {
            //By calling this method an empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e("DatabaseHelper", e.getMessage());
                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                File dbFile = myContext.getDatabasePath(DB_NAME);
                myPath = dbFile.getPath();
                Log.i("DatabaseHelper", "here1: " + myPath);

            } else {
                myPath = DB_PATH + DB_NAME;
                Log.i("DatabaseHelper", "here2: " + myPath);
            }
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.NO_LOCALIZED_COLLATORS);

        } catch (SQLiteException e) {

            //database does't exist yet.
            Log.e("DatabaseHelper", e.getLocalizedMessage());

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {
        Log.i("DatabaseHelper", "DB is not existing. COPYING");
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db

        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    @Override
    public synchronized void close() {
        if (myDatabase != null)
            myDatabase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion) {
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public List<TownModel> getAllTowns(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<TownModel> data = new ArrayList<>();

        Cursor cursor;

        try{
            cursor = db.query("tblTowns", null, null, null, null, null,null);
            while(cursor.moveToNext()){
                TownModel town = new TownModel(cursor.getInt(0), cursor.getString(1));
                data.add(town);
            }
            Log.i("DatabaseHelper", "" + data);
        }catch(Exception e){
            Log.e("DatabaseHelper", "" + e.getLocalizedMessage());
        }

        return data;
    }



    public List<AccommodationsModel> getAccommodationsByTownID(long townID){
        SQLiteDatabase db = this.getWritableDatabase();
        List<AccommodationsModel> data = new ArrayList<>();
        Cursor cursor;

        try{
            cursor = db.query("tblAccommodations", null, "townID=?", new String[]{String.valueOf(townID)}, null, null, null);
            while(cursor.moveToNext()){
                AccommodationsModel spots = new AccommodationsModel(cursor.getInt(0), cursor.getInt(1),
                        cursor.getString(2),cursor.getString(3), cursor.getString(4),
                        cursor.getString(5),cursor.getInt(6), cursor.getString(7));
                data.add(spots);
            }
            Log.i("DatabaseHelper", "" + data);
        }catch(Exception e){
            Log.e("DatabaseHelper", "" + e.getLocalizedMessage());
        }

        return data;
    }

    public List<FavoritesModel> getAllFavorites(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<FavoritesModel> data = new ArrayList<>();

        Cursor cursor;

        try{
            cursor = db.query("tblFavorites", null, null, null, null, null,null);
            while(cursor.moveToNext()){
                FavoritesModel fav = new FavoritesModel(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
                data.add(fav);
            }
            Log.i("DatabaseHelper", "" + data);
        }catch(Exception e){
            Log.e("DatabaseHelper", "" + e.getLocalizedMessage());
        }
        return data;
    }


    private void removeFavoriteByAccID(int accID) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        Cursor cursor = db.query("tblAccommodations", new String[]{"accName"}, "accID=?", new String[]{String.valueOf(accID)}, null, null, null);
        String accName = null;

        if (cursor.moveToFirst()) {
            accName = cursor.getString(cursor.getInt(0));
        }

        cursor.close();

        if (accName != null) {
            // Remove the item from tblFavorites using favName
            db.delete("tblFavorites", "favName = ?", new String[]{accName});
        }
    }

    public void updateFavorite(int accID, int value) {
        SharedPreferences preferences = myContext.getSharedPreferences("favorites", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (value == 1) {
            // Add the item to favorites
            editor.putBoolean(String.valueOf(accID), true);
            editor.apply();
            addFavoriteFromAccommodation(accID);
        } else if (value == 0) {
            // Remove the item from favorites and tblFavorites
            editor.remove(String.valueOf(accID));
            editor.apply();
            removeFavorite(accID);
            removeFavoriteByAccID(accID);
        }
    }


    private void addFavoriteFromAccommodation(int accID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("tblAccommodations", null, "accID=?", new String[]{String.valueOf(accID)}, null, null, null);

        if (cursor.moveToFirst()) {
            String accName = cursor.getString(2);
            String accImage = cursor.getString(5);

            // Remove existing item with the same accID from tblFavorites
            removeFavoriteByAccID(accID);

            // Add the new item to tblFavorites
            ContentValues cv = new ContentValues();
            cv.put("favName", accName);
            cv.put("favImage", accImage);
            db.insert("tblFavorites", null, cv);
        }

        cursor.close();
    }


    public boolean isFavorite(int accID) {
        SharedPreferences preferences = myContext.getSharedPreferences("favorites", Context.MODE_PRIVATE);
        return preferences.getBoolean(String.valueOf(accID), false);
    }

    public List<AccommodationsModel> searchAccommodations(long townID, String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<AccommodationsModel> data = new ArrayList<>();
        Cursor cursor = null;

        try {
            String selection = "townID=? AND accName LIKE ?";
            String[] selectionArgs = {String.valueOf(townID), "%" + query + "%"};

            cursor = db.query("tblAccommodations", null, selection, selectionArgs, null, null, null);

            while (cursor.moveToNext()) {
                AccommodationsModel spots = new AccommodationsModel(cursor.getInt(0), cursor.getInt(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getInt(6),cursor.getString(7));
                data.add(spots);
            }

            Log.i("DatabaseHelper", "" + data);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "" + e.getLocalizedMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return data;
    }

    public void removeFavorite(int favID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tblFavorites", "favID = ?", new String[]{String.valueOf(favID)});
    }

}