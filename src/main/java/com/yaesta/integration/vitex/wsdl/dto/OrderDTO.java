//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.14 at 08:35:23 PM ECT 
//


package com.yaesta.integration.vitex.wsdl.dto;

import java.math.BigDecimal;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import com.yaesta.integration.vitex.wsdl.DateTimeOffset;


/**
 * <p>Java class for OrderDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Address" type="{http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts}AddressOrderDTO" minOccurs="0"/>
 *         &lt;element name="AffiliateId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Campaign" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Client" type="{http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts}ClientDTO" minOccurs="0"/>
 *         &lt;element name="ClientIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ClientId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Coupon" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GiftListStockKeepingUnits" type="{http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts}ArrayOfGiftListStockKeepingUnitDTO" minOccurs="0"/>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="IdV3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IsGiftList" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IsMultiStore" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="Keywords" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ListOrderDiscount" type="{http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts}ArrayOfOrderDiscountDTO" minOccurs="0"/>
 *         &lt;element name="Media" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OrderDeliveries" type="{http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts}ArrayOfOrderDeliveryDTO" minOccurs="0"/>
 *         &lt;element name="OrderInvoices" type="{http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts}ArrayOfOrderInvoiceDTO" minOccurs="0"/>
 *         &lt;element name="OrderPayments" type="{http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts}ArrayOfOrderPaymentDTO" minOccurs="0"/>
 *         &lt;element name="PurchaseDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="PurchaseDateUtc" type="{http://schemas.datacontract.org/2004/07/System}DateTimeOffset" minOccurs="0"/>
 *         &lt;element name="Source" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StoreId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="StoreName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Tax" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="TelemarketingId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="TelemarketingLogin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TelemarketingObservation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TelemarketingStoreLocatorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TelemarketingStoreLocatorName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="_Cost" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="_CostFinal" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderDTO", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", propOrder = {
    "address",
    "affiliateId",
    "campaign",
    "client",
    "clientIP",
    "clientId",
    "coupon",
    "giftListStockKeepingUnits",
    "id",
    "idV3",
    "isGiftList",
    "isMultiStore",
    "keywords",
    "listOrderDiscount",
    "media",
    "orderDeliveries",
    "orderInvoices",
    "orderPayments",
    "purchaseDate",
    "purchaseDateUtc",
    "source",
    "storeId",
    "storeName",
    "tax",
    "telemarketingId",
    "telemarketingLogin",
    "telemarketingObservation",
    "telemarketingStoreLocatorCode",
    "telemarketingStoreLocatorName",
    "cost",
    "costFinal"
})
public class OrderDTO {

    @XmlElementRef(name = "Address", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<AddressOrderDTO> address;
    @XmlElementRef(name = "AffiliateId", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<String> affiliateId;
    @XmlElementRef(name = "Campaign", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<String> campaign;
    @XmlElementRef(name = "Client", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<ClientDTO> client;
    @XmlElementRef(name = "ClientIP", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<String> clientIP;
    @XmlElementRef(name = "ClientId", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<Integer> clientId;
    @XmlElementRef(name = "Coupon", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<String> coupon;
    @XmlElementRef(name = "GiftListStockKeepingUnits", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<ArrayOfGiftListStockKeepingUnitDTO> giftListStockKeepingUnits;
    @XmlElementRef(name = "Id", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<Integer> id;
    @XmlElementRef(name = "IdV3", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<String> idV3;
    @XmlElement(name = "IsGiftList")
    protected Boolean isGiftList;
    @XmlElement(name = "IsMultiStore")
    protected Boolean isMultiStore;
    @XmlElementRef(name = "Keywords", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<String> keywords;
    @XmlElementRef(name = "ListOrderDiscount", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<ArrayOfOrderDiscountDTO> listOrderDiscount;
    @XmlElementRef(name = "Media", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<String> media;
    @XmlElementRef(name = "OrderDeliveries", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<ArrayOfOrderDeliveryDTO> orderDeliveries;
    @XmlElementRef(name = "OrderInvoices", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<ArrayOfOrderInvoiceDTO> orderInvoices;
    @XmlElementRef(name = "OrderPayments", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<ArrayOfOrderPaymentDTO> orderPayments;
    @XmlElementRef(name = "PurchaseDate", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<XMLGregorianCalendar> purchaseDate;
    @XmlElementRef(name = "PurchaseDateUtc", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<DateTimeOffset> purchaseDateUtc;
    @XmlElementRef(name = "Source", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<String> source;
    @XmlElement(name = "StoreId")
    protected Integer storeId;
    @XmlElementRef(name = "StoreName", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<String> storeName;
    @XmlElementRef(name = "Tax", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<BigDecimal> tax;
    @XmlElementRef(name = "TelemarketingId", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<Integer> telemarketingId;
    @XmlElementRef(name = "TelemarketingLogin", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<String> telemarketingLogin;
    @XmlElementRef(name = "TelemarketingObservation", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<String> telemarketingObservation;
    @XmlElementRef(name = "TelemarketingStoreLocatorCode", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<String> telemarketingStoreLocatorCode;
    @XmlElementRef(name = "TelemarketingStoreLocatorName", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<String> telemarketingStoreLocatorName;
    @XmlElementRef(name = "_Cost", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<BigDecimal> cost;
    @XmlElementRef(name = "_CostFinal", namespace = "http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts", type = JAXBElement.class)
    protected JAXBElement<BigDecimal> costFinal;

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AddressOrderDTO }{@code >}
     *     
     */
    public JAXBElement<AddressOrderDTO> getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AddressOrderDTO }{@code >}
     *     
     */
    public void setAddress(JAXBElement<AddressOrderDTO> value) {
        this.address = ((JAXBElement<AddressOrderDTO> ) value);
    }

    /**
     * Gets the value of the affiliateId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAffiliateId() {
        return affiliateId;
    }

    /**
     * Sets the value of the affiliateId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAffiliateId(JAXBElement<String> value) {
        this.affiliateId = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the campaign property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCampaign() {
        return campaign;
    }

    /**
     * Sets the value of the campaign property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCampaign(JAXBElement<String> value) {
        this.campaign = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the client property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ClientDTO }{@code >}
     *     
     */
    public JAXBElement<ClientDTO> getClient() {
        return client;
    }

    /**
     * Sets the value of the client property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ClientDTO }{@code >}
     *     
     */
    public void setClient(JAXBElement<ClientDTO> value) {
        this.client = ((JAXBElement<ClientDTO> ) value);
    }

    /**
     * Gets the value of the clientIP property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getClientIP() {
        return clientIP;
    }

    /**
     * Sets the value of the clientIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setClientIP(JAXBElement<String> value) {
        this.clientIP = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the clientId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getClientId() {
        return clientId;
    }

    /**
     * Sets the value of the clientId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setClientId(JAXBElement<Integer> value) {
        this.clientId = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the coupon property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCoupon() {
        return coupon;
    }

    /**
     * Sets the value of the coupon property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCoupon(JAXBElement<String> value) {
        this.coupon = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the giftListStockKeepingUnits property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfGiftListStockKeepingUnitDTO }{@code >}
     *     
     */
    public JAXBElement<ArrayOfGiftListStockKeepingUnitDTO> getGiftListStockKeepingUnits() {
        return giftListStockKeepingUnits;
    }

    /**
     * Sets the value of the giftListStockKeepingUnits property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfGiftListStockKeepingUnitDTO }{@code >}
     *     
     */
    public void setGiftListStockKeepingUnits(JAXBElement<ArrayOfGiftListStockKeepingUnitDTO> value) {
        this.giftListStockKeepingUnits = ((JAXBElement<ArrayOfGiftListStockKeepingUnitDTO> ) value);
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setId(JAXBElement<Integer> value) {
        this.id = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the idV3 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIdV3() {
        return idV3;
    }

    /**
     * Sets the value of the idV3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIdV3(JAXBElement<String> value) {
        this.idV3 = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the isGiftList property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsGiftList() {
        return isGiftList;
    }

    /**
     * Sets the value of the isGiftList property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsGiftList(Boolean value) {
        this.isGiftList = value;
    }

    /**
     * Gets the value of the isMultiStore property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsMultiStore() {
        return isMultiStore;
    }

    /**
     * Sets the value of the isMultiStore property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsMultiStore(Boolean value) {
        this.isMultiStore = value;
    }

    /**
     * Gets the value of the keywords property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getKeywords() {
        return keywords;
    }

    /**
     * Sets the value of the keywords property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setKeywords(JAXBElement<String> value) {
        this.keywords = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the listOrderDiscount property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfOrderDiscountDTO }{@code >}
     *     
     */
    public JAXBElement<ArrayOfOrderDiscountDTO> getListOrderDiscount() {
        return listOrderDiscount;
    }

    /**
     * Sets the value of the listOrderDiscount property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfOrderDiscountDTO }{@code >}
     *     
     */
    public void setListOrderDiscount(JAXBElement<ArrayOfOrderDiscountDTO> value) {
        this.listOrderDiscount = ((JAXBElement<ArrayOfOrderDiscountDTO> ) value);
    }

    /**
     * Gets the value of the media property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMedia() {
        return media;
    }

    /**
     * Sets the value of the media property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMedia(JAXBElement<String> value) {
        this.media = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the orderDeliveries property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfOrderDeliveryDTO }{@code >}
     *     
     */
    public JAXBElement<ArrayOfOrderDeliveryDTO> getOrderDeliveries() {
        return orderDeliveries;
    }

    /**
     * Sets the value of the orderDeliveries property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfOrderDeliveryDTO }{@code >}
     *     
     */
    public void setOrderDeliveries(JAXBElement<ArrayOfOrderDeliveryDTO> value) {
        this.orderDeliveries = ((JAXBElement<ArrayOfOrderDeliveryDTO> ) value);
    }

    /**
     * Gets the value of the orderInvoices property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfOrderInvoiceDTO }{@code >}
     *     
     */
    public JAXBElement<ArrayOfOrderInvoiceDTO> getOrderInvoices() {
        return orderInvoices;
    }

    /**
     * Sets the value of the orderInvoices property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfOrderInvoiceDTO }{@code >}
     *     
     */
    public void setOrderInvoices(JAXBElement<ArrayOfOrderInvoiceDTO> value) {
        this.orderInvoices = ((JAXBElement<ArrayOfOrderInvoiceDTO> ) value);
    }

    /**
     * Gets the value of the orderPayments property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfOrderPaymentDTO }{@code >}
     *     
     */
    public JAXBElement<ArrayOfOrderPaymentDTO> getOrderPayments() {
        return orderPayments;
    }

    /**
     * Sets the value of the orderPayments property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfOrderPaymentDTO }{@code >}
     *     
     */
    public void setOrderPayments(JAXBElement<ArrayOfOrderPaymentDTO> value) {
        this.orderPayments = ((JAXBElement<ArrayOfOrderPaymentDTO> ) value);
    }

    /**
     * Gets the value of the purchaseDate property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * Sets the value of the purchaseDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setPurchaseDate(JAXBElement<XMLGregorianCalendar> value) {
        this.purchaseDate = ((JAXBElement<XMLGregorianCalendar> ) value);
    }

    /**
     * Gets the value of the purchaseDateUtc property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DateTimeOffset }{@code >}
     *     
     */
    public JAXBElement<DateTimeOffset> getPurchaseDateUtc() {
        return purchaseDateUtc;
    }

    /**
     * Sets the value of the purchaseDateUtc property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DateTimeOffset }{@code >}
     *     
     */
    public void setPurchaseDateUtc(JAXBElement<DateTimeOffset> value) {
        this.purchaseDateUtc = ((JAXBElement<DateTimeOffset> ) value);
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSource(JAXBElement<String> value) {
        this.source = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the storeId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getStoreId() {
        return storeId;
    }

    /**
     * Sets the value of the storeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setStoreId(Integer value) {
        this.storeId = value;
    }

    /**
     * Gets the value of the storeName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getStoreName() {
        return storeName;
    }

    /**
     * Sets the value of the storeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setStoreName(JAXBElement<String> value) {
        this.storeName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the tax property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public JAXBElement<BigDecimal> getTax() {
        return tax;
    }

    /**
     * Sets the value of the tax property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public void setTax(JAXBElement<BigDecimal> value) {
        this.tax = ((JAXBElement<BigDecimal> ) value);
    }

    /**
     * Gets the value of the telemarketingId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getTelemarketingId() {
        return telemarketingId;
    }

    /**
     * Sets the value of the telemarketingId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setTelemarketingId(JAXBElement<Integer> value) {
        this.telemarketingId = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the telemarketingLogin property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTelemarketingLogin() {
        return telemarketingLogin;
    }

    /**
     * Sets the value of the telemarketingLogin property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTelemarketingLogin(JAXBElement<String> value) {
        this.telemarketingLogin = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the telemarketingObservation property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTelemarketingObservation() {
        return telemarketingObservation;
    }

    /**
     * Sets the value of the telemarketingObservation property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTelemarketingObservation(JAXBElement<String> value) {
        this.telemarketingObservation = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the telemarketingStoreLocatorCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTelemarketingStoreLocatorCode() {
        return telemarketingStoreLocatorCode;
    }

    /**
     * Sets the value of the telemarketingStoreLocatorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTelemarketingStoreLocatorCode(JAXBElement<String> value) {
        this.telemarketingStoreLocatorCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the telemarketingStoreLocatorName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTelemarketingStoreLocatorName() {
        return telemarketingStoreLocatorName;
    }

    /**
     * Sets the value of the telemarketingStoreLocatorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTelemarketingStoreLocatorName(JAXBElement<String> value) {
        this.telemarketingStoreLocatorName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the cost property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public JAXBElement<BigDecimal> getCost() {
        return cost;
    }

    /**
     * Sets the value of the cost property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public void setCost(JAXBElement<BigDecimal> value) {
        this.cost = ((JAXBElement<BigDecimal> ) value);
    }

    /**
     * Gets the value of the costFinal property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public JAXBElement<BigDecimal> getCostFinal() {
        return costFinal;
    }

    /**
     * Sets the value of the costFinal property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public void setCostFinal(JAXBElement<BigDecimal> value) {
        this.costFinal = ((JAXBElement<BigDecimal> ) value);
    }

}
