package net.sfte.htlibrary.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import net.sfte.htlibrary.util.HtLibraryAuthorInfo;

public class StatusBarInfoDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	public StatusBarInfoDialog(Frame parent, final StatusBar statusBar) {
		super(parent, "״̬����Ϣ�޸�", true);
		setSize(400, 270);
		setResizable(false);

		nameField = new JTextField();
		versionField = new JTextField();
		authorField = new JTextField();
		phoneField = new JTextField();
		addressField = new JTextField();
		emailField = new JTextField();
		statusLabel = new JLabel(loadedInfo);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.add(new JLabel("�������޸�״̬����ʾ����Ϣ, �޸ĺ���������:"), new GBC(0, 0, 4,
				1).setAnchor(GBC.WEST).setInsets(5));

		mainPanel.add(new JLabel("����: "), new GBC(0, 1).setAnchor(GBC.EAST)
				.setInsets(5));
		mainPanel.add(nameField, new GBC(1, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 50));

		mainPanel.add(new JLabel("�汾: "), new GBC(2, 1).setAnchor(GBC.EAST)
				.setInsets(5));
		mainPanel.add(versionField, new GBC(3, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 50));

		mainPanel.add(new JLabel("����: "), new GBC(0, 2).setAnchor(GBC.EAST)
				.setInsets(5));
		mainPanel.add(authorField, new GBC(1, 2).setFill(GBC.HORIZONTAL)
				.setWeight(100, 50));

		mainPanel.add(new JLabel("�绰: "), new GBC(2, 2).setAnchor(GBC.EAST)
				.setInsets(5));
		mainPanel.add(phoneField, new GBC(3, 2).setFill(GBC.HORIZONTAL)
				.setWeight(100, 50));

		mainPanel.add(new JLabel("��ַ: "), new GBC(0, 3).setAnchor(GBC.EAST)
				.setInsets(5));
		mainPanel.add(addressField, new GBC(1, 3, 3, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 50));

		mainPanel.add(new JLabel("����: "), new GBC(0, 4).setAnchor(GBC.EAST)
				.setInsets(5));
		mainPanel.add(emailField, new GBC(1, 4, 3, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 50));

		mainPanel.add(new JLabel(""), new GBC(0, 5, 4, 1).setAnchor(GBC.EAST)
				.setInsets(5));

		mainPanel.add(statusLabel, new GBC(0, 6, 4, 1).setAnchor(GBC.CENTER)
				.setInsets(5));

		mainPanel.setBorder(BorderFactory.createEtchedBorder());

		this.add(mainPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		saveButton = new JButton("����");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (!checkFields()) {
					statusLabel.setText(errorInfo);
					return ;
				}
				if (saveInfo()) {
					statusLabel.setText(successInfo);
					statusBar.reloadStatusBar();
				} else {
					statusLabel.setText(failInfo);
				}
			}
		});
		refreshButton = new JButton("ˢ��");
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				loadInfo();
				statusLabel.setText(refreshInfo);
			}
		});
		exitButton = new JButton("�˳�");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				StatusBarInfoDialog.this.setVisible(false);
			}
		});
		buttonPanel.add(saveButton);
		buttonPanel.add(refreshButton);
		buttonPanel.add(exitButton);
		this.add(buttonPanel, BorderLayout.SOUTH);

		this.getRootPane().setDefaultButton(saveButton);
	}

	public void loadInfo() {
		nameField.setText(HtLibraryAuthorInfo.getLibraryName());
		versionField.setText(HtLibraryAuthorInfo.getVersion());
		authorField.setText(HtLibraryAuthorInfo.getAuthorName());
		phoneField.setText(HtLibraryAuthorInfo.getAuthorPhone());
		addressField.setText(HtLibraryAuthorInfo.getAuthorAddress());
		emailField.setText(HtLibraryAuthorInfo.getAuthorEmail());

		statusLabel.setText(loadedInfo);
	}

	private boolean saveInfo() {
		HtLibraryAuthorInfo.setLibraryName(nameField.getText());
		HtLibraryAuthorInfo.setVersion(versionField.getText());
		HtLibraryAuthorInfo.setAuthorName(authorField.getText());
		HtLibraryAuthorInfo.setAuthorPhone(phoneField.getText());
		HtLibraryAuthorInfo.setAuthorAddress(addressField.getText());
		HtLibraryAuthorInfo.setAuthorEmail(emailField.getText());

		return HtLibraryAuthorInfo.storeInfo();
	}

	private boolean checkFields() {
		if (nameField.getText().length() == 0)
			return false;
		if (versionField.getText().length() == 0)
			return false;
		if (authorField.getText().length() == 0)
			return false;
		if (phoneField.getText().length() == 0)
			return false;
		if (addressField.getText().length() == 0)
			return false;
		if (emailField.getText().length() == 0)
			return false;
		return true;
	}

	private JButton saveButton;

	private JButton refreshButton;

	private JButton exitButton;

	private JTextField nameField;

	private JTextField versionField;

	private JTextField authorField;

	private JTextField phoneField;

	private JTextField addressField;

	private JTextField emailField;

	private JLabel statusLabel;
	
	private String loadedInfo = "<html><font color=\"blue\">��Ϣ�ѵ���</font></html>";

	private String successInfo = "<html><font color=\"fuchsia\">��Ϣ�ѳɹ�����, ϵͳ�´�����ʱ��Ч!</font></html>";

	private String failInfo = "<html><font color=\"red\">����ʧ��, ������!</font></html>";

	private String refreshInfo = "<html><font color=\"olive\">ˢ�³ɹ�</font></html>";

	private String errorInfo = "<html><font color=\"red\">����дȫ��������Ϣ!</font></html>";
}
