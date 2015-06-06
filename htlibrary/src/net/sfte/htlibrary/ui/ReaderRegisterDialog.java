package net.sfte.htlibrary.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.sfte.htlibrary.database.Reader;
import net.sfte.htlibrary.database.ReaderOperation;

/**
 * This class defines the dialog used to register new reader.
 * 
 * @author wenwen
 */
public class ReaderRegisterDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReaderRegisterDialog() {
		setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		idField = new JTextField("自动生成");
		idField.setEditable(false);
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
		dateField.setValue(new Date());
		dateField.setEditable(false);

		summaryArea = new JTextArea();
		summaryArea.setLineWrap(true);

		JPanel labelPanel = new JPanel();
		labelPanel.add(new JLabel("欢迎读者注册本馆会员, 享受全面优质服务"));
		labelPanel.setBorder(BorderFactory.createEtchedBorder());
		add(labelPanel, BorderLayout.NORTH);

		panel.add(new JLabel("编号: "), new GBC(0, 0).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(idField, new GBC(1, 0).setFill(GBC.HORIZONTAL).setWeight(100,
				0).setInsets(5));

		panel.add(new JLabel("日期: "), new GBC(2, 0).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(dateField, new GBC(3, 0).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));

		panel.add(new JLabel("学号: "), new GBC(0, 1).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(studentIdField, new GBC(1, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));

		panel.add(new JLabel("姓名: "), new GBC(2, 1).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(nameField, new GBC(3, 1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));

		panel.add(new JLabel("学院: "), new GBC(0, 2).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(academyField, new GBC(1, 2).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));

		panel.add(new JLabel("系别: "), new GBC(2, 2).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(departmentField, new GBC(3, 2).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));

		panel.add(new JLabel("年龄: "), new GBC(0, 3).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(ageField, new GBC(1, 3).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));

		panel.add(new JLabel("性别: "), new GBC(2, 3).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(sexPanel, new GBC(3, 3).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));

		panel.add(new JLabel("简介: "), new GBC(0, 4).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(new JScrollPane(summaryArea), new GBC(1, 4, 4, 4).setFill(
				GBC.BOTH).setWeight(100, 100).setInsets(5));

		panel.setBorder(BorderFactory.createEtchedBorder());
		add(panel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		registerButton = new JButton("注册");
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!checkInput()) {
					JOptionPane.showMessageDialog(dialog,
							"为了更好地管理和提供服务, 请输入完整读者信息", "输入错误",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				Reader reader = buildReaderFromInput();
				if (ReaderOperation.registerReader(reader)) {
					JOptionPane.showMessageDialog(dialog,
							"读者登记成功, 注册信息已导入数据库!", "操作成功",
							JOptionPane.INFORMATION_MESSAGE);
					initField();
				} else {
					JOptionPane.showMessageDialog(dialog,
							"非常抱歉, 读者登记失败, 请重新注册!", "操作失败",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		clearButton = new JButton("清除");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initField();
			}
		});
		cancelButton = new JButton("退出");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		buttonPanel.add(registerButton);
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
			dialog.getRootPane().setDefaultButton(registerButton);
			dialog.pack();
		}
		dialog.setTitle("读者注册登记");
		dialog.setSize(700, 450);
		// set the Location of this dialog to the center
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension size = kit.getScreenSize();
		int width = size.width;
		int height = size.height;
		dialog.setLocation((width - dialog.getWidth()) / 2, (height - dialog
				.getHeight()) / 2);
		dialog.setVisible(true);
	}

	private void initField() {
		idField.setText("自动生成");
		studentIdField.setText("");
		nameField.setText("");
		academyField.setText("");
		departmentField.setText("");
		ageField.setValue(null);
		maleButton.setSelected(true);
		dateField.setValue(new Date());
		summaryArea.setText("");
	}

	private boolean checkInput() {
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
		return true;
	}

	private Reader buildReaderFromInput() {
		int readerId = 0;
		String studentId = studentIdField.getText();
		String name = nameField.getText();
		int age = ((Number) ageField.getValue()).intValue();
		String sex = "男";
		if (femaleButton.isSelected())
			sex = "女";
		String academy = academyField.getText();
		String department = departmentField.getText();
		String registerDate = dateField.getText();
		String summary = summaryArea.getText();
		Reader reader = new Reader(readerId, studentId, name, age, sex,
				academy, department, registerDate, 0, summary);
		return reader;
	}

	private JTextField idField;

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

	private JButton registerButton;

	private JButton clearButton;

	private JButton cancelButton;

	private JDialog dialog = null;
}