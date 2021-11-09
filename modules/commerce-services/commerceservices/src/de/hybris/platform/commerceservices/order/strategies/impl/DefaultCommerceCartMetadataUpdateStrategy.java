/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.strategies.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.order.hook.CommerceCartMetadataUpdateMethodHook;
import de.hybris.platform.commerceservices.order.strategies.CommerceCartMetadataUpdateStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartMetadataParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.solr.common.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link CommerceCartMetadataUpdateStrategy}
 */
public class DefaultCommerceCartMetadataUpdateStrategy implements CommerceCartMetadataUpdateStrategy
{
	private List<CommerceCartMetadataUpdateMethodHook> commerceCartMetadataUpdateMethodHooks;
	private ModelService modelService;

	@Override
	public void updateCartMetadata(CommerceCartMetadataParameter parameter)
	{
		validateParameterNotNull(parameter, "parameter cannot be null");
		validateParameterNotNull(parameter.getCart(), "cart property cannot be null");

		beforeUpdateMetadata(parameter);

		final boolean shouldSaveCart = doMetadataUpdate(parameter);

		afterUpdateMetadata(parameter);

		if (shouldSaveCart)
		{
			getModelService().save(parameter.getCart());
		}
	}

	protected boolean doMetadataUpdate(final CommerceCartMetadataParameter parameter)
	{
		final CartModel cart = parameter.getCart();
		boolean shouldSaveCart = false;

		final Optional<String> optionalName = parameter.getName();
		if (optionalName.isPresent())
		{
			final String name = optionalName.get();
			cart.setName(StringUtils.isEmpty(name) ? null : name);
			shouldSaveCart = true;
		}

		final Optional<String> optionalDescription = parameter.getDescription();
		if (optionalDescription.isPresent())
		{
			final String description = optionalDescription.get();
			cart.setDescription(StringUtils.isEmpty(description) ? null : description);
			shouldSaveCart = true;
		}

		if (parameter.isRemoveExpirationTime())
		{
			cart.setExpirationTime(null);
			shouldSaveCart = true;
		}
		else
		{
			final Optional<Date> optionalExpirationTime = parameter.getExpirationTime();
			if (optionalExpirationTime.isPresent())
			{
				final Date expirationTime = optionalExpirationTime.get();
				cart.setExpirationTime(expirationTime);
				shouldSaveCart = true;
			}
		}

		return shouldSaveCart;
	}

	protected void beforeUpdateMetadata(final CommerceCartMetadataParameter parameter)
	{
		if (parameter.isEnableHooks())
		{
			for (final CommerceCartMetadataUpdateMethodHook metadataUpdateMethodHook : getCommerceCartMetadataUpdateMethodHooks())
			{
				metadataUpdateMethodHook.beforeMetadataUpdate(parameter);
			}
		}
	}

	protected void afterUpdateMetadata(final CommerceCartMetadataParameter parameter)
	{
		if (parameter.isEnableHooks())
		{
			for (final CommerceCartMetadataUpdateMethodHook metadataUpdateMethodHook : getCommerceCartMetadataUpdateMethodHooks())
			{
				metadataUpdateMethodHook.afterMetadataUpdate(parameter);
			}
		}
	}

	protected List<CommerceCartMetadataUpdateMethodHook> getCommerceCartMetadataUpdateMethodHooks()
	{
		return commerceCartMetadataUpdateMethodHooks;
	}

	@Required
	public void setCommerceCartMetadataUpdateMethodHooks(
			final List<CommerceCartMetadataUpdateMethodHook> commerceCartMetadataUpdateMethodHooks)
	{
		this.commerceCartMetadataUpdateMethodHooks = commerceCartMetadataUpdateMethodHooks;
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
