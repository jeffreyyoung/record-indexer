package gui.indexer.table;

import gui.BatchState;
import gui.BatchStateListener;
import gui.indexer.suggestion.SuggestionPopUp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class IndexerTabelPanel extends JPanel implements BatchStateListener,
		Serializable {

	private IndexerTableModel tableModel;

	private JTable table;
	private static BatchState batchState;

	public IndexerTabelPanel(BatchState batchState_in) {
		// super();

		IndexerTabelPanel.batchState = batchState_in;
		this.tableModel = new IndexerTableModel(batchState);
		this.table = new JTable(tableModel);

		table.setRowHeight(18);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(true);
		table.getTableHeader().setReorderingAllowed(false);

		TableColumnModel columnModel = table.getColumnModel();
		for (int i = 0; i < tableModel.getColumnCount(); ++i) {
			TableColumn column = columnModel.getColumn(i);
			column.setPreferredWidth(70);
		}
		for (int i = 1; i < tableModel.getColumnCount(); ++i) {
			TableColumn column = columnModel.getColumn(i);
			column.setCellRenderer(new IndexerTableCellRenderer(batchState));
		}
		
		table.addMouseListener(new RightClickAdapter(batchState));

		setLayout(new BorderLayout());
		
		add(table.getTableHeader(), BorderLayout.NORTH);
		add(table, BorderLayout.CENTER);

	}

	private class RightClickAdapter extends MouseAdapter{
		
		BatchState bs;
		
		public RightClickAdapter(BatchState bs){
			
			super();
			
			this.bs = bs;
		}
		
		@Override
		public void mouseClicked(MouseEvent e){
			if (SwingUtilities.isRightMouseButton(e)) {

				JTable target = (JTable) e.getSource();

				int clickedRow = target.rowAtPoint(e.getPoint());
				int clickedColumn = target.columnAtPoint(e.getPoint());
				
				if (batchState.getValueSuggestions().get(clickedRow).get(clickedColumn - 1) != null){
					JPopupMenu popup = new SuggestionPopUp(clickedRow, clickedColumn, bs);
		            popup.show(e.getComponent(), e.getX(), e.getY());
				}

			}
			
		}
	}

	@Override
	public void selectedCellChanged(int row, int col) {
		table.changeSelection(row, col + 1, false, false);

	}

	@Override
	public void valueChanged(int row, int col, String value) {
		

	}


	class IndexerTableCellRenderer extends JLabel implements TableCellRenderer {

		private Border selectedBorder = BorderFactory.createLineBorder(
				Color.BLUE, 2);
		private BatchState batchState;

		public IndexerTableCellRenderer(BatchState batchState) {
			this.batchState = batchState;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			if (isSelected) {
				this.setBorder(selectedBorder);		
				batchState.setSelectedRowAndCol(row, column - 1);

			} else {
				this.setBorder(null);

			}

			this.setText((String) value);

			if (batchState.getValueSuggestions() != null && batchState.getValueSuggestions().get(row).get(column - 1) != null) {	
				this.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
			} 
			
			return this;
		}

	}
	
	public IndexerTableModel getTableModel() {
		return tableModel;
	}

}
