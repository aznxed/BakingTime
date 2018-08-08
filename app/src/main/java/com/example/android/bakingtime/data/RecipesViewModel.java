package com.example.android.bakingtime.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.bakingtime.utils.RecipeObject;
import com.example.android.bakingtime.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class RecipesViewModel extends AndroidViewModel {

    private MutableLiveData<RecipeObject[]> recipes;

    public RecipesViewModel(@NonNull Application application) {
        super(application);
        recipes = new MutableLiveData<>();
        new RecipeQueryTask().execute(NetworkUtils.buildUrl());
    }

    public LiveData<RecipeObject[]> getRecipes(){
        return recipes;
    }

    private class RecipeQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String recipeJSON = null;
            try {
                recipeJSON = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return recipeJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            RecipeObject[] recipeObjects = NetworkUtils.parseJSON(s);
            recipes.setValue(recipeObjects);
        }
    }
}
