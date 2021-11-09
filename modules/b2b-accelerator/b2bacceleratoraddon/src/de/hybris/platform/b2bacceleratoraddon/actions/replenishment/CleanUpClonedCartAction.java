/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratoraddon.actions.replenishment;

import de.hybris.platform.b2bacceleratorservices.model.process.ReplenishmentProcessModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;

import org.apache.log4j.Logger;


public class CleanUpClonedCartAction extends AbstractProceduralAction<ReplenishmentProcessModel>
{

	private static final Logger LOG = Logger.getLogger(CleanUpClonedCartAction.class);

	@Override
	public void executeAction(final ReplenishmentProcessModel process) throws Exception
	{
		final BusinessProcessParameterModel clonedCartParameter = processParameterHelper.getProcessParameterByName(process, "cart");
		final CartModel clonedCart = (CartModel) clonedCartParameter.getValue();
		try
		{
			getModelService().remove(clonedCart);
		}
		catch (final ModelRemovalException mre)
		{
			LOG.error("unable to remove the cloned cart guid:" + clonedCart.getGuid(), mre);
		}

	}

}
