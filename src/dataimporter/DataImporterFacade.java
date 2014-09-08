package dataimporter;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import server.database.Database;
import server.database.DatabaseException;
import shared.model.Batch;
import shared.model.Cell;
import shared.model.Field;
import shared.model.Project;
import shared.model.User;

public class DataImporterFacade {

	private Database db;

	DataImporterFacade(){
		this.db = new Database();
		try {
			db.initialize();
			dropAndCreateTables();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

	
	public void addUser(User user){
		try {
			db.startTransaction();
			db.getUserDAO().addUser(user);
			db.endTransaction(true);
		} catch (DatabaseException e) {
			db.endTransaction(false);
			e.printStackTrace();
		}
	}

	public void addProject(Project project){
		try {
			db.startTransaction();
			db.getProjectDAO().add(project);
			db.endTransaction(true);
		} catch (DatabaseException e) {
			db.endTransaction(false);
			e.printStackTrace();
		}

	}

	public void addBatch(Batch batch){
		try {
			db.startTransaction();
			db.getBatchDAO().add(batch);
			db.endTransaction(true);
		} catch (DatabaseException e) {
			db.endTransaction(false);
			e.printStackTrace();
		}

	}

	public void addCell(Cell cell){
		try {
			db.startTransaction();
			db.getCellDAO().add(cell);
			db.endTransaction(true);
		} catch (DatabaseException e) {
			db.endTransaction(false);
			e.printStackTrace();
		}
	}
	
	public void updateCellValue(String value, int batchId, int relativeFieldId, int recordNum){
		try {
			db.startTransaction();
			db.getCellDAO().updateCellValue(value, batchId, relativeFieldId, recordNum);
			db.endTransaction(true);
		} catch (DatabaseException e) {
			db.endTransaction(false);
			e.printStackTrace();
		}
	}
	
	public void addField(Field field){
		try {
			db.startTransaction();
			db.getFieldDAO().add(field);
			
			
			db.endTransaction(true);
		} catch (DatabaseException e) {
			db.endTransaction(false);
			e.printStackTrace();
		}
	}

	public void dropAndCreateTables(){

		try {

			db.startTransaction();

			Connection c = db.getConnection();

			Statement stmt = c.createStatement();

			stmt.addBatch("drop table if exists project");
			stmt.addBatch("drop table if exists field");
			stmt.addBatch("drop table if exists user");
			stmt.addBatch("drop table if exists batch");
			stmt.addBatch("drop table if exists cell");

			stmt.addBatch("create table project(id integer primary key autoincrement,title text not null,fieldsperimage integer not null,recordsperimage integer not null,firstycoord integer not null,recordheight integer not null);");
			stmt.addBatch("create table field(id integer primary key autoincrement,projectid integer not null,relativeid integer not null,knowndata text not null,title text not null,width integer not null,xcoord integer not null, helphtml text);");
			stmt.addBatch("create table batch(id integer primary key autoincrement,imageurl text not null,projectid integer not null,isavailable integer not null default 1);");
			stmt.addBatch("create table cell(id integer primary key autoincrement,batchid integer not null,fieldid integer not null,relativeid integer not null,recordnum integer not null,value text);");
			stmt.addBatch("create table user(id integer primary key autoincrement,username text not null unique,password text not null,firstname text not null,lastname text not null,email text not null,recordsindexed integer,batchid integer);");

			stmt.executeBatch();
			stmt.close();
			db.endTransaction(true);

		} catch (DatabaseException e) {
			db.endTransaction(false);
			e.printStackTrace();
		} catch (SQLException e) {
			db.endTransaction(false);
			e.printStackTrace();
		}
	}
}