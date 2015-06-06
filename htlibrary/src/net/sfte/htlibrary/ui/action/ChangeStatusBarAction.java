package net.sfte.htlibrary.ui.action;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import net.sfte.htlibrary.ui.StatusBar;
import net.sfte.htlibrary.ui.StatusBarInfoDialog;

public class ChangeStatusBarAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private Frame parent = null;
	private StatusBar statusBar = null;
	private StatusBarInfoDialog dialog = null;
	
	public ChangeStatusBarAction(Frame parent, StatusBar statusBar) {
		this.parent  = parent;
		this.statusBar  = statusBar;
		putValue(Action.NAME, "ÐÞ¸Ä×´Ì¬À¸ÐÅÏ¢");
	}

	public void actionPerformed(ActionEvent event) {
		if (dialog == null) {
			dialog = new StatusBarInfoDialog(parent, statusBar);
		}
		dialog.setLocationRelativeTo(parent);
		dialog.loadInfo();
		dialog.setVisible(true);
	}
}
