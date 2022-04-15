package com.example.igr203_interactive_application.Order;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// OrderData is designed for recording one order.
public class OrderDetail {
    public ArrayList<OrderData> orderDataList;

    public OrderDetail(){
        orderDataList = new ArrayList<>();
    }

    public void addItem(int num, JSONObject itemData, ArrayList<Boolean> ingredientList) throws JSONException{
        String keyName = itemData.getString("name");
        boolean hasFound = false;
        int foundIdx = -1;
        // first check
        for(int i=0; i<orderDataList.size(); i++){
            OrderData orderData = orderDataList.get(i);
            if(orderData.itemData.getString("name").equals(keyName)){
                hasFound = true;
                foundIdx = i;
                break;
            }
        }

        boolean isNew;
        if(hasFound){
            // second check, make sure all ingredient selections are same.
            isNew = false; // set it to false at the beginning, only works for this check
            OrderData orderData = orderDataList.get(foundIdx);
            for(int j=0;j<orderData.ingredientList.size(); j++){
                boolean select = orderData.ingredientList.get(j);
                if(select != ingredientList.get(j)){
                    // if ingredients are different, it's new
                    isNew = true;
                    break;
                }
            }
        }
        else{
            isNew = true;
        }

        if(isNew){
            OrderData newOrderData = new OrderData();
            newOrderData.id = orderDataList.size(); // use its index to be id.
            newOrderData.num = num;
            newOrderData.itemData = itemData;
            newOrderData.ingredientList = ingredientList;
            orderDataList.add(newOrderData);
        }
        else{
            OrderData oldOrderData = orderDataList.get(foundIdx);
            oldOrderData.num += num;
        }
    }

    public int getTotalItemNum(){
        int total = 0;
        for(OrderData od : orderDataList){
            total += od.num;
        }
        return total;
    }

    public void changeOrderDataNum(int id, int change){
        int newNum = orderDataList.get(id).num + change;
        if(newNum<0){
            // remove this item, and update all orderData.id
            for(int i=orderDataList.size()-1;i>=id;i--){
                if(i > id){
                    orderDataList.get(i).id -= 1;
                }
                else{
                    orderDataList.remove(id);
                }
            }
        }
        else{
            orderDataList.get(id).num = newNum;
        }
    }

    public String getTotalPrice() throws JSONException {
        float total = 0;
        for(OrderData od : orderDataList){
            total += Float.parseFloat(od.itemData.getString("price")) * od.num;
        }
        return Float.toString(total)+" €";
    }

    public String getDetailStr(){
        String total = "";
        for(int i=0; i<orderDataList.size(); i++){
            OrderData od = orderDataList.get(i);
            if(od.num <= 0)
                continue;
            try {
                total += (od.itemData.getString("name")+" * "+od.num);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(i != orderDataList.size()-1){
                total += ", ";
            }
        }
        total += ".";
        return total;
    }
}
