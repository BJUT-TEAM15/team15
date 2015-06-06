package net.sfte.htlibrary.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class defines a set of operation of reader. Such as new reader register,
 * modify reader information, reader query, delete reader accounts and so on.
 * 
 * @author wenwen
 */
public class ReaderOperation {
	// new reader register.
	public static boolean registerReader(Reader reader) {
		Connection con = null;
		try {
			con = HtConnection.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("INSERT INTO reader "
							+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setNull(1, java.sql.Types.NULL);
			pstmt.setString(2, reader.getStudentId());
			pstmt.setString(3, reader.getName());
			pstmt.setInt(4, reader.getAge());
			pstmt.setString(5, reader.getSex());
			pstmt.setString(6, reader.getAcademy());
			pstmt.setString(7, reader.getDepartment());
			pstmt.setDate(8, new java.sql.Date(reader.getRegisterDate().getTime()));
			pstmt.setInt(9, reader.getBookAmount());
			pstmt.setString(10, reader.getSummary());
			int result = pstmt.executeUpdate();
			if (result == 1)
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("插入新读者信息失败");
			return false;
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("关闭数据库连接出错!");
			}
		}
	}

	// modify reader's basic information
	public static boolean modifyReader(Reader reader) {
		Connection con = null;
		try {
			con = HtConnection.getConnection();
			PreparedStatement pstmt = con.prepareStatement("UPDATE reader "
					+ "SET student_id = ?, reader_name = ?, age = ?, "
					+ "sex = ?, academy = ?, department = ?, summary = ? "
					+ "WHERE reader_id = ?");
			pstmt.setString(1, reader.getStudentId());
			pstmt.setString(2, reader.getName());
			pstmt.setInt(3, reader.getAge());
			pstmt.setString(4, reader.getSex());
			pstmt.setString(5, reader.getAcademy());
			pstmt.setString(6, reader.getDepartment());
			pstmt.setString(7, reader.getSummary());
			pstmt.setInt(8, reader.getReaderId());
			int result = pstmt.executeUpdate();
			if (result == 1)
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("修改读者信息失败");
			return false;
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("关闭数据库连接出错!");
			}
		}
	}

	/**
	 * Check the specified reader has borrowed books or not.
	 * 
	 * @param readerId
	 * @return true(borrowed books) or false(no books)
	 */
	public static boolean hasBorrowedBook(int readerId) {
		Connection con = null;
		try {
			con = HtConnection.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("SELECT * FROM borrowbook WHERE reader_id = ?");
			pstmt.setInt(1, readerId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				return true;
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}
	}

	/**
	 * This method delete the specified reader accounts from database.
	 * 
	 * @param readerId
	 * @return succeed or not
	 */
	public static boolean deleteReader(int readerId) {
		Connection con = null;
		try {
			con = HtConnection.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("DELETE FROM reader WHERE reader_id = ?");
			pstmt.setInt(1, readerId);
			int result = pstmt.executeUpdate();
			if (result == 1)
				return true;
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}