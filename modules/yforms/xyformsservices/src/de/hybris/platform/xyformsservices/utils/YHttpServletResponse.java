/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsservices.utils;

import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.mock.web.MockHttpServletResponse;


/**
 * {@link HttpServletResponse} own implementation for getting output stream back from an http connection.
 */
public class YHttpServletResponse extends MockHttpServletResponse
{
	private final ServletOutputStream outputStream;

	public YHttpServletResponse(final OutputStream os)
	{
		super();
		this.outputStream = new DelegatingServletOutputStream(os);
	}

	@Override
	public ServletOutputStream getOutputStream()
	{
		return this.outputStream;
	}
}
