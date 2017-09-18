package com.example.android.bakingtime.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nikhil on 9/8/17.
 */

public class BakeFoodItem implements Parcelable{
    int foodItemId;
    String foodItemName;
    int foodItemServing;

    public BakeFoodItem() {
    }

    protected BakeFoodItem(Parcel in) {

        foodItemId = in.readInt();
        foodItemName = in.readString();
        foodItemServing = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(foodItemId);
        dest.writeString(foodItemName);
        dest.writeInt(foodItemServing);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BakeFoodItem> CREATOR = new Creator<BakeFoodItem>() {
        @Override
        public BakeFoodItem createFromParcel(Parcel in) {
            return new BakeFoodItem(in);
        }

        @Override
        public BakeFoodItem[] newArray(int size) {
            return new BakeFoodItem[size];
        }
    };

    public int getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(int foodItemId) {
        this.foodItemId = foodItemId;
    }

    public String getFoodItemName() {
        return foodItemName;
    }

    public void setFoodItemName(String foodItemName) {
        this.foodItemName = foodItemName;
    }

    public int getFoodItemServing() {
        return foodItemServing;
    }

    public void setFoodItemServing(int foodItemServing) {
        this.foodItemServing = foodItemServing;
    }
}


