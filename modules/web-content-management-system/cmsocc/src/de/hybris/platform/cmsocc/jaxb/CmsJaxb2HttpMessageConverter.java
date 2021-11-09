/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.jaxb;

import de.hybris.platform.cmsocc.data.CMSPageWsDTO;
import de.hybris.platform.cmsocc.jaxb.adapters.components.ComponentAdapterUtil.ComponentAdaptedData;
import de.hybris.platform.cmsocc.jaxb.adapters.components.ComponentListWsDTOAdapter.ListAdaptedComponents;
import de.hybris.platform.webservicescommons.jaxb.Jaxb2HttpMessageConverter;

import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;

import org.eclipse.persistence.jaxb.JAXBMarshaller;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


/**
 * An XmlHttpMessageConverter that reads and writes messages.
 *
 */
public class CmsJaxb2HttpMessageConverter extends Jaxb2HttpMessageConverter
{

	@Override
	protected void marshal(final HttpHeaders headers, final Result result, final Object input, final Class clazz,
			final Marshaller marshaller)
	{
		if (!MediaType.APPLICATION_XML.isCompatibleWith(headers.getContentType()) && isCmsOutput(input))
		{
			// by default, we don't want reduce any array
			((JAXBMarshaller) marshaller).getXMLMarshaller().setReduceAnyArrays(false);
		}
		super.marshal(headers, result, input, clazz, marshaller);
	}

	/**
	 * Check whether the output object is from cmsocc
	 */
	protected boolean isCmsOutput(final Object obj)
	{
		return obj instanceof ListAdaptedComponents || obj instanceof CMSPageWsDTO || obj instanceof ComponentAdaptedData;
	}
}
