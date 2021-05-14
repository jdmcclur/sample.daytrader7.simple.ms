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

import com.ibm.websphere.samples.daytrader.entities.HoldingDataBean;

import java.math.BigDecimal;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "holdingClient")
@Path("/")
public interface HoldingClient {  
  
  @GET
  @Path("/getHolding/{holdingId}")
  @Produces(MediaType.APPLICATION_JSON)
  public HoldingDataBean getHolding(@PathParam("holdingId") Integer holdingId); 

  @POST
  @Path("/createHolding")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public HoldingDataBean createHolding(
      @FormParam("accountId") String accountId, 
      @FormParam("quoteSymbol") String quoteSymbol, 
      @FormParam("quantity") double quantity, 
      @FormParam("purchasePrice") BigDecimal purchasePrice);

  @GET
  @Path("/removeHolding/{holdingId}")
  public void removeHolding(@PathParam("holdingId") Integer holdingId);


}
