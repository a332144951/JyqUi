package com.jyq.android.ui.widget.dialog;

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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jyq.android.ui.R;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/18.
 */

public class MenuDialog extends JDialogFragment {
    private CharSequence[] menus;
    private int dangerousIndex = -1;
    private MenuAdapter adapter;
    private OnItemClickListener onClickListener;

    private static final String KEY_ITEMS = "items";
    private static final String KEY_DANGER = "danger";
    private static final String KEY_LISTENER = "listener";

    public static MenuDialog createMenuDialog(CharSequence[] items, int dangerousIndex, OnItemClickListener onItemClick) {
        MenuDialog dialog = new MenuDialog();
        Bundle bundle = new Bundle();
        bundle.putCharSequenceArray(KEY_ITEMS, items);
        bundle.putInt(KEY_DANGER, dangerousIndex);
        bundle.putSerializable(KEY_LISTENER, onItemClick);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent();
    }

    private void parseIntent() {
        menus = getArguments().getCharSequenceArray(KEY_ITEMS);
        dangerousIndex = getArguments().getInt(KEY_DANGER, -1);
        onClickListener = (OnItemClickListener) getArguments().getSerializable(KEY_LISTENER);
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        adapter = new MenuAdapter(getActivity(), menus, dangerousIndex);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setSingleChoiceItems(adapter, 0, mClickListener)
                .create();
        dialog.getListView().setDivider(getResources().getDrawable(R.color.toolbar_line_color));

        dialog.getListView().setDividerHeight(getResources().getDimensionPixelOffset(R.dimen.line_height));
        return dialog;
    }

    private DialogInterface.OnClickListener mClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
                if (onClickListener!=null){
                    onClickListener.onClick(dialog, which);
                }
        }
    };

    @Override
    protected boolean hasTitle() {
        return false;
    }


    public interface OnItemClickListener extends Serializable {
        void onClick(DialogInterface dialog, int which);
    }

    private class MenuAdapter extends ArrayAdapter {
        private int dangerousIndex;
        private final int defaultColor;
        private final int dangerColor;

        public MenuAdapter(@NonNull Context context, CharSequence[] items, int dangerousIndex) {
            super(context, R.layout.menu_dialog_item_layout, items);
            this.dangerousIndex = dangerousIndex;
            this.defaultColor = context.getResources().getColor(R.color.toolbar_title_text_color);
            this.dangerColor = context.getResources().getColor(R.color.menu_dialog_danger_text_color);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            if (position == dangerousIndex) {
                ((TextView) view).setTextColor(dangerColor);
            } else {
                ((TextView) view).setTextColor(defaultColor);
            }
            return view;
        }
    }
}
