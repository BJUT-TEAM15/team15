package net.sfte.htlibrary.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.sfte.htlibrary.util.HtLibraryAuthorInfo;

/**
 * This class defines the exit dialog of htlibrary. User can choose exit, switch
 * user, cancel or hang up.
 * 
 * @author wenwen
 */
public class ExitDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExitDialog() {
		exitButton = new JButton("直接退出", new ImageIcon("images/exit.png"));
		cancelButton = new JButton("取消返回", new ImageIcon("images/cancel.png"));
		switchButton = new JButton("切换用户", new ImageIcon("images/switch.png"));
		hangupButton = new JButton("离开挂起", new ImageIcon("images/hangup.png"));

		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		switchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				if (switchDialog == null)
					switchDialog = new LoginDialog("请输入另一个管理员帐号", 2);
				switchDialog.showDialog(ExitDialog.this, "切换用户");
			}
		});
		hangupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				if (hangupDialog == null)
					hangupDialog = new LoginDialog(
							"<html>" + HtLibraryAuthorInfo.getLibraryFullName() 
									+ "正在运行且</p>已被锁定, "
									+ "只能由管理员进行解除</html>", 1);
				hangupDialog.showDialog(ExitDialog.this, "离开挂起");
			}
		});
		add(exitButton);
		add(cancelButton);
		add(switchButton);
		add(hangupButton);
		setBorder(BorderFactory.createEtchedBorder());
	}

	private JButton exitButton;

	private JButton switchButton;

	private JButton cancelButton;

	private JButton hangupButton;

	private JDialog dialog = null;

	private LoginDialog switchDialog = null;

	private LoginDialog hangupDialog = null;

	public void showDialog(Component parent) {
		Frame owner = null;
		if (parent instanceof Frame)
			owner = (Frame) parent;
		else
			owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class,
					parent);

		if (dialog == null || dialog.getOwner() != owner) {
			dialog = new JDialog(owner, true);
			dialog.add(this);
			dialog.getRootPane().setDefaultButton(cancelButton);
			dialog.pack();
		}

		dialog.setTitle("退出提示");
		dialog.setSize(300, 130);
		dialog.setResizable(false);
		// set the Location of this dialog to the center
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension size = kit.getScreenSize();
		int width = size.width;
		int height = size.height;
		dialog.setLocation((width - dialog.getWidth()) / 2, (height - dialog
				.getHeight()) / 2);
		dialog.setVisible(true);
	}
}