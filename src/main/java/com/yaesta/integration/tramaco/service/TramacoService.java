package com.yaesta.integration.tramaco.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Service;

import com.yaesta.app.mail.MailInfo;
import com.yaesta.app.mail.MailParticipant;
import com.yaesta.app.mail.MailService;
import com.yaesta.app.persistence.entity.Guide;
import com.yaesta.app.persistence.entity.GuideDetail;
import com.yaesta.app.persistence.entity.TramacoZone;
import com.yaesta.app.persistence.entity.YaEstaLog;
import com.yaesta.app.persistence.repository.GuideRepository;
import com.yaesta.app.persistence.repository.TramacoZoneRepository;
import com.yaesta.app.persistence.service.GuideService;
import com.yaesta.app.persistence.service.TableSequenceService;
import com.yaesta.app.persistence.service.YaEstaLogService;
import com.yaesta.app.persistence.vo.TableSequenceResponseVO;
import com.yaesta.app.service.SystemOutService;
import com.yaesta.app.util.SupplierUtil;
import com.yaesta.integration.base.enums.DeliveryEnum;
import com.yaesta.integration.base.util.BaseUtil;
import com.yaesta.integration.datil.json.enums.PagoEnum;
import com.yaesta.integration.tramaco.dto.GuideBeanDTO;
import com.yaesta.integration.tramaco.dto.GuideDTO;
import com.yaesta.integration.tramaco.dto.TramacoAuthDTO;
import com.yaesta.integration.vitex.json.bean.Dimension;
import com.yaesta.integration.vitex.json.bean.ItemComplete;
import com.yaesta.integration.vitex.json.bean.Payment;
import com.yaesta.integration.vitex.json.bean.PriceTag;
import com.yaesta.integration.vitex.json.bean.Total;
import com.yaesta.integration.vitex.json.bean.Transaction;
import com.yaesta.integration.vitex.json.bean.enums.PaymentEnum;
import com.yaesta.integration.vitex.service.OrderVitexService;
import com.yaesta.integration.vitex.bean.SupplierDeliveryInfo;

import dmz.comercial.servicio.cliente.dto.EntityActor;
import dmz.comercial.servicio.cliente.dto.EntityCarga;
import dmz.comercial.servicio.cliente.dto.EntityCargaDestino;
import dmz.comercial.servicio.cliente.dto.EntityContrato;
import dmz.comercial.servicio.cliente.dto.EntityGuia;
import dmz.comercial.servicio.cliente.dto.EntityLocalidad;
import dmz.comercial.servicio.cliente.dto.EntityProducto;
import dmz.comercial.servicio.cliente.dto.EntityServicio;
import dmz.comercial.servicio.cliente.dto.EntradaAutenticarWs;
import dmz.comercial.servicio.cliente.dto.EntradaGenerarGuiaWs;
import dmz.comercial.servicio.cliente.dto.EntradaGenerarPdfWs;
import dmz.comercial.servicio.cliente.dto.EntradaTrackGuiaWs;
import dmz.comercial.servicio.cliente.dto.RespuestaAutenticarWs;
import dmz.comercial.servicio.cliente.dto.RespuestaGenerarGuiaWs;
import dmz.comercial.servicio.cliente.dto.RespuestaGenerarPdfWs;
import dmz.comercial.servicio.cliente.dto.RespuestaTrackGuiaWs;
import dmz.comercial.servicio.cliente.dto.SalidaAutenticarWs;
import dmz.comercial.servicio.cliente.dto.SalidaGenerarGuiaWs;
import dmz.comercial.servicio.cliente.dto.SalidaGenerarPdfWs;
import dmz.comercial.servicio.cliente.dto.SalidaTrackGuiaWs;
import dmz.comercial.servicio.cliente.impl.ServicioAutenticar;
import dmz.comercial.servicio.cliente.impl.ServicioGenerarGuias;
import dmz.comercial.servicio.cliente.impl.ServicioGenerarPdf;
import dmz.comercial.servicio.cliente.impl.ServicioTrackingGuia;


@Service
public class TramacoService implements Serializable {

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 8403748444468007479L;

	@Autowired
	PropertySourcesPlaceholderConfigurer propertyConfigurer;

	@Autowired
	TableSequenceService tableSequenceService;

	@Autowired
	TramacoZoneRepository tramacoZoneRepository;

	@Autowired
	YaEstaLogService logService;

	@Autowired
	GuideService guideService;

	@Autowired
	GuideRepository guideRepository;

	@Autowired
	OrderVitexService orderVitexService;

	@Autowired
	private MailService mailService;

	@Autowired
	private SystemOutService systemOut;

	private @Value("${tramaco.url}") String tramacoUrl;
	private @Value("${tramaco.port}") String tramacoPort;
	private @Value("${tramaco.user}") String tramacoUser;
	private @Value("${tramaco.password}") String tramacoPassword;
	private @Value("${tramaco.pdf.path}") String tramacoPdfPath;
	private @Value("${tramaco.default.document}") String tramacoDefaultDocument;
	private @Value("${tramaco.maxsequence.warning}") String tramacoMaxSequenceWarning;
	private @Value("${yaesta.ruc}") String yaestaRuc;
	private @Value("${datil.iva.value}") String datilIvaValue;
	private @Value("${datil.iva.percent.value}") String datilIvaPercentValue;
	private @Value("${mail.smtp.from}") String mailFrom;
	private @Value("${mail.smtp.to}") String mailTo;
	private @Value("${mail.smtp.to.name}") String mailToName;
	private @Value("${mail.text.sequence.tramaco}") String mailTextSequenceTramaco;
	private @Value("${mail.text.sequence.token}") String mailTextSequenceToken;

	public TramacoAuthDTO authService() {

		TramacoAuthDTO tramacoAuth = new TramacoAuthDTO();
		String response = "OK";
		String mensaje = "EXITO";

		systemOut.println("Inicio auhtService");

		try {
			String url = "http://" + tramacoUrl + ":" + tramacoPort + "/";
			// systemOut.println("URL:"+url);
			// systemOut.println("USER:"+tramacoUser);
			// systemOut.println("PASSWORD:"+tramacoPassword);
			ServicioAutenticar client = new ServicioAutenticar(url);

			/**
			 * Datos de entrada
			 */
			EntradaAutenticarWs entAut = new EntradaAutenticarWs();
			entAut.setLogin(tramacoUser);
			entAut.setPassword(tramacoPassword);
			/* Consumo */
			RespuestaAutenticarWs respuestaAutenticarWs = client.autenticarCliente(entAut);

			/**
			 * Datos de salida
			 */
			if (respuestaAutenticarWs != null) {
				if (respuestaAutenticarWs.getCuerpoRespuesta() != null) {
					systemOut.println("CODIGO:" + respuestaAutenticarWs.getCuerpoRespuesta().getCodigo());
					systemOut.println("MENSAJE:" + respuestaAutenticarWs.getCuerpoRespuesta().getMensaje());
					systemOut.println("EXCEPCION:" + respuestaAutenticarWs.getCuerpoRespuesta().getExcepcion());

					if (!respuestaAutenticarWs.getCuerpoRespuesta().getCodigo().equals("1")) {
						response = "ERROR";
					}
					mensaje = respuestaAutenticarWs.getCuerpoRespuesta().getMensaje();
				}
				if (respuestaAutenticarWs.getSalidaAutenticarWs() != null) {
					SalidaAutenticarWs salida = respuestaAutenticarWs.getSalidaAutenticarWs();
					systemOut.println("CONTRATOS:");
					for (EntityContrato contrato : salida.getLstContrato()) {
						systemOut.println("ID:" + contrato.getId() + " NUMERO:" + contrato.getNumero());
						systemOut.println(" PRODUCTOS:");
						for (EntityProducto producto : contrato.getLstProducto()) {
							systemOut.println("ID:" + producto.getId() + " NOMBRE:" + producto.getNombre() + " TIPO:"
									+ producto.getTipo());
						}
						systemOut.println(" SERVICIOS:");
						for (EntityServicio servicio : contrato.getLstServicio()) {
							systemOut.println("ID:" + servicio.getId() + " NOMBRE:" + servicio.getNombre() + " TIPO:"
									+ servicio.getTipo());
						}

					}
					systemOut.println("LOCALIDADES:");
					for (EntityLocalidad localidad : salida.getLstLocalidad()) {
						systemOut.println("ID:" + localidad.getId() + " NOMBRE:" + localidad.getNombre());
					}
					/*
					 * systemOut.println("PRODUCTOS:"); for (EntityProducto
					 * producto : salida.getLstProducto()) {
					 * systemOut.println("ID:" + producto.getId() + " NOMBRE:" +
					 * producto.getNombre() + " TIPO:" + producto.getTipo()); }
					 * systemOut.println("SERVICIOS:"); for (EntityServicio
					 * servicio : salida.getLstServicio()) {
					 * systemOut.println("ID:" + servicio.getId() + " NOMBRE:" +
					 * servicio.getNombre() + " TIPO:" + servicio.getTipo()); }
					 */
				}else{
					response = "ERROR";
					mensaje  = "No se recupera informacion de contratos y servicios activos";
				}
				tramacoAuth.setRespuestaAutenticarWs(respuestaAutenticarWs);
			}

		} catch (Exception e) {

			systemOut.println("Error al consumir servicio tramaco Auth");

			response = "ERROR";
		}

		tramacoAuth.setMessage(mensaje);
		tramacoAuth.setResponse(response);

		return tramacoAuth;
	}

	/**
	 * Servicio para generacion de guias
	 * 
	 * @param guideInfo
	 * @return
	 */
	public GuideDTO generateGuides(GuideDTO guideInfo) {

		try {

			String response = "OK";
			// Autenticar
			TramacoAuthDTO tramacoAuth = authService();
			System.out.println("AUTH RESPONSE 1"+tramacoAuth.getResponse());
			System.out.println("AUTH RESPONSE 2"+tramacoAuth.getRespuestaAutenticarWs().getSalidaAutenticarWs());

			if (response.equals(tramacoAuth.getResponse())) {

				if (tramacoAuth.getRespuestaAutenticarWs() != null
						&& tramacoAuth.getRespuestaAutenticarWs().getSalidaAutenticarWs() != null) {

					String url = "http://" + tramacoUrl + ":" + tramacoPort + "/";
					// Obtener informacion para la guia
					ServicioGenerarGuias cliente = new ServicioGenerarGuias(url);

					// ***Cargar el destinatario al inicio***//
					EntityActor destinatario = new EntityActor();
					destinatario.setApellidos(guideInfo.getOrderComplete().getClientProfileData().getLastName());
					destinatario
							.setCallePrimaria(guideInfo.getOrderComplete().getShippingData().getAddress().getStreet());
					String complemento = guideInfo.getOrderComplete().getShippingData().getAddress().getComplement();

					if (complemento != null) {
						if (complemento.length() > 150) {
							complemento = complemento.substring(0, 149);
						}
						destinatario.setCalleSecundaria(complemento);
					} else {
						destinatario.setCalleSecundaria("");
					}

					if (guideInfo.getOrderComplete().getClientProfileData().getDocument() != null
							&& !guideInfo.getOrderComplete().getClientProfileData().getDocument().equals("")) {
						destinatario.setCiRuc(guideInfo.getOrderComplete().getClientProfileData().getDocument());
						if (guideInfo.getCustomerDocument() != null && !guideInfo.getCustomerDocument().equals("")) {
							destinatario.setCiRuc(guideInfo.getCustomerDocument());
						}
						destinatario.setTipoIden("05");
					} else if (guideInfo.getOrderComplete().getClientProfileData().getIsCorporate()
							&& guideInfo.getOrderComplete().getClientProfileData().getCorporateDocument() != null
							&& !guideInfo.getOrderComplete().getClientProfileData().getCorporateDocument().equals("")) {
						destinatario
								.setCiRuc(guideInfo.getOrderComplete().getClientProfileData().getCorporateDocument());
						if (guideInfo.getCustomerDocument() != null && !guideInfo.getCustomerDocument().equals("")) {
							destinatario.setCiRuc(guideInfo.getCustomerDocument());
						}
						destinatario.setTipoIden("04");
					} else {
						destinatario.setCiRuc(tramacoDefaultDocument);
						destinatario.setTipoIden("08");
					}

					if (guideInfo.getOrderComplete().getShippingData().getAddress().getCity() != null) {
						String province = guideInfo.getOrderComplete().getShippingData().getAddress().getState()
								.toUpperCase();
						String canton = guideInfo.getOrderComplete().getShippingData().getAddress().getCity()
								.toUpperCase();
						List<TramacoZone> zones = tramacoZoneRepository.findByProvinciaAndCantonAndParroquia(province,
								canton, canton);
						if (zones != null && !zones.isEmpty()) {
							// destinatario.setCodigoPostal(zones.get(0).getCodigo()+"");
							destinatario.setCodigoParroquia(zones.get(0).getCodigo().intValue());
						}

					} else if (guideInfo.getOrderComplete().getShippingData().getAddress().getPostalCode() != null) {
						// destinatario.setCodigoPostal(guideInfo.getOrderComplete().getShippingData().getAddress().getPostalCode());
						destinatario.setCodigoParroquia(new Integer(
								guideInfo.getOrderComplete().getShippingData().getAddress().getPostalCode()));
					} else {
						// destinatario.setCodigoPostal("0");
						destinatario.setCodigoParroquia(0);
					}

					destinatario.setEmail(guideInfo.getOrderComplete().getClientProfileData().getEmail());
					destinatario.setNombres(guideInfo.getOrderComplete().getClientProfileData().getFirstName());
					destinatario.setNumero(guideInfo.getOrderComplete().getShippingData().getAddress().getNumber());
					if (guideInfo.getOrderComplete().getShippingData().getAddress().getReference() != null) {
						destinatario.setReferencia(
								guideInfo.getOrderComplete().getShippingData().getAddress().getReference());
					} else {
						destinatario.setReferencia("");
					}
					if (guideInfo.getOrderComplete().getClientProfileData().getPhone() != null) {
						destinatario.setTelefono(guideInfo.getOrderComplete().getClientProfileData().getPhone());
					} else {
						destinatario.setTelefono("         ");
					}

					String formaPago = "N/A";
					if (guideInfo.getOrderComplete().getPaymentData().getTransactions() != null
							&& !guideInfo.getOrderComplete().getPaymentData().getTransactions().isEmpty()) {
						for (Transaction tr : guideInfo.getOrderComplete().getPaymentData().getTransactions()) {
							if (tr.getPayments() != null && !tr.getPayments().isEmpty()) {
								for (Payment py : tr.getPayments()) {
									formaPago = py.getPaymentSystemName();
									if (py.getPaymentSystemName()
											.equals(PaymentEnum.PAGO_CONTRA_ENTREGA.getPaymentSystemName())) {
										formaPago = formaPago + ": " + PagoEnum.EFECTIVO.getDescripcionSRI();
									} else if (py.getPaymentSystemName()
											.equals(PaymentEnum.SAFETYPAY.getPaymentSystemName())) {
										formaPago = formaPago + ": " + PagoEnum.TRANSFER_OTRO_BANCO.getDescripcionSRI();
									} else if (py.getPaymentSystemName()
											.equals(PaymentEnum.TRANSFERENCIA_BANCARIA_OTRAS_ENTIDADES
													.getPaymentSystemName())) {
										formaPago = formaPago + ": " + PagoEnum.TRANSFER_OTRO_BANCO.getDescripcionSRI();
									} else if (py.getPaymentSystemName()
											.equals(PaymentEnum.PAYCLUB.getPaymentSystemName())) {
										formaPago = formaPago + ": "
												+ PagoEnum.TARJETA_CREDITO_NACIONAL.getDescripcionSRI();
									} else if (py.getPaymentSystemName()
											.equals(PaymentEnum.TARJETA_ALIA.getPaymentSystemName())) {
										formaPago = formaPago + ": "
												+ PagoEnum.TARJETA_CREDITO_NACIONAL.getDescripcionSRI();
									} else if (py.getPaymentSystemName()
											.equals(PaymentEnum.TARJETA_CREDITO.getPaymentSystemName())) {
										formaPago = formaPago + ": "
												+ PagoEnum.TARJETA_CREDITO_NACIONAL.getDescripcionSRI();
									} else if (py.getPaymentSystemName()
											.equals(PaymentEnum.PAYPAL.getPaymentSystemName())) {
										formaPago = formaPago + ": "
												+ PagoEnum.TARJETA_CREDITO_INTERNACIONAL.getDescripcionSRI();
									}

								} // fin for

							} //
						}
					}

					Double shippingValue = 0D;
					Double partialShipping = 0D;
					Double ivaPartial = 0D;

					for (Total vtot : guideInfo.getOrderComplete().getTotals()) {

						if (vtot.getId().equals("Shipping")) {
							shippingValue = shippingValue + vtot.getValue();
						}
					}

					if (shippingValue > 0) {

						if (guideInfo.getOrderComplete().getSupplierDeliveryInfoList() != null
								&& !guideInfo.getOrderComplete().getSupplierDeliveryInfoList().isEmpty()) {

							partialShipping = shippingValue
									/ guideInfo.getOrderComplete().getSupplierDeliveryInfoList().size();
							partialShipping = BaseUtil.roundValue(partialShipping);
							ivaPartial = BaseUtil.calculateIVA(partialShipping, new Integer(datilIvaValue),
									datilIvaPercentValue);
							partialShipping = partialShipping + ivaPartial;
							partialShipping = BaseUtil.roundValue(partialShipping);
						}
					}

					systemOut.println("PartialShipping " + partialShipping);

					Double deliveryPayment = 0D;
					Boolean hasAdjunto = false;
					if (guideInfo.getOrderComplete().getPaymentData().getTransactions() != null
							&& !guideInfo.getOrderComplete().getPaymentData().getTransactions().isEmpty()) {
						for (Transaction tr : guideInfo.getOrderComplete().getPaymentData().getTransactions()) {
							if (tr.getPayments() != null && !tr.getPayments().isEmpty()) {
								for (Payment py : tr.getPayments()) {
									if (py.getPaymentSystemName().trim().toLowerCase().equals(
											PaymentEnum.PAGO_CONTRA_ENTREGA.getPaymentSystemName().toLowerCase())) {
										hasAdjunto = true;
										deliveryPayment = deliveryPayment + py.getValue();
									}
								} // fin for

							} //
						}
					} // fin payment data

					String observacionText = "Orden: " + guideInfo.getOrderComplete().getOrderId() + " de "
							+ guideInfo.getOrderComplete().getCustomerName() + " "
							+ guideInfo.getOrderComplete().getClientProfileData().getDocument() + " \n ";
					observacionText = observacionText + "Forma de Pago: " + formaPago;

					if (guideInfo.getCustomerAdditionalInfo() != null
							&& !guideInfo.getCustomerAdditionalInfo().equals("")) {
						observacionText = observacionText + "Referencia" + guideInfo.getCustomerAdditionalInfo();
					}

					for (SupplierDeliveryInfo sdi : guideInfo.getOrderComplete().getSupplierDeliveryInfoList()) {

						List<String> errorInfo = SupplierUtil.validateSupplierInfo(sdi.getSupplier());

						// if(errorInfo.isEmpty() && sdi.getSelected() &&
						// sdi.getDelivery()!=null &&
						// sdi.getDelivery().getNemonic().equals(DeliveryEnum.TRAMACO.getNemonic())){
						if (errorInfo.isEmpty() && sdi.getSelected() && guideInfo.getDeliverySelected() != null
								&& guideInfo.getDeliverySelected().getNemonic()
										.equals(DeliveryEnum.TRAMACO.getNemonic())) {

							/**
							 * Datos de entrada
							 */
							EntradaGenerarGuiaWs entGen = new EntradaGenerarGuiaWs();

							// ***********/
							EntityActor remitente = new EntityActor();
							remitente.setNombres(sdi.getSupplier().getName() + " - ");
							remitente.setApellidos(
									sdi.getSupplier().getContactName() + " " + sdi.getSupplier().getContactLastName());
							remitente.setCallePrimaria(sdi.getSupplier().getStreetMain());
							remitente.setCalleSecundaria(sdi.getSupplier().getStreetSecundary());
							remitente.setCiRuc(yaestaRuc);
							if (sdi.getSupplier().getPostalCode() != null) {
								// remitente.setCodigoPostal(sdi.getSupplier().getPostalCode());
								remitente.setCodigoParroquia(new Integer(sdi.getSupplier().getPostalCode()));
							}

							remitente.setEmail(sdi.getSupplier().getContactEmail());

							if (sdi.getSupplier().getStreetNumber() == null) {
								remitente.setNumero("SN");
							} else {
								remitente.setNumero(sdi.getSupplier().getStreetNumber());
							}

							if (sdi.getSupplier().getAddressReference() == null) {
								remitente.setReferencia(sdi.getSupplier().getAddressReference());
							} else {
								remitente.setReferencia("");
							}
							if (sdi.getSupplier().getPhone() != null) {
								remitente.setTelefono(sdi.getSupplier().getPhone());
							} else {
								remitente.setTelefono("         ");
							}
							remitente.setTipoIden("04");

							// *****//
							List<EntityCargaDestino> lstCargaDestino = new ArrayList<>();
							// ................................TRANSACCION
							// 1.........................................//

							EntityCargaDestino entCargaDestino = new EntityCargaDestino();
							entCargaDestino.setId(tableSequenceService.getNextValue("CARGA_DESTINO").intValue());
							// *******//
							EntityCarga carga = new EntityCarga();
							carga.setBultos(sdi.getPackages().intValue());
							Integer contrato = 0;
							Integer producto = 0;
							EntityContrato contratoTramaco = null;
							for (EntityContrato entityContrato : tramacoAuth.getRespuestaAutenticarWs()
									.getSalidaAutenticarWs().getLstContrato()) {
								contrato = entityContrato.getId();
								contratoTramaco = entityContrato;
								if (contrato == 2977) { // 2977 --> Produccion
									break;
								}
							}
							EntityProducto productoTramaco = null;
							for (EntityProducto entityProducto : contratoTramaco.getLstProducto()) {
								producto = entityProducto.getId();
								productoTramaco = entityProducto;
								if (producto == 1) {
									break;
								}
							}

							carga.setContrato(contrato);

							String desc = "";
							Long ite = new Long(1);
							Double itemValue = 0D;
							Double deliveryCost = 0D;
							Double totalValue = 0D;
							Double totalAsegurado = 0D;

							if (hasAdjunto && partialShipping > 0) {
								totalValue = partialShipping;
							}

							List<GuideDetail> detailList = new ArrayList<GuideDetail>();
							for (ItemComplete ic : sdi.getItems()) {
								itemValue = 0D;
								systemOut.println("Ite==>> " + ite);
								ite++;
								Dimension dim = (Dimension) ic.getAdditionalProperties().get("dimension");

								if (dim != null) {
									carga.setAlto(dim.getHeight());
									carga.setAncho(dim.getWidth());
									carga.setLargo(dim.getLength());
									carga.setPeso(dim.getWeight());
								} else {
									carga.setAlto(0D);
									carga.setAncho(0D);
									carga.setLargo(0D);
									carga.setPeso(0D);
								}

								if (sdi.getItemIdentityType() != null
										&& sdi.getItemIdentityType().getNemonic().equals("NOMBREPRODUCTO")) {
									desc = desc + "#Can. " + ic.getQuantity() + " PRO:" + ic.getName() + " _ ";
								} else {
									String[] supplierCodes = SupplierUtil.returnSupplierCode((String) ic.getRefId());
									desc = desc + "#Can. " + ic.getQuantity() + " COD:" + supplierCodes[2] + " _ ";
								}

								if (desc.length() > 200) {
									desc = desc.substring(0, 199);
								}

								carga.setDescripcion(desc);
								carga.setObservacion(observacionText);

								itemValue = itemValue + ic.getPrice() * ic.getQuantity();
								itemValue = (double) Math.round(itemValue * 100) / 100;

								systemOut.println("itemValue 0-> " + itemValue);

								Double discount = 0D;
								Boolean hasTax = Boolean.FALSE;
								if (ic.getPriceTags() != null && !ic.getPriceTags().isEmpty()) {
									for (PriceTag pt : ic.getPriceTags()) {
										if (pt.getName().contains("discount@price")) {
											Double val = pt.getValue();
											if (val < 0) {
												val = val * (-1);
											}
											val = (double) Math.round(val * 100) / 100;
											discount = discount + Math.abs(val);
											// break;
										}
										if (pt.getName().contains("DISCOUNT@MARKETPLACE")) {
											Double val = pt.getValue();
											if (val < 0) {
												val = val * (-1);
											}
											val = (double) Math.round(val * 100) / 100;
											discount = discount + Math.abs(val);
										}
										if (pt.getName().contains("tax@price")) {
											hasTax = Boolean.TRUE;
										}
									}
								} else {
									discount = 0D;
								}

								if (ic.getShippingPrice() != null && hasAdjunto) {
									systemOut.println("shippingPrice " + ic.getShippingPrice());
									carga.setValorCobro(ic.getShippingPrice());
									deliveryCost = deliveryCost + ic.getShippingPrice();
								} else {
									systemOut.println("Sin costo de cobro de envio");
								}
								Double iva = 0D;
								totalAsegurado = totalAsegurado + itemValue;
								totalAsegurado = (double) Math.round(totalAsegurado * 100) / 100;
								itemValue = itemValue - discount;
								itemValue = (double) Math.round(itemValue * 100) / 100;

								systemOut.println("itemValue 1-> " + itemValue);

								if (ic.getTax() > 0) {
									iva = ic.getTax();
								} else {
									if (hasTax) {
										iva = BaseUtil.calculateIVA(itemValue, new Integer(datilIvaValue),
												datilIvaPercentValue);
									}
								}
								if (itemValue > 0) {
									itemValue = itemValue + iva;
								}
								itemValue = (double) Math.round(itemValue * 100) / 100;

								systemOut.println("itemValue 2-> " + itemValue);

								totalValue = totalValue + itemValue;
								totalValue = (double) Math.round(totalValue * 100) / 100;

								systemOut.println("totalValue 1-> " + totalValue);

								if (hasAdjunto && itemValue > 0 || (hasAdjunto && partialShipping > 0)) {
									carga.setAdjuntos(Boolean.TRUE);
									TableSequenceResponseVO codigoAdjunto = getTramacoAdjCode();

									systemOut.println("Codigo Adjunto " + codigoAdjunto.getCode());
									systemOut.println("Valor al Cobro " + totalValue);
									carga.setCodigoAdjunto(codigoAdjunto.getCode());
									carga.setValorCobro(totalValue);

									if (!codigoAdjunto.getInsideLimit()) {
										// Enviar email

										MailInfo mailInfo = prepareMailSequence(codigoAdjunto);
										mailService.sendMailTemplate(mailInfo, "sequenceNotification.vm");
									}

								} else {
									carga.setAdjuntos(Boolean.FALSE);
									systemOut.println("No hay adjunto " + hasAdjunto + " " + itemValue);
								}

								if (hasAdjunto && totalValue > guideInfo.getOrderComplete().getValue().doubleValue()) {
									carga.setValorCobro(guideInfo.getOrderComplete().getValue().doubleValue());
									systemOut.println("Valor a cobrar" + carga.getValorCobro());

								}

								if (productoTramaco != null) {
									carga.setProducto(productoTramaco.getId());
								} else {
									carga.setProducto(contratoTramaco.getLstProducto().get(1).getId());
								}
								carga.setValorAsegurado(totalAsegurado);
								carga.setLocalidad(0);
								carga.setGuia(guideInfo.getOrderComplete().getOrderId());

								GuideDetail guiD = new GuideDetail();
								guiD.setItemName(ic.getName());
								guiD.setOrderVitexId(guideInfo.getOrderComplete().getOrderId());
								guiD.setVitexId(ic.getId());
								guiD.setQuantity(new Long(ic.getQuantity()));
								guiD.setItemValue(itemValue);
								guideInfo.getDetails().add(guiD);
								detailList.add(guiD);

							} // for de items

							systemOut.println("Contrato " + carga.getContrato());
							systemOut.println("Producto " + carga.getProducto());

							entCargaDestino.setCarga(carga);

							// *************//
							List<EntityServicio> lstServicio = new ArrayList<>();
							entCargaDestino.setLstServicio(lstServicio);

							/********/
							entCargaDestino.setDestinatario(destinatario);
							lstCargaDestino.add(entCargaDestino);
							// *******//
							entGen.setRemitente(remitente);
							entGen.setLstCargaDestino(lstCargaDestino);
							entGen.setUsuario(
									tramacoAuth.getRespuestaAutenticarWs().getSalidaAutenticarWs().getUsuario()); // Verificar

							/**/
							RespuestaGenerarGuiaWs respuestaGenerarGuiaWs = cliente.generarGuia(entGen);
							/**
							 * Datos de salida
							 */
							if (respuestaGenerarGuiaWs != null) {
								if (respuestaGenerarGuiaWs.getCuerpoRespuesta() != null) {
									String codigo = "CODIGO:" + respuestaGenerarGuiaWs.getCuerpoRespuesta().getCodigo();
									String mensaje = "MENSAJE:"
											+ respuestaGenerarGuiaWs.getCuerpoRespuesta().getMensaje();
									String excepcion = "EXCEPCION:"
											+ respuestaGenerarGuiaWs.getCuerpoRespuesta().getExcepcion();
									systemOut.println(codigo);
									systemOut.println(mensaje);
									systemOut.println(excepcion);

									String textInfo = codigo + " " + mensaje + " " + excepcion;

									YaEstaLog yaestalog = new YaEstaLog();
									yaestalog.setLogDate(new Date());
									yaestalog.setProcessName("WAYBILL-TRAMACO");
									yaestalog.setTextinfo(guideInfo.getOrderComplete().getOrderId());
									yaestalog.setTextinfo(textInfo);
									yaestalog.setOrderId(guideInfo.getOrderComplete().getOrderId());
									logService.save(yaestalog);

									if (respuestaGenerarGuiaWs.getCuerpoRespuesta().getCodigo() != "1") {
										response = respuestaGenerarGuiaWs.getCuerpoRespuesta().getMensaje();
										errorInfo.add(response);
									}

								}
								if (respuestaGenerarGuiaWs.getSalidaGenerarGuiaWs() != null) {
									SalidaGenerarGuiaWs salida = respuestaGenerarGuiaWs.getSalidaGenerarGuiaWs();
									for (EntityGuia guia : salida.getLstGuias()) {
										systemOut.println("ID:" + guia.getId() + " GUIA:" + guia.getGuia());
									}
								}

								GuideBeanDTO gbd = new GuideBeanDTO();
								gbd.setGuideResponse(respuestaGenerarGuiaWs);
								gbd.setItemValue(itemValue);
								gbd.setDeliveryCost(deliveryCost);
								gbd.setDeliveryPayment(deliveryPayment);
								gbd.setSupplier(sdi.getSupplier());
								gbd.setItemList(sdi.getItems());
								gbd.setDetails(detailList);
								gbd.setHasPayment(hasAdjunto);
								gbd.setTotalValue(totalValue);
								guideInfo.getGuideBeanList().add(gbd);

							}

						} // no hay error en informacion de proveedor
						else {
							systemOut.println("Error en informacion de proveedor " + sdi.getSupplier().getName());
							response = "Error";
							guideInfo.setErrorList(errorInfo);
							for (String e : errorInfo) {
								systemOut.println("Problemas en::" + e);
								YaEstaLog yaestalog = new YaEstaLog();
								yaestalog.setLogDate(new Date());
								yaestalog.setProcessName("WAYBILL-TRAMACO");
								yaestalog.setTextinfo(guideInfo.getOrderComplete().getOrderId());
								yaestalog.setTextinfo(e);
								yaestalog.setOrderId(guideInfo.getOrderComplete().getOrderId());
								logService.save(yaestalog);
							}
						}

						// Por cada proveedor llamar al servicio de guias

					} // for de supplier delivery info

				} // tengo response y tengo salida de auth
				else {
					YaEstaLog yaestalog = new YaEstaLog();
					yaestalog.setLogDate(new Date());
					yaestalog.setProcessName("WAYBILL-TRAMACO");
					yaestalog.setTextinfo(guideInfo.getOrderComplete().getOrderId());
					yaestalog.setTextinfo("Error no se tiene respuesta de autenticacion "+tramacoAuth.getMessage());
					yaestalog.setOrderId(guideInfo.getOrderComplete().getOrderId());
					response = "ERROR";
					logService.save(yaestalog);
				}

			} // response de servicio auth
			else {
				response = tramacoAuth.getResponse();
				YaEstaLog yaestalog = new YaEstaLog();
				yaestalog.setLogDate(new Date());
				yaestalog.setProcessName("WAYBILL-TRAMACO");
				yaestalog.setTextinfo(guideInfo.getOrderComplete().getOrderId());
				yaestalog.setTextinfo("Error no se encuentra autenticado "+tramacoAuth.getMessage());
				yaestalog.setOrderId(guideInfo.getOrderComplete().getOrderId());
				response = "ERROR";
				logService.save(yaestalog);
			}

			guideInfo.setResponse(response);

		} catch (Exception e) {
			systemOut.println("Error en generacion de guias");
			e.printStackTrace();
			guideInfo.setResponse("Error " + e.getMessage());
			YaEstaLog yaestalog = new YaEstaLog();
			yaestalog.setLogDate(new Date());
			yaestalog.setProcessName("WAYBILL-TRAMACO");
			yaestalog.setTextinfo(guideInfo.getOrderComplete().getOrderId());
			yaestalog.setTextinfo("Error " + e.getMessage());
			yaestalog.setOrderId(guideInfo.getOrderComplete().getOrderId());
			guideInfo.setResponse("ERROR");
			logService.save(yaestalog);
		}

		return guideInfo;
	}

	/**
	 * Producir PDF de la GUIA
	 * 
	 * @param guideInfo
	 * @return
	 */
	public GuideDTO generateGuidesPDF(GuideDTO guideInfo) {

		String response = "OK";

		// Autenticar
		TramacoAuthDTO tramacoAuth = authService();

		// guideInfo.getGuideBean().setResponse("OK");

		if (response.equals(tramacoAuth.getResponse())) {

			systemOut.println("Esta Auth");
			String url = "http://" + tramacoUrl + ":" + tramacoPort + "/";

			List<GuideBeanDTO> resultGuideBeanList = new ArrayList<GuideBeanDTO>();
			for (GuideBeanDTO gbd : guideInfo.getGuideBeanList()) {

				String[] guidePart = gbd.getGuide().getVitexDispatcherId().split("%");

				ServicioGenerarPdf cliente = new ServicioGenerarPdf(url);
				/**
				 * Datos de entrada
				 */
				EntradaGenerarPdfWs entGen = new EntradaGenerarPdfWs();

				systemOut.println("URL:" + url);

				entGen.setUsuario(tramacoAuth.getRespuestaAutenticarWs().getSalidaAutenticarWs().getUsuario());
				// ***********/
				List<EntityGuia> lstGuia = new ArrayList<>();

				EntityGuia entityGuia = new EntityGuia(new Integer(guidePart[0]), guidePart[1]);
				systemOut.println("Guia: " + guidePart[1]);
				lstGuia.add(entityGuia);
				entGen.setLstGuia(lstGuia);

				/**/
				RespuestaGenerarPdfWs respuestaGenerarPdfWs = cliente.generarPdf(entGen);
				/**
				 * Datos de salida
				 */
				if (respuestaGenerarPdfWs != null) {
					if (respuestaGenerarPdfWs.getCuerpoRespuesta() != null) {

						String codigo = "CODIGO:" + respuestaGenerarPdfWs.getCuerpoRespuesta().getCodigo();
						String mensaje = "MENSAJE:" + respuestaGenerarPdfWs.getCuerpoRespuesta().getMensaje();
						String excepcion = "EXCEPCION:" + respuestaGenerarPdfWs.getCuerpoRespuesta().getExcepcion();
						systemOut.println(codigo);
						systemOut.println(mensaje);
						systemOut.println(excepcion);

						String textInfo = codigo + " " + mensaje + " " + excepcion;

						YaEstaLog yaestalog = new YaEstaLog();
						yaestalog.setLogDate(new Date());
						yaestalog.setProcessName("WAYBILL-PDF-TRAMACO");
						yaestalog.setTextinfo(guideInfo.getOrderComplete().getOrderId());
						yaestalog.setTextinfo(textInfo);
						yaestalog.setOrderId(guideInfo.getOrderComplete().getOrderId());
						logService.save(yaestalog);

					}
					if (respuestaGenerarPdfWs.getSalidaGenerarPdfWs() != null) {
						systemOut.println("Esta Tiene salida PDF");
						String location = tramacoPdfPath + "/GuiaPdf-" + (new Date()).getTime() + "-"
								+ gbd.getGuide().getVitexDispatcherId() + "-" + gbd.getGuide().getOrderVitexId()
								+ ".pdf";
						systemOut.println("location:" + location);
						SalidaGenerarPdfWs salida = respuestaGenerarPdfWs.getSalidaGenerarPdfWs();
						if (salida.getInStrPfd() != null) {
							// String location = tramacoPdfPath +"/GuiaPdf-" +
							// (new Date()).getTime()+ "-" +
							// guideInfo.getGuide().getVitexDispatcherId() +"-"
							// + guideInfo.getGuide().getOrderVitexId() +
							// ".pdf";
							// systemOut.println("location:"+location);
							FileOutputStream out;
							try {
								out = new FileOutputStream(location);

								try (InputStream is = salida.getInStrPfd()) {

									ByteArrayOutputStream baos = new ByteArrayOutputStream();
									org.apache.commons.io.IOUtils.copy(is, baos);
									InputStream is2 = new ByteArrayInputStream(baos.toByteArray());

									int len;
									// systemOut.println("Hay inputStream");

									byte[] buffer = new byte[4096];
									while ((len = is2.read(buffer)) != -1) {
										// systemOut.println("++"+new
										// String(buffer));
										out.write(buffer, 0, len);
									}
									out.flush();
									out.close();
								}
							} catch (IOException ex) {
								systemOut.println("==>>" + ex);
								ex.printStackTrace();
							}
							gbd.setPdfUrl(location);
							// systemOut.println("location:"+location);
							gbd.setGuidePdfResponse(respuestaGenerarPdfWs);
						} else {
							systemOut.println("salida.getInStrPfd() es nulo");
						}

					} else {
						systemOut.println("NO tienes salida PDF");
					}
				} // if respuesta
				resultGuideBeanList.add(gbd);
			} // fin del for
			guideInfo.setGuideBeanList(resultGuideBeanList);
		} else {
			systemOut.println("NO es AUTH");
			response = tramacoAuth.getResponse();
		}

		guideInfo.setResponse(response);
		return guideInfo;
	}

	/**
	 * Tracking de GUIA
	 * 
	 * @param guideInfo
	 * @return
	 */
	public GuideDTO trackingService(GuideDTO guideInfo) {

		String url = "http://" + tramacoUrl + ":" + tramacoPort + "/";

		String response = "OK";

		// Autenticar
		TramacoAuthDTO tramacoAuth = authService();

		if (response.equals(tramacoAuth.getResponse())) {

			ServicioTrackingGuia cliente = new ServicioTrackingGuia(url);
			/**
			 * Datos de entrada
			 */
			EntradaTrackGuiaWs entTraGui = new EntradaTrackGuiaWs();
			entTraGui.setUsuario(tramacoAuth.getRespuestaAutenticarWs().getSalidaAutenticarWs().getUsuario());
			entTraGui.setGuia(guideInfo.getGuideBean().getGuideDeliveryId());

			/**/
			RespuestaTrackGuiaWs respuestaTrackGuiaWs = cliente.obtenerTracking(entTraGui);
			/**
			 * Datos de salida
			 */
			if (respuestaTrackGuiaWs != null) {
				if (respuestaTrackGuiaWs.getCuerpoRespuesta() != null) {
					String codigo = "CODIGO:" + respuestaTrackGuiaWs.getCuerpoRespuesta().getCodigo();
					String mensaje = "MENSAJE:" + respuestaTrackGuiaWs.getCuerpoRespuesta().getMensaje();
					String excepcion = "EXCEPCION:" + respuestaTrackGuiaWs.getCuerpoRespuesta().getExcepcion();
					systemOut.println(codigo);
					systemOut.println(mensaje);
					systemOut.println(excepcion);
					
					String textInfo = codigo + " " + mensaje + " " + excepcion;

					YaEstaLog yaestalog = new YaEstaLog();
					yaestalog.setLogDate(new Date());
					yaestalog.setProcessName("WAYWILL-TRACKING-TRAMACO");
					yaestalog.setTextinfo(guideInfo.getGuideBean().getGuide().getOrderVitexId());
					yaestalog.setTextinfo(textInfo);
					yaestalog.setOrderId(guideInfo.getGuideBean().getGuide().getOrderVitexId());
					logService.save(yaestalog);

				}
				if (respuestaTrackGuiaWs.getLstSalidaTrackGuiaWs() != null) {
					for (SalidaTrackGuiaWs trackGuia : respuestaTrackGuiaWs.getLstSalidaTrackGuiaWs()) {
						systemOut.println("FECHA_HORA:" + trackGuia.getFechaHora() + " DESCRIPCION:"
								+ trackGuia.getDescripcion());
						
					}

					Guide guide = guideInfo.getGuideBean().getGuide();

					systemOut.println("COORDENADAS:" + respuestaTrackGuiaWs.getTransaccion().getLatitud() + ":"
							+ respuestaTrackGuiaWs.getTransaccion().getLongitud());
					if (respuestaTrackGuiaWs.getTransaccion().getLatitud() != null) {
						guideInfo.setLatitude(respuestaTrackGuiaWs.getTransaccion().getLatitud().toString());
					}
					if (respuestaTrackGuiaWs.getTransaccion().getLongitud() != null) {
						guideInfo.setLongitude(respuestaTrackGuiaWs.getTransaccion().getLongitud().toString());
					}

					if (guide != null) {
						if (respuestaTrackGuiaWs.getTransaccion().getLatitud() != null
								&& respuestaTrackGuiaWs.getTransaccion().getLongitud() != null) {
							guide.setLatitude(respuestaTrackGuiaWs.getTransaccion().getLatitud().toString());
							guide.setLongitude(respuestaTrackGuiaWs.getTransaccion().getLongitud().toString());

							guideRepository.save(guide);
						}
					}

				}
				guideInfo.getGuideBean().setGuideTrackResponse(respuestaTrackGuiaWs);
			}
		} else {
			guideInfo.getGuideBean().setResponse("ERROR");
		}

		return guideInfo;
	}

	private TableSequenceResponseVO getTramacoAdjCode() {

		String code = tableSequenceService.getNextValue("SEQ_TRAMACO_ADJ") + "";

		if (code.length() < 7) {
			code = "0" + code;
		}

		TableSequenceResponseVO response = tableSequenceService.validateMaxValue("SEQ_TRAMACO_ADJ",
				new Long(tramacoMaxSequenceWarning));

		response.setCode(code);

		return response;
	}

	private MailInfo prepareMailSequence(TableSequenceResponseVO tableSequenceResponse) {

		MailInfo mailInfo = new MailInfo();

		MailParticipant sender = new MailParticipant();
		sender.setName("YaEsta.com");
		sender.setEmail(mailFrom);
		mailInfo.setMailSender(sender);

		MailParticipant receiver = new MailParticipant();
		receiver.setName(mailToName);
		receiver.setEmail(mailTo);
		mailInfo.setMailReceiver(receiver);

		String subject = "Notificación de alerta secuencia Tramaco";
		String strMailTextTramaco = mailTextSequenceTramaco;
		strMailTextTramaco = strMailTextTramaco.replace(mailTextSequenceToken,
				tableSequenceResponse.getDiference() + "");
		mailInfo.setGeneralText(strMailTextTramaco);
		mailInfo.setSubject(subject);

		return mailInfo;
	}
}
