package com.example.igr203_interactive_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.igr203_interactive_application.Menu.CustomMenuAdapter;
import com.example.igr203_interactive_application.Menu.MenuItem;
import com.example.igr203_interactive_application.Navigation.CustomNavigationAdapter;
import com.example.igr203_interactive_application.Navigation.NavigationItem;
import com.example.igr203_interactive_application.Order.OrderAdapter;
import com.example.igr203_interactive_application.Order.OrderData;
import com.example.igr203_interactive_application.Order.OrderDetail;
import com.example.igr203_interactive_application.Order.OrderItem;
import com.example.igr203_interactive_application.TrackOrder.OutsideOrder;
import com.example.igr203_interactive_application.TrackOrder.OutsideListViewAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

enum PageType{
    DETAIL,
    ORDER_TRACK,
    CART
}

public class MainActivity extends AppCompatActivity {
    private JSONObject allData;
    private JSONArray navDataList;
    private NavigatorType navType;
    private String menuKeyWord;

    // for detail view
    private ArrayList<OrderDetail> orderDetailList; // index is the time of order.
    private int currentOrderIndex;
    private Button cartBtn;

    private PageType pageType;

    private void initData(){
        allData = DataManager.getInstance().loadJSONFromAsset(this, "jsondata.json");
        navType = NavigatorType.FOOD;
        menuKeyWord = "";
        orderDetailList = new ArrayList<OrderDetail>();
        currentOrderIndex = 0;
        pageType = PageType.ORDER_TRACK;
    }

    // initialize left part
    private void initNavigator(){
        // for navigation list
        ArrayList<NavigationItem> navigation_list=new ArrayList();
        ListView navi_list=(ListView)findViewById(R.id.leftBar);

        try {
            navDataList = allData.getJSONArray("navigator");
            for(int i=0;i<navDataList.length();i++){
                JSONObject navData = navDataList.getJSONObject(i);
                NavigationItem item = new NavigationItem(navData.getString("name"),
                        DataManager.getInstance().getPicture(navData.getString("pic")));
                navigation_list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomNavigationAdapter first_adapter=new CustomNavigationAdapter(this,R.layout.navigation_item,navigation_list);
        navi_list.setAdapter(first_adapter);
        navi_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                TextView mymenuTitle= (TextView)findViewById(R.id.Title);
                switch(position) {
                    case 0:
                        mymenuTitle.setText("Food");
                        navType = NavigatorType.FOOD;
                        break;
                    case 1:
                        mymenuTitle.setText("Drinks");
                        navType = NavigatorType.DRINKS;
                        break;
                    case 2:
                        mymenuTitle.setText("Snack");
                        navType = NavigatorType.SNACK;
                        break;
                    case 3:
                        mymenuTitle.setText("Dessert");
                        navType = NavigatorType.DESSERT;
                        break;
                }
                updateMenu();
            }
        });
    }

    private JSONArray getNavDataList() throws JSONException {
        String dataKey = null;
        switch (navType){
            case FOOD:
                dataKey = "Food"; break;
            case DRINKS:
                dataKey = "Drinks"; break;
            case SNACK:
                dataKey = "Snack"; break;
            case DESSERT:
                dataKey = "Dessert"; break;
        }
        return allData.getJSONArray(dataKey);
    }

    private void updateMenu(){
        // update this menu by navType

        //for menu list
        ArrayList<MenuItem> menu_list=new ArrayList();
        ListView men_list=(ListView)findViewById(R.id.menuBar);

        try {
            JSONArray dataList = getNavDataList();
            for(int i=0;i<dataList.length();i++){
                JSONObject data = dataList.getJSONObject(i);
                String itemName = data.getString("name");
                boolean visible = menuKeyWord == "" || itemName.toLowerCase().contains(menuKeyWord.toLowerCase());
                JSONArray ingredients = data.getJSONArray("ingredients");
                for(int j=0; j<ingredients.length(); j++){
                    String ingredientStr = ingredients.getString(j);
                    visible |= ingredientStr.toLowerCase().contains(menuKeyWord.toLowerCase());
                }
                if(visible){
                    MenuItem item = new MenuItem(1, itemName,
                            data.getString("price")+" €",
                            DataManager.getInstance().getPicture(data.getString("pic")));
                    menu_list.add(item);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomMenuAdapter second_adapter= new CustomMenuAdapter(this,R.layout.menu_food_layout,menu_list);
        men_list.setAdapter(second_adapter);
    }

    private void initSearchBar(){
        EditText editText = findViewById(R.id.Searchbar);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                menuKeyWord = editText.getText().toString();
                updateMenu();
            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    private JSONObject getItemData(JSONArray dataList, String key) throws JSONException {
        for(int i=0;i<dataList.length();i++) {
            JSONObject data = dataList.getJSONObject(i);
            if (data.getString("name") == key) {
                return data;
            }
        }
        return null;
    }

    private void updatePageVisible(){
        ConstraintLayout orderView = findViewById(R.id.orderLayout); // never mind the id
        orderView.setVisibility(pageType == PageType.DETAIL ?View.VISIBLE:View.INVISIBLE);
        ConstraintLayout orderTrackView = findViewById(R.id.track_order_layout); // never mind the id
        orderTrackView.setVisibility(pageType == PageType.ORDER_TRACK?View.VISIBLE:View.INVISIBLE);
        ConstraintLayout cartView = findViewById(R.id.cartLayout); // never mind the id
        cartView.setVisibility(pageType == PageType.CART ?View.VISIBLE:View.INVISIBLE);
    }

    public void showDetailData(String detailName) throws JSONException {
        pageType = PageType.DETAIL;
        updatePageVisible();

        Log.d("tag", detailName);
        JSONObject itemData = getItemData(getNavDataList(), detailName);
        updateDetailView(itemData);
    }

    private void updateDetailView(JSONObject itemData) throws JSONException {
        ConstraintLayout orderView = findViewById(R.id.orderLayout); // never mind the id
        TextView nameText = orderView.findViewById(R.id.item_selected_name);
        nameText.setText(itemData.getString("name"));
        TextView priceText = orderView.findViewById(R.id.price_item_selected);
        priceText.setText(itemData.getString("price")+" €");
        ImageView picView = orderView.findViewById(R.id.imageView);
        picView.setImageResource(DataManager.getInstance().getPicture(itemData.getString("pic")));
        TextView itemNumText = orderView.findViewById(R.id.id_number_order);
        itemNumText.setText(Integer.toString(1)); // set number to 1 by default
        ChipGroup ingredientChipGroup = orderView.findViewById(R.id.chipGroup);
        ingredientChipGroup.removeAllViews();
        JSONArray ingredientsDataList = itemData.getJSONArray("ingredients");
        for(int i=0; i<ingredientsDataList.length(); i++){
            String ingredientStr = ingredientsDataList.getString(i);
            Chip chip = new Chip(this);
            chip.setText(ingredientStr);
            chip.setTypeface(null, Typeface.BOLD);

            ingredientChipGroup.addView(chip);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean hasDelete = (chip.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0;
                    if(hasDelete){
                        // if it has delete, which means it wants to add ingredient now.
                        chip.setPaintFlags(chip.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    }
                    else{
                        chip.setPaintFlags(chip.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                }
            });
        }
        Button addToCartBtn = orderView.findViewById(R.id.add_cartt_second_button);
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetail orderDetail = orderDetailList.get(currentOrderIndex);
                ArrayList<Boolean> ingredientList = new ArrayList<>();
                for(int i=0; i<ingredientChipGroup.getChildCount(); i++){
                    Chip chip = (Chip) ingredientChipGroup.getChildAt(i);
                    boolean hasDelete = (chip.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0;
                    ingredientList.add(!hasDelete); // if not delete, it means it add this ingredient.
                }
                try {
                    int itemNum = Integer.parseInt(itemNumText.getText().toString());
                    orderDetail.addItem(itemNum, itemData, ingredientList);
                    updateCartButton();
                    showItemAddDialog(itemData.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addItemToCart(String detailName) throws JSONException {
        // fast-order, all ingredients are selected in this case
        OrderDetail orderDetail = orderDetailList.get(currentOrderIndex);
        JSONObject itemData = getItemData(getNavDataList(), detailName);
        JSONArray ingredientsDataList = itemData.getJSONArray("ingredients");
        ArrayList<Boolean> ingredientList = new ArrayList<>();
        for(int i=0; i<ingredientsDataList.length(); i++){
            ingredientList.add(true);
        }
        try {
            orderDetail.addItem(1, itemData, ingredientList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        pageType = PageType.CART;
        updatePageVisible();
        updateCartView();
        updateCartButton();
        showItemAddDialog(detailName);
    }

    private void initDetailView(){
        cartBtn = findViewById(R.id.cart_number);
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageType = PageType.CART;
                updatePageVisible();
                updateCartView();
            }
        });
        ConstraintLayout orderView = findViewById(R.id.orderLayout); // never mind the id
        Button addBtn = orderView.findViewById(R.id.add_item_button);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView itemNumText = orderView.findViewById(R.id.id_number_order);
                int itemNum = Integer.parseInt(itemNumText.getText().toString());
                itemNum += 1;
                itemNumText.setText(Integer.toString(itemNum));
            }
        });
        Button removeBtn = orderView.findViewById(R.id.remove_number_button);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView itemNumText = orderView.findViewById(R.id.id_number_order);
                int itemNum = Integer.parseInt(itemNumText.getText().toString());
                itemNum -= 1;
                itemNum = Math.max(1, itemNum);
                itemNumText.setText(Integer.toString(itemNum));
            }
        });
    }

    private void updateCartButton(){
        OrderDetail currentOD = orderDetailList.get(currentOrderIndex);
        int total = currentOD.getTotalItemNum();
        cartBtn.setText("My Cart : "+ total);
    }

    private void updateCartView(){
        OrderDetail currentOD = orderDetailList.get(currentOrderIndex);
        ArrayList<OrderItem> orderItemList = new ArrayList();
        try {
            for(OrderData orderData : currentOD.orderDataList){
                JSONObject jsonData = orderData.itemData;
                String itemName = jsonData.getString("name");
                String price = jsonData.getString("price")+" €";
                int imageID = DataManager.getInstance().getPicture(jsonData.getString("pic"));
                JSONArray jsonIngredients = jsonData.getJSONArray("ingredients");
                ArrayList<Pair<String, Boolean>> ingredients = new ArrayList<>();
                for(int i=0; i<jsonIngredients.length(); i++) {
                    String ingredientStr = jsonIngredients.getString(i);
                    boolean select = orderData.ingredientList.get(i);
                    ingredients.add(new Pair<String, Boolean>(ingredientStr, select));
                }
                OrderItem orderItem = new OrderItem(itemName, orderData.id, price, imageID, ingredients, orderData.num);
                orderItemList.add(orderItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListView cartListView = findViewById(R.id.insidelist);
        OrderAdapter adapter= new OrderAdapter(this, R.layout.card_item, orderItemList);
        cartListView.setAdapter(adapter);

        try {
            TextView totalText = findViewById(R.id.order_cart_price);
            totalText.setText(currentOD.getTotalPrice());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void enterNextOrder(){
        currentOrderIndex = orderDetailList.size();
        orderDetailList.add(new OrderDetail());
    }

    public void changeItemNum(int orderDataID, boolean isAdd){
        OrderDetail orderDetail = orderDetailList.get(currentOrderIndex);
        orderDetail.changeOrderDataNum(orderDataID, isAdd?1:-1);
        updateCartButton();
        updateCartView();
    }

    private void showWrongOrderDialog(){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("It seems you have not add any item, please order at least one thing. Thank you for your understanding!")
                .setPositiveButton("I got it!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // START THE GAME!
                    }
                });
        // Create the AlertDialog object and return it
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void showConfirmOrderDialog(){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your order has been confirmed, and the delicious will arrive soon. Thank you for your patience!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // START THE GAME!
                    }
                });
        // Create the AlertDialog object and return it
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void initConfirmBtn(){
        ConstraintLayout cartView = findViewById(R.id.cartLayout); // never mind the id
        Button confirmBtn = cartView.findViewById(R.id.button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetail currentOD = orderDetailList.get(currentOrderIndex);
                int total = currentOD.getTotalItemNum();
                if(total <= 0){
                    showWrongOrderDialog();
                }
                else{
                    showAddNoteDialog();
                }
            }
        });
    }

    private void afterConfirmOrder(){
        pageType = PageType.ORDER_TRACK;
        updatePageVisible();
        updateCartButton();
        updateOrderTrackView();
    }

    private void showItemAddDialog(String itemName){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(itemName + " has been added into your cart!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // START THE GAME!
                    }
                });
        // Create the AlertDialog object and return it
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void updateOrderTrackView(){
        boolean hasOrdered = orderDetailList.size() > 1;
        TextView nonOrderTextView = findViewById(R.id.non_order);
        nonOrderTextView.setVisibility(hasOrdered?View.INVISIBLE:View.VISIBLE);
        if(hasOrdered){
            ArrayList<OutsideOrder> outside_order_list=new ArrayList();
            ListView outside_list=(ListView)findViewById(R.id.outside_list);

            for(int i=0; i<orderDetailList.size()-1; i++){
                OrderDetail orderDetail = orderDetailList.get(i);
                String order_id = "OrderNum: " + Integer.toString(i+1);
                int remainTime = 2 * orderDetail.getTotalItemNum();
                String remainTimeStr = "Remaining time: "+remainTime+" min";
                String detailStr = "Details: "+orderDetail.getDetailStr();
                String notesStr = orderDetail.getOrderNotes();
                if(!notesStr.isEmpty()){
                    notesStr = "Notes: "+notesStr;
                }
                try {
                    String totalPriceStr = "Total: "+orderDetail.getTotalPrice();
                    OutsideOrder outsideorder=new OutsideOrder(order_id, remainTimeStr, totalPriceStr, detailStr, notesStr);
                    outside_order_list.add(outsideorder);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            OutsideListViewAdapter first_adapter=new OutsideListViewAdapter(this,R.layout.items_order_track,outside_order_list);
            outside_list.setAdapter(first_adapter);
        }
    }

    private void initOrderTrackBtn(){
        Button orderTrackBtn = findViewById(R.id.track_order_button);
        orderTrackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageType = PageType.ORDER_TRACK;
                updatePageVisible();
            }
        });
    }

    private void showAddNoteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.note_dialog, null))
                // Add action buttons
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // mark down the notes
                        Dialog noteDialog = (Dialog) dialog;
                        EditText notesText = noteDialog.findViewById(R.id.notes_field);
                        OrderDetail orderDetail = orderDetailList.get(currentOrderIndex);
                        orderDetail.setOrderNotes(notesText.getText().toString());

                        dialog.cancel();

                        // make a order
                        showConfirmOrderDialog();
                        enterNextOrder(); // enter to next order
                        afterConfirmOrder();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initNavigator();
        initSearchBar();
        initDetailView();
        initConfirmBtn();
        initOrderTrackBtn();

        enterNextOrder();

        updateMenu();
        updatePageVisible();
        updateCartButton();
        updateOrderTrackView();
    }
}