package gui.loadsave;

import gui.BatchState;

import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;

public class BatchSaveData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double scale;
	private Boolean isInverted;
	private Boolean showHighlights;
	
	private Point indexerLoc;
	private int verticalDivideLoc;
	private int horizontalDivideLoc;
	private Dimension size; // = frame.getBounds().getSize()
	
	private int w_x;
	private int w_y;
	
	/**
	 * The BatchStateSaveData class recieves a BatchState in the constructor and extracts save data 
	 * from the current Batch State such as user settings
	 * @param bs
	 */
	public BatchSaveData(BatchState bs){
		this.scale = bs.getIndexerFrame().getImageComponent().getScale();
		this.isInverted = bs.getIndexerFrame().getImageComponent().getIsInverted();
		this.showHighlights = bs.getIndexerFrame().getImageComponent().getShowHighlights();
		
		this.indexerLoc = bs.getIndexerFrame().getLocation();
		this.verticalDivideLoc = bs.getIndexerFrame().getVerticalSplitPane().getDividerLocation();
		this.horizontalDivideLoc = bs.getIndexerFrame().getHorizontalSplitPane().getDividerLocation();
		this.size = bs.getIndexerFrame().getSize();
		
		this.w_x = bs.getIndexerFrame().getImageComponent().getW_x();
		this.w_y = bs.getIndexerFrame().getImageComponent().getW_y();
		
	}
	
	public int getW_x(){
		return w_x;
	}
	
	public int getW_y(){
		return w_y;
	}
	
	public Double getScale() {
		return scale;
	}
	public void setScale(Double scale) {
		this.scale = scale;
	}
	public Boolean getIsInverted() {
		return isInverted;
	}
	public void setIsInverted(Boolean isInverted) {
		this.isInverted = isInverted;
	}
	public Boolean getShowHighlights() {
		return showHighlights;
	}
	public void setShowHighlights(Boolean showHighlights) {
		this.showHighlights = showHighlights;
	}
	public Point getIndexerLoc() {
		return indexerLoc;
	}
	public void setIndexerLoc(Point indexerLoc) {
		this.indexerLoc = indexerLoc;
	}
	public int getVerticalDivideLoc() {
		return verticalDivideLoc;
	}
	public void setVerticalDivideLoc(int verticalDivideLoc) {
		this.verticalDivideLoc = verticalDivideLoc;
	}
	public int getHorizontalDivideLoc() {
		return horizontalDivideLoc;
	}
	public void setHorizontalDivideLoc(int horizontalDivideLoc) {
		this.horizontalDivideLoc = horizontalDivideLoc;
	}
	public Dimension getSize() {
		return size;
	}
	public void setSize(Dimension size) {
		this.size = size;
	}
	
}


