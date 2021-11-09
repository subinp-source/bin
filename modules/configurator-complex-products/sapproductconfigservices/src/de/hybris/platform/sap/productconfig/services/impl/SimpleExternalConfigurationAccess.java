/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.services.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.sap.productconfig.services.intf.ExternalConfigurationAccess;


/**
 * Trivial implementation of {@link ExternalConfigurationAccess}, active in a deployment where no SAP integration is
 * available. If SAP integration is available, this bean isn't active, another implementation of the interface residing
 * in an SAP integration extension will take over. <br>
 * <br>
 * This implementation does not access {@link AbstractOrderEntryModel} at all.
 */
public class SimpleExternalConfigurationAccess implements ExternalConfigurationAccess
{

	@Override
	public void setExternalConfiguration(final String externalConfiguration, final AbstractOrderEntryModel orderEntryModel)
	{
		// Do nothing because the SAP integration extensions to AbstractOrderEntry are not available
	}

	@Override
	public String getExternalConfiguration(final AbstractOrderEntryModel orderEntryModel)
	{
		return null;
	}

}
