package shared.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import shared.model.Field;

public class GetFields_Result extends Result implements Serializable {

	List<Field> fields = new ArrayList <Field>();

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
	public void updateFieldsHelpUrl(String host){
		for ( Field f: fields){
			f.appendHostToUrl(host);	
		}
	}
	
	public String toString() {
		
		StringBuilder b = new StringBuilder();
		
		if (fields.size() == 0){
			b.append("FAILED");
		}
		
		for (Field f: fields){
			if (f.getTitle() != null) {
				b.append(f.getProjectId());
				b.append("\n");
				b.append(f.getId());
				b.append("\n");
				b.append(f.getTitle());
				b.append("\n");
			}
		}
		
		return b.toString();
		
	}
}
