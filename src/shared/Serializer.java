package shared;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;



public class Serializer {
	
	public static String serialize(Serializable o){
		XStream xstream = new XStream(new DomDriver());
		
		String xml = xstream.toXML(o);
		
		return xml;
        
	}
	
	public static Object deserialzie(String s){
		
		XStream xstream = new XStream(new DomDriver());
		
        return xstream.fromXML(s);
	}
	
	public static Object deserialzie(InputStream s){
		
		XStream xstream = new XStream(new DomDriver());
		
        return xstream.fromXML(s);
	}
	
	public static void serializeToOutputStream(Object postData, OutputStream o){
		XStream xstream = new XStream(new DomDriver());
		
		xstream.toXML(postData, o);
		
	}
	
}
