package com.example.android.bakingtime;

public class RecipeObject {
    private String id;
    private String name;
    private Ingredient[] ingredients;
    private Step[] steps;

    public RecipeObject(String id, String name, Ingredient[] ingredients, Step[] steps){
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public Step[] getSteps() {
        return steps;
    }


    public static class Ingredient {
        private String quantity;
        private String measure;
        private String ingredient;

        public Ingredient(String quantity, String measure, String ingredient){
            this.quantity = quantity;
            this.measure = measure;
            this.ingredient = ingredient;
        }

        public String getIngredient() {
            return ingredient;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getMeasure() {
            return measure;
        }
    }

    public static class Step {
        private String id;
        private String shortDescription;
        private String description;
        private String videoURL;
        private String thumbnailURL;

        public Step(String id, String shortDescription, String description, String videoURL, String thumbnailURL){
            this.id = id;
            this.shortDescription = shortDescription;
            this.description = description;
            this.videoURL = videoURL;
            this.thumbnailURL = thumbnailURL;
        }

        public String getId() {
            return id;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public String getDescription() {
            return description;
        }

        public String getVideoURL() {
            return videoURL;
        }

        public String getThumbnailURL() {
            return thumbnailURL;
        }
    }
}
