package server.handler;

import java.io.IOException;
import java.io.OutputStream;
import server.Facade;
import server.FacadeException;
import shared.Serializer;
import shared.communication.DownloadBatch_Params;
import shared.communication.DownloadBatch_Result;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class DownloadBatchHandler implements HttpHandler {


	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		try {
						
			Facade f = new Facade();
			DownloadBatch_Params params = new DownloadBatch_Params();
			params = (DownloadBatch_Params)Serializer.deserialzie(exchange.getRequestBody());
			
			DownloadBatch_Result result = f.downloadBatch(params);
			OutputStream os = exchange.getResponseBody();

	        String response = Serializer.serialize(result);
	        exchange.sendResponseHeaders(200, response.length());
	        os.write(response.getBytes()); //TODO figure out why error is thrown here

	        os.close();
			
		} catch (FacadeException e) {
			e.printStackTrace();
		}

	}
}