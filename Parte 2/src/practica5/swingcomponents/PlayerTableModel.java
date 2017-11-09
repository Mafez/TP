package practica5.swingcomponents;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class PlayerTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private String[] column = {"Player", "Mode", "#Piece"};
	private ArrayList<Object[]> content;
	
	public PlayerTableModel() {
		this.content = new ArrayList<Object[]>();
	}
	
	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		return this.content.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if(row < 0 || row >= this.getRowCount() || col < 0 || col >= this.getColumnCount())
			return null;
		else
			return this.content.get(row)[col];
	}
	
	@Override
	public String getColumnName(int col){
		return this.column[col];
	}
	
	/**
	 * Añade una fila a la tabla.
	 * @param row Fila a añadir.
	 */
	public void addRow(Object[] row) {
		this.content.add(row);
		this.fireTableDataChanged();;
	}

	public void deleteAllRows() {
		content = new ArrayList<Object[]>();
	}
}
