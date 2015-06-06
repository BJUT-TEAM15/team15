package net.sfte.htlibrary.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class defines a set of database operation of admin. Such as delete
 * admin, add admin and modify.
 * 
 * @author wenwen
 */
public class AdminOperation {
	public AdminOperation(Connection aConnection) {
		con = aConnection;
		if (con == null)
			con = HtConnection.getConnection();
	}

	public boolean deleteAdmin(Admin admin) {
		try {
			PreparedStatement pstmt = con.prepareStatement("DELETE FROM admin "
					+ "WHERE admin_name = ?");
			pstmt.setString(1, admin.getName());
			int result = pstmt.executeUpdate();
			if (result == 1)
				return true;
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateAdmin(Admin admin) {
		try {
			PreparedStatement pstmt = con.prepareStatement("UPDATE admin "
					+ "SET phone = ?, email = ? " + "WHERE admin_name = ?");
			pstmt.setString(1, admin.getPhone());
			pstmt.setString(2, admin.getEmail());
			pstmt.setString(3, admin.getName());
			int result = pstmt.executeUpdate();
			if (result == 1)
				return true;
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean addAdmin(Admin admin) {
		try {
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO admin "
					+ "VALUES(?, ?, ?, ?, ?, ?)");
			pstmt.setNull(1, java.sql.Types.NULL);
			pstmt.setString(2, admin.getName());
			pstmt.setString(3, new String(admin.getPassword()));
			pstmt.setDate(4, new java.sql.Date(admin.getCreateDate().getTime()));
			pstmt.setString(5, admin.getPhone());
			pstmt.setString(6, admin.getEmail());
			int result = pstmt.executeUpdate();
			if (result == 1)
				return true;
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private Connection con = null;
}