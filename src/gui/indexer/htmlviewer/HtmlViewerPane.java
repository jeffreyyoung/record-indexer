package gui.indexer.htmlviewer;

import gui.BatchState;
import gui.BatchStateListener;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JEditorPane;

public class HtmlViewerPane extends JEditorPane implements BatchStateListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8409933088483723672L;
	private BatchState batchState;

	public HtmlViewerPane(BatchState batchState){

		super();
		this.batchState = batchState;
		setOpaque(true);
		setBackground(Color.white);
		setPreferredSize(new Dimension(400, 400));
		setEditable(false);
		setContentType("text/html");

	}

	public void loadPage(String url) {
	
		try {
			setPage(url);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}

	public void blank(){
		setText("");
	}

	@Override
	public void selectedCellChanged(int row, int col) {
		String helpUrl = batchState.getFields().get(col).getHelphtml();
		loadPage(helpUrl);
	}

	@Override
	public void valueChanged(int row, int col, String value) {
		//who cares../

	}

}
