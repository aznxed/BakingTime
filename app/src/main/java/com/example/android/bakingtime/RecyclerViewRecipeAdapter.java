package com.example.android.bakingtime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alespero.expandablecardview.ExpandableCardView;
import com.squareup.picasso.Picasso;

public class RecyclerViewRecipeAdapter extends RecyclerView.Adapter<RecyclerViewRecipeAdapter.RecyclerViewHolders>{

    private RecipeObject[] recipes;
    private Context context;
    final private ListItemClickListener clickListener;
    private String[] links = {"https://www.recipeboy.com/wp-content/uploads/2016/09/No-Bake-Nutella-Pie.jpg",
            "https://images-gmi-pmc.edge-generalmills.com/c95a0455-70d0-4667-bc17-acfaf2894210.jpg",
            "https://d2gk7xgygi98cy.cloudfront.net/14-3-large.jpg",
            "http://www.tennesseecheesecake.com/assets/images/TC%207-29-166281_10428_NYSTYLECHEESECAKE_CROP.jpg"};

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public RecyclerViewRecipeAdapter(Context context, ListItemClickListener clickListener){
        this.context = context;
        this.clickListener = clickListener;
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
        recyclerViewHolders.recipeName.setTitle(recipes[position].getName());
        RecipeObject.Ingredient[] ingredients = recipes[position].getIngredients();
        StringBuilder ingredientsList = new StringBuilder();

        for(RecipeObject.Ingredient ingredient: ingredients){
            ingredientsList.append("- ");
            ingredientsList.append(ingredient.getQuantity());
            ingredientsList.append(" ");
            ingredientsList.append(ingredient.getMeasure());
            ingredientsList.append(" ");
            ingredientsList.append(ingredient.getIngredient());
            ingredientsList.append("\n");
        }

        recyclerViewHolders.ingredients.setText(ingredientsList.toString());
        Picasso.with(context).load(links[position]).error(R.drawable.ic_broken_image_black_24dp).into(recyclerViewHolders.recipeImage);

    }

    @Override
    public int getItemCount() {
        if(recipes == null){
            return 0;
        }
        return recipes.length;
    }

    class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView recipeImage;
        private TextView ingredients;
        private ExpandableCardView recipeName;

        public RecyclerViewHolders(View itemView){
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipe_name);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            ingredients = itemView.findViewById(R.id.ingredients);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            clickListener.onListItemClick(clickedPosition);
        }
    }
}
