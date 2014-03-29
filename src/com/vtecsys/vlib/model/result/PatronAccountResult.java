package com.vtecsys.vlib.model.result;

import java.util.ArrayList;
import java.util.List;

import com.vtecsys.vlib.model.Loan;
import com.vtecsys.vlib.model.Patron;
import com.vtecsys.vlib.model.Reservation;

public class PatronAccountResult {
	
	private Patron patron;
	private List<Loan> loans;
	private List<Reservation> reservations;
	
	public PatronAccountResult() {
		loans = new ArrayList<Loan>();
		reservations = new ArrayList<Reservation>();
	}
	
	public Patron getPatron() {
		return patron;
	}
	
	public void setPatron(Patron patron) {
		this.patron = patron;
	}
	
	public List<Loan> getLoans() {
		return loans;
	}
	
	public void setLoans(List<Loan> loans) {
		this.loans = loans;
	}
	
	public void addLoan(Loan loan) {
		if (loans != null) {
			loans.add(loan);
		}
	}
	
	public List<Reservation> getReservations() {
		return reservations;
	}
	
	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
	
	public void addReservation(Reservation reservation) {
		if (reservations != null) {
			reservations.add(reservation);
		}
	}

}
