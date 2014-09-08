package shared.model;

import java.io.File;
import java.io.Serializable;


public class Batch implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 634637499597515262L;
	private int id;
	private int projectId;
	private String imageURL;
	private Boolean isAvailable;
	
	public Batch(int id, int projectId, String imageURL, Boolean isAvailable) {
		super();
		this.id = id;
		this.projectId = projectId;
		this.imageURL = imageURL;
		this.isAvailable = isAvailable;
	}
	
	public Batch(int projectId, String imageURL, Boolean isAvailable) {
		super();
		this.projectId = projectId;
		this.imageURL = imageURL;
		this.isAvailable = isAvailable;
	}
	
	public Batch() {
		
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public Boolean getIsAvailable() {
		return isAvailable;
	}
	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	public void appendHostToUrl(String host){
		this.imageURL = "http://" + host + File.separator + imageURL;
	}

}
