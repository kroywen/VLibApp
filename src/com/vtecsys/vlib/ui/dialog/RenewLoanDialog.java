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
import com.vtecsys.vlib.model.Loan;
import com.vtecsys.vlib.util.Utilities;

public class RenewLoanDialog extends DialogFragment implements OnClickListener {
	
	public interface OnRenewClickListener {
		void onRenewYesClick(DialogFragment dialog, Loan loan);
		void onRenewNoClick(DialogFragment dialog);
		void onRenewOkClick(DialogFragment dialog);
	}
	
	public static final int MODE_BEFORE_RENEW = 0;
	public static final int MODE_AFTER_RENEW = 1;
	
	private OnRenewClickListener listener;
	private Loan loan;
	private int mode;
	
	private TextView dialogTitle;
	private TextView itemNo;
	private View itemNoContainer;
	private TextView dueDate;
	private View dueDateContainer;
	private TextView title;
	private TextView author;
	private TextView publisher;
	private Button yesBtn;
	private Button noBtn;
	private Button okBtn;
	
	public void setLoan(Loan loan) {
		this.loan = loan;
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	private void initializeViews(View view) {
		dialogTitle = (TextView) view.findViewById(R.id.dialogTitle);
		dialogTitle.setText(mode == MODE_BEFORE_RENEW ? 
			R.string.confirm_renew_title : 
			R.string.renew_success_title);		
		
		itemNo = (TextView) view.findViewById(R.id.itemNo);
		itemNo.setText(loan.getItemNumber());
		
		itemNoContainer = view.findViewById(R.id.itemNoContainer);
		itemNoContainer.setVisibility(mode == MODE_AFTER_RENEW ? 
			View.VISIBLE : View.GONE);
		
		dueDate = (TextView) view.findViewById(R.id.dueDate);
		dueDate.setText(Utilities.convertDate(loan.getDueDate()));
		
		dueDateContainer = view.findViewById(R.id.dueDateContainer);
		dueDateContainer.setVisibility(mode == MODE_AFTER_RENEW ? 
			View.VISIBLE : View.GONE);
		
		title = (TextView) view.findViewById(R.id.title);
		title.setText(loan.getTitle());
		
		author = (TextView) view.findViewById(R.id.author);
		author.setText(loan.getAuthor());
		
		publisher = (TextView) view.findViewById(R.id.publisher);
		publisher.setText(loan.getPublisher());
		
		yesBtn = (Button) view.findViewById(R.id.yesBtn);
		yesBtn.setOnClickListener(this);
		yesBtn.setVisibility(mode == MODE_BEFORE_RENEW ?
			View.VISIBLE : View.GONE);		
		
		noBtn = (Button) view.findViewById(R.id.noBtn);
		noBtn.setOnClickListener(this);
		noBtn.setVisibility(mode == MODE_BEFORE_RENEW ?
				View.VISIBLE : View.GONE);
		
		okBtn = (Button) view.findViewById(R.id.okBtn);
		okBtn.setOnClickListener(this);
		okBtn.setVisibility(mode == MODE_AFTER_RENEW ?
				View.VISIBLE : View.GONE);
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnRenewClickListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.renew_loan_dialog, null);
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
				listener.onRenewYesClick(RenewLoanDialog.this, loan);
				break;
			case R.id.noBtn:
				listener.onRenewNoClick(RenewLoanDialog.this);
				break;
			case R.id.okBtn:
				listener.onRenewOkClick(RenewLoanDialog.this);
				break;
			}
		}
		dismiss();
	}

}
