package ar.com.hipotecario.api.vendedores.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class VendedorBeanN3 {

	private String idSecuencial3;
	private String idSecuencial1;
	private String idCobis2;
	private String idCobis3;
	private String descCanalVenta;
	private String estado;
	

	@JsonProperty(value="idSecuencial3", access=Access.READ_ONLY)
	public String getIdSecuencial3() {
		return idSecuencial3;
	}
	
	@JsonProperty(value="SEC_", access=Access.WRITE_ONLY)
	public void setIdSecuencial3(String idSecuencial3) {
		this.idSecuencial3 = idSecuencial3;
	}
		
	@JsonProperty(value="idSecuencial1", access=Access.READ_ONLY)
	public String getIdSecuencial1() {
		return idSecuencial1;
	}
	
	@JsonProperty(value="NIVEL_1", access=Access.WRITE_ONLY)
	public void setIdSecuencial1(String idSecuencial1) {
		this.idSecuencial1 = idSecuencial1;
	}
	
	@JsonProperty(value="idCobis2", access=Access.READ_ONLY)
	public String getIdCobis2() {
		return idCobis2;
	}
	
	@JsonProperty(value="NIVEL_2", access=Access.WRITE_ONLY)
	public void setIdCobis2(String idCobis2) {
		this.idCobis2 = idCobis2;
	}
	
	@JsonProperty(value="idCobis3", access=Access.READ_ONLY)
	public String getIdCobis3() {
		return idCobis3;
	}
	
	@JsonProperty(value="C_D__NIVEL_3", access=Access.WRITE_ONLY)
	public void setIdCobis3(String idCobis3) {
		this.idCobis3 = idCobis3;
	}
	
	
	@JsonProperty(value="descCanalVenta", access=Access.READ_ONLY)
	public String getDescCanalVenta() {
		return descCanalVenta;
	}
	
	@JsonProperty(value="DES__NIVEL_3", access=Access.WRITE_ONLY)
	public void setDescCanalVenta(String descCanalVenta) {
		this.descCanalVenta = descCanalVenta;
	}
	

	@JsonProperty(value="estado", access=Access.READ_ONLY)
	public String getEstado() {
		return estado;
	}
	
	@JsonProperty(value="ESTADO", access=Access.WRITE_ONLY)
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
}
