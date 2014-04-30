package com.vtecsys.vlib.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.model.Notices;

public class NoticesDialog extends DialogFragment implements OnClickListener {
	
	private TextView noticeText;
	private ImageView closeBtn;
	
	private Notices notices;
	
	public void setNotices(Notices notices) {
		this.notices = notices;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.notices_dialog, null);
	    initializeViews(view);
		
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setView(view);
	    Dialog dialog = builder.create();
	    alignDialogBottom(dialog);
	    return dialog;
	}
	
	private void initializeViews(View rootView) {
		noticeText = (TextView) rootView.findViewById(R.id.noticeText);
		String text = Notices.getNoticesText(notices);
		noticeText.setText(text);
		
		closeBtn = (ImageView) rootView.findViewById(R.id.closeBtn);
		closeBtn.setOnClickListener(this);
	}
	
	private void alignDialogBottom(Dialog dialog) {
		Window window = dialog.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.gravity = Gravity.BOTTOM;
		params.verticalMargin = .01f;
		params.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		window.setAttributes(params);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.closeBtn) {
			dismiss();
		}
	}

}
