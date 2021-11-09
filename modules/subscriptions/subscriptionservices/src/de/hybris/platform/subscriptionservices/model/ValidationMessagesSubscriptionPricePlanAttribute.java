/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.model;

import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


public class ValidationMessagesSubscriptionPricePlanAttribute extends
		AbstractDynamicAttributeHandler<Collection<String>, SubscriptionPricePlanModel>
{
	@Autowired
	@Qualifier("oneTimeChargeEntryValidationService")
	private OneTimeChargeEntryValidationService otceValidationService;

	@Autowired
	@Qualifier("recurringChargeEntryValidationService")
	private RecurringChargeEntryValidationService rceValidationService;

	@Autowired
	@Qualifier("usageChargeValidationService")
	private UsageChargeValidationService ucValidationService;

	@Override
	public Collection<String> get(final SubscriptionPricePlanModel model)
	{
		final Collection<String> validationMessages = new ArrayList<String>();

		validationMessages.addAll(rceValidationService.validate(model.getRecurringChargeEntries()));
		validationMessages.addAll(otceValidationService.validate(model.getOneTimeChargeEntries()));
		validationMessages.addAll(ucValidationService.validate(model.getUsageCharges()));

		if (validationMessages.isEmpty())
		{
			validationMessages.add(Localization.getLocalizedString("subscriptionservices.customvalidation.priceplan.correct"));
		}

		return validationMessages;
	}

}
