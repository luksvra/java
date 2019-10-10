package ar.com.hipotecario.api.vendedores.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class VendedorDuenio {

	protected String idSecuencial1;
	protected String idSecuencial2;
	protected String idSecuencial3;
	protected String idVendedor; //secuencial4
	protected String idCobis2;
	protected String idCobis3;
	protected String descCanalVenta;
	protected String estado;
	protected String idFuncionario;
	protected String cbu;
	protected String nombre;
	protected String apellido;
	
	public String getIdSecuencial1() {
		return idSecuencial1;
	}
	
	public String getIdSecuencial3() {
		return idSecuencial3;
	}

	public void setIdSecuencial3(String idSecuencial3) {
		this.idSecuencial3 = idSecuencial3;
	}

	public String getIdCobis3() {
		return idCobis3;
	}

	public void setIdCobis3(String idCobis3) {
		this.idCobis3 = idCobis3;
	}

	public void setIdSecuencial1(String idSecuencial1) {
		this.idSecuencial1 = idSecuencial1;
	}
	
	public String getIdSecuencial2() {
		return idSecuencial2;
	}
	
	public void setIdSecuencial2(String idSecuencial2) {
		this.idSecuencial2 = idSecuencial2;
	}
	
	public String getIdCobis2() {
		return idCobis2;
	}
	
	public void setIdCobis2(String idCobis2) {
		this.idCobis2 = idCobis2;
	}
	
	public String getDescCanalVenta() {
		return descCanalVenta;
	}
	
	public void setDescCanalVenta(String descCanalVenta) {
		this.descCanalVenta = descCanalVenta;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getIdVendedor() {
		return idVendedor;
	}

	public void setIdVendedor(String idVendedor) {
		this.idVendedor = idVendedor;
	}

	public String getIdFuncionario() {
		return idFuncionario;
	}

	public void setIdFuncionario(String idFuncionario) {
		this.idFuncionario = idFuncionario;
	}

	public String getCbu() {
		return cbu;
	}

	public void setCbu(String cbu) {
		this.cbu = cbu;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	
	

}
