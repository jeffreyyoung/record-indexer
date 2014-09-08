package gui.loadsave;

import gui.BatchState;

import java.io.Serializable;
import java.util.List;

import shared.model.Batch;
import shared.model.Field;
import shared.model.Project;
import shared.model.User;

public class SerializableBatchState implements Serializable {

	private User user;
	private Batch currentBatch;
	private Project currentProject;
	private List<Field> fields;
	private List< List<String> > values;
	
	private Integer selectedColumn = 0;
	private Integer selectedRow = 0;
	
	private BatchSaveData saveData;
	private List< List< List <String>>> valueSuggestions;
	
	SerializableBatchState(BatchState batchState){
		
		this.user = batchState.getUser();
		this.currentBatch = batchState.getCurrentBatch();
		this.currentProject = batchState.getCurrentProject();
		this.fields = batchState.getFields();
		this.values = batchState.getValues();
		
		this.selectedColumn = batchState.getSelectedColumn();
		this.selectedRow = batchState.getSelectedRow();
		this.saveData = batchState.getSaveData();
		this.valueSuggestions = batchState.getValueSuggestions();
		
	}

	public User getUser() {
		return user;
	}

	public Batch getCurrentBatch() {
		return currentBatch;
	}

	public Project getCurrentProject() {
		return currentProject;
	}

	public List<Field> getFields() {
		return fields;
	}

	public List<List<String>> getValues() {
		return values;
	}

	public Integer getSelectedColumn() {
		return selectedColumn;
	}

	public Integer getSelectedRow() {
		return selectedRow;
	}
	
	public BatchSaveData getSaveData(){
		return saveData;
	}

	
	
	
}
