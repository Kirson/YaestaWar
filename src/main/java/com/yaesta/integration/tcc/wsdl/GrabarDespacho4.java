//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2016.09.06 a las 03:08:21 PM ECT 
//


package com.yaesta.integration.tcc.wsdl;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="objDespacho" type="{http://www.tcc.com.co/xsd}tpGrabarRemesaCompleta" minOccurs="0"/&gt;
 *         &lt;element name="remesa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="URLRelacionEnvio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="URLRotulos" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="URLRemesa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IMGRelacionEnvio" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *         &lt;element name="IMGRotulos" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *         &lt;element name="IMGRemesa" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *         &lt;element name="respuesta" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="mensaje" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GrabarDespacho4",  namespace = "http://clientes.tcc.com.co/", propOrder = {
    "objDespacho",
    "remesa",
    "URLRelacionEnvio",
    "URLRotulos",
    "URLRemesa",
    "IMGRelacionEnvio",
    "IMGRotulos",
    "IMGRemesa",
    "respuesta",
    "mensaje"
})
@XmlRootElement(name = "GrabarDespacho4")
public class GrabarDespacho4 implements Serializable {

    /**
	 * Serial version
	 */
	private static final long serialVersionUID = 5534771890405966493L;
	protected TpGrabarRemesaCompleta objDespacho;
    protected String remesa;
    @XmlElement(name = "URLRelacionEnvio")
    protected String URLRelacionEnvio;
    @XmlElement(name = "URLRotulos")
    protected String URLRotulos;
    @XmlElement(name = "URLRemesa")
    protected String URLRemesa;
    @XmlElement(name = "IMGRelacionEnvio")
    protected byte[] IMGRelacionEnvio;
    @XmlElement(name = "IMGRotulos")
    protected byte[] IMGRotulos;
    @XmlElement(name = "IMGRemesa")
    protected byte[] IMGRemesa;
    protected int respuesta;
    protected String mensaje;

    /**
     * Obtiene el valor de la propiedad objDespacho.
     * 
     * @return
     *     possible object is
     *     {@link TpGrabarRemesaCompleta }
     *     
     */
    public TpGrabarRemesaCompleta getObjDespacho() {
        return objDespacho;
    }

    /**
     * Define el valor de la propiedad objDespacho.
     * 
     * @param value
     *     allowed object is
     *     {@link TpGrabarRemesaCompleta }
     *     
     */
    public void setObjDespacho(TpGrabarRemesaCompleta value) {
        this.objDespacho = value;
    }

    /**
     * Obtiene el valor de la propiedad remesa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemesa() {
        return remesa;
    }

    /**
     * Define el valor de la propiedad remesa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemesa(String value) {
        this.remesa = value;
    }

    
  

    /**
     * Obtiene el valor de la propiedad respuesta.
     * 
     */
    public int getRespuesta() {
        return respuesta;
    }

    /**
     * Define el valor de la propiedad respuesta.
     * 
     */
    public void setRespuesta(int value) {
        this.respuesta = value;
    }

    /**
     * Obtiene el valor de la propiedad mensaje.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Define el valor de la propiedad mensaje.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMensaje(String value) {
        this.mensaje = value;
    }

	public String getURLRelacionEnvio() {
		return URLRelacionEnvio;
	}

	public void setURLRelacionEnvio(String uRLRelacionEnvio) {
		URLRelacionEnvio = uRLRelacionEnvio;
	}

	public String getURLRotulos() {
		return URLRotulos;
	}

	public void setURLRotulos(String uRLRotulos) {
		URLRotulos = uRLRotulos;
	}

	public String getURLRemesa() {
		return URLRemesa;
	}

	public void setURLRemesa(String uRLRemesa) {
		URLRemesa = uRLRemesa;
	}

	public byte[] getIMGRelacionEnvio() {
		return IMGRelacionEnvio;
	}

	public void setIMGRelacionEnvio(byte[] iMGRelacionEnvio) {
		IMGRelacionEnvio = iMGRelacionEnvio;
	}

	public byte[] getIMGRotulos() {
		return IMGRotulos;
	}

	public void setIMGRotulos(byte[] iMGRotulos) {
		IMGRotulos = iMGRotulos;
	}

	public byte[] getIMGRemesa() {
		return IMGRemesa;
	}

	public void setIMGRemesa(byte[] iMGRemesa) {
		IMGRemesa = iMGRemesa;
	}
    
    

}
