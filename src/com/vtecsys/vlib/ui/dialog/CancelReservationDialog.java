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
import com.vtecsys.vlib.model.Reservation;
import com.vtecsys.vlib.util.LocaleManager;

public class CancelReservationDialog extends DialogFragment implements OnClickListener {
	
	public interface OnCancelReservationClickListener {
		void onCancelReservationYesClick(DialogFragment dialog, Reservation reservation);
		void onCancelReservationNoClick(DialogFragment dialog);
	}
	
	private OnCancelReservationClickListener listener;
	private Reservation reservation;
	
	private TextView dialogTitle;
	private TextView title;
	private Button yesBtn;
	private Button noBtn;
	
	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}
	
	private void initializeViews(View view) {
		LocaleManager locale = LocaleManager.getInstance();
		locale.apply(view);
		
		dialogTitle = (TextView) view.findViewById(R.id.dialogTitle);
		dialogTitle.setText(locale.get(LocaleManager.TITLE_CONFIRM_CANCEL_RESERVATION));		
				
		title = (TextView) view.findViewById(R.id.title);
		title.setText(reservation.getTitle());
		
		yesBtn = (Button) view.findViewById(R.id.yesBtn);
		yesBtn.setOnClickListener(this);		
		
		noBtn = (Button) view.findViewById(R.id.noBtn);
		noBtn.setOnClickListener(this);
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnCancelReservationClickListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.cancel_reservation_dialog, null);
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
				listener.onCancelReservationYesClick(CancelReservationDialog.this, reservation);
				break;
			case R.id.noBtn:
				listener.onCancelReservationNoClick(CancelReservationDialog.this);
				break;
			}
		}
		dismiss();
	}

}
