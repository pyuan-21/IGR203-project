package com.example.igr203_interactive_application.Order;

import android.util.Pair;

import java.util.ArrayList;

public class OrderItem {

    private String Name_item;
    private int id; // it's useful, don't delete it!
    private String price;
    private  int image_item;
    private ArrayList<Pair<String, Boolean>> ingredients;
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public OrderItem(String name_item, int id, String price, int image_item, ArrayList<Pair<String, Boolean>> ingredients, int number) {
        Name_item = name_item;
        this.id = id;
        this.price = price;
        this.image_item = image_item;
        this.ingredients = ingredients;
        this.number=number;
    }

    public String getName_item() {
        return Name_item;
    }

    public void setName_item(String name_item) {
        Name_item = name_item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImage_item() {
        return image_item;
    }

    public void setImage_item(int image_item) {
        this.image_item = image_item;
    }

    public ArrayList<Pair<String, Boolean>> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Pair<String, Boolean>> ingredients) {
        this.ingredients = ingredients;
    }
}
