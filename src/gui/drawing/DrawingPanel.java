package gui.drawing;

import gui.BatchState;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class DrawingPanel extends JPanel {

	private DrawingComponent component;
	
	public DrawingComponent getComponent() {
		return component;
	}

	public void setComponent(DrawingComponent component) {
		this.component = component;
	}


	private Boolean isNavigator;
	private BatchState batchState;
	
	
	public DrawingPanel(Boolean isNavigator, BatchState batchState) {
		
		this.isNavigator = isNavigator;
		this.batchState = batchState;

		component = new DrawingComponent(batchState, isNavigator);
		
		if (!isNavigator){
			batchState.getIndexerFrame().setImageComponent(component);
		}
		
		this.add(component, BorderLayout.CENTER);
		if (!isNavigator){
			component.setScale(.5);
			this.addMouseWheelListener(mouseWheelListener);
		}
		else {
			component.setScale(.20);
		}

	}

	public void setOrigin(int w_newOriginX, int w_newOriginY) {
		component.setOrigin(w_newOriginX, w_newOriginY);
	}

	public void addDrawingListener(DrawingListener listener) {
		component.addDrawingListener(listener);
	}


	private MouseWheelListener mouseWheelListener = new MouseWheelListener() {

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {

			int notches = e.getWheelRotation();
			System.out.println(notches);
			
			if (notches > 0){
				component.zoomIn();
			}
			else if (notches < 0) {
				component.zoomOut();
			}
		}

	};
	
}
