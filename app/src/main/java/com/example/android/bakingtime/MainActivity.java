package com.example.android.bakingtime;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.android.bakingtime.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private LiveData<RecipeObject[]> recipes;
    @BindView(R.id.error_message) TextView errorText;
    @BindView(R.id.recycler_view) ShimmerRecyclerView recipeRecyclerView;
    private RecyclerViewRecipeAdapter recyclerViewRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind the views
        ButterKnife.bind(this);
        recyclerViewRecipeAdapter = new RecyclerViewRecipeAdapter(this);
        recipeRecyclerView.showShimmerAdapter();
        recipeRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), calculateBestSpanCount(300)));
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

    private int calculateBestSpanCount(int imageWidth) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float dpWidth  = outMetrics.widthPixels / outMetrics.density;
        return Math.round(dpWidth / imageWidth);
    }


}
