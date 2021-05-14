/**
 * (C) Copyright IBM Corporation 2021.
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

package com.ibm.websphere.samples.daytrader.restclient;

import com.ibm.websphere.samples.daytrader.entities.AccountDataBean;
import com.ibm.websphere.samples.daytrader.entities.AccountProfileDataBean;

import java.math.BigDecimal;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "accountClient")
@Path("/")
public interface AccountClient {

  @GET
  @Path("/getAccountData/{userId}")
  @Produces(MediaType.APPLICATION_JSON)
  public AccountDataBean getAccountData(@PathParam("userId") String userId);

  @GET
  @Path("/getAccountProfileData/{userId}")
  @Produces(MediaType.APPLICATION_JSON)
  public AccountProfileDataBean getAccountProfileData(@PathParam("userId") String userId);

  // TODO - check id?
  @POST
  @Path("/updateAccountProfile")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public AccountProfileDataBean updateAccountProfile(AccountProfileDataBean profileData);

  @POST
  @Path("/updateAccountBalance")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public BigDecimal updateAccountBalance(@FormParam("userId") String userId,
      @FormParam("balanceUpdate") BigDecimal balanceUpdate);

  @POST
  @Path("/login")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public AccountDataBean login(@FormParam("userId") String userId, @FormParam("password") String password);

  @POST
  @Path("/logout")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public void logout(@FormParam("userId") String userId);

  @POST
  @Path("/register")
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
