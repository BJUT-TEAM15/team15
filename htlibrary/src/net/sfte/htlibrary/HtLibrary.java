/*
 * htLibrary: 海天图书馆管理系统
 * Copyright (C) 2005 Zhangqiwen
 * 
 * tabSize=8: indentSize=4: noTabs=false
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

/**
 * This is the main entry point for the htLibrary application.
 * @author wenwen
 */
package net.sfte.htlibrary;

import java.awt.Frame;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import net.sfte.htlibrary.ui.FlashWindow;
import net.sfte.htlibrary.ui.LoginDialog;
import net.sfte.htlibrary.ui.MainFrame;
import net.sfte.htlibrary.util.HtLibraryAuthorInfo;
import net.sfte.htlibrary.util.SingleApplicationLock;

/**
 * The main class of the htLibrary management system.
 * 
 * @author wenwen
 */
public class HtLibrary {
	public static void main(String[] args) {
		// check HtLibrary MIS is active.
		String lockFile = ".lck";
		SingleApplicationLock appLock = new SingleApplicationLock(lockFile);
		if (appLock.isAppActive()) {
			// HtLibrary system is active
			JOptionPane.showMessageDialog((Frame) null, 
					HtLibraryAuthorInfo.getLibraryFullName() 
					+ "已经运行, 同时只允许启动一个!", "系统错误", 
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		// check for java 1.5 or later.
		String javaVersion = System.getProperty("java.version");
		if (javaVersion.compareTo("1.5") < 0) {
			System.err.println("You are running java Version: " + javaVersion
					+ ".");
			System.err.println("htLibrary requires java 1.5 or laters.");
			System.err.println("You need download a new one from "
					+ "[java.sun.com]");
			System.exit(1);
		}

		// Test database connection.
		Properties prop = new Properties();
		try {
			FileInputStream in = new FileInputStream("database.properties");
			prop.load(in);
			in.close();
		} catch (IOException e) {
			System.out.println("数据库配置文件丢失!");
			System.out.println("重建配置文件...");
			SetupDB.createDBConfigFile();
			System.out.println("请重新启动本管理系统!");
			System.exit(1);
		}
		String s = prop.getProperty("jdbc.ok");
		if (!s.equals("true")) {
			boolean success = SetupDB.setupDB();
			if (!success) {
				System.out.println("数据库安装失败! 请重新启动本系统!");
				System.out.println("联系zhangqiwen1234@163.com");
				System.exit(1);
			}
		}

		// set the look and feel.
		try {
			UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
		} catch (Exception ex) {
			// nothing need to do
		}

		// main frame startup.
		MainFrame frame = new MainFrame();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// show the Login dialog, '0' denote this is a LoginDialog.
		LoginDialog dialog = new LoginDialog("登录" + HtLibraryAuthorInfo.getLibraryFullName(), 0);

		// flash window
		FlashWindow flashWindow = new FlashWindow();
		flashWindow.showWindow(frame, dialog);
	}
}