/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmssmarteditwebservices.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;


/**
 * Wrapper for HttpServletRequest which allows reading the body from the post request multiple times. Without the
 * wrapper, the input stream is closed after the body content is read.
 */
public class MultiReadHttpServletRequest extends HttpServletRequestWrapper
{
	private final ByteArrayOutputStream cachedBytes;

	public MultiReadHttpServletRequest(final HttpServletRequest request) throws IOException
	{
		super(request);
		// Cache the input stream in order to read it multiple times.
		cachedBytes = new ByteArrayOutputStream();
		IOUtils.copy(super.getInputStream(), cachedBytes);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException
	{
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getCachedBytes().toByteArray());
		return new ServletInputStream()
		{
			@Override
			public boolean isFinished()
			{
				return byteArrayInputStream.available() == 0;
			}

			@Override
			public boolean isReady()
			{
				return true;
			}

			@Override
			public void setReadListener(final ReadListener readListener)
			{
				throw new UnsupportedOperationException("ServletInputStream.setReadListener not implemented");
			}

			@Override
			public int read() throws IOException
			{
				return byteArrayInputStream.read();
			}
		};
	}

	@Override
	public BufferedReader getReader() throws IOException
	{
		return new BufferedReader(new InputStreamReader(this.getInputStream()));
	}

	protected ByteArrayOutputStream getCachedBytes()
	{
		return this.cachedBytes;
	}
}
