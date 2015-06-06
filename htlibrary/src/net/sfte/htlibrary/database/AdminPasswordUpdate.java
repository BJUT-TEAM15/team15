package net.sfte.htlibrary.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class defines a static method to modify the amdin's password.
 * 
 * @author wenwen
 */
public class AdminPasswordUpdate {
	public static boolean updatePassword(Admin admin) {
		Connection con = HtConnection.getConnection();
		String name = admin.getName();
		String password = new String(admin.getPassword());
		try {
			PreparedStatement pstmt = con
					.prepareStatement("UPDATE admin SET password = ? "
							+ "WHERE admin_name = ?");
			pstmt.setString(1, password);
			pstmt.setString(2, name);
			int amounts = pstmt.executeUpdate();
			// password update succeed
			if (amounts == 1)
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}