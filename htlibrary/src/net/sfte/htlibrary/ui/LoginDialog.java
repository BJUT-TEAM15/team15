package net.sfte.htlibrary.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import net.sfte.htlibrary.database.Admin;
import net.sfte.htlibrary.database.Login;

/**
 * The Login window(dialog) of htlibrary system.You can enter only when you have
 * a adminstrator accounts and password This class will show a dialog when the
 * user login into the system, switch user, and hang up the htlibrary system.
 * All these three situation use this same class, so use dialogType to
 * distinguish 0 denotes LoginDialog, 1 denotes HangupDialog and 2 denotes
 * SwitchUserDialog. The difference of these dialogs are the behavior of
 * cancelButton and close window widget.
 * 
 * @author wenwen
 */
public class LoginDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The main panel of this dialog.
	 * 
	 * @param message
	 *            the JLabel string
	 * @param dialogType
	 *            0 is LoginDialog, 1 is HangupDialog, and 2 is
	 *            SwitchUserDialog.
	 */
	public LoginDialog(String message, final int dialogType) {
		this.dialogType = dialogType;
		setLayout(new BorderLayout());

		JLabel messageLabel = new JLabel(message, JLabel.CENTER);
		add(messageLabel, BorderLayout.NORTH);

		// Construct a panel with admin name and password fields
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		JLabel usernameLabel = new JLabel("管理员:  ");
		JLabel passwordLabel = new JLabel("密码:  ");
		username = new JTextField("admin", 7);
		password = new JPasswordField("", 7);
		panel.add(usernameLabel, new GBC(0, 0).setAnchor(GBC.EAST));
		panel.add(username, new GBC(1, 0).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(10));
		panel.add(passwordLabel, new GBC(0, 1).setAnchor(GBC.EAST));
		panel.add(password, new GBC(1, 1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(10));
		add(panel, BorderLayout.CENTER);

		// a panel with two buttons that terminate this dialog
		okButton = new JButton("确定", new ImageIcon("images/login.png"));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = username.getText().trim();
				if (name.equalsIgnoreCase("demo")) {
					JOptionPane.showMessageDialog(dialog,
							"现在进入的是演示版本, 不提供实际数据库功能!", "系统提示",
							JOptionPane.INFORMATION_MESSAGE);
					dialog.setVisible(false);
					mainWindow.setVisible(true);
					return;
				}
				Admin admin = new Admin(name, password.getPassword());
				// this accounts passed verify.
				if (Login.checkLogin(admin)) {
					dialog.setVisible(false);
					mainWindow.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "用户名/"
							+ "密码无效, 请确认您拥有管理员帐号", "登录失败",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		cancelButton = new JButton("取消", new ImageIcon("images/cancel.png"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// this dialog is loginDialog, if press cancel button,
				// htlibrary system will exit.
				if (dialogType == 0)
					System.exit(1);
				// is switchUserDialog, so press cancel button just
				// do nothing but hide this dialog
				else if (dialogType == 2)
					dialog.setVisible(false);
			}
		});
		// dialog type is hangupDialog, you cannot cancel this dialog.
		if (dialogType == 1)
			cancelButton.setEnabled(false);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel, BorderLayout.SOUTH);
		Border etched = BorderFactory.createEtchedBorder();
		messageLabel.setBorder(etched);
		panel.setBorder(etched);
		buttonPanel.setBorder(etched);
	}

	public void showDialog(Component parent, String title) {
		if (parent instanceof Frame)
			mainWindow = (Frame) parent;
		else
			mainWindow = (Frame) SwingUtilities.getAncestorOfClass(Frame.class,
					parent);
		/*
		 * if first time show this dialog, or if owner has changed, construct a
		 * new one.
		 */
		if (dialog == null || dialog.getOwner() != mainWindow) {
			// a model dialog
			dialog = new JDialog(mainWindow, true);
			dialog.add(this);
			/*
			 * Set the default button as okButton, so you can fill the username
			 * and password then simply press Enter key.
			 */
			dialog.getRootPane().setDefaultButton(okButton);
			dialog.pack();
		}
		// clear the password field
		password.setText("");

		dialog.setTitle(title);
		dialog.setSize(280, 220);
		dialog.setResizable(false);
		// set the Location of this dialog to the center
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension size = kit.getScreenSize();
		int width = size.width;
		int height = size.height;
		dialog.setLocation((width - dialog.getWidth()) / 2, (height - dialog
				.getHeight()) / 2);
		// LoginDialog, close dialog also exit the htlibrary.
		if (dialogType == 0) {
			dialog.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
		}
		// HangupDialog, you cannot close this dialog.
		if (dialogType == 1)
			dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		// SwitchUserDialog, close dialog just hide, don't exit the system.
		if (dialogType == 2)
			dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		dialog.setVisible(true);
	}

	private JTextField username;

	private JPasswordField password;

	private JButton okButton;

	private JButton cancelButton;

	private JDialog dialog = null;

	private Frame mainWindow = null;

	/*
	 * The type of this dialog, 0 for LoginDialog, 1 for hangup Dialog, and 2
	 * for switchUserDialog.
	 */
	private int dialogType = 0;
}