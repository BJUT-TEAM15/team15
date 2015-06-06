package net.sfte.htlibrary.ui;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import net.sfte.htlibrary.database.HtConnection;

/**
 * This class defines a panel with Reader's basic information. It will be used
 * in borrow book dialog, return back book and other dialogs.
 * 
 * @author wenwen
 */
public class ReaderInfoPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReaderInfoPanel() {
		setLayout(new GridBagLayout());

		idField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		idField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setField();
			}
		});
		idField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				setField();
			}
		});
		idField.setInputVerifier(new FormattedTextFieldVerifier());
		studentIdField = new JTextField();
		studentIdField.setEditable(false);
		nameField = new JTextField();
		nameField.setEditable(false);
		ageField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		ageField.setEditable(false);
		sexField = new JTextField();
		sexField.setEditable(false);
		academyField = new JTextField();
		academyField.setEditable(false);
		departmentField = new JTextField();
		departmentField.setEditable(false);
		departmentField.setColumns(10);
		dateField = new JFormattedTextField(DateFormat.getDateInstance());
		dateField.setEditable(false);
		bookAmountField = new JFormattedTextField(NumberFormat
				.getIntegerInstance());
		bookAmountField.setEditable(false);
		totalAmountField = new JFormattedTextField(NumberFormat
				.getIntegerInstance());
		totalAmountField.setEditable(false);
		summaryArea = new JTextArea();
		summaryArea.setEditable(false);
		summaryArea.setLineWrap(true);

		add(new JLabel("���߱��: "), new GBC(0, 0).setAnchor(GBC.EAST));
		add(idField, new GBC(1, 0).setFill(GBC.HORIZONTAL).setWeight(100, 0)
				.setInsets(5));
		add(new JLabel("[�س�]"), new GBC(2, 0).setAnchor(GBC.EAST));

		add(new JLabel("����ѧ��: "), new GBC(0, 1).setAnchor(GBC.EAST));
		add(studentIdField, new GBC(1, 1, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));

		add(new JLabel("����: "), new GBC(0, 2).setAnchor(GBC.EAST));
		add(nameField, new GBC(1, 2, 2, 1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));

		add(new JLabel("����: "), new GBC(0, 3).setAnchor(GBC.EAST));
		add(ageField, new GBC(1, 3, 2, 1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));

		add(new JLabel("�Ա�: "), new GBC(0, 4).setAnchor(GBC.EAST));
		add(sexField, new GBC(1, 4, 2, 1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));

		add(new JLabel("ѧԺ: "), new GBC(0, 5).setAnchor(GBC.EAST));
		add(academyField, new GBC(1, 5, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));

		add(new JLabel("ϵ��: "), new GBC(0, 6).setAnchor(GBC.EAST));
		add(departmentField, new GBC(1, 6, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));

		add(new JLabel("ע������: "), new GBC(0, 7).setAnchor(GBC.EAST));
		add(dateField, new GBC(1, 7, 2, 1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));

		add(new JLabel("��ǰ����: "), new GBC(0, 8).setAnchor(GBC.EAST));
		add(bookAmountField, new GBC(1, 8, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));

		add(new JLabel("��������: "), new GBC(0, 9).setAnchor(GBC.EAST));
		add(totalAmountField, new GBC(1, 9, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));

		add(new JLabel("���: "), new GBC(0, 10).setAnchor(GBC.EAST));

		add(new JScrollPane(summaryArea), new GBC(1, 10, 2, 3)
				.setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));

		Border etched = BorderFactory.createEtchedBorder();
		setBorder(BorderFactory.createTitledBorder(etched, "���߻�����Ϣ"));
	}

	private void setField() {
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
				ageField.setValue(rs.getInt(4));
				sexField.setText(rs.getString(5));
				academyField.setText(rs.getString(6));
				departmentField.setText(rs.getString(7));
				dateField.setValue(rs.getDate(8));
				totalAmountField.setValue(rs.getInt(9));
				summaryArea.setText(rs.getString(10));
			} else {
				initField();
				return;
			}

			pstmt = con.prepareStatement("SELECT COUNT(*) FROM borrowbook "
					+ "WHERE reader_id = ? " + "GROUP BY reader_id");
			pstmt.setInt(1, Integer.parseInt(idField.getText()));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				bookAmountField.setValue(rs.getInt(1));
			} else {
				bookAmountField.setValue(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void initField() {
		idField.setValue(new Integer(0));
		studentIdField.setText("");
		nameField.setText("");
		ageField.setValue(null);
		sexField.setText("");
		academyField.setText("");
		departmentField.setText("");
		dateField.setValue(null);
		bookAmountField.setValue(null);
		totalAmountField.setValue(null);
		summaryArea.setText("");
	}

	private class FormattedTextFieldVerifier extends InputVerifier {
		public boolean verify(JComponent component) {
			JFormattedTextField field = (JFormattedTextField) component;
			return field.isEditValid();
		}
	}

	protected JFormattedTextField idField;

	private JTextField studentIdField;

	private JTextField nameField;

	private JFormattedTextField ageField;

	private JTextField sexField;

	private JTextField academyField;

	private JTextField departmentField;

	private JFormattedTextField dateField;

	private JFormattedTextField bookAmountField;

	private JFormattedTextField totalAmountField;

	private JTextArea summaryArea;
}