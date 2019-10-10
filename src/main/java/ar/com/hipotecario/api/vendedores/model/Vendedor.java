package ar.com.hipotecario.api.vendedores.model;

import io.swagger.annotations.ApiModelProperty;


public class Vendedor {

	@ApiModelProperty(required=true, value="Id Cobis 2 al que pertenece el canal")
	private Integer idCobis2;
	@ApiModelProperty(required=true, value="Id Cobis 3 al que pertenece el canal")
	private Integer idCobis3;
	@ApiModelProperty(required=true, value="Secuencial nivel 1")
	private Integer idSecuencial1;
	@ApiModelProperty(required=true, value="Secuencial nivel 2")
	private Integer idSecuencial2;
	@ApiModelProperty(required=true, value="Secuencial nivel 3")
	private Integer idSecuencial3;
	@ApiModelProperty(required=true, value="Descripcion Canal de Venta 2-3; necesario para operar a nivel 2, 3")
	private String  descCanalVenta;
	@ApiModelProperty(required=false, value="Codido Cliente; necesario para operar a nivel 2")
	private Integer  codCliente;
	@ApiModelProperty(required=false, value="estado; necesario para operar a nivel 2, 3")
	protected String estado;
	@ApiModelProperty(required=true, value="Nombre vendedor; necesario para operar a nivel 4")
	protected String nombre;
	@ApiModelProperty(required=true, value="Apellido vendedor; necesario para operar a nivel 4")
	protected String apellido;
	@ApiModelProperty(required=true, value="Codigo de vendedor, secuencial nivel 4; necesario para operar a nivel 4")
	protected String idVendedor;
	@ApiModelProperty(required=true, value="Codigo de funcionario (usuario); necesario para operar a nivel 4")
	protected String idFuncionario;
	@ApiModelProperty(required=false, value="CBU; necesario para operar a nivel 4")
	protected String cbu;
	
	
	public Integer getIdCobis2() {
		return idCobis2;
	}
	
	public void setIdCobis2(Integer idCobis2) {
		this.idCobis2 = idCobis2;
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
	
	public String getDescCanalVenta() {
		return descCanalVenta;
	}
	
	public void setDescCanalVenta(String descCanalVenta) {
		this.descCanalVenta = descCanalVenta;
	}
	
	public Integer getCodCliente() {
		return codCliente;
	}
	
	public void setCodCliente(Integer codCliente) {
		this.codCliente = codCliente;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Integer getIdCobis3() {
		return idCobis3;
	}

	public void setIdCobis3(Integer idCobis3) {
		this.idCobis3 = idCobis3;
	}

	public Integer getIdSecuencial3() {
		return idSecuencial3;
	}

	public void setIdSecuencial3(Integer idSecuencial3) {
		this.idSecuencial3 = idSecuencial3;
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
	
	
	

}
