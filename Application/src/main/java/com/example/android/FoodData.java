package com.example.android;

import java.io.Serializable;

public class FoodData implements Serializable {
    public String foodName;
    public String cat;
    public int price;
    public int quantity;


    public FoodData(String foodName, String cat, int price) {
        this.foodName = foodName;
        this.cat = cat;
        this.price = price;
        this.quantity =0;
    }

    public FoodData()
    {

    }

}
