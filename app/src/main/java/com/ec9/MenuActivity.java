package com.ec9;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.ec9.adapters.MenuAdapter;
import com.ec9.bll.MenuBLL;
import com.ec9.bll.OrderBLL;
import com.ec9.exceptions.RuleExceptionMessage;
import com.ec9.data.menu.MenuListBinding;
import com.ec9.exceptions.RuleException;

import java.util.List;


public class MenuActivity extends Activity {
    private Activity ThisActivity = MenuActivity.this;
    public static Boolean updateMenu;

    MenuAdapter adapter;
    Button btnOrder;
    ExpandableListView elvMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        if (OrderBLL.checkOrderActive(this)) {
            setTitle(OrderBLL.Order.getCustomerName());
            btnOrder = ((Button) findViewById(R.id.btnOrder));
            btnOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ThisActivity, OrderReviewActivity.class);
                    startActivity(intent);
                }
            });
            if (updateMenu != null && updateMenu)
                new HttpGetMenu().execute();
            else
                addToAdapter(MenuBLL.MenuCurrent);
            MenuActivity.updateMenu = false;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        addToAdapter(MenuBLL.MenuCurrent);
    }

    void addToAdapter(List<MenuListBinding> list) {
        if (list != null) {
            adapter = new MenuAdapter(ThisActivity, list);
            elvMenu = (ExpandableListView) findViewById(R.id.elvMenu);
            elvMenu.setAdapter(adapter);
            elvMenu.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    MenuBLL.MenuItemToAdd = adapter.getChild(groupPosition, childPosition);
                    Intent intent = new Intent(ThisActivity, AddItemActivity.class);
                    startActivity(intent);
                    return false;
                }
            });
            if (!OrderBLL.canOrder()) {
                btnOrder.setVisibility(View.INVISIBLE);
            } else {
                btnOrder.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                p.bottomMargin = 100;
                elvMenu.setLayoutParams(p);
            }
        } else {
            OrderBLL.checkOrderActive(ThisActivity);
        }
    }

    public class HttpGetMenu extends AsyncTask<Void, Void, List<MenuListBinding>> {
        private RuleExceptionMessage ruleExceptionMessage;
        private ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ThisActivity, getResources().getString(R.string.loadingMenu), getResources().getString(R.string.loading), true, false);
        }

        @Override
        protected List<MenuListBinding> doInBackground(Void... params) {
            try {
                return new MenuBLL().getMenus(OrderBLL.Order.getCustomerID(),ThisActivity);
            } catch (RuleException e) {
                ruleExceptionMessage = e.getRuleExceptionMessage(ThisActivity);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<MenuListBinding> list) {
            if (ruleExceptionMessage != null) {
                ruleExceptionMessage.process(ThisActivity);
            } else {
                addToAdapter(list);
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
