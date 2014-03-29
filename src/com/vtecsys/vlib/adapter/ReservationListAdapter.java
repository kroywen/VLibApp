package com.vtecsys.vlib.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.model.Reservation;

public class ReservationListAdapter extends BaseAdapter {
	
	private Context context;
	private List<Reservation> reservations;
	
	public ReservationListAdapter(Context context, List<Reservation> reservations) {
		this.context = context;
		this.reservations = reservations;
	}

	@Override
	public int getCount() {
		return reservations.size();
	}

	@Override
	public Reservation getItem(int position) {
		return reservations.get(position);
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
			convertView = inflater.inflate(R.layout.reservation_list_item, null);
		}
		
		Reservation reservation = getItem(position);
		
		TextView reserveDate = (TextView) convertView.findViewById(R.id.reserveDate);
		reserveDate.setText(reservation.getReserveDate());
		
		TextView callNumber = (TextView) convertView.findViewById(R.id.callNumber);
		callNumber.setText(reservation.getCallNumber());
		
		TextView volumeNumber = (TextView) convertView.findViewById(R.id.volumeNumber);
		volumeNumber.setText(reservation.getVolume());
		
		TextView readyDate = (TextView) convertView.findViewById(R.id.readyDate);
		readyDate.setText(reservation.getIsReady());
		
		TextView title = (TextView) convertView.findViewById(R.id.title);
		title.setText(reservation.getTitle());
		
		Button cancelBtn = (Button) convertView.findViewById(R.id.cancelBtn);
		cancelBtn.setEnabled(reservation.canCancel());
		
		return convertView;
	}

}
