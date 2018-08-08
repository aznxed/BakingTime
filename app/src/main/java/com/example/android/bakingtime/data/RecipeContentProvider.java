package com.example.android.bakingtime.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.android.bakingtime.data.RecipeContract.RecipeEntry.TABLE_NAME;

public class RecipeContentProvider extends ContentProvider {

    private RecipeDbHelper recipeDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        recipeDbHelper = new RecipeDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = recipeDbHelper.getReadableDatabase();
        Cursor retCursor = db.query(TABLE_NAME,
                projection, selection, selectionArgs,
                null,null, sortOrder);

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = recipeDbHelper.getWritableDatabase();
        long id = db.insert(TABLE_NAME, null, contentValues);
        Uri returnUri;

        if ( id > 0 ) {
            returnUri = ContentUris.withAppendedId(RecipeContract.RecipeEntry.CONTENT_URI, id);
        } else {
            throw new android.database.SQLException("Failed to insert row into " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = recipeDbHelper.getWritableDatabase();
        return db.delete(TABLE_NAME,"recipeId = ?", selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
