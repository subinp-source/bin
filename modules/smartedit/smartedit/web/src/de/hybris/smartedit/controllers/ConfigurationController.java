/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.smartedit.controllers;

import de.hybris.platform.smartedit.dto.ConfigurationData;
import de.hybris.platform.smartedit.dto.ConfigurationDataListWsDto;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Unauthenticated controller using various gateways to send the requested CRUD operations to the secured webservice
 * responsible of executing the operation.
 * <p>
 * By default, {@code smarteditwebservices} is the targeted web extension. This is defined by the property
 * {@code configurationServiceLocation}.
 */
@RestController("configurationController")
@RequestMapping("/configuration")
@Api(tags = "configurations")
public class ConfigurationController
{
	private static final String HEADER_AUTHORIZATION = "Authorization";

	private final HttpGETGateway httpGETGateway;
	private final HttpPOSTGateway httpPOSTGateway;
	private final HttpPUTGateway httpPUTGateway;
	private final HttpDELETEGateway httpDELETEGateway;

	private final ObjectMapper mapper = new ObjectMapper();

	private static final String KEY = "key";
	private static final String VALUE = "value";
	private static final String SECURED = "secured";
	private static final String DUMMY_TOKEN = "dummy";

	@Autowired
	public ConfigurationController(final HttpGETGateway httpGETGateway, final HttpPOSTGateway httpPOSTGateway,
			final HttpPUTGateway httpPUTGateway, final HttpDELETEGateway httpDELETEGateway)
	{
		this.httpGETGateway = httpGETGateway;
		this.httpPOSTGateway = httpPOSTGateway;
		this.httpPUTGateway = httpPUTGateway;
		this.httpDELETEGateway = httpDELETEGateway;
	}

	@GetMapping
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Get configurations", notes = "Endpoint to retrieve configurations.",
					nickname = "getConfiguration")
	@ApiResponses(value =
	{ @ApiResponse(code = 401, message = "Must be authenticated as an Admin or CMS Manager to access this resource") })
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "Authorization", value = "Authorization bearer token", required = true, dataType = "string", paramType = "header") })
	@SuppressWarnings("squid:S1166")
	public ResponseEntity<Collection<ConfigurationData>> getConfiguration(final HttpServletRequest request) throws IOException
	{
		String data = null;
		try
		{
			data = httpGETGateway.loadAll("", getAuthorization(request));
		}
		catch (final HttpClientErrorException e)
		{
			return new ResponseEntity<>(e.getStatusCode());
		}
		final ConfigurationDataListWsDto configurations = mapper.readValue(data, ConfigurationDataListWsDto.class);
		return new ResponseEntity<>(configurations.getConfigurations(), HttpStatus.OK);
	}

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(value = "Create configurations", notes = "Endpoint to create configurations.",
					nickname = "createConfiguration")
	@ApiResponses(value =
	{ @ApiResponse(code = 401, message = "Must be authenticated as an Admin to access this resource") })
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "Authorization", value = "Authorization bearer token", required = true, dataType = "string", paramType = "header") })
	@SuppressWarnings("squid:S1166")
	public ResponseEntity<ConfigurationData> saveConfiguration( //
			@ApiParam(value = "Map representing the configurations to create", required = true) //
			@RequestBody
			final Map<String, String> payload, //
			final HttpServletRequest request) throws IOException
	{
		payload.remove(SECURED);
		String stringPayload = null;
		try
		{
			stringPayload = httpPOSTGateway.save(payload, getAuthorization(request));
		}
		catch (final HttpClientErrorException e)
		{
			return new ResponseEntity<>(e.getStatusCode());
		}
		return new ResponseEntity<>(mapper.readValue(stringPayload, ConfigurationData.class), HttpStatus.CREATED);
	}

	@PutMapping(value = "/{key:.+}")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Update configurations", notes = "Endpoint to update configurations.",
					nickname = "replaceConfiguration")
	@ApiResponses(value =
	{ //
			@ApiResponse(code = 400, message = "Configuration data input is invalid"),
			@ApiResponse(code = 401, message = "Must be authenticated as an Admin to access this resource"),
			@ApiResponse(code = 405, message = "The configuration key is missing from the payload") //
	})
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "Authorization", value = "Authorization bearer token", required = true, dataType = "string", paramType = "header") })
	@SuppressWarnings("squid:S1166")
	public ResponseEntity<ConfigurationData> updateConfiguration( //
			@ApiParam(value = "Map representing the configurations to update", required = true) //
			@RequestBody
			final Map<String, String> payload, //
			@ApiParam(value = "The key of the configuration to update", required = true) //
			@PathVariable("key")
			final String configId, //
			final HttpServletRequest request) throws IOException
	{
		final Optional<ConfigurationData> optional = getConfiguration(request).getBody().stream() //
				.filter(configurationData -> configurationData.getKey().equals(configId)) //
				.findFirst();

		if (optional.isPresent())
		{
			String stringPayload = null;
			try
			{
				stringPayload = httpPUTGateway.update(payload, configId, getAuthorization(request));
			}
			catch (final HttpClientErrorException e)
			{
				return new ResponseEntity<>(e.getStatusCode());
			}
			return new ResponseEntity<>(mapper.readValue(stringPayload, ConfigurationData.class), HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	@DeleteMapping(value = "/{key:.+}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete configurations", notes = "Endpoint to delete configurations.",
					nickname = "removeConfiguration")
	@ApiResponses(value =
	{ @ApiResponse(code = 401, message = "Must be authenticated as an Admin to access this resource") })
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "Authorization", value = "Authorization bearer token", required = true, dataType = "string", paramType = "header") })
	@SuppressWarnings("squid:S1166")
	public ResponseEntity<Void> deleteConfiguration( //
			@ApiParam(value = "The key of the configuration to delete", required = true) //
			@PathVariable("key")
			final String configId, //
			final HttpServletRequest request) throws IOException
	{
		final Map<String, String> configuration = new HashMap<>();
		getConfiguration(request).getBody().stream() //
				.filter(configurationData -> configurationData.getKey().equals(configId)) //
				.forEach(configurationData -> {
					configuration.put(KEY, configurationData.getKey());
					configuration.put(VALUE, configurationData.getValue());
				});

		try
		{
			httpDELETEGateway.delete(configuration, configId, getAuthorization(request));
		}
		catch (final HttpClientErrorException e)
		{
			return new ResponseEntity<>(e.getStatusCode());
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	protected String getAuthorization(final HttpServletRequest request)
	{
		String auth = request.getHeader(HEADER_AUTHORIZATION);
		if (auth == null)
		{
			auth = DUMMY_TOKEN;
		}
		return auth;
	}
}
