package com.example.android.bakingtime.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by nikhil on 10/8/17.
 */

public class BakingRecipe implements Parcelable {
    BakeFoodItem bakeFoodItem;
    ArrayList<RecipeIngredient> recipeIngredientList;
    ArrayList<RecipeStep> recipeStepList;

    public BakingRecipe() {
        recipeIngredientList=new ArrayList<>();
        recipeStepList=new ArrayList<>();
    }

    protected BakingRecipe(Parcel in) {
        this();

        bakeFoodItem = in.readParcelable(BakingRecipe.class.getClassLoader());
        in.readTypedList(recipeIngredientList, RecipeIngredient.CREATOR);
        in.readTypedList(recipeStepList, RecipeStep.CREATOR);


    }

    public static final Creator<BakingRecipe> CREATOR = new Creator<BakingRecipe>() {
        @Override
        public BakingRecipe createFromParcel(Parcel in) {
            return new BakingRecipe(in);
        }

        @Override
        public BakingRecipe[] newArray(int size) {
            return new BakingRecipe[size];
        }
    };

    public BakeFoodItem getBakeFoodItem() {
        return bakeFoodItem;
    }

    public void setBakeFoodItem(BakeFoodItem bakeFoodItem) {
        this.bakeFoodItem = bakeFoodItem;
    }

    public ArrayList<RecipeIngredient> getRecipeIngList() {
        return recipeIngredientList;
    }

    public void setRecipeIngList(ArrayList<RecipeIngredient> recipeIngList) {
        this.recipeIngredientList = recipeIngList;
    }

    public ArrayList<RecipeStep> getRecipeStepList() {
        return recipeStepList;
    }

    public void setRecipeStepList(ArrayList<RecipeStep> recipeStepList) {
        this.recipeStepList = recipeStepList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable(bakeFoodItem, flags);

        dest.writeTypedList(recipeIngredientList);
        dest.writeTypedList(recipeStepList);

    }

    @Override
    public String toString() {
        return recipeStepList.toString()+recipeIngredientList.toString();

    }
}
