/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsoccaddon.jaxb.adapters;

import java.io.IOException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * Custom resolver to prevent external entities from being resolved.
 */
public class ExternalEntityResolver implements EntityResolver
{

	@Override
	public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException
	{
		throw new SAXException("Blocking all attempts to resolve external entities");
	}

}
