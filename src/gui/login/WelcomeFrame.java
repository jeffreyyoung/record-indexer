package gui.login;

import gui.BatchState;
import gui.loadsave.BatchStateLoadSave;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WelcomeFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1294933635457506561L;
	private int DEFAULT_WIDTH = 370;
	private int DEFAULT_HEIGHT = 120;
	private BatchState batchState;
	private JButton okButton;

	@SuppressWarnings("deprecation")
	WelcomeFrame(BatchState batchState){
		super();

		this.batchState = batchState;
		batchState.getLoginFrame().disable();
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("Welcome, " + batchState.getUser().getFirstname() + " " + batchState.getUser().getLastname()), c);
		c.gridx = 0;
		c.gridy = 1;
		panel.add(new JLabel("You have indexed " + batchState.getUser().getNumIndexedRecords() + " records."), c);
		c.gridx = 1;
		c.gridy = 2;
		okButton = new JButton("ok");
		panel.add(okButton, c);
		add(panel);

		okButton.addActionListener(okListener);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setAlwaysOnTop (true);
	}

	private ActionListener okListener = new ActionListener(){
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e) {
			batchState.getLoginFrame().setVisible(false);

			if (batchState.getUser().getCurBatchId() != -1){
				batchState.blankPanels();
				BatchStateLoadSave.loadBatchState(batchState);	
			}
			batchState.getLoginFrame().enable();
			setVisible(false);
			batchState.getIndexerFrame().setVisible(true);
		}
	};
}
