package shared.communication;

import java.util.ArrayList;
import java.util.List;

public class Search_Params extends Params {
	
	List<Integer> fieldIds;
	List<String> cellValues;
	
	public Search_Params(){
		this.fieldIds = new ArrayList<Integer>();
		this.cellValues = new ArrayList<String>();
	}
	
	public List<Integer> getFieldIds() {
		return fieldIds;
	}
	public void setFieldIds(List<Integer> fieldIds) {
		this.fieldIds = fieldIds;
	}
	public List<String> getCellValues() {
		return cellValues;
	}
	public void setCellValues(List<String> cellValues) {
		this.cellValues = cellValues;
	}
	
	public void addCellValues(String sValues){
		String[] values = sValues.split(",", -1);
		for (String value: values){
			cellValues.add(value);
		}
		
	}
	
	public void addFieldIds(String sIds){
		String[] ids = sIds.split(",", -1);
		for (String id : ids){
			
			Integer number = Integer.valueOf(id);
			fieldIds.add(number);
		}
		
	}
	


	
}
