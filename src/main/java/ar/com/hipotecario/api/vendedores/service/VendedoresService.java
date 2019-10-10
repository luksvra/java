package ar.com.hipotecario.api.vendedores.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.hipotecario.api.vendedores.model.Vendedor;
import ar.com.hipotecario.api.vendedores.model.VendedorResponse;
import ar.com.hipotecario.api.vendedores.model.VendedorResponse.Error;
import ar.com.hipotecario.api.vendedores.model.VendedorResult;
import ar.com.hipotecario.api.vendedores.model.WSGatewayQuery;
import ar.com.hipotecario.api.vendedores.model.core.Table;
import ar.com.hipotecario.api.vendedores.model.core.VendedorBeanN2;
import ar.com.hipotecario.api.vendedores.model.core.VendedorBeanN3;
import ar.com.hipotecario.api.vendedores.model.core.VendedorBeanN4;
import ar.com.hipotecario.api.vendedores.model.core.VendedorDuenio;
import ar.com.hipotecario.api.vendedores.model.core.WOCI;
import ar.com.hipotecario.config.exception.BusinessFuncionalException;
import ar.com.hipotecario.config.exception.ErrorResponseFactory;
import ar.com.hipotecario.config.exception.ErrorTranslatorException;
import ar.com.hipotecario.config.exception.TechnicalException;
import ar.com.hipotecario.rest.client.BHRestClient;
import ar.com.hipotecario.rest.client.BHRestException;
import ar.com.hipotecario.rest.spgateway.client.ClientSPGateway;
import ar.com.hipotecario.rest.spgateway.client.exception.ErrorResponseException;
import ar.com.hipotecario.rest.spgateway.model.Param;
import ar.com.hipotecario.rest.spgateway.model.SPGatewayBean;
import ar.com.hipotecario.utils.bean.HipotecarioBeanUtils;

@Service("vendedoresService")
public class VendedoresService {

	private static final String NIVEL_2 = "2";
	private static final String NIVEL_3 = "3";
	private static final String NIVEL_4 = "4";
	//private static final Integer CANT_REG = 20;

	private static BHRestClient bhRestClient;
	private String urlWOCI;
	private ClientSPGateway campanias;

	@Autowired
	public void setBhRestClient(BHRestClient bhRestClient) {
		this.bhRestClient = bhRestClient;
	}

	@Autowired
	private void Campanias(@Value("${api.cmp-campanias.url:null}") String url_string) { 
		this.campanias = new ClientSPGateway(url_string);
	}

	@Autowired
	private void setWociUrl(@Value("${api.cmp-woci.url:null}") String url_string) {
		this.urlWOCI = url_string;
	}
	
	ObjectMapper objectMapper = new ObjectMapper();	{
		objectMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
	}

	public ResponseEntity<VendedorResponse> insertar(Map<String, String> header, Vendedor vendedor, String nivel) { 
		VendedorResponse resultInsert = new VendedorResponse();
		ResponseEntity<Object> result = null;
		WSGatewayQuery wsGatewayQuery = null;		
		
		if (nivel.equals(NIVEL_2)) {
			wsGatewayQuery = new WSGatewayQuery("WS_CANAL_VENTA_N2");
			wsGatewayQuery.put("OPERACION", "I");
			wsGatewayQuery.put("COD_CANAL_VTA1", String.valueOf(vendedor.getIdSecuencial1()));
			if(vendedor.getIdCobis2() != null)
				wsGatewayQuery.put("COD_CANAL_VTA2", String.valueOf(vendedor.getIdCobis2()));
			wsGatewayQuery.put("CANAL_DESCRIPCION", vendedor.getDescCanalVenta());
			wsGatewayQuery.put("ESTADO", vendedor.getEstado());
		} else if (nivel.equals(NIVEL_3)) {
			wsGatewayQuery = new WSGatewayQuery("WS_CANAL_VENTA_N3");
			wsGatewayQuery.put("OPERACION", "I");
			wsGatewayQuery.put("COD_CANAL_VTA1", String.valueOf(vendedor.getIdSecuencial1()));
			wsGatewayQuery.put("COD_CANAL_VTA2", String.valueOf(vendedor.getIdCobis2()));
			if(vendedor.getIdCobis3() != null)
				wsGatewayQuery.put("COD_CANAL_VTA3",String.valueOf(vendedor.getIdCobis3()));
			wsGatewayQuery.put("CANAL_DESCRIPCION", vendedor.getDescCanalVenta());
			wsGatewayQuery.put("ESTADO", vendedor.getEstado());
		} else if (nivel.equals(NIVEL_4)) {
			wsGatewayQuery = new WSGatewayQuery("WS_VENDEDORES");
			wsGatewayQuery.put("OPERACION", "I");
			wsGatewayQuery.put("COD_CANAL_VTA1", String.valueOf(vendedor.getIdSecuencial1()));
			wsGatewayQuery.put("COD_CANAL_VTA2", String.valueOf(vendedor.getIdCobis2()));
			wsGatewayQuery.put("COD_CANAL_VTA3", String.valueOf(vendedor.getIdCobis3()));
			wsGatewayQuery.put("APELLIDO", vendedor.getApellido());
			wsGatewayQuery.put("NOMBRE", vendedor.getNombre());
			wsGatewayQuery.put("LOGIN", vendedor.getIdFuncionario());	
			wsGatewayQuery.put("ESTADO", vendedor.getEstado());	
		} 
	
		try {
			result = bhRestClient.build(false, false)
					.addHeader(header)
					.post(urlWOCI, 
					wsGatewayQuery.toJson(), Object.class);
			if (result.getStatusCode() != HttpStatus.OK) {
				throw new TechnicalException(ErrorResponseFactory.createError("Error al comunicarse con woci.",
						String.valueOf(result.getStatusCode()), String.valueOf(result.getBody())));
			}
		} catch (BHRestException e) {
			throw new TechnicalException(ErrorResponseFactory.createError("Error: ", e.getMessage(), e.toString()));
		}
		List<VendedorResult> res = null;
		res = objectMapper.convertValue(result.getBody(), new TypeReference<List<VendedorResult>>(){});
		
		if(res.get(0).getResultCode() != 0){
			throw new ErrorTranslatorException(String.valueOf(res.get(0).getResultCode()), 
					"WOCI: " + res.get(0).getResultString());
		}
		
		String secuencialCobis = res.get(0).getTables().get(0).getRecords().get(0).getSecuencial();
		String idCobis = res.get(0).getTables().get(0).getRecords().get(0).getCodigo(); 
		
		if("".equals(secuencialCobis)) {
			throw new ErrorTranslatorException("102", "NO SE PUDO DAR DE ALTA EN COBIS");
		}
		
		List<VendedorDuenio> vendedoresDuenios = new ArrayList<VendedorDuenio>();
		try {
			Param i_operacion = new Param("I", "string");
			Param i_opcion 	  = new Param("0", "integer");
			SPGatewayBean spBean = null;
			if (nivel.equals(NIVEL_2)) {
				Param i_can_vta_niv1 	 = new Param(String.valueOf(vendedor.getIdSecuencial1()), "integer");
				Param i_can_vta_niv2 	 = new Param(idCobis, "integer");
				Param i_des_can_vta_niv2 = new Param(vendedor.getDescCanalVenta(), "string");
				Param i_estado 		 	 = new Param(vendedor.getEstado(), "string");
				Param i_secuencial_niv2  = new Param(secuencialCobis, "integer");
				Map<String, Param> params = new LinkedHashMap<>();
					params.put("i_operacion", 		 i_operacion);
					params.put("i_opcion", 			 i_opcion);
					params.put("i_can_vta_niv1", 	 i_can_vta_niv1);
					params.put("i_can_vta_niv2", 	 i_can_vta_niv2);
					params.put("i_des_can_vta_niv2", i_des_can_vta_niv2);
					params.put("i_estado", 			 i_estado);
					params.put("i_secuencial_niv2",  i_secuencial_niv2);
				spBean = new SPGatewayBean("CampaniasWF", "SpWsCanalVentaNivel2", params, false);
			} else if (nivel.equals(NIVEL_3)) {
				Param i_can_vta_niv1 	 = new Param(String.valueOf(vendedor.getIdSecuencial1()), "integer");
				Param i_can_vta_niv2 	 = new Param(String.valueOf(vendedor.getIdCobis2()), "integer");
				Param i_can_vta_niv3 	 = new Param(((vendedor.getIdCobis3() != null) ? String.valueOf(vendedor.getIdCobis3()) : idCobis), 	"integer");
				Param i_des_can_vta_niv3 = new Param(vendedor.getDescCanalVenta(), "string");
				Param i_estado 		 	 = new Param(vendedor.getEstado(), 	"string");
				Param i_secuencial_niv3  = new Param(secuencialCobis, "integer");
				Map<String, Param> params = new LinkedHashMap<>();
					params.put("i_operacion", 		 i_operacion);
					params.put("i_opcion", 			 i_opcion);
					params.put("i_can_vta_niv1", 	 i_can_vta_niv1);
					params.put("i_can_vta_niv2", 	 i_can_vta_niv2);
					params.put("i_can_vta_niv3", 	 i_can_vta_niv3);
					params.put("i_des_can_vta_niv3", i_des_can_vta_niv3);
					params.put("i_estado", 			 i_estado);
					params.put("i_secuencial_niv3",  i_secuencial_niv3);
				spBean = new SPGatewayBean("CampaniasWF", "SpWsCanalVentaNivel3", params, false);
			} else if (nivel.equals(NIVEL_4)) {
				Param i_can_vta_niv1 = new Param(String.valueOf(vendedor.getIdSecuencial1()), "integer");
				Param i_can_vta_niv2 = new Param(String.valueOf(vendedor.getIdCobis2()), "integer");
				Param i_can_vta_niv3 = new Param(String.valueOf(vendedor.getIdCobis3()), "integer");
				Param i_vendedor	 = new Param(idCobis, "integer");
				Param i_ape_vendedor = new Param(vendedor.getApellido(), "string");
				Param i_nom_vendedor = new Param(vendedor.getNombre(), "string");
				Param i_funcionario  = new Param(vendedor.getIdFuncionario() != null ? vendedor.getIdFuncionario() : null ,"string");
				Param i_estado 		 = new Param(vendedor.getEstado(), "string");
				Param i_cbu 		 = new Param(vendedor.getCbu() != null ? vendedor.getCbu() : null , "string");
				Map<String, Param> params = new LinkedHashMap<>();
					params.put("i_operacion", 	 i_operacion);
					params.put("i_opcion", 		 i_opcion);
					params.put("i_can_vta_niv1", i_can_vta_niv1);
					params.put("i_can_vta_niv2", i_can_vta_niv2);
					params.put("i_can_vta_niv3", i_can_vta_niv3);
					params.put("i_vendedor", 	 i_vendedor);
					params.put("i_ape_vendedor", i_ape_vendedor);
					params.put("i_nom_vendedor", i_nom_vendedor);
					params.put("i_funcionario",  i_funcionario);
					params.put("i_estado", 		 i_estado);
					params.put("i_cbu", 		 i_cbu);
				spBean = new SPGatewayBean("CampaniasWF", "SpWsVendedores", params, false);
			}
			vendedoresDuenios = campanias.callsp(header, spBean, VendedorDuenio.class);
		} catch (BHRestException | HttpServerErrorException | ArrayIndexOutOfBoundsException e) {
			this.buildError(nivel, vendedor, res, e.getMessage().toString(), "insert");
		} catch (ErrorResponseException e) {
			/*if(e.getCodigo().equals("358047"))
				throw new BusinessFuncionalException(ErrorResponseFactory.createEmptyError());
			throw new ErrorTranslatorException(e.getCodigo(), e.getDescripcion());*/
			this.buildError(nivel, vendedor, res, e.getMessage().toString(), "insert");
		}

		/**
		 * Armado Json Salida por Nivel
		 */
		Vendedor insert = new Vendedor();
		if (nivel.equals(NIVEL_2)) {		
			insert.setCodCliente(vendedor.getCodCliente());
			insert.setDescCanalVenta(vendedor.getDescCanalVenta());
			insert.setEstado(vendedor.getEstado());
			insert.setIdCobis2(Integer.valueOf(res.get(0).getTables().get(0).getRecords().get(0).getCodigo()));
			insert.setIdSecuencial1(vendedor.getIdSecuencial1());
			insert.setIdSecuencial2(Integer.valueOf(res.get(0).getTables().get(0).getRecords().get(0).getSecuencial()));
		} else if (nivel.equals(NIVEL_3)) {
			insert.setIdSecuencial1(vendedor.getIdSecuencial1());
			insert.setIdCobis3(Integer.valueOf(res.get(0).getTables().get(0).getRecords().get(0).getCodigo()));
			insert.setIdSecuencial3(Integer.valueOf(res.get(0).getTables().get(0).getRecords().get(0).getSecuencial()));
			insert.setEstado(vendedor.getEstado());
			insert.setDescCanalVenta(vendedor.getDescCanalVenta());
			insert.setIdCobis2(vendedor.getIdCobis2());
			insert.setIdSecuencial2(vendedor.getIdSecuencial2());
		} else if (nivel.equals(NIVEL_4)) {
			insert.setIdSecuencial1(Integer.valueOf(vendedor.getIdSecuencial1()));
			insert.setIdCobis2(vendedor.getIdCobis2());
			insert.setIdCobis3(vendedor.getIdCobis3());
			insert.setApellido(vendedor.getApellido());
			insert.setNombre(vendedor.getNombre());
			insert.setEstado(vendedor.getEstado());
			insert.setIdFuncionario(vendedor.getIdFuncionario());
			insert.setIdVendedor(idCobis);
			insert.setCbu(vendedor.getCbu());
		}
		resultInsert.setResult(Arrays.asList(insert));
		
		return new ResponseEntity<VendedorResponse>(resultInsert, HttpStatus.OK);
	}


	public ResponseEntity<VendedorResponse> actualizar(Map<String, String> header, Vendedor vendedor, String nivel) {
		VendedorResponse resultUpdate = new VendedorResponse();
		ResponseEntity<Object> result = null;
		WSGatewayQuery wsGatewayQuery = null;
		if (nivel.equals(NIVEL_2)) {
			wsGatewayQuery = new WSGatewayQuery("WS_CANAL_VENTA_N2");
			wsGatewayQuery.put("OPERACION", "U");
			wsGatewayQuery.put("SECUENCIAL", String.valueOf(vendedor.getIdSecuencial2()));
			wsGatewayQuery.put("CANAL_DESCRIPCION", vendedor.getDescCanalVenta());
			wsGatewayQuery.put("ESTADO", vendedor.getEstado());
			if(vendedor.getCodCliente() != null)
				wsGatewayQuery.put("ENTE",  String.valueOf(vendedor.getCodCliente()));
		} else if (nivel.equals(NIVEL_3)) {
			wsGatewayQuery = new WSGatewayQuery("WS_CANAL_VENTA_N3");
			wsGatewayQuery.put("OPERACION", "U");
			wsGatewayQuery.put("SECUENCIAL", String.valueOf(vendedor.getIdSecuencial3()));
			wsGatewayQuery.put("CANAL_DESCRIPCION", vendedor.getDescCanalVenta());
			wsGatewayQuery.put("ESTADO", vendedor.getEstado());
			wsGatewayQuery.put("COD_CANAL_VTA1", String.valueOf(vendedor.getIdSecuencial1()));
			wsGatewayQuery.put("COD_CANAL_VTA2", String.valueOf(vendedor.getIdCobis2()));
			wsGatewayQuery.put("COD_CANAL_VTA3", String.valueOf(vendedor.getIdCobis3()));
		} else if (nivel.equals(NIVEL_4)) {
			wsGatewayQuery = new WSGatewayQuery("WS_VENDEDORES");
			wsGatewayQuery.put("OPERACION", "U");
			wsGatewayQuery.put("COD_CANAL_VTA1", String.valueOf(vendedor.getIdSecuencial1()));
			wsGatewayQuery.put("COD_CANAL_VTA2", String.valueOf(vendedor.getIdCobis2()));
			wsGatewayQuery.put("COD_CANAL_VTA3", String.valueOf(vendedor.getIdCobis3()));
			wsGatewayQuery.put("COD_VENDEDOR", vendedor.getIdVendedor());
			wsGatewayQuery.put("APELLIDO", vendedor.getApellido());
			wsGatewayQuery.put("NOMBRE", vendedor.getNombre());
			wsGatewayQuery.put("LOGIN", vendedor.getIdFuncionario());
			wsGatewayQuery.put("ESTADO", vendedor.getEstado());
		}

		
		try {
			result = bhRestClient.build(false, false)
					.addHeader(header)
					.post(urlWOCI,
					wsGatewayQuery.toJson(), Object.class);
			if (result.getStatusCode() != HttpStatus.OK) {
				throw new TechnicalException(ErrorResponseFactory.createError("Error al comunicarse con woci.",
						String.valueOf(result.getStatusCode()), String.valueOf(result.getBody())));
			}
		} catch (BHRestException e) {
			throw new TechnicalException(ErrorResponseFactory.createError("Error: ", e.getMessage(), e.toString()));
		}
		List<VendedorResult> res = null;
		res = objectMapper.convertValue(result.getBody(), new TypeReference<List<VendedorResult>>(){});
		
		if(res.get(0).getResultCode() != 0){
			throw new ErrorTranslatorException(String.valueOf(res.get(0).getResultCode()), 
					"WOCI: " + res.get(0).getResultString());
		}
		
		List<VendedorDuenio> vendedoresDuenios = new ArrayList<VendedorDuenio>();
		
		try {
			Param i_operacion = new Param("U", "string");
			Param i_opcion 	  = new Param("0", "integer");
			SPGatewayBean spBean = null;
			if (nivel.equals(NIVEL_2)) {
				Param i_can_vta_niv1 	 = new Param(String.valueOf(vendedor.getIdSecuencial1()), "integer");
				Param i_can_vta_niv2 	 = new Param(String.valueOf(vendedor.getIdCobis2()), "integer");
				Param i_des_can_vta_niv2 = new Param(vendedor.getDescCanalVenta(), "string");
				Param i_estado 		 	 = new Param(vendedor.getEstado(), "string");
				Param i_secuencial_niv2  = new Param(String.valueOf(vendedor.getIdSecuencial2()), "integer");
				Map<String, Param> params = new LinkedHashMap<>();
					params.put("i_operacion", 		 i_operacion);
					params.put("i_opcion", 			 i_opcion);
					params.put("i_can_vta_niv1", 	 i_can_vta_niv1);
					params.put("i_can_vta_niv2", 	 i_can_vta_niv2);
					params.put("i_des_can_vta_niv2", i_des_can_vta_niv2);
					params.put("i_estado", 			 i_estado);
					params.put("i_secuencial_niv2",  i_secuencial_niv2);
				spBean = new SPGatewayBean("CampaniasWF", "SpWsCanalVentaNivel2", params, false);
			}else if (nivel.equals(NIVEL_3)) {
				Param i_can_vta_niv1 	 = new Param(String.valueOf(vendedor.getIdSecuencial1()), "integer");
				Param i_can_vta_niv2 	 = new Param(String.valueOf(vendedor.getIdCobis2()), "integer");
				Param i_can_vta_niv3 	 = new Param(String.valueOf(vendedor.getIdCobis3()), "integer");
				Param i_des_can_vta_niv3 = new Param(vendedor.getDescCanalVenta(), "string");
				Param i_estado 		 	 = new Param(vendedor.getEstado(), 	"string");
				Param i_secuencial_niv3  = new Param(String.valueOf(vendedor.getIdSecuencial3()), "integer");
				Map<String, Param> params = new LinkedHashMap<>();
					params.put("i_operacion", 		 i_operacion);
					params.put("i_opcion", 			 i_opcion);
					params.put("i_can_vta_niv1", 	 i_can_vta_niv1);
					params.put("i_can_vta_niv2", 	 i_can_vta_niv2);
					params.put("i_can_vta_niv3", 	 i_can_vta_niv3);
					params.put("i_des_can_vta_niv3", i_des_can_vta_niv3);
					params.put("i_estado", 			 i_estado);
					params.put("i_secuencial_niv3",  i_secuencial_niv3);
				spBean = new SPGatewayBean("CampaniasWF", "SpWsCanalVentaNivel3", params, false);
			}  else if (nivel.equals(NIVEL_4)) {
				Param i_can_vta_niv1 	 = new Param(String.valueOf(vendedor.getIdSecuencial1()), "integer");
				Param i_can_vta_niv2 	 = new Param(String.valueOf(vendedor.getIdCobis2()), "integer");
				Param i_can_vta_niv3 	 = new Param(String.valueOf(vendedor.getIdCobis3()), "integer");
				Param i_vendedor 	 	 = new Param(vendedor.getIdVendedor(), "integer");
				Param i_ape_vendedor 	 = new Param(vendedor.getApellido(), "string");
				Param i_nom_vendedor	 = new Param(vendedor.getNombre(), "string");
				Param i_funcionario  	 = new Param(vendedor.getIdFuncionario() != null ? vendedor.getIdFuncionario() : null,  "string");
				Param i_estado 		 	 = new Param(vendedor.getEstado(), "string");
				Param i_cbu              = new Param(vendedor.getCbu() != null ? vendedor.getCbu() : null, "String");
				Map<String, Param> params = new LinkedHashMap<>();
					params.put("i_operacion", 	 i_operacion);
					params.put("i_opcion", 		 i_opcion);
					params.put("i_can_vta_niv1", i_can_vta_niv1);
					params.put("i_can_vta_niv2", i_can_vta_niv2);
					params.put("i_can_vta_niv3", i_can_vta_niv3);
					params.put("i_vendedor", 	 i_vendedor);
					params.put("i_ape_vendedor", i_ape_vendedor);
					params.put("i_nom_vendedor", i_nom_vendedor);
					params.put("i_funcionario",  i_funcionario);
					params.put("i_estado", 		 i_estado);
					params.put("i_cbu", 		 i_cbu);
				spBean = new SPGatewayBean("CampaniasWF", "SpWsVendedores", params, false);
			}
			vendedoresDuenios = campanias.callsp(header, spBean, VendedorDuenio.class);
		} catch (BHRestException e) {
			this.buildError(nivel, vendedor, res, e.getMessage().toString(), "update");
		} catch (ErrorResponseException e) {
			/*if(e.getCodigo().equals("358047"))
				throw new BusinessFuncionalException(ErrorResponseFactory.createEmptyError());
			throw new ErrorTranslatorException(e.getCodigo(), e.getDescripcion());*/
			this.buildError(nivel, vendedor, res, e.getMessage().toString(), "update");
		}
			
		resultUpdate.setResult(Arrays.asList(vendedor));
		return new ResponseEntity<VendedorResponse>(resultUpdate, HttpStatus.OK);
	}
	

	public List<Vendedor> consulta(Map<String, String> header, String nivel, String param1, String param2, String param3) throws Exception {
		Boolean existCobis = true;
		Boolean existCampanias = true;
		List<Vendedor> vendedores = new ArrayList<Vendedor>();
		TypeReference<?> type = null;
		WOCI<?> wociList = null;
		ResponseEntity<Object> result = null;
		WSGatewayQuery wsGatewayQuery = null;
		if (nivel.equals(NIVEL_2)) {
			wsGatewayQuery = new WSGatewayQuery("WS_CANAL_VENTA_N2");
			type = new TypeReference<WOCI<VendedorBeanN2>>() {};
		} else if (nivel.equals(NIVEL_3)) {
			wsGatewayQuery = new WSGatewayQuery("WS_CANAL_VENTA_N3");
			type = new TypeReference<WOCI<VendedorBeanN3>>() {};
		} else if (nivel.equals(NIVEL_4)) {
			wsGatewayQuery = new WSGatewayQuery("WS_VENDEDORES");
			type = new TypeReference<WOCI<VendedorBeanN4>>() {};
		}

		wsGatewayQuery.put("OPERACION", "S");
		if (nivel.equals(NIVEL_2) || nivel.equals(NIVEL_3)) {
			wsGatewayQuery.put("OPCION", "0");
			wsGatewayQuery.put("COD_CANAL_VTA1", param1);
			wsGatewayQuery.put("COD_CANAL_VTA2", param2);
			wsGatewayQuery.put("COD_CANAL_VTA3", param3);
		}
		if (nivel.equals(NIVEL_4)) {
			wsGatewayQuery.put("COD_VENDEDOR", param1);
		}
		
		/**
		 * Ciclo FOR llamando al SP para obtener la totalidad de los registros para la consulta 
		 * en COBIS, mediante los conectores de WOCI. 
		 * Necesario ya que el SP retorna de a 20 registros
		 */
//		boolean ultimaLectura = Boolean.FALSE;
//		while (ultimaLectura==Boolean.FALSE) {
			try {
				result = bhRestClient.build(false, false)
						.addHeader(header)
						.post(urlWOCI, 
						wsGatewayQuery.toJson(), Object.class);
				if (result.getStatusCode() != HttpStatus.OK) {
					throw new TechnicalException(ErrorResponseFactory.createError("Error al comunicarse con woci.",	
							String.valueOf(result.getStatusCode()), String.valueOf(result.getBody())));
				} else {
					try {
						List<?> body = (List<?>) result.getBody();
						wociList = objectMapper.readValue(objectMapper.writeValueAsString(body.get(0)), type);
						
					/*	if ((wociList.getResultCode() == 0 && null == wociList.getTables()
								|| wociList.getResultCode().equals(701025))
								|| wociList.getResultCode() == 0 && wociList.getTables().isEmpty())
							return vendedores;//TODO validar este comportamiento*/
						
						if (wociList.getResultCode() != 0)
							throw new ErrorTranslatorException(String.valueOf(wociList.getResultCode()), 
									wociList.getResultString());

						for (Table<?> t : wociList.getTables()) {
							if (t != null) {
								for (Object o : t.getRecords()) {
									Vendedor ve = new Vendedor();
									HipotecarioBeanUtils.copyPropertiesNotNull(ve, o);
									vendedores.add(ve);
								}
							}

							/**
							 * Control de lecturas registros obtenidos
							 */
//							Integer size = t.getRecords().size();
//							ultimaLectura = size<CANT_REG ? Boolean.TRUE : Boolean.FALSE;
						}
						
						if(vendedores.isEmpty()) {
							existCobis = false;
							vendedores.add(new Vendedor());
						}

						/**
						 * Los siguientes llamados al SP requieren estos cambios al wsGatewayQuery
						 */
//						String ultimoSec = vendedores.get(vendedores.size()-1).getSecuencial();
//						wsGatewayQuery.put("OPCION", "1");
//						wsGatewayQuery.put("SECUENCIAL", ultimoSec);

					} catch (IOException e) {
						throw new TechnicalException(ErrorResponseFactory.createError("Error al desearilizar Json.", 
								e.getMessage(), e.toString()));
					}
				}
			} catch (BHRestException e) {
				throw new TechnicalException(ErrorResponseFactory.createError("Error: ", 
						e.getMessage(), e.toString()));
			}
//		}

		List<VendedorDuenio> vendedoresDuenios = new ArrayList<VendedorDuenio>();
		try {
			SPGatewayBean spBean = null;
			Param i_operacion 	 = new Param("S", "string");
			Param i_opcion 		 = new Param("0", "integer");
			Param i_can_vta_niv1 = new Param(param1, "integer");
			Param i_can_vta_niv2 = new Param(param2, "integer");
			Param i_can_vta_niv3 = new Param(param3, "integer");			
			Map<String, Param> params = new LinkedHashMap<>();
				params.put("i_operacion", 	 i_operacion);
				params.put("i_opcion", 		 i_opcion);			
			if (nivel.equals(NIVEL_2)) {
				params.put("i_can_vta_niv1", i_can_vta_niv1);
				params.put("i_can_vta_niv2", i_can_vta_niv2);
				params.put("i_can_vta_niv3", i_can_vta_niv3);
				spBean = new SPGatewayBean("CampaniasWF", "SpWsCanalVentaNivel2", params, false);
			} else if (nivel.equals(NIVEL_3)) {
				params.put("i_can_vta_niv1", i_can_vta_niv1);
				params.put("i_can_vta_niv2", i_can_vta_niv2);
				params.put("i_can_vta_niv3", i_can_vta_niv3);
				spBean = new SPGatewayBean("CampaniasWF", "SpWsCanalVentaNivel3", params, false);
			} else if (nivel.equals(NIVEL_4)) {
				params.put("i_can_vta_niv1", new Param(null, "integer"));
				params.put("i_can_vta_niv2", new Param(null, "integer"));
				params.put("i_can_vta_niv3", new Param(null, "integer"));
				params.put("i_vendedor", new Param(param1, "integer"));
				spBean = new SPGatewayBean("CampaniasWF", "SpWsVendedores", params, false);
			}
			vendedoresDuenios = campanias.callsp(header, spBean, VendedorDuenio.class);
		} catch (BHRestException e) {
			throw new TechnicalException(ErrorResponseFactory.createError("Error al comunicarse con cmp-campanias.", e.getMessage(), e.toString()));
		} catch (ErrorResponseException e) {
			if(e.getCodigo().equals("358047"))
				throw new BusinessFuncionalException(ErrorResponseFactory.createEmptyError());
			throw new ErrorTranslatorException(e.getCodigo(), e.getDescripcion());
		}
		vendedoresDuenios.forEach(vd -> {
			if (vd != null) {
				Vendedor v = new Vendedor();
				HipotecarioBeanUtils.copyPropertiesNotNull(v, vd);
				vendedores.add(v);
			}
		});
		
		if(vendedoresDuenios.isEmpty()) {
			existCampanias = false;
			vendedores.add(new Vendedor());
		}
		
		if(existCobis == false && existCampanias == false) {
			vendedores.clear();
		}

		return vendedores;
	}
	
	private ResponseEntity<VendedorResponse> buildError(String nivel, Vendedor vendedor, List<VendedorResult> res, String e, String type) {
		Vendedor ventas= new Vendedor();
		VendedorResponse result = new VendedorResponse();
			
		Error error1 = new Error("Error al comunicarse con cmp-campanias.");
		Error error2 = new Error("Se dio de alta en Cobis pero no en campañas");
		Error error3 = new Error(e);
		
		if("2".equals(nivel)) {
			ventas.setCodCliente(vendedor.getCodCliente());
			ventas.setDescCanalVenta(vendedor.getDescCanalVenta());
			ventas.setEstado(vendedor.getEstado());
			ventas.setIdCobis2(vendedor.getIdCobis2());
			ventas.setIdSecuencial1(vendedor.getIdSecuencial1());
			ventas.setIdSecuencial2(Integer.valueOf(res.get(0).getTables().get(0).getRecords().get(0).getSecuencial()));
			result.setResult(Arrays.asList(ventas));
			if("update".equals(type)) {
				error2.setDescripcion("Se actualizo en Cobis pero no en campañas");
			}
		}
		if("3".equals(nivel)) {
			ventas.setDescCanalVenta(vendedor.getDescCanalVenta());
			ventas.setEstado(vendedor.getEstado());
			ventas.setIdCobis2(vendedor.getIdCobis2());
			ventas.setIdSecuencial1(vendedor.getIdSecuencial1());
			ventas.setIdSecuencial3(Integer.valueOf(res.get(0).getTables().get(0).getRecords().get(0).getSecuencial()));
			ventas.setIdCobis3(Integer.valueOf(res.get(0).getTables().get(0).getRecords().get(0).getCodigo()));
			result.setResult(Arrays.asList(ventas));
			if("update".equals(type)) {
				error2.setDescripcion("Se actualizo en Cobis pero no en campañas");
			}	
		}
		result.setError(Arrays.asList(error1,error2,error3));
		return new ResponseEntity<VendedorResponse>(result, HttpStatus.CONFLICT);
	}


}
