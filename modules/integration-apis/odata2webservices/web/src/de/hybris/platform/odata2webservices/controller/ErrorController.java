/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.controller;

import de.hybris.platform.integrationservices.util.Log;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller for handling 403 and 404 error codes which happen
 */
@RestController
public class ErrorController
{
	private static final Log LOG = Log.getLogger(ErrorController.class);

	private static final Map<Integer, String> MESSAGES = Map.of(
			HttpStatus.UNAUTHORIZED.value(), "user must be authenticated",
			HttpStatus.FORBIDDEN.value(), "credentials provided are not authorized for the operation",
			HttpStatus.NOT_FOUND.value(), "requested resource does not exist");

	@RequestMapping("/error")
	public String handleError(final HttpServletRequest request, final HttpServletResponse response)
	{
		final String[] args = { asCode(response), asMessage(response) };
		final String contentType = request.getHeader("Accept");
		return contentType != null && contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)
				? jsonTemplate().format(args)
				: xmlTemplate().format(args);
	}

	private String asCode(final HttpServletResponse response)
	{
		return Optional.ofNullable(HttpStatus.resolve(response.getStatus()))
		               .map(HttpStatus::name)
		               .map(String::toLowerCase)
		               .orElseGet(() -> unknown(response));
	}

	private String unknown(final HttpServletResponse response)
	{
		LOG.warn("Unexpected error[status: {}, content-type: {}]", response.getStatus(), response.getContentType());
		return "unknown";
	}

	private String asMessage(final HttpServletResponse response)
	{
		final int status = response.getStatus();
		return MESSAGES.getOrDefault(status, "unexpected error occurred");
	}

	private static MessageFormat jsonTemplate()
	{
		return new MessageFormat("'{'\n" +
				"  \"error\": '{'\n" +
				"    \"code\": \"{0}\",\n" +
				"    \"message\": '{'\n" +
				"      \"lang\": \"en\",\n" +
				"      \"value\": \"{1}\"\n" +
				"    '}'\n" +
				"  '}'\n" +
				"'}'");
	}

	private static MessageFormat xmlTemplate()
	{
		return new MessageFormat("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<error xmlns=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\">\n" +
				"  <code>{0}</code>\n" +
				"  <message xml:lang=\"en\">{1}</message>\n" +
				"</error>");
	}
}
