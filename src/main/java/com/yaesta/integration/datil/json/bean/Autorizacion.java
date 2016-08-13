
package com.yaesta.integration.datil.json.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "estado",
    "mensajes",
    "numero",
    "fecha"
})
public class Autorizacion {

    @JsonProperty("estado")
    private String estado;
    @JsonProperty("mensajes")
    private List<Mensaje> mensajes = new ArrayList<Mensaje>();
    @JsonProperty("numero")
    private String numero;
    @JsonProperty("fecha")
    private String fecha;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The estado
     */
    @JsonProperty("estado")
    public String getEstado() {
        return estado;
    }

    /**
     * 
     * @param estado
     *     The estado
     */
    @JsonProperty("estado")
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * 
     * @return
     *     The mensajes
     */
    @JsonProperty("mensajes")
    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    /**
     * 
     * @param mensajes
     *     The mensajes
     */
    @JsonProperty("mensajes")
    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    /**
     * 
     * @return
     *     The numero
     */
    @JsonProperty("numero")
    public String getNumero() {
        return numero;
    }

    /**
     * 
     * @param numero
     *     The numero
     */
    @JsonProperty("numero")
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * 
     * @return
     *     The fecha
     */
    @JsonProperty("fecha")
    public String getFecha() {
        return fecha;
    }

    /**
     * 
     * @param fecha
     *     The fecha
     */
    @JsonProperty("fecha")
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
