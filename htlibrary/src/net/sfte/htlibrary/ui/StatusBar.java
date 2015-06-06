package net.sfte.htlibrary.ui;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.sfte.htlibrary.util.HtLibraryAuthorInfo;

/**
 * This class defines the status bar of htlibrary. The status bar displays some
 * useful information of the author.
 * 
 * @author wenwen
 */
public class StatusBar extends JPanel {
	private static final long serialVersionUID = 1L;

	public StatusBar() {
		setLayout(new GridLayout(1, 0));
		libraryLabel = new JLabel(HtLibraryAuthorInfo.getLibraryFullName()
				+ HtLibraryAuthorInfo.getVersion(), JLabel.CENTER);
		authorLabel = new JLabel("����: " + HtLibraryAuthorInfo.getAuthorName(),
				JLabel.CENTER);
		adressLabel = new JLabel("��ַ: "
				+ HtLibraryAuthorInfo.getAuthorAddress(), JLabel.CENTER);
		phoneLabel = new JLabel(
				"��ϵ�绰: " + HtLibraryAuthorInfo.getAuthorPhone(), JLabel.CENTER);
		emailLabel = new JLabel("����: " + HtLibraryAuthorInfo.getAuthorEmail(),
				JLabel.CENTER);
		addLabel(libraryLabel);
		addLabel(authorLabel);
		addLabel(adressLabel);
		addLabel(phoneLabel);
		addLabel(emailLabel);
	}
	
	public void reloadStatusBar() {
		libraryLabel.setText(HtLibraryAuthorInfo.getLibraryFullName()
				+ HtLibraryAuthorInfo.getVersion());
		authorLabel.setText("����: " + HtLibraryAuthorInfo.getAuthorName());
		adressLabel.setText("��ַ: " + HtLibraryAuthorInfo.getAuthorAddress());
		phoneLabel.setText("��ϵ�绰: " + HtLibraryAuthorInfo.getAuthorPhone());
		emailLabel.setText("����: " + HtLibraryAuthorInfo.getAuthorEmail());
	}

	private void addLabel(JLabel label) {
		label.setBorder(BorderFactory.createEtchedBorder());
		this.add(label);
	}

	private JLabel libraryLabel;

	private JLabel authorLabel;

	private JLabel adressLabel;

	private JLabel phoneLabel;

	private JLabel emailLabel;
}