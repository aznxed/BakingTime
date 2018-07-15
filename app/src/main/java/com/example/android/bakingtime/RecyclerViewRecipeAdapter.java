package com.example.android.bakingtime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class RecyclerViewRecipeAdapter extends RecyclerView.Adapter<RecyclerViewRecipeAdapter.RecyclerViewHolders>{

    private RecipeObject[] recipes;
    private Context context;
    private String[] links = {"https://www.recipeboy.com/wp-content/uploads/2016/09/No-Bake-Nutella-Pie.jpg",
            "https://images-gmi-pmc.edge-generalmills.com/c95a0455-70d0-4667-bc17-acfaf2894210.jpg",
            "https://d2gk7xgygi98cy.cloudfront.net/14-3-large.jpg",
            "http://www.tennesseecheesecake.com/assets/images/TC%207-29-166281_10428_NYSTYLECHEESECAKE_CROP.jpg"};

    public RecyclerViewRecipeAdapter(Context context){
        this.context = context;
    }

    public void setRecipes(RecipeObject[] recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewHolders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_view, null);
        return new RecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolders recyclerViewHolders, int position) {
        recyclerViewHolders.recipeName.setText(recipes[position/2].getName());
        Picasso.with(context).load(links[position/2]).error(R.drawable.ic_broken_image_black_24dp).into(recyclerViewHolders.recipeImage);
    }

    @Override
    public int getItemCount() {
        if(recipes == null){
            return 0;
        }
        return recipes.length*2;
    }

    class RecyclerViewHolders extends RecyclerView.ViewHolder{

        private TextView recipeName;
        private ImageView recipeImage;

        public RecyclerViewHolders(View itemView){
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipe_name);
            recipeImage = itemView.findViewById(R.id.recipe_image);
        }
    }
}
