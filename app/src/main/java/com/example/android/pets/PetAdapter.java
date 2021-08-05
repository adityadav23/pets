package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.data.PetContract;

public class PetAdapter extends CursorAdapter{
    public PetAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent , false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Getting reference of textView of listItem where we will display name and breed
        TextView nameTextView = view.findViewById(R.id.petNameTextView);
        TextView breedTextView = view.findViewById(R.id.petBreedTextView);



        //Extract the properties from cursor
        String petName = cursor.getString(cursor.getColumnIndexOrThrow(PetContract.PetEntry.COLUMN_PET_NAME));
        String petBreed = cursor.getString(cursor.getColumnIndexOrThrow(PetContract.PetEntry.COLUMN_PET_BREED));

        // If the pet breed is empty string or null, then use some default text
        // that says "Unknown breed", so the TextView isn't blank.
        if (TextUtils.isEmpty(petName)) {
            petName = context.getString(R.string.unknown_name);
        }
        // If the pet breed is empty string or null, then use some default text
        // that says "Unknown breed", so the TextView isn't blank.
        if (TextUtils.isEmpty(petBreed)) {
            petBreed = context.getString(R.string.unknown_breed);
        }
        //Populating data to textview
        nameTextView.setText(petName);
        breedTextView.setText(petBreed);

    }
}
