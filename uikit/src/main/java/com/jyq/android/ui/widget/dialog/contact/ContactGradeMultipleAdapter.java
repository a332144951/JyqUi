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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.CheckedTextView;

import com.google.common.collect.Lists;
import com.jyq.android.net.modal.Grade;
import com.jyq.android.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/24.
 */

public class ContactGradeMultipleAdapter extends ContactGradeAdapter {
    private ArrayList<Grade> selectedList;
    private ArrayList<Grade> gradeArrayList;
    private Context mContext;
    public ContactGradeMultipleAdapter(Context context,ArrayList<Grade> selectedList) {
        this.selectedList = selectedList != null ? selectedList : Lists.<Grade>newArrayList();
        this.mContext=context;
    }

    @Override
    protected void onBindView(CheckedTextView view, int groupPosition, int childPosition, Grade grade) {
        super.onBindView(view, groupPosition, childPosition, grade);
        view.setChecked(selectedList.contains(grade));
    }

    @Override
    ArrayList<Grade> getSelects() {
        return selectedList;
    }

    @Override
    void reloadGradeList(ArrayList<Grade> gradeArrayList) {
        this.gradeArrayList = gradeArrayList;
    }

    @Override
    List<String> getGroupData() {
        return Lists.newArrayList("所有班级");
    }

    @Override
    List<Grade> getChildData(int groupPosition) {
        return gradeArrayList;
    }

    @Override
    Drawable getCheckedDrawable() {
        return mContext.getResources().getDrawable(R.drawable.checkbox_selector);
    }

}
