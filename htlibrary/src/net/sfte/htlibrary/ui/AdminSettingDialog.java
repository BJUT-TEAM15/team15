package net.sfte.htlibrary.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import net.sfte.htlibrary.database.Admin;
import net.sfte.htlibrary.database.AdminOperation;
import net.sfte.htlibrary.database.HtConnection;

/**
 * This class defines a dialog used to add, delete or modity adminstrator's
 * accounts.
 * 
 * @author wenwen
 */
public class AdminSettingDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AdminSettingDialog() {
		setLayout(new BorderLayout());

		// prepare database connection.
		prepareDatabase();
		final AdminOperation adminOperation = new AdminOperation(con);
		addButton = new JButton("添  加");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addButton.setEnabled(false);
				modifyButton.setEnabled(false);
				deleteButton.setEnabled(false);
				saveButton.setEnabled(true);
				cancelButton.setEnabled(true);

				adminName.setEditable(true);
				adminName.setText("");
				createdDate.setValue(new java.util.Date());
				phoneField.setEditable(true);
				phoneField.setText("");
				emailField.setEditable(true);
				emailField.setText("");
				oldPassword.setEditable(true);
				oldPassword.setText("");
				newPassword.setEditable(true);
				newPassword.setText("");
				// INSERT a new admin INTO database.
				saveType = 2;
			}
		});
		modifyButton = new JButton("修  改");
		modifyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (adminName.getText().equals("")) {
					JOptionPane.showMessageDialog(dialog, "请先选择要修改的管理员帐号!",
							"非法操作", JOptionPane.ERROR_MESSAGE);
					return;
				}
				addButton.setEnabled(false);
				modifyButton.setEnabled(false);
				deleteButton.setEnabled(false);
				saveButton.setEnabled(true);
				cancelButton.setEnabled(true);

				phoneField.setEditable(true);
				emailField.setEditable(true);
				// UPDATE the selected admin's information
				saveType = 1;
			}
		});
		deleteButton = new JButton("删  除");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (adminName.getText().equals("")) {
					JOptionPane.showMessageDialog(dialog, "请先选择要删除的管理员帐号!",
							"非法操作", JOptionPane.ERROR_MESSAGE);
					return;
				}
				// confirm to delete admin accounts.
				int result = JOptionPane.showConfirmDialog(dialog,
						"确实要删除该管理员帐号及其全部资料吗?", "确认删除管理员",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (result != JOptionPane.OK_OPTION)
					return;
				// only one admin left, unallowed to delete.
				if (infoTable.getRowCount() == 1) {
					JOptionPane.showMessageDialog(dialog,
							"数据库中只剩下一个管理员帐号, 无法进行删除操作, " + "否则无法登录系统!", "非法请求",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				Admin admin = new Admin(adminName.getText());
				boolean isSucceed = adminOperation.deleteAdmin(admin);
				if (isSucceed) {
					JOptionPane.showMessageDialog(dialog, "成功删除管理员", "操作成功",
							JOptionPane.INFORMATION_MESSAGE);
					infoTable.setModel(getTableModel());
					initButtonAndField();
				} else {
					JOptionPane.showMessageDialog(dialog, "删除管理员失败", "操作失败",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
		saveButton = new JButton("保  存");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// UPDATE the selected admin information.
				if (saveType == 1) {
					Admin admin = new Admin(0, adminName.getText(), oldPassword
							.getPassword(), createdDate.getText(), phoneField
							.getText(), emailField.getText());
					if (adminOperation.updateAdmin(admin)) {
						JOptionPane.showMessageDialog(dialog, "成功修改管理员信息",
								"操作成功", JOptionPane.INFORMATION_MESSAGE);
						infoTable.setModel(getTableModel());
						initButtonAndField();
					} else {
						JOptionPane.showMessageDialog(dialog, "修改管理员信息失败",
								"操作失败", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				// INSERT a new admin INTO database.
				else if (saveType == 2) {
					String oldPass = new String(oldPassword.getPassword());
					String newPass = new String(newPassword.getPassword());
					if (adminName.getText().equals("")) {
						JOptionPane.showMessageDialog(dialog, "必须输入管理员用户名!",
								"非法操作", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if (!oldPass.equals(newPass)) {
						JOptionPane.showMessageDialog(dialog,
								"密码与确认密码不一致, 请重新输入!", "非法输入",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					Admin admin = new Admin(0, adminName.getText(), newPassword
							.getPassword(), createdDate.getText(), phoneField
							.getText(), emailField.getText());
					if (adminOperation.addAdmin(admin)) {
						JOptionPane.showMessageDialog(dialog, "成功添加管理员帐号!",
								"操作成功", JOptionPane.INFORMATION_MESSAGE);
						infoTable.setModel(getTableModel());
						initButtonAndField();
					} else {
						JOptionPane.showMessageDialog(dialog,
								"添加管理员帐号失败, 请检查输入, 管理员不能重名!", "操作失败",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}
		});
		saveButton.setEnabled(false);
		cancelButton = new JButton("取  消");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initButtonAndField();
			}
		});
		cancelButton.setEnabled(false);
		exitButton = new JButton("退  出");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(addButton);
		buttonBox.add(Box.createHorizontalStrut(5));
		buttonBox.add(modifyButton);
		buttonBox.add(Box.createHorizontalStrut(5));
		buttonBox.add(deleteButton);
		buttonBox.add(Box.createHorizontalStrut(35));
		buttonBox.add(saveButton);
		buttonBox.add(Box.createHorizontalStrut(5));
		buttonBox.add(cancelButton);
		buttonBox.add(Box.createHorizontalStrut(35));
		buttonBox.add(exitButton);
		buttonBox.setBorder(BorderFactory.createEtchedBorder());
		add(buttonBox, BorderLayout.NORTH);

		// Center panel with admin base information.
		JPanel adminInfoPanel = new JPanel();
		adminInfoPanel.setLayout(new GridBagLayout());
		adminName = new JTextField(15);
		DateFormat format = DateFormat.getDateInstance();
		format.setLenient(false);
		createdDate = new JFormattedTextField(format);
		createdDate.setColumns(15);
		phoneField = new JTextField(15);
		emailField = new JTextField(15);
		oldPassword = new JPasswordField(15);
		newPassword = new JPasswordField(15);

		adminInfoPanel.add(new JLabel("用  户  名: "), new GBC(0, 0)
				.setAnchor(GBC.EAST));
		adminInfoPanel.add(adminName, new GBC(1, 0).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));
		adminInfoPanel.add(new JLabel("创建时间: "), new GBC(2, 0)
				.setAnchor(GBC.EAST));
		adminInfoPanel.add(createdDate, new GBC(3, 0).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));
		adminInfoPanel.add(new JLabel("联系电话: "), new GBC(0, 1)
				.setAnchor(GBC.EAST));
		adminInfoPanel.add(phoneField, new GBC(1, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));
		adminInfoPanel.add(new JLabel("联系邮箱: "), new GBC(2, 1)
				.setAnchor(GBC.EAST));
		adminInfoPanel.add(emailField, new GBC(3, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));
		adminInfoPanel
				.add(new JLabel("<html><p>下面密码框只有在添加管理员时候才允许输入, "
						+ "如果需要修改管理员密码, 请在主界面选择修改密码功能:</p></html>"), new GBC(0,
						2, 4, 1).setFill(GBC.HORIZONTAL).setAnchor(GBC.EAST));
		adminInfoPanel.add(new JLabel("  密      码: "), new GBC(0, 3)
				.setAnchor(GBC.EAST));
		adminInfoPanel.add(oldPassword, new GBC(1, 3).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));
		adminInfoPanel.add(new JLabel("确认密码: "), new GBC(2, 3)
				.setAnchor(GBC.EAST));
		adminInfoPanel.add(newPassword, new GBC(3, 3).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));

		// create the table and add it to the bottom of dialog.
		ResultSet rs = prepareDatabase();
		String[] columnNames = { "编号", "用户名", "创建时间", "联系电话", "联系邮箱" };
		model = new ResultSetTableModel(rs, columnNames);
		infoTable = new JTable(model);
		infoTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		tableListener = new TableMouseListener();
		infoTable.addMouseListener(tableListener);
		adminInfoPanel.add(new JScrollPane(infoTable), new GBC(0, 4, 4, 4)
				.setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));

		add(adminInfoPanel, BorderLayout.CENTER);
	}

	public void showDialog(Component parent) {
		// unallowed to edit some of these fields. users must press
		// the buttons on the top first.
		initButtonAndField();
		// locate the owner Frame.
		Frame owner = null;
		if (parent instanceof Frame)
			owner = (Frame) parent;
		else
			owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class,
					parent);
		if (dialog == null || dialog.getOwner() != owner) {
			dialog = new JDialog(owner, true);
			dialog.add(this, BorderLayout.CENTER);
			dialog.getRootPane().setDefaultButton(saveButton);
			dialog.pack();
		}
		// initialize the dialog. Fill the textFields and table.
		int result = JOptionPane.showConfirmDialog(dialog,
				"只有管理员才允许继续以下操作, 请确认您拥有管理员帐号", "是否继续?",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (result != JOptionPane.OK_OPTION)
			return;
		// verify this men is or not a admin.
		if (verifyDialog == null) {
			verifyDialog = new LoginDialog("<html>现在需要验证您的管理员身份, "
					+ "如果没有正确的帐号和密码,</p> 您将无法继续操作, 请与管理员联系!</html>", 1);
		}
		verifyDialog.showDialog(owner, "身份验证");

		dialog.setTitle("管理员设置");
		dialog.setSize(750, 500);
		// set the Location of this dialog to the center
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension size = kit.getScreenSize();
		int width = size.width;
		int height = size.height;
		dialog.setLocation((width - dialog.getWidth()) / 2, (height - dialog
				.getHeight()) / 2);
		dialog.setVisible(true);
	}

	/**
	 * Prepare database connection. some of the Statement and PreparedStatement
	 * alse created.
	 */
	private ResultSet prepareDatabase() {
		ResultSet rs = null;
		try {
			if (con == null) {
				con = HtConnection.getConnection();
			}
			getAdminStmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = getAdminStmt
					.executeQuery("SELECT admin_id, admin_name, create_date, "
							+ "phone, email FROM admin");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * Mouse Listener of the JTable. When clicked one row, fill the JTextField
	 * with correspond value.
	 * 
	 * @author wenwen
	 */
	private class TableMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			int row = infoTable.getSelectedRow();
			// no row selected.
			if (row == -1) {
				return;
			} else {
				Object obj = null;
				obj = infoTable.getValueAt(row, 1);
				adminName.setText(obj.toString());
				obj = infoTable.getValueAt(row, 2);
				createdDate.setValue(obj);
				obj = infoTable.getValueAt(row, 3);
				if (obj == null)
					phoneField.setText("");
				else
					phoneField.setText(obj.toString());
				obj = infoTable.getValueAt(row, 4);
				if (obj == null)
					emailField.setText("");
				else
					emailField.setText(obj.toString());
			}
		}
	}

	private void initButtonAndField() {
		addButton.setEnabled(true);
		modifyButton.setEnabled(true);
		deleteButton.setEnabled(true);
		saveButton.setEnabled(false);
		cancelButton.setEnabled(false);

		adminName.setText("");
		adminName.setEditable(false);
		createdDate.setText("");
		createdDate.setEditable(false);
		phoneField.setText("");
		phoneField.setEditable(false);
		emailField.setText("");
		emailField.setEditable(false);
		oldPassword.setText("");
		oldPassword.setEditable(false);
		newPassword.setText("");
		newPassword.setEditable(false);
	}

	private ResultSetTableModel getTableModel() {
		ResultSet rs = prepareDatabase();
		String[] columnNames = { "编号", "用户名", "创建时间", "联系电话", "联系邮箱" };
		return new ResultSetTableModel(rs, columnNames);
	}

	private JButton addButton;

	private JButton modifyButton;

	private JButton deleteButton;

	private JButton saveButton;

	private JButton cancelButton;

	private JButton exitButton;

	// saveType for saveButton, 1 --> UPDATE, 2 --> INSERT.
	private int saveType = 1;

	private JTextField adminName;

	private JFormattedTextField createdDate;

	private JTextField phoneField;

	private JTextField emailField;

	private JPasswordField oldPassword;

	private JPasswordField newPassword;

	ResultSetTableModel model;

	private JTable infoTable = null;

	private TableMouseListener tableListener;

	private JDialog dialog = null;

	private LoginDialog verifyDialog = null;

	private Connection con = null;

	private Statement getAdminStmt;
}