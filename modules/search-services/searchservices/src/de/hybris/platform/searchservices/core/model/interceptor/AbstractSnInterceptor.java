/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.model.interceptor;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.regex.Matcher;

import org.springframework.beans.factory.annotation.Required;


/**
 * Base interceptor for search related types.
 */
public abstract class AbstractSnInterceptor<T extends ItemModel> implements ValidateInterceptor<T>
{
	protected static final String RESOURCE_PREFIX = "searchservices.core.identifier.invalid";

	private ModelService modelService;
	private L10NService l10nService;

	protected void validateIdentifier(final T model, final String attribute) throws InterceptorException
	{
		final String identifier = modelService.getAttributeValue(model, attribute);
		if (identifier != null)
		{
			final Matcher matcher = SearchservicesConstants.IDENTIFIER_PATTERN.matcher(identifier);
			if (!matcher.matches())
			{
				throw new InterceptorException(getLocalizedMessage(RESOURCE_PREFIX, attribute, model, model.getItemtype()), this);
			}
		}
	}

	protected String getLocalizedMessage(final String key, final Object... arguments)
	{
		return l10nService.getLocalizedString(key, arguments);
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public L10NService getL10nService()
	{
		return l10nService;
	}

	@Required
	public void setL10nService(final L10NService l10nService)
	{
		this.l10nService = l10nService;
	}
}
