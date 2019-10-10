package ar.com.hipotecario.api.vendedores.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class WSGatewayQuery {

	private List<Map<String, String>> RECORD;

	public WSGatewayQuery(String serviceName) {
		this.RECORD = new ArrayList<>();
		this.RECORD.add(new HashMap<String, String>()); 
				new HashMap<>();
		this.RECORD.get(0).put("SERVICE_NAME", serviceName);		
	}
	
	public void put(String clave, String valor) {
		this.RECORD.get(0).put(clave, valor);
	}
	
	public String toJson() {		
		return new Gson().toJson(this);
	}
}
