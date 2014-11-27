package com.vtecsys.vlib.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vtecsys.vlib.R;

public class DialogUtils {
	
	@SuppressLint("InflateParams")
	public static void showDialog(Context context, 
			String title, String message, 
			final DialogInterface.OnClickListener okListener, 
			DialogInterface.OnCancelListener cancelListener)
	{		
		LayoutInflater inflater = (LayoutInflater)
			context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.simple_dialog, null);
		
		LocaleManager locale = LocaleManager.getInstance();
		locale.apply(view);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(view);
		builder.setOnCancelListener(cancelListener);
		final AlertDialog dialog = builder.create();
		
		TextView titleView = (TextView) view.findViewById(R.id.title);
		titleView.setText(title);
		titleView.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);
		
		TextView messageView = (TextView) view.findViewById(R.id.message);
		messageView.setText(message);
		
		Button okBtn = (Button) view.findViewById(R.id.okBtn);
		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (okListener != null) {
					okListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
				}
			}
		});
		
		dialog.show();
	}
	
	@SuppressLint("InflateParams")
	public static void showConfirmDialog(Context context, 
			String title, String message, 
			final DialogInterface.OnClickListener okListener, 
			final DialogInterface.OnCancelListener cancelListener)
	{		
		LayoutInflater inflater = (LayoutInflater)
			context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.confirm_dialog, null);
		
		LocaleManager locale = LocaleManager.getInstance();
		locale.apply(view);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(view);
		builder.setOnCancelListener(cancelListener);
		final AlertDialog dialog = builder.create();
		
		TextView titleView = (TextView) view.findViewById(R.id.title);
		titleView.setText(title);
		titleView.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);
		
		TextView messageView = (TextView) view.findViewById(R.id.message);
		messageView.setText(message);
		
		Button okBtn = (Button) view.findViewById(R.id.okBtn);
		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (okListener != null) {
					okListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
				}
			}
		});
		
		Button cancelBtn = (Button) view.findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (cancelListener != null) {
					cancelListener.onCancel(dialog);
				}
			}
		});
		
		dialog.show();
	}
	
	public static void showDialog(Context context, int titleId, int messageId,
		DialogInterface.OnClickListener okListener, 
		DialogInterface.OnCancelListener cancelListener)
	{
		showDialog(context, context.getString(titleId), 
			context.getString(messageId), okListener, cancelListener);
	}
	
	public static void showDialog(Context context, int titleId, int messageId) {
		showDialog(context, context.getString(titleId), 
			context.getString(messageId), null, null);
	}
	
	public static void showDialog(Context context, String title, String message) {
		showDialog(context, title, message, null, null);
	}

}
