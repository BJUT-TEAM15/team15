package net.sfte.htlibrary.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import net.sfte.htlibrary.util.HtLibraryAuthorInfo;

public class FlashWindow extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Image img = null;

	private JFrame mainWindow = null;

	private LoginDialog loginDialog = null;

	private JDialog dialog = null;

	private JProgressBar progressBar = null;

	private static final int MAXIMUM = 1000;

	public static final int DEFAULT_WIDTH = 248;

	public static final int DEFAULT_HEIGHT = 365;

	public FlashWindow() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		img = kit.getImage("images/start.png");
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		if (img != null) {
			g2.drawImage(img, 0, 0, getWidth(), getHeight(), 0, 0, img
					.getWidth(null), img.getHeight(null), null);
		} else {
			g2.drawString(HtLibraryAuthorInfo.getLibraryFullName() + "正在启动", 10, 100);
		}
	}

	public void showWindow(JFrame mainWindow, LoginDialog loginDialog) {
		this.mainWindow = mainWindow;
		this.loginDialog = loginDialog;
		if (dialog == null) {
			dialog = new JDialog(mainWindow, "启动窗口", false);
			dialog.setUndecorated(true);
			dialog.add(this, BorderLayout.CENTER);

			progressBar = new JProgressBar(0, MAXIMUM);
			progressBar.setStringPainted(false);
			dialog.add(progressBar, BorderLayout.SOUTH);

			dialog.pack();
		}
		dialog.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		dialog.setLocation((screenSize.width - DEFAULT_WIDTH) / 2,
				(screenSize.height - DEFAULT_HEIGHT) / 2);
		dialog.setVisible(true);

		Thread t = new Thread(new MyMonitor());
		t.start();
	}

	private class MyMonitor implements Runnable {
		private volatile int current = 0;

		public void run() {
			while (current <= MAXIMUM) {
				FlashWindow.this.repaint();
				try {
					Thread.sleep(100);
					current += 30;
					progressBar.setValue(current);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			dialog.setVisible(false);
			loginDialog.showDialog(mainWindow, "登录窗口");
		}
	}
}
