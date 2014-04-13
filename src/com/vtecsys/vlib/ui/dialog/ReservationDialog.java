package com.vtecsys.vlib.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.model.Book;
import com.vtecsys.vlib.model.Volume;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.util.LocaleManager;

public class ReservationDialog extends DialogFragment implements OnClickListener {
	
	public interface OnReservationClickListener {
		void onReservationYesClick(DialogFragment dialog, Volume volume);
		void onReservationNoClick(DialogFragment dialog);
		void onReservationOkClick(DialogFragment dialog);
	}
	
	public static final int MODE_BEFORE_RESERVATION = 0;
	public static final int MODE_AFTER_RESERVATION = 1;
	
	private OnReservationClickListener listener;
	private Book book;
	private Volume volume;
	private int mode;
	
	private TextView dialogTitle;
	private TextView memberId;
	private View memberIdContainer;
	private TextView memberName;
	private View memberNameContainer;
	private TextView title;
	private TextView author;
	private TextView publisher;
	private Button yesBtn;
	private Button noBtn;
	private Button okBtn;
	
	public void setBook(Book book) {
		this.book = book;
	}
	
	public void setVolume(Volume volume) {
		this.volume = volume;
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	private void initializeViews(View view) {
		Settings settings = new Settings(getActivity());
		LocaleManager locale = LocaleManager.getInstance();
		locale.apply(view);
		
		dialogTitle = (TextView) view.findViewById(R.id.dialogTitle);
		dialogTitle.setText(mode == MODE_BEFORE_RESERVATION ? 
			locale.get(LocaleManager.TITLE_CONFIRM_RESERVE) : 
			locale.get(LocaleManager.TITLE_RESERVED));		
		
		memberId = (TextView) view.findViewById(R.id.memberId);
		memberId.setText(settings.getString(Settings.MEMBER_ID));
		
		memberIdContainer = view.findViewById(R.id.memberIdContainer);
		memberIdContainer.setVisibility(mode == MODE_AFTER_RESERVATION ? 
			View.VISIBLE : View.GONE);
		
		memberName = (TextView) view.findViewById(R.id.memberName);
		memberName.setText(settings.getString(Settings.MEMBER_NAME));
		
		memberNameContainer = view.findViewById(R.id.memberNameContainer);
		memberNameContainer.setVisibility(mode == MODE_AFTER_RESERVATION ? 
			View.VISIBLE : View.GONE);
		
		title = (TextView) view.findViewById(R.id.title);
		title.setText(book.getTitle());
		
		author = (TextView) view.findViewById(R.id.author);
		author.setText(book.getAuthor());
		
		publisher = (TextView) view.findViewById(R.id.publisher);
		publisher.setText(book.getPublisher());
		
		yesBtn = (Button) view.findViewById(R.id.yesBtn);
		yesBtn.setOnClickListener(this);
		yesBtn.setVisibility(mode == MODE_BEFORE_RESERVATION ?
			View.VISIBLE : View.GONE);		
		
		noBtn = (Button) view.findViewById(R.id.noBtn);
		noBtn.setOnClickListener(this);
		noBtn.setVisibility(mode == MODE_BEFORE_RESERVATION ?
				View.VISIBLE : View.GONE);
		
		okBtn = (Button) view.findViewById(R.id.okBtn);
		okBtn.setOnClickListener(this);
		okBtn.setVisibility(mode == MODE_AFTER_RESERVATION ?
				View.VISIBLE : View.GONE);
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnReservationClickListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.reservation_dialog, null);
	    initializeViews(view);
		
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setView(view);
	    return builder.create();
	}

	@Override
	public void onClick(View v) {
		if (listener != null) {
			switch (v.getId()) {
			case R.id.yesBtn:
				listener.onReservationYesClick(ReservationDialog.this, volume);
				break;
			case R.id.noBtn:
				listener.onReservationNoClick(ReservationDialog.this);
				break;
			case R.id.okBtn:
				listener.onReservationOkClick(ReservationDialog.this);
				break;
			}
		}
		dismiss();
	}

}
