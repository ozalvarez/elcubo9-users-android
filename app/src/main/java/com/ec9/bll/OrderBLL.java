package com.ec9.bll;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.ec9.BaseActivity;
import com.ec9.MenuActivity;
import com.ec9.OrderReviewActivity;
import com.ec9.OrderSentActivity;
import com.ec9.R;
import com.ec9.data.order.OrderBinding;
import com.ec9.data.order.OrderItemBinding;
import com.ec9.exceptions.RuleException;
import com.google.gson.GsonBuilder;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;


/**
 * Created by Oswaldo on 11/19/2014.
 */
public class OrderBLL extends BaseBLL {
    public static OrderBinding Order;

    public static void clearMenu() {
        if (Order != null)
            OrderBLL.Order.getItems().clear();
    }

    public static void returnToTop(Activity activity) {
        clearMenu();
        Intent intent = new Intent(activity, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void addItem(OrderItemBinding orderItemBinding) {
        Order.getItems().add(orderItemBinding);
    }

    public static boolean canOrder() {
        return !Order.getItems().isEmpty();
    }

    public static boolean canOrder(Activity activity) {
        if (!canOrder()) {
            returnToTop(activity);
            return false;
        }
        return true;
    }

    public int send(final OrderReviewActivity activity) throws RuleException {
        String postParam = new GsonBuilder().create().toJson(Order);
        final int _OrderID = postInteger("api/order", postParam, activity);

        String server = URL + "signalr";
        String CONNECTION_QUERYSTRING = "Bearer=" + BaseActivity.getToken(activity).getAccess_token();
        HubConnection connection = new HubConnection(server, CONNECTION_QUERYSTRING, false, new Logger() {
            @Override
            public void log(String message, LogLevel level) {
                System.out.println(message);
            }
        });
        final HubProxy proxy = connection.createHubProxy("OrderHub");
        //Start connection
        SignalRFuture<Void> awaitConnection = connection.start();
        connection.start().done(new Action<Void>() {
            @Override
            public void run(Void obj) {
                proxy.invoke("JoinGroup", "O-" + _OrderID).done(new Action<Void>() {
                    @Override
                    public void run(Void obj) {
                        proxy.invoke("Send", Order.getCustomerID(), _OrderID).done(new Action<Void>() {
                            @Override
                            public void run(Void obj) {
                                Log.i("O", "ORDER SENT");
                            }
                        }).onError(new ErrorCallback() {

                            @Override
                            public void onError(Throwable error) {
                                Log.e("O", error.getMessage());
                            }
                        });
                    }
                });
            }
        }).onError(new ErrorCallback() {
            @Override
            public void onError(Throwable error) {
                Log.e("O", error.getMessage());
            }
        });
        proxy.on("changeStatus", new SubscriptionHandler1<Integer>() {
            @Override
            public void run(Integer p1) {
                Log.i("SC", "Status Changed to=" + p1);
                activity.ChangeStatus(p1);
            }
        }, Integer.class);
        return _OrderID;
    }

    public static boolean checkOrderActive(Activity activity) {
        if (Order == null) {
            returnToTop(activity);
            return false;
        } else return true;
    }
}
