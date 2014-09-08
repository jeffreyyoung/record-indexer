package gui.login;

import gui.BatchState;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InvalidCredFrame extends JFrame{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 65998190522534937L;
	public static int DEFAULT_WIDTH = 370;
	public static int DEFAULT_HEIGHT = 120;
	private BatchState batchState;
	private JButton okButton;
	
	@SuppressWarnings("deprecation")
	InvalidCredFrame(BatchState batchState){
		super();
		this.batchState = batchState;
		batchState.getLoginFrame().disable();
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("Invalid username and/or password"), c);
		c.gridx = 0;
		c.gridy = 1;
		okButton = new JButton("ok");
		panel.add(okButton, c);
		add(panel);
		
		okButton.addActionListener(okListener);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	private ActionListener okListener = new ActionListener(){
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e) {
			batchState.getLoginFrame().enable();
			setVisible(false);
		}
	};
}
