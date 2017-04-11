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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.jyq.android.net.exception.ApiException;
import com.jyq.android.net.modal.Grade;
import com.jyq.android.net.modal.User;
import com.jyq.android.net.service.GradeService;
import com.jyq.android.net.subscriber.HttpSubscriber;
import com.jyq.android.ui.R;
import com.jyq.android.ui.widget.dialog.JDialogFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/3/23.
 */

public abstract class ContactSelectDialog extends JDialogFragment {
    protected ContactSelectListener listener;

    protected ContactSelectDialog(ContactSelectListener listener) {
        this.listener = listener;
    }

    protected interface ContactSelectListener<T> extends Serializable {
        void onSelect(T object);

    }

    public interface ContactSingleGradeListener extends ContactSelectListener<Grade> {
    }

    public interface ContactMultipleGradeListener extends ContactSelectListener<ArrayList<Grade>> {

    }

    public interface ContactSingleUserListener extends ContactSelectListener<User> {

    }

    public interface ContactMultipleUserListener extends ContactSelectListener<ArrayList<User>> {

    }

    public static ContactSelectDialog showSingleUserDialog(int roleRange,User selectedUser, ContactSingleUserListener listener) {
        return new ContactGradeUserDialog(roleRange,selectedUser, listener);
    }
    public static ContactSelectDialog showMultipleUserDialog(int roleRange,ArrayList<User> selectedUsers, ContactMultipleUserListener listener) {
        return new ContactGradeUserDialog(roleRange,selectedUsers, listener);
    }

    public static ContactSelectDialog showSingleGradeDialog(Grade selectedGrade, ContactSingleGradeListener listener) {
        return new ContactOnlyGradeDialog(false, Lists.newArrayList(selectedGrade), listener);
    }

    public static ContactSelectDialog showMultipleGradeDialog(ArrayList<Grade> selectedGrades, ContactMultipleGradeListener listener) {
        return new ContactOnlyGradeDialog(true, selectedGrades, listener);
    }

    abstract void init();

    abstract void onClearClick();

    abstract void onGradeLoaded(List<Grade> gradeList);

    abstract void onOkClick();

    abstract void onCancelClick();

    protected CompositeSubscription subscriptions = new CompositeSubscription();
    protected ExpandableListView gradeListView;
    protected ListView UserListView;

    private TextView titleView, resultText, clearText;
    private Button okButton, cancelButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_contact_select, container, false);
        gradeListView = (ExpandableListView) root.findViewById(R.id.contact_dialog_grade_listview);
        UserListView = (ListView) root.findViewById(R.id.contact_dialog_user_listview);
        titleView = (TextView) root.findViewById(R.id.contact_dialog_title);
        resultText = (TextView) root.findViewById(R.id.contact_dialog_result);
        clearText = (TextView) root.findViewById(R.id.contact_dialog_clear);
        okButton = (Button) root.findViewById(R.id.contact_dialog_ok);
        cancelButton = (Button) root.findViewById(R.id.contact_dialog_cancel);
        gradeListView.setVisibility(View.GONE);
        UserListView.setVisibility(View.GONE);
        blockHeaderListener();
        initListener();
        init();
        return root;
    }

    private void initListener() {
        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearClick();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick();
//                ArrayList result = getSelected();
//                Log.e(TAG, "onClick: " + Arrays.toString(result.toArray(new Grade[result.size()])));
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClick();
            }
        });
    }

    private void blockHeaderListener() {
        gradeListView.setOnGroupClickListener(blockHeaderClick);
//        UserListView.setOnGroupClickListener(blockHeaderClick);
    }

    private ExpandableListView.OnGroupClickListener blockHeaderClick = new ExpandableListView.OnGroupClickListener() {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            return true;
        }
    };

    protected void setSelectCount(int count) {
        resultText.setText(getString(R.string.contact_dialog_result, count));
    }

    protected void setTitle(CharSequence title) {
        titleView.setText(title);
    }

    private static final String TAG = "ContactSelectDialog";

    protected void loadGradeInfo() {
        Subscription subscription = GradeService.getGradeList()
                .subscribe(new HttpSubscriber<List<Grade>>() {
                    @Override
                    protected void onSuccess(List<Grade> grades) {
                        onGradeLoaded(grades);
                    }

                    @Override
                    protected void onApiError(ApiException e) {
                        e.printStackTrace();
                    }
                });
        subscriptions.add(subscription);
    }


    @Override
    public void onDestroyView() {
        if (subscriptions.isUnsubscribed()) {
            subscriptions.unsubscribe();
        }
        super.onDestroyView();
    }

    @Override
    protected boolean hasTitle() {
        return false;
    }

}
