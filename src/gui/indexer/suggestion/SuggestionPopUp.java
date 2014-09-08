package gui.indexer.suggestion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gui.BatchState;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class SuggestionPopUp extends JPopupMenu {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JMenuItem seeSuggestions;
    public SuggestionPopUp(final int row, final int col, final BatchState batchState){
    	seeSuggestions = new JMenuItem("See Suggestions");
        add(seeSuggestions);
        
        seeSuggestions.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(batchState.getValueSuggestions().get(row).get(col - 1).toString());
				new SuggestionFrame(row, col, batchState);
				
			}
        	
        });
        

    }
    

    
}
