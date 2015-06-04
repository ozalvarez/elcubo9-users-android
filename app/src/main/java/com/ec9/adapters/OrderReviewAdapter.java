package com.ec9.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ec9.R;
import com.ec9.data.order.OrderItemBinding;

import java.util.List;

/**
 * Created by Oswaldo on 11/17/2014.
 */
public class OrderReviewAdapter extends ArrayAdapter<OrderItemBinding> {
    private List<OrderItemBinding> orders;

    public OrderReviewAdapter(Context context, List<OrderItemBinding> orders) {
        super(context, 0, orders);
        this.orders = orders;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final OrderItemBinding order = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_order_review, parent, false);
        }
        // Lookup view for data population
        TextView tvMenuTitle = (TextView) convertView.findViewById(R.id.tvMenuTitle);
        TextView tvMenuSubtitle = (TextView) convertView.findViewById(R.id.tvMenuSubtitle);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);

        Button btnDelete = ((Button) convertView.findViewById(R.id.btnDelete));
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orders.size() > 1) {
                    orders.remove(position);
                    OrderReviewAdapter.this.notifyDataSetChanged();
                }
            }
        });

        tvMenuTitle.setText(order.getQuantity() + " - " + order.getTitle());
        tvMenuSubtitle.setText(order.getSubtitle());
        tvPrice.setText("$" + order.getPrice());

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
