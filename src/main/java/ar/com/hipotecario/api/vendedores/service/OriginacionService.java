package ar.com.hipotecario.api.vendedores.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ar.com.hipotecario.api.vendedores.model.Originacion;
import ar.com.hipotecario.api.vendedores.model.OriginacionBean;
import ar.com.hipotecario.config.exception.BusinessFuncionalException;
import ar.com.hipotecario.config.exception.ErrorResponseFactory;
import ar.com.hipotecario.config.exception.ErrorTranslatorException;
import ar.com.hipotecario.config.exception.TechnicalException;
import ar.com.hipotecario.rest.client.BHRestException;
import ar.com.hipotecario.rest.spgateway.client.ClientSPGateway;
import ar.com.hipotecario.rest.spgateway.client.exception.ErrorResponseException;
import ar.com.hipotecario.rest.spgateway.model.Param;
import ar.com.hipotecario.rest.spgateway.model.SPGatewayBean;
import ar.com.hipotecario.utils.bean.HipotecarioBeanUtils;

@Service("originacionService")
public class OriginacionService {
	

	private static final String NIVEL_1 = "1";
	private static final String NIVEL_2 = "2";
	private static final String NIVEL_3 = "3";

	private ClientSPGateway campanias;

	@Autowired
	private void Campanias(@Value("${api.cmp-campanias.url:null}") String url_string) { 
		this.campanias = new ClientSPGateway(url_string);
	}
	
	
	public List<Originacion> insertarOriginacion(Map<String, String> header, Originacion originacion, String nivel) {
		List<Originacion> lista = new ArrayList<Originacion>();
		List<OriginacionBean> listaBean = new ArrayList<OriginacionBean>();
		
			Param i_operacion = new Param("I", "string");
			SPGatewayBean spBean = null;
			if (nivel.equals(NIVEL_1)) {
				Param CO1_Id 		  = new Param(null, "integer");
					String booleano = BooleanUtils.toString(originacion.getVisibilidad(), "1", "0");
				Param CO1_Visibilidad = new Param(booleano, "string");
				Param CO1_Descripcion = new Param(originacion.getDescOriginacion(), "string");
				Param CO1_Abreviatura = new Param(originacion.getAbreviatura(), "string");
				Param CO1_IdRol 	  = new Param(originacion.getIdRol() == null ? null : String.valueOf(originacion.getIdRol()), "integer");
				Param CO1_IdCobis 	  = new Param(originacion.getIdCobis1() == null ? null : String.valueOf(originacion.getIdCobis1()), "integer");
				Map<String, Param> params = new LinkedHashMap<>();
					params.put("i_operacion", 	  i_operacion);
					params.put("CO1_Id", 		  CO1_Id);
					params.put("CO1_Visibilidad", CO1_Visibilidad);
					params.put("CO1_Descripcion", CO1_Descripcion);
					params.put("CO1_Abreviatura", CO1_Abreviatura);
					params.put("CO1_IdRol", 	  CO1_IdRol);
					params.put("CO1_IdCobis", 	  CO1_IdCobis);
				spBean = new SPGatewayBean("CampaniasWF", "SpCanalOriginacionNivel1", params, false);
			} else if (nivel.equals(NIVEL_2)) {
				Param CO2_Id 		  = new Param(null, "integer");
				Param CO2_Descripcion = new Param(originacion.getDescOriginacion(), "string");
				Param CO2_Sucursal 	  = new Param(originacion.getIdSucursal() == null ? null : String.valueOf(originacion.getIdSucursal()), "integer");
				Param CO2_CO1_Id	  = new Param(String.valueOf(originacion.getIdSecuencial1()), "integer");
				Param CO2_IdCobis     = new Param(originacion.getIdCobis2() == null ? null : String.valueOf(originacion.getIdCobis2()), "integer");
				Map<String, Param> params = new LinkedHashMap<>();
					params.put("i_operacion", 	  i_operacion);
					params.put("CO2_Id", 		  CO2_Id);
					params.put("CO2_Descripcion", CO2_Descripcion);
					params.put("CO2_Sucursal", 	  CO2_Sucursal);
					params.put("CO2_CO1_Id", 	  CO2_CO1_Id);
					params.put("CO2_IdCobis", 	  CO2_IdCobis);
				spBean = new SPGatewayBean("CampaniasWF", "SpCanalOriginacionNivel2", params, false);
			} else if (nivel.equals(NIVEL_3)) {
				Param CO3_Id 	   = new Param(originacion.getIdSecuencial3(), "string");
				Param CO3_Nombre   = new Param(originacion.getNombre(), "string");
				Param CO3_Apellido = new Param(originacion.getApellido(), "string");
				Param CO3_CO2_Id   = new Param(String.valueOf(originacion.getIdSecuencial2()), "integer");
				Param CO3_IdCobis  = new Param(originacion.getIdCobis3() ==  null ? null : String.valueOf(originacion.getIdCobis3()), "integer");
				Map<String, Param> params = new LinkedHashMap<>();
					params.put("i_operacion",  i_operacion);
					params.put("CO3_Id", 	   CO3_Id);
					params.put("CO3_Nombre",   CO3_Nombre);
					params.put("CO3_Apellido", CO3_Apellido);
					params.put("CO3_CO2_Id",   CO3_CO2_Id);
					params.put("CO3_IdCobis",  CO3_IdCobis);
				spBean = new SPGatewayBean("CampaniasWF", "SpCanalOriginacionNivel3", params, false);
			}
		try {
			listaBean = campanias.callsp(header, spBean, OriginacionBean.class);
		}
		catch (BHRestException e) {
			throw new TechnicalException(ErrorResponseFactory.createError("Error al comunicarse con cmp-campanias.", e.getMessage(), e.toString()));
		} catch (ErrorResponseException e) {
			if(e.getCodigo().equals("358047"))
				throw new BusinessFuncionalException(ErrorResponseFactory.createEmptyError());
			if("4".equals(e.getDescripcion()))
				throw new ErrorTranslatorException("400", "El idSecuencial3: " + originacion.getIdSecuencial3() + " ya esta registrado" );
			if (nivel.equals(NIVEL_1) || nivel.equals(NIVEL_2)) { 
				String canal_originacion = "CO1";
				String canal_venta = "CV1";
				String id_cobis = "IdCobis1";

				
				if(nivel.equals(NIVEL_2)) {
					canal_originacion = "CO2";
					canal_venta = "CV2";
					id_cobis = "IdCobis2";
				}
				
				if(e.getCodigo().equals("409"))
					throw new ErrorTranslatorException("409", "Error al crear " + canal_originacion + ". No existe registro de " + canal_venta + " correspondiente." );
				if(e.getCodigo().equals("4090"))
					throw new ErrorTranslatorException("409", id_cobis + " no puede contener valores durante el alta de " + canal_originacion + "." );			
					
			}
		}
		listaBean.forEach(lb -> {
			if (lb != null) {
				Originacion o = new Originacion();
				HipotecarioBeanUtils.copyPropertiesNotNull(o, lb);
				lista.add(o);
			}
		});
		return lista;
	}

	
	public List<Originacion> actualizarOriginacion(Map<String, String> header, Originacion originacion, String nivel) {
		List<Originacion> lista = new ArrayList<Originacion>();
		List<OriginacionBean> listaBean = new ArrayList<OriginacionBean>();
		String secuencial =  null;
		try {
			Param i_operacion = new Param("U", "string");
			SPGatewayBean spBean = null;
			if (nivel.equals(NIVEL_1)) {
				secuencial = String.valueOf(originacion.getIdSecuencial1());
				Param CO1_Id = new Param(String.valueOf(originacion.getIdSecuencial1()), "integer");
				Param CO1_Visibilidad = new Param(originacion.getVisibilidad() == null ? null : BooleanUtils.toString(originacion.getVisibilidad(), "1", "0") , "string");
				Param CO1_Descripcion = new Param(originacion.getDescOriginacion(), "string");
				Param CO1_Abreviatura = new Param(originacion.getAbreviatura(), "string");
				Param CO1_IdRol 	  = new Param(originacion.getIdRol() == null ? null : String.valueOf(originacion.getIdRol()), "integer");
				Param CO1_IdCobis 	  = new Param(originacion.getIdCobis1() == null ? null : String.valueOf(originacion.getIdCobis1()), "integer");
				Map<String, Param> params = new LinkedHashMap<>();
					params.put("i_operacion", 	  i_operacion);
					params.put("CO1_Id", 		  CO1_Id);
					params.put("CO1_Visibilidad", CO1_Visibilidad);
					params.put("CO1_Descripcion", CO1_Descripcion);
					params.put("CO1_Abreviatura", CO1_Abreviatura);
					params.put("CO1_IdRol", 	  CO1_IdRol);
					params.put("CO1_IdCobis", 	  CO1_IdCobis);
				spBean = new SPGatewayBean("CampaniasWF", "SpCanalOriginacionNivel1", params, false);
			} else if (nivel.equals(NIVEL_2)) {
				secuencial = String.valueOf(originacion.getIdSecuencial2());
				Param CO2_Id 		  = new Param(String.valueOf(originacion.getIdSecuencial2()), "integer");
				Param CO2_Descripcion = new Param(originacion.getDescOriginacion(), "string");
				Param CO2_Sucursal 	  = new Param(originacion.getIdSucursal() == null ? null : String.valueOf(originacion.getIdSucursal()), "integer");
				Param CO2_CO1_Id	  = new Param(originacion.getIdSecuencial1() == null ? null : String.valueOf(originacion.getIdSecuencial1()), "integer");
				Param CO2_IdCobis	  = new Param(originacion.getIdCobis2() == null ? null : String.valueOf(originacion.getIdCobis2()), "integer");
				Map<String, Param> params = new LinkedHashMap<>();
					params.put("i_operacion", 	  i_operacion);
					params.put("CO2_Id", 		  CO2_Id);
					params.put("CO2_Descripcion", CO2_Descripcion);
					params.put("CO2_Sucursal", 	  CO2_Sucursal);
					params.put("CO2_CO1_Id", 	  CO2_CO1_Id);
					params.put("CO2_IdCobis", 	  CO2_IdCobis);
				spBean = new SPGatewayBean("CampaniasWF", "SpCanalOriginacionNivel2", params, false);
			} else if (nivel.equals(NIVEL_3)) {
				secuencial = String.valueOf(originacion.getIdSecuencial3());
				Param CO3_Id 	   = new Param(originacion.getIdSecuencial3(), "string");
				Param CO3_Nombre   = new Param(originacion.getNombre(), "string");
				Param CO3_Apellido = new Param(originacion.getApellido(), "string");
				Param CO3_CO2_Id   = new Param(originacion.getIdSecuencial2() == null ? null : String.valueOf(originacion.getIdSecuencial2()), "integer");
				Param CO3_IdCobis  = new Param(originacion.getIdCobis3() == null ? null : String.valueOf(originacion.getIdCobis3()), "integer");
				Map<String, Param> params = new LinkedHashMap<>();
					params.put("i_operacion",  i_operacion);
					params.put("CO3_Id", 	   CO3_Id);
					params.put("CO3_Nombre",   CO3_Nombre);
					params.put("CO3_Apellido", CO3_Apellido);
					params.put("CO3_CO2_Id",   CO3_CO2_Id);
					params.put("CO3_IdCobis",  CO3_IdCobis);
				spBean = new SPGatewayBean("CampaniasWF", "SpCanalOriginacionNivel3", params, false);
			}
			listaBean = campanias.callsp(header, spBean, OriginacionBean.class);
		} catch (BHRestException e) {
			throw new TechnicalException(ErrorResponseFactory.createError("Error al comunicarse con cmp-campanias.", e.getMessage(), e.toString()));
		} catch (ErrorResponseException e) {
			if(e.getCodigo().equals("358047"))
				throw new BusinessFuncionalException(ErrorResponseFactory.createEmptyError());
			throw new ErrorTranslatorException(e.getCodigo(), e.getDescripcion());
		}
		listaBean.forEach(lb -> {
			if (lb != null) {
				Originacion o = new Originacion();
				HipotecarioBeanUtils.copyPropertiesNotNull(o, lb);
				lista.add(o);
			}
		});
		
		if(lista == null || lista.isEmpty()) {
			throw new ErrorTranslatorException("100","El secuencial: " + secuencial + " no esta registrado");
		}
		return lista;
	}


	public List<Originacion> consultaOriginacion(Map<String, String> header, String nivel, String idSecuencial) {
		List<Originacion> lista = new ArrayList<Originacion>();
		List<OriginacionBean> listaBean = new ArrayList<OriginacionBean>();
		try {
			Param i_operacion = new Param("S", "string");
			SPGatewayBean spBean = null;
			if (nivel.equals(NIVEL_1)) {
				Param CO1_Id 		  = new Param(idSecuencial, "integer");
				Param CO1_Visibilidad = new Param(null, "string");
				Param CO1_Descripcion = new Param(null, "string");
				Param CO1_Abreviatura = new Param(null, "string");
				Param CO1_IdRol 	  = new Param(null, "integer");
				Map<String, Param> params = new LinkedHashMap<>();
					params.put("i_operacion", 	  i_operacion);
					params.put("CO1_Id", 		  CO1_Id);
					params.put("CO1_Visibilidad", CO1_Visibilidad);
					params.put("CO1_Descripcion", CO1_Descripcion);
					params.put("CO1_Abreviatura", CO1_Abreviatura);
					params.put("CO1_IdRol", 	  CO1_IdRol);
				spBean = new SPGatewayBean("CampaniasWF", "SpCanalOriginacionNivel1", params, false);
			} else if (nivel.equals(NIVEL_2)) {
				Param CO2_Id 		  = new Param(idSecuencial, "integer");
				Param CO2_Descripcion = new Param(null, "string");
				Param CO2_Sucursal 	  = new Param(null, "integer");
				Param CO2_CO1_Id 	  = new Param(null, "integer");
				Map<String, Param> params = new LinkedHashMap<>();
					params.put("i_operacion", 	  i_operacion);
					params.put("CO2_Id", 		  CO2_Id);
					params.put("CO2_Descripcion", CO2_Descripcion);
					params.put("CO2_Sucursal", 	  CO2_Sucursal);
					params.put("CO2_CO1_Id", 	  CO2_CO1_Id);
				spBean = new SPGatewayBean("CampaniasWF", "SpCanalOriginacionNivel2", params, false);
			} else if (nivel.equals(NIVEL_3)) {
				Param CO3_Id 	   = new Param(idSecuencial, "string");
				Param CO3_Nombre   = new Param(null, "string");
				Param CO3_Apellido = new Param(null, "string");
				Param CO3_CO2_Id   = new Param(null, "integer");
				Param CO3_IdCobis  = new Param(null, "integer");
				Map<String, Param> params = new LinkedHashMap<>();
					params.put("i_operacion",  i_operacion);
					params.put("CO3_Id", 	   CO3_Id);
					params.put("CO3_Nombre",   CO3_Nombre);
					params.put("CO3_Apellido", CO3_Apellido);
					params.put("CO3_CO2_Id",   CO3_CO2_Id);
					params.put("CO3_IdCobis",  CO3_IdCobis);
				spBean = new SPGatewayBean("CampaniasWF", "SpCanalOriginacionNivel3", params, false);
			}
			listaBean = campanias.callsp(header, spBean, OriginacionBean.class);
		} catch (BHRestException e) {
			throw new TechnicalException(ErrorResponseFactory.createError("Error al comunicarse con cmp-campanias.", e.getMessage(), e.toString()));
		} catch (ErrorResponseException e) {
			if(e.getCodigo().equals("358047"))
				throw new BusinessFuncionalException(ErrorResponseFactory.createEmptyError());
			throw new ErrorTranslatorException(e.getCodigo(), e.getDescripcion());
		}
		listaBean.forEach(lb -> {
			if (lb != null) {
				Originacion o = new Originacion();
				HipotecarioBeanUtils.copyPropertiesNotNull(o, lb);
				lista.add(o);
			}
		});
		return lista;
	}
}
