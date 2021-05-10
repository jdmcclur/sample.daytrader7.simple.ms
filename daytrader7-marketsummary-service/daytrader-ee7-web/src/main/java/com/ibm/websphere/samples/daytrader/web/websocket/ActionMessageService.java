package com.ibm.websphere.samples.daytrader.web.websocket;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
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
import com.ibm.websphere.samples.daytrader.util.Log;
import java.io.StringReader;
import javax.json.Json;
import javax.json.stream.JsonParser;

/**
 * Service class for ActionMessage - Generated by Cardinal
 */

@Path("/ActionMessageService")
public class ActionMessageService {
    private static final Logger klu__logger = CardinalLogger.getLogger(ActionMessageService.class);




    // health check service
    @GET 
    @Path("/health") 
    @Produces(MediaType.TEXT_HTML) 
    public String getHealth() { 
        klu__logger.info("[ActionMessage] getHealth() called");
        return "ActionMessageService::Health OK"; 
    }



    // service for incrementing object reference count
    @POST
    @Path("/incObjectCount")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void incObjectCount(@FormParam("klu__referenceID") String klu__referenceID) {
        klu__logger.info("[ActionMessageService] incObjectCount() called with ref: "+klu__referenceID);
        ClusterObjectManager.incObjectCount(klu__referenceID);
    }



    // service for decrementing object reference count
    @POST
    @Path("/decObjectCount")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void decObjectCount(@FormParam("klu__referenceID") String klu__referenceID) {
        klu__logger.info("[ActionMessage] decObjectCount() called with ref: "+klu__referenceID);
        ClusterObjectManager.decObjectCount(klu__referenceID);
    }



    // getter service for field "decodedAction" (generated)
    @GET 
    @Path("/get__decodedAction")
    @Produces(MediaType.APPLICATION_JSON) 
    public Response get__decodedAction(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {
        String response;
        ActionMessage instActionMessage = (ActionMessage)ClusterObjectManager.getObject(klu__referenceID);
        response = instActionMessage.decodedAction;
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        JsonObject jsonobj = jsonresp.add("return_value", response).build();
        klu__logger.info("[ActionMessage] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();
    }



    // setter service for field "decodedAction" (generated)
    @POST
    @Path("/set__decodedAction")
    public void set__decodedAction(
        @FormParam("klu__referenceID") String klu__referenceID,
        @FormParam("decodedAction") String decodedAction,
        @Context HttpServletResponse servletResponse
    ) {
        String decodedAction_fpar = decodedAction;
        ActionMessage instActionMessage = (ActionMessage)ClusterObjectManager.getObject(klu__referenceID);
        instActionMessage.decodedAction = decodedAction_fpar;
    }


    @POST
    @Path("/ActionMessage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response ActionMessage(
        @Context HttpServletResponse servletResponse
    ) {

        // call constructor, add created object to cluster object manager, and return ref ID
        ActionMessage instActionMessage;
        try {
            instActionMessage = new ActionMessage();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to constructor ActionMessage() raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        String refid = ClusterObjectManager.putObject(instActionMessage);
        instActionMessage.setKlu__referenceID(refid);
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
    @Path("/doDecoding")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void doDecoding(
        @FormParam("klu__referenceID") String klu__referenceID,
        @FormParam("jsonText") String jsonText,
        @Context HttpServletResponse servletResponse
    ) {

        String jsonText_fpar = jsonText;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        ActionMessage instActionMessage = (ActionMessage)ClusterObjectManager.getObject(klu__referenceID);

        try {
            instActionMessage.doDecoding(jsonText_fpar);
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method doDecoding() of ActionMessage raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

    @POST
    @Path("/getDecodedAction")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDecodedAction(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        String response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        ActionMessage instActionMessage = (ActionMessage)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instActionMessage.getDecodedAction();
        }
        catch (java.lang.Throwable t) {
            String msg = "Call to method getDecodedAction() of ActionMessage raised exception: "+t.getMessage();
            klu__logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        JsonObject jsonobj = jsonresp.add("return_value", response).build();
        klu__logger.info("[ActionMessage] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

}