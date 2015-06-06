package net.sfte.htlibrary.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.sfte.htlibrary.database.Admin;
import net.sfte.htlibrary.database.AdminPasswordUpdate;
import net.sfte.htlibrary.database.Login;

/**
 * This class make a dialog used to change the password
 * 
 * @author wenwen
 */
public class ChangePasswordDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChangePasswordDialog() {
		setLayout(new BorderLayout());

		// main panel
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		JLabel adminLabel = new JLabel("�� �� Ա: ");
		JLabel oldLabel = new JLabel("ԭ �� ��: ");
		JLabel newLabel = new JLabel("�� �� ��: ");
		JLabel confirmLabel = new JLabel("ȷ������: ");
		adminField = new JTextField("admin", 7);
		oldPassword = new JPasswordField("", 7);
		newPassword = new JPasswordField("", 7);
		confirmPassword = new JPasswordField("", 7);
		panel.add(adminLabel, new GBC(0, 0).setAnchor(GBC.EAST));
		panel.add(adminField, new GBC(1, 0).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(oldLabel, new GBC(0, 1).setAnchor(GBC.EAST));
		panel.add(oldPassword, new GBC(1, 1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(newLabel, new GBC(0, 2).setAnchor(GBC.EAST));
		panel.add(newPassword, new GBC(1, 2).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(confirmLabel, new GBC(0, 3).setAnchor(GBC.EAST));
		panel.add(confirmPassword, new GBC(1, 3).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));

		add(panel, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createEtchedBorder());

		okButton = new JButton("ȷ��");
		// start to update the password
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String adminName = adminField.getText();
				String oldPass = new String(oldPassword.getPassword());
				String newPass = new String(newPassword.getPassword());
				String confirmPass = new String(confirmPassword.getPassword());
				// admin Field is empty.
				if (adminName.equals("")) {
					JOptionPane.showMessageDialog(null, "����Ա����Ϊ��", "����Ա�������",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				// old password not equals with new password.
				if (!newPass.equals(confirmPass)) {
					JOptionPane.showMessageDialog(null, "��������ȷ�����벻һ��", "�������",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				// accounts not passed verify.
				if (!Login.checkLogin(new Admin(adminName, oldPassword
						.getPassword()))) {
					JOptionPane.showMessageDialog(null, "�û���/������Ч, ��ȷ����ӵ��"
							+ "����Ա�ʺ�", "�Ƿ��ʺ�", JOptionPane.ERROR_MESSAGE);
					return;
				}
				// new password is equals to old password, no need to update.
				if (oldPass.equals(newPass)) {
					JOptionPane.showMessageDialog(null, "�¾�������ͬ, ����δ����",
							"����δ�޸�", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				Admin admin = new Admin(adminName, newPassword.getPassword());
				// if passed verify, update the accounts.
				if (AdminPasswordUpdate.updatePassword(admin)) {
					JOptionPane.showMessageDialog(null, "�����޸ĳɹ�, " + "���μ���������",
							"�������޸�", JOptionPane.INFORMATION_MESSAGE);
					dialog.setVisible(false);
				} else {
					JOptionPane.showMessageDialog(null, "�ǳ���Ǹ, �����޸�ʧ��" + "�������"
							+ JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		cancelButton = new JButton("ȡ��");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	public void showDialog(Component parent) {
		// locate the owner frame
		Frame owner = null;
		if (parent instanceof Frame)
			owner = (Frame) parent;
		else
			owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class,
					parent);
		if (dialog == null || dialog.getOwner() != owner) {
			dialog = new JDialog(owner, true);
			dialog.add(this);
			dialog.getRootPane().setDefaultButton(okButton);
			dialog.pack();
		}
		oldPassword.setText("");
		newPassword.setText("");
		confirmPassword.setText("");

		dialog.setTitle("�޸Ĺ���Ա����");
		dialog.setSize(200, 210);
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

	private JTextField adminField;

	private JPasswordField oldPassword;

	private JPasswordField newPassword;

	private JPasswordField confirmPassword;

	private JButton okButton;

	private JButton cancelButton;

	private JDialog dialog = null;
}