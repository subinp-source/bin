/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.services.impl;

import de.hybris.platform.apiregistryservices.dto.EventExportDeadLetterData;
import de.hybris.platform.apiregistryservices.model.EventExportDeadLetterModel;
import de.hybris.platform.apiregistryservices.services.EventDlqService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.UniqueAttributesInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link EventDlqService}
 */
public class DefaultEventDlqService implements EventDlqService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultEventDlqService.class);

	private ModelService modelService;

	@Override
	public void sendToQueue(final EventExportDeadLetterData data)
	{
		final EventExportDeadLetterModel letter = getModelService().create(EventExportDeadLetterModel.class);
		letter.setDestinationTarget(data.getDestinationTarget());
		letter.setDestinationChannel(data.getDestinationTarget().getDestinationChannel());
		letter.setError(data.getError());
		letter.setId(data.getId());
		letter.setEventType(data.getEventType());
		letter.setPayload(data.getPayload());
		letter.setTimestamp(data.getTimestamp());
		try
		{
			getModelService().save(letter);
		}
		catch (final ModelSavingException exception)
		{
			if (getModelService().isUniqueConstraintErrorAsRootCause(exception)
					|| (Utilities.getRootCauseOfType(exception, InterceptorException.class) != null
							&& (((InterceptorException) Utilities.getRootCauseOfType(exception, InterceptorException.class))
									.getInterceptor() instanceof UniqueAttributesInterceptor)))
			{
				LOG.debug("Data for the same id '{}' should not be stored twice", letter.getId());
			}
		}
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
