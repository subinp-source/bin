/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.webservicescommons.testsupport.client;

import java.net.Socket;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;


public class DummyTrustManager extends X509ExtendedTrustManager
{
	private static final X509Certificate[] ACCEPTED_ISSUERS = new X509Certificate[0];

	@Override
	public void checkClientTrusted(final X509Certificate[] arg0, final String arg1)
	{
		return;
	}

	@Override
	public void checkServerTrusted(final X509Certificate[] arg0, final String arg1)
	{
		return;
	}

	@Override
	public X509Certificate[] getAcceptedIssuers()
	{
		return ACCEPTED_ISSUERS;
	}

	@Override
	public void checkClientTrusted(final X509Certificate[] arg0, final String arg1, final Socket arg2)
	{
		return;
	}

	@Override
	public void checkClientTrusted(final X509Certificate[] arg0, final String arg1, final SSLEngine arg2)
	{
		return;
	}

	@Override
	public void checkServerTrusted(final X509Certificate[] arg0, final String arg1, final Socket arg2)
	{
		return;
	}

	@Override
	public void checkServerTrusted(final X509Certificate[] arg0, final String arg1, final SSLEngine arg2)
	{
		return;
	}
}
