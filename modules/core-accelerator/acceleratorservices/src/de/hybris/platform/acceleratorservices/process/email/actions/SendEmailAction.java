/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.process.email.actions;


import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.task.RetryLaterException;

import org.springframework.beans.factory.annotation.Required;


/**
 * A process action to send emails.
 */
public class SendEmailAction extends AbstractProceduralAction
{
	private EmailService emailService;

	protected EmailService getEmailService()
	{
		return emailService;
	}

	@Required
	public void setEmailService(final EmailService emailService)
	{
		this.emailService = emailService;
	}

	@Override
	public void executeAction(final de.hybris.platform.processengine.model.BusinessProcessModel businessProcessModel)
			throws RetryLaterException
	{
		for (final EmailMessageModel email : businessProcessModel.getEmails())
		{
			getEmailService().send(email);
		}
	}
}
