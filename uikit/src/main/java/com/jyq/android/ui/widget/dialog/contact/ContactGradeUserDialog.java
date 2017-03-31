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

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.google.common.collect.Lists;
import com.jyq.android.net.exception.ApiException;
import com.jyq.android.net.modal.Grade;
import com.jyq.android.net.modal.User;
import com.jyq.android.net.service.GradeService;
import com.jyq.android.net.subscriber.HttpSubscriber;
import com.jyq.android.ui.ToastUtils;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import rx.Subscription;

/**
 * Created by Administrator on 2017/3/27.
 */

class ContactGradeUserDialog extends ContactSelectDialog {
    private ArrayList<User> selectedUser;
    private ContactGradeSingleAdapter adapter;
    private int roleRange;
    private ContactUserAdapter userAdapter;
    private ArrayList<User> oldSelectedUser;
    private boolean isMultiple;
    List<String> roleFilter;

    protected ContactGradeUserDialog(int roleRange, User selectedUser, ContactSingleUserListener listener) {
        super(listener);
        this.roleRange = roleRange;
        this.selectedUser = selectedUser != null ? Lists.newArrayList(selectedUser) : Lists.<User>newArrayList();
        this.oldSelectedUser = Lists.newArrayList(this.selectedUser);
        isMultiple = false;
        initRoleFilter();
    }

    protected ContactGradeUserDialog(int roleRange, ArrayList<User> selectedUsers, ContactMultipleUserListener listener) {
        super(listener);
        this.roleRange = roleRange;
        this.selectedUser = selectedUsers != null ? selectedUsers : Lists.<User>newArrayList();
        this.oldSelectedUser = Lists.newArrayList(this.selectedUser);
        isMultiple = true;
        initRoleFilter();
    }

    private void initRoleFilter() {
        roleFilter = new ArrayList<>();
        if ((roleRange & GradeService.RoleRange.ROLE_PARENT) != 0) {
            roleFilter.add("parent");
        }
        if ((roleRange & GradeService.RoleRange.ROLE_TEACHER) != 0) {
            roleFilter.add("teacher");
        }
        if ((roleRange & GradeService.RoleRange.ROLE_BABY) != 0) {
            roleFilter.add("baby");
        }
    }

    private static final String TAG = "ContactSingleGradeDialo";

    @Override
    void init() {
        setTitle("人员选择");
        adapter = new ContactGradeSingleAdapter(new Grade.Builder().id(-1).name("最近").build());
        gradeListView.setAdapter(adapter);
        gradeListView.setVisibility(View.VISIBLE);
        UserListView.setVisibility(View.VISIBLE);
        gradeListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                loadUsers(adapter.getChild(groupPosition, childPosition).id);
                return true;
            }
        });
        userAdapter = new ContactUserAdapter(selectedUser);
        UserListView.setAdapter(userAdapter);
        UserListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onUserItemClick(userAdapter.getItem(position));
            }
        });
        loadGradeInfo();
        setSelectCount(selectedUser.size());
    }

    void onUserItemClick(User user) {
        if (isMultiple) {
            if (selectedUser.contains(user)) {
                selectedUser.remove(user);
            } else {
                selectedUser.add(user);
            }
        } else {
            selectedUser.clear();
            selectedUser.add(user);
        }
        setSelectCount(selectedUser.size());
        userAdapter.notifyDataSetChanged();
    }

    @Override
    void onClearClick() {
        selectedUser.clear();
        userAdapter.notifyDataSetChanged();
        setSelectCount(selectedUser.size());
    }

    @Override
    void onGradeLoaded(List<Grade> gradeList) {
        adapter.reloadGradeList(Lists.newArrayList(gradeList));
        gradeListView.expandGroup(0);
        gradeListView.expandGroup(1);
        gradeListView.setSelectedChild(0, 0, true);
    }

    @Override
    void onOkClick() {
        if (selectedUser.size() == 0) {
            ToastUtils.showShort(getContext(), "至少选择一个");
            return;
        }
        ContactCache.getInstance().updateRecentContacts(selectedUser);
        if (listener instanceof ContactSingleUserListener) {
            ((ContactSingleUserListener) listener).onSelect(selectedUser.size() == 0 ? null : selectedUser.get(0));
        } else if (listener instanceof ContactMultipleUserListener) {
            ((ContactMultipleUserListener) listener).onSelect(selectedUser);
        }

        dismiss();
    }

    @Override
    void onCancelClick() {
        if (listener instanceof ContactSingleUserListener) {
            ((ContactSingleUserListener) listener).onSelect(oldSelectedUser.size() == 0 ? null : oldSelectedUser.get(0));
        } else if (listener instanceof ContactMultipleUserListener) {
            ((ContactMultipleUserListener) listener).onSelect(oldSelectedUser);
        }
        dismiss();
    }

    void onUsersLoaded(List<User> users) {
        userAdapter.reloadUsers(Lists.<User>newArrayList(users));

    }

    void loadUsers(int gradeId) {
        if (gradeId != -1) {
            Subscription subscription = GradeService.fetchGradeUsers(gradeId, roleRange)
                    .subscribe(new HttpSubscriber<List<User>>() {
                        @Override
                        protected void onSuccess(List<User> userList) {
                            onUsersLoaded(userList);
                        }

                        @Override
                        protected void onApiError(ApiException e) {

                        }
                    });
            subscriptions.add(subscription);
        } else {
            onUsersLoaded(ContactCache.getInstance().getRecentContacts(roleFilter));
        }
    }
}
