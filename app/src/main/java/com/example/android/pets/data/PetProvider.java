package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;



public class PetProvider extends ContentProvider {

    private PetDbHelper mPetdbHelper ;
    public boolean onCreate() {
        mPetdbHelper = new PetDbHelper(getContext());
        return false;
    }



    public Cursor query( Uri uri,  String[] projection,  String selection,  String[] selectionArgs,  String sortOrder) {
        return null;
    }



    public String getType( Uri uri) {
        return null;
    }



    public Uri insert( Uri uri,  ContentValues values) {
        return null;
    }


    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        return 0;
    }


    public int update( Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        return 0;
    }
}
