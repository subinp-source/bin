/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.process.email.actions;

import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import org.apache.commons.collections.CollectionUtils;


/**
 * A process action to remove emails that were sent successfully.
 */
public class RemoveSentEmailAction extends AbstractProceduralAction
{
	@Override
	public void executeAction(final BusinessProcessModel businessProcessModel)
	{
		for (final EmailMessageModel emailMessage : businessProcessModel.getEmails())
		{
			if (emailMessage.isSent())
			{
				//remove to address email models containing customer email addresses.
				if (CollectionUtils.isNotEmpty(emailMessage.getToAddresses()))
				{
					getModelService().removeAll(emailMessage.getToAddresses());
				}
				getModelService().remove(emailMessage);
			}
		}
	}
}
