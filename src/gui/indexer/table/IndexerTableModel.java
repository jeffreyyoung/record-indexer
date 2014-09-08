package gui.indexer.table;

import gui.BatchState;

import java.io.Serializable;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class IndexerTableModel extends AbstractTableModel implements Serializable{
	
	private List<List<String>> dataTable;
	private String[] columnNames;
	private BatchState batchState;
	
	IndexerTableModel(BatchState batchState){
		//super();
		this.dataTable = batchState.getValues();
		this.batchState = batchState;
		this.columnNames = getColumnNames();
	}
	
	public String getColumnName(int columnIndex) {
	    return columnNames[columnIndex];
	}
	
	private String[] getColumnNames(){
		String[] names = new String[batchState.getFields().size() + 1];
		
		names[0] = "Record";
		
		for (int i = 0; i < batchState.getFields().size(); i++){
		
			int index = i + 1;
			
			names[index] = batchState.getFields().get(i).getTitle();
			
		}
		
		return names;
		
	}
	
	public boolean isCellEditable(int row, int col) { 
		if (col == 0)
			return false;
		else
			return true; 
	}
	
	public void setValueAt(Object value, int row, int col) {
		dataTable.get(row).set(col, (String)value);
		
		batchState.getIndexerFrame().getBottomLeftPane().getIndexerFormPanel().updateFieldValues();
		batchState.suggestWords((String)value, row, col - 1); //TODO add setSugestedWords, maybe change to getSuggestedWords()
		
	}

	
	@Override
	public int getColumnCount() {
		return dataTable.get(0).size();
	}

	@Override
	public int getRowCount() {
		return dataTable.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		return dataTable.get(row).get(col);
	}

}
