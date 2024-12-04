package ua.co.tensa.modules.requests;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import ua.co.tensa.Message;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpRequest {

	private final String url;
	private final String method;
	private final Map<String, String> parameters;

	public HttpRequest(String url, String method, Map<String, String> parameters) {
		this.url = url;
		this.method = method;
		this.parameters = parameters;
	}

	public JsonElement send() throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		if (method.equalsIgnoreCase("POST")) {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(getParamsList(), "UTF-8"));
			return processResponse(httpClient.execute(httpPost));
		} else if (method.equalsIgnoreCase("GET")) {
			HttpGet httpGet = new HttpGet(getUrlWithParams());
			return processResponse(httpClient.execute(httpGet));
		}

		return null;
	}

	private List<NameValuePair> getParamsList() {
		List<NameValuePair> params = new ArrayList<>();
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return params;
	}

	private String getUrlWithParams() {
		StringBuilder sb = new StringBuilder(url);
		sb.append('?');
		for (Map.Entry<String, String> param : parameters.entrySet()) {
			String encodedValue = URLEncoder.encode(param.getValue(), StandardCharsets.UTF_8);
			sb.append(param.getKey()).append('=').append(encodedValue).append('&');
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	private JsonElement processResponse(CloseableHttpResponse httpResponse) throws Exception {
		int status = httpResponse.getStatusLine().getStatusCode();
		HttpEntity entity = httpResponse.getEntity();
		if (entity != null) {
			String result = EntityUtils.toString(entity);
			try {
				JsonElement jsonElement = JsonParser.parseString(result);
				Message.info("Request was successful. Method: " + method + ". URL: " + url);
				return jsonElement;
			} catch (JsonSyntaxException ex) {
				if (status == 200) {
					Message.warn("The response is not a valid JSON. Method: " + method + ". URL: " + url);
				} else {
					Message.error("Request failed with status code: " + status + ". \nMethod: " + method + ". \nURL: " + url + ". Response: \n" + result);
				}
				return null;
			}
		}
		return null;
	}
}
