/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.validators;

import de.hybris.platform.notificationservices.enums.NotificationType;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validate the notification type by the extension which inject the specific notification type list.
 */
public class NotificationTypeValidator implements Validator
{
	private static final String INVALID_NOTIFICATION_TYPE_MESSAGE = "The value for notification type is invalid.";
	private static final String NOTIFICATION_TYPE_INVALID = "notificationTypeInvalid";

	private List<NotificationType> notificationTypes;

	@Override
	public boolean supports(final Class<?> arg0)
	{
		return true;
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final String notificaionType = (String) obj;
		if (notificationTypes.stream().noneMatch(x -> x.name().equalsIgnoreCase(notificaionType)))
		{
			errors.reject(NOTIFICATION_TYPE_INVALID, INVALID_NOTIFICATION_TYPE_MESSAGE);
		}
	}

	protected List<NotificationType> getNotificationTypes()
	{
		return notificationTypes;
	}

	public void setNotificationTypes(final List<NotificationType> notificationTypes)
	{
		this.notificationTypes = notificationTypes;
	}

}
