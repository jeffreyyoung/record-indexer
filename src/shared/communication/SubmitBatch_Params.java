package shared.communication;


public class SubmitBatch_Params extends Params {

	private int batchId;
	String fieldValues;
	
	public int getBatchId() {
		return batchId;
	}
	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}
	public String getFieldValues() {
		return fieldValues;
	}
	public void setFieldValues(String fieldValues) {
		this.fieldValues = fieldValues;
	}

}
