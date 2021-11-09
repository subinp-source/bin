/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.attribute;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;

import java.util.HashMap;
import java.util.Map;


/**
 * Return a mapped value for the ConsignmentStatus
 */
public class ConsignmentStatusDisplayDynamicAttributeHandler implements DynamicAttributeHandler<String, ConsignmentModel>
{
	private final Map<ConsignmentStatus, String> statusDisplayMap = new HashMap<ConsignmentStatus, String>();
	private String defaultStatus;

	public String getDefaultStatus()
	{
		return defaultStatus;
	}

	public void setDefaultStatus(final String defaultStatus)
	{
		this.defaultStatus = defaultStatus;
	}

	public void setStatusDisplayMap(final Map<ConsignmentStatus, String> statusDisplayMap)
	{
		this.statusDisplayMap.putAll(statusDisplayMap);
	}

	protected Map<ConsignmentStatus, String> getStatusDisplayMap()
	{
		return statusDisplayMap;
	}

	@Override
	public String get(final ConsignmentModel consignment)
	{
		String statusLocalisationKey = getDefaultStatus();

		if (consignment != null && consignment.getStatus() != null)
		{
			final ConsignmentStatus statusCode = consignment.getStatus();
			final String statusDisplayEntry = getStatusDisplayMap().get(statusCode);
			if (statusDisplayEntry != null)
			{
				statusLocalisationKey = statusDisplayEntry;
			}
		}

		if (statusLocalisationKey == null || statusLocalisationKey.isEmpty())
		{
			return "";
		}
		return Localization.getLocalizedString(statusLocalisationKey);
	}

	@Override
	public void set(final ConsignmentModel model, final String value)
	{
		throw new UnsupportedOperationException();
	}
}
