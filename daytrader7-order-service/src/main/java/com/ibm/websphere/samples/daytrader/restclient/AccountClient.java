package com.ibm.websphere.samples.daytrader.restclient;

import javax.ws.rs.Produces;

import java.math.BigDecimal;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey="accountClient")
@Path("/")
public interface AccountClient {  
  
  @Path("/updateAccountBalance")
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public BigDecimal updateAccountBalance( 
    @FormParam("userId") String userId, 
    @FormParam("balanceUpdate") BigDecimal balanceUpdate);


}

