/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.consent.dao;

import de.hybris.platform.commerceservices.model.consent.ConsentModel;
import de.hybris.platform.commerceservices.model.consent.ConsentTemplateModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import java.util.List;


/**
 * Data Access Object for looking up items related to the consents.
 */
public interface ConsentDao extends GenericDao<ConsentModel>
{
	/**
	 * Finds the latest consent for the specified customer and consent template.
	 *
	 * @param customer
	 *           the customer to get the consent for
	 * @param consentTemplate
	 *           the consent template to get the consent for
	 * @return the consent if found, otherwise null
	 */
	ConsentModel findConsentByCustomerAndConsentTemplate(final CustomerModel customer, final ConsentTemplateModel consentTemplate);

	/**
	 * Finds all consents for the specified customer.
	 *
	 * @param customer
	 *           the customer to get the consents for
	 * @return the consents
	 */
	List<ConsentModel> findAllConsentsByCustomer(final CustomerModel customer);
}
