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

import com.yaesta.integration.vitex.wsdl.dto.ArrayOfStockKeepingUnitQuantityDTO;


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
 *         &lt;element name="giftListId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="skuQuantity" type="{http://schemas.datacontract.org/2004/07/Vtex.Commerce.WebApps.AdminWcfService.Contracts}ArrayOfStockKeepingUnitQuantityDTO" minOccurs="0"/>
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
    "giftListId",
    "skuQuantity"
})
@XmlRootElement(name = "GiftListSkuDeleteByList")
public class GiftListSkuDeleteByList {

    protected Integer giftListId;
    @XmlElementRef(name = "skuQuantity", namespace = "http://tempuri.org/", type = JAXBElement.class)
    protected JAXBElement<ArrayOfStockKeepingUnitQuantityDTO> skuQuantity;

    /**
     * Gets the value of the giftListId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getGiftListId() {
        return giftListId;
    }

    /**
     * Sets the value of the giftListId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setGiftListId(Integer value) {
        this.giftListId = value;
    }

    /**
     * Gets the value of the skuQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfStockKeepingUnitQuantityDTO }{@code >}
     *     
     */
    public JAXBElement<ArrayOfStockKeepingUnitQuantityDTO> getSkuQuantity() {
        return skuQuantity;
    }

    /**
     * Sets the value of the skuQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfStockKeepingUnitQuantityDTO }{@code >}
     *     
     */
    public void setSkuQuantity(JAXBElement<ArrayOfStockKeepingUnitQuantityDTO> value) {
        this.skuQuantity = ((JAXBElement<ArrayOfStockKeepingUnitQuantityDTO> ) value);
    }

}
