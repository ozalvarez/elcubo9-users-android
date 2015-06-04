package com.ec9;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ec9.adapters.OrderReviewAdapter;
import com.ec9.bll.OrderBLL;
import com.ec9.exceptions.RuleException;
import com.ec9.exceptions.RuleExceptionMessage;


public class OrderReviewActivity extends BaseActivity {
    private Activity ThisActivity = OrderReviewActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_review);
        if (OrderBLL.checkOrderActive(this) && OrderBLL.canOrder(this)) {
            OrderReviewAdapter adapter = new OrderReviewAdapter(OrderReviewActivity.this, OrderBLL.Order.getItems());
            ListView listView = (ListView) findViewById(R.id.lvOrders);
            listView.setAdapter(adapter);

            Button btnSendOrder = ((Button) findViewById(R.id.btnSendOrder));
            btnSendOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText etNumberTable = ((EditText) findViewById(R.id.etNumberTable));
                    OrderBLL.Order.setTableNumber(etNumberTable.getText().toString());
                    new HttpPostSendOrder().execute();
                }
            });
            if (OrderBLL.Order.getTableNumber() != null) {
                EditText tvTableNumber = (EditText) findViewById(R.id.etNumberTable);
                tvTableNumber.setText(OrderBLL.Order.getTableNumber());
            }
        }
    }

    public void ChangeStatus(int NewStatus) {
        boolean _ShowNot = false;
        String _Title="";
        String _Text="";
        switch (NewStatus) {
            case 2://RECEIVED
                _ShowNot = true;
                _Title=getResources().getString(R.string.notTitleOrderReceived);
                _Text=getResources().getString(R.string.notTextOrderReceived);

                break;
            case 4://INVALID
                _ShowNot = true;
                _Title=getResources().getString(R.string.notTitleOrderInvalid);
                _Text=getResources().getString(R.string.notTextOrderInvalid);
                break;
        }
        if (_ShowNot) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.drawable.ic_menu_ec9);
            mBuilder.setContentTitle(_Title);
            mBuilder.setContentText(_Text);
            mBuilder.setLights(Color.RED, 500, 500);
            long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
            mBuilder.setVibrate(pattern);
            mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

            Intent resultIntent = new Intent(this, OrderSentActivity.class);
            resultIntent.putExtra("NewStatus",NewStatus);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(OrderSentActivity.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(0, mBuilder.build());
        }
    }

    public class HttpPostSendOrder extends AsyncTask<Void, Void, Integer> {
        private RuleExceptionMessage ruleExceptionMessage;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ThisActivity, getResources().getString(R.string.loadingSend), getResources().getString(R.string.loading), true, false);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                return new OrderBLL().send(OrderReviewActivity.this);
            } catch (RuleException e) {
                ruleExceptionMessage = e.getRuleExceptionMessage(ThisActivity);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Integer orderID) {

            if (ruleExceptionMessage != null)
                ruleExceptionMessage.process(ThisActivity);
            else {
                OrderBLL.Order.setOrderID(orderID);
                Intent intent = new Intent(OrderReviewActivity.this, OrderSentActivity.class);
                startActivity(intent);
                OrderReviewActivity.this.finish();
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
