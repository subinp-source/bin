/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integration.cis.subscription.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.subscriptionfacades.data.SubscriptionBillingDetailFileStream;

import java.io.ByteArrayInputStream;

import com.hybris.cis.api.subscription.model.CisFileStreamResult;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

/**
 * Populates a {@link SubscriptionBillingDetailFileStream} from a {@link CisFileStreamResult}.
 */
public class CisFileStreamPopulator implements Populator<CisFileStreamResult, SubscriptionBillingDetailFileStream>
{
	@Override
	public void populate(final CisFileStreamResult source, final SubscriptionBillingDetailFileStream target)
			throws ConversionException
	{
		validateParameterNotNullStandardMessage("target", source);
		validateParameterNotNullStandardMessage("source", target);

		target.setFileName(source.getFileName());
		target.setInputStream(new ByteArrayInputStream(source.getBytes()));
		target.setMimeType(source.getMimeType());
	}
}
