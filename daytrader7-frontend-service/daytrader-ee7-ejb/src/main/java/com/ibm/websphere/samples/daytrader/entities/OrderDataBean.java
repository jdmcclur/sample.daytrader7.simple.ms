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

package com.ibm.websphere.samples.daytrader.entities;

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

import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;




public class OrderDataBean implements KluInterface, Serializable {
    private static final long serialVersionUID = 120650490200739057L;

    private String klu__referenceID = "";
    private static String klu__serviceURI;
    private static Client klu__client;
    private static final Logger klu__logger = CardinalLogger.getLogger(OrderDataBean.class);

    static {
        klu__client = ClientBuilder.newClient();

        klu__logger.info("Static initializer of OrderDataBean of cluster order_services");

        klu__serviceURI = System.getenv().get("APPLICATION_ORDER_SERVICES_REST_URL");
        if (klu__serviceURI == null) {
            throw new java.lang.RuntimeException("Environment variable "+
                "APPLICATION_ORDER_SERVICES_REST_URL not set\n"+
                "Please set APPLICATION_ORDER_SERVICES_REST_URL to "+
                "order_services host:port"
            );
        }

        if (!klu__serviceURI.endsWith("/")) {
            klu__serviceURI += "/";
        }
        klu__serviceURI += "OrderDataBeanService";

        try {
            java.net.URI uri = java.net.URI.create(klu__serviceURI);
        }
        catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException("Invalid URI for partition order_services, "+
                "service OrderDataBeanService: "+klu__serviceURI, e);
        }

        klu__logger.info("order_services OrderDataBeanService URI = " + klu__serviceURI);
    }




    // constructor for creating proxy instances of this class using reference ID
    public OrderDataBean(CardinalString referenceId) {
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




public OrderDataBean() {
        // create form for service request
        Form form = new Form();
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/OrderDataBean_001 with form: "+form.asMap());

        // call constructor service and store ref ID
        Response svc_response;
        try {
            svc_response = 
                klu__client.target(klu__serviceURI) 
                .path("OrderDataBean_001") 
                .request(MediaType.APPLICATION_JSON) 
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();
    }

  public OrderDataBean(Integer orderID, String orderType, String orderStatus, Date openDate, Date completionDate,
      double quantity, BigDecimal price, BigDecimal orderFee, String symbol) {
        // create form for service request
        Form form = new Form();
        
        // convert physical/proxy object(s) referenced by "orderID" to reference ID(s)
        String orderID_fpar = SerializationUtil.encodeWithDynamicTypeCheck(orderID);
        form.param("orderID", orderID_fpar);

        form.param("orderType", orderType); 

        form.param("orderStatus", orderStatus); 

        
        // convert physical/proxy object(s) referenced by "openDate" to reference ID(s)
        String openDate_fpar = SerializationUtil.encodeWithDynamicTypeCheck(openDate);
        form.param("openDate", openDate_fpar);

        
        // convert physical/proxy object(s) referenced by "completionDate" to reference ID(s)
        String completionDate_fpar = SerializationUtil.encodeWithDynamicTypeCheck(completionDate);
        form.param("completionDate", completionDate_fpar);

        form.param("quantity", String.valueOf(quantity)); 

        
        // convert physical/proxy object(s) referenced by "price" to reference ID(s)
        String price_fpar = SerializationUtil.encodeWithDynamicTypeCheck(price);
        form.param("price", price_fpar);

        
        // convert physical/proxy object(s) referenced by "orderFee" to reference ID(s)
        String orderFee_fpar = SerializationUtil.encodeWithDynamicTypeCheck(orderFee);
        form.param("orderFee", orderFee_fpar);

        form.param("symbol", symbol); 

        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/OrderDataBean_002 with form: "+form.asMap());

        // call constructor service and store ref ID
        Response svc_response;
        try {
            svc_response = 
                klu__client.target(klu__serviceURI) 
                .path("OrderDataBean_002") 
                .request(MediaType.APPLICATION_JSON) 
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();
    }

  public OrderDataBean(String orderType, String orderStatus, Date openDate, Date completionDate, double quantity,
      BigDecimal price, BigDecimal orderFee, AccountDataBean account, QuoteDataBean quote, HoldingDataBean holding) {
        // create form for service request
        Form form = new Form();
        form.param("orderType", orderType); 

        form.param("orderStatus", orderStatus); 

        
        // convert physical/proxy object(s) referenced by "openDate" to reference ID(s)
        String openDate_fpar = SerializationUtil.encodeWithDynamicTypeCheck(openDate);
        form.param("openDate", openDate_fpar);

        
        // convert physical/proxy object(s) referenced by "completionDate" to reference ID(s)
        String completionDate_fpar = SerializationUtil.encodeWithDynamicTypeCheck(completionDate);
        form.param("completionDate", completionDate_fpar);

        form.param("quantity", String.valueOf(quantity)); 

        
        // convert physical/proxy object(s) referenced by "price" to reference ID(s)
        String price_fpar = SerializationUtil.encodeWithDynamicTypeCheck(price);
        form.param("price", price_fpar);

        
        // convert physical/proxy object(s) referenced by "orderFee" to reference ID(s)
        String orderFee_fpar = SerializationUtil.encodeWithDynamicTypeCheck(orderFee);
        form.param("orderFee", orderFee_fpar);

        
        // convert physical/proxy object(s) referenced by "account" to reference ID(s)
        String account_fpar = SerializationUtil.encodeWithDynamicTypeCheck(account);
        form.param("account", account_fpar);

        
        // convert physical/proxy object(s) referenced by "quote" to reference ID(s)
        String quote_fpar = SerializationUtil.encodeWithDynamicTypeCheck(quote);
        form.param("quote", quote_fpar);

        
        // convert physical/proxy object(s) referenced by "holding" to reference ID(s)
        String holding_fpar = SerializationUtil.encodeWithDynamicTypeCheck(holding);
        form.param("holding", holding_fpar);

        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/OrderDataBean_003 with form: "+form.asMap());

        // call constructor service and store ref ID
        Response svc_response;
        try {
            svc_response = 
                klu__client.target(klu__serviceURI) 
                .path("OrderDataBean_003") 
                .request(MediaType.APPLICATION_JSON) 
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        setKlu__referenceID(response_json.getString("return_value"));
        svc_response.close();
    }

  public static OrderDataBean getRandomInstance() {
        // create form for service request
        Form form = new Form();
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/getRandomInstance with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getRandomInstance")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        OrderDataBean response_obj = (OrderDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

  @Override
  public String toString() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/toString with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("toString")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

  public String toHTML() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/toHTML with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("toHTML")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

  public void print() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/print with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("print")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

  public Integer getOrderID() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/getOrderID with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getOrderID")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        Integer response_obj = (Integer)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

  public void setOrderID(Integer orderID) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "orderID" to reference ID(s)
        String orderID_fpar = SerializationUtil.encodeWithDynamicTypeCheck(orderID);
        form.param("orderID", orderID_fpar);

        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/setOrderID with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setOrderID")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

  public String getOrderType() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/getOrderType with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getOrderType")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

  public void setOrderType(String orderType) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("orderType", orderType); 

        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/setOrderType with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setOrderType")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

  public String getOrderStatus() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/getOrderStatus with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getOrderStatus")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

  public void setOrderStatus(String orderStatus) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("orderStatus", orderStatus); 

        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/setOrderStatus with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setOrderStatus")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

  public Date getOpenDate() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/getOpenDate with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getOpenDate")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        Date response_obj = (Date)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

  public void setOpenDate(Date openDate) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "openDate" to reference ID(s)
        String openDate_fpar = SerializationUtil.encodeWithDynamicTypeCheck(openDate);
        form.param("openDate", openDate_fpar);

        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/setOpenDate with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setOpenDate")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

  public Date getCompletionDate() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/getCompletionDate with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getCompletionDate")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        Date response_obj = (Date)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

  public void setCompletionDate(Date completionDate) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "completionDate" to reference ID(s)
        String completionDate_fpar = SerializationUtil.encodeWithDynamicTypeCheck(completionDate);
        form.param("completionDate", completionDate_fpar);

        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/setCompletionDate with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setCompletionDate")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

  public double getQuantity() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/getQuantity with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getQuantity")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Double.parseDouble(response);
    }

  public void setQuantity(double quantity) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("quantity", String.valueOf(quantity)); 

        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/setQuantity with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setQuantity")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

  public BigDecimal getPrice() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/getPrice with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getPrice")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        BigDecimal response_obj = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

  public void setPrice(BigDecimal price) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "price" to reference ID(s)
        String price_fpar = SerializationUtil.encodeWithDynamicTypeCheck(price);
        form.param("price", price_fpar);

        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/setPrice with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setPrice")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

  public BigDecimal getOrderFee() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/getOrderFee with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getOrderFee")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        BigDecimal response_obj = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

  public void setOrderFee(BigDecimal orderFee) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "orderFee" to reference ID(s)
        String orderFee_fpar = SerializationUtil.encodeWithDynamicTypeCheck(orderFee);
        form.param("orderFee", orderFee_fpar);

        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/setOrderFee with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setOrderFee")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

  public String getSymbol() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/getSymbol with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getSymbol")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return response;
    }

  public void setSymbol(String symbol) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        form.param("symbol", symbol); 

        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/setSymbol with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setSymbol")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

  public AccountDataBean getAccount() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/getAccount with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getAccount")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        AccountDataBean response_obj = (AccountDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

  public void setAccount(AccountDataBean account) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "account" to reference ID(s)
        String account_fpar = SerializationUtil.encodeWithDynamicTypeCheck(account);
        form.param("account", account_fpar);

        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/setAccount with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setAccount")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

  public QuoteDataBean getQuote() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/getQuote with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getQuote")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        QuoteDataBean response_obj = (QuoteDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

  public void setQuote(QuoteDataBean quote) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "quote" to reference ID(s)
        String quote_fpar = SerializationUtil.encodeWithDynamicTypeCheck(quote);
        form.param("quote", quote_fpar);

        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/setQuote with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setQuote")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

  public HoldingDataBean getHolding() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/getHolding with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("getHolding")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        
        // convert reference ID(s) stored in "response" to physical/proxy object(s)
        HoldingDataBean response_obj = (HoldingDataBean)SerializationUtil.decodeWithDynamicTypeCheck(response);
        return response_obj;

    }

  public void setHolding(HoldingDataBean holding) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "holding" to reference ID(s)
        String holding_fpar = SerializationUtil.encodeWithDynamicTypeCheck(holding);
        form.param("holding", holding_fpar);

        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/setHolding with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("setHolding")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

  public boolean isBuy() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/isBuy with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("isBuy")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }

  public boolean isSell() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/isSell with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("isSell")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }

  public boolean isOpen() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/isOpen with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("isOpen")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }

  public boolean isCompleted() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/isCompleted with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("isCompleted")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }

  public boolean isCancelled() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/isCancelled with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("isCancelled")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }

  public void cancel() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/cancel with form: "+form.asMap());

        // call service (no response for "void" return type)
        try {
            klu__client.target(klu__serviceURI)
                .path("cancel")
                .request()
                .post(Entity.form(form));
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }

    }

  @Override
  public int hashCode() {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/hashCode with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("hashCode")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Integer.parseInt(response);
    }

  @Override
  public boolean equals(Object object) {
        // create form for service request
        Form form = new Form();
        form.param("klu__referenceID", getKlu__referenceID());
        
        // convert physical/proxy object(s) referenced by "object" to reference ID(s)
        String object_fpar = SerializationUtil.encodeWithDynamicTypeCheck(object);
        form.param("object", object_fpar);

        klu__logger.info("[OrderDataBean] Calling service "+klu__serviceURI+
            "/equals with form: "+form.asMap());

        // call service and get encoded response from response JSON
        Response svc_response;
        try {
            svc_response = klu__client.target(klu__serviceURI)
                .path("equals")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form), Response.class);
        }
        catch (WebApplicationException wae) {
            java.lang.Throwable cause = wae.getCause();
            klu__logger.warning("[OrderDataBean] Exception thrown in service call: "+wae.getMessage());
            throw (java.lang.RuntimeException)cause;
        }
        String response_json_str = svc_response.readEntity(String.class);
        klu__logger.info("[OrderDataBean] Response JSON string: "+response_json_str);
        JsonReader json_reader = Json.createReader(new StringReader(response_json_str));
        JsonObject response_json = json_reader.readObject();
        String response = response_json.getString("return_value");
        svc_response.close();
        return Boolean.parseBoolean(response);
    }
}
