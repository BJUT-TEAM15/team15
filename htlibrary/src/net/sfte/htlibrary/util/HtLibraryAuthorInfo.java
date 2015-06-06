package net.sfte.htlibrary.util;

import java.io.*;
import java.util.*;

/**
 * This class contians some information of the htLibrary system.
 */
public class HtLibraryAuthorInfo {
	// common infomation.
	private static String version;

	private static String libraryName;

	private static String aboutDialogImage;

	private static String mainPanelImage;

	// author information.
	private static String authorName;

	private static String authorAddress;

	private static String authorPhone;

	private static String authorEmail;

	private static Properties prop = null;

	// load the htlibrary and author information.
	static {
		prop = new Properties();
		try {
			prop.load(new FileInputStream("HtLibraryAuthorInfo.properties"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		version = prop.getProperty("version", "V1.1");
		libraryName = prop.getProperty("library.name", "海天");
		aboutDialogImage = prop.getProperty("bgImage.aboutDialog",
				"images/htMM.png");
		mainPanelImage = prop.getProperty("bgImage.mainPanel",
				"images/library.jpg");

		authorName = prop.getProperty("author.name", "张岐文");
		authorAddress = prop.getProperty("author.address", "南昌大学191108");
		authorPhone = prop.getProperty("author.phone", "0791-8888888");
		authorEmail = prop
				.getProperty("author.email", "zhangqiwen1234@163.com");
	}

	public static boolean storeInfo() {
		StringN2A n2a = new StringN2A();
		boolean success = true;
		try {
			PrintWriter writer = new PrintWriter(
				new FileWriter("HtLibraryAuthorInfo.properties"));
			writer.println("#Please do NOT change this file.");
			writer.println("#You can do this change in the htlibrary system.");
			
			writer.println("version=" + n2a.toAscii(version));
			writer.println("library.name=" + n2a.toAscii(libraryName));
			writer.println("bgImage.aboutDialog=" + n2a.toAscii(aboutDialogImage));
			writer.println("bgImage.mainPanel=" + n2a.toAscii(mainPanelImage));

			writer.println("author.name=" + n2a.toAscii(authorName));
			writer.println("author.address=" + n2a.toAscii(authorAddress));
			writer.println("author.phone=" + n2a.toAscii(authorPhone));
			writer.println("author.email=" + n2a.toAscii(authorEmail));
			
			writer.close();
		} catch (IOException ex) {
			success = false;
		}
		return success;
	}

	public static String getVersion() {
		return version;
	}

	public static String getLibraryFullName() {
		return libraryName + "图书馆管理系统";
	}

	public static String getLibraryName() {
		return libraryName;
	}

	public static String getAuthorName() {
		return authorName;
	}

	public static String getAuthorAddress() {
		return authorAddress;
	}

	public static String getAuthorPhone() {
		return authorPhone;
	}

	public static String getAuthorEmail() {
		return authorEmail;
	}

	public static String getAboutDialogImage() {
		return aboutDialogImage;
	}
	
	public static String getMainPanelImage() {
		return mainPanelImage;
	}

	public static void setAboutDialogImage(String aboutDialogImage) {
		HtLibraryAuthorInfo.aboutDialogImage = aboutDialogImage;
	}

	public static void setMainPanelImage(String mainPanelImage) {
		HtLibraryAuthorInfo.mainPanelImage = mainPanelImage;
	}

	public static void setAuthorAddress(String authorAddress) {
		HtLibraryAuthorInfo.authorAddress = authorAddress;
	}

	public static void setAuthorEmail(String authorEmail) {
		HtLibraryAuthorInfo.authorEmail = authorEmail;
	}

	public static void setAuthorName(String authorName) {
		HtLibraryAuthorInfo.authorName = authorName;
	}

	public static void setAuthorPhone(String authorPhone) {
		HtLibraryAuthorInfo.authorPhone = authorPhone;
	}

	public static void setLibraryName(String libraryName) {
		HtLibraryAuthorInfo.libraryName = libraryName;
	}

	public static void setVersion(String version) {
		HtLibraryAuthorInfo.version = version;
	}
}