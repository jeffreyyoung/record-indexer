package server;

import java.sql.SQLException;
import java.util.List;

import server.database.Database;
import server.database.DatabaseException;
import shared.SearchHit;
import shared.communication.DownloadBatch_Params;
import shared.communication.DownloadBatch_Result;
import shared.communication.DownloadFile_Params;
import shared.communication.DownloadFile_Result;
import shared.communication.GetFields_Params;
import shared.communication.GetFields_Result;
import shared.communication.GetProjects_Params;
import shared.communication.GetProjects_Result;
import shared.communication.GetSampleImage_Params;
import shared.communication.GetSampleImage_Result;
import shared.communication.Search_Params;
import shared.communication.Search_Result;
import shared.communication.SubmitBatch_Params;
import shared.communication.SubmitBatch_Result;
import shared.communication.ValidateUser_Params;
import shared.communication.ValidateUser_Result;
import shared.model.Batch;
import shared.model.Cell;
import shared.model.Field;
import shared.model.Project;
import shared.model.User;
import client.ClientException;

public class Facade {

	private static Database db; 
	
	public Facade(){
		this.db = new Database();
	}
	
	public static void initialize() throws FacadeException {	
		try {
			Database.initialize();		
		}
		catch (DatabaseException e) {
			throw new FacadeException(e.getMessage(), e);
		}		
	}
	
	/**
	 * 
	 * @param params - UserName and password
	 * @return Returns null if invalid params, else returns a User object contained in a ValidateUser_Result object
	 * @throws ClientException
	 */
	public ValidateUser_Result validateUser(ValidateUser_Params params) throws FacadeException {
		
		ValidateUser_Result result = new ValidateUser_Result();
		try {
			db.startTransaction();
			
			String username = params.getUsername();
			String password = params.getPassword();
			
			result.setUser(db.getUserDAO().getUserByUsernameAndPassword(username, password));
			
			if (result.getUser() == null){
				result.setMessage("FALSE\n");
			}
			
			db.endTransaction(true);
			
			return result;
		
		} catch (DatabaseException e) {
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
			return result;
		} catch (SQLException e) {
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
			return result;
		}
	}
	
	/**
	 * 
	 * @return A list of projects
	 * @throws ClientException
	 */
	public GetProjects_Result getProjects(GetProjects_Params params) throws FacadeException{
		try {
			db.startTransaction();
			
			GetProjects_Result result = new GetProjects_Result();
			User user = db.getUserDAO().getUserByUsernameAndPassword(params.getUsername(), params.getPassword());
			
			if (user == null){
				db.endTransaction(false);
				return result;
			}

			result.setProjects(db.getProjectDAO().getAll());
			db.endTransaction(true);
			return result;
		
		} catch (DatabaseException e) {
			db.endTransaction(false);
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			db.endTransaction(false);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param params, project id
	 * @return Url of sample image for project
	 * @throws ClientException
	 */
	public GetSampleImage_Result getSampleImage(GetSampleImage_Params params) throws FacadeException{
		try {
			db.startTransaction();
			
			GetSampleImage_Result result = new GetSampleImage_Result();
			
			User user = db.getUserDAO().getUserByUsernameAndPassword(params.getUsername(), params.getPassword());
			
			if (user == null){
				db.endTransaction(false);
				return result;
			}
			
			result.setImageUrl(db.getBatchDAO().getSampleImage(params.getProjectId()));
			db.endTransaction(true);
			
			return result;
		
		} catch (DatabaseException e) {
			db.endTransaction(false);
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			db.endTransaction(false);
			e.printStackTrace();
			return null;
		}	
	}
	
	/**
	 * 
	 * @param params Project id
	 * @return A an available batch to be indexed
	 * @throws ClientException
	 */
	public DownloadBatch_Result downloadBatch(DownloadBatch_Params params) throws FacadeException{
		
		DownloadBatch_Result result = new DownloadBatch_Result();
		
		try {
			db.startTransaction();
			User user = db.getUserDAO().getUserByUsernameAndPassword(params.getUsername(), params.getPassword());
			
			if (user == null || user.getCurBatchId() != -1){
				db.endTransaction(false);
				result.setMessage("FAILED\n");
				return result;
			}
			
			Batch batch = db.getBatchDAO().getNextAvailableBatch(params.getProjectId());
			Project project = db.getProjectDAO().getProject(params.getProjectId());
			
			if (project == null){
				db.endTransaction(false);
				return null;
			}
			
			List<Field> fields = db.getFieldDAO().getFieldsForProject(params.getProjectId());
						
			result = new DownloadBatch_Result(batch, project, fields);
			db.getBatchDAO().setIsAvailable(0, batch.getId()); //set batch to unavailable as it has now been downloaded
			db.getUserDAO().updateCurrentBatchId(params.getUsername(), batch.getId()); //set user's currentbatch
			db.endTransaction(true);
			return result;
		
		} catch (DatabaseException e) {
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
			return result;
		} catch (SQLException e) {
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
			return null;
		}
	}
	
	/**
	 * 
	 * @param params batchId
	 * @return
	 * @throws ClientException
	 */
	public SubmitBatch_Result submitBatch(SubmitBatch_Params params) throws FacadeException{
		SubmitBatch_Result result = new SubmitBatch_Result();
		try {
			db.startTransaction();
			String sFieldValues = params.getFieldValues();
			int batchId = params.getBatchId();
			User user = db.getUserDAO().getUserByUsernameAndPassword(params.getUsername(), params.getPassword());
			
			if (user == null || user.getCurBatchId() != batchId){ //or if user doesn't own submitted batch
				db.endTransaction(false);
				result.setMessage("FAILED\n");
				return result;
			}
			
			Batch batch = db.getBatchDAO().getBatch(batchId);
			Project project = db.getProjectDAO().getProject(batch.getProjectId());
			List<Field> fields = db.getFieldDAO().getFieldsForProject(batch.getProjectId());
			sFieldValues = sFieldValues.replace(";", ",");
			String[] fieldValues = sFieldValues.split(",", -1);
			
			int numCellValues = project.getRecordsPerImage() * fields.size();
			
			System.out.println(numCellValues);
			System.out.println(fieldValues.length);
			
			if (fieldValues.length != numCellValues){
				System.out.println("your trying to submit " + fieldValues.length + "values while " + numCellValues + " is required");
				db.endTransaction(false);//in valid argument, number of cell values entered does not equal num cell values per batch
				result.setMessage("FAILED\n");
				return result;
			}
			
			System.out.println("updating cell values");
			
			int numRows = project.getRecordsPerImage();
			int arrayIndex = 0;
			for (int row = 0; row < numRows; row++){
				for (int col = 0; col < fields.size(); col++){	
					String value = fieldValues[arrayIndex++];
					db.getCellDAO().updateCellValue(value, batchId, col, row);
					
				}
			}
			db.getUserDAO().updateCurrentBatchId(params.getUsername(), -1);
			db.getUserDAO().incrementNumRecordsIndexed(user.getId(), project.getRecordsPerImage());
			
			db.endTransaction(true);
			result.setMessage("TRUE\n");
			return result;
			
		} catch (DatabaseException e) {
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
			return result;
		} catch (SQLException e) {
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
			return result;
		}
	}
	
	/**
	 * 
	 * @param params project id
	 * @return List of fields for project
	 * @throws ClientException
	 */
	public GetFields_Result getFields(GetFields_Params params) throws FacadeException{
		GetFields_Result result = new GetFields_Result();
		try {
			db.startTransaction();
			
			User user = db.getUserDAO().getUserByUsernameAndPassword(params.getUsername(), params.getPassword());
			
			if (user == null){
				db.endTransaction(false);
				return result;
			}
			
			if (params.getProjectId() == -1){
				result.setFields(db.getFieldDAO().getAll());
			}
			else {
				result.setFields(db.getFieldDAO().getFieldsForProject(params.getProjectId()));
			}
			
			db.endTransaction(true);
			
			return result;
		
		} catch (DatabaseException e) {
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
			return result;
		} catch (SQLException e) {
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
			return result;
		}	
	}
	
	/**
	 * 
	 * @param params List of fields to be search and in addition to a list of strings to be searched for
	 * @return
	 * @throws ClientException
	 */
	public Search_Result search(Search_Params params) throws FacadeException{//TODO fix search to allow multiple parameters
		
		Search_Result search_result = new Search_Result();
		try {
			db.startTransaction();
			
			User user = db.getUserDAO().getUserByUsernameAndPassword(params.getUsername(), params.getPassword());
			
			if (user == null){
				db.endTransaction(false);
				return search_result;
			}
			
			
			System.out.println("Searching for: " + params.getCellValues().toString());
			System.out.println("in fields: " + params.getFieldIds().toString());
			
			for (int f = 0; f < params.getFieldIds().size(); f++){
				
				for (int c = 0; c < params.getCellValues().size(); c++){
					
					String cellValue = params.getCellValues().get(c);
					Integer fieldId = params.getFieldIds().get(f);
					List <Cell> results = db.getCellDAO().getCellsByStringAndFieldId(cellValue, fieldId); 

					for (Cell cell: results){
						
						Batch batch = db.getBatchDAO().getBatch(cell.getBatchId());
						
						SearchHit hit = new SearchHit();
						hit.setCell(cell);
						hit.setBatch(batch);
						search_result.addHit(hit);
					}	
				}
			}
			
			db.endTransaction(true);
			
			return search_result;
		
		} catch (DatabaseException e) {
			db.endTransaction(false);
			e.printStackTrace();
			search_result.setMessage("FAILED\n");
			return search_result;
		} catch (SQLException e) {
			db.endTransaction(false);
			e.printStackTrace();
			search_result.setMessage("FAILED\n");
			return search_result;
		}	
	}
	
	/**
	 * 
	 * @param params
	 * @return Downloads file to bin
	 * @throws ClientException
	 */
	public DownloadFile_Result downloadFile(DownloadFile_Params params) throws FacadeException{
		return null;
	}
}
