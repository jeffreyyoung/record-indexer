package gui.drawing;

import gui.BatchState;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class Drawing {
	
	private DrawingPanel imagePanel;
	private DrawingPanel navigator;
	private BatchState batchState;
	
	public void addImagePanelsToHolderPanels(JPanel imageHolder, JPanel navigatorPanel, BatchState batchState){
		this.batchState = batchState;
		imagePanel = new DrawingPanel(false, batchState);
		navigator = new DrawingPanel(true, batchState);
		
		imagePanel.addDrawingListener(drawingListener1);
		navigator.addDrawingListener(drawingListener2);
						
		imagePanel.setVisible(true);
		navigator.setVisible(true);
		
		
		imageHolder.add(imagePanel);
		navigatorPanel.add(navigator);
		
		if (batchState.getSaveData() != null){
			
			imagePanel.getComponent().setScale(batchState.getSaveData().getScale());
			if (batchState.getSaveData().getIsInverted()){
				imagePanel.getComponent().invert();
			}
			if (!batchState.getSaveData().getShowHighlights()){
				imagePanel.getComponent().toggleHighlights();
			}
			
		}
		
	}
	
	private DrawingListener drawingListener1 = new DrawingListener() {

		@Override
		public void originChanged(int w_newOriginX, int w_newOriginY) {
			navigator.setOrigin(w_newOriginX, w_newOriginY);	
		}			
	};
	
	private DrawingListener drawingListener2 = new DrawingListener() {

		@Override
		public void originChanged(int w_newOriginX, int w_newOriginY) {
			imagePanel.setOrigin(w_newOriginX, w_newOriginY);	
		}			
	};

	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setMinimumSize(new Dimension(900, 900));
		
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		
		Drawing drawing = new Drawing();
		drawing.addImagePanelsToHolderPanels(panel1, panel2, null);//edit this
		
		JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel1, panel2);
		verticalSplitPane.setDividerLocation(400/2);
		frame.add(verticalSplitPane);
		
	}
	
}
