package shared.model;

import java.io.Serializable;


public class Project implements Serializable {
	
	public Project(String title, int recordsPerImage, int fieldsPerImage,
			int recordHeight, int firstYCoord) {
		super();
		this.title = title;
		this.recordsPerImage = recordsPerImage;
		this.fieldsPerImage = fieldsPerImage;
		this.recordHeight = recordHeight;
		this.firstYCoord = firstYCoord;
	}
	
	
	
	public Project(int id, String title, int recordsPerImage,
			int fieldsPerImage, int recordHeight, int firstYCoord) {
		super();
		this.id = id;
		this.title = title;
		this.recordsPerImage = recordsPerImage;
		this.fieldsPerImage = fieldsPerImage;
		this.recordHeight = recordHeight;
		this.firstYCoord = firstYCoord;
	}



	public Project() {

	}



	private int id;
	private String title;
	
	private int recordsPerImage; //records and rows per image are the same
	private int fieldsPerImage; //is equal to fields per image

	
	private int recordHeight;
	private int firstYCoord;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getRecordsPerImage() {
		return recordsPerImage;
	}
	public void setRecordsPerImage(int recordsPerImage) {
		this.recordsPerImage = recordsPerImage;
	}
	public int getFieldsPerImage() {
		return fieldsPerImage;
	}
	public void setFieldsPerImage(int fieldsPerImage) {
		this.fieldsPerImage = fieldsPerImage;
	}
	public int getRecordHeight() {
		return recordHeight;
	}
	public void setRecordHeight(int recordHeight) {
		this.recordHeight = recordHeight;
	}
	public int getFirstYCoord() {
		return firstYCoord;
	}
	public void setFirstYCoord(int firstYCoord) {
		this.firstYCoord = firstYCoord;
	}


	
	

}
