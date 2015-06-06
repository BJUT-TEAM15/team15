package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.LoginDialog;
import net.sfte.htlibrary.util.HtLibraryAuthorInfo;

/**
 * This class defines the hang up action in htlibrary.
 * 
 * @author wenwen
 */
public class HangupAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HangupAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "离开挂起");
		putValue(Action.SMALL_ICON, new ImageIcon("images/hangup.png"));
		putValue(Action.SHORT_DESCRIPTION, "暂时离开并锁定图书馆管理系统");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new LoginDialog("<html>" + HtLibraryAuthorInfo.getLibraryFullName()
					+ "正在运行且</p>已被锁定, "
					+ "只能由管理员进行解除</html>", 1); // '1' is HangupDialog
		dialog.showDialog(parent, "系统挂起");
	}

	private Component parent = null;

	private LoginDialog dialog = null;
}