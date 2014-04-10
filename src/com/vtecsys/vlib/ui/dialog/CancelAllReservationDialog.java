package com.vtecsys.vlib.ui.dialog;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.model.Reservation;
import com.vtecsys.vlib.util.Utilities;

public class CancelAllReservationDialog extends DialogFragment implements OnClickListener {
	
	public interface OnCancelAllReservationClickListener {
		void onCancelAllReservationYesClick(DialogFragment dialog, List<Reservation> reservations);
		void onCancelAllReservationNoClick(DialogFragment dialog);
	}
	
	private OnCancelAllReservationClickListener listener;
	private List<Reservation> reservations;
	
	private TextView dialogTitle;
	private LinearLayout titleContainer;
	private Button yesBtn;
	private Button noBtn;
	
	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
	
	private void initializeViews(View view) {
		dialogTitle = (TextView) view.findViewById(R.id.dialogTitle);
		dialogTitle.setText(R.string.confirm_cancel_reservation_title);		
				
		titleContainer = (LinearLayout) view.findViewById(R.id.titleContainer);
		populateTitleContainer();
		
		yesBtn = (Button) view.findViewById(R.id.yesBtn);
		yesBtn.setOnClickListener(this);		
		
		noBtn = (Button) view.findViewById(R.id.noBtn);
		noBtn.setOnClickListener(this);
	}
	
	private void populateTitleContainer() {
		titleContainer.removeAllViews();
		if (Utilities.isEmpty(reservations)) {
			return;
		}
		
		LayoutInflater inflater = (LayoutInflater) 
			getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i=0; i<reservations.size(); i++) {
			Reservation reservation = reservations.get(i);
			TextView title = (TextView) 
				inflater.inflate(R.layout.cancel_all_reservation_item, null);
			title.setText((i + 1) + ". " + reservation.getTitle());
			titleContainer.addView(title);
		}	
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnCancelAllReservationClickListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.cancel_all_reservation_dialog, null);
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
				listener.onCancelAllReservationYesClick(CancelAllReservationDialog.this, reservations);
				break;
			case R.id.noBtn:
				listener.onCancelAllReservationNoClick(CancelAllReservationDialog.this);
				break;
			}
		}
		dismiss();
	}

}
