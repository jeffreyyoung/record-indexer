package gui.miscframes;

import gui.BatchState;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.ClientException;
import client.communication.ClientCommunicator;
import shared.communication.GetSampleImage_Params;
import shared.communication.GetSampleImage_Result;
import shared.model.Project;

public class SampleImageFrame extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4709331585732449773L;
	public static int DEFAULT_WIDTH = 500;
	public static int DEFAULT_HEIGHT = 500;
	
	private Project project;
	private JButton closeButton;
	private BatchState batchState;
	
	SampleImageFrame(Project project, BatchState batchState) throws IOException{
		
		super();
		this.project = project;
		this.batchState = batchState;
		
		setTitle("Sample image from " + project.getTitle());
		
		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BorderLayout());
		
		String path = ClientCommunicator.URL_PREFIX + "/" + getImagePath();
		
		System.out.println(path);
		Image sampleImage = ImageIO.read(new URL(path));	
		JLabel imageLabel = new JLabel(new ImageIcon(sampleImage.getScaledInstance(500, 500, Image.SCALE_SMOOTH)));
		layoutPanel.add(imageLabel, BorderLayout.CENTER);
		
		closeButton = new JButton("Close");
		layoutPanel.add(closeButton, BorderLayout.SOUTH);
		
		add(layoutPanel);
		closeButton.addActionListener(closeListener);
		
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	private ActionListener closeListener = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			dispose();
		}
	};
	
	private String getImagePath(){
		
		GetSampleImage_Params params = new GetSampleImage_Params();
		params.setUsername(batchState.getUser().getUsername());
		params.setPassword(batchState.getUser().getPassword());
		params.setProjectId(project.getId());
		
		try {
			GetSampleImage_Result result = ClientCommunicator.getSingleton().getSampleImage(params);
			return result.getImageUrl();
		} catch (ClientException e) {
			e.printStackTrace();
			return null;
		}
		

	}

}
