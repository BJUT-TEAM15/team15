package net.sfte.htlibrary.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import net.sfte.htlibrary.database.HtConnection;
import net.sfte.htlibrary.database.Reader;
import net.sfte.htlibrary.database.ReaderOperation;

/**
 * This class defines a dialog used to modify reader's basic information,
 * reader_id and student_id are not allowed to modify. And also used to delete a
 * reader's accounts.
 * 
 * @author wenwen
 */
public class ModifyDeleteReaderDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ModifyDeleteReaderDialog() {
		setLayout(new BorderLayout());

		idField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		idField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				if (idField.getText().equals(""))
					return;
				Connection con = null;
				try {
					con = HtConnection.getConnection();
					PreparedStatement pstmt = con
							.prepareStatement("SELECT * FROM reader WHERE reader_id = ?");
					pstmt.setInt(1, Integer.parseInt(idField.getText()));
					ResultSet rs = pstmt.executeQuery();
					if (rs.next()) {
						studentIdField.setText(rs.getString(2));
						nameField.setText(rs.getString(3));
						ageField.setValue(rs.getObject(4));
						if (rs.getString(5).startsWith("男"))
							maleButton.setSelected(true);
						else
							femaleButton.setSelected(true);
						academyField.setText(rs.getString(6));
						departmentField.setText(rs.getString(7));
						dateField.setValue(rs.getObject(8));
						summaryArea.setText(rs.getString(10));
					} else {
						initField();
					}
				} catch (SQLException exception) {
					exception.printStackTrace();
				}
			}
		});

		studentIdField = new JTextField("");
		nameField = new JTextField("");
		academyField = new JTextField("");
		departmentField = new JTextField("");
		ageField = new JFormattedTextField(NumberFormat.getIntegerInstance());

		group = new ButtonGroup();
		maleButton = new JRadioButton("男");
		maleButton.setSelected(true);
		femaleButton = new JRadioButton("女");
		group.add(maleButton);
		group.add(femaleButton);
		JPanel sexPanel = new JPanel();
		sexPanel.add(maleButton);
		sexPanel.add(femaleButton);
		sexPanel.setBorder(BorderFactory.createEtchedBorder());

		dateField = new JFormattedTextField(DateFormat.getDateInstance());
		dateField.setEditable(false);

		summaryArea = new JTextArea();
		summaryArea.setLineWrap(true);

		readerQuery = "SELECT reader_id, student_id, reader_name, "
				+ "age, sex, academy, department, register_date, summary "
				+ "FROM reader ORDER BY reader_id";
		columnNames = new String[] { "编号", "学号", "姓名", "年龄", "性别", "学院", "系别",
				"注册日期", "简介" };
		tableModel = HtConnection.getTableModel(readerQuery, columnNames);
		readerTable = new JTable(tableModel);
		readerTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		readerTable.addMouseListener(new TableMouseListener());

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		panel.add(new JLabel("请从下表中选择要删除或修改的读者, " + "或者直接输入正确的读者编号: "),
				new GBC(0, 0, 4, 1).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST)
						.setInsets(5));

		panel.add(new JLabel("编号: "), new GBC(0, 1).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(idField, new GBC(1, 1).setFill(GBC.HORIZONTAL).setWeight(100,
				0).setInsets(5));

		panel.add(new JLabel("日期: "), new GBC(2, 1).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(dateField, new GBC(3, 1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));

		panel.add(new JLabel("学号: "), new GBC(0, 2).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(studentIdField, new GBC(1, 2).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));

		panel.add(new JLabel("姓名: "), new GBC(2, 2).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(nameField, new GBC(3, 2).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));

		panel.add(new JLabel("学院: "), new GBC(0, 3).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(academyField, new GBC(1, 3).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));

		panel.add(new JLabel("系别: "), new GBC(2, 3).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(departmentField, new GBC(3, 3).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));

		panel.add(new JLabel("年龄: "), new GBC(0, 4).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(ageField, new GBC(1, 4).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));

		panel.add(new JLabel("性别: "), new GBC(2, 4).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(sexPanel, new GBC(3, 4).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));

		panel.add(new JLabel("简介: "), new GBC(0, 5).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(new JScrollPane(summaryArea), new GBC(1, 5, 4, 2).setFill(
				GBC.BOTH).setWeight(100, 50).setInsets(5));

		panel.add(new JScrollPane(readerTable), new GBC(0, 9, 4, 4).setFill(
				GBC.BOTH).setWeight(100, 100).setInsets(5));
		panel.setBorder(BorderFactory.createEtchedBorder());
		add(panel, BorderLayout.CENTER);

		modifyButton = new JButton("修 改");
		modifyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!checkInput()) {
					JOptionPane.showMessageDialog(dialog, "请输入完整读者信息, 否则无法修改!",
							"操作失败", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Reader reader = buildReaderFromField();
				if (ReaderOperation.modifyReader(reader)) {
					// modify reader information succeed.
					JOptionPane.showMessageDialog(dialog,
							"成功修改读者信息, 数据已导入数据库!", "操作成功",
							JOptionPane.INFORMATION_MESSAGE);
					readerTable.setModel(HtConnection.getTableModel(
							readerQuery, columnNames));
					initField();
				} else {
					// modify reader information failed.
					JOptionPane.showMessageDialog(dialog,
							"非常抱歉, 修改读者信息失败, 请检查输入!", "操作失败",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		deleteButton = new JButton("删 除");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!checkInput()) {
					JOptionPane.showMessageDialog(dialog,
							"请输入完整读者信息, 否则无法删除读者!", "操作失败",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				int result = JOptionPane.showConfirmDialog(dialog,
						"是否删除该读者帐号及全部信息", "确认删除", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null);
				if (result != JOptionPane.OK_OPTION)
					return;
				int readerId = ((Number) idField.getValue()).intValue();
				// This reader has borrowed some book, so cannot be deleted.
				if (ReaderOperation.hasBorrowedBook(readerId)) {
					JOptionPane.showMessageDialog(dialog,
							"该读者还有图书尚未归还, 所以暂时不能删除!", "操作失败",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (ReaderOperation.deleteReader(readerId)) {
					JOptionPane.showMessageDialog(dialog, "该读者帐号及全部信息已被成功删除!",
							"操作成功", JOptionPane.INFORMATION_MESSAGE);
					readerTable.setModel(HtConnection.getTableModel(
							readerQuery, columnNames));
				} else {
					JOptionPane.showMessageDialog(dialog,
							"非常抱歉, 删除读者操作发生错误, 请管理员检查读者信息表!", "操作失败",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		clearButton = new JButton("清 除");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initField();
			}
		});
		cancelButton = new JButton("退 出");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(modifyButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(clearButton);
		buttonPanel.add(cancelButton);
		buttonPanel.setBorder(BorderFactory.createEtchedBorder());
		add(buttonPanel, BorderLayout.SOUTH);
	}

	public void showDialog(Component parent) {
		initField();
		Frame owner = null;
		if (parent instanceof Frame)
			owner = (Frame) parent;
		else
			owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class,
					parent);
		if (dialog == null || dialog.getOwner() != owner) {
			dialog = new JDialog(owner, true);
			dialog.add(this);
			dialog.getRootPane().setDefaultButton(modifyButton);
			dialog.pack();
		}
		readerTable.setModel(HtConnection.getTableModel(readerQuery,
				columnNames));
		dialog.setTitle("删除或修改读者信息");
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

	private class TableMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			int row = readerTable.getSelectedRow();
			if (row != -1) {
				Object obj = null;
				obj = readerTable.getValueAt(row, 0);
				idField.setValue(obj);
				obj = readerTable.getValueAt(row, 1);
				studentIdField.setText(obj.toString());
				obj = readerTable.getValueAt(row, 2);
				nameField.setText(obj.toString());
				obj = readerTable.getValueAt(row, 3);
				ageField.setValue(obj);
				obj = readerTable.getValueAt(row, 4);
				if (obj.toString().startsWith("男"))
					maleButton.setSelected(true);
				else
					femaleButton.setSelected(true);
				obj = readerTable.getValueAt(row, 5);
				academyField.setText(obj.toString());
				obj = readerTable.getValueAt(row, 6);
				departmentField.setText(obj.toString());
				obj = readerTable.getValueAt(row, 7);
				dateField.setValue(obj);
				obj = readerTable.getValueAt(row, 8);
				summaryArea.setText(obj.toString());
			}
		}
	}

	private boolean checkInput() {
		if (idField.getText().equals(""))
			return false;
		if (studentIdField.getText().equals(""))
			return false;
		if (nameField.getText().equals(""))
			return false;
		if (academyField.getText().equals(""))
			return false;
		if (departmentField.getText().equals(""))
			return false;
		if (ageField.getValue() == null)
			return false;
		if (dateField.getValue() == null)
			return false;
		if (summaryArea.getText().equals(""))
			return false;
		return true;
	}

	private void initField() {
		idField.setValue(null);
		studentIdField.setText("");
		nameField.setText("");
		academyField.setText("");
		departmentField.setText("");
		ageField.setValue(null);
		maleButton.setSelected(true);
		dateField.setValue(null);
		summaryArea.setText("");
	}

	private Reader buildReaderFromField() {
		int id = ((Number) idField.getValue()).intValue();
		String stuId = studentIdField.getText();
		String name = nameField.getText();
		int age = ((Number) ageField.getValue()).intValue();
		String sex = "男";
		if (femaleButton.isSelected())
			sex = "女";
		String academy = academyField.getText();
		String department = departmentField.getText();
		String dateAsString = dateField.getText();
		int amount = 0;
		String summary = summaryArea.getText();
		Reader reader = new Reader(id, stuId, name, age, sex, academy,
				department, dateAsString, amount, summary);
		return reader;
	}

	private JFormattedTextField idField;

	private JTextField studentIdField;

	private JTextField nameField;

	private JTextField academyField;

	private JTextField departmentField;

	private JFormattedTextField ageField;

	private ButtonGroup group;

	private JRadioButton maleButton;

	private JRadioButton femaleButton;

	private JFormattedTextField dateField;

	private JTextArea summaryArea;

	private JButton modifyButton;

	private JButton deleteButton;

	private JButton clearButton;

	private JButton cancelButton;

	private JDialog dialog = null;

	private ResultSetTableModel tableModel;

	private JTable readerTable;

	private String readerQuery;

	private String[] columnNames;
}