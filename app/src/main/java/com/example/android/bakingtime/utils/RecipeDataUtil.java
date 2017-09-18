package com.example.android.bakingtime.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.JsonReader;
import android.util.Log;

import com.example.android.bakingtime.data.BakingContract;
import com.example.android.bakingtime.model.BakeFoodItem;
import com.example.android.bakingtime.model.BakingRecipe;
import com.example.android.bakingtime.model.RecipeIngredient;
import com.example.android.bakingtime.model.RecipeStep;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by nikhil on 10/8/17.
 */

public class RecipeDataUtil {

    public static ArrayList<BakingRecipe> getBakingRecipeObjFromJSON(String jsonString) throws JSONException, IOException {

        ArrayList<BakingRecipe> bakingRecipeArrayList = new ArrayList<>();


        JSONArray recipeData = new JSONArray(jsonString);

        for (int i = 0; i < recipeData.length(); i++) {
            BakingRecipe bakingRecipe = new BakingRecipe();
            BakeFoodItem bakeFoodItem = new BakeFoodItem();


            JSONObject recipeObj = recipeData.getJSONObject(i);
            int foodItemId = recipeObj.getInt("id");
            bakeFoodItem.setFoodItemId(foodItemId);
            bakeFoodItem.setFoodItemName(recipeObj.getString("name"));
            bakeFoodItem.setFoodItemServing(recipeObj.getInt("servings"));


            ArrayList<RecipeIngredient> ingArrayList = new ArrayList<RecipeIngredient>();
            JSONArray ingArray = recipeObj.getJSONArray("ingredients");
            for (int j = 0; j < ingArray.length(); j++) {

                JSONObject ingObject = ingArray.getJSONObject(j);
                RecipeIngredient recipeIngredient = new RecipeIngredient();

                recipeIngredient.setFoodItemId(foodItemId);
                recipeIngredient.setIngredientDesc(ingObject.getString("ingredient"));
                recipeIngredient.setIngredientId(j);
                recipeIngredient.setIngredientMeasure(ingObject.getString("measure"));
                recipeIngredient.setIngredientQuantity(ingObject.getDouble("quantity"));

                ingArrayList.add(recipeIngredient);
            }


            ArrayList<RecipeStep> stepArrayList = new ArrayList<RecipeStep>();
            JSONArray stepArray = recipeObj.getJSONArray("steps");
            for (int k = 0; k < stepArray.length(); k++) {
                RecipeStep recipeStep = new RecipeStep();
                JSONObject stepObj = stepArray.getJSONObject(k);

                recipeStep.setFoodItemId(foodItemId);
                recipeStep.setStepId(stepObj.getInt("id"));
                recipeStep.setStepShortDesc(stepObj.getString("shortDescription"));
                recipeStep.setStepLongDesc(stepObj.getString("description"));
                recipeStep.setStepThumbnailUrl(stepObj.getString("thumbnailURL"));
                recipeStep.setStepVideoUrl(stepObj.getString("videoURL"));

                stepArrayList.add(recipeStep);
            }

            bakingRecipe.setBakeFoodItem(bakeFoodItem);
            bakingRecipe.setRecipeIngList(ingArrayList);
            bakingRecipe.setRecipeStepList(stepArrayList);

            bakingRecipeArrayList.add(bakingRecipe);


        }
        return bakingRecipeArrayList;

        //   JSONObject recipeData = new JSONObject(jsonString);
        //  Log.i("DataUtil",recipeData.toString());


        //return bakingRecipe;
    }


    public static ArrayList<ContentValues[]> getContentValues(String jsonString) throws JSONException {

        ArrayList<ContentValues[]> contentValuesList = new ArrayList<>();


        ArrayList<ContentValues> ingValueList = new ArrayList<>();
        ArrayList<ContentValues> stepValueList = new ArrayList<>();


        JSONArray recipeData = new JSONArray(jsonString);
        ContentValues[] fooditemValuesArray = new ContentValues[recipeData.length()];

        for (int i = 0; i < recipeData.length(); i++) {
            BakingRecipe bakingRecipe = new BakingRecipe();


            BakeFoodItem bakeFoodItem = new BakeFoodItem();


            JSONObject recipeObj = recipeData.getJSONObject(i);
            int foodItemId = recipeObj.getInt("id");


            ContentValues foodItemValue = new ContentValues();
            foodItemValue.put(BakingContract.FoodItem.COLUMN_FOOD_ID, foodItemId);
            foodItemValue.put(BakingContract.FoodItem.COLUMN_FOOD_NAME, recipeObj.getString("name"));
            foodItemValue.put(BakingContract.FoodItem.COLUMN_SERVINGS, recipeObj.getInt("servings"));

            fooditemValuesArray[i] = foodItemValue;


            JSONArray ingArray = recipeObj.getJSONArray("ingredients");
            for (int j = 0; j < ingArray.length(); j++) {
                ContentValues ingredientValue = new ContentValues();


                JSONObject ingObject = ingArray.getJSONObject(j);


                ingredientValue.put(BakingContract.Ingredient.COLUMN_FOOD_ID, foodItemId);
                ingredientValue.put(BakingContract.Ingredient.COLUMN_ING_ID, j);
                ingredientValue.put(BakingContract.Ingredient.COLUMN_DESC, ingObject.getString("ingredient"));
                ingredientValue.put(BakingContract.Ingredient.COLUMN_MEASURE, ingObject.getString("measure"));
                ingredientValue.put(BakingContract.Ingredient.COLUMN_QUANTITY, ingObject.getDouble("quantity"));

                ingValueList.add(ingredientValue);

            }


            JSONArray stepArray = recipeObj.getJSONArray("steps");
            for (int k = 0; k < stepArray.length(); k++) {

                ContentValues stepValue = new ContentValues();

                JSONObject stepObj = stepArray.getJSONObject(k);
                stepValue.put(BakingContract.Step.COLUMN_FOOD_ID, foodItemId);
                stepValue.put(BakingContract.Step.COLUMN_STEP_ID, stepObj.getInt("id"));
                stepValue.put(BakingContract.Step.COLUMN_SHORTDESC, stepObj.getString("shortDescription"));
                stepValue.put(BakingContract.Step.COLUMN_LONGDESC, stepObj.getString("description"));
                stepValue.put(BakingContract.Step.COLUMN_THUMBNAILURL, stepObj.getString("thumbnailURL"));
                stepValue.put(BakingContract.Step.COLUMN_VIDEOURL, stepObj.getString("videoURL"));


                stepValueList.add(stepValue);

            }


        }
        ContentValues[] ingValuesArray = new ContentValues[ingValueList.size()];
        ingValuesArray = ingValueList.toArray(ingValuesArray);


        ContentValues[] stepValuesArray = new ContentValues[stepValueList.size()];
        stepValuesArray = stepValueList.toArray(stepValuesArray);

        contentValuesList.add(fooditemValuesArray);
        contentValuesList.add(ingValuesArray);
        contentValuesList.add(stepValuesArray);

        return contentValuesList;
    }


    public static BakingRecipe getBakingRecipeFromDB(int foodId, Context context) {

        BakingRecipe bakingRecipe = new BakingRecipe();

        bakingRecipe.setBakeFoodItem(getBakeFoodItemFromDB(foodId,context));
        bakingRecipe.setRecipeStepList(getRecipeStepListFromDB(foodId,context));
        bakingRecipe.setRecipeIngList(getRecipeIngredientFromDB(foodId,context));

        return bakingRecipe;
    }


    public static BakeFoodItem getBakeFoodItemFromDB(int foodId, Context context) {
        BakeFoodItem bakeFoodItem = new BakeFoodItem();
        String sFoodId = String.valueOf(foodId);
        String mSelection = BakingContract.COMMON_COLUMN_FOODID + "=?";
        String[] mSelectionArgs = new String[]{sFoodId};
        final Cursor foodItemCursor = context.getContentResolver().query(BakingContract.FoodItem.CONTENT_URI, null, mSelection, mSelectionArgs, null);

        if (foodItemCursor != null && foodItemCursor.getCount() > 0) {
            foodItemCursor.moveToFirst();
            bakeFoodItem.setFoodItemId(foodId);
            int serving = foodItemCursor.getInt(foodItemCursor.getColumnIndex(BakingContract.FoodItem.COLUMN_SERVINGS));
            Log.i("DataUtil","servings"+serving);

            bakeFoodItem.setFoodItemServing(foodItemCursor.getInt(foodItemCursor.getColumnIndex(BakingContract.FoodItem.COLUMN_SERVINGS)));
            bakeFoodItem.setFoodItemName(foodItemCursor.getString(foodItemCursor.getColumnIndex(BakingContract.FoodItem.COLUMN_FOOD_NAME)));
        }


        foodItemCursor.close();

        return bakeFoodItem;
    }

    public static ArrayList<RecipeIngredient> getRecipeIngredientFromDB(int foodId, Context context) {
        ArrayList<RecipeIngredient> recipeIngredientList = new ArrayList<>();
        String sFoodId = String.valueOf(foodId);
        String mSelection = BakingContract.COMMON_COLUMN_FOODID + "=?";
        String[] mSelectionArgs = new String[]{sFoodId};
        final Cursor ingredientCursor = context.getContentResolver().query(BakingContract.Ingredient.CONTENT_URI, null, mSelection, mSelectionArgs, null);

        if (ingredientCursor != null && ingredientCursor.getCount() > 0) {
            ingredientCursor.moveToFirst();
            do {
                RecipeIngredient recipeIngredient = new RecipeIngredient();
                recipeIngredient.setIngredientQuantity(ingredientCursor.getDouble(ingredientCursor.getColumnIndex(BakingContract.Ingredient.COLUMN_QUANTITY)));
                recipeIngredient.setIngredientMeasure(ingredientCursor.getString(ingredientCursor.getColumnIndex(BakingContract.Ingredient.COLUMN_MEASURE)));
                recipeIngredient.setIngredientDesc(ingredientCursor.getString(ingredientCursor.getColumnIndex(BakingContract.Ingredient.COLUMN_DESC)));
                recipeIngredient.setFoodItemId(ingredientCursor.getInt(ingredientCursor.getColumnIndex(BakingContract.Ingredient.COLUMN_FOOD_ID)));
                recipeIngredient.setIngredientId(ingredientCursor.getInt(ingredientCursor.getColumnIndex(BakingContract.Ingredient.COLUMN_ING_ID)));

                recipeIngredientList.add(recipeIngredient);
            } while (ingredientCursor.moveToNext());
        }

        ingredientCursor.close();
        return recipeIngredientList;

    }

    public static ArrayList<RecipeStep> getRecipeStepListFromDB(int foodId, Context context) {
        ArrayList<RecipeStep> recipeStepList = new ArrayList<>();

        String sFoodId = String.valueOf(foodId);
        String mSelection = BakingContract.COMMON_COLUMN_FOODID + "=?";
        String[] mSelectionArgs = new String[]{sFoodId};
        final Cursor stepCursor = context.getContentResolver().query(BakingContract.Step.CONTENT_URI, null, mSelection, mSelectionArgs, null);

        if(stepCursor!=null && stepCursor.getCount()>0){
            stepCursor.moveToFirst();
            do{
                RecipeStep recipeStep = new RecipeStep();

                recipeStep.setStepVideoUrl(stepCursor.getString(stepCursor.getColumnIndex(BakingContract.Step.COLUMN_VIDEOURL)));
                recipeStep.setStepId(stepCursor.getInt(stepCursor.getColumnIndex(BakingContract.Step.COLUMN_STEP_ID)));
                recipeStep.setFoodItemId(stepCursor.getInt(stepCursor.getColumnIndex(BakingContract.Step.COLUMN_FOOD_ID)));
                //recipeStep.setStepThumbnailUrl();
                recipeStep.setStepShortDesc(stepCursor.getString(stepCursor.getColumnIndex(BakingContract.Step.COLUMN_SHORTDESC)));
                recipeStep.setStepLongDesc(stepCursor.getString(stepCursor.getColumnIndex(BakingContract.Step.COLUMN_LONGDESC)));



                recipeStepList.add(recipeStep);




            }while (stepCursor.moveToNext());
        }

        stepCursor.close();
        return recipeStepList;
    }

}
