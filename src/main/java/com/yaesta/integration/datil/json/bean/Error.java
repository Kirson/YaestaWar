
package com.yaesta.integration.datil.json.bean;

import java.util.HashMap;
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
    "details",
    "message",
    "code",
    "parameter"
})
public class Error {

    @JsonProperty("details")
    private String details;
    @JsonProperty("message")
    private String message;
    @JsonProperty("code")
    private String code;
    @JsonProperty("parameter")
    private String parameter;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The details
     */
    @JsonProperty("details")
    public String getDetails() {
        return details;
    }

    /**
     * 
     * @param details
     *     The details
     */
    @JsonProperty("details")
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * 
     * @return
     *     The message
     */
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     *     The message
     */
    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 
     * @return
     *     The code
     */
    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    /**
     * 
     * @param code
     *     The code
     */
    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 
     * @return
     *     The parameter
     */
    @JsonProperty("parameter")
    public String getParameter() {
        return parameter;
    }

    /**
     * 
     * @param parameter
     *     The parameter
     */
    @JsonProperty("parameter")
    public void setParameter(String parameter) {
        this.parameter = parameter;
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
