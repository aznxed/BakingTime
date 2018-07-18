package com.example.android.bakingtime;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.android.bakingtime.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements RecyclerViewRecipeAdapter.ListItemClickListener{

    @BindView(R.id.error_message) TextView errorText;
    @BindView(R.id.recycler_view) ShimmerRecyclerView recipeRecyclerView;
    private RecyclerViewRecipeAdapter recyclerViewRecipeAdapter;
    private RecyclerViewRecipeAdapter.ListItemClickListener listClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind the views
        ButterKnife.bind(this);
        listClickListener = this;
        recyclerViewRecipeAdapter = new RecyclerViewRecipeAdapter(this, listClickListener);
        recipeRecyclerView.showShimmerAdapter();
        recipeRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(calculateBestSpanCount(), StaggeredGridLayoutManager.VERTICAL));
        recipeRecyclerView.setAdapter(recyclerViewRecipeAdapter);
        recipeRecyclerView.setHasFixedSize(true);

        if(NetworkUtils.isConnected(this)){
            RecipesViewModel viewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
            viewModel.getRecipes().observe(this, new Observer<RecipeObject[]>() {
                @Override
                public void onChanged(@Nullable RecipeObject[] recipeObjects) {
                    recyclerViewRecipeAdapter.setRecipes(recipeObjects);
                }
            });

            recipeRecyclerView.hideShimmerAdapter();
        }
        else{
            errorText.setVisibility(View.VISIBLE);
        }
    }

    private int calculateBestSpanCount() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float dpWidth  = outMetrics.widthPixels / outMetrics.density;
        return Math.round(dpWidth / 300);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        //Prepare Extras
        RecipesViewModel viewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
        RecipeObject[] recipeObjects = viewModel.getRecipes().getValue();
        RecipeObject.Step[] steps = recipeObjects[clickedItemIndex].getSteps();
        List<RecipeObject.Step> stepArray = new ArrayList<>(Arrays.asList(steps));

        //Put extras and start intent
        Intent recipeDetail = new Intent(this, RecipeStepActivity.class);
        recipeDetail.putParcelableArrayListExtra("steps", (ArrayList)stepArray);
        startActivity(recipeDetail);
    }
}
