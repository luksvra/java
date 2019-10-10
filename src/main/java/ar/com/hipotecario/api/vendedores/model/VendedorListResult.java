package ar.com.hipotecario.api.vendedores.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.google.gson.Gson;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class VendedorListResult {

	private String codigo;
	private String secuencial;

	@JsonProperty(value="CODIGO", access=Access.READ_ONLY)
	public String getCodigo() {
		return codigo;
	}
	@JsonProperty(value="SECUENCIAL", access=Access.READ_ONLY)
	public String getSecuencial() {
		return secuencial;
	}

	@JsonProperty(value="CODIGO", access=Access.WRITE_ONLY)
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	@JsonProperty(value="SECUENCIAL", access=Access.WRITE_ONLY)
	public void setSecuencial(String secuencial) {
		this.secuencial = secuencial;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
