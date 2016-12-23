package com.efx.ic.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(value = "risk", description = "Endpoint for risk specific operations")
@org.springframework.stereotype.Controller
@EnableAutoConfiguration
@RequestMapping("/risk")
@Path("/risk")
public class RiskDecisioningService {

	@RequestMapping("/heartbeat")
	@ResponseBody
	String home() {
		return "Hello " + new java.util.Date();
	}

	@ApiOperation(value = "Returns user details", notes = "Returns user details.", response = BusinessEntity.class)
	@RequestMapping("/get")
	@ResponseBody
	@Produces(MediaType.APPLICATION_JSON)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successful retrieval of user entity", response = BusinessEntity.class),
	        @ApiResponse(code = 404, message = "User with given uuid does not exist"),
	        @ApiResponse(code = 500, message = "Internal server error") }
	    )
	public Response getRisk(@RequestParam("id") String businessIdentifier) {
		if (businessIdentifier.equals("")) {
			return Response.status(Response.Status.NOT_FOUND).entity("Invalid Business Identifier: " + businessIdentifier).build();
		}

		BusinessEntity entity = Dataservice.getById(businessIdentifier);
		if (entity.isUnknown()) {
			//return Response.ok(getJson(), MediaType.APPLICATION_JSON).build();
			return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for Business Identifier: " + businessIdentifier).build();
		}

		System.out.println("identifier=" + entity.getName());
		return Response.ok(entity, MediaType.APPLICATION_JSON).build();
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(RiskDecisioningService.class, args);
	}
	
	private Map getJson() {
		Map json = new HashMap();
		json.put("error", "service failed");
		return json;
	}
	
}
