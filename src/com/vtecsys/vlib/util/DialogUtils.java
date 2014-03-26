package com.vtecsys.vlib.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtils {
	
	public static void showErrorDialog(Context context, 
			int titleId, int messageId, 
			DialogInterface.OnClickListener okListener, 
			DialogInterface.OnCancelListener cancelListener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(titleId)
			.setMessage(messageId)
			.setPositiveButton(android.R.string.ok, okListener)
			.setOnCancelListener(cancelListener)
			.create()
			.show();
	}
	
	public static void showErrorDialog(Context context, int titleId, int messageId) {
		showErrorDialog(context, titleId, messageId, null, null);
	}

}
