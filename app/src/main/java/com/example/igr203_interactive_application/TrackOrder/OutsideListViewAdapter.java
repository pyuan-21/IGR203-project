package com.example.igr203_interactive_application.TrackOrder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.igr203_interactive_application.R;

import java.util.List;

public class OutsideListViewAdapter extends ArrayAdapter<OutsideOrder> {

    private int resourceLayout;
    private Context mContext;

    public OutsideListViewAdapter(@NonNull Context context, int resource, @NonNull List<OutsideOrder> objects) {
        super(context, resource, objects);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        //resourceLayout=R.layout.items_order_track;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        OutsideOrder p = getItem(position);

        if (p != null) {
            TextView order_track_number = (TextView) v.findViewById(R.id.order_track_number);
            TextView id_remaining_time = (TextView) v.findViewById(R.id.id_remaining_time);
            TextView order_track_price = (TextView) v.findViewById(R.id.order_price);
            TextView number_items_order = v.findViewById(R.id.number_items_order);
            if (order_track_number != null) {
                order_track_number.setText(p.getOrder_id());
            }

            if (id_remaining_time != null) {
                id_remaining_time.setText(p.getRemaining_time());
            }

            if (order_track_price != null) {
                order_track_price.setText(p.getTotal_price());
            }

            if(number_items_order!=null){
                number_items_order.setText(p.getDetailStr());
            }
        }

        return v;
    }
}
