package dataimporter;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import server.database.Database;
import server.database.DatabaseException;
import shared.model.Batch;
import shared.model.Cell;
import shared.model.Field;
import shared.model.Project;
import shared.model.User;


public class DataImporter {

	private DataImporterFacade facade;
	
	public static void main(String[] args) throws Exception {
		DataImporter importer = new DataImporter();
		importer.importData(args[0]);
	}
	
	public DataImporter(){
		this.facade = new DataImporterFacade();
	}
	
	public void importData(String fileLocation) throws Exception{

		String copiedFileLoc = copyFiles(fileLocation);
		Element root = getXmlRoot(copiedFileLoc);
		addUsersToDb(root);
		addProjectsToDb(root);
		addCellsToDb();
		addCellValuesToDb(root);
	}
	
	public String copyFiles(String filename) throws Exception
	{
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		File file = new File(filename);
		File dest = new File("Records");
		

		/*
		 * (**APACHE**)
		 */
		try
		{
			//	We make sure that the directory we are copying is not the the destination
			//	directory.  Otherwise, we delete the directories we are about to copy.
			if(!file.getParentFile().getCanonicalPath().equals(dest.getCanonicalPath()))
				FileUtils.deleteDirectory(dest);
				
			//	Copy the directories (recursively) from our source to our destination.
			FileUtils.copyDirectory(file.getParentFile(), dest);
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		/*
		 * (**APACHE**)
		 */
		
		return dest.getPath() + File.separator + file.getName();

	}
	
	private int getFieldIdAssociatedWithRelativeId(List<Field> fields, int relativeId){
		for (Field field: fields){
			if (field.getRelativeId() == relativeId){
				return field.getId();
			}
		}
		return -1;
	}
	
	private void addCellValuesToDb(Element root){
		
		NodeList batches = root.getElementsByTagName("image");
		
		for (int i = 0; i < batches.getLength(); i++){
	
			int batchId = i + 1;
			
			Element batch = (Element) batches.item(i);
			
			NodeList records = batch.getElementsByTagName("record");
			
			for (int recordNum = 0; recordNum < records.getLength(); recordNum++){
				
				Element record = (Element) records.item(recordNum);
				
				NodeList values = record.getElementsByTagName("value");
				
				for (int relativeId = 0; relativeId < values.getLength(); relativeId++){
					
					String value = values.item(relativeId).getTextContent();
					
					facade.updateCellValue(value, batchId, relativeId, recordNum);
						
				}		
				
			}
			
		}
	}
	
	private Element getXmlRoot(String fileName){
		File xmlFile = new File(fileName); 

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile); //Can use URI instead of xmlFile 
			doc.getDocumentElement().normalize(); 
			Element root = doc.getDocumentElement(); 
			
			return root;
			
			
		} catch (ParserConfigurationException e) {

			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	private void addCellsToDb(){
		Database db = new Database();
		try {
			db.initialize();
			db.startTransaction();

			List<Batch> batches = db.getBatchDAO().getAll();
			
			for (Batch batch : batches){
				
				Project project = db.getProjectDAO().getProject(batch.getProjectId());
				List<Field> fields = db.getFieldDAO().getFieldsForProject(batch.getProjectId());
				
				int numRows = project.getRecordsPerImage();

				for (int row = 0; row < numRows; row++){	
					for (int col = 0; col < fields.size(); col++){
						
						int relativeId = col;
						int recordNum = row;
						int batchId = batch.getId();
						String value = null;						
						int fieldId = getFieldIdAssociatedWithRelativeId(fields, relativeId);
						Cell cell = new Cell(batchId, recordNum, relativeId, fieldId, value);
						
						db.getCellDAO().add(cell);
					}
				}
			}	
			db.endTransaction(true);
		} catch (DatabaseException e) {
			db.endTransaction(false);
			e.printStackTrace();
		} catch (SQLException e) {
			db.endTransaction(false);
			e.printStackTrace();
		}
		
	}
	
	private void addUsersToDb(Element e){
		NodeList users = e.getElementsByTagName("user");
		
		for (int i = 0; i < users.getLength(); i++){
			
			Element user = (Element)users.item(i);
		
			String username = user.getElementsByTagName("username").item(0).getTextContent();
			String password = user.getElementsByTagName("password").item(0).getTextContent();
			String firstname = user.getElementsByTagName("firstname").item(0).getTextContent();
			String lastname = user.getElementsByTagName("lastname").item(0).getTextContent();
			String email = user.getElementsByTagName("email").item(0).getTextContent();
			int indexedrecords = Integer.parseInt(user.getElementsByTagName("indexedrecords").item(0).getTextContent());
			
			User u = new User(username, password, firstname, lastname, email, indexedrecords, -1);
			facade.addUser(u);
		}
	}

	private void addProjectsToDb(Element e){
		NodeList projects = e.getElementsByTagName("project");
		
		for (int i  = 0; i < projects.getLength(); i++ ){
			int projectId = i + 1;
			
			Element project = (Element)projects.item(i);
			
			String title =  project.getElementsByTagName("title").item(0).getTextContent();
			int recordsperimage = Integer.parseInt(project.getElementsByTagName("recordsperimage").item(0).getTextContent());
			int firstycoord = Integer.parseInt(project.getElementsByTagName("firstycoord").item(0).getTextContent());
			int recordheight = Integer.parseInt(project.getElementsByTagName("recordheight").item(0).getTextContent());
			int fieldsperimage = project.getElementsByTagName("field").getLength(); //CHANGE THIS
		
			Project p = new Project(title, recordsperimage, fieldsperimage, recordheight, firstycoord);
			facade.addProject(p);
			
			processFieldsForProject(project, projectId);
			processImagesForProject(project, projectId);
			
		}
	}
	
	private void processFieldsForProject(Element project, int projectId){
		NodeList fields = project.getElementsByTagName("field");
		
		for (int i = 0; i < fields.getLength(); i++){
			
			Element field = (Element)fields.item(i);
			
			String title = field.getElementsByTagName("title").item(0).getTextContent();
			int xcoord = Integer.parseInt(field.getElementsByTagName("xcoord").item(0).getTextContent());
			int width = Integer.parseInt(field.getElementsByTagName("width").item(0).getTextContent());
			String helphtml = field.getElementsByTagName("helphtml").item(0).getTextContent();
			String knowndata = new String();
			
			if (field.getElementsByTagName("knowndata").getLength() > 0){
				knowndata = field.getElementsByTagName("knowndata").item(0).getTextContent();
			}
			
			int relativeId = i; //TODO should columns start at 0 or 1??
			
			Field f = new Field(projectId, title, relativeId, xcoord, width, knowndata, helphtml);
			facade.addField(f);
			
		}
	}
	
	private void processImagesForProject(Element project, int projectId){
		NodeList images = project.getElementsByTagName("image");
		
		for (int i = 0; i < images.getLength(); i++){
			
			Element image = (Element)images.item(i);
			
			String file = image.getElementsByTagName("file").item(0).getTextContent();
			
			Batch b = new Batch(projectId, file, true);
			facade.addBatch(b);
			
			
		}
	}	
}


