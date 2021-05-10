package com.ibm.websphere.samples.daytrader.beans;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

import com.ibm.cardinal.util.CardinalException;
import com.ibm.cardinal.util.CardinalLogger;
import com.ibm.cardinal.util.CardinalString;
import com.ibm.cardinal.util.ClusterObjectManager;
import com.ibm.cardinal.util.SerializationUtil;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.util.FinancialUtils;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Service class for MarketSummaryDataBean - Generated by Cardinal
 */

@Path("/MarketSummaryDataBeanService")
public class MarketSummaryDataBeanService {
    private static final Logger klu__logger = CardinalLogger.getLogger(MarketSummaryDataBeanService.class);




    // health check service
    @GET 
    @Path("/health") 
    @Produces(MediaType.TEXT_HTML) 
    public String getHealth() { 
        klu__logger.info("[MarketSummaryDataBean] getHealth() called");
        return "MarketSummaryDataBeanService::Health OK"; 
    }



    // service for incrementing object reference count
    @POST
    @Path("/incObjectCount")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void incObjectCount(@FormParam("klu__referenceID") String klu__referenceID) {
        klu__logger.info("[MarketSummaryDataBeanService] incObjectCount() called with ref: "+klu__referenceID);
        ClusterObjectManager.incObjectCount(klu__referenceID);
    }



    // service for decrementing object reference count
    @POST
    @Path("/decObjectCount")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void decObjectCount(@FormParam("klu__referenceID") String klu__referenceID) {
        klu__logger.info("[MarketSummaryDataBean] decObjectCount() called with ref: "+klu__referenceID);
        ClusterObjectManager.decObjectCount(klu__referenceID);
    }





    @POST
    @Path("/MarketSummaryDataBean_001")
    @Produces(MediaType.APPLICATION_JSON)
    public Response MarketSummaryDataBean_001(
        @Context HttpServletResponse servletResponse
    ) {

        // call constructor, add created object to cluster object manager, and return ref ID
        MarketSummaryDataBean instMarketSummaryDataBean;
        try {
            instMarketSummaryDataBean = new MarketSummaryDataBean();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to constructor MarketSummaryDataBean() raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        String refid = ClusterObjectManager.putObject(instMarketSummaryDataBean);
        instMarketSummaryDataBean.setKlu__referenceID(refid);
        JsonObject jsonobj = Json
            .createObjectBuilder()
            .add("return_value", refid)
            .build();
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();
    }

    @POST
    @Path("/MarketSummaryDataBean_002")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response MarketSummaryDataBean_002(
        @FormParam("TSIA") String TSIA,
        @FormParam("openTSIA") String openTSIA,
        @FormParam("volume") String volume,
        @FormParam("topGainers") String topGainers,
        @FormParam("topLosers") String topLosers,
        @Context HttpServletResponse servletResponse
    ) {

        
        // convert reference ID(s) stored in "TSIA" to physical/proxy object(s)
        BigDecimal TSIA_fpar = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(TSIA);

        
        // convert reference ID(s) stored in "openTSIA" to physical/proxy object(s)
        BigDecimal openTSIA_fpar = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(openTSIA);

        double volume_fpar = Double.parseDouble(volume);

        
        // convert reference ID(s) stored in "topGainers" to physical/proxy object(s)
        Collection<QuoteDataBean> topGainers_fpar = (Collection<QuoteDataBean>)SerializationUtil.decodeWithDynamicTypeCheck(topGainers);

        
        // convert reference ID(s) stored in "topLosers" to physical/proxy object(s)
        Collection<QuoteDataBean> topLosers_fpar = (Collection<QuoteDataBean>)SerializationUtil.decodeWithDynamicTypeCheck(topLosers);

        // call constructor, add created object to cluster object manager, and return ref ID
        MarketSummaryDataBean instMarketSummaryDataBean;
        try {
            instMarketSummaryDataBean = new MarketSummaryDataBean(TSIA_fpar, openTSIA_fpar, volume_fpar, topGainers_fpar, topLosers_fpar);
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to constructor MarketSummaryDataBean() raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        String refid = ClusterObjectManager.putObject(instMarketSummaryDataBean);
        instMarketSummaryDataBean.setKlu__referenceID(refid);
        JsonObject jsonobj = Json
            .createObjectBuilder()
            .add("return_value", refid)
            .build();
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();
    }

    @POST
    @Path("/getRandomInstance")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRandomInstance(
        @Context HttpServletResponse servletResponse
    ) {

        MarketSummaryDataBean response;

        try {
            response = MarketSummaryDataBean.getRandomInstance();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method getRandomInstance() of MarketSummaryDataBean raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        
        // convert physical/proxy object(s) referenced by "response" to reference ID(s)
        String response_obj = SerializationUtil.encodeWithDynamicTypeCheck(response);
        JsonObject jsonobj = jsonresp.add("return_value", response_obj).build();

        klu__logger.info("[MarketSummaryDataBean] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/toString")
    @Produces(MediaType.APPLICATION_JSON)
    public Response toString(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        String response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        MarketSummaryDataBean instMarketSummaryDataBean = (MarketSummaryDataBean)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instMarketSummaryDataBean.toString();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method toString() of MarketSummaryDataBean raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        JsonObject jsonobj = jsonresp.add("return_value", response).build();
        klu__logger.info("[MarketSummaryDataBean] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/toHTML")
    @Produces(MediaType.APPLICATION_JSON)
    public Response toHTML(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        String response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        MarketSummaryDataBean instMarketSummaryDataBean = (MarketSummaryDataBean)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instMarketSummaryDataBean.toHTML();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method toHTML() of MarketSummaryDataBean raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        JsonObject jsonobj = jsonresp.add("return_value", response).build();
        klu__logger.info("[MarketSummaryDataBean] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/toJson")
    @Produces(MediaType.APPLICATION_JSON)
    public Response toJson(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        JsonObject response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        MarketSummaryDataBean instMarketSummaryDataBean = (MarketSummaryDataBean)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instMarketSummaryDataBean.toJson();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method toJson() of MarketSummaryDataBean raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        
        // convert physical/proxy object(s) referenced by "response" to reference ID(s)
        String response_obj = SerializationUtil.encodeWithDynamicTypeCheck(response);
        JsonObject jsonobj = jsonresp.add("return_value", response_obj).build();

        klu__logger.info("[MarketSummaryDataBean] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/print")
    public void print(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        MarketSummaryDataBean instMarketSummaryDataBean = (MarketSummaryDataBean)ClusterObjectManager.getObject(klu__referenceID);

        try {
            instMarketSummaryDataBean.print();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method print() of MarketSummaryDataBean raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

    @POST
    @Path("/getGainPercent")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGainPercent(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        BigDecimal response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        MarketSummaryDataBean instMarketSummaryDataBean = (MarketSummaryDataBean)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instMarketSummaryDataBean.getGainPercent();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method getGainPercent() of MarketSummaryDataBean raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        
        // convert physical/proxy object(s) referenced by "response" to reference ID(s)
        String response_obj = SerializationUtil.encodeWithDynamicTypeCheck(response);
        JsonObject jsonobj = jsonresp.add("return_value", response_obj).build();

        klu__logger.info("[MarketSummaryDataBean] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/getTsia")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTsia(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        BigDecimal response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        MarketSummaryDataBean instMarketSummaryDataBean = (MarketSummaryDataBean)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instMarketSummaryDataBean.getTsia();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method getTsia() of MarketSummaryDataBean raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        
        // convert physical/proxy object(s) referenced by "response" to reference ID(s)
        String response_obj = SerializationUtil.encodeWithDynamicTypeCheck(response);
        JsonObject jsonobj = jsonresp.add("return_value", response_obj).build();

        klu__logger.info("[MarketSummaryDataBean] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/setTsia")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void setTsia(
        @FormParam("klu__referenceID") String klu__referenceID,
        @FormParam("tsia") String tsia,
        @Context HttpServletResponse servletResponse
    ) {

        
        // convert reference ID(s) stored in "tsia" to physical/proxy object(s)
        BigDecimal tsia_fpar = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(tsia);

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        MarketSummaryDataBean instMarketSummaryDataBean = (MarketSummaryDataBean)ClusterObjectManager.getObject(klu__referenceID);

        try {
            instMarketSummaryDataBean.setTsia(tsia_fpar);
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method setTsia() of MarketSummaryDataBean raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

    @POST
    @Path("/getOpenTsia")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOpenTsia(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        BigDecimal response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        MarketSummaryDataBean instMarketSummaryDataBean = (MarketSummaryDataBean)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instMarketSummaryDataBean.getOpenTsia();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method getOpenTsia() of MarketSummaryDataBean raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        
        // convert physical/proxy object(s) referenced by "response" to reference ID(s)
        String response_obj = SerializationUtil.encodeWithDynamicTypeCheck(response);
        JsonObject jsonobj = jsonresp.add("return_value", response_obj).build();

        klu__logger.info("[MarketSummaryDataBean] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/setOpenTsia")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void setOpenTsia(
        @FormParam("klu__referenceID") String klu__referenceID,
        @FormParam("openTsia") String openTsia,
        @Context HttpServletResponse servletResponse
    ) {

        
        // convert reference ID(s) stored in "openTsia" to physical/proxy object(s)
        BigDecimal openTsia_fpar = (BigDecimal)SerializationUtil.decodeWithDynamicTypeCheck(openTsia);

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        MarketSummaryDataBean instMarketSummaryDataBean = (MarketSummaryDataBean)ClusterObjectManager.getObject(klu__referenceID);

        try {
            instMarketSummaryDataBean.setOpenTsia(openTsia_fpar);
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method setOpenTsia() of MarketSummaryDataBean raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

    @POST
    @Path("/getVolume")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVolume(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        double response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        MarketSummaryDataBean instMarketSummaryDataBean = (MarketSummaryDataBean)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instMarketSummaryDataBean.getVolume();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method getVolume() of MarketSummaryDataBean raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        JsonObject jsonobj = jsonresp.add("return_value", String.valueOf(response)).build();
        klu__logger.info("[MarketSummaryDataBean] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/setVolume")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void setVolume(
        @FormParam("klu__referenceID") String klu__referenceID,
        @FormParam("volume") String volume,
        @Context HttpServletResponse servletResponse
    ) {

        double volume_fpar = Double.parseDouble(volume);

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        MarketSummaryDataBean instMarketSummaryDataBean = (MarketSummaryDataBean)ClusterObjectManager.getObject(klu__referenceID);

        try {
            instMarketSummaryDataBean.setVolume(volume_fpar);
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method setVolume() of MarketSummaryDataBean raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

    @POST
    @Path("/getTopGainers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTopGainers(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        Collection<QuoteDataBean> response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        MarketSummaryDataBean instMarketSummaryDataBean = (MarketSummaryDataBean)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instMarketSummaryDataBean.getTopGainers();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method getTopGainers() of MarketSummaryDataBean raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        
        // convert physical/proxy object(s) referenced by "response" to reference ID(s)
        String response_obj = SerializationUtil.encodeWithDynamicTypeCheck(response);
        JsonObject jsonobj = jsonresp.add("return_value", response_obj).build();

        klu__logger.info("[MarketSummaryDataBean] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/setTopGainers")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void setTopGainers(
        @FormParam("klu__referenceID") String klu__referenceID,
        @FormParam("topGainers") String topGainers,
        @Context HttpServletResponse servletResponse
    ) {

        
        // convert reference ID(s) stored in "topGainers" to physical/proxy object(s)
        Collection<QuoteDataBean> topGainers_fpar = (Collection<QuoteDataBean>)SerializationUtil.decodeWithDynamicTypeCheck(topGainers);

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        MarketSummaryDataBean instMarketSummaryDataBean = (MarketSummaryDataBean)ClusterObjectManager.getObject(klu__referenceID);

        try {
            instMarketSummaryDataBean.setTopGainers(topGainers_fpar);
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method setTopGainers() of MarketSummaryDataBean raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

    @POST
    @Path("/getTopLosers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTopLosers(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        Collection<QuoteDataBean> response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        MarketSummaryDataBean instMarketSummaryDataBean = (MarketSummaryDataBean)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instMarketSummaryDataBean.getTopLosers();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method getTopLosers() of MarketSummaryDataBean raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        
        // convert physical/proxy object(s) referenced by "response" to reference ID(s)
        String response_obj = SerializationUtil.encodeWithDynamicTypeCheck(response);
        JsonObject jsonobj = jsonresp.add("return_value", response_obj).build();

        klu__logger.info("[MarketSummaryDataBean] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/setTopLosers")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void setTopLosers(
        @FormParam("klu__referenceID") String klu__referenceID,
        @FormParam("topLosers") String topLosers,
        @Context HttpServletResponse servletResponse
    ) {

        
        // convert reference ID(s) stored in "topLosers" to physical/proxy object(s)
        Collection<QuoteDataBean> topLosers_fpar = (Collection<QuoteDataBean>)SerializationUtil.decodeWithDynamicTypeCheck(topLosers);

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        MarketSummaryDataBean instMarketSummaryDataBean = (MarketSummaryDataBean)ClusterObjectManager.getObject(klu__referenceID);

        try {
            instMarketSummaryDataBean.setTopLosers(topLosers_fpar);
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method setTopLosers() of MarketSummaryDataBean raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

    @POST
    @Path("/getSummaryDate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSummaryDate(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        Date response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        MarketSummaryDataBean instMarketSummaryDataBean = (MarketSummaryDataBean)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instMarketSummaryDataBean.getSummaryDate();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method getSummaryDate() of MarketSummaryDataBean raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        
        // convert physical/proxy object(s) referenced by "response" to reference ID(s)
        String response_obj = SerializationUtil.encodeWithDynamicTypeCheck(response);
        JsonObject jsonobj = jsonresp.add("return_value", response_obj).build();

        klu__logger.info("[MarketSummaryDataBean] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/setSummaryDate")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void setSummaryDate(
        @FormParam("klu__referenceID") String klu__referenceID,
        @FormParam("summaryDate") String summaryDate,
        @Context HttpServletResponse servletResponse
    ) {

        
        // convert reference ID(s) stored in "summaryDate" to physical/proxy object(s)
        Date summaryDate_fpar = (Date)SerializationUtil.decodeWithDynamicTypeCheck(summaryDate);

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        MarketSummaryDataBean instMarketSummaryDataBean = (MarketSummaryDataBean)ClusterObjectManager.getObject(klu__referenceID);

        try {
            instMarketSummaryDataBean.setSummaryDate(summaryDate_fpar);
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method setSummaryDate() of MarketSummaryDataBean raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

}