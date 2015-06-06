package net.sfte.htlibrary.database;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.sfte.htlibrary.ui.ResultSetTableModel;
import net.sfte.htlibrary.SetupDB;

/**
 * This class defines some operation of htlibrary database.
 * 
 * @author wenwen
 */
public class HtConnection {
	public static Connection getConnection() {
		Connection con = null;
		try {
			Properties props = new Properties();
			FileInputStream fis = new FileInputStream("database.properties");
			props.load(fis);
			fis.close();

			String drivers = props.getProperty("jdbc.drivers");
			Class.forName(drivers);
			String url = props.getProperty("jdbc.url");
			String username = props.getProperty("jdbc.username");
			String password = props.getProperty("jdbc.password");
			con = DriverManager.getConnection(url, username, password);
			return con;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog((JFrame) null,
					"系统与数据库连接失败! 请重启系统或查看 readme 文件的配置说明!", "系统错误",
					JOptionPane.ERROR_MESSAGE);
			SetupDB.createDBConfigFile();
			SetupDB.setupDB();
			System.exit(1);
			return null;
		} catch (Exception e) {
			System.out.println("数据库配置文件出错!");
			SetupDB.createDBConfigFile();
			System.out.println("重建数据库配置文件, 请重新启动本系统!");
			System.exit(1);
			return null;
		}
	}

	public static ResultSetTableModel getTableModel(String query,
			String[] columnNames) {
		Connection con = null;
		ResultSet rs = null;
		ResultSetTableModel model = null;
		try {
			con = getConnection();
			Statement stmt = con
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(query);
			model = new ResultSetTableModel(rs, columnNames);
			return model;
		} catch (SQLException e) {
			e.printStackTrace();
			return model;
		}
	}
}