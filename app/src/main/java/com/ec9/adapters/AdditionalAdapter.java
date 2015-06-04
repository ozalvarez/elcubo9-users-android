package com.ec9.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ec9.R;
import com.ec9.data.menu.MenuAdditionalBinding;

import java.util.List;

/**
 * Created by Oswaldo on 11/17/2014.
 */
public class AdditionalAdapter extends ArrayAdapter<MenuAdditionalBinding> {
    public AdditionalAdapter(Context context, List<MenuAdditionalBinding> additionals) {
        super(context, 0, additionals);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MenuAdditionalBinding additional = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_additional, parent, false);
        }
        // Lookup view for data population
        TextView tvMenuAdditionalName = (TextView) convertView.findViewById(R.id.tvMenuAdditionalName);
        TextView tvRequiredOrOptional = (TextView) convertView.findViewById(R.id.tvRequiredOrOptional);

        tvMenuAdditionalName.setText(additional.getMenuAdditionalName());
        if (additional.getRequired())
            tvRequiredOrOptional.setText(convertView.getResources().getString(R.string.required));
        else
            tvRequiredOrOptional.setText(convertView.getResources().getString(R.string.optional));

        return convertView;
    }
}
