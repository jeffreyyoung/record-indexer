package gui.indexer.form;

import gui.BatchState;
import gui.BatchStateListener;
import gui.indexer.suggestion.SuggestionPopUp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import shared.model.Field;

public class IndexerFormPanel extends JPanel implements Serializable, BatchStateListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<List <String> > table;
	List< JTextField> fields;
	List< JLabel> labels;
	BatchState batchState;
	JList<Integer> list;


	public IndexerFormPanel(BatchState batchState){
		super();
		this.batchState = batchState;
		this.fields = new ArrayList< JTextField>();
		this.labels = new ArrayList< JLabel>();
		this.table = batchState.getValues();

		for (int i = 0; i < batchState.getFields().size(); i++){

			Field s= batchState.getFields().get(i);

			JTextField textField = new JTextField();
			textField.addFocusListener(new FieldFocusListener(i));
			textField.getDocument().addDocumentListener(new DocListener());
			textField.addMouseListener(new RightClickAdapter(batchState, i));

			fields.add(textField);

			labels.add(new JLabel(s.getTitle()));
		}



		//	SINGLE_SELECTION
		DefaultListModel<Integer> listModel = new DefaultListModel<Integer>();

		for (Integer i = 0; i < batchState.getCurrentProject().getRecordsPerImage(); i++){
			//Integer f = i + 1;
			listModel.add(i, i);
		}



		this.list = new JList<Integer>(listModel);

		DefaultListCellRenderer renderer =  (DefaultListCellRenderer)list.getCellRenderer();  
		renderer.setHorizontalAlignment(JLabel.CENTER); 
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFixedCellWidth(150);
		list.setFixedCellHeight(20);
		setLayout(new BorderLayout());
		list.setSelectedIndex(0);

		JComponent panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		// Turn on automatically adding gaps between components
		layout.setAutoCreateGaps(true);
		// Create a sequential group for the horizontal axis.
		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
		GroupLayout.Group yLabelGroup = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);
		hGroup.addGroup(yLabelGroup);
		GroupLayout.Group yFieldGroup = layout.createParallelGroup();
		hGroup.addGroup(yFieldGroup);
		layout.setHorizontalGroup(hGroup);
		// Create a sequential group for the vertical axis.
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		layout.setVerticalGroup(vGroup);

		int p = GroupLayout.PREFERRED_SIZE;
		// add the components to the groups
		for (JLabel label : labels) {
			yLabelGroup.addComponent(label);
		}
		for (JTextField field : fields) {	
			yFieldGroup.addComponent(field, p, 100, 125);

		}
		for (int ii = 0; ii < labels.size(); ii++) {
			vGroup.addGroup(layout.createParallelGroup().
					addComponent(labels.get(ii)).
					addComponent(fields.get(ii), p, p, p));
		}

		list.addMouseListener(mouseListListener);

		
		JScrollPane listScrollPanel = new JScrollPane();
		listScrollPanel.setViewportView(list);

		JScrollPane fieldScrollPanel = new JScrollPane();
		fieldScrollPanel.setViewportView(panel);
		
		add(fieldScrollPanel, BorderLayout.CENTER);
		add(listScrollPanel, BorderLayout.WEST);
		
		
		
	}


	@Override
	public void selectedCellChanged(int row, int col) {
		list.setSelectedIndex(row);

		for(int i = 0; i < fields.size(); i++){
			String value = batchState.getValues().get(row).get(i + 1);
			fields.get(i).setText(value);
		}

		fields.get(col).requestFocus();
	}


	public void updateFieldValues(){
		int row = batchState.getSelectedRow();

		if (list.getSelectedIndex() == row){
			for (int i = 0; i < fields.size(); i++){

				fields.get(i).setText(batchState.getValues().get(row).get(i + 1));

				if (batchState.getValueSuggestions().get(row).get(i) == null){
					fields.get(i).setBackground(Color.white);
				}
				else {
					System.out.println(batchState.getValueSuggestions().get(row).get(i).toString());
					fields.get(i).setBackground(Color.red);
				}

			}
		}
	}

	@Override
	public void valueChanged(int row, int col, String value) {
		if (list.getSelectedIndex() == row){
			fields.get(col).setText(value);
		}

		if (batchState.getValueSuggestions().get(row).get(col) == null){
			System.out.println("change background to red");
			fields.get(col).setBackground(Color.red);
		}
		else {
			fields.get(col).setBackground(Color.white);
		}

	}

	private MouseListener mouseListListener = new MouseAdapter() {
		public void mouseClicked(MouseEvent mouseEvent) {

			if (mouseEvent.getClickCount() > 0) {
				int index = list.locationToIndex(mouseEvent.getPoint());
				if (index >= 0) {

					System.out.println("clicked on " + index);
					batchState.setSelectedRowAndCol(index, batchState.getSelectedColumn());
				}
			}

			if (SwingUtilities.isRightMouseButton(mouseEvent)) {

			}
		}
	};

	private class DocListener implements DocumentListener  
	{

		public void changedUpdate(DocumentEvent documentEvent) {
			printIt(documentEvent);
		}
		public void insertUpdate(DocumentEvent documentEvent) {
			printIt(documentEvent);
		}
		public void removeUpdate(DocumentEvent documentEvent) {
			printIt(documentEvent);
		}
		private void printIt(DocumentEvent documentEvent) {	  
			for (int i = 0; i < fields.size(); i++){
				if (documentEvent.getDocument() == fields.get(i).getDocument()){					
					String value = fields.get(i).getText();

					batchState.getValues().get(list.getSelectedIndex()).set(i + 1, value);
					batchState.suggestWords(value, list.getSelectedIndex(), i);

					if (batchState.getValueSuggestions().get(list.getSelectedIndex()).get(i) == null){
						fields.get(i).setBackground(Color.white);
					}
					else {
						fields.get(i).setBackground(Color.red);
					}
				}
			}
		}
	}

	private class FieldFocusListener implements FocusListener{

		private int col;
		public FieldFocusListener(int i){
			super();
			this.col = i;
		}

		@Override
		public void focusGained(FocusEvent e) {
			batchState.setSelectedRowAndCol(list.getSelectedIndex(), col);
		}

		@Override
		public void focusLost(FocusEvent e) {

		}
	}

	private class RightClickAdapter extends MouseAdapter{

		BatchState bs;
		int column;

		public RightClickAdapter(BatchState bs, int col){

			super();
			this.column = col;
			this.bs = bs;
		}

		public void mouseClicked(MouseEvent e){
			if (SwingUtilities.isRightMouseButton(e)) {

				int clickedRow = list.getSelectedIndex();
				int clickedColumn = column;

				System.out.println("selectedRow " + clickedRow + ", Selected Column " + clickedColumn);

				if (batchState.getValueSuggestions().get(clickedRow).get(clickedColumn) != null){//stuff
					JPopupMenu popup = new SuggestionPopUp(clickedRow, clickedColumn + 1, bs);
					popup.show(e.getComponent(), e.getX(), e.getY());
				}

			}
		}
	}
}
