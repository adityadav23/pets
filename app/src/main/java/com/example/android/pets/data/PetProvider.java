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

        //Setting notificationUri on the using cursor
        cursor.setNotificationUri(getContext().getContentResolver() , uri);
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
                    throw new IllegalArgumentException("Insertion is not supported for "+ uri);
        }

    }

    private Uri InsertPet(Uri uri , ContentValues contentValues){

//        //Data validation for pet name
//        String petName = contentValues.getAsString(PetEntry.COLUMN_PET_NAME);
//        if(petName.isEmpty()){
//             throw new IllegalArgumentException("Pet requires a name");
//        }


        // Data validation for  valid gender
        Integer petGender = contentValues.getAsInteger(PetEntry.COLUMN_PET_GENDER);
        if( !PetEntry.isValidGender(petGender)){
            throw new IllegalArgumentException("gender not specified");
        }
        // Data validation for weight, checking for null and positive value
        Integer petWeight = contentValues.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
        if(petWeight == null || petWeight < 0){
            throw new IllegalArgumentException("enter correct weight");
        }

        // Getting reference to writable database
        SQLiteDatabase db = mPetdbHelper.getWritableDatabase();
        // insert new row in database table pet
        long id = db.insert(PetEntry.TABLE_NAME, null , contentValues);

        if(id==-1){
            Toast.makeText(getContext(), "Failed to insert row for :" + uri, Toast.LENGTH_SHORT).show();
            return null;
        }

        //sending notification if any new entry is made
        getContext().getContentResolver().notifyChange(uri,  null);
        // if successful to add row
        return ContentUris.withAppendedId(uri, id);
    }

    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        SQLiteDatabase db = mPetdbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                //sending notification if any new entry is made
                getContext().getContentResolver().notifyChange(uri,  null);
                return db.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
            case PETS_ID:
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                //sending notification if any new entry is made
                getContext().getContentResolver().notifyChange(uri,  null);
                return db.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("delete not supported for uri: " + uri);



        }

    }


    public int update( Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        switch(match){
            case PETS:
                 return updatePet(uri,values, selection , selectionArgs);
            case PETS_ID:
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri,values, selection , selectionArgs);
            default:
                throw new IllegalArgumentException("update not supported for uri: "+ uri);

        }


    }
    private int updatePet( Uri uri,ContentValues contentValues,  String selection,  String[] selectionArgs){
//        //Data validation for pet name
//        String petName = contentValues.getAsString(PetEntry.COLUMN_PET_NAME);
//        if(petName.isEmpty()){
//            throw new IllegalArgumentException("Pet requires a name");
//        }


        // Data validation for  valid gender
        Integer petGender = contentValues.getAsInteger(PetEntry.COLUMN_PET_GENDER);
        if( !PetEntry.isValidGender(petGender)){
            throw new IllegalArgumentException("gender not specified");
        }
        // Data validation for weight, checking for null and positive value
        Integer petWeight = contentValues.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
        if(petWeight == null || petWeight < 0){
            throw new IllegalArgumentException("enter correct weight");
        }
        /**
         * If there is no value to update. then don't update database
         */

        if(contentValues.size()==0){
            return 0;
        }
        //SQLiteDatabase writable reference
        SQLiteDatabase db = mPetdbHelper.getWritableDatabase();

        //sending notification if any new entry is made
        getContext().getContentResolver().notifyChange(uri,  null);

        return db.update(PetEntry.TABLE_NAME, contentValues, selection, selectionArgs);
    }
}
