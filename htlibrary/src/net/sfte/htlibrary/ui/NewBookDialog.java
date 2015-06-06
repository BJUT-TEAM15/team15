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
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.sfte.htlibrary.database.Book;
import net.sfte.htlibrary.database.BookOperation;
import net.sfte.htlibrary.database.HtConnection;

/**
 * This class defines the dialog of new book register. You must provide the
 * detailed information of the new book Such as book isbn, title, author,
 * publisher, price and so on.
 * 
 * @author wenwen
 */
public class NewBookDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NewBookDialog() {
		setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		idField = new JTextField("自动生成");
		idField.setEditable(false);
		isbnField = new JTextField("");
		titleField = new JTextField("");
		authorField = new JTextField("");
		publisherField = new JTextField("");

		priceField = new JFormattedTextField(NumberFormat.getNumberInstance());
		totalAmountField = new JFormattedTextField(NumberFormat
				.getIntegerInstance());
		dateField = new JFormattedTextField(DateFormat.getDateInstance());
		dateField.setEditable(false);
		dateField.setValue(new Date());

		bookQuery = "SELECT * FROM book ORDER BY book_id";
		columnNames = new String[] { "图书编号", "标准ISBN", "图书名称", "图书作者", "出版社",
				"图书价格", "馆藏总数", "现存数量", "入馆日期", "被借次数" };
		model = HtConnection.getTableModel(bookQuery, columnNames);
		bookTable = new JTable(model);

		panel.add(new JLabel("请输入完整图书信息: "), new GBC(0, 0, 4, 1).setAnchor(
				GBC.WEST).setInsets(5));
		panel.add(new JLabel("图书编号: "), new GBC(0, 1).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(idField, new GBC(1, 1).setFill(GBC.HORIZONTAL).setWeight(100,
				0).setInsets(5));
		panel.add(new JLabel("标准ISBN: "), new GBC(2, 1).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(isbnField, new GBC(3, 1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("图书名称: "), new GBC(0, 2).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(titleField, new GBC(1, 2).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("图书作者: "), new GBC(2, 2).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(authorField, new GBC(3, 2).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("出版社: "), new GBC(0, 3).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(publisherField, new GBC(1, 3).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));
		panel.add(new JLabel("图书价格: "), new GBC(2, 3).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(priceField, new GBC(3, 3).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("加入数量: "), new GBC(0, 4).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(totalAmountField, new GBC(1, 4).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));
		panel.add(new JLabel("入馆时间: "), new GBC(2, 4).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(dateField, new GBC(3, 4).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JScrollPane(bookTable), new GBC(0, 5, 4, 4).setFill(
				GBC.BOTH).setWeight(100, 100).setInsets(5));
		panel.setBorder(BorderFactory.createEtchedBorder());
		add(panel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		okButton = new JButton("添加");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!checkInput()) {
					JOptionPane.showMessageDialog(dialog, "请输入图书完整信息, 否则无法入库!",
							"非法请求", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Book book = buildBookFromField();
				if (BookOperation.newBook(book)) {
					JOptionPane.showMessageDialog(dialog,
							"成功添加新书, 新书信息已保存到数据库中!", "操作成功",
							JOptionPane.INFORMATION_MESSAGE);
					initField();
					bookTable.setModel(HtConnection.getTableModel(bookQuery,
							columnNames));
				} else {
					JOptionPane.showMessageDialog(dialog,
							"非常抱歉, 发生错误导致添加新书失败!", "操作失败",
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
		buttonPanel.add(okButton);
		buttonPanel.add(clearButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	public void showDialog(Component parent) {
		initField();
		bookTable.setModel(HtConnection.getTableModel(bookQuery, columnNames));
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
		dialog.setTitle("新书登记");
		dialog.setSize(800, 550);
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
		isbnField.setText("");
		titleField.setText("");
		authorField.setText("");
		publisherField.setText("");
		priceField.setValue(null);
		totalAmountField.setValue(null);
		dateField.setValue(new Date());
	}

	private boolean checkInput() {
		if (isbnField.getText().equals(""))
			return false;
		if (titleField.getText().equals(""))
			return false;
		if (authorField.getText().equals(""))
			return false;
		if (publisherField.getText().equals(""))
			return false;
		if (priceField.getValue() == null)
			return false;
		if (totalAmountField.getValue() == null)
			return false;
		return true;
	}

	private Book buildBookFromField() {
		int id = 0;
		String isbn = isbnField.getText();
		String title = titleField.getText();
		String author = authorField.getText();
		String publisher = publisherField.getText();
		double price = ((Number) priceField.getValue()).doubleValue();
		int totalAmount = ((Number) totalAmountField.getValue()).intValue();
		int amount = totalAmount;
		String addDateAsString = dateField.getText();
		int borrowedTimes = 0;
		Book book = new Book(id, isbn, title, author, publisher, price,
				totalAmount, amount, addDateAsString, borrowedTimes);
		return book;
	}

	private JTextField idField;

	private JTextField isbnField;

	private JTextField titleField;

	private JTextField authorField;

	private JTextField publisherField;

	private JFormattedTextField priceField;

	private JFormattedTextField totalAmountField;

	private JFormattedTextField dateField;

	private ResultSetTableModel model;

	private JTable bookTable;

	private String bookQuery;

	private String[] columnNames;

	private JButton okButton;

	private JButton clearButton;

	private JButton cancelButton;

	private JDialog dialog = null;
}