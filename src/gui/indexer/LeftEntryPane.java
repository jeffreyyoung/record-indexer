package gui.indexer;

import gui.indexer.form.IndexerFormPanel;
import gui.indexer.table.IndexerTabelPanel;

import java.io.Serializable;

import javax.swing.*;


@SuppressWarnings("serial")
public class LeftEntryPane extends JTabbedPane implements Serializable{


	private JPanel tablePanel = new JPanel();
	private IndexerTabelPanel indexerTablePanel;
	
	private JPanel formPanel = new JPanel();
	private IndexerFormPanel indexerFormPanel;
	
	public JPanel getTablePanel() {
		return tablePanel;
	}
	
	public JPanel getFormPanel() {
		return formPanel;
	}

	public IndexerFormPanel getIndexerFormPanel(){
		return indexerFormPanel;
	}
	
	public void addIndexerFormPanel(IndexerFormPanel ifp){
		this.indexerFormPanel = ifp;
		formPanel.add(ifp);
	}
	
	public IndexerTabelPanel getIndexerTablePanel(){
		return indexerTablePanel;
	}
	
	public void addIndexerTablePanel(IndexerTabelPanel itp){
		//this.tablePanel = new JScrollPane();
		this.indexerTablePanel = itp;
		//tablePanel.setViewportView(itp);
		tablePanel.add(itp);
	}
	
	LeftEntryPane(){
		super();
		
		addTab("Table Entry", null, tablePanel, "Table View");
		
		addTab("Form Entry", null, formPanel, "Form Entry");
		
	}
	
}
