
package gui.drawing;
import gui.BatchState;
import gui.BatchStateListener;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.event.*;

import javax.imageio.*;
import javax.swing.*;

import shared.model.Field;

import java.util.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;


@SuppressWarnings("serial")
public class DrawingComponent extends JComponent implements Serializable, BatchStateListener {
	
	private int w_originX;
	private int w_originY;
	
	
	private boolean showHighLights;
	
	private Image record;
	
	public static Color highlightColor = new Color(210, 180, 140, 192);
	private Color transparentColor = new Color(210, 180, 140, 0);
	
	private BufferedImage invertedImage;
	private Image notInvertedImage;
	private Boolean isInverted;
	private DrawingShape batchImage;
	private DrawingShape batchHighlight;
	
	private double scale;
	
	private boolean dragging;
	private int w_dragStartX;
	private int w_dragStartY;
	private int w_dragStartOriginX;
	private int w_dragStartOriginY;

	private boolean isNavigator;
	
	private Color background = new Color(200, 222, 222);
	private BatchState batchState;
	private ArrayList<DrawingListener> listeners;
	
	public DrawingComponent(BatchState batchState, boolean isNavigator) {
		w_originX = 0;
		w_originY = 0;
		scale = .5;
		
		this.isNavigator = isNavigator;
		this.showHighLights = true;
		
		initDrag();
		
		this.batchState = batchState;
		
		listeners = new ArrayList<DrawingListener>();
		
		this.setBackground(background);
		this.setPreferredSize(new Dimension(999, 450));


		
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.addComponentListener(componentAdapter);
		
		
		loadImage(batchState.getCurrentBatch().getImageURL());
		this.batchHighlight = (new DrawingRect(new Rectangle2D.Double(20, 90, 150, 200), transparentColor));
	}
	
	public void toggleHighlights(){
		if (showHighLights){
			this.showHighLights = false;
			batchHighlight.setColor(transparentColor);
			//make shape1 transparent
		}
		else {
			this.showHighLights = true;
			batchHighlight.setColor(highlightColor);
			//make shape1 not transparent
		}
		repaint();
	}
	
	public void invert() {
		
		//BufferedImage bimage = new RescaleOp(-1.0f,255.0f,null).filter((BufferedImage)record,null);
		if (isInverted){
			this.batchImage = new DrawingImage(notInvertedImage, new Rectangle2D.Double(0, 0, record.getWidth(null), record.getHeight(null)));
			this.isInverted = false;
		}
		else {
			this.batchImage = new DrawingImage(invertedImage, new Rectangle2D.Double(0, 0, record.getWidth(null), record.getHeight(null)));
			this.isInverted = true;
		}
		repaint();
		
		
	}
	
	private void initDrag() {
		dragging = false;
		w_dragStartX = 0;
		w_dragStartY = 0;
		w_dragStartOriginX = 0;
		w_dragStartOriginY = 0;
	}
		
	public void loadImage(String imageFile) {
		try {
			
			this.record = ImageIO.read(new URL(imageFile));
			this.notInvertedImage = ImageIO.read(new URL(imageFile));
			
			this.invertedImage = new RescaleOp(-1.0f,255.0f,null).filter((BufferedImage)record,null);
			
			this.isInverted = false;
			this.batchImage = new DrawingImage(record, new Rectangle2D.Double(0, 0, record.getWidth(null), record.getHeight(null)));
		}
		catch (IOException e) {
			try {
				Image record = ImageIO.read(new URL("http://www.humanrightslogo.net/sites/default/files/styles/gallery-thumbnail/public/images/submissions/19795%2520-%2520transparent.png?itok=eUKpgyER"));
				this.batchImage = new DrawingImage(record, new Rectangle2D.Double(0, 0, record.getWidth(null), record.getHeight(null)));
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}


		}
	}
	
	
	public void setScale(double newScale) {
		scale = newScale;
		this.repaint();
	}
	
	public void zoomIn(){
		scale = scale + .05;
		this.repaint();
	}
	
	public void zoomOut(){
		scale = scale - .05;
		
		if (scale <= 0) scale = 0;
		
		this.repaint();
	}
	
	public int getW_x(){
		return w_originX;
	}
	
	public int getW_y(){
		return w_originY;
	}
	
	public void setOrigin(int w_newOriginX, int w_newOriginY) {//modify this
		w_originX = w_newOriginX;
		w_originY = w_newOriginY;
		this.repaint();
	}
	
	public void addDrawingListener(DrawingListener listener) {
		listeners.add(listener);
	}
	
	private void notifyOriginChanged(int w_newOriginX, int w_newOriginY) {
		for (DrawingListener listener : listeners) {
			listener.originChanged(w_newOriginX, w_newOriginY);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
		drawBackground(g2);

		g2.translate(getWidth()/2.0, getHeight()/2.0);
		g2.scale(scale, scale);
		g2.translate(-w_originX, -w_originY);

		drawShapes(g2);
	}
	
	private void drawBackground(Graphics2D g2) {
		g2.setColor(getBackground());
		g2.fillRect(0,  0, getWidth(), getHeight());
	}

	private void drawShapes(Graphics2D g2) {
		batchImage.draw(g2);
		batchHighlight.draw(g2);

	}
	
	private MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {
			int d_X = e.getX();
			int d_Y = e.getY();
			
			AffineTransform transform = new AffineTransform();
			
			transform.translate(getWidth()/2.0, getHeight()/2.0);
			transform.scale(scale, scale);
			transform.translate(-w_originX, -w_originY);
			
			Point2D d_Pt = new Point2D.Double(d_X, d_Y);
			Point2D w_Pt = new Point2D.Double();
			try
			{
				transform.inverseTransform(d_Pt, w_Pt);
			}
			catch (NoninvertibleTransformException ex) {
				return;
			}
			int w_X = (int)w_Pt.getX();
			int w_Y = (int)w_Pt.getY();
			
			boolean hitShape = false;
			
			Graphics2D g2 = (Graphics2D)getGraphics();

			if (batchImage.contains(g2, w_X, w_Y)) {
				hitShape = true;

				double rowHeight = batchState.getCurrentProject().getRecordHeight();
				double numRows = batchState.getCurrentProject().getRecordsPerImage();
				double firstYCoord = batchState.getCurrentProject().getFirstYCoord();	
				
				if (w_Y > firstYCoord && w_Y < firstYCoord * numRows && !isNavigator){
					System.out.println("x " + w_X + " - y " + w_Y);
					Integer row = null;
					Integer col = null;
					
					for (int i = 0; i < numRows; i++){
						if(w_Y > (firstYCoord+rowHeight* i) && w_Y < (firstYCoord + rowHeight * (i + 1) ) ){
							//System.out.println("clicked row: " + i);
							row = i;
						}
					}
					
					for (int i = 0; i < batchState.getFields().size(); i++){
						Field f = batchState.getFields().get(i);
						
						if(w_X > f.getX_coord() && w_X < (f.getX_coord() + f.getWidth()) ){
							col = i;
						}
						
					}
					
					if (col != null && row != null){
						System.out.println("row: " + row + ", col: " + col);
						batchState.setSelectedRowAndCol(row, col);
					}
					
				}
				
				
			}

			
			if (hitShape) {
				dragging = true;		
				w_dragStartX = w_X;
				w_dragStartY = w_Y;		
				w_dragStartOriginX = w_originX;
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {		
			if (dragging) {
				int d_X = e.getX();
				int d_Y = e.getY();
				
				AffineTransform transform = new AffineTransform();
				
				transform.translate(getWidth()/2, getHeight()/2);
				transform.scale(scale, scale);
				transform.translate(-w_dragStartOriginX, -w_dragStartOriginY);
				
				Point2D d_Pt = new Point2D.Double(d_X, d_Y);
				Point2D w_Pt = new Point2D.Double();
				try
				{
					transform.inverseTransform(d_Pt, w_Pt);
				}
				catch (NoninvertibleTransformException ex) {
					return;
				}
				int w_X = (int)w_Pt.getX();
				int w_Y = (int)w_Pt.getY();
				
				int w_deltaX = w_X - w_dragStartX;
				int w_deltaY = w_Y - w_dragStartY;
				
				w_originX = w_dragStartOriginX - w_deltaX;
				w_originY = w_dragStartOriginY - w_deltaY;
				
				notifyOriginChanged(w_originX, w_originY);
				
				repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			initDrag();
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			return;
		}	
	};
	
	private ComponentAdapter componentAdapter = new ComponentAdapter() {

		@Override
		public void componentHidden(ComponentEvent e) {
			return;
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			return;
		}

		@Override
		public void componentResized(ComponentEvent e) {
			return;
		}

		@Override
		public void componentShown(ComponentEvent e) {
			return;
		}	
	};

	
	/////////////////
	// Drawing Shape
	/////////////////
	
	
	interface DrawingShape {
		boolean contains(Graphics2D g2, double x, double y);
		void draw(Graphics2D g2);
		Rectangle2D getBounds(Graphics2D g2);
		void setColor(Color color);
	}

	class DrawingImage implements DrawingShape {

		private Image image;
		private Rectangle2D rect;
		
		public DrawingImage(Image image, Rectangle2D rect) {
			this.image = image;
			this.rect = rect;
		}

		@Override
		public boolean contains(Graphics2D g2, double x, double y) {
			return rect.contains(x, y);
		}

		@Override
		public void draw(Graphics2D g2) {
			g2.drawImage(image, (int)rect.getMinX(), (int)rect.getMinY(), (int)rect.getMaxX(), (int)rect.getMaxY(),
							0, 0, image.getWidth(null), image.getHeight(null), null);
		}	
		
		@Override
		public Rectangle2D getBounds(Graphics2D g2) {
			return rect.getBounds2D();
		}

		@Override
		public void setColor(Color color) {
			
		}
	}

	@Override
	public void selectedCellChanged(int row, int col) {

		int height = batchState.getCurrentProject().getRecordHeight();
		int width = batchState.getFields().get(col).getWidth();
		int x = batchState.getFields().get(col).getX_coord();
		int y = batchState.getCurrentProject().getFirstYCoord() + row * height;
		
		Color currentColor;
		
		if (showHighLights){
			currentColor = highlightColor;
		}
		else {
			currentColor = transparentColor;
		}
		
		this.batchHighlight = (new DrawingRect(new Rectangle2D.Double(x, y, width, height), currentColor));	
		repaint();
	}

	@Override
	public void valueChanged(int row, int col, String value) {
		//no shiz given
		
	}
	
	class DrawingRect implements DrawingShape {

		private Rectangle2D rect;
		private Color color;
		
		public DrawingRect(Rectangle2D rect, Color color) {
			this.rect = rect;
			this.color = color;
		}
		
		public void setColor(Color color){
			this.color = color;
		}

		@Override
		public boolean contains(Graphics2D g2, double x, double y) {
			return rect.contains(x, y);
		}

		@Override
		public void draw(Graphics2D g2) {
			g2.setColor(color);
			g2.fill(rect);
		}
		
		@Override
		public Rectangle2D getBounds(Graphics2D g2) {
			return rect.getBounds2D();
		}
	}

	public Boolean getIsInverted() {
		return isInverted;
	}

	public Boolean getShowHighlights() {
		return showHighLights;
	}

	public double getScale() {
		return scale;
	}
	
}




