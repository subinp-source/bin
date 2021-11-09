/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundlefacades;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.subscriptionfacades.converters.SubscriptionXStreamAliasConverter;

import org.springframework.beans.factory.annotation.Required;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public class BundleXStreamConverter
{
	private static final Logger LOG = Logger.getLogger(BundleXStreamConverter.class);

	private SubscriptionXStreamAliasConverter xStreamAliasConverter;

	public String getXStreamXmlFromCartData(final CartData cartData)
	{
		final String xml = getXstream().toXML(cartData);
		LOG.debug(xml);
		return xml;
	}

	public CartData getCartDataFromXml(final String xml)
	{
		if (StringUtils.isNotEmpty(xml))
		{
			return (CartData) getXstream().fromXML(xml);
		}

		return null;
	}

	public XStream getXstream()
	{
		return xStreamAliasConverter.getXstream();
	}

	protected SubscriptionXStreamAliasConverter getAliasConverter()
	{
		return xStreamAliasConverter;
	}

	@Required
	public void setAliasConverter(final SubscriptionXStreamAliasConverter xStreamAliasConverter)
	{
		this.xStreamAliasConverter = xStreamAliasConverter;
	}
}
