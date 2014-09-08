package server.handler;

import java.io.IOException;
import java.io.OutputStream;

import server.Facade;
import server.FacadeException;
import shared.Serializer;
import shared.communication.Search_Params;
import shared.communication.Search_Result;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class SearchHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		try {
						
			Facade f = new Facade();
			Search_Params params = new Search_Params();
			params = (Search_Params)Serializer.deserialzie(exchange.getRequestBody());
			
			Search_Result result = f.search(params);
	
	        String response = Serializer.serialize(result);
	        exchange.sendResponseHeaders(200, response.length());
	        OutputStream os = exchange.getResponseBody();
	        os.write(response.getBytes());
	        os.close();
			
		} catch (FacadeException e) {
			e.printStackTrace();
		}

	}
}