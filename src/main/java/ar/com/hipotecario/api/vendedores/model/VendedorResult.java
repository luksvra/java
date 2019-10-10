package ar.com.hipotecario.api.vendedores.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.google.gson.Gson;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class VendedorResult {

	private String serviceName;
	private Integer resultCode;
	private String resultString;
	private Integer retStatus;
	private List<TableResult<VendedorListResult>> tables;

	@JsonProperty(value="service_name", access=Access.READ_ONLY)
	public String getServiceName() {
		return serviceName;
	}
	@JsonProperty(value="resultCode", access=Access.READ_ONLY)
	public Integer getResultCode() {
		return resultCode;
	}
	@JsonProperty(value="resultString", access=Access.READ_ONLY)
	public String getResultString() {
		return resultString;
	}
	@JsonProperty(value="ret_status", access=Access.READ_ONLY)
	public Integer getRetStatus() {
		return retStatus;
	}
	@JsonProperty(value="TABLE", access=Access.READ_ONLY)
	public List<TableResult<VendedorListResult>> getTables() {
		return tables;
	}

	@JsonProperty(value="service_name", access=Access.WRITE_ONLY)
	public void setServiceName(String result) {
		this.serviceName = result;
	}
	@JsonProperty(value="resultCode", access=Access.WRITE_ONLY)
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}
	@JsonProperty(value="resultString", access=Access.WRITE_ONLY)
	public void setResultString(String resultString) {
		this.resultString = resultString;
	}
	@JsonProperty(value="ret_status", access=Access.WRITE_ONLY)
	public void setRetStatus(Integer retStatus) {
		this.retStatus = retStatus;
	}
	@JsonProperty(value = "TABLE", access = Access.WRITE_ONLY)
	public void setTables(List<TableResult<VendedorListResult>> tables) {
		this.tables = tables;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
