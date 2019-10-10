package ar.com.hipotecario.api.vendedores.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class VendedorBeanN4 {

	
	private String idVendedor;
	private String apellido;
	private String nombre;
	private String idSecuencial1;
	private String idCobis2;
	private String idCobis3;
	private String idFuncionario;
	private String estado;
	
		
	@JsonProperty(value="idVendedor", access=Access.READ_ONLY)
	public String getIdVendedor() {
		return idVendedor;
	}

	@JsonProperty(value="VEND_", access=Access.WRITE_ONLY)
	public void setIdVendedor(String idVendedor) {
		this.idVendedor = idVendedor;
	}
	
	@JsonProperty(value="apellido", access=Access.READ_ONLY)
	public String getApellido() {
		return apellido;
	}
	
	@JsonProperty(value="APE__VENDEDOR", access=Access.WRITE_ONLY)
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	@JsonProperty(value="nombre", access=Access.READ_ONLY)
	public String getNombre() {
		return nombre;
	}

	@JsonProperty(value="NOM__VENDEDOR", access=Access.WRITE_ONLY)
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@JsonProperty(value="idSecuencial1", access=Access.READ_ONLY)
	public String getIdSecuencial1() {
		return idSecuencial1;
	}
	
	@JsonProperty(value="CANAL_VTA", access=Access.WRITE_ONLY)
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
	
	@JsonProperty(value="NIVEL_3", access=Access.WRITE_ONLY)
	public void setIdCobis3(String idCobis3) {
		this.idCobis3 = idCobis3;
	}
		
	@JsonProperty(value="idFuncionario", access=Access.READ_ONLY)
	public String getIdFuncionario() {
		return idFuncionario;
	}

	@JsonProperty(value="LOGIN", access=Access.WRITE_ONLY)
	public void setIdFuncionario(String idFuncionario) {
		this.idFuncionario = idFuncionario;
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
