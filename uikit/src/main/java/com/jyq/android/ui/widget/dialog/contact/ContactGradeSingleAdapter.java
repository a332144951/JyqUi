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
import android.widget.CheckedTextView;

import com.google.common.collect.Lists;
import com.jyq.android.net.modal.Grade;
import com.jyq.android.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/24.
 */

public class ContactGradeSingleAdapter extends ContactGradeAdapter {
    private Grade selected;
    private ArrayList<Grade> gradeArrayList;

    public ContactGradeSingleAdapter(Grade selected) {
        this.selected = selected;
    }

    @Override
    protected void onBindView(CheckedTextView view, int groupPosition, int childPosition, Grade grade) {
        super.onBindView(view, groupPosition, childPosition, grade);
    }

    @Override
    ArrayList<Grade> getSelects() {
        return Lists.newArrayList(selected);
    }

    @Override
    void reloadGradeList(ArrayList<Grade> gradeArrayList) {
        this.gradeArrayList=gradeArrayList;
    }

    @Override
    List<String> getGroupData() {
        return Lists.newArrayList("","所有班级");
    }

    @Override
    List<Grade> getChildData(int groupPosition) {
        if (groupPosition==0){
            return Lists.newArrayList(new Grade.Builder().id(-1).name("最近").build());
        }else{
            return gradeArrayList;
        }
    }
    @Override
    Drawable getCheckedDrawable() {
        return null;
    }

}
