package shared;

import java.io.Serializable;

import shared.model.Batch;
import shared.model.Cell;

public class SearchHit implements Serializable{
	Cell cell;
	Batch batch;
	
	public Cell getCell() {
		return cell;
	}
	public void setCell(Cell cell) {
		this.cell = cell;
	}
	public Batch getBatch() {
		return batch;
	}
	public void setBatch(Batch batch) {
		this.batch = batch;
	}
	
	
}
