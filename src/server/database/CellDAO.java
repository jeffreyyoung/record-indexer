package server.database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import shared.model.Cell;


public class CellDAO {
	
	private Database db;
	
	CellDAO(Database db) {
		this.db = db;
	}
	
	/**
	 * Get cell by associated batch, column and row
	 * @param batchId
	 * @param relativeId - column
	 * @param recordNum - row
	 * @return - Cell
	 */
	
	public Cell getCell(int batchId, int relativeId, int recordNum){
		Connection connection = db.getConnection();
		try {

			
			String sql = "Select * from cell where batchid = ? and relativeid = ? and recordnum = ?";
			
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, batchId);
			stmt.setInt(2, relativeId);
			stmt.setInt(3, recordNum);
			
			ResultSet rs = stmt.executeQuery();
			
			Cell c = null;
			
			if ( rs.next() ) {
				
				int id = rs.getInt("id");
				int batchid = rs.getInt("batchid");
				int fieldid = rs.getInt("fieldid");
				int relativeid = rs.getInt("relativeid");
				int recordnum = rs.getInt("recordnum");
				String cellValue = rs.getString("value");
				
				c = new Cell(id, batchid, recordnum, relativeid, fieldid, cellValue );
				
			}
			
			rs.close();
			stmt.close();
			return c;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get cell by String value and File id
	 * 
	 * @param value - String to be searched for
	 * @param fieldId - Id of searched field
	 * @return - List of cells with parameter attributes
	 */
	public List<Cell> getCellsByStringAndFieldId(String value, int fieldId ){
		Connection connection = db.getConnection();
		try {

			
			String sql = "Select * from cell where value = ? and fieldid = ?";
			
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, value);
			stmt.setInt(2, fieldId);
			
			ResultSet rs = stmt.executeQuery();
			
			List<Cell> cells = new ArrayList<Cell>();
			
			while ( rs.next() ) {
				
				int id = rs.getInt("id");
				int batchid = rs.getInt("batchid");
				//int fieldid = rs.getInt("fieldid");
				int relativeid = rs.getInt("relativeid");
				int recordnum = rs.getInt("recordnum");
				//String cellValue = rs.getString("value");
				
				Cell c = new Cell(id, batchid, recordnum, relativeid, fieldId, value );
				
				cells.add(c);
			}
			rs.close();
			stmt.close();
			return cells;

		} catch (SQLException e) {
			return null;
		}
	}
	/**
	 * add cell to db
	 * 
	 * @param cell - Cell object
	 * @return Returns a boolean indicating whether or not a specific cell could be added
	 */
	public Boolean add(Cell cell){
		Connection connection = db.getConnection();
		PreparedStatement stmt = null;
		try {  
			String sql = "INSERT INTO cell"
					+ "(batchid, fieldid, relativeid, recordnum, value)"
					+ "VALUES (?, ?, ?, ?,?)"; 
			stmt = connection.prepareStatement(sql); 
			stmt.setInt(1, cell.getBatchId()); 
			stmt.setInt(2, cell.getFieldId()); 
			stmt.setInt(3, cell.getRelativeId()); 
			stmt.setInt(4, cell.getRecordNum()); 
			
			if (cell.getValue() == null){
				stmt.setString(5, null);
			}
			else {
				stmt.setString(5, cell.getValue().toLowerCase());
			}
	
			stmt.executeUpdate();	
			stmt.close();
		} 
		catch (SQLException e) { 
			try {stmt.close();} catch (SQLException e1){ e1.printStackTrace();}
			e.printStackTrace();
			return false;
		} 
		return true;
	}
	
	/**
	 * 
	 * Updates cell that fits the given parameters with the string value
	 * 
	 * @param batchId - Batch to which the cell pertains
	 * @param relativeFieldId - Batch column to which the cell pertains
	 * @param recordNum - Batch row to which the cell pertains
	 * @return Returns true if update was successful, false if not
	 */
	public Boolean updateCellValue(String value, int batchId, int relativeFieldId, int recordNum ) {
		StringBuilder sb = new StringBuilder();
		sb.append("updating cell... value: ").append(value).append(", batchId: ").append(batchId).append(", relative field Id: ").append(relativeFieldId).append(", recordNum: ").append(recordNum);
		
		System.out.println(sb.toString());
		
		Connection connection = db.getConnection();
		try {

			String sql = "UPDATE cell SET value = ? where relativeid = ? and batchid = ? and recordnum = ?";
			//String sql = "UPDATE cell SET value = \"hooooorayyy\" where batchid = 1 and relativeid = 1 and recordnum = 1";
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, value.toLowerCase());
			stmt.setInt(2, relativeFieldId);
			stmt.setInt(3, batchId);
			stmt.setInt(4, recordNum);
			stmt.execute();
			
			stmt.close();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
