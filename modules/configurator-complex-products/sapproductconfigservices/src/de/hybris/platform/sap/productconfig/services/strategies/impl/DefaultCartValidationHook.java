/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.services.strategies.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.strategies.hooks.CartValidationHook;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.sap.productconfig.services.impl.CPQConfigurableChecker;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationPricingStrategy;
import de.hybris.platform.sap.productconfig.services.strategies.intf.ProductConfigurationCartEntryValidationStrategy;

import java.util.List;


/**
 * Implementation of CartValidationHook for Product Configuration
 */
public class DefaultCartValidationHook implements CartValidationHook
{
	private final ProductConfigurationPricingStrategy productConfigurationPricingStrategy;
	private final CommerceCartService commerceCartService;
	private final CPQConfigurableChecker cpqConfigurableChecker;
	private final ProductConfigurationCartEntryValidationStrategy productConfigurationCartEntryValidationStrategy;

	public DefaultCartValidationHook(final ProductConfigurationPricingStrategy productConfigurationPricingStrategy,
			final CommerceCartService commerceCartService, final CPQConfigurableChecker cpqConfigurableChecker,
			final ProductConfigurationCartEntryValidationStrategy productConfigurationCartEntryValidationStrategy)
	{
		this.productConfigurationPricingStrategy = productConfigurationPricingStrategy;
		this.commerceCartService = commerceCartService;
		this.cpqConfigurableChecker = cpqConfigurableChecker;
		this.productConfigurationCartEntryValidationStrategy = productConfigurationCartEntryValidationStrategy;
	}


	@Override
	public void beforeValidateCart(final CommerceCartParameter parameter, final List<CommerceCartModification> modifications)
	{
		markOutdatedCartEntriesForCalculation(parameter);
		//calculation is only triggered when the cart is flagged accordingly
		getCommerceCartService().calculateCart(parameter);
	}


	protected void markOutdatedCartEntriesForCalculation(final CommerceCartParameter parameter)
	{
		final CartModel cart = parameter.getCart();
		//checks each entry whether the price has changed - if yes, the entry and the cart are flagged for calculation
		cart.getEntries().stream()
				.filter(entry -> getCpqConfigurableChecker().isCPQConfiguratorApplicableProduct(entry.getProduct()))
				.forEach(entry -> getProductConfigurationPricingStrategy().updateCartEntryPrices(entry, false, null));
	}

	@Override
	public void afterValidateCart(final CommerceCartParameter parameter, final List<CommerceCartModification> modifications)
	{
		final CartModel cart = parameter.getCart();
		cart.getEntries().stream()
				.filter(entry -> getCpqConfigurableChecker().isCPQConfiguratorApplicableProduct(entry.getProduct()))
				.filter(entry -> isEntrySuccess(entry, modifications))
				.forEach(entryToCheck -> validateConfiguration(modifications, entryToCheck));
	}


	protected void validateConfiguration(final List<CommerceCartModification> modifications,
			final AbstractOrderEntryModel entryToCheck)
	{
		final CommerceCartModification resultfromConfigurationValidation = getProductConfigurationCartEntryValidationStrategy()
				.validateConfiguration(entryToCheck);
		if (resultfromConfigurationValidation != null)
		{
			final CommerceCartModification oldModification = retrieveModificationForEntry(entryToCheck, modifications);
			modifications.remove(oldModification);
			modifications.add(resultfromConfigurationValidation);
		}
	}

	protected boolean isEntrySuccess(final AbstractOrderEntryModel entry, final List<CommerceCartModification> modifications)
	{
		final CommerceCartModification matchingModification = retrieveModificationForEntry(entry, modifications);
		return CommerceCartModificationStatus.SUCCESS.equals(matchingModification.getStatusCode());
	}


	protected CommerceCartModification retrieveModificationForEntry(final AbstractOrderEntryModel entry,
			final List<CommerceCartModification> modifications)
	{
		return modifications.stream().filter(modification -> modification.getEntry().getPk().equals(entry.getPk())).findFirst()
				.orElseThrow(() -> new IllegalStateException("No CartModification found for cart entry " + entry.getPk()));
	}


	protected ProductConfigurationPricingStrategy getProductConfigurationPricingStrategy()
	{
		return productConfigurationPricingStrategy;
	}

	protected CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}


	protected CPQConfigurableChecker getCpqConfigurableChecker()
	{
		return cpqConfigurableChecker;
	}


	protected ProductConfigurationCartEntryValidationStrategy getProductConfigurationCartEntryValidationStrategy()
	{
		return productConfigurationCartEntryValidationStrategy;
	}

}
