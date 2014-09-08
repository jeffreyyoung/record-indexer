package shared.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import shared.SearchHit;

public class Search_Result extends Result implements Serializable {
	
	List<SearchHit> hits;
	
	public Search_Result(){
		this.hits = new ArrayList<SearchHit>();
	}

	public List<SearchHit> getHits() {
		return hits;
	}

	public void setHits(List<SearchHit> hits) {
		this.hits = hits;
	}
	
	public void addHit(SearchHit hit){
		hits.add(hit);
	}
	
	public void prependHostToUrl(String host){
		for (SearchHit hit: hits){
			hit.getBatch().appendHostToUrl(host);
		}
	}
	
	public String toString(){
		
		StringBuilder b = new StringBuilder();
		
		if (hits.size() == 0){
			b.append("FAILED");
		}
		
		for (SearchHit h : hits){
			b.append(h.getBatch().getId());
			b.append("\n");
			b.append(h.getBatch().getImageURL());
			b.append("\n");
			b.append(h.getCell().getRecordNum());
			b.append("\n");
			b.append(h.getCell().getFieldId());
			b.append("\n");
		}
		
		return b.toString();
	}
	
}
