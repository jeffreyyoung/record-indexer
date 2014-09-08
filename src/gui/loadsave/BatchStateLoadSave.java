package gui.loadsave;

import gui.BatchState;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BatchStateLoadSave {

	
	
	public static void saveBatchState(BatchState batchState){
		 try {
		      FileOutputStream out = new FileOutputStream("BatchStates/" + batchState.getUser().getUsername() + ".o");
		      
		      
		      batchState.setSaveData(new BatchSaveData(batchState));
		      
		      SerializableBatchState serialBS = new SerializableBatchState(batchState);
		      
		      ObjectOutputStream oos = new ObjectOutputStream(out);
		      oos.writeObject(serialBS);
		      oos.flush();
		    } catch (Exception e) {
		      System.out.println("Problem serializing: " + e);
		      e.printStackTrace();
		    }

	}
	
	@SuppressWarnings("resource")
	public static void loadBatchState(BatchState batchState) {

		FileInputStream inFile;
		try {
			inFile = new FileInputStream("BatchStates/" + batchState.getUser().getUsername() + ".o");
			ObjectInputStream inStream = new ObjectInputStream(inFile);
			SerializableBatchState serialBS = (SerializableBatchState)inStream.readObject();
			
			extractDataFromSerializableBatchStateToCurrentBatchState(serialBS, batchState);
			
			batchState.initPanles();
			batchState.getIndexerFrame().getImageComponent().setOrigin(serialBS.getSaveData().getW_x(), serialBS.getSaveData().getW_y());
			batchState.setSelectedRowAndCol(serialBS.getSelectedRow(), serialBS.getSelectedColumn());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	public static void extractDataFromSerializableBatchStateToCurrentBatchState(SerializableBatchState bs, BatchState currentBS){
		currentBS.setCurrentBatch(bs.getCurrentBatch());
		currentBS.setCurrentProject(bs.getCurrentProject());
		currentBS.setFields(bs.getFields());
		currentBS.setUser(bs.getUser());
		currentBS.setValues(bs.getValues());
		currentBS.setSaveData(bs.getSaveData());
		
	}
	
}
