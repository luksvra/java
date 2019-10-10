package ar.com.hipotecario.api.vendedores.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VendedorResponse {

	private List<Vendedor> result; 
	private List<Error> error = new ArrayList<Error>();

	
	public List<Vendedor> getResult() {
		return result;
	}

	public void setResult(List<Vendedor> result) {
		this.result = result;
	}

	public List<Error> getError() {
		return error;
	}

	public void setError(List<Error> error) {
		this.error = error;
	}

	public static class Error{
		
		protected String descripcion;

		public Error(String descripcion) {
			super();
			this.descripcion = descripcion;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
	
	}
	
		
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
