package com.example.igr203_interactive_application.Order;


import com.example.igr203_interactive_application.MainActivity;
import com.example.igr203_interactive_application.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends ArrayAdapter<OrderItem> {

    private int resourceLayout;
    private Context mContext;

    public OrderAdapter(@NonNull Context context, int resource,  @NonNull List<OrderItem> objects) {
        super(context, resource, objects);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        OrderItem p = getItem(position);

        if (p != null) {
            ImageView image_item = (ImageView) v.findViewById(R.id.image_order_item);
            TextView name_order_item = (TextView) v.findViewById(R.id.name_order_detail);
            TextView price_order_item= (TextView) v.findViewById(R.id.price_order_item);
            TextView number_order_item=(TextView) v.findViewById(R.id.number_order_item);
            Button customize_btn = v.findViewById(R.id.customize_btn);

            if (image_item != null) {
                image_item.setImageResource(p.getImage_item());
            }

            if (name_order_item != null) {
                name_order_item .setText(p.getName_item());
            }

            if (price_order_item != null) {
                price_order_item.setText(p.getPrice());
            }

            if (number_order_item != null) {
                number_order_item.setText(Integer.toString(p.getNumber()));
            }

            if (customize_btn != null){
                customize_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mContext instanceof MainActivity) {
                            showIngredientsDialog((MainActivity)mContext, p);
                        }
                    }
                });
            }
            Button plus_button_order=(Button) v.findViewById(R.id.plus_button_order);
            plus_button_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext instanceof MainActivity) {
                        ((MainActivity)mContext).changeItemNum(p.getId(), true);
                    }
                }
            });
            Button minus_button_order=(Button) v.findViewById(R.id.minus_button_order);
            minus_button_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext instanceof MainActivity) {
                        ((MainActivity)mContext).changeItemNum(p.getId(), false);
                    }
                }
            });
        }

        return v;
    }

    public void showIngredientsDialog(MainActivity activity, OrderItem p) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // Set the dialog title
        ArrayList<Pair<String, Boolean>> ingredients = p.getIngredients();
        String[] ingreStrArr = new String[ingredients.size()];
        boolean[] checkedItems = new boolean[ingredients.size()];
        for(int i=0; i<ingredients.size(); i++){
            ingreStrArr[i] = ingredients.get(i).first;
            checkedItems[i] = ingredients.get(i).second;
        }
        builder.setTitle("Select customized ingredients:")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(ingreStrArr, checkedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                Pair<String, Boolean> oldValue = ingredients.get(which);
                                Pair<String, Boolean> newValue = new Pair<>(oldValue.first, isChecked);
                                ingredients.set(which, newValue);
                            }
                        })
                // Set the action buttons
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the selectedItems results somewhere
                        // or return them to the component that opened the dialog

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        Dialog dialog = builder.create();
        dialog.show();
    }
}

