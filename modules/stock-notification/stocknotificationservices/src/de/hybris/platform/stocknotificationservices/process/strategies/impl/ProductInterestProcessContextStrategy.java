/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.stocknotificationservices.process.strategies.impl;

import de.hybris.platform.acceleratorservices.process.strategies.impl.AbstractProcessContextStrategy;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customerinterestsservices.model.ProductInterestsProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Optional;


/**
 * New strategy to handle product interest process
 */
public class ProductInterestProcessContextStrategy extends AbstractProcessContextStrategy
{

	@Override
	public BaseSiteModel getCmsSite(final BusinessProcessModel businessProcessModel)
	{
		ServicesUtil.validateParameterNotNull(businessProcessModel, BUSINESS_PROCESS_MUST_NOT_BE_NULL_MSG);

		return Optional.of(businessProcessModel).filter(businessProcess -> businessProcess instanceof ProductInterestsProcessModel)
				.map(businessProcess -> ((ProductInterestsProcessModel) businessProcess).getBaseSite())
				.orElse(null);
	}

	@Override
	protected CustomerModel getCustomer(final BusinessProcessModel businessProcess)
	{
		return Optional.of(businessProcess).filter(bp -> bp instanceof ProductInterestsProcessModel)
				.map(bp -> ((ProductInterestsProcessModel) businessProcess).getCustomer()).orElse(null);
	}

	@Override
	protected LanguageModel computeLanguage(final BusinessProcessModel businessProcess)
	{
		return Optional.of(businessProcess).filter(bp -> bp instanceof ProductInterestsProcessModel)
				.map(bp -> ((ProductInterestsProcessModel) businessProcess).getLanguage())
				.orElse(super.computeLanguage(businessProcess));
	}


}
