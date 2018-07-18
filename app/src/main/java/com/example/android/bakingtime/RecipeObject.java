package com.example.android.bakingtime;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeObject{
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

    public static class Step implements Parcelable{
        private String id;
        private String shortDescription;
        private String description;
        private String videoURL;
        private String thumbnailURL;

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public Step createFromParcel(Parcel in) {
                return new Step(in);
            }

            public Step[] newArray(int size) {
                return new Step[size];
            }
        };

        public Step(Parcel in){
            this.id = in.readString();
            this.shortDescription = in.readString();
            this.description = in.readString();
            this.videoURL = in.readString();
            this.thumbnailURL = in.readString();
        }

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.id);
            parcel.writeString(this.shortDescription);
            parcel.writeString(this.description);
            parcel.writeString(this.videoURL);
            parcel.writeString(this.thumbnailURL);
        }
    }
}
