package com.jyq.android.ui.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.jyq.android.common.log.LogUtil;


public class DialogMaker {
	private static JProgressDialog progressDialog;

    public static JProgressDialog showProgressDialog(Context context, String message) {
        return showProgressDialog(context, null, message, true, null);
    }

	public static JProgressDialog showProgressDialog(Context context, String message, boolean cancelable) {
		return showProgressDialog(context, null, message, cancelable, null);
	}
	
	@Deprecated
	public static JProgressDialog showProgressDialog(Context context,
													 String title, String message, boolean canCancelable, OnCancelListener listener) {

		if (progressDialog == null) {
			progressDialog = new JProgressDialog(context, message);
		} else if (progressDialog.getContext() != context) {
			// maybe existing dialog is running in a destroyed activity cotext
			// we should recreate one
			LogUtil.e("dialog", "there is a leaked window here,orign context: "
					+ progressDialog.getContext() + " now: " + context);
			dismissProgressDialog();
			progressDialog = new JProgressDialog(context, message);
		}

		progressDialog.setCancelable(canCancelable);
		progressDialog.setOnCancelListener(listener);

		progressDialog.show();

		return progressDialog;
	}
	
	public static void dismissProgressDialog() {
		if (null == progressDialog) {
			return;
		}
		if (progressDialog.isShowing()) {
			try {
				progressDialog.dismiss();
				progressDialog = null;
			} catch (Exception e) {
				// maybe we catch IllegalArgumentException here.
			}

		}

	}
	public static void setMessage(String message) {
		if (null != progressDialog && progressDialog.isShowing()
				&& !TextUtils.isEmpty(message)) {
			progressDialog.setMessage(message);
		}
	}

	public static void updateLoadingMessage(String message) {
		if (null != progressDialog && progressDialog.isShowing()
				&& !TextUtils.isEmpty(message)) {
			progressDialog.updateLoadingMessage(message);
		}
	}
	
	public static boolean isShowing() {
		return (progressDialog != null && progressDialog.isShowing());
	}


	private static AlertDialog confirmDialog;
	public static AlertDialog createConfirmDialog(Context context, String content, DialogInterface.OnClickListener onOK){
		return createConfirmDialog(context, content, "确定", "取消", onOK, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
	}
	public static AlertDialog createConfirmDialog(Context context,String content, String okBtn, String cancelBtn, DialogInterface.OnClickListener onOk, DialogInterface.OnClickListener onCancel){
		return createConfirmDialog(context,"提示",content, okBtn, cancelBtn, onOk, onCancel);
	}
	public static AlertDialog createConfirmDialog(Context context, String title, String message, String okbtn, String cancelbtn,
												  DialogInterface.OnClickListener onOk, DialogInterface.OnClickListener onCancel){
		if (confirmDialog==null){
			confirmDialog=new AlertDialog.Builder(context).create();
		}else if(confirmDialog.getContext()!=context){
			dismissConfirmDialog();
			confirmDialog=new AlertDialog.Builder(context).create();
		}
		confirmDialog.setTitle(title);
		confirmDialog.setMessage(message);
		confirmDialog.setButton(DialogInterface.BUTTON_POSITIVE,okbtn,onOk);
		confirmDialog.setButton(DialogInterface.BUTTON_NEGATIVE,cancelbtn,onCancel);
		return confirmDialog;
	}
	public static void dismissConfirmDialog(){
		if (confirmDialog==null){
			return;
		}
		if (confirmDialog.isShowing()) {
			try {
				confirmDialog.dismiss();
				confirmDialog = null;
			} catch (Exception e) {
				// maybe we catch IllegalArgumentException here.
			}

		}
	}
}
