package com.example.igr203_interactive_application.TrackOrder;

import android.util.Pair;

import com.example.igr203_interactive_application.Order.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OutsideOrder {

    private String order_id;
    private String remaining_time;
    private String total_price;
    private String detailStr;
    private String notesStr;

    public OutsideOrder(String order_id, String remaining_time, String total_price, String detailStr, String notesStr) {
        this.order_id = order_id;
        this.remaining_time = remaining_time;
        this.total_price = total_price;
        this.detailStr = detailStr;
        this.notesStr = notesStr;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getRemaining_time() {
        return remaining_time;
    }

    public void setRemaining_time(String remaining_time) {
        this.remaining_time = remaining_time;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getDetailStr(){
        return this.detailStr;
    }

    public String getNotesStr() {return this.notesStr;}
}
