package gui.miscframes;

import gui.BatchState;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.ClientException;
import client.communication.ClientCommunicator;
import shared.communication.DownloadBatch_Params;
import shared.communication.DownloadBatch_Result;
import shared.communication.GetProjects_Params;
import shared.communication.GetProjects_Result;
import shared.model.Project;

public class DownloadBatchFrame extends JFrame{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -4157604365678078471L;
	public static int DEFAULT_WIDTH = 450;
	public static int DEFAULT_HEIGHT = 200;
	private BatchState batchState;
	private JComboBox<String> dropdown;
	private JButton viewSampleButton;
	private JButton cancelButton;
	private JButton downloadButton;
	private List<Project> projects;
	
	@SuppressWarnings("deprecation")
	public DownloadBatchFrame(BatchState batchState){
		super();
		
		this.batchState = batchState;
		
		batchState.getIndexerFrame().disable();
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("Project:  "), c);
		
		c.gridx = 1;
		c.gridy = 0;
		
		populateDropdown();
		panel.add(dropdown, c);
		
		c.gridx = 2;
		c.gridy = 0;
		viewSampleButton = new JButton("View Sample");
		panel.add(viewSampleButton, c); 
		
		c.gridx = 0;
		c.gridy = 1;
		cancelButton = new JButton("Cancel");
		panel.add(cancelButton, c);

		
		c.gridx = 1;
		c.gridy = 1;
		downloadButton = new JButton("Download");
		panel.add(downloadButton, c);
		
		add(panel);
		
		viewSampleButton.addActionListener(viewSampleListener);
		cancelButton.addActionListener(cancelButtonListener);
		downloadButton.addActionListener(downloadButtonListener);
		
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setAlwaysOnTop (true);
		addWindowListener(windowListener);
	}
	
	private WindowAdapter windowListener = new WindowAdapter()
	{
	    @SuppressWarnings("deprecation")
		public void windowClosing(WindowEvent e)
	    {
	        batchState.getIndexerFrame().enable();
	    }
	};
	
	private void populateDropdown(){
		this.dropdown = new JComboBox<String>();
		
		GetProjects_Params params = new GetProjects_Params();
		params.setUsername(batchState.getUser().getUsername());
		params.setPassword(batchState.getUser().getPassword());
		
		GetProjects_Result result;
		try {
			result = ClientCommunicator.getSingleton().getProjects(params);
			this.projects = result.getProjects();
			
			for (Project p : projects){
				dropdown.addItem(p.getTitle());
			}
			
		} catch (ClientException e) {
			e.printStackTrace();
		}	
	}
	
	private ActionListener viewSampleListener = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			
			for (Project p : projects){
				if (p.getTitle().equals(dropdown.getSelectedItem())){
					
					SampleImageFrame siFrame;
					try {
						siFrame = new SampleImageFrame(p, batchState);
						siFrame.setVisible(true);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					
					
				}
			}
		}
	};
	
	private ActionListener cancelButtonListener = new ActionListener(){
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e) {
			batchState.getIndexerFrame().enable();
			setVisible(false);
			dispose();
			
		}
	};
	
	private ActionListener downloadButtonListener = new ActionListener(){
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e) {
			batchState.getIndexerFrame().enable();
			DownloadBatch_Params params = new DownloadBatch_Params();
			params.setUsername(batchState.getUser().getUsername());
			params.setPassword(batchState.getUser().getPassword());
			
			for (Project p : projects){
				if (p.getTitle().equals(dropdown.getSelectedItem())){
					params.setProjectId(p.getId());
				}
			}
			
			
			try {
				DownloadBatch_Result result = ClientCommunicator.getSingleton().downloadBatch(params);
				result.getBatch().setImageURL("http://" + ClientCommunicator.SERVER_HOST + ":" + ClientCommunicator.SERVER_PORT + "/" + result.getBatch().getImageURL());
				result.prependToFieldsHelpUrl("http://" + ClientCommunicator.SERVER_HOST + ":" + ClientCommunicator.SERVER_PORT + "/");
				
				batchState.setCurrentBatch(result.getBatch());
				batchState.getUser().setCurBatchId(batchState.getCurrentBatch().getId());
				batchState.setCurrentProject(result.getProject());
				batchState.setFields(result.getFields());
				batchState.initPanles();
				setVisible(false);
				dispose();
				
			} catch (ClientException e1) {
				e1.printStackTrace();
			} 
			
		}
	};
}
