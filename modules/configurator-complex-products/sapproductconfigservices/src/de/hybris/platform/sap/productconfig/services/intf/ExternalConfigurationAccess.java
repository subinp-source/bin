/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.services.intf;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;


/**
 * Wraps accesses to the external configuration that can be present at {@link AbstractOrderEntryModel} in case SAP
 * integration is deployed, and allows to run complex product configuration also in case SAP integrations are not
 * present
 */
public interface ExternalConfigurationAccess
{
	/**
	 * Sets external configuration into order entry
	 * 
	 * @param externalConfiguration
	 *           External configuration in XML or JSON format
	 * @param orderEntryModel
	 *           Order entry
	 */
	void setExternalConfiguration(final String externalConfiguration, final AbstractOrderEntryModel orderEntryModel);

	/**
	 * @param orderEntryModel
	 *           Order entry
	 * @return External configuration in XML or JSON format
	 */
	String getExternalConfiguration(final AbstractOrderEntryModel orderEntryModel);
}
