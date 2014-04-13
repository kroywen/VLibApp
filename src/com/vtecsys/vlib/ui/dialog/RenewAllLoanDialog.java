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
import com.vtecsys.vlib.model.Loan;
import com.vtecsys.vlib.util.LocaleManager;
import com.vtecsys.vlib.util.Utilities;

public class RenewAllLoanDialog extends DialogFragment implements OnClickListener {
	
	public interface OnRenewAllClickListener {
		void onRenewAllYesClick(DialogFragment dialog, List<Loan> loans);
		void onRenewAllNoClick(DialogFragment dialog);
	}
	
	private OnRenewAllClickListener listener;
	private List<Loan> loans;
	
	private TextView dialogTitle;
	private LinearLayout titleContainer;
	private Button yesBtn;
	private Button noBtn;
	
	public void setLoans(List<Loan> loans) {
		this.loans = loans;
	}
	
	private void initializeViews(View view) {
		LocaleManager locale = LocaleManager.getInstance();
		locale.apply(view);
		
		dialogTitle = (TextView) view.findViewById(R.id.dialogTitle);
		dialogTitle.setText(locale.get(LocaleManager.TITLE_CONFIRM_RENEW));		
				
		titleContainer = (LinearLayout) view.findViewById(R.id.titleContainer);
		populateTitleContainer();
		
		yesBtn = (Button) view.findViewById(R.id.yesBtn);
		yesBtn.setOnClickListener(this);		
		
		noBtn = (Button) view.findViewById(R.id.noBtn);
		noBtn.setOnClickListener(this);
	}
	
	private void populateTitleContainer() {
		titleContainer.removeAllViews();
		if (Utilities.isEmpty(loans)) {
			return;
		}
		
		LayoutInflater inflater = (LayoutInflater) 
			getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i=0; i<loans.size(); i++) {
			Loan loan = loans.get(i);
			TextView title = (TextView) 
				inflater.inflate(R.layout.renew_all_loan_item, null);
			title.setText((i + 1) + ". " + loan.getTitle());
			titleContainer.addView(title);
		}	
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnRenewAllClickListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.renew_all_loan_dialog, null);
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
				listener.onRenewAllYesClick(RenewAllLoanDialog.this, loans);
				break;
			case R.id.noBtn:
				listener.onRenewAllNoClick(RenewAllLoanDialog.this);
				break;
			}
		}
		dismiss();
	}

}
