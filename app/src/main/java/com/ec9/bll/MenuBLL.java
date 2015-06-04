package com.ec9.bll;

import android.app.Activity;

import com.ec9.BaseActivity;
import com.ec9.data.LoginAccountBinding;
import com.ec9.data.menu.MenuBinding;
import com.ec9.data.menu.MenuListBinding;
import com.ec9.exceptions.RuleException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Oswaldo on 11/18/2014.
 */
public class MenuBLL extends BaseBLL {
    public static MenuBinding MenuItemToAdd;
    public static List<MenuListBinding> MenuCurrent;

    public List<MenuListBinding> getMenus(int customerID, Activity activity) throws RuleException {
        String _JSON=getJSON("api/menu?CustomerID=" + customerID, activity);
        MenuCurrent= new Gson().fromJson(_JSON,new TypeToken<List<MenuListBinding>>(){}.getType());
        return MenuCurrent;
    }
}
