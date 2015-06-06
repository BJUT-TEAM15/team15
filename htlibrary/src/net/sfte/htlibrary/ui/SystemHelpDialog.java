package net.sfte.htlibrary.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class SystemHelpDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SystemHelpDialog() {
		setLayout(new BorderLayout());

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		add(new JScrollPane(textArea), BorderLayout.CENTER);
		try {
			File f = new File("Help");
			Scanner in = new Scanner(f);
			while (in.hasNext()) {
				textArea.append(in.nextLine() + "\n");
			}
		} catch (IOException e) {
			textArea.append("请联系: zhangqiwen1234@163.com");
		}
	}

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
			dialog.pack();
		}
		dialog.setTitle("系统帮助");
		dialog.setSize(600, 650);
		// set the Location of this dialog to the center
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension size = kit.getScreenSize();
		int width = size.width;
		int height = size.height;
		dialog.setLocation((width - dialog.getWidth()) / 2, (height - dialog
				.getHeight()) / 2);
		dialog.setVisible(true);
	}

	private JTextArea textArea;

	private JDialog dialog = null;
}