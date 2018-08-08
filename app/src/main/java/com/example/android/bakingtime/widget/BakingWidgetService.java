package com.example.android.bakingtime.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.data.RecipeContract;

public class BakingWidgetService extends RemoteViewsService {
    Cursor recipeData;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingWidgetItemFactory(getApplicationContext(), intent);
    }

    class BakingWidgetItemFactory implements RemoteViewsFactory{
        private Context context;
        private int appWidgetId;

        BakingWidgetItemFactory(Context context, Intent intent){
            this.context = context;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if(recipeData != null){
                recipeData.close();
            }
            recipeData = context.getContentResolver().query(RecipeContract.RecipeEntry.CONTENT_URI, null, null, null, null);
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if(recipeData == null){
                return 0;
            }
            return recipeData.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if(recipeData == null || recipeData.getCount() == 0){
                return null;
            }
            recipeData.moveToPosition(position);
            int recipeIndex = recipeData.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INGREDIENTS);
            int recipeNameIndex = recipeData.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_NAME);
            String recipeString = recipeData.getString(recipeIndex);
            String recipeName = recipeData.getString(recipeNameIndex);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
            remoteViews.setTextViewText(R.id.widget_item_text, recipeString);
            remoteViews.setTextViewText(R.id.widget_title, recipeName);
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
