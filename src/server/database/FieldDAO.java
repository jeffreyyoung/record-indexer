package server.database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import shared.model.Field;

public class FieldDAO {
	
	private Database db;
	
	public FieldDAO(Database db) {
		this.db = db;
	}
	
	
	public List<Field> getAll(){
		Connection connection = db.getConnection();
		System.out.println("getting fields for project");
		try {
			Statement stmt = connection.createStatement();
			String sql = "Select * from field";
			
			ResultSet rs = stmt.executeQuery(sql);
			
			List<Field> fields = new ArrayList<Field>();
			
			while ( rs.next() ) {
				
				int id = rs.getInt("id");
				int projectid = rs.getInt("projectid");
				int relativeid = rs.getInt("relativeid");
				String knowndata = rs.getString("knowndata");
				String title = rs.getString("title");
				int width = rs.getInt("width");
				int xcoord = rs.getInt("xcoord");
				String helphtml = rs.getString("helphtml");

				
				Field f = new Field(id, projectid, title, relativeid, xcoord, width, knowndata, helphtml);
				
				fields.add(f);
				
			}
			
			rs.close();
			stmt.close();
			return fields;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get all fields for given project
	 * 
	 * @param projectId
	 * @return returns list of fields for given project
	 */
	public List<Field> getFieldsForProject(int projectId){
		Connection connection = db.getConnection();
		System.out.println("getting fields for project");
		try {
			Statement stmt = connection.createStatement();
			String sql = "Select * from field where projectid = " + projectId;
			
			ResultSet rs = stmt.executeQuery(sql);
			
			List<Field> fields = new ArrayList<Field>();
			
			while ( rs.next() ) {
				
				int id = rs.getInt("id");
				int projectid = rs.getInt("projectid");
				int relativeid = rs.getInt("relativeid");
				String knowndata = rs.getString("knowndata");
				String title = rs.getString("title");
				int width = rs.getInt("width");
				int xcoord = rs.getInt("xcoord");
				String helphtml = rs.getString("helphtml");

				
				Field f = new Field(id, projectid, title, relativeid, xcoord, width, knowndata, helphtml);
				
				fields.add(f);
				
			}
			
			rs.close();
			stmt.close();
			return fields;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Add field to DB
	 * 
	 * @param field to be added to DB
	 * @return true if successful
	 */
	public Boolean add(Field field){
		Connection connection = db.getConnection();
		PreparedStatement stmt = null;
		try {  
			String sql = "INSERT INTO field"
					+ "(projectid, relativeid, knowndata, title, width, xcoord, helphtml)"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)"; 
			stmt = connection.prepareStatement(sql); 
			stmt.setInt(1, field.getProjectId()); 
			stmt.setInt(2, field.getRelativeId()); 
			stmt.setString(3, field.getKnownData()); 
			stmt.setString(4, field.getTitle()); 
			stmt.setInt(5, field.getWidth());
			stmt.setInt(6, field.getX_coord());
			stmt.setString(7, field.getHelphtml());
	
			stmt.executeUpdate();	
			stmt.close();
		} 
		catch (SQLException e) { 
			e.printStackTrace();
			return null;
		} 
		return true;
	}
}
