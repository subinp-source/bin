/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.controller;

import de.hybris.platform.odata2webservices.facade.IntegrationDataFacade;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for exchanging integration data and metadata
 */
@RestController
public class IntegrationDataController
{
	private final IntegrationDataFacade integrationDataFacade;

	public IntegrationDataController(final IntegrationDataFacade integrationDataFacade)
	{
		this.integrationDataFacade = integrationDataFacade;
	}

	@GetMapping(value = "/{service}/$metadata",
				produces = {MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<String> getSchema(final HttpServletRequest request)
	{
		return integrationDataFacade.convertAndHandleSchemaRequest(request);
	}

	@GetMapping(value = {"/{service}/{entity}", "/{service}/{entity}/{property}"},
				produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE})
	public ResponseEntity<String> getEntity(final HttpServletRequest request)
	{
		return integrationDataFacade.convertAndHandleRequest(request);
	}

	@GetMapping(value = "/{service}",
				produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<String> getEntities(final HttpServletRequest request)
	{
		return integrationDataFacade.convertAndHandleRequest(request);
	}

	@PostMapping(value = {"/{service}/{entity}", "/{service}/$batch"},
				 produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE})
	public ResponseEntity<String> post(final HttpServletRequest request)
	{
		return integrationDataFacade.convertAndHandleRequest(request);
	}

	@DeleteMapping(value = "/{service}/{entity}")
	public ResponseEntity<String> delete(final HttpServletRequest request)
	{
		return integrationDataFacade.convertAndHandleRequest(request);
	}
	
	@PatchMapping(value = "/{service}/{entity}")
	public ResponseEntity<String> patch(final HttpServletRequest request)
	{
		return integrationDataFacade.convertAndHandleRequest(request);
	}
}
