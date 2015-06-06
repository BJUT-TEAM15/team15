package net.sfte.htlibrary.database;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

/**
 * This class defines the Book of htlibrary.
 * 
 * @author wenwen
 */
public class Book {
	public Book(int aId, String aIsbn, String aTitle, String aAuthor,
			String aPublisher, double aPrice, int aTotalAmount, int aAmount,
			String aAddDate, int aBorrowedTimes) {
		id = aId;
		isbn = aIsbn;
		title = aTitle;
		author = aAuthor;
		publisher = aPublisher;
		price = aPrice;
		totalAmount = aTotalAmount;
		amount = aAmount;
		addDateAsString = aAddDate;
		StringTokenizer stk = new StringTokenizer(addDateAsString, "-");
		int year = Integer.parseInt(stk.nextToken());
		int month = Integer.parseInt(stk.nextToken());
		int day = Integer.parseInt(stk.nextToken());
		GregorianCalendar g = new GregorianCalendar(year, month - 1, day);
		addDate = g.getTime();
		borrowedTimes = aBorrowedTimes;
	}

	public Book(int aId, String aIsbn, String aTitle, String aAuthor,
			String aPublisher, double aPrice, int aTotalAmount, int aAmount,
			GregorianCalendar aAddDate, int aBorrowedTimes) {
		id = aId;
		isbn = aIsbn;
		title = aTitle;
		author = aAuthor;
		publisher = aPublisher;
		price = aPrice;
		totalAmount = aTotalAmount;
		amount = aAmount;
		addDate = aAddDate.getTime();
		int year = aAddDate.get(Calendar.YEAR);
		int month = aAddDate.get(Calendar.MONTH) + 1;
		int day = aAddDate.get(Calendar.DAY_OF_MONTH);
		addDateAsString = year + "-" + month + "-" + day;
		borrowedTimes = aBorrowedTimes;
	}

	public int getId() {
		return id;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getPublisher() {
		return publisher;
	}

	public double getPrice() {
		return price;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public int getAmount() {
		return amount;
	}

	public Date getAddDate() {
		return addDate;
	}

	public String getAddDateAsString() {
		return addDateAsString;
	}

	public int getBorrowedTimes() {
		return borrowedTimes;
	}

	private int id = 0;

	private String isbn = "";

	private String title = "";

	private String author = "";

	private String publisher = "";

	private double price = 0;

	private int totalAmount = 0;

	private int amount = 0;

	private Date addDate = null;

	private String addDateAsString = "";

	// book borrowed times
	private int borrowedTimes = 0;
}