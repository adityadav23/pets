package com.example.android.pets.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class PetContract {

  private  PetContract(){

    }

    /**
     * Declaring constant authority name, which could be accessed by all cases
     */

      public static final String CONTENT_AUTHORITY = "com.example.android.pets";
    /**
     * Appending authority name with the scheme
     */
     public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);

    /**
     * Name of table
     */
    public static final String PATH_PETS = "pets";





    public static final class PetEntry implements BaseColumns {

        /**
         * complete content_uri
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PETS);

     /**
      * Defining table name and column names
      */

     public final static String TABLE_NAME ="pets";
     public final static String _ID = BaseColumns._ID;
     public final static String COLUMN_PET_NAME ="name";
     public final static String COLUMN_PET_BREED ="breed";
     public final static String COLUMN_PET_GENDER ="gender";
     public final static String COLUMN_PET_WEIGHT ="weight";

        /**
         * Declaring integer  constants for Gender
         */
     public final static int GENDER_UNKNOWN = 0;
     public final static int GENDER_MALE = 1 ;
     public final static int GENDER_FEMALE = 2;


     public static boolean isValidGender(int gender){
         if(gender==GENDER_FEMALE || gender == GENDER_MALE || gender == GENDER_UNKNOWN ) {
             return true;
         }
             return false;

     }

 }

}
