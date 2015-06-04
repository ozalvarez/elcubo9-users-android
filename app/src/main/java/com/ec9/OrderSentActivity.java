package com.ec9;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ec9.bll.OrderBLL;


public class OrderSentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_sent);
        if (OrderBLL.checkOrderActive(this)) {
            int _NewStatus = getIntent().getIntExtra("NewStatus", 0);
            TextView tvOrderStatusTitle = (TextView) findViewById(R.id.tvOrderStatusTitle);
            if (_NewStatus == 4) {
                tvOrderStatusTitle.setTextColor(getResources().getColor(R.color.red));
                tvOrderStatusTitle.setText(getResources().getText(R.string.orderInvalid));
            }else if (_NewStatus == 2) {
                tvOrderStatusTitle.setText(getResources().getText(R.string.orderReceived));
            }
            OrderBLL.clearMenu();
            TextView tvOrderID = (TextView) findViewById(R.id.tvOrderID);
            Button btnNewOrder = (Button) findViewById(R.id.btnNewOrder);
            btnNewOrder.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    OrderBLL.returnToTop(OrderSentActivity.this);
                }
            });
            tvOrderID.setText("ID de la Orden = " + OrderBLL.Order.getOrderID());
        }
    }

    @Override
    public void onBackPressed() {
        OrderBLL.returnToTop(OrderSentActivity.this);
    }
}
