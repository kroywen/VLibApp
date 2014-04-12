package com.vtecsys.vlib.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vtecsys.vlib.R;

public class DialogUtils {
	
	public static void showDialog(Context context, 
			String title, String message, 
			final DialogInterface.OnClickListener okListener, 
			DialogInterface.OnCancelListener cancelListener)
	{
//		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		builder.setTitle(title)
//			.setMessage(message)
//			.setPositiveButton(android.R.string.ok, okListener)
//			.setOnCancelListener(cancelListener)
//			.create()
//			.show();
		
		LayoutInflater inflater = (LayoutInflater)
			context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.simple_dialog, null);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(view);
		builder.setOnCancelListener(cancelListener);
		final AlertDialog dialog = builder.create();
		
		TextView titleView = (TextView) view.findViewById(R.id.title);
		titleView.setText(title);
		
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
