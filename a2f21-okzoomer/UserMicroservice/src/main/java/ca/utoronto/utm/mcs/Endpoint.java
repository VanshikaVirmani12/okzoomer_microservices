package ca.utoronto.utm.mcs;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;
import org.json.JSONException;

public abstract class Endpoint implements HttpHandler {

    public PostgresDAO dao;
    public HashMap<Integer, String> errorMap;

    public Endpoint() {
        this.dao = new PostgresDAO();
        errorMap = new HashMap<>();
        errorMap.put(200, "OK");
        errorMap.put(400, "BAD REQUEST");
        errorMap.put(403, "FORBIDDEN");
        errorMap.put(404, "NOT FOUND");
        errorMap.put(405, "METHOD NOT ALLOWED");
        errorMap.put(500, "INTERNAL SERVER ERROR");
    }

    public void handle(HttpExchange r) {
        try {
            switch (r.getRequestMethod()) {
            case "GET":
                this.handleGet(r);
                break;
            case "PATCH":
                this.handlePatch(r);
                break;
            case "POST":
                this.handlePost(r);
                break;
            case "PUT":
                this.handlePut(r);
                break;
            case "DELETE":
                this.handleDelete(r);
                break;
            default:
                this.sendStatus(r, 405);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeOutputStream(HttpExchange r, String response) throws IOException {
        OutputStream os = r.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void sendResponse(HttpExchange r, JSONObject obj, int statusCode) throws JSONException, IOException {
        obj.put("status", errorMap.get(statusCode));
        String response = obj.toString();
        r.sendResponseHeaders(200, response.length());
        this.writeOutputStream(r, response);
    }

    public void sendStatus(HttpExchange r, int statusCode) throws JSONException, IOException {
        JSONObject res = new JSONObject();
        res.put("status", errorMap.get(statusCode));
        String response = res.toString();
        r.sendResponseHeaders(statusCode, response.length());
        this.writeOutputStream(r, response);
    }

    public void sendStatus(HttpExchange r, int statusCode, boolean hasEmptyData) throws JSONException, IOException {
        JSONObject res = new JSONObject();
        res.put("status", errorMap.get(statusCode));
        res.put("data", new JSONObject());
        String response = res.toString();
        r.sendResponseHeaders(statusCode, response.length());
        this.writeOutputStream(r, response);
    }

    public boolean validateFields(JSONObject JSONRequest, ArrayList<String> stringFields, ArrayList<String> integerFields) {
		try {
			return validateFieldsHelper(JSONRequest,stringFields, String.class) && validateFieldsHelper(JSONRequest, integerFields, Integer.class);
		} catch (JSONException e) {
			System.err.println("Caught Exception: " + e.getMessage());
			return false;
		}
	}

	// check if fields are in JSONRequest and make sure they're of type classOfFields
	private boolean validateFieldsHelper(JSONObject JSONRequest, ArrayList<String> fields, Class<?> classOfFields) throws JSONException {
		for (String field : fields) {
			if (!(JSONRequest.has(field) && JSONRequest.get(field).getClass().equals(classOfFields))) {
				return false;
			}
		}
		return true;
	}

    public void handleGet(HttpExchange r) throws IOException, JSONException {};

    public void handlePatch(HttpExchange r) throws IOException, JSONException {};

    public void handlePost(HttpExchange r) throws IOException, JSONException {};

    public void handlePut(HttpExchange r) throws IOException, JSONException {};

    public void handleDelete(HttpExchange r) throws IOException, JSONException {};

}