package ar.com.hipotecario.api.vendedores.model.core;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class WOCI<T> {

	private String serviceName;
	private Integer resultCode;
	private Integer retStatus;
	private String resultString;
	private List<Table<T>> tables;

	public String getServiceName() {
		return serviceName;
	}
	public Integer getResultCode() {
		return resultCode;
	}
	public Integer getRetStatus() {
		return retStatus;
	}
	public List<Table<T>> getTables() {
		return tables;
	}
	public String getResultString() {
		return resultString;
	}

	@JsonProperty(value = "service_name", access = Access.WRITE_ONLY)
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	@JsonProperty(value = "resultCode", access = Access.WRITE_ONLY)
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}
	@JsonProperty(value = "ret_status", access = Access.WRITE_ONLY)
	public void setRetStatus(Integer retStatus) {
		this.retStatus = retStatus;
	}
	@JsonProperty(value = "TABLE", access = Access.WRITE_ONLY)
	public void setTables(List<Table<T>> tables) {
		this.tables = tables;
	}
	@JsonProperty(value = "resultString", access = Access.WRITE_ONLY)
	public void setResultString(String resultString) {
		this.resultString = resultString;
	}
	

}
