package com.yaesta.integration.vitex.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.MultivaluedHashMap;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yaesta.app.mail.ItemInfo;
import com.yaesta.app.mail.MailInfo;
import com.yaesta.app.mail.MailParticipant;
import com.yaesta.app.mail.MailService;
import com.yaesta.app.pdf.BuildGuidePDF;
import com.yaesta.app.pdf.BuildTagGuidePDF;
import com.yaesta.app.pdf.bean.GuideDataBean;
import com.yaesta.app.pdf.bean.ItemData;
import com.yaesta.app.persistence.entity.Catalog;
import com.yaesta.app.persistence.entity.Customer;
import com.yaesta.app.persistence.entity.Guide;
import com.yaesta.app.persistence.entity.GuideDetail;
import com.yaesta.app.persistence.entity.Order;
import com.yaesta.app.persistence.entity.OrderItem;
import com.yaesta.app.persistence.entity.Supplier;
import com.yaesta.app.persistence.entity.SupplierContact;
import com.yaesta.app.persistence.entity.YaEstaLog;
import com.yaesta.app.persistence.repository.SupplierContactRepository;
import com.yaesta.app.persistence.service.CatalogService;
import com.yaesta.app.persistence.service.ClientService;
import com.yaesta.app.persistence.service.GuideService;
import com.yaesta.app.persistence.service.OrderService;
import com.yaesta.app.persistence.service.SupplierService;
import com.yaesta.app.persistence.service.YaEstaLogService;
//import com.yaesta.app.persistence.util.HibernateProxyTypeAdapter;
import com.yaesta.app.persistence.util.YaestaTypeAdapter;
import com.yaesta.app.service.SystemOutService;
import com.yaesta.app.util.SupplierUtil;
import com.yaesta.app.util.UtilDate;
import com.yaesta.integration.base.enums.DeliveryEnum;
import com.yaesta.integration.base.util.BaseUtil;
import com.yaesta.integration.datil.json.bean.Destinatario;
import com.yaesta.integration.datil.json.bean.FacturaConsulta;
import com.yaesta.integration.datil.json.bean.FacturaRespuestaSRI;
import com.yaesta.integration.datil.json.bean.GuiaRemisionRespuesta;
import com.yaesta.integration.datil.json.bean.ItemGuiaRemision;
import com.yaesta.integration.datil.json.enums.PagoEnum;
import com.yaesta.integration.datil.service.DatilService;
import com.yaesta.integration.tcc.service.TccService;
import com.yaesta.integration.tramaco.dto.GuideBeanDTO;
import com.yaesta.integration.tramaco.dto.GuideDTO;
import com.yaesta.integration.tramaco.service.TramacoService;
import com.yaesta.integration.vitex.bean.GuideContainerBean;
import com.yaesta.integration.vitex.bean.GuideInfoBean;
import com.yaesta.integration.vitex.bean.GuideRemisionContainer;
import com.yaesta.integration.vitex.bean.InvoiceSchemaBean;
import com.yaesta.integration.vitex.bean.OrderResponseBean;
import com.yaesta.integration.vitex.bean.OrderSchemaContainerBean;
import com.yaesta.integration.vitex.bean.PaymentBean;
import com.yaesta.integration.vitex.bean.SupplierDeliveryInfo;
import com.yaesta.integration.vitex.bean.WayBillSchema;
import com.yaesta.integration.vitex.json.bean.CategoryVtex;
import com.yaesta.integration.vitex.json.bean.InvoiceResponse;
import com.yaesta.integration.vitex.json.bean.InvoiceSchema;
import com.yaesta.integration.vitex.json.bean.ItemComplete;
import com.yaesta.integration.vitex.json.bean.ItemInvoice;
import com.yaesta.integration.vitex.json.bean.OrderBean;
import com.yaesta.integration.vitex.json.bean.OrderCancel;
import com.yaesta.integration.vitex.json.bean.OrderComplete;
import com.yaesta.integration.vitex.json.bean.OrderConversation;
import com.yaesta.integration.vitex.json.bean.OrderSchema;
import com.yaesta.integration.vitex.json.bean.Paging;
import com.yaesta.integration.vitex.json.bean.Payment;
import com.yaesta.integration.vitex.json.bean.PriceTag;
import com.yaesta.integration.vitex.json.bean.Total;
import com.yaesta.integration.vitex.json.bean.Transaction;
import com.yaesta.integration.vitex.json.bean.enums.PaymentEnum;
import com.yaesta.integration.vitex.service.base.BaseVitexService;
import com.yaesta.integration.vitex.util.OrderVtexUtil;
import com.yaesta.integration.vitex.wsdl.OrderChangeStatus;
import com.yaesta.integration.vitex.wsdl.OrderChangeStatusResponse;
import com.yaesta.integration.vitex.wsdl.OrderGet;
import com.yaesta.integration.vitex.wsdl.OrderGetNext50FromIdV3;
import com.yaesta.integration.vitex.wsdl.OrderGetNext50FromIdV3Response;
import com.yaesta.integration.vitex.wsdl.OrderGetResponse;
import com.yaesta.integration.vitex.wsdl.OrderGetV3;
import com.yaesta.integration.vitex.wsdl.OrderGetV3Response;
import com.yaesta.integration.vitex.wsdl.dto.ArrayOfOrderDTO;
import com.yaesta.integration.vitex.wsdl.dto.OrderDTO;
import com.yaesta.integration.vitex.wsdl.dto.OrderDeliveryDTO;
import com.yaesta.integration.vitex.wsdl.dto.OrderItemDTO;
import com.yaesta.integration.vitex.wsdl.dto.OrderItemDiscountDTO;
import com.yaesta.integration.vitex.wsdl.dto.ProductDTO;
import com.yaesta.integration.vitex.wsdl.vo.ItemDiscountVO;
import com.yaesta.integration.vitex.wsdl.vo.OrderDeliveryVO;
import com.yaesta.integration.vitex.wsdl.vo.OrderItemVO;
import com.yaesta.integration.vitex.wsdl.vo.OrderVO;

@Service
public class OrderVitexService extends BaseVitexService {

	/**
	 * Serial version
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private WebServiceTemplate webServiceTemplate;

	@Autowired
	private ProductVitexService productVitexService;

	@Autowired
	private CategoryVitexService categoryVitexService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private GuideService guideService;

	@Autowired
	private SupplierService supplierService;

	@Autowired
	private SupplierContactRepository supplierContactRepository;

	@Autowired
	private TramacoService tramacoService;

	@Autowired
	TccService tccService;

	@Autowired
	private MailService mailService;

	@Autowired
	private DatilService datilService;

	@Autowired
	private CatalogService catalogService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private YaEstaLogService logService;

	@Autowired
	SystemOutService systemOut;

	private Client client;
	private WebTarget target;

	private @Value("${mail.smtp.from}") String mailFrom;
	private @Value("${mail.smtp.to}") String mailTo;
	private @Value("${mail.smtp.to.name}") String mailToName;
	private @Value("${mail.smtp.cc}") String mailCc;
	private @Value("${mail.smtp.cc.name}") String mailCcName;
	private @Value("${mail.text.guide}") String mailTextGuide;
	private @Value("${mail.text.guide.2}") String mailTextGuide2;
	private @Value("${mail.text.guide.3}") String mailTextGuide3;
	private @Value("${mail.text.guide.4}") String mailTextGuide4;
	private @Value("${mail.text.guide.5}") String mailTextGuide5;
	private @Value("${mail.text.guide.6}") String mailTextGuide6;
	private @Value("${mail.text.guide.7}") String mailTextGuide7;
	private @Value("${mail.text.guide.8}") String mailTextGuide8;
	private @Value("${mail.text.guide.token}") String mailTextGuideToken;
	private @Value("${tramaco.contacts}") String tramacoContacts;
	private @Value("${tramaco.contacts.names}") String tramacoContactsNames;
	private @Value("${tramaco.tracking.url}") String tramacoTrackingUrl;
	private @Value("${datil.iva.value}") String datilIvaValue;
	private @Value("${datil.iva.percent.value}") String datilIvaPercentValue;
	private @Value("${datil.establishment.code}") String datilEstablishmentCode;
	private @Value("${datil.emission.code}") String datilEmissionCode;
	private @Value("${guide.prefix}") String guidePrefix;
	private @Value("${moto.pdf.path}") String motoExpressPdfPath;
	private @Value("${internal.pdf.path}") String internalPdfPath;
	private @Value("${cyclist.pdf.path}") String cyclistPdfPath;
	private @Value("${mail.path.logo.image}") String logoPath;
	private @Value("${mail.image.prefix}") String mailImagePrefix;
	private @Value("${mail.text.guide.customer}") String mailTextGuideCustomer;
	private @Value("${mail.text.guide.customer.2}") String mailTextGuideCustomer2;
	private @Value("${mail.text.guide.customer.3}") String mailTextGuideCustomer3;
	private @Value("${mail.text.guide.customer.4}") String mailTextGuideCustomer4;
	private @Value("${mail.text.guide.customer.5}") String mailTextGuideCustomer5;
	private @Value("${yaesta.log.path}") String yaestaLogPath;
	private @Value("${yaesta.log.prefix}") String yaestaPrefix;
	private @Value("${vitex.rest.maxpages}") String vitexRestMaxPages;
	private @Value("${mail.notify.customer}") String mailNotifyCustomer;
	private @Value("${mail.notify.supplier}") String mailNotifySupplier;
	private @Value("${yaesta.pdf.guide.path}") String yaestaPdfGuidePath;
	private @Value("${yaesta.pdf.guide.prefix}") String yaestaGuidePrefix;
	private @Value("${yaesta.pdf.guide.leyend}") String yaestaGuideLeyend;
	private @Value("${yaesta.pdf.guide.logo}") String yaestaGuideLogo;
	private @Value("${yaesta.address}") String yaestaAddress;

	public OrderVitexService() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	@Bean(name = "propertyConfigurer")
	public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();

		return configurer;
	}

	public ArrayOfOrderDTO getOrders(Integer sequence) {

		OrderGetNext50FromIdV3 query = objectFactory.createOrderGetNext50FromIdV3();
		query.setSequence(sequence);
		OrderGetNext50FromIdV3Response response = (OrderGetNext50FromIdV3Response) webServiceTemplate
				.marshalSendAndReceive(query,
						new SoapActionCallback("http://tempuri.org/IService/OrderGetNext50FromIdV3"));
		return response.getOrderGetNext50FromIdV3Result().getValue();

	}

	public List<OrderVO> getOrdersNext50(Integer sequence) {
		List<OrderVO> result = new ArrayList<OrderVO>();

		ArrayOfOrderDTO array = getOrders(sequence);

		if (array != null) {
			if (array.getOrderDTO() != null && !array.getOrderDTO().isEmpty()) {
				for (OrderDTO odto : array.getOrderDTO()) {
					OrderVO ovo = new OrderVO();
					ovo.setId(odto.getId().getValue().longValue());
					ovo.setVitexId(odto.getId().getValue().longValue() + "");
					ovo.setCity(odto.getAddress().getValue().getCity().getValue());
					ovo.setCost(odto.getCost().getValue());
					ovo.setStore(odto.getStoreName().getValue());
					ovo.setCustomerVitexId(odto.getClientId().getValue() + "");
					ovo.setCustomerFirstName(odto.getClient().getValue().getFirstName().getValue());
					ovo.setCustomerLastName(odto.getClient().getValue().getLastName().getValue());
					ovo.setPurchaseDate(UtilDate.toDate(odto.getPurchaseDate().getValue()));
					if (odto.getOrderDeliveries().getValue().getOrderDeliveryDTO() != null
							&& !odto.getOrderDeliveries().getValue().getOrderDeliveryDTO().isEmpty()) {
						for (OrderDeliveryDTO oddto : odto.getOrderDeliveries().getValue().getOrderDeliveryDTO()) {
							OrderDeliveryVO odvo = new OrderDeliveryVO();
							odvo.setFreightId(oddto.getFreightId().getValue() + "");
							odvo.setFreightName(oddto.getFreightName().getValue());
							odvo.setOrderStatusDescription(
									oddto.getOrderStatus().getValue().getDescription().getValue());
							odvo.setOrderStatusCode(oddto.getOrderStatus().getValue().getId().getValue());
							if (oddto.getDeliveryDate().getValue() != null) {
								odvo.setDeliveryDate(UtilDate.toDate(oddto.getDeliveryDate().getValue()));
							}
							if (oddto.getOrderItems().getValue().getOrderItemDTO() != null
									&& !oddto.getOrderItems().getValue().getOrderItemDTO().isEmpty()) {
								for (OrderItemDTO oidto : oddto.getOrderItems().getValue().getOrderItemDTO()) {
									OrderItemVO oivo = new OrderItemVO();
									oivo.setCost(oidto.getCost().getValue());

									ProductDTO pdto = productVitexService.findById(oidto.getProductId().getValue());

									oivo.setProductRefId(oidto.getProductRefId().getValue());
									oivo.setProductName(pdto.getName().getValue());
									oivo.setProductId(oidto.getProductId().getValue() + "");
									oivo.setProductVitexId(oidto.getProductId().getValue() + "");
									oivo.setShippingCost(oidto.getShippingCost().getValue());
									if (oidto.getDiscounts() != null && oidto.getDiscounts().getValue() != null
											&& oidto.getDiscounts().getValue().getOrderItemDiscountDTO() != null
											&& !oidto.getDiscounts().getValue().getOrderItemDiscountDTO().isEmpty()) {
										oivo.setTotalDiscount(new BigDecimal(0));
										for (OrderItemDiscountDTO oiddto : oidto.getDiscounts().getValue()
												.getOrderItemDiscountDTO()) {
											ItemDiscountVO idvo = new ItemDiscountVO();
											idvo.setValue(oiddto.getValue().getValue());
											idvo.setDescription(oiddto.getDiscountName().getValue());
											BigDecimal tempDiscount = oivo.getTotalDiscount();
											tempDiscount = tempDiscount.add(oiddto.getValue().getValue());
											oivo.setTotalDiscount(tempDiscount);
											oivo.getDiscounts().add(idvo);
										}
									}
									odvo.getItems().add(oivo);

								}
							}
							ovo.getDeliveries().add(odvo);
						}
					}

					result.add(ovo);
				}

			}

		}

		return result;
	}

	public OrderDTO findById(Integer orderId) {
		OrderGet query = objectFactory.createOrderGet();
		query.setOrderId(orderId);
		OrderGetResponse response = (OrderGetResponse) webServiceTemplate.marshalSendAndReceive(query,
				new SoapActionCallback("http://tempuri.org/IService/OrderGet"));
		return response.getOrderGetResult().getValue();

	}

	public OrderDTO findOrderById(String orderId) {
		OrderGetV3 query = objectFactory.createOrderGetV3();
		query.setOrderId(objectFactory.createString(orderId));

		OrderGetV3Response response = (OrderGetV3Response) webServiceTemplate.marshalSendAndReceive(query,
				new SoapActionCallback("http://tempuri.org/IService/OrderGetV3"));
		return response.getOrderGetV3Result().getValue();
	}

	public OrderVO findOrder(String orderId) {
		OrderVO ovo = new OrderVO();
		OrderDTO odto = findOrderById(orderId);
		if (odto != null) {

			ovo.setId(odto.getId().getValue().longValue());
			ovo.setVitexId(odto.getId().getValue().longValue() + "");
			ovo.setCity(odto.getAddress().getValue().getCity().getValue());
			ovo.setCost(odto.getCost().getValue());
			ovo.setStore(odto.getStoreName().getValue());
			ovo.setCustomerVitexId(odto.getClientId().getValue() + "");
			ovo.setCustomerFirstName(odto.getClient().getValue().getFirstName().getValue());
			ovo.setCustomerLastName(odto.getClient().getValue().getLastName().getValue());
			ovo.setPurchaseDate(UtilDate.toDate(odto.getPurchaseDate().getValue()));
			if (odto.getOrderDeliveries().getValue().getOrderDeliveryDTO() != null
					&& !odto.getOrderDeliveries().getValue().getOrderDeliveryDTO().isEmpty()) {
				for (OrderDeliveryDTO oddto : odto.getOrderDeliveries().getValue().getOrderDeliveryDTO()) {
					OrderDeliveryVO odvo = new OrderDeliveryVO();
					odvo.setFreightId(oddto.getFreightId().getValue() + "");
					odvo.setFreightName(oddto.getFreightName().getValue());
					odvo.setOrderStatusDescription(oddto.getOrderStatus().getValue().getDescription().getValue());
					odvo.setOrderStatusCode(oddto.getOrderStatus().getValue().getId().getValue());
					if (oddto.getDeliveryDate().getValue() != null) {
						odvo.setDeliveryDate(UtilDate.toDate(oddto.getDeliveryDate().getValue()));
					}
					if (oddto.getOrderItems().getValue().getOrderItemDTO() != null
							&& !oddto.getOrderItems().getValue().getOrderItemDTO().isEmpty()) {
						for (OrderItemDTO oidto : oddto.getOrderItems().getValue().getOrderItemDTO()) {
							OrderItemVO oivo = new OrderItemVO();
							oivo.setCost(oidto.getCost().getValue());

							ProductDTO pdto = productVitexService.findById(oidto.getProductId().getValue());

							oivo.setProductRefId(oidto.getProductRefId().getValue());
							oivo.setProductName(pdto.getName().getValue());
							oivo.setProductId(oidto.getProductId().getValue() + "");
							oivo.setProductVitexId(oidto.getProductId().getValue() + "");
							oivo.setProductRefId(pdto.getRefId().getValue());
							oivo.setShippingCost(oidto.getShippingCost().getValue());
							if (oidto.getDiscounts().getValue().getOrderItemDiscountDTO() != null
									&& !oidto.getDiscounts().getValue().getOrderItemDiscountDTO().isEmpty()) {
								oivo.setTotalDiscount(new BigDecimal(0));
								for (OrderItemDiscountDTO oiddto : oidto.getDiscounts().getValue()
										.getOrderItemDiscountDTO()) {
									ItemDiscountVO idvo = new ItemDiscountVO();
									idvo.setValue(oiddto.getValue().getValue());
									idvo.setDescription(oiddto.getDiscountName().getValue());
									BigDecimal tempDiscount = oivo.getTotalDiscount();
									tempDiscount = tempDiscount.add(oiddto.getValue().getValue());
									oivo.setTotalDiscount(tempDiscount);
									oivo.getDiscounts().add(idvo);
								}
							}
							odvo.getItems().add(oivo);

						}
					}
					ovo.getDeliveries().add(odvo);
				}
			}

		}

		return ovo;
	}

	public OrderSchema getOrdersRest(String status) {

		OrderSchema response = null;

		client = ClientBuilder.newClient();

		String restUrl = this.vitexRestUrl + "/api/oms/pvt/orders/?per_page=100";

		if (status != null) {
			restUrl = restUrl + "&filter=status:" + status;
		}

		systemOut.println("restUrl::" + restUrl);

		try {

			target = client.target(restUrl);

			MultivaluedMap<String, Object> myHeaders = new MultivaluedHashMap<String, Object>();
			myHeaders.add(vitexRestAppkeyName, vitexRestAppkey);
			myHeaders.add(vitexRestTokenName, vitexRestToken);
			String json = target.request(MediaType.TEXT_PLAIN).headers(myHeaders).get(String.class);

			response = new Gson().fromJson(json, OrderSchema.class);

			ObjectMapper mapper = new ObjectMapper();

			mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Object oJson = mapper.readValue(json, OrderSchema.class);
			String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(oJson);
			String fileName = yaestaLogPath + yaestaPrefix + (new Date()).getTime() + ".txt";
			FileUtils.writeStringToFile(new File(fileName), indented);

		} catch (IOException e) {

			YaEstaLog yaestalog = new YaEstaLog();
			yaestalog.setLogDate(new Date());
			yaestalog.setProcessName("ORDER_FEED-QUERY");
			yaestalog.setTextinfo("Error IO consulta VTEX ordenes " + e.getMessage());
			// yaestalog.setXmlInfo(json);
			logService.save(yaestalog);

			e.printStackTrace();
		} catch (Exception e) {

			YaEstaLog yaestalog = new YaEstaLog();
			yaestalog.setLogDate(new Date());
			yaestalog.setProcessName("ORDER_FEED-QUERY");
			yaestalog.setTextinfo("Error general consulta VTEX ordenes " + e.getMessage());
			// yaestalog.setXmlInfo(json);
			logService.save(yaestalog);

			e.printStackTrace();
		}

		if (response != null && response.getList() != null && !response.getList().isEmpty()) {
			List<OrderBean> list = new ArrayList<OrderBean>();
			for (OrderBean ob : response.getList()) {
				OrderBean obean = OrderVtexUtil.setRealValuesToOrderBean(ob);
				list.add(obean);
			}
			response.setList(list);
		}

		return response;
	}

	public OrderSchema getOrdersRestPage(String status, Long page) {

		client = ClientBuilder.newClient();

		String restUrl = this.vitexRestUrl + "/api/oms/pvt/orders/?per_page=100&page=" + page;

		if (status != null) {
			restUrl = restUrl + "&filter=status:" + status;
		}

		systemOut.println("restUrl::" + restUrl);

		target = client.target(restUrl);

		MultivaluedMap<String, Object> myHeaders = new MultivaluedHashMap<String, Object>();
		myHeaders.add(vitexRestAppkeyName, vitexRestAppkey);
		myHeaders.add(vitexRestTokenName, vitexRestToken);
		String json = target.request(MediaType.TEXT_PLAIN).headers(myHeaders).get(String.class);

		OrderSchema response = new Gson().fromJson(json, OrderSchema.class);

		try {

			ObjectMapper mapper = new ObjectMapper();

			mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Object oJson = mapper.readValue(json, OrderSchema.class);
			String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(oJson);
			String fileName = yaestaLogPath + yaestaPrefix + (new Date()).getTime() + ".txt";
			FileUtils.writeStringToFile(new File(fileName), indented);

		} catch (IOException e) {

			YaEstaLog yaestalog = new YaEstaLog();
			yaestalog.setLogDate(new Date());
			yaestalog.setProcessName("ORDER_FEED-QUERY");
			yaestalog.setTextinfo("Error consulta VTEX ordenes");
			// yaestalog.setXmlInfo(json);
			logService.save(yaestalog);

			e.printStackTrace();
		}

		if (response.getList() != null && !response.getList().isEmpty()) {
			List<OrderBean> list = new ArrayList<OrderBean>();
			for (OrderBean ob : response.getList()) {
				OrderBean obean = OrderVtexUtil.setRealValuesToOrderBean(ob);
				list.add(obean);
			}
			response.setList(list);
		}

		return response;
	}

	public OrderSchema getOrdersRestPaging() {

		client = ClientBuilder.newClient();

		String restUrl = this.vitexRestUrl + "/api/oms/pvt/orders/";

		OrderSchema input = new OrderSchema();
		Paging paging = new Paging();
		paging.setPerPage(50);
		input.setPaging(paging);

		Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
		String jsonInput = gson.toJson(input);
		restUrl = restUrl + "[" + jsonInput + "]";
		systemOut.println("rest" + restUrl);
		target = client.target(restUrl);

		MultivaluedMap<String, Object> myHeaders = new MultivaluedHashMap<String, Object>();
		myHeaders.add(vitexRestAppkeyName, vitexRestAppkey);
		myHeaders.add(vitexRestTokenName, vitexRestToken);
		String json = target.request(MediaType.TEXT_PLAIN).headers(myHeaders).get(String.class);

		OrderSchema response = new Gson().fromJson(json, OrderSchema.class);
		return response;
	}

	public OrderComplete getOrderComplete(String orderId) {

		client = ClientBuilder.newClient();

		String restUrl = this.vitexRestUrl + "/api/oms/pvt/orders/" + orderId;
		target = client.target(restUrl);

		MultivaluedMap<String, Object> myHeaders = new MultivaluedHashMap<String, Object>();
		myHeaders.add(vitexRestAppkeyName, vitexRestAppkey);
		myHeaders.add(vitexRestTokenName, vitexRestToken);
		String json = target.request(MediaType.TEXT_PLAIN).headers(myHeaders).get(String.class);

		OrderComplete responseOri = new Gson().fromJson(json, OrderComplete.class);
		// Los precios en VTex vienen en string se considera que los dos ultimos
		// son posiciones
		// decimales por lo cual esta conversion los transformara en el valor
		// real de la orden
		OrderComplete response = OrderVtexUtil.setRealValuesToOrderComplete(responseOri);

		List<Total> updateTotals = new ArrayList<Total>();
		BigDecimal totalPrice = new BigDecimal(0);
		BigDecimal totalDiscounts = new BigDecimal(0);
		BigDecimal totalShipping = new BigDecimal(0);
		for (Total total : response.getTotals()) {
			// systemOut.println("====>>>>"+total.getName());
			if (total.getId().equals("Items")) {
				// systemOut.println("====>>>>");
				total.setSpanishName("Total Items");
				totalPrice.add(new BigDecimal(total.getValue()));
			} else if (total.getId().equals("Discounts")) {
				total.setSpanishName("Total Descuentos");
				totalPrice.subtract(new BigDecimal(total.getValue()));
				totalDiscounts = new BigDecimal(total.getValue());
			} else if (total.getId().equals("Shipping")) {
				total.setSpanishName("Costo de Envio");
				totalPrice.add(new BigDecimal(total.getValue()));
				totalShipping = new BigDecimal(total.getValue());
			} else if (total.getId().equals("Tax")) {
				total.setSpanishName("Total Impuestos");
				totalPrice.add(new BigDecimal(total.getValue()));
			}
			updateTotals.add(total);
		}
		response.setTotalPrice(totalPrice);
		response.setTotals(updateTotals);

		List<ItemComplete> icList = new ArrayList<ItemComplete>();

		if (response.getItems() != null && !response.getItems().isEmpty()) {

			BigDecimal partialSellerPrice = totalShipping.divide(new BigDecimal(response.getItems().size()), 2,
					RoundingMode.CEILING);
			BigDecimal partialDiscount = totalDiscounts.divide(new BigDecimal(response.getItems().size()), 2,
					RoundingMode.CEILING);

			for (ItemComplete ic : response.getItems()) {
				ItemComplete itc = ic;
				try {
					String refId = (String) ic.getRefId();
					String[] supplierCode = SupplierUtil.returnSupplierCode(refId);
					Supplier sp = new Supplier();
					try {
						sp = supplierService.findById(new Long(supplierCode[0]));
					} catch (EntityNotFoundException e) {
						YaEstaLog yaestalog = new YaEstaLog();
						yaestalog.setLogDate(new Date());
						yaestalog.setProcessName("SUPPLIER-QUERY");
						yaestalog.setTextinfo("Error: NO existe proveedor con ID" + supplierCode[0]);
						yaestalog.setOrderId(orderId);
						logService.save(yaestalog);
					}
					itc.setSupplierName(sp.getName());
				} catch (Exception e) {
					// Nothing TODO.
				}
				itc.setDiscount(partialDiscount.doubleValue());
				itc.setSellingPrice(partialSellerPrice.doubleValue());
				icList.add(itc);
			}

			response.setItems(icList);
		}

		List<SupplierDeliveryInfo> sdiList = buildSupplierDeliveryInfo(response);
		response.setSupplierDeliveryInfoList(sdiList);

		OrderConversation orderConversation = getOrderConversation(orderId);
		if (!orderConversation.getCustomerEmail().equals("")) {
			response.getClientProfileData().setEmail(orderConversation.getCustomerEmail());
		}
		response = persistOrder(response, null);

		try {
			generateOrderItem(response);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// generateGuides(response);
		return response;
	}

	public OrderConversation getOrderConversation(String orderId) {
		client = ClientBuilder.newClient();

		String restUrl = this.vitexRestUrl + "/api/oms/pvt/orders/" + orderId + "/conversation-message";
		target = client.target(restUrl);

		MultivaluedMap<String, Object> myHeaders = new MultivaluedHashMap<String, Object>();
		myHeaders.add(vitexRestAppkeyName, vitexRestAppkey);
		myHeaders.add(vitexRestTokenName, vitexRestToken);
		String json = target.request(MediaType.TEXT_PLAIN).headers(myHeaders).get(String.class);
		String email = "";
		try {
			if (json != null) {
				json = json.substring(1, json.length());
				json = json.substring(0, json.length() - 1);
				systemOut.println("json " + json);
				String[] partZero = json.split("\"to\":");
				String[] partOne = partZero[1].split("\"email\":");
				String[] emailData = partOne[1].split(",");
				email = emailData[0];
				email = email.replace("\"", "");
				email = email.trim();
			}
		} catch (Exception e) {
			systemOut.println("Error obteniendo email enmascarado de cliente " + json);
		}
		OrderConversation ocon = new OrderConversation();

		ocon.setCustomerEmail(email);

		return ocon;
	}

	private OrderComplete persistOrder(OrderComplete orderComplete, String status) {

		Order found = orderService.findByVitexId(orderComplete.getOrderId());

		Order order = new Order();

		if (found != null) {
			order = found;
			order.setUpdDate(new Date());
			orderComplete.setPending(found.getPending());
			orderComplete.setMotivePendingText(found.getMotivePending());
			orderComplete.setPendingSolutionText(found.getPendingSolution());

		} else {
			// order.setCreateDate(orderComplete.getCreationDate());
			order.setCreateDate(new Date());
			orderComplete.setAppStatus("invoice_pending");
		}

		if (status != null) {
			order.setStatus(status);
		} else {
			order.setStatus(orderComplete.getAppStatus());
		}
		order.setVitexStatus(orderComplete.getStatus());
		order.setVitexId(orderComplete.getOrderId());
		order.setTotalPrice(orderComplete.getValue());
		PaymentBean pb = OrderVtexUtil.getPaymentBean(orderComplete);
		if (pb != null) {
			order.setPaymentMethod(pb.getPaymentMethod());
		}
		String dateParts[] = UtilDate.dateParts(order.getCreateDate());
		order.setPeriode(dateParts[0] + "-" + dateParts[1]);

		GsonBuilder gBuilder = new GsonBuilder();
		// gBuilder.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
		gBuilder.registerTypeAdapter(OrderComplete.class, new YaestaTypeAdapter<OrderComplete>());

		systemOut.println("Order Complete " + orderComplete.getOrderId());

		Gson gson = gBuilder.create();

		String orderInfo = gson.toJson(orderComplete);

		order.setOrderInfo(orderInfo);

		orderService.saveOrder(order);

		Customer customer = clientService.updateCustomerInfo(orderComplete); // capturar
																				// informacion
																				// del
																				// cliente

		if (order.getClient() == null) {
			order.setClient(customer);
			orderService.saveOrder(order);
		}

		List<Guide> guides = guideService.findByOrder(order);

		if (guides != null && !guides.isEmpty()) {
			orderComplete.setGuides(guides);
		}

		return orderComplete;

	}

	private List<SupplierDeliveryInfo> buildSupplierDeliveryInfo(OrderComplete orderComplete) {
		List<SupplierDeliveryInfo> deliveryInfoList = new ArrayList<SupplierDeliveryInfo>();

		List<String> supplierCodes = new ArrayList<String>();

		// Obtener los codigos de proveedores
		for (ItemComplete ic : orderComplete.getItems()) {
			if (ic.getRefId() != null) {
				String refId = (String) ic.getRefId();
				systemOut.println("refId " + refId);
				String[] supplierCode = SupplierUtil.returnSupplierCode(refId);
				systemOut.println("refId " + supplierCode[0] + " " + supplierCode[1]);
				if (!supplierCodes.contains(supplierCode[0])) {
					systemOut.println("agrega " + supplierCode[0]);
					supplierCodes.add(supplierCode[0]);
				}
			}
		}

		for (String sc : supplierCodes) {
			if (sc != null && !sc.equals("")) {

				Supplier supplier = new Supplier();
				try {
					supplier = supplierService.findById(new Long(sc));
				} catch (EntityNotFoundException e) {
					YaEstaLog yaestalog = new YaEstaLog();
					yaestalog.setLogDate(new Date());
					yaestalog.setProcessName("SUPPLIER-QUERY");
					yaestalog.setTextinfo("Error: NO existe proveedor con ID" + sc);
					yaestalog.setOrderId(orderComplete.getOrderId());
					logService.save(yaestalog);
				}

				SupplierDeliveryInfo sdi = new SupplierDeliveryInfo();
				sdi.setSupplier(supplier);
				for (ItemComplete ic : orderComplete.getItems()) {
					String refId = (String) ic.getRefId();
					String[] supplierCode = SupplierUtil.returnSupplierCode(refId);
					if (sc.equals(supplierCode[0])) {
						if (!sdi.getItems().contains(ic)) { // prevenenir items
															// duplicados
							sdi.getItems().add(ic);
						}
					}
				}
				deliveryInfoList.add(sdi);
			}
		}

		return deliveryInfoList;
	}

	public GuideContainerBean generateGuides(GuideInfoBean guideInfoBean) {

		if (guideInfoBean.getDeliverySelected().getNemonic().equals("TRAMACO")) {
			return generateGuidesTramaco(guideInfoBean);
		} else if (guideInfoBean.getDeliverySelected().getNemonic().equals("TCC")) {
			return generateGuidesTcc(guideInfoBean);
		} else if (guideInfoBean.getDeliverySelected().getNemonic().equals("MOTO_EXPRESS")
				|| guideInfoBean.getDeliverySelected().getNemonic().equals("DESPACHO_INTERNO")
				|| guideInfoBean.getDeliverySelected().getNemonic().equals("CICLISTA")) {
			return generateGuideStandar(guideInfoBean);
		} else {
			return new GuideContainerBean();
		}
	}

	public GuideContainerBean resendGuides(GuideInfoBean guideInfoBean) {
		GuideContainerBean guideContainer = new GuideContainerBean();
		if (guideInfoBean.getDeliverySelected().getNemonic().equals("TRAMACO")) {

			OrderComplete orderComplete = guideInfoBean.getOrderComplete();

			Order order = orderService.findByVitexId(orderComplete.getOrderId());

			List<Guide> guides = guideService.findByOrder(order);

			List<SupplierDeliveryInfo> supplierDeliveryInfoList = guideInfoBean.getSupplierDeliveryInfoList();

			Catalog delivery = guideInfoBean.getDeliverySelected();

			List<MailInfo> mailInfoList = prepareMailOrder(orderComplete, supplierDeliveryInfoList, delivery,
					"Reenviar");

			for (MailInfo mailInfo : mailInfoList) {
				for (Guide guide : guides) {
					if (guide.getSupplier().getId().longValue() == mailInfo.getRefId().longValue()) {
						mailInfo.getAttachmentList().add(guide.getDocumentUrl());
						if (guide.getDocumentTagUrl() != null) {
							mailInfo.getAttachmentList().add(guide.getDocumentTagUrl());
						}
					}
				}
				mailService.sendMailTemplate(mailInfo, "guideNotification.vm");
			}
		}

		return guideContainer;
	}

	private GuideContainerBean generateGuidesTramaco(GuideInfoBean guideInfoBean) {
		GuideContainerBean result = new GuideContainerBean();
		GuideInfoBean response = guideInfoBean;
		List<GuideDTO> responseList = new ArrayList<GuideDTO>();

		OrderComplete orderComplete = guideInfoBean.getOrderComplete();

		Order order = orderService.findByVitexId(orderComplete.getOrderId());

		order.setDeliveryName("TRAMACO");
		orderService.saveOrder(order);

		List<SupplierDeliveryInfo> supplierDeliveryInfoList = guideInfoBean.getSupplierDeliveryInfoList();

		orderComplete.setSupplierDeliveryInfoList(supplierDeliveryInfoList);
		GuideDTO guideDTO = new GuideDTO();
		guideDTO.setOrderComplete(orderComplete);
		guideDTO.setCustomerAdditionalInfo(guideInfoBean.getCustomerAdditionalInfo());
		guideDTO.setDeliverySelected(guideInfoBean.getDeliverySelected());
		guideDTO.setCustomerDocument(guideInfoBean.getCustomerDocument());

		GuideDTO resultGuideInfo = tramacoService.generateGuides(guideDTO);
		
		System.out.println("Resultado generar Guias " + resultGuideInfo.getResponse());

		if (resultGuideInfo.getResponse().equals("OK") || resultGuideInfo.getResponse().equals("EXITO")) {

			List<GuideBeanDTO> guideInfoBeanList = resultGuideInfo.getGuideBeanList();
			List<GuideBeanDTO> guideInfoList = new ArrayList<GuideBeanDTO>();
			String pdfURL = yaestaPdfGuidePath;

			if (guideInfoBeanList != null && !guideInfoBeanList.isEmpty()) {
				for (GuideBeanDTO gbd : guideInfoBeanList) {
					Guide guide = new Guide();
					guide.setCreateDate(new Date());
					guide.setOrderVitexId(orderComplete.getOrderId());
					guide.setVitexDispatcherId(
							gbd.getGuideResponse().getSalidaGenerarGuiaWs().getLstGuias().get(0).getId() + "%"
									+ gbd.getGuideResponse().getSalidaGenerarGuiaWs().getLstGuias().get(0).getGuia());
					guide.setGuideInfo(new Gson().toJson(gbd));
					guide.setOrder(order);
					guide.setDeliveryCost(gbd.getDeliveryCost());
					guide.setDeliveryPayment(gbd.getDeliveryPayment());
					guide.setItemValue(gbd.getItemValue());
					guide.setDeliveryStatus("GENERATED");
					guide.setSupplier(gbd.getSupplier());
					guide.setCustomerName(orderComplete.getCustomerName());
					guide.setCustomerPhone(orderComplete.getClientProfileData().getPhone());
					guide.setGuideNumber(
							gbd.getGuideResponse().getSalidaGenerarGuiaWs().getLstGuias().get(0).getGuia());
					guide.setSerial(orderComplete.getSequence());

					String dateParts[] = UtilDate.dateParts(order.getCreateDate());
					guide.setPeriode(dateParts[0] + "-" + dateParts[1]);

					try {
						guide.setOrderDate(UtilDate.fromIsoToDateTime(orderComplete.getCreationDate()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					guide.setOrderStatus(orderComplete.getStatus());
					guide.setPaymentMethod(OrderVtexUtil.getPaymentBean(orderComplete).getPaymentMethod());
					guide.setHasPayment(gbd.getHasPayment());
					guide.setTotalValue(gbd.getTotalValue());

					guideService.saveGuide(guide);
					gbd.setGuide(guide);
					guideInfoList.add(gbd);

					List<GuideDetail> details = gbd.getDetails();
					// Guardar los detalles de la guia
					if (details != null && !details.isEmpty()) {
						guideService.saveGuideDetail(guide, details);
					}
				} // lista de guias

				guideDTO.setGuideBeanList(guideInfoList);
				// LLamar ahora al servicio de pdfs
				System.out.println("Antes de llamar a generar PDF");
				resultGuideInfo = tramacoService.generateGuidesPDF(guideDTO);
				System.out.println("Luego de llamar a generar PDF");
				guideInfoBeanList = resultGuideInfo.getGuideBeanList();
				// Realizar segunda iteracion para las guias
				List<GuideBeanDTO> newGuideInfoBeanList = new ArrayList<GuideBeanDTO>();
				List<Guide> guides = new ArrayList<Guide>();
				for (GuideBeanDTO gbd : guideInfoBeanList) {
					Guide guide = gbd.getGuide();
					guide.setStatus("GENERATED-PDF");
					guide.setDocumentUrl(gbd.getPdfUrl());
					guide.setDeliveryName("TRAMACO");
					guide.setTrackingUrl(tramacoTrackingUrl);
					guideService.saveGuide(guide);
					guides.add(guide);
					response.getPdfPathList().add(gbd.getPdfUrl());

					// Generar Tag Guide
					GuideDataBean gDbean = new GuideDataBean();
					pdfURL = yaestaPdfGuidePath;
					pdfURL = pdfURL + yaestaGuidePrefix + "TRAMACO" + "_" + guide.getGuideNumber() + "_" + ".pdf";
					gDbean.setPdfPath(pdfURL);
					// gDbean.setLogoPath(logoPath);
					gDbean.setOrderId(orderComplete.getOrderId());
					gDbean.setGuideNumber(guide.getGuideNumber());
					// guide.setDocumentUrl(pdfURL);

					List<GuideDetail> details = guideService.getGuideDetails(guide);
					List<ItemData> itemDataList = new ArrayList<ItemData>();
					if (details != null && !details.isEmpty()) {
						for (GuideDetail gd : details) {
							ItemData id = new ItemData();
							id.setCode(gd.getVitexId());
							id.setName(gd.getItemName());
							id.setQuantity(gd.getQuantity() + "");
							itemDataList.add(id);
						}
					}
					gDbean.setItemDataList(itemDataList);

					List<String> strList = new ArrayList<String>();
					// strList.add(" ");
					// String str = "Guia # " + guide.getGuideNumber() + "";
					// strList.add(str);
					String courier = "Gestor Entrega: " + "Tramaco";
					if (gbd.getHasPayment()) {
						courier = courier + " - Valor a Cobrar: " + gbd.getTotalValue();
					}
					strList.add(courier);
					/*
					 * String strPaymentForm = "Forma de pago: " +
					 * grr.getInformacionAdicional().getFormaPago();
					 * strList.add(strPaymentForm); String strPayment =
					 * "Valor a cobrar: " +
					 * grr.getInformacionAdicional().getValorACobrar();
					 * strList.add(strPayment);
					 */
					strList.add("______________________________________________");
					// strList.add(" ");

					String supplierAddress = "Origen: " + yaestaAddress;
					// supplierAddress = "Origen: "+
					// gbd.getSupplier().getAddress();
					strList.add(supplierAddress);
					/*
					 * String supplierContact = "Contacto: " +
					 * gbd.getSupplier().getContactName() + " " +
					 * gbd.getSupplier().getContactLastName();
					 * strList.add(supplierContact); String supplierAux =
					 * "Email: " + gbd.getSupplier().getContactEmail();
					 * strList.add(supplierAux);
					 */
					strList.add("______________________________________________");
					// strList.add(" ");

					String customerAddress = "Destino: " + orderComplete.getShippingData().getAddress().getState() + " "
							+ orderComplete.getShippingData().getAddress().getCity();
					customerAddress = customerAddress + " " + orderComplete.getShippingData().getAddress().getStreet();
					customerAddress = customerAddress + " "
							+ orderComplete.getShippingData().getAddress().getReference();
					strList.add(customerAddress);
					String customer = "Destinatario: " + orderComplete.getCustomerName();
					strList.add(customer);
					/*
					 * String email = "Email: " +
					 * orderComplete.getClientProfileData().getEmail();
					 * strList.add(email); String phone = "Telefono: " +
					 * orderComplete.getClientProfileData().getPhone();
					 * strList.add(phone);
					 */
					strList.add("______________________________________________");

					// strList.add(" ");
					gDbean.setParagraphs(strList);

					gDbean.setGuideLeyend(yaestaGuideLeyend);
					gDbean.setLogoPath(yaestaGuideLogo);
					gDbean = BuildTagGuidePDF.generateGuidePDF(gDbean);
					gbd.setPdfTagUrl(gDbean.getPdfPath());
					guide.setDocumentTagUrl(gDbean.getPdfPath());
					guideService.saveGuide(guide);
					newGuideInfoBeanList.add(gbd);
				}

				resultGuideInfo.setGuideBeanList(newGuideInfoBeanList);

				orderService.saveOrder(order);
				responseList.add(resultGuideInfo);
				guideDTO = resultGuideInfo;

			} // si la generacion fue exitosa

			
			result.setGuideInfoBean(response);

			result.setGuides(responseList);

			List<MailInfo> mailInfoList = prepareMailOrder(orderComplete, supplierDeliveryInfoList,
					guideInfoBean.getDeliverySelected(), null);

			for (MailInfo mailInfo : mailInfoList) {
				for (GuideBeanDTO gDto : guideDTO.getGuideBeanList()) {
					if (gDto.getSupplier().getId() == mailInfo.getRefId()) {
						mailInfo.getAttachmentList().add(gDto.getPdfUrl());
						if (gDto.getPdfTagUrl() != null) {
							mailInfo.getAttachmentList().add(gDto.getPdfTagUrl());
						}
					}
				}
				mailService.sendMailTemplate(mailInfo, "guideNotification.vm");
			}

			if (mailNotifyCustomer.equals("Y")) {
				sendGuideMailCustomer(orderComplete);
			}

			
		}//si hubo exito en la generacion de guias, se controla no se envie el email

		
		updateDetails(orderComplete);

		return result;
	}

	private GuideContainerBean generateGuidesTcc(GuideInfoBean guideInfoBean) {

		systemOut.println("Inicio guias TCC");
		GuideContainerBean result = new GuideContainerBean();
		GuideInfoBean response = guideInfoBean;
		List<GuideDTO> responseList = new ArrayList<GuideDTO>();

		OrderComplete orderComplete = guideInfoBean.getOrderComplete();

		Order order = orderService.findByVitexId(orderComplete.getOrderId());
		order.setDeliveryName("TCC");
		orderService.saveOrder(order);

		List<SupplierDeliveryInfo> supplierDeliveryInfoList = guideInfoBean.getSupplierDeliveryInfoList();

		orderComplete.setSupplierDeliveryInfoList(supplierDeliveryInfoList);
		GuideDTO guideDTO = new GuideDTO();
		guideDTO.setOrderComplete(orderComplete);
		guideDTO.setCustomerAdditionalInfo(guideInfoBean.getCustomerAdditionalInfo());
		guideDTO.setDeliverySelected(guideInfoBean.getDeliverySelected());

		GuideDTO resultGuideInfo = tccService.generateGuides(guideDTO);

		List<GuideBeanDTO> guideInfoBeanList = resultGuideInfo.getGuideBeanList();
		List<GuideBeanDTO> guideInfoList = new ArrayList<GuideBeanDTO>();

		if (guideInfoBeanList != null && !guideInfoBeanList.isEmpty()) {
			for (GuideBeanDTO gbd : guideInfoBeanList) {
				Guide guide = new Guide();
				guide.setCreateDate(new Date());
				guide.setOrderVitexId(orderComplete.getOrderId());
				guide.setVitexDispatcherId(gbd.getGuideResponse().getSalidaGenerarGuiaWs().getLstGuias().get(0).getId()
						+ "%" + gbd.getGuideResponse().getSalidaGenerarGuiaWs().getLstGuias().get(0).getGuia());
				guide.setGuideInfo(new Gson().toJson(gbd));
				guide.setOrder(order);
				guide.setDeliveryCost(gbd.getDeliveryCost());
				guide.setDeliveryPayment(gbd.getDeliveryPayment());
				guide.setItemValue(gbd.getItemValue());
				guide.setDeliveryStatus("GENERATED");
				guide.setSupplier(gbd.getSupplier());
				guide.setCustomerName(orderComplete.getCustomerName());
				try {
					guide.setOrderDate(UtilDate.fromIsoToDateTime(orderComplete.getCreationDate()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				guide.setOrderStatus(orderComplete.getStatus());
				guideService.saveGuide(guide);
				gbd.setGuide(guide);
				guideInfoList.add(gbd);
			}

			guideDTO.setGuideBeanList(guideInfoList);
			// LLamar ahora al servicio de pdfs
			/*
			 * resultGuideInfo = tccService.generateGuidesPDF(guideDTO);
			 * guideInfoBeanList = resultGuideInfo.getGuideBeanList();
			 * List<Guide> guides = new ArrayList<Guide>(); for(GuideBeanDTO
			 * gbd:guideInfoBeanList){ Guide guide = gbd.getGuide();
			 * guide.setStatus("GENERATED-PDF");
			 * guide.setDocumentUrl(gbd.getPdfUrl());
			 * guide.setDeliveryName("TRAMACO"); guideService.saveGuide(guide);
			 * guides.add(guide);
			 * response.getPdfPathList().add(gbd.getPdfUrl()); }
			 * 
			 * responseList.add(resultGuideInfo); guideDTO=resultGuideInfo;
			 */
			orderService.saveOrder(order);
		}

		result.setGuideInfoBean(response);

		result.setGuides(responseList);
		/*
		 * List<MailInfo> mailInfoList=
		 * prepareMailOrder(orderComplete,supplierDeliveryInfoList,guideInfoBean
		 * .getDeliverySelected());
		 * 
		 * for(MailInfo mailInfo:mailInfoList){ for(GuideBeanDTO
		 * gDto:guideDTO.getGuideBeanList()){
		 * if(gDto.getSupplier().getId()==mailInfo.getRefId()){
		 * mailInfo.getAttachmentList().add(gDto.getPdfUrl()); } }
		 * mailService.sendMailTemplate(mailInfo, "guideNotification.vm"); }
		 */

		if (mailNotifyCustomer.equals("Y")) {
			sendGuideMailCustomer(orderComplete);
		}

		return result;
	}

	public OrderCancel cancelOrder(OrderComplete orderComplete) {

		client = ClientBuilder.newClient();

		String restUrl = this.vitexRestUrl + "/api/oms/pvt/orders/" + orderComplete.getOrderId() + "/cancel";
		target = client.target(restUrl);

		MultivaluedMap<String, Object> myHeaders = new MultivaluedHashMap<String, Object>();
		myHeaders.add(vitexRestAppkeyName, vitexRestAppkey);
		myHeaders.add(vitexRestTokenName, vitexRestToken);
		String json = target.request(MediaType.TEXT_PLAIN).headers(myHeaders)
				.post(Entity.json(orderComplete.getOrderId()), String.class);

		OrderCancel response = new Gson().fromJson(json, OrderCancel.class);

		Order order = orderService.findByVitexId(orderComplete.getOrderId());
		order.setMotiveCancelText(orderComplete.getMotiveCancelText());

		Catalog motiveCancel = catalogService.findById(orderComplete.getMotiveCancelId());
		order.setMotiveCancel(motiveCancel);
		order.setStatus("canceled");
		order.setCancelDate(new Date());

		orderService.saveOrder(order);

		return response;
	}

	public OrderComplete changeStatus(OrderComplete orderComplete, String action) {

		if (orderComplete != null) {
			orderComplete.setError("OK");
			systemOut.println("====>>>>1)");
			try {
				if (action == null) {
					action = orderComplete.getAppStatus();
				} else {
					orderComplete.setAppStatus(action);
				}

				if (action.equals("cancel")) {
					OrderComplete cancelOrder = changeVitexOrder(orderComplete.getOrderId(), "/" + action);
					orderComplete = cancelOrder;
				}

				if (action.equals("invoice")) {
					OrderComplete invoiceOrder = changeVitexOrder(orderComplete.getOrderId(), "/" + action);
					orderComplete = invoiceOrder;
				}

				if (action.equals("approved")) {

					List<SupplierDeliveryInfo> sdiList = buildSupplierDeliveryInfo(orderComplete);
					orderComplete.setSupplierDeliveryInfoList(sdiList);
				}

				orderComplete = persistOrder(orderComplete, action);
			} catch (Exception e) {
				orderComplete.setError("ERROR:" + e.getMessage());
			}

		} else {
			systemOut.println("====>>>>2)");

		}
		return orderComplete;
	}

	private OrderComplete changeVitexOrder(String orderId, String action) {
		client = ClientBuilder.newClient();

		String restUrl = this.vitexRestUrl + "/api/oms/pvt/orders/" + orderId + "/changestatus/" + action;
		target = client.target(restUrl);

		MultivaluedMap<String, Object> myHeaders = new MultivaluedHashMap<String, Object>();
		myHeaders.add(vitexRestAppkeyName, vitexRestAppkey);
		myHeaders.add(vitexRestTokenName, vitexRestToken);
		String json = target.request(MediaType.TEXT_PLAIN).headers(myHeaders).post(Entity.json(orderId), String.class);

		OrderComplete response = new Gson().fromJson(json, OrderComplete.class);

		return response;
	}

	private GuideContainerBean generateGuideStandar(GuideInfoBean guideInfoBean) {
		GuideContainerBean result = new GuideContainerBean();

		if (guideInfoBean.getDeliverySelected().getNemonic().equals("MOTO_EXPRESS")) {
			return generateGuideMotoExpress(guideInfoBean);
		} else if (guideInfoBean.getDeliverySelected().getNemonic().equals("DESPACHO_INTERNO")) {
			return generateGuideMotoInternal(guideInfoBean);
		} else if (guideInfoBean.getDeliverySelected().getNemonic().equals("CICLISTA")) {
			return generateGuideCyclist(guideInfoBean);
		}

		updateDetails(guideInfoBean.getOrderComplete());

		return result;
	}

	private GuideContainerBean generateGuideCyclist(GuideInfoBean guideInfoBean) {
		GuideContainerBean result = new GuideContainerBean();
		WayBillSchema response = datilService.processWayBill(guideInfoBean.getOrderComplete(),
				guideInfoBean.getDeliverySelected(), "SEQ_WAYBILL");
		result = processStandarGuide(response, guideInfoBean.getOrderComplete(), guideInfoBean.getDeliverySelected(),
				guideInfoBean.getSupplierDeliveryInfoList());

		if (mailNotifyCustomer.equals("Y")) {
			sendGuideMailCustomer(guideInfoBean.getOrderComplete());
		}
		return result;
	}

	private GuideContainerBean generateGuideMotoExpress(GuideInfoBean guideInfoBean) {
		GuideContainerBean result = new GuideContainerBean();
		WayBillSchema response = datilService.processWayBill(guideInfoBean.getOrderComplete(),
				guideInfoBean.getDeliverySelected(), "SEQ_WAYBILL");
		result = processStandarGuide(response, guideInfoBean.getOrderComplete(), guideInfoBean.getDeliverySelected(),
				guideInfoBean.getSupplierDeliveryInfoList());

		if (mailNotifyCustomer.equals("Y")) {
			sendGuideMailCustomer(guideInfoBean.getOrderComplete());
		}
		return result;
	}

	private GuideContainerBean generateGuideMotoInternal(GuideInfoBean guideInfoBean) {
		GuideContainerBean result = new GuideContainerBean();
		// WayBillSchema response =
		// datilService.processWayBill(guideInfoBean.getOrderComplete(),
		// guideInfoBean.getDeliverySelected(), "SEQ_WAYBILL_INTERNAL");
		WayBillSchema response = datilService.processWayBill(guideInfoBean.getOrderComplete(),
				guideInfoBean.getDeliverySelected(), "SEQ_WAYBILL");
		result = processStandarGuide(response, guideInfoBean.getOrderComplete(), guideInfoBean.getDeliverySelected(),
				guideInfoBean.getSupplierDeliveryInfoList());

		if (mailNotifyCustomer.equals("Y")) {
			sendGuideMailCustomer(guideInfoBean.getOrderComplete());
		}
		return result;
	}

	private GuideContainerBean processStandarGuide(WayBillSchema wayBill, OrderComplete orderComplete, Catalog delivery,
			List<SupplierDeliveryInfo> supplierDeliveryInfoList) {
		GuideContainerBean result = new GuideContainerBean();

		Order order = orderService.findByVitexId(orderComplete.getOrderId());

		order.setDeliveryName(delivery.getNemonic());
		orderService.saveOrder(order);
		if (wayBill != null) {
			List<GuideBeanDTO> guideInfoBeanList = new ArrayList<GuideBeanDTO>();
			for (GuideRemisionContainer grc : wayBill.getGuideContainerList()) {
				GuiaRemisionRespuesta grr = grc.getGuiaRemisionRespuesta();
				GuideBeanDTO gbd = new GuideBeanDTO();
				Guide guide = new Guide();
				guide.setCreateDate(new Date());
				guide.setOrder(order);
				guide.setOrderVitexId(orderComplete.getOrderId());
				guide.setVitexDispatcherId(grr.getId());
				guide.setGuideInfo(new Gson().toJson(grr));
				guide.setOrder(order);
				guide.setDeliveryName(delivery.getNemonic());
				guide.setDeliveryStatus("GENERATED-PDF");
				guide.setStatus("GENERATED-PDF");
				guide.setOrderStatus(orderComplete.getStatus());
				guide.setAccessCode(grr.getClaveAcceso());
				;
				guide.setSerial(grr.getSecuencial() + "");
				guide.setCustomerPhone(orderComplete.getClientProfileData().getPhone());
				guide.setGuideNumber(formatGuideNumber(grr.getSecuencial() + ""));

				String dateParts[] = UtilDate.dateParts(order.getCreateDate());
				guide.setPeriode(dateParts[0] + "-" + dateParts[1]);

				try {
					Long idSup = new Long(grr.getInformacionAdicional().getIdProveedor());

					Supplier supp = new Supplier();
					try {
						supp = supplierService.findById(idSup);
					} catch (EntityNotFoundException e) {
						YaEstaLog yaestalog = new YaEstaLog();
						yaestalog.setLogDate(new Date());
						yaestalog.setProcessName("SUPPLIER-QUERY");
						yaestalog.setTextinfo("Error: NO existe proveedor con ID" + idSup);
						yaestalog.setOrderId(orderComplete.getOrderId());
						logService.save(yaestalog);
					}

					guide.setSupplier(supp);
					gbd.setSupplier(supp);
				} catch (Exception e) {
					e.printStackTrace();
				}
				guide.setCustomerName(orderComplete.getCustomerName());
				try {
					guide.setOrderDate(UtilDate.fromIsoToDateTime(orderComplete.getCreationDate()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String pdfURL = "";

				if (delivery.getNemonic().equals("MOTO_EXPRESS")) {
					pdfURL = motoExpressPdfPath;
				} else if (delivery.getNemonic().equals("DESPACHO_INTERNO")) {
					pdfURL = internalPdfPath;
				} else if (delivery.getNemonic().equals("CICLISTA")) {
					pdfURL = cyclistPdfPath;
				}

				GuideDataBean gdb = new GuideDataBean();
				pdfURL = pdfURL + guidePrefix + delivery.getNemonic() + "_" + grr.getSecuencial() + "_" + ".pdf";
				gdb.setPdfPath(pdfURL);
				gbd.setPdfUrl(pdfURL);
				gdb.setLogoPath(logoPath);
				gdb.setOrderId(orderComplete.getOrderId());
				guide.setDocumentUrl(pdfURL);
				List<ItemData> itemDataList = new ArrayList<ItemData>();
				for (Destinatario des : grr.getDestinatarios()) {
					for (ItemGuiaRemision igr : des.getItems()) {
						ItemData id = new ItemData();
						id.setCode(igr.getCodigoPrincipal());
						id.setName(igr.getDescripcion());
						id.setQuantity(igr.getCantidad() + "");
						itemDataList.add(id);
					}
				}
				gdb.setItemDataList(itemDataList);
				List<String> strList = new ArrayList<String>();
				strList.add(" ");
				String str = "Guia # " + formatGuideNumber(grr.getSecuencial() + "");
				strList.add(str);
				String courier = "Courier " + delivery.getName();
				strList.add(courier);
				String strPaymentForm = "Forma de pago: " + grr.getInformacionAdicional().getFormaPago();
				strList.add(strPaymentForm);
				String strPayment = "Valor a cobrar: " + grr.getInformacionAdicional().getValorACobrar();
				strList.add(strPayment);
				strList.add("______________________________________________");
				strList.add(" ");

				String supplierName = "Origen [Proveedor]: " + gbd.getSupplier().getName() + " ";
				strList.add(supplierName);
				String supplierAddress = "Direccion: " + gbd.getSupplier().getAddress();
				strList.add(supplierAddress);
				String supplierContact = "Contacto: " + gbd.getSupplier().getContactName() + " "
						+ gbd.getSupplier().getContactLastName();
				strList.add(supplierContact);
				String supplierAux = "Email: " + gbd.getSupplier().getContactEmail();
				strList.add(supplierAux);
				strList.add("______________________________________________");
				strList.add(" ");

				String customer = "Destinatario [Cliente]:" + orderComplete.getCustomerName();
				strList.add(customer);
				String customerAddress = "Direccion: " + orderComplete.getShippingData().getAddress().getState() + " "
						+ orderComplete.getShippingData().getAddress().getCity();
				customerAddress = customerAddress + " " + orderComplete.getShippingData().getAddress().getStreet();
				customerAddress = customerAddress + " " + orderComplete.getShippingData().getAddress().getReference();
				strList.add(customerAddress);
				String email = "Email: " + orderComplete.getClientProfileData().getEmail();
				strList.add(email);
				String phone = "Telefono: " + orderComplete.getClientProfileData().getPhone();
				strList.add(phone);
				strList.add("______________________________________________");
				strList.add(" ");
				gdb.setParagraphs(strList);
				guideService.saveGuide(guide);

				gdb = BuildGuidePDF.generateGuidePDF(gdb);
				guideInfoBeanList.add(gbd);

				List<GuideDetail> details = grc.getDetailList();

				if (details != null && !details.isEmpty()) {
					guideService.saveGuideDetail(guide, details);
				}

			} // fin for

			orderService.saveOrder(order);

			List<MailInfo> mailInfoList = prepareMailOrder(orderComplete, supplierDeliveryInfoList, delivery, null);

			for (MailInfo mailInfo : mailInfoList) {
				for (GuideBeanDTO gDto : guideInfoBeanList) {
					if (gDto.getSupplier().getId().longValue() == mailInfo.getRefId().longValue()) {
						// systemOut.println("A "+gDto.getSupplier().getId());
						// systemOut.println("A1 "+gDto.getPdfUrl());
						mailInfo.getAttachmentList().add(gDto.getPdfUrl());
					} else {
						systemOut.println("B " + gDto.getSupplier().getId());
					}
				}
				mailService.sendMailTemplate(mailInfo, "guideNotification.vm");
			}
		}

		return result;
	}

	private List<MailInfo> prepareMailOrder(OrderComplete orderComplete,
			List<SupplierDeliveryInfo> supplierDeliveryInfoList, Catalog delivery, String flag) {

		List<MailInfo> mailInfoList = new ArrayList<MailInfo>();

		for (SupplierDeliveryInfo sdi : supplierDeliveryInfoList) {

			if (sdi.getSelected()) {
				MailInfo mailInfo = new MailInfo();
				mailInfo.setReferenceId(orderComplete.getOrderId());

				MailParticipant sender = new MailParticipant();
				sender.setName("YaEsta.com");
				sender.setEmail(mailFrom);
				mailInfo.setMailSender(sender);

				MailParticipant receiver = new MailParticipant();
				receiver.setName(mailToName);
				receiver.setEmail(mailTo);
				mailInfo.setMailReceiver(receiver);

				List<MailParticipant> receiverTotal = new ArrayList<MailParticipant>();

				MailParticipant mpCc = new MailParticipant();
				mpCc.setEmail(mailCc);
				mpCc.setName(mailCcName);
				receiverTotal.add(mpCc);

				// if( sdi.getDelivery()!=null &&
				// sdi.getDelivery().getNemonic().equals(DeliveryEnum.TRAMACO.getNemonic())){
				if (delivery != null && delivery.getNemonic().equals(DeliveryEnum.TRAMACO.getNemonic())) {
					String[] contactsCourierNames = tramacoContactsNames.split("%");
					String[] contactsCourierEmails = tramacoContacts.split("%");

					for (int j = 0; j < contactsCourierNames.length; j++) {
						MailParticipant mpCourier = new MailParticipant();
						mpCourier.setEmail(contactsCourierEmails[j]);
						mpCourier.setName(contactsCourierNames[j]);
						receiverTotal.add(mpCourier);
					}
				}

				List<MailParticipant> recSupplierList = new ArrayList<MailParticipant>();
				Supplier supplier = sdi.getSupplier();
				List<SupplierContact> supplierContactList = supplierContactRepository.findBySupplier(supplier);
				if (supplierContactList != null && !supplierContactList.isEmpty()) {
					for (SupplierContact sc : supplierContactList) {
						if (sc.getEmail() != null) {
							MailParticipant mp = new MailParticipant();
							mp.setEmail(sc.getEmail());
							mp.setName(supplier.getName());
							mp.setIsSupplier(Boolean.TRUE);
							recSupplierList.add(mp);
						}
					}
				}
				if (supplier.getContactEmail() != null) {
					MailParticipant mp = new MailParticipant();
					mp.setEmail(supplier.getContactEmail());
					mp.setName(supplier.getName());
					mp.setIsSupplier(Boolean.TRUE);
					recSupplierList.add(mp);
				}
				// if(mailNotifySupplier.equals("Y")){
				receiverTotal.addAll(recSupplierList);
				// }
				List<ItemComplete> icSupplierList = sdi.getItems();
				if (icSupplierList != null && !icSupplierList.isEmpty()) {
					for (ItemComplete ic : icSupplierList) {
						ItemInfo iInfo = new ItemInfo();
						iInfo.setSupplierName(sdi.getSupplier().getName());
						iInfo.setName(ic.getName());
						iInfo.setPrice(ic.getPrice());
						iInfo.setUrl(mailImagePrefix + ic.getImageUrl());
						if (ic.getRefId() != null) {
							iInfo.setRefCode((String) ic.getRefId());
						}
						iInfo.setQuantity(ic.getQuantity());
						mailInfo.getItemInfoList().add(iInfo);
					}
				}

				String subject = "Notificación de pedido " + " - Orden: " + orderComplete.getOrderId()
						+ " - Proveedor: " + supplier.getName();

				if (flag != null) {
					subject = flag + " - " + subject;
				}

				mailInfo.setReceivers(receiverTotal);
				String strMailTextGuide = mailTextGuide;
				strMailTextGuide = strMailTextGuide.replace(mailTextGuideToken, determineDeliveryName(delivery));
				mailInfo.setGeneralText(strMailTextGuide);
				mailInfo.setRefId(supplier.getId());
				mailInfo.setRefVtexId(orderComplete.getOrderId());
				mailInfo.setSubject(subject);

				// Agregar textos de guia
				mailInfo.getTextList().add(mailTextGuide2);
				String codeText = mailTextGuide3;
				codeText = codeText.replace(mailTextGuideToken, orderComplete.getOrderId());
				mailInfo.getTextList().add(codeText);
				mailInfo.getTextList().add(mailTextGuide4);
				mailInfo.getTextList().add(mailTextGuide5);
				mailInfo.getTextList().add(mailTextGuide6);
				mailInfo.getTextList().add(mailTextGuide7);
				mailInfo.getTextList().add(mailTextGuide8);

				mailInfoList.add(mailInfo);

			}
		} // fin del for

		return mailInfoList;
	}

	public MultivaluedMap<String, Object> buildHeaders() {

		MultivaluedMap<String, Object> myHeaders = new MultivaluedHashMap<String, Object>();
		myHeaders.add(vitexRestAppkeyName, vitexRestAppkey);
		myHeaders.add(vitexRestTokenName, vitexRestToken);
		return myHeaders;
	}

	public InvoiceResponse prepareVitexInvoice(OrderComplete orderComplete) {
		InvoiceResponse response = new InvoiceResponse();
		Order order = orderService.findByVitexId(orderComplete.getOrderId());

		InvoiceSchemaBean isb = new InvoiceSchemaBean();
		isb.setOrderComplete(orderComplete);
		InvoiceSchema is = new InvoiceSchema();
		is.setType("OUTPUT");

		if (order.getInvoiceReference() != null) {
			FacturaConsulta fc = datilService.findInvoice(order.getInvoiceReference());

			List<Guide> guides = guideService.findByOrder(order);
			if (guides != null && !guides.isEmpty()) {
				for (Guide g : guides) {
					is.setTrackingNumber(g.getGuideNumber());
				}
			}

			if (fc != null) {
				is.setInvoiceNumber(fc.getAutorizacion().getNumero());
				is.setIssuanceDate(fc.getFechaEmision());
				is.setCourier(order.getDeliveryName());
				is.setInvoiceValue(order.getTotalPrice().doubleValue());
				List<ItemInvoice> itemInvoiceList = new ArrayList<ItemInvoice>();
				for (ItemComplete ic : orderComplete.getItems()) {
					ItemInvoice ii = new ItemInvoice();
					ii.setId(ic.getId());
					ii.setPrice(ic.getPrice());
					ii.setQuantity(ic.getQuantity());
					itemInvoiceList.add(ii);
				}
				is.setItems(itemInvoiceList);
			}
			isb.setInvoiceSchema(is);
			response = prepareInvoiceVitexOrder(isb);
		} else if (order.getInvoice() != null) {
			Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
			FacturaRespuestaSRI facturaRespuestaSRI = gson.fromJson(order.getInvoice(), FacturaRespuestaSRI.class);
			String trackNumber = "";
			List<Guide> guides = guideService.findByOrder(order);
			if (guides != null && !guides.isEmpty()) {
				String vitexDispatcherId = guides.get(0).getVitexDispatcherId();
				String[] codes = vitexDispatcherId.split("%");
				if (codes.length > 0) {
					trackNumber = codes[1];
				}
			}

			is.setTrackingNumber(trackNumber);

			FacturaConsulta fc = datilService.findInvoice(facturaRespuestaSRI.getId());
			if (fc != null) {
				is.setInvoiceNumber(fc.getAutorizacion().getNumero());
				is.setIssuanceDate(fc.getFechaEmision());
				is.setCourier("");
				is.setInvoiceValue(facturaRespuestaSRI.getTotales().getImporteTotal());
				List<ItemInvoice> itemInvoiceList = new ArrayList<ItemInvoice>();
				for (ItemComplete ic : orderComplete.getItems()) {
					ItemInvoice ii = new ItemInvoice();
					ii.setId(ic.getId());
					ii.setPrice(ic.getPrice());
					ii.setQuantity(ic.getQuantity());
					itemInvoiceList.add(ii);
				}
				is.setItems(itemInvoiceList);
			}
			isb.setInvoiceSchema(is);
			response = prepareInvoiceVitexOrder(isb);
		}

		return response;
	}

	private InvoiceResponse prepareInvoiceVitexOrder(InvoiceSchemaBean invoiceSchemaBean) {
		InvoiceResponse response = new InvoiceResponse();
		String restUrl = this.vitexRestUrl + "/api/oms/pvt/orders/" + invoiceSchemaBean.getOrderComplete().getOrderId()
				+ "/invoice";

		systemOut.println("URL" + restUrl);

		client = ClientBuilder.newClient();
		target = client.target(restUrl);

		Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

		String json = gson.toJson(invoiceSchemaBean.getInvoiceSchema());

		systemOut.println("VTEXT INVOICE:" + json);

		String responseJson = target.request(MediaType.APPLICATION_JSON_TYPE).headers(buildHeaders())
				.post(Entity.json(json), String.class);

		systemOut.println("==>>" + responseJson);

		response = gson.fromJson(responseJson, InvoiceResponse.class);

		return response;
	}

	@SuppressWarnings("unused")
	public OrderComplete changeStatus(String orderId, String status) {
		OrderComplete oc = this.getOrderComplete(orderId);
		OrderChangeStatus query = objectFactory.createOrderChangeStatus();
		query.setIdOrder(new Integer(oc.getSequence()));
		query.setStatusOrder(objectFactory.createString(status));

		OrderChangeStatusResponse response = (OrderChangeStatusResponse) webServiceTemplate.marshalSendAndReceive(query,
				new SoapActionCallback("http://tempuri.org/IService/OrderChangeStatus"));
		return this.getOrderComplete(orderId);
	}

	@Transactional
	public synchronized String loadOrderItem() {
		String response = "OK";
		try {
			OrderSchemaContainerBean oscb = this.getVitexOrders(new Long(this.vitexRestMaxPages));

			if (oscb != null) {
				for (OrderBean ob : oscb.getOrderBeanList()) {
					OrderComplete oc = getOrderComplete(ob.getOrderId());
					systemOut.println("Orden completa " + oc.getOrderId());
					Order order = orderService.findByVitexId(oc.getOrderId());
					if (order != null && order.getInvoiceReference() != null) {
						FacturaConsulta fc = datilService.findInvoice(order.getInvoiceReference());
						if (fc != null) {
							order.setInvoiceNumber(fc.getAutorizacion().getNumero());
							orderService.saveOrder(order);
						}
					}
				}

			}
		} catch (Exception e) {
			response = "ERROR";
			systemOut.println("Error en " + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	private void generateOrderItem(OrderComplete oc) throws ParseException {

		Order order = orderService.findByVitexId(oc.getOrderId());

		if (order.getHasItems().equals("0")) {

			String formaPago = "N/A";
			if (oc.getPaymentData().getTransactions() != null && !oc.getPaymentData().getTransactions().isEmpty()) {
				for (Transaction tr : oc.getPaymentData().getTransactions()) {
					if (tr.getPayments() != null && !tr.getPayments().isEmpty()) {
						for (Payment py : tr.getPayments()) {
							formaPago = py.getPaymentSystemName();
							if (py.getPaymentSystemName()
									.equals(PaymentEnum.PAGO_CONTRA_ENTREGA.getPaymentSystemName())) {
								formaPago = formaPago + ": " + PagoEnum.EFECTIVO.getDescripcionSRI();
							} else if (py.getPaymentSystemName().equals(PaymentEnum.SAFETYPAY.getPaymentSystemName())) {
								formaPago = formaPago + ": " + PagoEnum.TRANSFER_OTRO_BANCO.getDescripcionSRI();
							} else if (py.getPaymentSystemName().equals(
									PaymentEnum.TRANSFERENCIA_BANCARIA_OTRAS_ENTIDADES.getPaymentSystemName())) {
								formaPago = formaPago + ": " + PagoEnum.TRANSFER_OTRO_BANCO.getDescripcionSRI();
							} else if (py.getPaymentSystemName().equals(PaymentEnum.PAYCLUB.getPaymentSystemName())) {
								formaPago = formaPago + ": " + PagoEnum.TARJETA_CREDITO_NACIONAL.getDescripcionSRI();
							} else if (py.getPaymentSystemName()
									.equals(PaymentEnum.TARJETA_ALIA.getPaymentSystemName())) {
								formaPago = formaPago + ": " + PagoEnum.TARJETA_CREDITO_NACIONAL.getDescripcionSRI();
							} else if (py.getPaymentSystemName()
									.equals(PaymentEnum.TARJETA_CREDITO.getPaymentSystemName())) {
								formaPago = formaPago + ": " + PagoEnum.TARJETA_CREDITO_NACIONAL.getDescripcionSRI();
							} else if (py.getPaymentSystemName().equals(PaymentEnum.PAYPAL.getPaymentSystemName())) {
								formaPago = formaPago + ": "
										+ PagoEnum.TARJETA_CREDITO_INTERNACIONAL.getDescripcionSRI();
							}

						} // fin for

					} //
				}
			}

			for (SupplierDeliveryInfo sdi : oc.getSupplierDeliveryInfoList()) {
				for (ItemComplete ic : sdi.getItems()) {
					OrderItem oi = new OrderItem();
					oi.setOrder(order);
					oi.setCustomerName(oc.getCustomerName());
					oi.setCustomerDocument(oc.getClientProfileData().getDocument());
					oi.setCustomerPhone(oc.getClientProfileData().getPhone());
					oi.setCustomerState(oc.getShippingData().getAddress().getState());
					oi.setCustomerCanton(oc.getShippingData().getAddress().getCity());
					String address = oc.getShippingData().getAddress().getStreet() + " "
							+ oc.getShippingData().getAddress().getNumber() + " "
							+ oc.getShippingData().getAddress().getComplement();
					Double deliveryPayment = 0D;
					Boolean hasAdjunto = false;
					if (oc.getPaymentData().getTransactions() != null
							&& !oc.getPaymentData().getTransactions().isEmpty()) {
						for (Transaction tr : oc.getPaymentData().getTransactions()) {
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
					}

					Double itemValue = 0D;

					itemValue = itemValue + ic.getPrice() * ic.getQuantity();
					itemValue = (double) Math.round(itemValue * 100) / 100;

					Double discount = 0D;
					Boolean hasTax = Boolean.FALSE;
					if (ic.getPriceTags() != null && !ic.getPriceTags().isEmpty()) {
						for (PriceTag pt : ic.getPriceTags()) {
							if (pt.getName().contains("discount@price")) {
								Double val = pt.getValue();
								if (val.intValue() < 0) {
									val = val * (-1);
								}
								val = (double) Math.round(val * 100) / 100;
								discount = val;
								// break;
							}
							if (pt.getName().contains("tax@price")) {
								hasTax = Boolean.TRUE;
							}
							if (pt.getName().contains("discount@price")) {
								Double val = pt.getValue();
								if (val < 0) {
									val = val * (-1);
								}
								val = (double) Math.round(val * 100) / 100;
								discount = val;
							}
						}
					} else {
						discount = 0D;
					}

					if (ic.getShippingPrice() != null) {
						systemOut.println("shippingPrice " + ic.getShippingPrice());
					} else {
						systemOut.println("Sin costo de cobro de envio");
						// carga.setValorCobro(0D);
					}
					Double iva = 0D;
					itemValue = itemValue - discount;
					if (ic.getTax().intValue() > 0) {
						iva = ic.getTax();
					} else {
						if (hasTax) {
							iva = BaseUtil.calculateIVA(itemValue, new Integer(datilIvaValue), datilIvaPercentValue);
						}
					}
					itemValue = itemValue + iva;

					oi.setCustomerAddress(address);
					try {
						oi.setSupplierName(sdi.getSupplier().getName());
						oi.setSupplier(sdi.getSupplier());
						oi.setIsWarehouse(sdi.getSupplier().getIsWarehouse());
					} catch (EntityNotFoundException e) {
						YaEstaLog yaestalog = new YaEstaLog();
						yaestalog.setLogDate(new Date());
						yaestalog.setProcessName("WAYBILL-PDF-TRAMACO");
						yaestalog.setProcessName("SUPPLIER-QUERY");
						yaestalog.setTextinfo("Error: NO existe proveedor " + sdi.getSupplierId());
						yaestalog.setOrderId(oc.getOrderId());
						logService.save(yaestalog);
					}
					oi.setVitexId(oc.getOrderId());
					oi.setUnitPrice(ic.getPrice());
					oi.setDiscount(discount);
					oi.setPrice(ic.getPrice() * ic.getQuantity());
					oi.setCustomerValue(itemValue);
					oi.setQuantity(new Long(ic.getQuantity()));
					oi.setProductDescription(ic.getName());
					String[] productKey = SupplierUtil.returnSupplierCode((String) ic.getRefId());
					oi.setProductKey(productKey[2]);
					oi.setOrderSequence(oc.getSequence());
					oi.setOrderDate(UtilDate.fromIsoToDateTime(oc.getCreationDate()));
					oi.setWayToPay(formaPago);
					oi.setOrderStatus(oc.getStatus());
					oi.setStatusDescription(oc.getStatusDescription());
					oi.setCustomerName(oc.getCustomerName());
					oi.setSkuId(ic.getSellerSku());
					oi.setItemVTexId(ic.getId());
					if (hasAdjunto) {
						oi.setValueReceivables(itemValue);
					} else {
						oi.setValueReceivables(0D);
					}

					if (ic.getAdditionalInfo() != null) {
						oi.setBrandName(ic.getAdditionalInfo().getBrandName());
						CategoryVtex cvt = categoryVitexService
								.getCategoryFromPath(ic.getAdditionalInfo().getCategoriesIds());
						if (cvt != null) {
							oi.setCategoryName(cvt.getName());
						}
					}

					orderService.saveOrderItem(oi);
				}
			}
			order.setHasItems("1");
		}
		orderService.saveOrder(order);
	}

	private String determineDeliveryName(Catalog delivery) {
		String result = "";
		if (delivery != null) {
			for (DeliveryEnum de : DeliveryEnum.values()) {
				if (de.getNemonic().equals(delivery.getNemonic())) {
					result = delivery.getName();
					break;
				}
			}
		}
		return result;
	}

	private String formatGuideNumber(String sequence) {
		while (sequence.length() < 9) {
			sequence = "0" + sequence;
		}

		sequence = datilEstablishmentCode + "-" + datilEmissionCode + "-" + sequence;

		return sequence;
	}

	public OrderResponseBean processPendingOrder(OrderComplete orderComplete) {

		OrderResponseBean orb = new OrderResponseBean();
		orb.setResponse("OK");
		try {
			Order order = orderService.findByVitexId(orderComplete.getOrderId());

			if (order != null) {
				order.setPending(orderComplete.getPending());
				order.setMotivePending(orderComplete.getMotivePendingText());
				order.setPendingSolution(orderComplete.getPendingSolutionText());
				order.setUpdDate(new Date());
				orderService.saveOrder(order);
				orb.setMessage("Order Pendiente Actualizada");
				orb.setOrderId(order.getId());
				orb.setOrderIdVtex(orderComplete.getOrderId());
			}

		} catch (Exception e) {
			orb.setResponse("Error procesando pendiente orden");
			orb.setOrderIdVtex(orderComplete.getOrderId());
		}
		return orb;
	}

	public List<OrderComplete> getPendingOrders() {
		List<OrderComplete> result = new ArrayList<OrderComplete>();

		List<Order> found = orderService.getPendingOrders();
		if (found != null && !found.isEmpty()) {
			for (Order o : found) {
				OrderComplete oc = this.getOrderComplete(o.getVitexId());
				oc.setPending(o.getPending());
				oc.setMotivePendingText(o.getMotivePending());
				oc.setPendingSolutionText(o.getPendingSolution());
				result.add(oc);
			}
		}
		return result;
	}

	public List<OrderComplete> getPendingInvoiceOrders(String flag) {
		List<OrderComplete> result = new ArrayList<OrderComplete>();

		List<Order> found = orderService.getPendingInvoiceOrders(flag);
		if (found != null && !found.isEmpty()) {
			for (Order o : found) {
				OrderComplete oc = this.getOrderComplete(o.getVitexId());
				oc.setPending(o.getPending());
				oc.setMotivePendingText(o.getMotivePending());
				oc.setPendingSolutionText(o.getPendingSolution());
				result.add(oc);
			}
		}
		return result;
	}

	private void sendGuideMailCustomer(OrderComplete orderComplete) {

		MailInfo mailInfo = new MailInfo();
		mailInfo.setReferenceId(orderComplete.getOrderId());

		MailParticipant sender = new MailParticipant();
		sender.setName("YaEsta.com");
		sender.setEmail(mailFrom);
		mailInfo.setMailSender(sender);

		MailParticipant receiver = new MailParticipant();
		receiver.setName(orderComplete.getCustomerName());
		receiver.setEmail(orderComplete.getClientProfileData().getEmail());
		mailInfo.setMailReceiver(receiver);

		String delivery = "";
		List<Guide> guides = guideService.findByOrderId(orderComplete.getOrderId());
		List<String> guideFiles = new ArrayList<String>();
		if (guides != null && !guides.isEmpty()) {
			for (Guide g : guides) {
				guideFiles.add(g.getDocumentUrl());
				delivery = g.getDeliveryName();
			}

		}

		String subject = "Notificación de inicio de despacho de pedido " + " - Orden: " + orderComplete.getOrderId();
		mailInfo.setSubject(subject);

		String mailText = mailTextGuideCustomer.replace(mailTextGuideToken, orderComplete.getCustomerName());
		mailInfo.setGeneralText(mailText);

		List<String> texts = new ArrayList<String>();

		String text2 = mailTextGuideCustomer2;
		String text3 = mailTextGuideCustomer3;
		String text4 = mailTextGuideCustomer4;
		String text5 = mailTextGuideCustomer5;

		if (delivery.equals("TRAMACO")) {
			text2 = mailTextGuideCustomer2.replace(mailTextGuideToken, tramacoTrackingUrl);
		}

		texts.add(text2);
		texts.add(text3);
		texts.add(text4);
		texts.add(text5);

		mailInfo.setTextList(texts);

		mailInfo.setAttachmentList(guideFiles);

		mailService.sendMailTemplate(mailInfo, "guideCustomerNotification.vm");

	}

	public OrderSchemaContainerBean getVitexOrders(Long maxPages) {
		OrderSchemaContainerBean oscb = new OrderSchemaContainerBean();
		OrderSchema os = getOrdersRest(null);

		if (os != null) {
			oscb.getSchemaList().add(os);
			oscb.setTotalPages(new Long(os.getPaging().getPages()));

			if (maxPages == null) {
				maxPages = new Long(vitexRestMaxPages);
			}

			oscb.getSearchPages().add(1L);

			if (os.getList() != null && !os.getList().isEmpty()) {
				oscb.getOrderBeanList().addAll(os.getList());
			}

			Long count = 2L;

			while (count <= maxPages) {

				OrderSchema ordSc = getOrdersRestPage(null, count);
				if (ordSc != null) {
					oscb.getOrderBeanList().addAll(ordSc.getList());
					oscb.getSchemaList().add(ordSc);
					oscb.getSearchPages().add(count);

				}
				count++;
			}

		}

		try {

			ObjectMapper mapper = new ObjectMapper();

			String json = new Gson().toJson(oscb);

			mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Object oJson = mapper.readValue(json, OrderSchemaContainerBean.class);
			String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(oJson);
			String fileName = yaestaLogPath + yaestaPrefix + "PAGING" + (new Date()).getTime() + ".txt";
			FileUtils.writeStringToFile(new File(fileName), indented);

		} catch (IOException e) {

			YaEstaLog yaestalog = new YaEstaLog();
			yaestalog.setLogDate(new Date());
			yaestalog.setProcessName("ORDER_FEED-QUERYPAGE");
			yaestalog.setTextinfo("Error consulta VTEX ordenes");
			// yaestalog.setXmlInfo(json);
			logService.save(yaestalog);

			e.printStackTrace();
		}

		return oscb;
	}

	private void updateDetails(OrderComplete oc) {
		Order order = orderService.findByVitexId(oc.getOrderId());
		List<Guide> guides = guideService.findByOrder(order);
		List<OrderItem> items = orderService.getOrderItems(order);
		int numItems = 1;
		if(items!=null && !items.isEmpty()){
			numItems = items.size();
		}

		if (guides != null && !guides.isEmpty()) {
			for (Guide guide : guides) {
				
				Double partialItemValue =0D;
				if(guide.getDeliveryPayment()!=null){
					partialItemValue = guide.getDeliveryPayment().doubleValue()/numItems;
				}
				List<GuideDetail> details = guideService.getGuideDetails(guide);
				if (details != null && !details.isEmpty())
					for (GuideDetail detail : details) {
						for (OrderItem oi : items) {
							if (detail.getVitexId().equals(oi.getSkuId())) {
								oi.setGuideDate(guide.getCreateDate());
								String strDate = UtilDate.fromDateToString(guide.getCreateDate(), "yyyy-MM-dd");
								oi.setStrGuideDate(strDate);
								if (guide.getGuideNumber() != null && oi.getGuideNumber() == null) {
									oi.setGuideNumber(guide.getGuideNumber());
								}
								oi.setProgrammedDate(guide.getProgrammedDate());
								if(guide.getDeliveryPayment()!=null){
									oi.setGuideValue(guide.getDeliveryPayment());
									oi.setItemPartialValue(partialItemValue);
								}
								orderService.updateOrderItem(oi);
							}
						}
					}
			}
		}
	}

}
