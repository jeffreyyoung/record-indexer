package gui.indexer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import gui.BatchState;
import gui.loadsave.BatchStateLoadSave;
import gui.miscframes.DownloadBatchFrame;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class IndexerMenuBar extends JMenuBar implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7452571178533546197L;
	private JMenuItem downloadBatchItem, logoutItem, exitItem;
	private BatchState batchState;
	
	IndexerMenuBar(BatchState batchState){
		super();
		
		this.batchState = batchState;
		JMenu fileMenu = new JMenu("File");
		
		downloadBatchItem = fileMenu.add("Download Batch");
		logoutItem = fileMenu.add("Logout");
		exitItem = fileMenu.add("Exit");
		add(fileMenu);
		
		downloadBatchItem.addActionListener(downloadBatchListener);
		logoutItem.addActionListener(logoutListener);
		exitItem.addActionListener(exitListener);
	}
	
	public JMenuItem getDownloadBatchItem() {
		return downloadBatchItem;
	}
	
	private ActionListener downloadBatchListener = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			DownloadBatchFrame dlFrame = new DownloadBatchFrame(batchState);
			dlFrame.setVisible(true);
			
		}	
	};
	
	private ActionListener logoutListener = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			BatchStateLoadSave.saveBatchState(batchState);
			batchState.getIndexerFrame().setVisible(false);
			batchState.getLoginFrame().setVisible(true);

			
		}
	};
	
	private ActionListener exitListener = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	};

}
