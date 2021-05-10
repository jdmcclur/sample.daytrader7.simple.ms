/**
 * This is a Cardinal generated proxy
 */

/**
 * (C) Copyright IBM Corporation 2015.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.websphere.samples.daytrader.util;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

import com.ibm.cardinal.util.CardinalException;
import com.ibm.cardinal.util.CardinalLogger;
import com.ibm.cardinal.util.CardinalString;
import com.ibm.cardinal.util.ClusterObjectManager;
import com.ibm.cardinal.util.KluInterface;
import com.ibm.cardinal.util.SerializationUtil;

import com.ibm.websphere.samples.daytrader.entities.HoldingDataBean;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

public class FinancialUtils implements KluInterface {
    public static final int ROUND = BigDecimal.ROUND_HALF_UP;
    public static final int SCALE = 2;
    public static final BigDecimal ZERO = (new BigDecimal(0.00)).setScale(SCALE);
    public static final BigDecimal ONE = (new BigDecimal(1.00)).setScale(SCALE);
    public static final BigDecimal HUNDRED = (new BigDecimal(100.00)).setScale(SCALE);

    private String klu__referenceID = "";
    private static String klu__serviceURI;
    private static Client klu__client;
    private static final Logger klu__logger = CardinalLogger.getLogger(FinancialUtils.class);

    static {
        klu__client = ClientBuilder.newClient();

        klu__logger.info("Static initializer of FinancialUtils of cluster frontend_service");

        klu__serviceURI = System.getenv().get("APPLICATION_FRONTEND_SERVICE_REST_URL");
        if (klu__serviceURI == null) {
            throw new java.lang.RuntimeException("Environment variable "+
                "APPLICATION_FRONTEND_SERVICE_REST_URL not set\n"+
                "Please set APPLICATION_FRONTEND_SERVICE_REST_URL to "+
                "frontend_service host:port"
            );
        }

        if (!klu__serviceURI.endsWith("/")) {
            klu__serviceURI += "/";
        }
        klu__serviceURI += "FinancialUtilsService";

        try {
            java.net.URI uri = java.net.URI.create(klu__serviceURI);
        }
        catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException("Invalid URI for partition frontend_service, "+
                "service FinancialUtilsService: "+klu__serviceURI, e);
        }

        klu__logger.info("frontend_service FinancialUtilsService URI = " + klu__serviceURI);
    }

    // default constructor (generated)
    public FinancialUtils() {
        Response svc_response =
            klu__client.target(klu__serviceURI) 
            .path("FinancialUtils_default_ctor") 
            .request(MediaType.APPLICATION_JSON) 
            .post(Entity.text(""), Response.class);
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[FinancialUtils()] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();

    }



    // constructor for creating proxy instances of this class using reference ID
    public FinancialUtils(CardinalString referenceId) {
        setKlu__referenceID(referenceId.getString());
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__client.target(klu__serviceURI)
            .path("incObjectCount")
            .request()
            .post(Entity.form(form));
	}



    public String getKlu__referenceID() {
        return this.klu__referenceID;
    }

    public void setKlu__referenceID(String referenceId) {
        this.klu__referenceID = referenceId;
    }

    // decrement object reference count when this object is garbage collected
    public void finalize() {
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__client.target(klu__serviceURI)
            .path("decObjectCount")
            .request()
            .post(Entity.form(form));
    }




public static BigDecimal computeGain(BigDecimal currentBalance, BigDecimal openBalance) {
        // create form for service request
        Form form = new Form();
        
        // convert physical/proxy object(s) referenced by "currentBalance" to reference ID(s)
        String currentBalance_fpar = SerializationUtil.encodeWithDynamicTypeCheck(currentBalance);
        form.param("currentBalance", currentBalance_fpar);

        
        // convert physical/proxy object(s) referenced by "openBalance" to reference ID(s)
        String openBalance_fpar = SerializationUtil.encodeWithDynamicTypeCheck(openBalance);
        form.param("openBalance", openBalance_fpar);

        klu__logger.info("[FinancialUtils] Calling service "+klu__serviceURI+
            "/computeGain with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("computeGain")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[FinancialUtils] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[FinancialUtils] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        BigDecimal response_obj = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

  public static BigDecimal computeGainPercent(BigDecimal currentBalance, BigDecimal openBalance) {
        // create form for service request
        Form form = new Form();
        
        // convert physical/proxy object(s) referenced by "currentBalance" to reference ID(s)
        String currentBalance_fpar = SerializationUtil.encodeWithDynamicTypeCheck(currentBalance);
        form.param("currentBalance", currentBalance_fpar);

        
        // convert physical/proxy object(s) referenced by "openBalance" to reference ID(s)
        String openBalance_fpar = SerializationUtil.encodeWithDynamicTypeCheck(openBalance);
        form.param("openBalance", openBalance_fpar);

        klu__logger.info("[FinancialUtils] Calling service "+klu__serviceURI+
            "/computeGainPercent with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("computeGainPercent")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[FinancialUtils] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[FinancialUtils] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        BigDecimal response_obj = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

  public static BigDecimal computeHoldingsTotal(Collection<?> holdingDataBeans) {
        // create form for service request
        Form form = new Form();
        
        // convert physical/proxy object(s) referenced by "holdingDataBeans" to reference ID(s)
        String holdingDataBeans_fpar = SerializationUtil.encodeWithDynamicTypeCheck(holdingDataBeans);
        form.param("holdingDataBeans", holdingDataBeans_fpar);

        klu__logger.info("[FinancialUtils] Calling service "+klu__serviceURI+
            "/computeHoldingsTotal with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("computeHoldingsTotal")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[FinancialUtils] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[FinancialUtils] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        BigDecimal response_obj = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

  public static String printGainHTML(BigDecimal gain) {
        // create form for service request
        Form form = new Form();
        
        // convert physical/proxy object(s) referenced by "gain" to reference ID(s)
        String gain_fpar = SerializationUtil.encodeWithDynamicTypeCheck(gain);
        form.param("gain", gain_fpar);

        klu__logger.info("[FinancialUtils] Calling service "+klu__serviceURI+
            "/printGainHTML with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("printGainHTML")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[FinancialUtils] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[FinancialUtils] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

  public static String printChangeHTML(double change) {
        // create form for service request
        Form form = new Form();
        form.param("change", String.valueOf(change)); 

        klu__logger.info("[FinancialUtils] Calling service "+klu__serviceURI+
            "/printChangeHTML with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("printChangeHTML")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[FinancialUtils] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[FinancialUtils] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

  public static String printGainPercentHTML(BigDecimal gain) {
        // create form for service request
        Form form = new Form();
        
        // convert physical/proxy object(s) referenced by "gain" to reference ID(s)
        String gain_fpar = SerializationUtil.encodeWithDynamicTypeCheck(gain);
        form.param("gain", gain_fpar);

        klu__logger.info("[FinancialUtils] Calling service "+klu__serviceURI+
            "/printGainPercentHTML with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("printGainPercentHTML")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[FinancialUtils] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[FinancialUtils] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

  public static String printQuoteLink(String symbol) {
        // create form for service request
        Form form = new Form();
        form.param("symbol", symbol); 

        klu__logger.info("[FinancialUtils] Calling service "+klu__serviceURI+
            "/printQuoteLink with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("printQuoteLink")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[FinancialUtils] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[FinancialUtils] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

}
