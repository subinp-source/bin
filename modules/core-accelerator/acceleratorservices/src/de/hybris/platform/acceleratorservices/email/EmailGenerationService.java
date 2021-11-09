/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.email;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;


/**
 * Service for generating an email.
 */
public interface EmailGenerationService
{
	/**
	 * Generates EmailMessage give business process and cms email page.
	 * 
	 * @param businessProcessModel
	 *           Business process object
	 * @param emailPageModel
	 *           Email page
	 * @return EmailMessage
	 */
	EmailMessageModel generate(BusinessProcessModel businessProcessModel, EmailPageModel emailPageModel);
}
