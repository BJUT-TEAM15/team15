package net.sfte.htlibrary.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This class defines a set of operation of book. Such as new book store, modify
 * book and so on.
 * 
 * @author wenwen
 * 
 */
public class BookOperation {
	// new book operation.
	public static boolean newBook(Book book) {
		Connection con = null;
		try {
			con = HtConnection.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("SELECT * FROM book WHERE isbn = ?");
			pstmt.setString(1, book.getIsbn());
			ResultSet rs = pstmt.executeQuery();
			// The library has this book already.
			if (rs.next()) {
				pstmt = con.prepareStatement("UPDATE book "
						+ "SET total_amount = total_amount + ?, "
						+ "amount = amount + ? " + "WHERE isbn = ?");
				pstmt.setInt(1, book.getTotalAmount());
				pstmt.setInt(2, book.getAmount());
				pstmt.setString(3, book.getIsbn());
				int result = pstmt.executeUpdate();
				if (result == 1)
					return true;
				else
					return false;
			} else // new Books.
			{
				pstmt = con.prepareStatement("INSERT INTO book "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				pstmt.setNull(1, java.sql.Types.NULL);
				pstmt.setString(2, book.getIsbn());
				pstmt.setString(3, book.getTitle());
				pstmt.setString(4, book.getAuthor());
				pstmt.setString(5, book.getPublisher());
				pstmt.setDouble(6, book.getPrice());
				pstmt.setInt(7, book.getTotalAmount());
				pstmt.setInt(8, book.getAmount());
				pstmt.setDate(9, new java.sql.Date(book.getAddDate().getTime()));
				pstmt.setInt(10, book.getBorrowedTimes());
				int result = pstmt.executeUpdate();
				if (result == 1)
					return true;
				else
					return false;
			}
		} catch (SQLException e) {
			System.out.println("插入新书时出错!");
			return false;
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
				System.out.println("关闭数据库连接出错!");
			}
		}
	}

	// modify basic information of book.
	public static boolean modifyBook(int id, String isbn, String title,
			String author, String publisher, double price) {
		Connection con = null;
		try {
			con = HtConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement("UPDATE book "
					+ "SET isbn = ?, title = ?, author = ?, "
					+ "publisher = ?, price = ? " + "WHERE book_id = ?");
			pstmt.setString(1, isbn);
			pstmt.setString(2, title);
			pstmt.setString(3, author);
			pstmt.setString(4, publisher);
			pstmt.setDouble(5, price);
			pstmt.setInt(6, id);
			int result = pstmt.executeUpdate();
			if (result == 1)
				return true;
			else
				return false;
		} catch (SQLException e) {
			System.err.println("修改图书出错");
			return false;
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean borrowBook(int readerId, int bookId) {
		Connection con = null;
		try {
			con = HtConnection.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("INSERT INTO borrowbook "
							+ "VALUES(?, ?, CURDATE(), ?)");
			pstmt.setInt(1, readerId);
			pstmt.setInt(2, bookId);
			int borrowMonth = 2;
			pstmt.setDate(3, new java.sql.Date(
					getExpireDate(borrowMonth).getTime()));
			int result = pstmt.executeUpdate();
			if (result == 1) {
				pstmt = con
						.prepareStatement("UPDATE book SET amount = amount - 1, "
								+ "borrowed_times = borrowed_times + 1 "
								+ "WHERE book_id = ?");
				pstmt.setInt(1, bookId);
				pstmt.executeUpdate();
				pstmt = con.prepareStatement("UPDATE reader "
						+ "SET book_amount = book_amount + 1 "
						+ "WHERE reader_id = ?");
				pstmt.setInt(1, readerId);
				pstmt.executeUpdate();
				return true;
			} else
				return false;
		} catch (SQLException e) {
//			e.printStackTrace();
			return false;
		}
	}

	public static int getOverdueDays(int readerId, int bookId) {
		Connection con = null;
		try {
			con = HtConnection.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("SELECT DATEDIFF('day', revertible_date, CURDATE()) "
							+ "FROM borrowbook "
							+ "WHERE reader_id = ? AND book_id = ?");
			pstmt.setInt(1, readerId);
			pstmt.setInt(2, bookId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(1);
			return 0;
		} catch (SQLException e) {
//			e.printStackTrace();
			return 0;
		}
	}

	public static boolean giveBackBook(int readerId, int bookId) {
		Connection con = null;
		try {
			con = HtConnection.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("DELETE FROM borrowbook "
							+ "WHERE reader_id = ? AND book_id = ?");
			pstmt.setInt(1, readerId);
			pstmt.setInt(2, bookId);
			int result = pstmt.executeUpdate();
			if (result != 1)
				return false;
			pstmt = con.prepareStatement("UPDATE book "
					+ "SET amount = amount + 1 " + "WHERE book_id = ?");
			pstmt.setInt(1, bookId);
			result = pstmt.executeUpdate();
			if (result != 1)
				return false;
			return true;
		} catch (SQLException e) {
//			e.printStackTrace();
			return false;
		}
	}

	// loss book operation.
	public static boolean lossBook(int readerId, int bookId) {
		Connection con = null;
		try {
			con = HtConnection.getConnection();
			// insert a record to lossbook Table.
			PreparedStatement pstmt = con
					.prepareStatement("INSERT INTO lossbook "
							+ "VALUES(?, ?, CURDATE())");
			pstmt.setInt(1, bookId);
			pstmt.setInt(2, readerId);
			int result = pstmt.executeUpdate();
			if (result != 1)
				return false;

			// delete a record from borrowbook table.
			pstmt = con.prepareStatement("DELETE FROM borrowbook "
					+ "WHERE reader_id = ? AND book_id = ?");
			pstmt.setInt(1, readerId);
			pstmt.setInt(2, bookId);
			result = pstmt.executeUpdate();
			if (result != 1)
				return false;
			// set the total amount of this book, total_amoutn - 1
			pstmt = con.prepareStatement("UPDATE book "
					+ "SET total_amount = total_amount - 1 "
					+ "WHERE book_id = ?");
			pstmt.setInt(1, bookId);
			result = pstmt.executeUpdate();
			if (result != 1)
				return false;
			// all operation succeed.
			return true;
		} catch (SQLException e) {
//			e.printStackTrace();
			return false;
		}
	}

	// renew book.
	public static boolean renewBook(int readerId, int bookId) {
		Connection con = null;
		try {
			con = HtConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement("UPDATE borrowbook "
					+ "SET revertible_date = ? "
					+ "WHERE reader_id = ? AND book_id = ?");
			int renewMonth = 1;
			pstmt.setDate(1, new java.sql.Date(
					getExpireDate(renewMonth).getTime()));
			pstmt.setInt(2, readerId);
			pstmt.setInt(3, bookId);
			int result = pstmt.executeUpdate();
			if (result == 1)
				return true;
			return false;
		} catch (SQLException e) {
//			e.printStackTrace();
			return false;
		}
	}
	
	// help method. get the Date from now add some month.
	private static Date getExpireDate(int numOfMonth) {
		Calendar today = new GregorianCalendar();
		today.setTimeInMillis(System.currentTimeMillis());
		today.add(Calendar.MONTH, numOfMonth);
		
		return today.getTime();
	}
}