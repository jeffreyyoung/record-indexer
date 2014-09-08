package gui;

import gui.indexer.IndexerFrame;
import gui.indexer.form.IndexerFormPanel;
import gui.indexer.htmlviewer.HtmlViewerPane;
import gui.indexer.table.IndexerTabelPanel;
import gui.loadsave.BatchSaveData;
import gui.login.LoginFrame;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import client.communication.ClientCommunicator;
import server.FacadeException;
import shared.model.*;
import spell.MySpellCorrector;

public class BatchState implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4661392049885940865L;
	private User user;
	private Batch currentBatch;
	private Project currentProject;
	private List<Field> fields;
	private List< List<String> > values;
	
	private List< List< List<String>>> valueSuggestions;
	private List< MySpellCorrector > spellCorrectors;

	private Integer selectedColumn = 0;
	private Integer selectedRow = 0;

	private LoginFrame loginFrame;
	private IndexerFrame indexerFrame;

	private List<BatchStateListener> bsListeners;


	BatchSaveData saveData;


	public BatchState(){

	}

	public void initPanles(){

		getIndexerFrame().getBottomLeftPane().addIndexerTablePanel(new IndexerTabelPanel(this));
		getIndexerFrame().getBottomLeftPane().addIndexerFormPanel(new IndexerFormPanel(this));
		indexerFrame.getButtonPanel().enableButtons();
		getIndexerFrame().initDrawingPanels();


		getIndexerFrame().getBottomRightPane().addHtmlPane(new HtmlViewerPane(this));

		if (user.getCurBatchId() == -1){
			getIndexerFrame().getIndexerMenuBar().getDownloadBatchItem().setEnabled(true);
		}
		else {
			getIndexerFrame().getIndexerMenuBar().getDownloadBatchItem().setEnabled(false);
		}

		if (saveData != null){

			getIndexerFrame().setSize(saveData.getSize());
			getIndexerFrame().getVerticalSplitPane().setDividerLocation(saveData.getVerticalDivideLoc());
			getIndexerFrame().getHorizontalSplitPane().setDividerLocation(saveData.getHorizontalDivideLoc());
			getIndexerFrame().setLocation(saveData.getIndexerLoc());

		}

		initSpellCheckers();
		initValueSuggestions();
		
		indexerFrame.refresh();

		addListenersToListenerList();

	}
	
	public void blankPanels(){
		indexerFrame.getBottomLeftPane().getTablePanel().removeAll();
		indexerFrame.getBottomLeftPane().getFormPanel().removeAll();
		indexerFrame.getImagePanel().removeAll();
		indexerFrame.getBottomRightPane().getImageNavigation().removeAll();
		indexerFrame.getBottomRightPane().getFieldHelpPanel().removeAll();

		this.currentBatch = null;
		this.fields = null;
		this.values = null;
		this.selectedColumn = 1;
		this.selectedRow = 1;
		this.spellCorrectors = null;
		this.valueSuggestions = null;

		if (user.getCurBatchId() == -1){
			getIndexerFrame().getIndexerMenuBar().getDownloadBatchItem().setEnabled(true);
		}
		else {
			getIndexerFrame().getIndexerMenuBar().getDownloadBatchItem().setEnabled(false);
		}

		indexerFrame.getButtonPanel().disableButtons();

		indexerFrame.refresh();
	}
	
	private void addListenersToListenerList(){
		this.bsListeners = new ArrayList<BatchStateListener>();

		bsListeners.add(getIndexerFrame().getBottomRightPane().getHtmlViewerPane());
		bsListeners.add(getIndexerFrame().getBottomLeftPane().getIndexerTablePanel());
		bsListeners.add(getIndexerFrame().getImageComponent());
		bsListeners.add(getIndexerFrame().getBottomLeftPane().getIndexerFormPanel());

	}

	private void initValues(){

		this.values = new ArrayList<List<String>>();

		for (Integer i = 0; i < currentProject.getRecordsPerImage(); i++){
			List<String> row = new ArrayList<String>();

			for (int v = 0; v < currentProject.getFieldsPerImage(); v++){
				if (v == 0){
					row.add(i.toString());
				}
				row.add("");

			}

			values.add(row);
		}
	}

	/**
	 * initValueSuggestions creates a 3 dimensional ArrayList, the first dimension represents the rows of the current batch, 
	 * the second dimension represents the columns
	 * and the third dimension represents the list of suggested values for the given cell
	 */
	private void initValueSuggestions(){

		this.valueSuggestions = new ArrayList<List<List<String>>>();

		for (Integer i = 0; i < currentProject.getRecordsPerImage(); i++){
			
			List<List<String>> row1 = new ArrayList<List<String>>();

			for (int v = 0; v < currentProject.getFieldsPerImage(); v++){
				row1.add(null);
			}

			valueSuggestions.add(row1);
		}
		
	}
	
	private void initSpellCheckers(){
		if (fields != null){
			this.spellCorrectors = new ArrayList<MySpellCorrector>();
			
			for (Field f: fields){
				
				if (f.getKnownData() != null && f.getKnownData().length() > 0){
					
					try {
						MySpellCorrector sp = new MySpellCorrector();
						System.out.println();
						sp.useDictionary(ClientCommunicator.URL_PREFIX + "/" + f.getKnownData());
						spellCorrectors.add(sp);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else {
					spellCorrectors.add(new MySpellCorrector());
				}
			}
		}
	}
	
	public void setSelectedRowAndCol(int row, int col) {

		//System.out.println("seting row: " + row + ", col: " + col);
		if (this.selectedRow != row || this.selectedColumn != col){
			this.selectedRow = row;
			this.selectedColumn = col;
			dispatchSelectedCellChangedEvent();
		}
	}

	private void dispatchSelectedCellChangedEvent(){

		for (int i = 0; i < bsListeners.size(); i++){
			bsListeners.get(i).selectedCellChanged(selectedRow, selectedColumn);
		}

	}

	public String valuesToString(){

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < values.size(); i++){
			for (int v = 0; v < values.get(0).size(); v++){

				if (v != 0) {

					if (i == 0 && v == 1){ //first value
						sb.append(values.get(i).get(v));
					}
					else {
						sb.append(",");
						sb.append(values.get(i).get(v));
					}

				}
			}
		}

		System.out.println(sb.toString());

		return sb.toString();
	}


	
	public List<List<String>> getValues() {
		if (values == null)
			initValues();

		return values;
	}

	public List<List <List <String>>> getValueSuggestions(){
		if (valueSuggestions == null) {
			initSpellCheckers();
			initValueSuggestions();
		}
		
		return valueSuggestions;
	}
	public void setValues(List<List<String>> values) {
		this.values = values;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LoginFrame getLoginFrame() {
		return loginFrame;
	}

	public void setLoginFrame(LoginFrame loginFrame) {
		this.loginFrame = loginFrame;
	}

	public IndexerFrame getIndexerFrame() {
		return indexerFrame;
	}

	public void setIndexerFrame(IndexerFrame indexerFrame) {
		this.indexerFrame = indexerFrame;
	}

	public Batch getCurrentBatch() {
		return currentBatch;
	}

	public void setCurrentBatch(Batch currentBatch) {
		this.currentBatch = currentBatch;
	}

	public Project getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(Project currentProject) {
		this.currentProject = currentProject;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public Integer getSelectedColumn() {
		return selectedColumn;
	}

	public Integer getSelectedRow() {
		return selectedRow;
	}

	public BatchSaveData getSaveData() {
		return saveData;
	}

	public void setSaveData(BatchSaveData saveData) {
		this.saveData = saveData;
	}
	
	public void suggestWords(String s, int row, int col){
		
		List<String> suggestedWords = getSuggestedWords(s, row, col);
		System.out.println("\nsuggested word table size... rows: " + getValueSuggestions().size() + ", columns: " + getValueSuggestions().get(0).size());
		System.out.println("suggesting words at row: " + row + ", col: " + col);
		
		getValueSuggestions().get(row).set(col, suggestedWords);

	}
	
	public List<MySpellCorrector> getSpellCorrectors(){
		return spellCorrectors;
	}
	
	private List<String> getSuggestedWords(String s, int row, int col){
		
		
		System.out.println("Getting spell corrector at " + col);
		
		MySpellCorrector sc = getSpellCorrectors().get(col);
		List<String> similarWords;
		if (sc != null){
			similarWords = sc.suggestSimilarWords(s);
		}
		else {
			similarWords = null;
		}
		
		if (similarWords != null){
		
			System.out.println("color row: " + row + ", col: " + col + " " +similarWords.toString());
		}
		else {
			System.out.println("don't color this cell, row: " + row + ", col: " + col);
		}
		return similarWords;	
	}

	public static void main( String args[] ) throws FacadeException
	{

		BatchState batchState = new BatchState();

		ClientCommunicator.SERVER_HOST = args[0];
		ClientCommunicator.SERVER_PORT = Integer.parseInt(args[1]);
		ClientCommunicator.URL_PREFIX = "http://" + ClientCommunicator.SERVER_HOST + ":" + ClientCommunicator.SERVER_PORT;

		LoginFrame frame = new LoginFrame(batchState);
		batchState.setLoginFrame(frame);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		IndexerFrame frame1 = new IndexerFrame(batchState);
		batchState.setIndexerFrame(frame1);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setVisible(false);

	}

}
