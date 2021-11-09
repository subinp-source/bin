/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.util;

import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import java.util.UUID;


public class GuidKeyGenerator implements KeyGenerator
{
	@Override
	public Object generate()
	{
		return UUID.randomUUID().toString();
	}

	@Override
	public Object generateFor(final Object object)
	{
		throw new UnsupportedOperationException("Not supported, please call generate().");
	}

	@Override
	public void reset()
	{
		throw new UnsupportedOperationException("A reset of GUID generator is not supported.");
	}
}
