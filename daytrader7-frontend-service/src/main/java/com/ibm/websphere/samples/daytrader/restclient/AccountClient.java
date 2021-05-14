package com.ibm.websphere.samples.daytrader.restclient;

import javax.ws.rs.Produces;

import java.math.BigDecimal;

import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.websphere.samples.daytrader.entities.AccountDataBean;
import com.ibm.websphere.samples.daytrader.entities.AccountProfileDataBean;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "accountClient")
@Path("/")
public interface AccountClient {

  @Path("/getAccountData/{userId}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public AccountDataBean getAccountData(@PathParam("userId") String userId);

  @Path("/getAccountProfileData/{userId}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public AccountProfileDataBean getAccountProfileData(@PathParam("userId") String userId);

  // TODO - check id?
  @Path("/updateAccountProfile")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public AccountProfileDataBean updateAccountProfile(AccountProfileDataBean profileData);

  @Path("/updateAccountBalance")
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public BigDecimal updateAccountBalance(@FormParam("userId") String userId,
      @FormParam("balanceUpdate") BigDecimal balanceUpdate);

  @Path("/login")
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public AccountDataBean login(@FormParam("userId") String userId, @FormParam("password") String password);

  @Path("/logout")
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public void logout(@FormParam("userId") String userId);

  @Path("/register")
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public AccountDataBean register(@FormParam("userId") String userId, @FormParam("password") String password,
      @FormParam("fullName") String fullName, @FormParam("address") String address, @FormParam("email") String email,
      @FormParam("creditcard") String creditcard, @FormParam("openBalance") BigDecimal openBalance);

  // DB ------

  @GET
  @Path("/db/createDB")
  public Response createDB();

  @GET
  @Path("/db/resetDB/{deleteAll}")
  public Response resetDB(@PathParam("deleteAll") Boolean deleteAll);

}
