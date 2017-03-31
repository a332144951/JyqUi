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

import android.view.View;
import android.widget.ExpandableListView;

import com.google.common.collect.Lists;
import com.jyq.android.net.modal.Grade;
import com.jyq.android.ui.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/28.
 */

public class ContactOnlyGradeDialog extends ContactSelectDialog {
    boolean mMultiple;
    private ArrayList<Grade> selects;
    private ContactGradeMultipleAdapter adapter;
    private final ArrayList<Grade> oldSelects;
    protected ContactOnlyGradeDialog(boolean multiple, ArrayList<Grade> selects, ContactMultipleGradeListener listener) {
        super(listener);
        this.mMultiple = multiple;
        this.selects = selects;
        oldSelects=Lists.newArrayList(selects);
    }

    protected ContactOnlyGradeDialog(boolean multiple, ArrayList<Grade> selects, ContactSingleGradeListener listener) {
        super(listener);
        this.mMultiple = multiple;
        this.selects = selects;
        oldSelects=Lists.newArrayList(selects);
    }

    @Override
    void init() {
        setTitle("班级选择");
        adapter = new ContactGradeMultipleAdapter(getContext(),selects);
        gradeListView.setAdapter(adapter);
        gradeListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                onItemClick(adapter.getChild(groupPosition, childPosition));
                notifyCount();
                return true;
            }
        });
        gradeListView.setVisibility(View.VISIBLE);
        loadGradeInfo();
        notifyCount();
    }
    void onItemClick(Grade grade){
        if (mMultiple){
            if (selects.contains(grade)){
                selects.remove(grade);
            }else{
                selects.add(grade);
            }
        }else{
            selects.clear();
            selects.add(0,grade);
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    void onClearClick() {
        selects.clear();
        adapter.notifyDataSetChanged();
        notifyCount();
    }
    void notifyCount(){
        setSelectCount(adapter.getSelects().size());
    }

    @Override
    void onGradeLoaded(List<Grade> gradeList) {
        selects.retainAll(gradeList);
        adapter.reloadGradeList(Lists.newArrayList(gradeList));
        gradeListView.expandGroup(0);
        notifyCount();
    }
    @Override
    void onOkClick() {
        ArrayList<Grade> result = adapter.getSelects();
        if (result.size() == 0) {
            ToastUtils.showShort(getContext(), "至少选择一个");
        }
        if (listener instanceof ContactSingleGradeListener) {
            ((ContactSingleGradeListener) listener).onSelect(result.get(0));
        } else {
            ((ContactMultipleGradeListener) listener).onSelect(result);
        }
        dismiss();
    }

    @Override
    void onCancelClick() {
        if (listener instanceof ContactSingleGradeListener) {
            ((ContactSingleGradeListener) listener).onSelect(oldSelects.get(0));
        } else {
            ((ContactMultipleGradeListener) listener).onSelect(oldSelects);
        }
        dismiss();
    }
}
