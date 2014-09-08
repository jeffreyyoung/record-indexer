package shared.communication;

import java.io.Serializable;

public class GetSampleImage_Result extends Result implements Serializable {

	private String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String string) {
		this.imageUrl = string;
	}

	public String toString() {
		if (imageUrl == null){
			return "FAILED";
		}
		return imageUrl;
	}
	
	
}
