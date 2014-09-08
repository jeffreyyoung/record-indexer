package server.handler;

import java.io.IOException;
import java.io.OutputStream;

import server.Facade;
import server.FacadeException;
import shared.Serializer;
import shared.communication.SubmitBatch_Params;
import shared.communication.SubmitBatch_Result;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class SubmitBatchHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		try {
						
			Facade f = new Facade();
			
			SubmitBatch_Params params = new SubmitBatch_Params();
			params = (SubmitBatch_Params)Serializer.deserialzie(exchange.getRequestBody());
			
			SubmitBatch_Result result = f.submitBatch(params);

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