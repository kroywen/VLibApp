package com.vtecsys.vlib.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtils {
	
	public static void showDialog(Context context, 
			String title, String message, 
			DialogInterface.OnClickListener okListener, 
			DialogInterface.OnCancelListener cancelListener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title)
			.setMessage(message)
			.setPositiveButton(android.R.string.ok, okListener)
			.setOnCancelListener(cancelListener)
			.create()
			.show();
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
