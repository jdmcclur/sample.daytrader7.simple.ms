/**
 * (C) Copyright IBM Corporation 2015, 2021
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

package com.ibm.websphere.samples.daytrader.rest;

import java.math.BigDecimal;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.websphere.samples.daytrader.entities.AccountDataBean;
import com.ibm.websphere.samples.daytrader.entities.AccountProfileDataBean;
import com.ibm.websphere.samples.daytrader.interfaces.AccountService;

@Path("/")
@ApplicationScoped
public class AccountRest {

  @Inject
  AccountService accountService;

  @Path("/getAccountData/{userId}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public AccountDataBean getAccountData(@PathParam("userId") String userId) throws Exception {
    return accountService.getAccountData(userId);
  }

  @Path("/getAccountProfileData/{userId}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public AccountProfileDataBean getAccountProfileData(@PathParam("userId") String userId) throws Exception {
    return accountService.getAccountProfileData(userId);
  }

  // TODO - check id?
  @Path("/updateAccountProfile")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public AccountProfileDataBean updateAccountProfile(AccountProfileDataBean profileData) throws Exception {
    return accountService.updateAccountProfile(profileData);
  }

  @Path("/updateAccountBalance")
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public BigDecimal updateAccountBalance(@FormParam("userId") String userId,
      @FormParam("balanceUpdate") BigDecimal balanceUpdate) throws Exception {
    return accountService.updateAccountBalance(userId, balanceUpdate);
  }

  @Path("/login")
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public AccountDataBean login(@FormParam("userId") String userId, @FormParam("password") String password)
      throws Exception {
    return accountService.login(userId, password);
  }

  @Path("/logout")
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public void logout(@FormParam("userId") String userId) throws Exception {
    accountService.logout(userId);
  }

  @Path("/register")
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public AccountDataBean register(@FormParam("userId") String userId, @FormParam("password") String password,
      @FormParam("fullName") String fullName, @FormParam("address") String address, @FormParam("email") String email,
      @FormParam("creditcard") String creditcard, @FormParam("openBalance") BigDecimal openBalance) throws Exception {
        AccountDataBean account = null;
        try { 
          account = accountService.register(userId, password, fullName, address, email, creditcard, openBalance);
        } catch (Exception e) {
          e.printStackTrace();
        }
    return account;
  }

  @GET
  @Path("/status")
  public Response status() {
    return Response.ok("OK").build();
  }
}
