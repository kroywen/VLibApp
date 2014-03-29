package com.vtecsys.vlib.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.ui.screen.ChangePasswordScreen;
import com.vtecsys.vlib.ui.screen.LoanActivitiesScreen;
import com.vtecsys.vlib.ui.screen.ReservationListScreen;

public class AccountFragment extends BaseFragment implements OnClickListener {
	
	private Button loanActivitiesBtn;
	private Button reservationListBtn;
	private Button changePasswordBtn;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.account_fragment, null);
		initializeViews(rootView);
		return rootView;
	}
	
	@Override
	protected void initializeViews(View rootView) {
		super.initializeViews(rootView);
		loanActivitiesBtn = (Button) rootView.findViewById(R.id.loanActivitiesBtn);
		loanActivitiesBtn.setOnClickListener(this);
		reservationListBtn = (Button) rootView.findViewById(R.id.reservationListBtn);
		reservationListBtn.setOnClickListener(this);
		changePasswordBtn = (Button) rootView.findViewById(R.id.changePasswordBtn);
		changePasswordBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loanActivitiesBtn:
			Intent intent = new Intent(getActivity(), LoanActivitiesScreen.class);
			getActivity().startActivity(intent);
			break;
		case R.id.reservationListBtn:
			intent = new Intent(getActivity(), ReservationListScreen.class);
			getActivity().startActivity(intent);
			break;
		case R.id.changePasswordBtn:
			intent = new Intent(getActivity(), ChangePasswordScreen.class);
			getActivity().startActivity(intent);
			break;
		}
	}

}
