package gui.login;
import gui.BatchState;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.*;

import client.ClientException;
import client.communication.ClientCommunicator;
import shared.communication.*;


public class LoginFrame extends JFrame implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3795493077167429245L;
	public static int DEFAULT_WIDTH = 470;
	public static int DEFAULT_HEIGHT = 120;
	
	private JTextField usernameField = new JTextField(30);
	private JTextField passwordField = new JTextField(30);
	private JButton loginButton = new JButton("Login");
	private JButton exitButton = new JButton("Exit");
	private BatchState batchState;
	
	public LoginFrame(BatchState batchState) {
		super();
		
		this.batchState = batchState;
		batchState.setLoginFrame(this);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("Username:  "), c);
		c.gridx = 1;
		c.gridy = 0;
		panel.add(usernameField, c);
		c.gridx = 0;
		c.gridy = 1;
		panel.add(new JLabel("Password:  "), c);
		c.gridx = 1;
		c.gridy = 1;
		panel.add(passwordField, c);
		c.gridx = 0;
		c.gridy = 2;
		panel.add(loginButton, c);
		c.gridx = 1;
		c.gridy = 2;
		panel.add(exitButton, c);
		
		add(panel);
		
		loginButton.addActionListener(loginListener);
		exitButton.addActionListener(exitListener);
		
		
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
	}
	
	private ActionListener exitListener = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			System.exit(0);
		}
	};
	
	private ActionListener loginListener = new ActionListener(){

		public void actionPerformed(ActionEvent e) {
			System.out.println(usernameField.getText());
			System.out.println(passwordField.getText());
			
			ValidateUser_Params params = new ValidateUser_Params();
			params.setPassword(passwordField.getText());
			params.setUsername(usernameField.getText());
			
			try {
				ValidateUser_Result result = ClientCommunicator.getSingleton().validateUser(params);
				System.out.println(result.toString());
				
				if (result != null && !result.getMessage().equals("FALSE\n")){
					
					batchState.setUser(result.getUser());
					WelcomeFrame welcomeFrame = new WelcomeFrame(batchState);
					welcomeFrame.setVisible(true);
					
				}
				else {
					InvalidCredFrame invalidCredFrame = new InvalidCredFrame(batchState);
					invalidCredFrame.setVisible(true);
				}
				
				
			} catch (ClientException e1) {
				e1.printStackTrace();
			}
			
			
		}
		
	};
}
