package net.sfte.htlibrary.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class check the admin and password, only if the username and password
 * are correct can login.
 * 
 * @author wenwen
 */
public class Login {
	public static boolean checkLogin(Admin admin) {
		// get database connection
		Connection con = null;
		String name = admin.getName();
		if (name == null)
			name = "";
		String password = new String(admin.getPassword());
		if (password == null)
			password = "";
		try {
			con = HtConnection.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("SELECT admin_id, admin_name, password, "
							+ "create_date, phone, email " + "FROM admin "
							+ "WHERE admin_name = ? AND password = ?");
			pstmt.setString(1, name);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			// accounts passed verify
			if (rs.next())
				return true;
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}