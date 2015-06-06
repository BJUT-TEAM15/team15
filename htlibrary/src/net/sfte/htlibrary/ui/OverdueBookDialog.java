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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import net.sfte.htlibrary.database.HtConnection;

/**
 * This class shows the overdue book.
 * 
 * @author wenwen
 */
public class OverdueBookDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OverdueBookDialog() {
		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();

		JToolBar toolBar = new JToolBar("ToolBar");
		toolBar.setFloatable(false);
		printButton = new JButton("打印", new ImageIcon("images/print.png"));
		printButton.setBorderPainted(false);
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					bookTable.print();
				} catch (java.awt.print.PrinterException exception) {
					System.out.println("打印出错");
				}
			}
		});
		exitButton = new JButton("退出", new ImageIcon("images/exit.png"));
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

		overdueBookQuery = "SELECT book.book_id, isbn, title, "
				+ "publisher, reader.reader_id, student_id, reader_name, "
				+ "academy, department, DATEDIFF('day', revertible_date, CURDATE()), "
				+ "revertible_date " + "FROM book, reader, borrowbook "
				+ "WHERE book.book_id = borrowbook.book_id AND "
				+ "reader.reader_id = borrowbook.reader_id AND "
				+ "DATEDIFF('day', revertible_date, CURDATE()) > 0";
		columnNames = new String[] { "图书编号", "标准ISBN", "图书名称", "出版社", "读者编号",
				"学号", "读者姓名", "学院", "系别", "超期天数", "应还日期" };
		model = HtConnection.getTableModel(overdueBookQuery, columnNames);
		bookTable = new JTable(model);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder(etched, "超期图书"));
		panel.add(new JScrollPane(bookTable), new GBC(0, 0).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));

		add(panel, BorderLayout.CENTER);
	}

	public void showDialog(Component parent) {
		bookTable.setModel(HtConnection.getTableModel(overdueBookQuery,
				columnNames));
		Frame owner = null;
		if (parent instanceof Frame)
			owner = (Frame) parent;
		else
			owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class,
					parent);
		if (dialog == null || dialog.getOwner() != owner) {
			dialog = new JDialog(owner, true);
			dialog.add(this);
			dialog.getRootPane().setDefaultButton(exitButton);
			dialog.pack();
		}
		dialog.setTitle("超期图书查询");
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

	private JButton printButton;

	private JButton exitButton;

	private ResultSetTableModel model;

	private JTable bookTable;

	private String overdueBookQuery;

	private String[] columnNames;

	private JDialog dialog = null;
}