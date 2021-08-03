package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import com.example.android.pets.data.PetContract.PetEntry;

import java.nio.channels.SelectableChannel;


public class PetProvider extends ContentProvider {

    /**
     * getting object of the PetDbHelper , which return the instance of database.
     */
    private PetDbHelper mPetdbHelper ;

    /**
     *  final constants for table pets and specific row queries
     * @return
     */
     private  static final int PETS = 100;
     private static final int PETS_ID = 101;

    /**
     *  Declaring UriMatcher
     */
    private static final UriMatcher sUriMatcher  = new UriMatcher(UriMatcher.NO_MATCH);

    /**
     * Declaring valid URIS
     * @return
     */

    static{
        // valid uri for table pets
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS, PETS);

        //valid uri for specific row in pets table
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS + "/#", PETS_ID);

    }


    public boolean onCreate() {
        mPetdbHelper = new PetDbHelper(getContext());
        return false;
    }



    public Cursor query( Uri uri,  String[] projection,  String selection,
                         String[] selectionArgs,  String sortOrder) {
        //getting readable instance of database
        SQLiteDatabase db =  mPetdbHelper.getReadableDatabase();
        Cursor cursor = null;
        int match = sUriMatcher.match(uri);

        switch(match){
            case PETS:     cursor = db.query(PetEntry.TABLE_NAME, projection, selection, selectionArgs,null, null,
                                            sortOrder);
                break;
            case PETS_ID:  return  QueryPet(db ,cursor ,uri, projection , selection , selectionArgs , sortOrder);


        }
        return cursor;


    }
     private Cursor QueryPet( SQLiteDatabase db, Cursor cursor,Uri uri,  String[] projection,  String selection,
                              String[] selectionArgs,  String sortOrder){


         selection = PetEntry._ID + "=?";
         selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri)) } ;
         cursor = db.query(PetEntry.TABLE_NAME,projection, selection, selectionArgs,null,
                 null, sortOrder);
        return cursor;
     }


    public String getType( Uri uri) {
        return null;
    }



    public Uri insert( Uri uri,  ContentValues values) {
        //Matching Uri with UriMatcher
        final int match = sUriMatcher.match(uri);
        // Here we  have only one case to insert data at end of table
        switch (match){
            case PETS:
                return InsertPet(uri, values);

            default:
                try {
                    throw new IllegalAccessException("Insertion is not supported for "+ uri);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

        }
            return null;

    }

    private Uri InsertPet(Uri uri , ContentValues contentValues){
        SQLiteDatabase db = mPetdbHelper.getWritableDatabase();
        // insert new row in database table pet
        long id = db.insert(PetEntry.TABLE_NAME, null , contentValues);

        if(id==-1){
            Toast.makeText(getContext(), "Failed to insert row for :" + uri, Toast.LENGTH_SHORT).show();
            return null;
        }

        // if successful to add row
        return ContentUris.withAppendedId(uri, id);
    }

    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        return 0;
    }


    public int update( Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        return 0;
    }
}
