package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.ReaderRegisterDialog;

public class ReaderRegisterAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReaderRegisterAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "读者登记");
		putValue(Action.SMALL_ICON, new ImageIcon("images/register.png"));
		putValue(Action.SHORT_DESCRIPTION, "新读者注册管理窗口");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new ReaderRegisterDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private ReaderRegisterDialog dialog = null;
}