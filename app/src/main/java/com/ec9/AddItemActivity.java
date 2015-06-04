package com.ec9;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ec9.adapters.AdditionalAdapter;
import com.ec9.bll.MenuBLL;
import com.ec9.bll.OrderBLL;
import com.ec9.data.menu.AdditionalItemBinding;
import com.ec9.data.menu.MenuAdditionalBinding;
import com.ec9.data.order.OrderItemBinding;
import com.ec9.fragment.QuantityDialogFragment;
import com.ec9.fragment.TextDialogFragment;


public class AddItemActivity extends Activity implements QuantityDialogFragment.NoticeDialogListener, TextDialogFragment.NoticeDialogListener {
    private int Quantity;
    private String AdditionalInfo;


    private TextView tvQuantity;
    private TextView tvAdditionalInfo;

    private void updateView() {
        tvQuantity.setText(String.valueOf(Quantity));

        tvAdditionalInfo.setText(AdditionalInfo == null ? getResources().getString(R.string.optional) : AdditionalInfo);
    }

    private void openDialog(final MenuAdditionalBinding item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddItemActivity.this);
        builder.setTitle(getResources().getString(R.string.select)+ item.getMenuAdditionalName())
                .setMultiChoiceItems(item.getAdditionals(), item.getChecked(), new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        AdditionalItemBinding add = item.getItems().get(which);
                        add.setActive(isChecked);
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        if (OrderBLL.checkOrderActive(this)) {
            Quantity = 1;
            AdditionalInfo = "";

            setTitle(MenuBLL.MenuItemToAdd.getTitle());

            ((TextView) findViewById(R.id.tvMenuTitle)).setText(MenuBLL.MenuItemToAdd.getTitle());
            ((TextView) findViewById(R.id.tvMenuSubtitle)).setText(MenuBLL.MenuItemToAdd.getSubtitle());
            ((TextView) findViewById(R.id.tvPrice)).setText("$ " + MenuBLL.MenuItemToAdd.getPrice());
            tvQuantity = ((TextView) findViewById(R.id.tvQuantity));
            tvAdditionalInfo = ((TextView) findViewById(R.id.tvAdditionalInfo));
            updateView();
            ((LinearLayout) findViewById(R.id.lQuantity)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    QuantityDialogFragment dialog = new QuantityDialogFragment();
                    dialog.setQuantity(Quantity);
                    dialog.show(getFragmentManager(), "QuantityDialogFragment");
                }
            });
            ((LinearLayout) findViewById(R.id.lAdditionalInfo)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    TextDialogFragment dialog = new TextDialogFragment();
                    dialog.setTitle(getResources().getString(R.string.additionalInfo));
                    dialog.show(getFragmentManager(), "TextDialogFragment");
                }
            });
            ((Button) findViewById(R.id.btnAdd)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    boolean order = true;
                    for (int i = 0; i < MenuBLL.MenuItemToAdd.getItems().size(); i++) {
                        MenuAdditionalBinding add = MenuBLL.MenuItemToAdd.getItems().get(i);
                        if (add.getRequired()) {
                            boolean oneActive = false;
                            for (int i2 = 0; i2 < add.getItems().size(); i2++) {
                                if (add.getItems().get(i2).getActive())
                                    oneActive = true;
                            }
                            if (!oneActive) {
                                openDialog(add);
                                order = false;
                            }
                        }
                    }
                    if (order) {
                        OrderBLL.addItem(new OrderItemBinding(MenuBLL.MenuItemToAdd, Quantity, AdditionalInfo));
                        AddItemActivity.this.finish();
                    }
                }
            });
            AdditionalAdapter adapter = new AdditionalAdapter(AddItemActivity.this, MenuBLL.MenuItemToAdd.getItems());
            ListView listView = (ListView) findViewById(R.id.lvAdditionals);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                    final MenuAdditionalBinding item = (MenuAdditionalBinding) parent.getItemAtPosition(position);
                    openDialog(item);
                }
            });
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String text) {
        AdditionalInfo = text;
        updateView();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, int quantity) {
        Quantity = quantity;
        updateView();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
