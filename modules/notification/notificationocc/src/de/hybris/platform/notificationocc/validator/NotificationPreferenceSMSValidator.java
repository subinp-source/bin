/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationocc.validator;


import de.hybris.platform.notificationfacades.facades.NotificationPreferenceFacade;
import de.hybris.platform.notificationocc.dto.conversation.BasicNotificationPreferenceWsDTO;
import de.hybris.platform.notificationservices.enums.NotificationChannel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validator for the SMS channel of notification preference.
 */
public class NotificationPreferenceSMSValidator implements Validator
{

	private final NotificationPreferenceFacade notificationPreferenceFacade;

	public NotificationPreferenceSMSValidator(final NotificationPreferenceFacade notificationPreferenceFacade)
	{
		this.notificationPreferenceFacade = notificationPreferenceFacade;
	}
	
	@Override
	public boolean supports(final Class<?> aClass)
	{
		return BasicNotificationPreferenceWsDTO.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		Assert.notNull(errors, "Errors object must not be null.");
		
		if (StringUtils.isBlank(getNotificationPreferenceFacade().getChannelValue(NotificationChannel.SMS)))
		{
			errors.rejectValue("channel", "There is no bound mobile number for channel {0}.", new String[]
			{ NotificationChannel.SMS.toString() }, null);

		}


	}

   protected NotificationPreferenceFacade getNotificationPreferenceFacade()
	{
		return notificationPreferenceFacade;
	}
}
