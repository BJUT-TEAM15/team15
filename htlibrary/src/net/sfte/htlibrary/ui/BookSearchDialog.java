package net.sfte.htlibrary.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import net.sfte.htlibrary.database.HtConnection;

public class BookSearchDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BookSearchDialog() {
		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();

		JToolBar toolBar = new JToolBar("ToolBar");
		toolBar.setFloatable(false);
		printButton = new JButton("��ӡ", new ImageIcon("images/print.png"));
		printButton.setBorderPainted(false);
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					bookTable.print();
				} catch (PrinterException exception) {
					exception.printStackTrace();
				}
			}
		});
		exitButton = new JButton("�˳�", new ImageIcon("images/exit.png"));
		exitButton.setBorderPainted(false);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		toolBar.add(printButton);
		toolBar.addSeparator();
		toolBar.add(exitButton);
		add(toolBar, BorderLayout.NORTH);

		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new GridBagLayout());
		searchPanel.setBorder(BorderFactory.createTitledBorder(etched, "��ѯ����"));
		bookIdField = new JTextField();
		isbnField = new JTextField();
		titleField = new JTextField();
		authorField = new JTextField();
		publisherField = new JTextField();
		searchButton = new JButton("��ʼ��ѯ");
		searchButton.addActionListener(new BookSearchListener());
		searchPanel.add(new JLabel("ͼ����: "), new GBC(0, 1).setAnchor(GBC.EAST)
				.setInsets(5));
		searchPanel.add(bookIdField, new GBC(1, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 100).setInsets(5));

		searchPanel.add(searchButton, new GBC(2, 1).setAnchor(GBC.WEST)
				.setInsets(5));

		searchPanel.add(new JLabel("��׼ISBN: "), new GBC(0, 2).setAnchor(
				GBC.EAST).setInsets(5));
		searchPanel.add(isbnField, new GBC(1, 2).setFill(GBC.HORIZONTAL)
				.setWeight(100, 100).setInsets(5));

		searchPanel.add(new JLabel("ͼ������: "), new GBC(2, 2).setAnchor(GBC.EAST)
				.setInsets(5));
		searchPanel.add(titleField, new GBC(3, 2).setFill(GBC.HORIZONTAL)
				.setWeight(100, 100).setInsets(5));

		searchPanel.add(new JLabel("ͼ������: "), new GBC(0, 3).setAnchor(GBC.EAST)
				.setInsets(5));
		searchPanel.add(authorField, new GBC(1, 3).setFill(GBC.HORIZONTAL)
				.setWeight(100, 100).setInsets(5));

		searchPanel.add(new JLabel("������: "), new GBC(2, 3).setAnchor(GBC.EAST)
				.setInsets(5));
		searchPanel.add(publisherField, new GBC(3, 3).setFill(GBC.HORIZONTAL)
				.setWeight(100, 100).setInsets(5));

		JPanel messagePanel = new JPanel();
		messagePanel
				.setBorder(BorderFactory.createTitledBorder(etched, "��ѯ˵��"));
		messagePanel.add(new JLabel("<html>�����ұ������ѯ����: <br>"
				+ "ͼ����, ISBN Ϊ��ȷƥ��<br>���������Ϊģ��ƥ��<br>" + "����ֻ��дһ���ĳ����</html>"));

		String[] columnNames = { "ͼ����", "��׼ISBN", "ͼ������", "����", "������", "ͼ��۸�",
				"�ݲ�����", "�ִ�����", "���ʱ��", "�������" };
		model = new DefaultTableModel(columnNames, 0);
		bookTable = new JTable(model);
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridBagLayout());
		tablePanel.setBorder(BorderFactory.createTitledBorder(etched, "��ѯ���"));
		tablePanel.add(new JScrollPane(bookTable), new GBC(0, 0).setFill(
				GBC.BOTH).setWeight(100, 100).setInsets(5));

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(messagePanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(35, 0)
				.setInsets(5));
		panel.add(searchPanel, new GBC(1, 0).setFill(GBC.BOTH)
				.setWeight(100, 0).setInsets(5));
		panel.add(tablePanel, new GBC(0, 1, 2, 2).setFill(GBC.BOTH).setWeight(
				100, 100).setInsets(5));
		add(panel, BorderLayout.CENTER);
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
			dialog.getRootPane().setDefaultButton(searchButton);
			dialog.pack();
		}
		dialog.setTitle("ͼ���ѯ");
		// set the Location of this dialog to the center
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension size = kit.getScreenSize();
		int width = size.width;
		int height = size.height;
		if (width <= 800)
			dialog.setSize(750, 600);
		else
			dialog.setSize(950, 640);
		dialog.setLocation((width - dialog.getWidth()) / 2, (height - dialog
				.getHeight()) / 2);
		dialog.setVisible(true);
	}

	private class BookSearchListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!checkInput()) {
				JOptionPane.showMessageDialog(dialog, "����������һ��ͼ����Ϣ! �����޷���ѯ!",
						"�������", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String bookQuery;
			if (!bookIdField.getText().trim().equals("")) {
				bookQuery = "SELECT * FROM book WHERE book_id = "
						+ Integer.parseInt(bookIdField.getText());
			} else {
				if (!isbnField.getText().trim().equals("")) {
					bookQuery = "SELECT * FROM book WHERE isbn = "
							+ ("'" + isbnField.getText() + "'");
				} else {
					bookQuery = "SELECT * FROM book " + "WHERE title LIKE "
							+ "'%" + titleField.getText() + "%'"
							+ " AND author LIKE " + "'%"
							+ authorField.getText() + "%'"
							+ " AND publisher LIKE " + "'%"
							+ publisherField.getText() + "%'";
				}
			}
			int rowCount = model.getRowCount();
			for (int i = 0; i < rowCount; i++)
				model.removeRow(0);
			try {
				Connection con = HtConnection.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(bookQuery);
				while (rs.next()) {
					int columnCount = rs.getMetaData().getColumnCount();
					Object[] values = new Object[columnCount];
					for (int i = 0; i < columnCount; i++)
						values[i] = rs.getObject(i + 1);
					model.addRow(values);
				}
			} catch (SQLException exception) {
				exception.printStackTrace();
			} finally {
				if (model.getRowCount() == 0) {
					JOptionPane.showMessageDialog(dialog,
							"���β�ѯδ�ҵ����ͼ��! �볢��������ѯģʽ!", "�����ʾ",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}

	private boolean checkInput() {
		boolean ok = false;
		if (!bookIdField.getText().trim().equals(""))
			ok = true;
		if (!isbnField.getText().trim().equals(""))
			ok = true;
		if (!titleField.getText().trim().equals(""))
			ok = true;
		if (!authorField.getText().trim().equals(""))
			ok = true;
		if (!publisherField.getText().trim().equals(""))
			ok = true;
		return ok;
	}

	private void initField() {
		bookIdField.setText(null);
		isbnField.setText("");
		titleField.setText("");
		authorField.setText("");
		publisherField.setText("");
		int rowCount = model.getRowCount();
		for (int i = 0; i < rowCount; i++)
			model.removeRow(0);
	}

	private JButton printButton;

	private JButton exitButton;

	private JTextField bookIdField;

	private JTextField isbnField;

	private JTextField titleField;

	private JTextField authorField;

	private JTextField publisherField;

	private JButton searchButton;

	private DefaultTableModel model;

	private JTable bookTable;

	private JDialog dialog = null;
}