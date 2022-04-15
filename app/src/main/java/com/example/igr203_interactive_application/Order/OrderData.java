package com.example.igr203_interactive_application.Order;

import org.json.JSONObject;

import java.util.ArrayList;

public class OrderData {
    public int id;
    public int num;
    public JSONObject itemData;
    public ArrayList<Boolean> ingredientList;
}
