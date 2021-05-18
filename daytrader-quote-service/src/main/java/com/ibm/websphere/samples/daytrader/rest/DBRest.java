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

package com.ibm.websphere.samples.daytrader.rest;

import com.ibm.websphere.samples.daytrader.util.DBUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/db")
@ApplicationScoped
public class DBRest {

  @Inject
  DBUtil dbUtil;

  @GET
  @Path("/createDB")
  public Response createDB() {
    
    try {
      dbUtil.createDB();
      return Response.ok().build();
    } catch (Exception e) {
      e.printStackTrace();
      return Response.serverError().build();
    }
  }

  @GET
  @Path("/resetDB/{deleteAll}")
  public Response resetDB(@PathParam("deleteAll") Boolean deleteAll) {
    try {
      dbUtil.resetTrade(deleteAll);
      return Response.ok().build();
    } catch (Exception e) {
      e.printStackTrace();
      return Response.serverError().build();
    }
  }
}
