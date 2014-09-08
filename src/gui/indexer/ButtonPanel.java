package gui.indexer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import gui.BatchState;
import gui.loadsave.BatchStateLoadSave;

import javax.swing.JButton;
import javax.swing.JPanel;

import client.ClientException;
import client.communication.ClientCommunicator;
import shared.communication.SubmitBatch_Params;
import shared.communication.SubmitBatch_Result;

@SuppressWarnings("serial")
public class ButtonPanel extends JPanel implements Serializable {

	private JButton zoomInButton;
	private JButton zoomOutButton;
	private JButton invertImageButton;
	private JButton toggleHighlightsButton;
	private JButton saveButton;
	private JButton submitButton;
	
	private BatchState batchState;
	
	
	ButtonPanel(BatchState batchState){
		super();
		this.batchState = batchState;
		zoomInButton = new JButton("Zoom In");
		zoomOutButton = new JButton("Zoom Out");
		invertImageButton = new JButton("Invert Image");
		toggleHighlightsButton = new JButton("Toggle Highlights");
		saveButton = new JButton("Save");
		submitButton = new JButton("Submit");

		add(zoomInButton);
		add(zoomOutButton);
		add(invertImageButton);
		add(toggleHighlightsButton);
		add(saveButton);
		add(submitButton);
		
		disableButtons();
		
		submitButton.addActionListener(submitButtonListener);
		zoomInButton.addActionListener(zoomInListener);
		zoomOutButton.addActionListener(zoomOutListener);
		invertImageButton.addActionListener(invertListener);
		toggleHighlightsButton.addActionListener(toggleHighlightsListener);
		saveButton.addActionListener(saveListener);
	}
	
	public void disableButtons(){
		zoomInButton.setEnabled(false);
		zoomOutButton.setEnabled(false);
		invertImageButton.setEnabled(false);
		toggleHighlightsButton.setEnabled(false);
		saveButton.setEnabled(false);
		submitButton.setEnabled(false);
	}
	
	public void enableButtons(){
		zoomInButton.setEnabled(true);
		zoomOutButton.setEnabled(true);
		invertImageButton.setEnabled(true);
		toggleHighlightsButton.setEnabled(true);
		saveButton.setEnabled(true);
		submitButton.setEnabled(true);
	}
	
	public ActionListener saveListener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			BatchStateLoadSave.saveBatchState(batchState);
		}
		
	};
	
	private ActionListener toggleHighlightsListener = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			batchState.getIndexerFrame().getImageComponent().toggleHighlights();
		}
	};
	
	private ActionListener invertListener = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			batchState.getIndexerFrame().getImageComponent().invert();
		}
	};
	
	private ActionListener zoomInListener = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			batchState.getIndexerFrame().getImageComponent().zoomIn();
		}
	};
	
	private ActionListener zoomOutListener = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			batchState.getIndexerFrame().getImageComponent().zoomOut();
		}
	};
	
	private ActionListener submitButtonListener = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			SubmitBatch_Params params = new SubmitBatch_Params();
			params.setUsername(batchState.getUser().getUsername());
			params.setPassword(batchState.getUser().getPassword());
			params.setFieldValues(batchState.valuesToString());
			params.setBatchId(batchState.getCurrentBatch().getId());
			
			try {
				SubmitBatch_Result result = ClientCommunicator.getSingleton().submitBatch(params);
				batchState.getUser().setCurBatchId(-1);
				batchState.blankPanels();
			} catch (ClientException e1) {
				e1.printStackTrace();
			}
			
		}
	};
	

}
