package net.sfte.htlibrary.database;

import java.util.Date;

/**
 * This class defines the administrator. A admin has id, name, password,
 * createDate, phone and email fields.
 * 
 * @author wenwen
 */
public class Admin {
	public Admin(String aName) {
		name = aName;
	}

	public Admin(String aName, char[] aPassword) {
		name = aName;
		password = aPassword;
	}

	public Admin(int aId, String aName, char[] aPassword, String aDateString,
			String aPhone, String aEmail) {
		id = aId;
		name = aName;
		password = aPassword;
		createDateAsString = aDateString;
		phone = aPhone;
		email = aEmail;
	}

	public Admin(int aId, String aName, char[] aPassword, Date aDate,
			String aPhone, String aEmail) {
		id = aId;
		name = aName;
		password = aPassword;
		createDate = aDate;
		phone = aPhone;
		email = aEmail;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public char[] getPassword() {
		return password;
	}

	public String getPasswordAsString() {
		return new String(password);
	}

	public Date getCreateDate() {
		return (Date) createDate.clone();
	}

	public String getCreateDateAsString() {
		return createDateAsString;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public void setName(String aName) {
		name = aName;
	}

	public void setPassword(String aPassword) {
		name = aPassword;
	}

	public void setPhone(String aPhone) {
		phone = aPhone;
	}

	public void setEmail(String aEmail) {
		email = aEmail;
	}

	private int id = 0;

	private String name = "";

	// for security reasons, the password is stroed as
	// a char[], not a String.
	private char[] password = {};

	private Date createDate = new Date();

	private String createDateAsString = "";

	private String phone = "";

	private String email = "";
}