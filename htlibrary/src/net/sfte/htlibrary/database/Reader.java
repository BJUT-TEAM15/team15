package net.sfte.htlibrary.database;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

/**
 * This class defines the Reader.
 * 
 * @author wenwen
 */
public class Reader {
	public Reader(int id, String stu_id, String aName, int aAge, String aSex,
			String aAcademy, String aDepartment, GregorianCalendar aDate,
			int amount, String aSummary) {
		readerId = id;
		studentId = stu_id;
		name = aName;
		age = aAge;
		sex = aSex;
		academy = aAcademy;
		department = aDepartment;
		registerDate = aDate.getTime();
		int year = aDate.get(Calendar.YEAR);
		int month = aDate.get(Calendar.MONTH) + 1;
		int day = aDate.get(Calendar.DAY_OF_MONTH);
		registerDateAsString = year + "-" + month + "-" + day;
		bookAmount = amount;
		summary = aSummary;
	}

	public Reader(int id, String stu_id, String aName, int aAge, String aSex,
			String aAcademy, String aDepartment, String aDateAsString,
			int amount, String aSummary) {
		readerId = id;
		studentId = stu_id;
		name = aName;
		age = aAge;
		sex = aSex;
		academy = aAcademy;
		department = aDepartment;
		registerDateAsString = aDateAsString;
		StringTokenizer stk = new StringTokenizer(aDateAsString, "-");
		int year = Integer.parseInt(stk.nextToken());
		int month = Integer.parseInt(stk.nextToken());
		int day = Integer.parseInt(stk.nextToken());
		GregorianCalendar g = new GregorianCalendar(year, month - 1, day);
		registerDate = g.getTime();
		bookAmount = amount;
		summary = aSummary;
	}

	public int getReaderId() {
		return readerId;
	}

	public String getStudentId() {
		return studentId;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public String getSex() {
		return sex;
	}

	public String getAcademy() {
		return academy;
	}

	public String getDepartment() {
		return department;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public String getRegisterDateAsString() {
		return registerDateAsString;
	}

	public int getBookAmount() {
		return bookAmount;
	}

	public String getSummary() {
		return summary;
	}

	private int readerId = 0;

	private String studentId = "";

	private String name = "";

	private int age = 0;

	private String sex = "";

	private String academy = "";

	private String department = "";

	private Date registerDate = null;

	private String registerDateAsString = "";

	// reader borrowed books total amount.
	private int bookAmount = 0;

	private String summary = "";
}