package com.ec9.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ec9.R;
import com.ec9.data.CustomerBinding;

import java.util.List;

/**
 * Created by Oswaldo on 11/17/2014.
 */
public class CustomerAdapter extends ArrayAdapter<CustomerBinding> {
    public CustomerAdapter(Context context, List<CustomerBinding> customers) {
        super(context, 0, customers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CustomerBinding _customer = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_customer, parent, false);
        }
        // Lookup view for data population
        TextView tvCustomerName = (TextView) convertView.findViewById(R.id.tvCustomerName);
        tvCustomerName.setText(_customer.getCustomerName());

        return convertView;
    }
}
