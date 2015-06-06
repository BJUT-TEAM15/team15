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
import java.text.NumberFormat;

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
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import net.sfte.htlibrary.database.BookOperation;
import net.sfte.htlibrary.database.HtConnection;

/**
 * This class defines the dialog Modify book information. You can modify book
 * isbn, title, author, publisher and price.
 * 
 * @author wenwen
 */
public class ModifyBookDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ModifyBookDialog() {
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
							.prepareStatement("SELECT * FROM book WHERE book_id = ?");
					pstmt.setInt(1, Integer.parseInt(idField.getText()));
					ResultSet rs = pstmt.executeQuery();
					if (rs.next()) {
						isbnField.setText(rs.getString(2));
						titleField.setText(rs.getString(3));
						authorField.setText(rs.getString(4));
						publisherField.setText(rs.getString(5));
						priceField.setValue(rs.getObject(6));
					} else {
						initField();
					}
				} catch (SQLException exception) {
					exception.printStackTrace();
				}
			}
		});
		isbnField = new JTextField("");
		titleField = new JTextField("");
		authorField = new JTextField("");
		publisherField = new JTextField("");
		priceField = new JFormattedTextField(NumberFormat.getNumberInstance());
		bookQuery = "SELECT book_id, isbn, title, author, "
				+ "publisher, price FROM book ORDER BY book_id";
		columnNames = new String[] { "图书编号", "标准ISBN", "图书名称", "图书作者", "出版社",
				"图书价格" };
		model = HtConnection.getTableModel(bookQuery, columnNames);
		bookTable = new JTable(model);
		bookTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		bookTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = bookTable.getSelectedRow();
				if (row != -1) {
					Object obj = null;
					obj = bookTable.getValueAt(row, 0);
					idField.setValue(obj);
					obj = bookTable.getValueAt(row, 1);
					isbnField.setText(obj.toString());
					obj = bookTable.getValueAt(row, 2);
					titleField.setText(obj.toString());
					obj = bookTable.getValueAt(row, 3);
					authorField.setText(obj.toString());
					obj = bookTable.getValueAt(row, 4);
					publisherField.setText(obj.toString());
					obj = bookTable.getValueAt(row, 5);
					priceField.setValue(obj);
				}
			}
		});

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(new JLabel("请从下表中选择要修改的图书, " + "或者直接输入正确的图书编号: "), new GBC(0,
				0, 4, 1).setAnchor(GBC.WEST).setInsets(5));
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
		panel.add(new JScrollPane(bookTable), new GBC(0, 4, 4, 4).setFill(
				GBC.BOTH).setWeight(100, 100).setInsets(5));
		panel.setBorder(BorderFactory.createEtchedBorder());
		add(panel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		okButton = new JButton("修改");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!checkInput()) {
					JOptionPane.showMessageDialog(dialog,
							"请填写图书完整信息, 否则无法修改图书信息", "非法请求",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				int id = ((Number) idField.getValue()).intValue();
				String isbn = isbnField.getText();
				String title = titleField.getText();
				String author = authorField.getText();
				String publisher = publisherField.getText();
				double price = ((Number) priceField.getValue()).doubleValue();
				if (BookOperation.modifyBook(id, isbn, title, author,
						publisher, price)) {
					JOptionPane.showMessageDialog(dialog, "成功修改图书信息", "操作成功",
							JOptionPane.INFORMATION_MESSAGE);
					bookTable.setModel(HtConnection.getTableModel(bookQuery,
							columnNames));
					initField();
				} else {
					JOptionPane.showMessageDialog(dialog, "非常抱歉, 修改图书信息失败",
							"操作失败", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		clearButton = new JButton("清空");
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
		dialog.setTitle("图书基本信息修改");
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

	private void initField() {
		idField.setValue(null);
		isbnField.setText("");
		titleField.setText("");
		authorField.setText("");
		publisherField.setText("");
		priceField.setValue(null);
	}

	private boolean checkInput() {
		if (idField.getValue() == null)
			return false;
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
		return true;
	}

	private JFormattedTextField idField;

	private JTextField isbnField;

	private JTextField titleField;

	private JTextField authorField;

	private JTextField publisherField;

	private JFormattedTextField priceField;

	private JButton okButton;

	private JButton clearButton;

	private JButton cancelButton;

	private ResultSetTableModel model;

	private JTable bookTable;

	private String bookQuery;

	private String[] columnNames;

	private JDialog dialog = null;
}