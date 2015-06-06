package net.sfte.htlibrary.ui;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

/**
 * This class defines a TableModel for JTable, with this class, You can
 * construct a JTable display data from database very easy. Warning: The
 * database must support scrolling cursors. If not, this class and the JTable
 * will break down.
 * 
 * @author wenwen
 */
public class ResultSetTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construction of ResultSetTableModel
	 * 
	 * @param aResultSet
	 *            data obtained from database query. The type of ResultSet must
	 *            be TYPE_SCROLL_INSENSITIVE or TYPE_SCROLL_SENSITIVE
	 * @param aColumnNames
	 *            column names of the JTable.
	 */
	public ResultSetTableModel(ResultSet aResultSet, String[] aColumnNames) {
		rs = aResultSet;
		columnNames = aColumnNames;
		try {
			rsmd = rs.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getColumnName(int c) {
		if (c < columnNames.length)
			return columnNames[c];
		else
			return "";
	}

	public int getColumnCount() {
		try {
			return rsmd.getColumnCount();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public int getRowCount() {
		try {
			rs.last();
			return rs.getRow();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public Object getValueAt(int r, int c) {
		try {
			rs.absolute(r + 1);
			return rs.getObject(c + 1);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private ResultSet rs;

	private ResultSetMetaData rsmd;

	private String[] columnNames;
}