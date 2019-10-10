package ar.com.hipotecario.api.vendedores.model;

import io.swagger.annotations.ApiModelProperty;


public class Originacion {

	@ApiModelProperty(required=true, value="Abreviatura Canal de Originacion 1; necesario para operar a nivel 1")
	private String  abreviatura;
	@ApiModelProperty(required=true, value="Descripcion Canal de Originacion 1 y 2; necesario para operar a nivel 1 y 2")
	private String  descOriginacion;
	@ApiModelProperty(required=true, value="Id Cobis 1 al que pertenece el canal")
	private Integer idCobis1;
	@ApiModelProperty(required=true, value="Id Cobis 2 al que pertenece el canal")
	private Integer idCobis2;
	@ApiModelProperty(required=true, value="Id Cobis 3 al que pertenece el canal")
	private Integer idCobis3;
	@ApiModelProperty(required=true, value="Id Rol Originacion 1 al que pertenece el canal")
	private Integer idRol;
	@ApiModelProperty(required=true, value="Secuencial nivel 1")
	private Integer idSecuencial1;
	@ApiModelProperty(required=true, value="Secuencial nivel 2")
	private Integer idSecuencial2;
	@ApiModelProperty(required=true, value="Secuencial nivel 3")
	private String  idSecuencial3;
	@ApiModelProperty(required=true, value="Visibilidad del Canal de Originacion; necesario para operar a nivel 1")
	private Boolean visibilidad;
	@ApiModelProperty(required=true, value="Sucursal de Originacion; necesario para operar a nivel 2")
	private Integer idSucursal;
	@ApiModelProperty(required=true, value="Apellido; necesario para operar a nivel 3")
	private String  apellido;
	@ApiModelProperty(required=true, value="Nombre; necesario para operar a nivel 3")
	private String  nombre;
	
	
	public String getAbreviatura() {
		return abreviatura;
	}
	
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}
	
	public String getDescOriginacion() {
		return descOriginacion;
	}
	
	public void setDescOriginacion(String descOriginacion) {
		this.descOriginacion = descOriginacion;
	}
	
	public Integer getIdCobis1() {
		return idCobis1;
	}
	
	public void setIdCobis1(Integer idCobis1) {
		this.idCobis1 = idCobis1;
	}
	
	public Integer getIdCobis2() {
		return idCobis2;
	}
	
	public void setIdCobis2(Integer idCobis2) {
		this.idCobis2 = idCobis2;
	}
	
	public Integer getIdCobis3() {
		return idCobis3;
	}
	
	public void setIdCobis3(Integer idCobis3) {
		this.idCobis3 = idCobis3;
	}
	
	public Integer getIdRol() {
		return idRol;
	}
	
	public void setIdRol(Integer idRol) {
		this.idRol = idRol;
	}
	
	public Integer getIdSecuencial1() {
		return idSecuencial1;
	}
	
	public void setIdSecuencial1(Integer idSecuencial1) {
		this.idSecuencial1 = idSecuencial1;
	}
	
	public Integer getIdSecuencial2() {
		return idSecuencial2;
	}
	
	public void setIdSecuencial2(Integer idSecuencial2) {
		this.idSecuencial2 = idSecuencial2;
	}
	
	public String getIdSecuencial3() {
		return idSecuencial3;
	}
	
	public void setIdSecuencial3(String idSecuencial3) {
		this.idSecuencial3 = idSecuencial3;
	}
	
	public Boolean getVisibilidad() {
		return visibilidad;
	}
	
	public void setVisibilidad(Boolean visibilidad) {
		this.visibilidad = visibilidad;
	}
	
	public Integer getIdSucursal() {
		return idSucursal;
	}
	
	public void setIdSucursal(Integer idSucursal) {
		this.idSucursal = idSucursal;
	}
	
	public String getApellido() {
		return apellido;
	}
	
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


}
