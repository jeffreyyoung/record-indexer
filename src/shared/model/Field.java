package shared.model;

import java.io.File;
import java.io.Serializable;


public class Field implements Serializable{
	
	public Field(int projectId, String title, int relativeId, int x_coord,
			int width, String knownData, String helphtml) {
		super();
		this.projectId = projectId;
		this.title = title;
		this.relativeId = relativeId;
		this.x_coord = x_coord;
		this.width = width;
		this.knownData = knownData;
		this.helphtml = helphtml;
	}
	
	public Field(int id, int projectId, String title, int relativeId, int x_coord,
			int width, String knownData, String helphtml) {
		super();
		this.id = id;
		this.projectId = projectId;
		this.title = title;
		this.relativeId = relativeId;
		this.x_coord = x_coord;
		this.width = width;
		this.knownData = knownData;
		this.helphtml = helphtml;
	}
	private int id;
	private int projectId;
	private String title;
	private int relativeId;
	private int x_coord;
	private int width;
	private String knownData;
	private String helphtml;
	
	
	public String getHelphtml() {
		return helphtml;
	}

	public void setHelphtml(String helphtml) {
		this.helphtml = helphtml;
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
	public int getRelativeId() {
		return relativeId;
	}
	public void setRelativeId(int fieldNum) {
		this.relativeId = fieldNum;
	}
	public int getX_coord() {
		return x_coord;
	}
	public void setX_coord(int x_coord) {
		this.x_coord = x_coord;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getKnownData() {
		return knownData;
	}
	public void setKnownData(String knownData) {
		this.knownData = knownData;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void appendHostToUrl(String host){
		this.helphtml = host + File.separator + helphtml;
		if (knownData != null && !knownData.isEmpty()){
			this.knownData = host + File.separator + knownData;
		}
	}
	

}
