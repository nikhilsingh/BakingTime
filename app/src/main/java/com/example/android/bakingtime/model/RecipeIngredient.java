package com.example.android.bakingtime.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class to describe all the ingredients required for a particular recipe.
 */

public class RecipeIngredient implements Parcelable {
    int foodItemId;
    int ingredientId;


    double ingredientQuantity;
    String ingredientMeasure;
    String ingredientDesc;

    public RecipeIngredient() {
    }

    protected RecipeIngredient(Parcel in) {
        foodItemId = in.readInt();
        ingredientId = in.readInt();
        ingredientQuantity = in.readDouble();
        ingredientMeasure = in.readString();
        ingredientDesc = in.readString();
    }

    public static final Creator<RecipeIngredient> CREATOR = new Creator<RecipeIngredient>() {
        @Override
        public RecipeIngredient createFromParcel(Parcel in) {
            return new RecipeIngredient(in);
        }

        @Override
        public RecipeIngredient[] newArray(int size) {
            return new RecipeIngredient[size];
        }
    };

    public int getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(int foodItemId) {
        this.foodItemId = foodItemId;
    }

    public double getIngredientQuantity() {
        return ingredientQuantity;
    }

    public void setIngredientQuantity(double ingredientQuantity) {
        this.ingredientQuantity = ingredientQuantity;
    }

    public String getIngredientMeasure() {
        return ingredientMeasure;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public void setIngredientMeasure(String ingredientMeasure) {
        this.ingredientMeasure = ingredientMeasure;
    }

    public String getIngredientDesc() {
        return ingredientDesc;
    }

    public void setIngredientDesc(String ingredientDesc) {
        this.ingredientDesc = ingredientDesc;
    }

    @Override
    public String toString() {
        return "\nFoodId: "+foodItemId+" ingredient desc "+ingredientDesc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(foodItemId);
        dest.writeInt(ingredientId);
        dest.writeDouble(ingredientQuantity);
        dest.writeString(ingredientMeasure);
        dest.writeString(ingredientDesc);
    }
}
