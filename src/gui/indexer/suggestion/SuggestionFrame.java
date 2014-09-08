package gui.indexer.suggestion;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import gui.BatchState;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class SuggestionFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3694174841058255854L;
	private int DEFAULT_WIDTH = 300;
	private int DEFAULT_HEIGHT = 200;
	BatchState batchState;
	JList<String> list;
	int row;
	int col;
	
	private JButton useSuggestionButton;
	private JButton cancelButton;
	
	@SuppressWarnings("deprecation")
	public SuggestionFrame(int row, int col, BatchState batchState){

		super();
		this.row = row;
		this.col = col;
		this.batchState = batchState;

		JPanel panel = new JPanel();
		
		panel.setLayout(new BorderLayout());
		

		List<String> suggestions = batchState.getValueSuggestions().get(row).get(col - 1);
		String[] sugg = suggestions.toArray(new String[suggestions.size()]);

		this.list = new JList<String>(sugg);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFixedCellWidth(150);
		list.setFixedCellHeight(20);
		
		this.useSuggestionButton = new JButton("Use Suggestion");
		useSuggestionButton.setEnabled(false);
		this.cancelButton = new JButton("Cancel");
		useSuggestionButton.addActionListener(useSuggestionListener);
		list.addMouseListener(mouseListListener);
		cancelButton.addActionListener(cancelListener);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(useSuggestionButton);
		buttonPanel.add(cancelButton);
		
		
		JScrollPane listScrollPane = new JScrollPane();
		listScrollPane.setViewportView(list);
		
		panel.add(listScrollPane, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		
		add(panel);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		setAlwaysOnTop (true);
		
		batchState.getIndexerFrame().disable();

	}

	
	private MouseListener mouseListListener = new MouseAdapter() {
		public void mouseClicked(MouseEvent mouseEvent) {

			if (mouseEvent.getClickCount() > 0) {
				int index = list.locationToIndex(mouseEvent.getPoint());
				if (index >= 0) {
					useSuggestionButton.setEnabled(true);
				}
			}

		}
	};
	
	private ActionListener cancelListener = new ActionListener(){

		@SuppressWarnings("deprecation")
		@Override
		public void actionPerformed(ActionEvent e) {
			batchState.getIndexerFrame().enable();
			
			dispose();
			
		}
		
	};
	
	private ActionListener useSuggestionListener = new ActionListener() {

		@SuppressWarnings("deprecation")
		@Override
		public void actionPerformed(ActionEvent e) {
			batchState.getIndexerFrame().enable();
			String value = (String)list.getSelectedValue();
			batchState.getValues().get(row).set(col, value);
			batchState.getValueSuggestions().get(row).set(col - 1, null);
			
			batchState.getIndexerFrame().getBottomLeftPane().getIndexerFormPanel().updateFieldValues();
			
			batchState.getIndexerFrame().refresh();
			dispose();
		}
	
	};
	
}




