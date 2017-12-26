package com.example.android.bakingtime.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * DB Contract template for all structure of different tables used.
 */

public class BakingContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.bakingtime";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FOODITEMS="fooditems";
    public static final String PATH_INGREDIENT="ingredients";

    public static final String PATH_STEPS="steps";
    public static final String COMMON_COLUMN_FOODID="foodid";

    public static final class FoodItem implements BaseColumns {

        public static final Uri CONTENT_URI=buildUri(PATH_FOODITEMS);

        public static final String TABLE_NAME="fooditems";

        public static final String COLUMN_FOOD_ID =COMMON_COLUMN_FOODID;
        public static final String COLUMN_FOOD_NAME ="foodname";
        public static final String COLUMN_SERVINGS ="servings";
        public static final String COLUMN_FOODIMAGE="image";


    }

    public static final class Ingredient implements BaseColumns {
        public static final Uri CONTENT_URI=buildUri(PATH_INGREDIENT);

        public static final String TABLE_NAME="ingredients";

        public static final String COLUMN_FOOD_ID =COMMON_COLUMN_FOODID;
        public static final String COLUMN_ING_ID ="ingredientid";
        public static final String COLUMN_QUANTITY ="quantity";
        public static final String COLUMN_MEASURE ="measure";
        public static final String COLUMN_DESC ="desc";

        public static Uri buildIngredientsUri(String foodid){
            return CONTENT_URI.buildUpon().appendPath(foodid).build();
        }

    }

    public static final class Step implements BaseColumns {
        public static final Uri CONTENT_URI=buildUri(PATH_STEPS);

        public static final String TABLE_NAME="steps";

        public static final String COLUMN_FOOD_ID =COMMON_COLUMN_FOODID;
        public static final String COLUMN_STEP_ID ="stepid";
        public static final String COLUMN_SHORTDESC ="shortdesc";
        public static final String COLUMN_LONGDESC ="longdesc";
        public static final String COLUMN_VIDEOURL ="videourl";
        public static final String COLUMN_THUMBNAILURL ="thumburl";



        public static Uri buildStepsUri(String foodid){
            return CONTENT_URI.buildUpon().appendPath(foodid).build();
        }

    }


    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();

        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

}
