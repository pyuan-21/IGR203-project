package com.example.igr203_interactive_application.Navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.igr203_interactive_application.R;

import java.util.List;


public class CustomNavigationAdapter extends ArrayAdapter<NavigationItem> {

    private int resourceLayout;
    private Context mContext;

    public CustomNavigationAdapter(@NonNull Context context, int resource,  @NonNull List<NavigationItem> objects) {
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

        NavigationItem p = getItem(position);

        if (p != null) {
            ImageView image_tem = (ImageView) v.findViewById(R.id.navitemid);
            TextView title_item = (TextView) v.findViewById(R.id.navitemtitle);

            if (image_tem != null) {
                image_tem.setImageResource(p.getIcon_item());
            }

            if (title_item != null) {
                title_item.setText(p.getTitle_item());
            }


        }

        return v;
    }
}
