package gui.indexer;
import gui.BatchState;
import gui.drawing.Drawing;
import gui.drawing.DrawingComponent;
import gui.loadsave.BatchStateLoadSave;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;

import javax.swing.*;


public class IndexerFrame extends JFrame implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3305568056916961559L;
	public static final int DEFAULT_WIDTH = 900;
	public static final int DEFAULT_HEIGHT = 600;
	
	private ButtonPanel buttonPanel;
	
	public ButtonPanel getButtonPanel() {
		return buttonPanel;
	}

	public void setButtonPanel(ButtonPanel buttonPanel) {
		this.buttonPanel = buttonPanel;
	}

	private LeftEntryPane bottomLeftPane;
	private RightEntryPane bottomRightPane;
	
	public RightEntryPane getBottomRightPane() {
		return bottomRightPane;
	}

	private BatchState batchState;
	private JPanel imagePanel;
	private DrawingComponent imageComponent;
	private IndexerMenuBar menuBar;
	private JSplitPane horizontalSplitPane;
	private JSplitPane verticalSplitPane;
	
	public IndexerMenuBar getIndexerMenuBar() {
		return menuBar;
	}

	public DrawingComponent getImageComponent() {
		return imageComponent;
	}

	public void setImageComponent(DrawingComponent imageComponent) {
		this.imageComponent = imageComponent;
	}

	public JPanel getImagePanel() {
		return imagePanel;
	}

	public IndexerFrame(BatchState batchState){
		this.batchState = batchState;
		batchState.setIndexerFrame(this);
		
		setTitle("Record Indexer");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		menuBar = new IndexerMenuBar(batchState);
		setJMenuBar(menuBar);

		imagePanel = new JPanel();
		buttonPanel = new ButtonPanel(batchState);
		bottomLeftPane = new LeftEntryPane();
		bottomRightPane = new RightEntryPane();
		
		horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, bottomLeftPane, bottomRightPane);
		horizontalSplitPane.setDividerLocation(DEFAULT_WIDTH/2);
		
		verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, imagePanel, horizontalSplitPane);
		verticalSplitPane.setDividerLocation(DEFAULT_HEIGHT/2);
		
		horizontalSplitPane.setResizeWeight(.5);
		verticalSplitPane.setResizeWeight(.5);

		//Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(250, 150);
		bottomLeftPane.setMinimumSize(minimumSize);
		bottomRightPane.setMinimumSize(minimumSize);

		
		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BorderLayout());
		layoutPanel.add(buttonPanel, BorderLayout.NORTH);
		layoutPanel.add(verticalSplitPane, BorderLayout.CENTER);

		add(layoutPanel);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(500, 500));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addWindowListener(windowListener);
		
	}
	
	private WindowAdapter windowListener = new WindowAdapter()
	{
	    public void windowClosing(WindowEvent e)
	    {
	    	
	        BatchStateLoadSave.saveBatchState(batchState);
	    }
	};
	
	public void initDrawingPanels(){
		Drawing drawing = new Drawing();
		drawing.addImagePanelsToHolderPanels(imagePanel, bottomRightPane.getImageNavigation(), batchState);
	}
	
	public void initTableAndFormEntry(){
		
	}
	
	public LeftEntryPane getBottomLeftPane() {
		return bottomLeftPane;
	}

	public JSplitPane getHorizontalSplitPane() {
		return horizontalSplitPane;
	}

	public JSplitPane getVerticalSplitPane() {
		return verticalSplitPane;
	}	
	
	public void refresh() {
		
		int randomNum = 1 + (int)(Math.random()*2); 
		if (randomNum == 1)
			setSize(getWidth()+1, getHeight());
		else {
			setSize(getWidth()-1, getHeight());
		}
	}
	
	
}
