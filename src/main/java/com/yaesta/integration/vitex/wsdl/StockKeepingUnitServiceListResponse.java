//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.14 at 08:35:23 PM ECT 
//


package com.yaesta.integration.vitex.wsdl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.yaesta.integration.vitex.wsdl.dto.ArrayOfStockKeepingUnitServiceDTO;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StockKeepingUnitServiceListResult" type="{http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts}ArrayOfStockKeepingUnitServiceDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "stockKeepingUnitServiceListResult"
})
@XmlRootElement(name = "StockKeepingUnitServiceListResponse")
public class StockKeepingUnitServiceListResponse {

    @XmlElementRef(name = "StockKeepingUnitServiceListResult", namespace = "http://tempuri.org/", type = JAXBElement.class)
    protected JAXBElement<ArrayOfStockKeepingUnitServiceDTO> stockKeepingUnitServiceListResult;

    /**
     * Gets the value of the stockKeepingUnitServiceListResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfStockKeepingUnitServiceDTO }{@code >}
     *     
     */
    public JAXBElement<ArrayOfStockKeepingUnitServiceDTO> getStockKeepingUnitServiceListResult() {
        return stockKeepingUnitServiceListResult;
    }

    /**
     * Sets the value of the stockKeepingUnitServiceListResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfStockKeepingUnitServiceDTO }{@code >}
     *     
     */
    public void setStockKeepingUnitServiceListResult(JAXBElement<ArrayOfStockKeepingUnitServiceDTO> value) {
        this.stockKeepingUnitServiceListResult = ((JAXBElement<ArrayOfStockKeepingUnitServiceDTO> ) value);
    }

}
