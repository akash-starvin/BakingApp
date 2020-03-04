package com.example.baking.properties;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
    private float quantity;
    private String measure;
    private String ingredient;

    private Ingredient(Parcel in) {
        quantity = in.readFloat();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
    @SuppressLint("DefaultLocale")
    public String getDoseStr(){
        String qty;
        if(quantity == (long) quantity)
            qty = String.format("%d",(long)quantity);
        else
            qty = String.format("%s",quantity);
        return qty  + " " +measure;
    }

    public String getIngredient() {
        return ingredient;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }
}
