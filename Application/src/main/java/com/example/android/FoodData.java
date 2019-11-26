package com.example.android;

public class FoodData {
    private String foodName;
    private String cat;
    private int price;
    private int quantity;


    public FoodData(String foodName, String cat, int price) {
        this.foodName = foodName;
        this.cat = cat;
        this.price = price;
        this.quantity =0;
    }

    public FoodData()
    {

    }



    public String getCat() {
        return cat;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
