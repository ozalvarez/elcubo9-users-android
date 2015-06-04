package com.ec9;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ec9.adapters.CustomerAdapter;
import com.ec9.bll.CustomerBLL;
import com.ec9.bll.OrderBLL;
import com.ec9.data.CustomerBinding;
import com.ec9.exceptions.RuleExceptionMessage;
import com.ec9.data.order.OrderBinding;
import com.ec9.exceptions.RuleException;
import com.facebook.Session;

import java.util.List;


public class InternActivity extends Activity {
    private Activity ThisActivity = InternActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intern);

        new HttpGetCustomers().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intern, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Handle presses on the action bar items
        switch (id) {
            case R.id.signOff:
                BaseActivity.signOff(ThisActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class HttpGetCustomers extends AsyncTask<Void, Void, List<CustomerBinding>> {
        private RuleExceptionMessage ruleExceptionMessage;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ThisActivity, getResources().getString(R.string.loadingIntern), getResources().getString(R.string.loading), true, false);
        }

        @Override
        protected List<CustomerBinding> doInBackground(Void... params) {
            try {
                return new CustomerBLL().getCustomers(ThisActivity);
            } catch (RuleException e) {
                ruleExceptionMessage = e.getRuleExceptionMessage(ThisActivity);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<CustomerBinding> list) {
            if (ruleExceptionMessage != null) {
                ruleExceptionMessage.process(ThisActivity);
            } else {
                CustomerAdapter adapter = new CustomerAdapter(InternActivity.this, list);
                ListView listView = (ListView) findViewById(R.id.lvCustomers);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                        final CustomerBinding item = (CustomerBinding) parent.getItemAtPosition(position);
                        MenuActivity.updateMenu = true;
                        OrderBLL.Order = new OrderBinding();
                        OrderBLL.Order.setCustomerID(item.getCustomerID());
                        OrderBLL.Order.setCustomerName(item.getCustomerName());
                        Intent intent = new Intent(ThisActivity, MenuActivity.class);
                        startActivity(intent);
                    }
                });
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            try {
                HttpGetCustomers.this.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
