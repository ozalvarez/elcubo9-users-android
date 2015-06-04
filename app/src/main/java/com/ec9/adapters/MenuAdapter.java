package com.ec9.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.ec9.R;
import com.ec9.data.menu.MenuBinding;
import com.ec9.data.menu.MenuListBinding;

import java.util.List;

/**
 * Created by Oswaldo on 11/17/2014.
 */
public class MenuAdapter extends BaseExpandableListAdapter {
    private final List<MenuListBinding> menus;
    public LayoutInflater inflater;
    public Activity activity;

    public MenuAdapter(Activity act, List<MenuListBinding> menus) {
        activity = act;
        this.menus = menus;
        inflater = act.getLayoutInflater();
    }

    @Override
    public MenuBinding getChild(int groupPosition, int childPosition) {
        return menus.get(groupPosition).getMenus().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final MenuBinding children = (MenuBinding) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_menu, null);
        }
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvMenuTitle);
        TextView tvMenuSubtitle = (TextView) convertView.findViewById(R.id.tvMenuSubtitle);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
        tvTitle.setText(children.getTitle());
        tvMenuSubtitle.setText(children.getSubtitle());
        tvPrice.setText("$ "+children.getPrice());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return menus.get(groupPosition).getMenus().size();
    }

    @Override
    public MenuListBinding getGroup(int groupPosition) {
        return menus.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return menus.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_menu_header, null);
        }
        MenuListBinding group = (MenuListBinding) getGroup(groupPosition);
        CheckedTextView tvTag = (CheckedTextView) convertView.findViewById(R.id.tvTag);
        tvTag.setText(group.getMenuTagName());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
