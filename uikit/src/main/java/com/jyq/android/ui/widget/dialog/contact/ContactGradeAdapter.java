package com.jyq.android.ui.widget.dialog.contact;

/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
           佛祖保佑       永无BUG
 */

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;

import com.jyq.android.net.modal.Grade;
import com.jyq.android.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 */

abstract class ContactGradeAdapter extends BaseExpandableListAdapter {

    abstract ArrayList<Grade> getSelects();

    abstract void reloadGradeList(ArrayList<Grade> gradeArrayList);

    abstract List<String> getGroupData();

    abstract List<Grade> getChildData(int groupPosition);
    abstract Drawable getCheckedDrawable();
    @Override
    public int getGroupCount() {
        return getGroupData().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return getChildData(groupPosition).size();
    }

    private static final String TAG = "ContactGradeAdapter";

    @Override
    public String getGroup(int groupPosition) {
        Log.e(TAG, "getGroup: " + groupPosition);
        return getGroupData().get(groupPosition);
    }

    @Override
    public Grade getChild(int groupPosition, int childPosition) {
        return getChildData(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        CheckedTextView textView = null;
        if (convertView == null) {
            textView = (CheckedTextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_contact_item_grade_group, parent, false);
        } else {
            textView = (CheckedTextView) convertView;
        }
        textView.setText(getGroup(groupPosition));
        return textView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        CheckedTextView textView = null;
        if (convertView == null) {
            textView = (CheckedTextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_contact_item_grade_child, parent, false);
            textView.setCheckMarkDrawable(getCheckedDrawable());
        } else {
            textView = (CheckedTextView) convertView;
        }
        onBindView(textView, groupPosition, childPosition, getChild(groupPosition, childPosition));
        return textView;
    }

    protected void onBindView(CheckedTextView view, int groupPosition, int childPosition, Grade grade) {
        view.setText(grade.name);
        view.setTag(grade);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
