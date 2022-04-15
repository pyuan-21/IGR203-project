package com.example.igr203_interactive_application.Menu;

import com.example.igr203_interactive_application.MainActivity;
import com.example.igr203_interactive_application.Navigation.NavigationItem;
import com.example.igr203_interactive_application.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;

import java.util.List;

public class CustomMenuAdapter extends ArrayAdapter<MenuItem>{

    private int resourceLayout;
    private Context mContext;

    public CustomMenuAdapter(@NonNull Context context, int resource,  @NonNull List<MenuItem> objects) {
        super(context, resource, objects);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        MenuItem p = getItem(position);

        if (p != null) {
            ImageView image_item = (ImageView) v.findViewById(R.id.imitemid);
            TextView title_item = (TextView) v.findViewById(R.id.title_item_id);
            TextView price_item = (TextView) v.findViewById(R.id.price_item);

            if (image_item != null) {
                image_item.setImageResource(p.getImage_item());
            }

            if (title_item != null) {
                title_item.setText(p.getTitle_item());
            }

            if (price_item != null) {
                price_item.setText(p.getPrice());
            }

            Button details_button=(Button)v.findViewById(R.id.button_details);
            details_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext instanceof MainActivity) {
                        try {
                            ((MainActivity)mContext).showDetailData(p.getTitle_item());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            Button addCart_button=(Button)v.findViewById(R.id.button_add_cart);
            addCart_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext instanceof MainActivity) {
                        try {
                            ((MainActivity)mContext).addItemToCart(p.getTitle_item());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        return v;
    }
}
