package shared.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import shared.model.Batch;
import shared.model.Field;
import shared.model.Project;

public class DownloadBatch_Result extends Result implements Serializable {

	public DownloadBatch_Result(Batch batch, Project project, List<Field> fields) {
		super();
		this.batch = batch;
		this.project = project;
		this.fields = fields;
	}

	public DownloadBatch_Result() {

	}

	private Batch batch;
	private Project project;
	private List<Field> fields = new ArrayList<Field>();
	
	public Batch getBatch() {
		return batch;
	}
	
	
	public void updateFieldsHelpUrl(String host){
		for ( Field f: fields){
			f.appendHostToUrl("http://" + host);
			
		}
	}
	
	public void prependToFieldsHelpUrl(String host){
		for ( Field f: fields){
			f.setHelphtml(host + f.getHelphtml());
		}
	}
	
	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
	public String toString(){
		
		StringBuilder sb = new StringBuilder();
		
		if (fields.size() == 0){
			sb.append("FAILED");
		}
		else {
			
			
			sb.append(batch.getId());//batch id
			sb.append("\n");
			sb.append(project.getId());//project id
			sb.append("\n");
			sb.append(batch.getImageURL());//image url
			sb.append("\n");
			sb.append(project.getFirstYCoord());//first y coord
			sb.append("\n");
			sb.append(project.getRecordHeight());//
			sb.append("\n");
			sb.append(project.getRecordsPerImage());
			sb.append("\n");
			sb.append(fields.size());
			sb.append("\n");
			
			for (Field f : fields){
				sb.append(f.getId());
				sb.append("\n");
				sb.append(f.getRelativeId());
				sb.append("\n");
				sb.append(f.getTitle());
				sb.append("\n");
				sb.append(f.getHelphtml());
				sb.append("\n");
				sb.append(f.getX_coord());
				sb.append("\n");
				sb.append(f.getWidth());
				sb.append("\n");
				
				if (f.getKnownData().length() > 1){
					sb.append(f.getKnownData());
					sb.append("\n");
				}
			}
		}
		
		return sb.toString();
	}

}
