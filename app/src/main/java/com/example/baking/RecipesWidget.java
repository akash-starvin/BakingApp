package com.example.baking;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import static android.content.Context.MODE_PRIVATE;

public class RecipesWidget extends AppWidgetProvider {

    SharedPreferences sharedPreferences;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String recipeName, String ingredients) {

        RemoteViews views = new RemoteViews( context.getPackageName(), R.layout.recipes_widget );
        views.setTextViewText(R.id.appwidget_header, recipeName);
        views.setTextViewText(R.id.appwidget_content, ingredients);

        appWidgetManager.updateAppWidget( appWidgetId, views );
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCE_TAG, MODE_PRIVATE);
        String recipeName = sharedPreferences.getString(Constants.SHARED_PREFERENCE_RECIPE_NAME, "No recipe found");
        String ingredients = sharedPreferences.getString(Constants.SHARED_PREFERENCE_RECIPE_INGREDIENTS, "Add recipe to widget");

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget( context, appWidgetManager, appWidgetId, recipeName, ingredients );
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {

    }
}

