package com.example.android.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.ActionBar;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeStepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeStepActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recipe_image) ImageView imageView;
    @Nullable @BindView(R.id.recipe_detail_container) FrameLayout frameLayout;
    @BindView(R.id.recipe_list) RecyclerView recyclerView;

    private String[] links = {"https://www.recipeboy.com/wp-content/uploads/2016/09/No-Bake-Nutella-Pie.jpg",
            "https://images-gmi-pmc.edge-generalmills.com/c95a0455-70d0-4667-bc17-acfaf2894210.jpg",
            "https://d2gk7xgygi98cy.cloudfront.net/14-3-large.jpg",
            "http://www.tennesseecheesecake.com/assets/images/TC%207-29-166281_10428_NYSTYLECHEESECAKE_CROP.jpg"};

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        ButterKnife.bind(this);
        //Get Extra from Intent
        Intent intent = getIntent();
        List<RecipeObject.Step> stepList = intent.getParcelableArrayListExtra(MainActivity.STEP_EXTRA);
        String recipeName = intent.getStringExtra(MainActivity.NAME_EXTRA);
        int index = intent.getIntExtra(MainActivity.INDEX_EXTRA, 0);
        String imageLink = intent.getStringExtra(MainActivity.IMAGE_EXTRA);

        // Show the Up button in the action bar.
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(recipeName);
        }

        if(!imageLink.isEmpty()){
            Picasso.with(this).load(imageLink).error(R.drawable.ic_broken_image_black_24dp).into(imageView);
        }
        else {
            Picasso.with(this).load(links[index]).error(R.drawable.ic_broken_image_black_24dp).fit().into(imageView);
        }

        if (frameLayout != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            mTwoPane = true;
        }

        assert recyclerView != null;
        recyclerView.setAdapter(new StepViewAdapter(this, stepList, mTwoPane));
    }

    public static class StepViewAdapter
            extends RecyclerView.Adapter<StepViewAdapter.ViewHolder> {

        private final RecipeStepActivity parentActivity;
        private final List<RecipeObject.Step> steps;
        private final boolean twoPane;
        private final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeObject.Step step = (RecipeObject.Step) view.getTag();
                if (twoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(RecipeStepDetailFragment.ARG_ITEM_ID, step);
                    RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
                    fragment.setArguments(arguments);
                    parentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeStepDetailActivity.class);
                    intent.putExtra(RecipeStepDetailFragment.ARG_ITEM_ID, step);
                    context.startActivity(intent);
                }
            }
        };


        StepViewAdapter(RecipeStepActivity parentActivity,
                        List<RecipeObject.Step> steps,
                        boolean twoPane) {
            this.steps = steps;
            this.parentActivity = parentActivity;
            this.twoPane = twoPane;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_content, parent, false);
            return new ViewHolder(view);
        }



        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            String step = "STEP " + steps.get(position).getId();
            holder.mIdView.setText(step);
            holder.mContentView.setText(steps.get(position).getShortDescription());

            holder.itemView.setTag(steps.get(position));
            holder.itemView.setOnClickListener(onClickListener);
        }

        @Override
        public int getItemCount() {
            if(steps == null){
                return 0;
            }
            return steps.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
            }
        }
    }
}
