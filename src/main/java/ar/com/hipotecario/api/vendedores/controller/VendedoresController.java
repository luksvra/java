package ar.com.hipotecario.api.vendedores.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.com.hipotecario.api.vendedores.model.Originacion;
import ar.com.hipotecario.api.vendedores.model.Vendedor;
import ar.com.hipotecario.api.vendedores.model.VendedorResponse;
import ar.com.hipotecario.api.vendedores.service.OriginacionService;
import ar.com.hipotecario.api.vendedores.service.VendedoresService;
import ar.com.hipotecario.logger.BHServiceName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value="/v1")
@Api(value = "API-Vendedores", tags="ABM de Vendedores")
public class VendedoresController {

	@Autowired
	@Qualifier("vendedoresService")
	VendedoresService vendedoresService;
	
	@Autowired
	@Qualifier("originacionService")
	OriginacionService originacionService;
		
	
	@BHServiceName(value="API-Vendedores_AltaCanalOriginacion")
	@PostMapping(value="/originacion/{nivel}")
	@ApiOperation("ABM de Vendedores - Alta Canal Originacion")
	public ResponseEntity<List<Originacion>> postOriginacion (
		@ApiIgnore @RequestHeader() Map<String, String> header,
		@ApiParam(required = true, value="Identificador Canal de Originacion (1-2-3)", allowableValues="1,2,3") @PathVariable(required = true, value = "nivel") String nivel,
		@ApiParam(value="originacion", required = true) @RequestBody() Originacion originacion) {
		
		List<Originacion> response = this.originacionService.insertarOriginacion(header, originacion, nivel);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@BHServiceName(value="API-Vendedores_ActualizarCanalOriginacion")
	@PatchMapping(value="/originacion/{nivel}")
	@ApiOperation("ABM de Vendedores - Actualizar Canal Originacion")
	public ResponseEntity<List<Originacion>> patchOriginacion (
		@ApiIgnore @RequestHeader() Map<String, String> header,
		@ApiParam(required = true, value="Identificador Canal de Originacion (1-2-3)", allowableValues="1,2,3") @PathVariable(required = true, value = "nivel") String nivel,
		@ApiParam(value="Originacion", required = true) @RequestBody() Originacion originacion) {
		
		List<Originacion> response = this.originacionService.actualizarOriginacion(header, originacion, nivel);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@BHServiceName(value="API-Vendedores_ConsultarcanalOriginacion")
	@GetMapping(value="/originacion/{nivel}")
	@ApiOperation("ABM de Vendedores - Consultar Canal Originacion")
	public ResponseEntity<List<Originacion>> getOriginacion (
		@ApiIgnore @RequestHeader() Map<String, String> header,
		@ApiParam(required = true, value="Identificador Canal de Originacion (1-2-3)", allowableValues="1,2,3") @PathVariable(required = true, value = "nivel") String nivel,
		@ApiParam(required = true, value = "idSecuencial en campañas") @RequestParam(required = true, value = "idSecuencial") String idSecuencial) {
		List<Originacion> response = this.originacionService.consultaOriginacion(header, nivel, idSecuencial);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	
	@BHServiceName(value="API-Vendedores_AltaCanalVenta")
	@PostMapping(value="/vendedores/{nivel}")
	@ApiOperation("ABM de Vendedores - Alta Canal Venta")
	public ResponseEntity<VendedorResponse> postVendedor (
		@ApiIgnore @RequestHeader() Map<String, String> header,
		@ApiParam(required = true, value="Identificador Canal de Venta (2-3-4)", allowableValues="2,3,4") @PathVariable(required = true, value = "nivel") String nivel,
		@ApiParam(value="vendedor", required = true) @RequestBody() Vendedor vendedor) {
		
		return this.vendedoresService.insertar(header, vendedor, nivel);
	}


	@BHServiceName(value="API-Vendedores_ActualizarCanalVenta")
	@PatchMapping(value="/vendedores/{nivel}")
	@ApiOperation("ABM de Vendedores - Actualizar Canal Venta")
	public ResponseEntity<VendedorResponse> patchVendedor (
		@ApiIgnore @RequestHeader() Map<String, String> header,
		@ApiParam(required = true, value="Identificador Canal de Venta (2-3-4)", allowableValues="2,3,4") @PathVariable(required = true, value = "nivel") String nivel,
		@ApiParam(value="Vendedor", required = true) 
		@RequestBody() Vendedor vendedor) {
		
		return this.vendedoresService.actualizar(header, vendedor, nivel);
	}


	@BHServiceName(value="API-Vendedores_ConsultarCanalVentas")
	@GetMapping(value="/vendedores/{nivel}")
	@ApiOperation("ABM de Vendedores - Consultar Canal Ventas")
	public ResponseEntity<List<Vendedor>> getVendedores (
		@ApiIgnore @RequestHeader() Map<String, String> header,
		@PathVariable(required = true, value = "nivel") String nivel,
		@ApiParam(value = "identificador parametro 1") @RequestParam(required = true, value = "param1") String param1,
		@ApiParam(value = "identificador parametro 2") @RequestParam(required = false, value = "param2") String param2,
		@ApiParam(value = "identificador parametro 3") @RequestParam(required = false, value = "param3") String param3) throws Exception {
		
		List<Vendedor> response = this.vendedoresService.consulta(header, nivel, param1, param2, param3);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	
}
