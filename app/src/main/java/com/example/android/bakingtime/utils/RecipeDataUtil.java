package com.example.android.bakingtime.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import android.util.Log;

import com.example.android.bakingtime.data.BakingContract;
import com.example.android.bakingtime.model.BakeFoodItem;
import com.example.android.bakingtime.model.BakingRecipe;
import com.example.android.bakingtime.model.RecipeIngredient;
import com.example.android.bakingtime.model.RecipeStep;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/**
 * Util class to handle processing of different data related to Baking Recipe
 */

public class RecipeDataUtil {
    public static final String TAG = "RecipeDataUtil";

    private static final String JSONKEY_FOODID = "id";
    private static final String JSONKEY_FOODNAME = "name";
    private static final String JSONKEY_SERVINGS = "servings";
    private static final String JSONKEY_FOODIMAGE="image";
    private static final String JSONKEY_INGREDIENTS = "ingredients";
    private static final String JSONKEY_INGREDIENT = "ingredient";
    private static final String JSONKEY_INGREDIENT_MEASURE = "measure";
    private static final String JSONKEY_INGREDIENT_QUANTITY = "quantity";
    private static final String JSONKEY_STEPS = "steps";
    private static final String JSONKEY_STEPS_ID = "id";
    private static final String JSONKEY_STEPS_SHORTDESC = "shortDescription";
    private static final String JSONKEY_STEPS_DESCRIPTION = "description";
    private static final String JSONKEY_STEPS_THUMBNAILURL = "thumbnailURL";
    private static final String JSONKEY_STEPS_VIDEOURL = "videoURL";


    public static ArrayList<ContentValues[]> getContentValues(String jsonString) throws JSONException {

        ArrayList<ContentValues[]> contentValuesList = new ArrayList<>();


        ArrayList<ContentValues> ingValueList = new ArrayList<>();
        ArrayList<ContentValues> stepValueList = new ArrayList<>();


        JSONArray recipeData = new JSONArray(jsonString);
        ContentValues[] fooditemValuesArray = new ContentValues[recipeData.length()];

        for (int i = 0; i < recipeData.length(); i++) {

            JSONObject recipeObj = recipeData.getJSONObject(i);
            int foodItemId = recipeObj.getInt(JSONKEY_FOODID);


            ContentValues foodItemValue = new ContentValues();
            foodItemValue.put(BakingContract.FoodItem.COLUMN_FOOD_ID, foodItemId);
            foodItemValue.put(BakingContract.FoodItem.COLUMN_FOOD_NAME, recipeObj.getString(JSONKEY_FOODNAME));
            foodItemValue.put(BakingContract.FoodItem.COLUMN_SERVINGS, recipeObj.getInt(JSONKEY_SERVINGS));

            String foodImg=recipeObj.getString(JSONKEY_FOODIMAGE);

            foodItemValue.put(BakingContract.FoodItem.COLUMN_FOODIMAGE,foodImg);

            fooditemValuesArray[i] = foodItemValue;


            JSONArray ingArray = recipeObj.getJSONArray(JSONKEY_INGREDIENTS);
            for (int j = 0; j < ingArray.length(); j++) {
                ContentValues ingredientValue = new ContentValues();


                JSONObject ingObject = ingArray.getJSONObject(j);


                ingredientValue.put(BakingContract.Ingredient.COLUMN_FOOD_ID, foodItemId);
                ingredientValue.put(BakingContract.Ingredient.COLUMN_ING_ID, j);
                ingredientValue.put(BakingContract.Ingredient.COLUMN_DESC, ingObject.getString(JSONKEY_INGREDIENT));
                ingredientValue.put(BakingContract.Ingredient.COLUMN_MEASURE, ingObject.getString(JSONKEY_INGREDIENT_MEASURE));
                ingredientValue.put(BakingContract.Ingredient.COLUMN_QUANTITY, ingObject.getDouble(JSONKEY_INGREDIENT_QUANTITY));

                ingValueList.add(ingredientValue);

            }


            JSONArray stepArray = recipeObj.getJSONArray(JSONKEY_STEPS);
            for (int k = 0; k < stepArray.length(); k++) {

                ContentValues stepValue = new ContentValues();

                JSONObject stepObj = stepArray.getJSONObject(k);
                stepValue.put(BakingContract.Step.COLUMN_FOOD_ID, foodItemId);
                stepValue.put(BakingContract.Step.COLUMN_STEP_ID, stepObj.getInt(JSONKEY_STEPS_ID));
                stepValue.put(BakingContract.Step.COLUMN_SHORTDESC, stepObj.getString(JSONKEY_STEPS_SHORTDESC));
                stepValue.put(BakingContract.Step.COLUMN_LONGDESC, stepObj.getString(JSONKEY_STEPS_DESCRIPTION));

                String thumbUrl = stepObj.getString(JSONKEY_STEPS_THUMBNAILURL);
                String videoUrl = stepObj.getString(JSONKEY_STEPS_VIDEOURL);

                if (!TextUtils.isEmpty(thumbUrl) && thumbUrl.endsWith(".mp4") && TextUtils.isEmpty(videoUrl)) {
                  thumbUrl="";
                }



                stepValue.put(BakingContract.Step.COLUMN_THUMBNAILURL, thumbUrl);
                stepValue.put(BakingContract.Step.COLUMN_VIDEOURL, videoUrl);

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
        Log.i(TAG, "getBakingRecipeFromDB starts. Food Id " + foodId);
        BakingRecipe bakingRecipe = new BakingRecipe();
        bakingRecipe.setBakeFoodItem(getBakeFoodItemFromDB(foodId, context));
        bakingRecipe.setRecipeStepList(getRecipeStepListFromDB(foodId, context));
        bakingRecipe.setRecipeIngList(getRecipeIngredientFromDB(foodId, context));

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

            bakeFoodItem.setFoodItemServing(foodItemCursor.getInt(foodItemCursor.getColumnIndex(BakingContract.FoodItem.COLUMN_SERVINGS)));
            bakeFoodItem.setFoodItemName(foodItemCursor.getString(foodItemCursor.getColumnIndex(BakingContract.FoodItem.COLUMN_FOOD_NAME)));
            bakeFoodItem.setFoodImg(foodItemCursor.getString(foodItemCursor.getColumnIndex(BakingContract.FoodItem.COLUMN_FOODIMAGE)));
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

        if (stepCursor != null && stepCursor.getCount() > 0) {
            stepCursor.moveToFirst();
            do {
                RecipeStep recipeStep = new RecipeStep();

                recipeStep.setStepVideoUrl(stepCursor.getString(stepCursor.getColumnIndex(BakingContract.Step.COLUMN_VIDEOURL)));
                recipeStep.setStepId(stepCursor.getInt(stepCursor.getColumnIndex(BakingContract.Step.COLUMN_STEP_ID)));
                recipeStep.setFoodItemId(stepCursor.getInt(stepCursor.getColumnIndex(BakingContract.Step.COLUMN_FOOD_ID)));
                recipeStep.setStepThumbnailUrl(stepCursor.getString(stepCursor.getColumnIndex(BakingContract.Step.COLUMN_THUMBNAILURL)));
                recipeStep.setStepShortDesc(stepCursor.getString(stepCursor.getColumnIndex(BakingContract.Step.COLUMN_SHORTDESC)));
                recipeStep.setStepLongDesc(stepCursor.getString(stepCursor.getColumnIndex(BakingContract.Step.COLUMN_LONGDESC)));


                recipeStepList.add(recipeStep);


            } while (stepCursor.moveToNext());
        }

        stepCursor.close();
        return recipeStepList;
    }

    public static ArrayList<BakeFoodItem> getAllFoodItemList(Cursor data) {
        ArrayList<BakeFoodItem> bakeFoodItemArrayList = new ArrayList<>();

        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                BakeFoodItem bakeFoodItem = new BakeFoodItem();

                bakeFoodItem.setFoodItemId(data.getInt(data.getColumnIndex(BakingContract.FoodItem.COLUMN_FOOD_ID)));
                bakeFoodItem.setFoodItemName(data.getString(data.getColumnIndex(BakingContract.FoodItem.COLUMN_FOOD_NAME)));
                bakeFoodItem.setFoodImg(data.getString(data.getColumnIndex(BakingContract.FoodItem.COLUMN_FOODIMAGE)));

                bakeFoodItemArrayList.add(bakeFoodItem);


            } while (data.moveToNext());
        }


        return bakeFoodItemArrayList;

    }

    public static ArrayList<RecipeIngredient> getIngListForDetailIndex(BakingRecipe bakingRecipe) {

        return bakingRecipe.getRecipeIngList();
    }

    public static RecipeStep getStepForDetailIndex(int detailIndex, BakingRecipe bakingRecipe) {
        return bakingRecipe.getRecipeStepList().get(detailIndex - 1);
    }


    public static ArrayList<Integer> getAllFoodIdList(Set<String> stringSet) {
        ArrayList<Integer> mAllFoodIdList = new ArrayList<>();

        for (String s : stringSet) {
            mAllFoodIdList.add(Integer.valueOf(s));
        }
        Collections.sort(mAllFoodIdList);
        return mAllFoodIdList;
    }
/*
    public static ArrayList<BakingRecipe> getBakingRecipeObjFromJSON(String jsonString) throws JSONException, IOException {

        ArrayList<BakingRecipe> bakingRecipeArrayList = new ArrayList<>();


        JSONArray recipeData = new JSONArray(jsonString);

        for (int i = 0; i < recipeData.length(); i++) {
            BakingRecipe bakingRecipe = new BakingRecipe();
            BakeFoodItem bakeFoodItem = new BakeFoodItem();


            JSONObject recipeObj = recipeData.getJSONObject(i);
            int foodItemId = recipeObj.getInt(JSONKEY_FOODID);
            bakeFoodItem.setFoodItemId(foodItemId);
            bakeFoodItem.setFoodItemName(recipeObj.getString(JSONKEY_FOODNAME));
            bakeFoodItem.setFoodItemServing(recipeObj.getInt(JSONKEY_SERVINGS));


            ArrayList<RecipeIngredient> ingArrayList = new ArrayList<RecipeIngredient>();
            JSONArray ingArray = recipeObj.getJSONArray(JSONKEY_INGREDIENTS);
            for (int j = 0; j < ingArray.length(); j++) {

                JSONObject ingObject = ingArray.getJSONObject(j);
                RecipeIngredient recipeIngredient = new RecipeIngredient();

                recipeIngredient.setFoodItemId(foodItemId);
                recipeIngredient.setIngredientDesc(ingObject.getString(JSONKEY_INGREDIENT));
                recipeIngredient.setIngredientId(j);
                recipeIngredient.setIngredientMeasure(ingObject.getString(JSONKEY_INGREDIENT_MEASURE));
                recipeIngredient.setIngredientQuantity(ingObject.getDouble(JSONKEY_INGREDIENT_QUANTITY));

                ingArrayList.add(recipeIngredient);
            }


            ArrayList<RecipeStep> stepArrayList = new ArrayList<RecipeStep>();
            JSONArray stepArray = recipeObj.getJSONArray(JSONKEY_STEPS);
            for (int k = 0; k < stepArray.length(); k++) {
                RecipeStep recipeStep = new RecipeStep();
                JSONObject stepObj = stepArray.getJSONObject(k);

                recipeStep.setFoodItemId(foodItemId);
                recipeStep.setStepId(stepObj.getInt(JSONKEY_STEPS_ID));
                recipeStep.setStepShortDesc(stepObj.getString(JSONKEY_STEPS_SHORTDESC));
                recipeStep.setStepLongDesc(stepObj.getString(JSONKEY_STEPS_DESCRIPTION));
                recipeStep.setStepThumbnailUrl(stepObj.getString(JSONKEY_STEPS_THUMBNAILURL));
                recipeStep.setStepVideoUrl(stepObj.getString(JSONKEY_STEPS_VIDEOURL));

                stepArrayList.add(recipeStep);
            }

            bakingRecipe.setBakeFoodItem(bakeFoodItem);
            bakingRecipe.setRecipeIngList(ingArrayList);
            bakingRecipe.setRecipeStepList(stepArrayList);

            bakingRecipeArrayList.add(bakingRecipe);


        }
        return bakingRecipeArrayList;

    }
    */
}
