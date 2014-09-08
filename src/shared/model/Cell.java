package shared.model;

public class Cell {

	public Cell(int batchId, int recordNum, int relativeId, int fieldId,
			String value) {
		super();
		this.batchId = batchId;
		this.recordNum = recordNum;
		this.relativeId = relativeId;
		this.fieldId = fieldId;
		this.value = value;
	}
	
	public Cell(int id, int batchId, int recordNum, int relativeId, int fieldId,
			String value) {
		super();
		this.id = id;
		this.batchId = batchId;
		this.recordNum = recordNum;
		this.relativeId = relativeId;
		this.fieldId = fieldId;
		this.value = value;
	}
	private int id;
	private int batchId; //batch
	private int recordNum; //row
	private int relativeId; //field
	private int fieldId;
	private String value;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBatchId() {
		return batchId;
	}
	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}
	public int getRecordNum() {
		return recordNum;
	}
	public void setRecordNum(int row) {
		this.recordNum = row;
	}
	public int getRelativeId() {
		return relativeId;
	}
	public void setRelativeId(int relativeId) {
		this.relativeId = relativeId;
	}
	public int getFieldId() {
		return fieldId;
	}
	public void setFieldId(int fieldId) {
		this.fieldId = fieldId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
