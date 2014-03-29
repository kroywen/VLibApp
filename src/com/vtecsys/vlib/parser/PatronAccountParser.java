package com.vtecsys.vlib.parser;

import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.vtecsys.vlib.model.Loan;
import com.vtecsys.vlib.model.Patron;
import com.vtecsys.vlib.model.Reservation;
import com.vtecsys.vlib.model.result.PatronAccountResult;

public class PatronAccountParser extends ApiParser {

	@Override
	public Object readData(XmlPullParser parser) 
		throws XmlPullParserException, IOException 

	{
		PatronAccountResult result = new PatronAccountResult();
		Patron patron = new Patron();
		
		parser.require(XmlPullParser.START_TAG, namespace, TAG_RESPONSE_DATA);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String tagName = parser.getName();
			if (TAG_PATRON.equalsIgnoreCase(tagName)) {
				readPatron(parser, patron);
			} else if (TAG_LOANS.equalsIgnoreCase(tagName)) {
				readLoans(parser, result.getLoans());
			} else if (TAG_RESERVATIONS.equalsIgnoreCase(tagName)) {
				readReservations(parser, result.getReservations());
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_RESPONSE_DATA);
		
		result.setPatron(patron);
		return result;
	}
	
	private void readPatron(XmlPullParser parser, Patron patron)
		throws XmlPullParserException, IOException
	{
		if (patron == null) {
			return;
		}
		
		parser.require(XmlPullParser.START_TAG, namespace, TAG_PATRON);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			String tagName = parser.getName();
			if (TAG_ID.equalsIgnoreCase(tagName)) {
				String id = readString(parser, TAG_ID);
				patron.setId(id);
			} else if (TAG_NAME.equalsIgnoreCase(tagName)) {
				String name = readString(parser, TAG_NAME);
				patron.setName(name);
			} else if (TAG_SURNAME.equalsIgnoreCase(tagName)) {
				String surname = readString(parser, TAG_SURNAME);
				patron.setSurname(surname);
			} else if (TAG_STATUS.equalsIgnoreCase(tagName)) {
				String status = readString(parser, TAG_STATUS);
				patron.setStatus(status);
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_PATRON);
	}
	
	private void readLoans(XmlPullParser parser, List<Loan> loans)
		throws XmlPullParserException, IOException
	{
		if (loans == null) {
			return;
		}
		
		parser.require(XmlPullParser.START_TAG, namespace, TAG_LOANS);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			String tagName = parser.getName();
			if (TAG_ONE_LOAN.equalsIgnoreCase(tagName)) {
				Loan loan = readLoan(parser);
				loans.add(loan);
			} else {
				skip(parser);
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_LOANS);
	}
	
	private Loan readLoan(XmlPullParser parser)
		throws XmlPullParserException, IOException
	{
		Loan loan = new Loan();
		parser.require(XmlPullParser.START_TAG, namespace, TAG_ONE_LOAN);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			String tagName = parser.getName();
			if (TAG_ITEM_NUMBER.equalsIgnoreCase(tagName)) {
				String itemNumber = readString(parser, TAG_ITEM_NUMBER);
				loan.setItemNumber(itemNumber);
			} else if (TAG_LOAN_DATE.equalsIgnoreCase(tagName)) {
				String loanDate = readString(parser, TAG_LOAN_DATE);
				loan.setLoanDate(loanDate);
			} else if (TAG_DUE_DATE.equalsIgnoreCase(tagName)) {
				String dueDate = readString(parser, TAG_DUE_DATE);
				loan.setDueDate(dueDate);
			} else if (TAG_TITLE.equalsIgnoreCase(tagName)) {
				String title = readString(parser, TAG_TITLE);
				loan.setTitle(title);
			} else if (TAG_AUTHOR.equalsIgnoreCase(tagName)) {
				String author = readString(parser, TAG_AUTHOR);
				loan.setAuthor(author);
			} else if (TAG_PUBLICATION.equalsIgnoreCase(tagName)) {
				String publication = readString(parser, TAG_PUBLICATION);
				loan.setPublication(publication);
			} else if (TAG_LOCATION.equalsIgnoreCase(tagName)) {
				String location = readString(parser, TAG_LOCATION);
				loan.setLocation(location);
			} else if (TAG_COLLECTION.equalsIgnoreCase(tagName)) {
				String collection = readString(parser, TAG_COLLECTION);
				loan.setCollection(collection);
			} else if (TAG_CALL_NUMBER.equalsIgnoreCase(tagName)) {
				String callNumber = readString(parser, TAG_CALL_NUMBER);
				loan.setCallNumber(callNumber);
			} else if (TAG_SUFFIX.equalsIgnoreCase(tagName)) {
				String suffix = readString(parser, TAG_SUFFIX);
				loan.setSuffix(suffix);
			} else if (TAG_VOLUME.equalsIgnoreCase(tagName)) {
				String volume = readString(parser, TAG_VOLUME);
				loan.setVolume(volume);
			} else if (TAG_IS_OVERDUE.equalsIgnoreCase(tagName)) {
				String isOverdue = readString(parser, TAG_IS_OVERDUE);
				loan.setIsOverdue("Y".equalsIgnoreCase(isOverdue));
			} else if (TAG_CAN_RENEW.equalsIgnoreCase(tagName)) {
				String canRenew = readString(parser, TAG_CAN_RENEW);
				loan.setCanRenew("Y".equalsIgnoreCase(canRenew));
			} else if (TAG_RENEWED.equalsIgnoreCase(tagName)) {
				int renewed = readInt(parser, TAG_RENEWED);
				loan.setRenewed(renewed);
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_ONE_LOAN);
		return loan;
	}
	
	private void readReservations(XmlPullParser parser, List<Reservation> reservations)
		throws XmlPullParserException, IOException
	{
		if (reservations == null) {
			return;
		}
		
		parser.require(XmlPullParser.START_TAG, namespace, TAG_RESERVATIONS);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			String tagName = parser.getName();
			if (TAG_ONE_RESERVATION.equalsIgnoreCase(tagName)) {
				Reservation reservation = readReservation(parser);
				reservations.add(reservation);
			} else {
				skip(parser);
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_RESERVATIONS);
	}
	
	private Reservation readReservation(XmlPullParser parser) 
		throws XmlPullParserException, IOException
	{
		Reservation reservation = new Reservation();
		parser.require(XmlPullParser.START_TAG, namespace, TAG_ONE_RESERVATION);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			String tagName = parser.getName();
			if (TAG_RESERVE_DATE.equalsIgnoreCase(tagName)) {
				String reserveDate = readString(parser, TAG_RESERVE_DATE);
				reservation.setReserveDate(reserveDate);
			} else if (TAG_IS_READY.equalsIgnoreCase(tagName)) {
				String isReady = readString(parser, TAG_IS_READY);
				reservation.setIsReady(isReady);
			} else if (TAG_ITEM_READY.equalsIgnoreCase(tagName)) {
				String itemReady = readString(parser, TAG_ITEM_READY);
				reservation.setItemReady(itemReady);
			} else if (TAG_IS_BOOKING.equalsIgnoreCase(tagName)) {
				String isBooking = readString(parser, TAG_IS_BOOKING);
				reservation.setIsBooking(isBooking);
			} else if (TAG_TYPE.equalsIgnoreCase(tagName)) {
				String type = readString(parser, TAG_TYPE);
				reservation.setType(type);
			} else if (TAG_RID.equalsIgnoreCase(tagName)) {
				String rid = readString(parser, TAG_RID);
				reservation.setRID(rid);
			} else if (TAG_ISSUE.equalsIgnoreCase(tagName)) {
				String issue = readString(parser, TAG_ISSUE);
				reservation.setIssue(issue);
			} else if (TAG_VOLUME.equalsIgnoreCase(tagName)) {
				String volume = readString(parser, TAG_VOLUME);
				reservation.setVolume(volume);
			} else if (TAG_TITLE.equalsIgnoreCase(tagName)) {
				String title = readString(parser, TAG_TITLE);
				reservation.setTitle(title);
			} else if (TAG_AUTHOR.equalsIgnoreCase(tagName)) {
				String author = readString(parser, TAG_AUTHOR);
				reservation.setAuthor(author);
			} else if (TAG_PUBLICATION.equalsIgnoreCase(tagName)) {
				String publication = readString(parser, TAG_PUBLICATION);
				reservation.setPublication(publication);
			} else if (TAG_CALL_NUMBER.equalsIgnoreCase(tagName)) {
				String callNumber = readString(parser, TAG_CALL_NUMBER);
				reservation.setCallNumber(callNumber);
			} else if (TAG_CAN_CANCEL.equalsIgnoreCase(tagName)) {
				String canCancel = readString(parser, TAG_CAN_CANCEL);
				reservation.setCanCancel("Y".equalsIgnoreCase(canCancel));
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_ONE_RESERVATION);
		return reservation;
	}

}
