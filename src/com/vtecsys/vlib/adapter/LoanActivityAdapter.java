package com.vtecsys.vlib.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.model.Loan;

public class LoanActivityAdapter extends BaseAdapter {
	
	private Context context;
	private List<Loan> loans;
	
	public LoanActivityAdapter(Context context, List<Loan> loans) {
		this.context = context;
		this.loans = loans;
	}

	@Override
	public int getCount() {
		return loans.size();
	}

	@Override
	public Loan getItem(int position) {
		return loans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.loan_activity_list_item, null);
		}
		
		Loan loan = getItem(position);
		
		TextView itemNumber = (TextView) convertView.findViewById(R.id.itemNumber);
		itemNumber.setText(loan.getItemNumber());
		
		TextView callNumber = (TextView) convertView.findViewById(R.id.callNumber);
		callNumber.setText(loan.getCallNumber());
		
		TextView dueDate = (TextView) convertView.findViewById(R.id.dueDate);
		dueDate.setText(loan.getDueDate());
		dueDate.setTextColor(loan.isOverdue() ? Color.RED : Color.BLUE);
		
		TextView title = (TextView) convertView.findViewById(R.id.title);
		title.setText(loan.getTitle());
		
		Button renew = (Button) convertView.findViewById(R.id.renewBtn);
		renew.setEnabled(loan.canRenew());
		// TODO
		
		return convertView;
	}

}