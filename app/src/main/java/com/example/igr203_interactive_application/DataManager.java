package com.example.igr203_interactive_application;

import static com.example.igr203_interactive_application.R.*;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

enum NavigatorType{
    FOOD,
    DRINKS,
    SNACK,
    DESSERT
}

public class DataManager {
    private static volatile DataManager instance = null;
    public static DataManager getInstance() {
        if (instance == null) {
            synchronized(DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();
                }
            }
        }
        return instance;
    }

    private DataManager(){
        initPicMap();
    }

    public JSONObject loadJSONFromAsset(AppCompatActivity activity, String fileName) {
        JSONObject jsonObj = null;
        try {
            InputStream is = activity.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonStr = new String(buffer, "UTF-8");
            jsonObj = new JSONObject(jsonStr);
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return jsonObj;
    }
    private Map<String, Integer> picMap = new HashMap<String, Integer>();
    private void initPicMap(){
        // for navigator
        picMap.put("burger", drawable.burger);
        picMap.put("frites", drawable.frites);
        picMap.put("drink2", drawable.drink2);
        picMap.put("donut", drawable.donut);

        // for food
        picMap.put("vegetarianpizza", drawable.vegetarianpizza);
        picMap.put("pepperonipizza", drawable.pepperonipizza);
        picMap.put("chickenburge", drawable.chickenburge);
        picMap.put("beefburger", drawable.beefburger);
        picMap.put("pestopasta", drawable.pestopasta);
        picMap.put("spaghettibolognese", drawable.spaghettibolognese);

        // for drinks
        picMap.put("heineken", drawable.heineken);
        picMap.put("orangejuice", drawable.orangejuice);
        picMap.put("icetea", drawable.icetea);
        picMap.put("pineapplejuice", drawable.pineapplejuice);
        picMap.put("coffee", drawable.coffee);
        picMap.put("tea", drawable.tea);


        // for snack
        picMap.put("indian_namkeen_mixture", drawable.indian_namkeen_mixture);
        picMap.put("popcorn", drawable.popcorn);
        picMap.put("potato_chips", drawable.potato_chips);
        picMap.put("pretzel", drawable.pretzel);
        picMap.put("salted_peanuts", drawable.salted_peanuts);
        picMap.put("trail_mix", drawable.trail_mix);

        // for dessert
        picMap.put("tiramisu", drawable.tiramisu);
        picMap.put("eclair", drawable.eclair);
        picMap.put("doughnut", drawable.doughnut);
        picMap.put("millefeuille", drawable.millefeuille);
        picMap.put("tarteaucitron", drawable.tarteaucitron);
        picMap.put("fruitcocktail", drawable.fruitcocktail);


    }

    public int getPicture(String key){
        return picMap.get(key);
    }
}
