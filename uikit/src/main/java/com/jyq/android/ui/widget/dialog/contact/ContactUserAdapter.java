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

import android.support.v4.app.AppLaunchChecker;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.jyq.android.common.imageloader.ImageLoaderKit;
import com.jyq.android.net.modal.User;
import com.jyq.android.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/28.
 */

public class ContactUserAdapter extends BaseAdapter {
    private ArrayList<User> users;
    private ArrayList<User> selectUsers;

    public ContactUserAdapter(ArrayList<User> selectUsers) {
        this.selectUsers = selectUsers;
        users=Lists.newArrayList();
    }

    public void reloadUsers(List<User> users){
        this.users= Lists.newArrayList(users);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            holder=new ViewHolder(parent);
            convertView=holder.itemView;
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.onBindView(position);
        return convertView;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        AppCompatImageView avatarImage;
        TextView nameText,titleText;
        AppCompatCheckBox checkBox;

        public ViewHolder(ViewGroup group) {
            super(LayoutInflater.from(group.getContext()).inflate(R.layout.dialog_contact_item_user,group,false));
            avatarImage= (AppCompatImageView) itemView.findViewById(R.id.contact_dialog_item_user_avatar);
            nameText= (TextView) itemView.findViewById(R.id.contact_dialog_item_user_name);
            titleText= (TextView) itemView.findViewById(R.id.contact_dialog_item_user_title);
            checkBox= (AppCompatCheckBox) itemView.findViewById(R.id.contact_dialog_item_user_checkbox);
        }
        void onBindView(int position){
            User user= getItem(position);
            ImageLoaderKit.getInstance().displayImage(avatarImage.getContext(),user.getAvatar(),R.drawable.default_avatar,R.drawable.default_avatar,avatarImage);
            nameText.setText(user.name);
            titleText.setText(user.getTitle().getTitleName());
            checkBox.setChecked(selectUsers.contains(user));
        }
    }
}
