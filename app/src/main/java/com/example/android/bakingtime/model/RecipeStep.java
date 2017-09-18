package com.example.android.bakingtime.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nikhil on 9/8/17.
 */

public class RecipeStep implements Parcelable {
    int foodItemId;
    int stepId;
    String stepShortDesc;
    String stepLongDesc;
    String stepVideoUrl;
    String stepThumbnailUrl;

    public RecipeStep() {
    }

    protected RecipeStep(Parcel in) {
        foodItemId = in.readInt();
        stepId = in.readInt();
        stepShortDesc = in.readString();
        stepLongDesc = in.readString();
        stepVideoUrl = in.readString();
        stepThumbnailUrl = in.readString();
    }

    public static final Creator<RecipeStep> CREATOR = new Creator<RecipeStep>() {
        @Override
        public RecipeStep createFromParcel(Parcel in) {
            return new RecipeStep(in);
        }

        @Override
        public RecipeStep[] newArray(int size) {
            return new RecipeStep[size];
        }
    };

    public int getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(int foodItemId) {
        this.foodItemId = foodItemId;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public String getStepShortDesc() {
        return stepShortDesc;
    }

    public void setStepShortDesc(String stepShortDesc) {
        this.stepShortDesc = stepShortDesc;
    }

    public String getStepLongDesc() {
        return stepLongDesc;
    }

    public void setStepLongDesc(String stepLongDesc) {
        this.stepLongDesc = stepLongDesc;
    }

    public String getStepVideoUrl() {
        return stepVideoUrl;
    }

    public void setStepVideoUrl(String stepVideoUrl) {
        this.stepVideoUrl = stepVideoUrl;
    }

    public String getStepThumbnailUrl() {
        return stepThumbnailUrl;
    }

    public void setStepThumbnailUrl(String stepThumbnailUrl) {
        this.stepThumbnailUrl = stepThumbnailUrl;
    }

    @Override
    public String toString() {
        return new String("\nFood Id "+foodItemId+"Desc : "+stepShortDesc+"Step Id : "+stepId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(foodItemId);
        dest.writeInt(stepId);
        dest.writeString(stepShortDesc);
        dest.writeString(stepLongDesc);
        dest.writeString(stepVideoUrl);
        dest.writeString(stepThumbnailUrl);
    }
}
