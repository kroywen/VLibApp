package com.vtecsys.vlib.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtils {
	
	public static void showErrorDialog(Context context, 
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
	
	public static void showErrorDialog(Context context, int titleId, int messageId,
		DialogInterface.OnClickListener okListener, 
		DialogInterface.OnCancelListener cancelListener)
	{
		showErrorDialog(context, context.getString(titleId), 
			context.getString(messageId), okListener, cancelListener);
	}
	
	public static void showErrorDialog(Context context, int titleId, int messageId) {
		showErrorDialog(context, context.getString(titleId), 
			context.getString(messageId), null, null);
	}
	
	public static void showErrorDialog(Context context, String title, String message) {
		showErrorDialog(context, title, message, null, null);
	}

}
