/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.notificationoccaddon.validator;

import de.hybris.platform.notificationoccaddon.dto.conversation.BasicNotificationPreferenceWsDTO;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validator for notification preference channels.
 */
public class NotificationPreferenceChannelValidator implements Validator
{
	private final Map<String, Validator> notificationChannelSpecificValidatorMap;
	private final Validator channelTypeValidator;

	public NotificationPreferenceChannelValidator(final Map notificationChannelSpecificValidatorMap,
			final Validator channelTypeValidator)
	{
		this.notificationChannelSpecificValidatorMap = notificationChannelSpecificValidatorMap;
		this.channelTypeValidator = channelTypeValidator;
	}
	
	@Override
	public boolean supports(final Class<?> aClass)
	{
		return BasicNotificationPreferenceWsDTO.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final BasicNotificationPreferenceWsDTO notificationPreference = (BasicNotificationPreferenceWsDTO) object;
		Assert.notNull(errors, "Errors object must not be null.");

		if (StringUtils.isNotBlank(notificationPreference.getChannel()))
		{
			getChannelTypeValidator().validate(new String[]
			{ notificationPreference.getChannel() }, errors);

			final Validator specificValidator = notificationChannelSpecificValidatorMap
					.get(notificationPreference.getChannel().toUpperCase(Locale.ROOT));

			if (specificValidator != null)
			{
				specificValidator.validate(notificationPreference, errors);
			}
		}

	}

	protected Validator getChannelTypeValidator()
	{
		return channelTypeValidator;
	}



}