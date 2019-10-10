package ar.com.hipotecario.api.vendedores.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class TableResult<T> {
	
	private List<T> records;

	@JsonProperty(value="RECORD", access=Access.READ_ONLY)
	public List<T> getRecords() {
		return records;
	}

	@JsonProperty(value = "RECORD", access = Access.WRITE_ONLY)
	public void setRecords(List<T> records) {
		this.records = records;
	}

}
