package net.sfte.htlibrary;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import net.sfte.htlibrary.database.HtConnection;
import net.sfte.htlibrary.util.HtLibraryAuthorInfo;

/**
 * This class used to build the htlibrary database and some necessary tables.
 * such as admin, book, borrowbook... You must run this class before htlibrary
 * system.
 * The htlibrary default uses HSQLDB database. This is a 
 * opensource DBMS used abroadly in Java.
 * If you want to use another DBMS such as MySQL, MS SQL server or Oracle.
 * you must manually setup the database. 
 * Please look readme.txt for help.
 * @author wenwen
 */
public class SetupDB {
	public static boolean setupDB() {
		clearDatabase();
		
		System.out.println("��ʼ�������ݿ�...");
		
		// test and get the database connection.
		Connection con = HtConnection.getConnection();

		// create five tables
		System.out.println("��ʼ�������ݱ�...");
		int count = 0;
		try {
			// create table admin.
			FileInputStream fis = new FileInputStream("create_admin.sql");
			Scanner in = new Scanner(fis);
			StringBuilder createAdmin = new StringBuilder("");
			while (in.hasNextLine()) {
				createAdmin.append(in.nextLine());
			}
			Statement stmt = con.createStatement();
			stmt.executeUpdate(createAdmin.toString());
			System.out.println("\n�ɹ�����admin��!");
			count++;

			// insert a record to table admin.
			int result = stmt
					.executeUpdate("INSERT INTO admin VALUES(NULL, 'admin', '', CURDATE(), NULL, NULL)");
			if (result == 1)
				System.out.println("�ɹ�����ϵͳĬ�Ϲ���Աadmin!");
			count++;

			// create table book.
			fis = new FileInputStream("create_book.sql");
			in = new Scanner(fis);
			StringBuilder createBook = new StringBuilder("");
			while (in.hasNextLine()) {
				createBook.append(in.nextLine());
			}
			stmt.executeUpdate(createBook.toString());
			System.out.println("�ɹ�����book��!");
			count++;

			// create table borrowbook.
			fis = new FileInputStream("create_borrowbook.sql");
			in = new Scanner(fis);
			StringBuilder createBorrowBook = new StringBuilder("");
			while (in.hasNextLine()) {
				createBorrowBook.append(in.nextLine());
			}
			stmt.executeUpdate(createBorrowBook.toString());
			System.out.println("�ɹ�����borrowbook��!");
			count++;

			// create table reader.
			fis = new FileInputStream("create_reader.sql");
			in = new Scanner(fis);
			StringBuilder createReader = new StringBuilder("");
			while (in.hasNextLine()) {
				createReader.append(in.nextLine());
			}
			stmt.executeUpdate(createReader.toString());
			System.out.println("�ɹ�����reader��!");
			count++;

			// create table lossbook.
			fis = new FileInputStream("create_lossbook.sql");
			in = new Scanner(fis);
			StringBuilder createLossBook = new StringBuilder("");
			while (in.hasNextLine()) {
				createLossBook.append(in.nextLine());
			}
			stmt.executeUpdate(createLossBook.toString());
			System.out.println("�ɹ�����lossbook��!");
			count++;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("����SQL�ļ�ȱʧ��ٻ�!");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (count == 6) {
				System.out.println("\n�������ݿ⽨��������ѳɹ����!");
				System.out.println("���ڿ�������ʹ��" + HtLibraryAuthorInfo.getLibraryFullName());
				System.out.println("лл����ʹ��!");
				FileOutputStream out = null;
				try {
					Properties prop = new Properties();
					prop.load(new FileInputStream("database.properties"));
					prop.setProperty("jdbc.ok", "true");
					out = new FileOutputStream(
							"database.properties");
					prop.store(out, "htlibrary database connection");
				} catch (IOException e) {
					// do nothing.
				} finally {
					try {
						out.close();
					} catch (Exception e) {
						// do nothing.
					}
				}
			} else {
				System.out.println("\n���ݿ⽨�����û��ȫ�����!");
				System.out.println("���������б���װ����!");
			}
			try {
				con.close();
			} catch (SQLException e) {
				// do nothing.
			}
		}
		return count == 6 ? true : false;
	}

	public static void createDBConfigFile() {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter("database.properties"));
			
			out.println("#database connection config");
			out.println("#use the hsqldb in default");
			out.println("jdbc.url=jdbc:hsqldb:file:database/htlibrary");
			out.println("jdbc.username=sa");
			out.println("jdbc.ok=true");
			out.println("jdbc.drivers=org.hsqldb.jdbcDriver");
			out.println("jdbc.password=");
		} catch (IOException ex) {
			// do nothing.
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	public static void clearDatabase() {
		Connection con = HtConnection.getConnection();
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DROP TABLE admin IF EXISTS");
			stmt.executeUpdate("DROP TABLE book IF EXISTS");
			stmt.executeUpdate("DROP TABLE borrowbook IF EXISTS");
			stmt.executeUpdate("DROP TABLE lossbook IF EXISTS");
			stmt.executeUpdate("DROP TABLE reader IF EXISTS");
		} catch (Exception ex) {
//			ex.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				
			}
		}
	}
}