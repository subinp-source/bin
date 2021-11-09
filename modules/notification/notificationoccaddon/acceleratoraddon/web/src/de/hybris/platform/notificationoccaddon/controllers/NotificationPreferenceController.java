/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationoccaddon.controllers;

import de.hybris.platform.notificationfacades.data.NotificationPreferenceData;
import de.hybris.platform.notificationfacades.data.NotificationPreferenceDataList;
import de.hybris.platform.notificationfacades.facades.NotificationPreferenceFacade;
import de.hybris.platform.notificationoccaddon.dto.conversation.BasicNotificationPreferenceListWsDTO;
import de.hybris.platform.notificationoccaddon.dto.conversation.NotificationPreferenceListWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;

import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * Web Services Controller to expose the functionality of the
 * {@link de.hybris.platform.notificationfacades.facades.NotificationPreferenceFacade}.
 * 
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/notificationpreferences")
@Api(tags = "Notification Preference")
public class NotificationPreferenceController
{
	@Resource(name = "notificationPreferenceFacade")
	private NotificationPreferenceFacade notificationPreferenceFacade;

	@Resource(name = "dataMapper")
	private DataMapper dataMapper;

	@Resource(name = "notificationPreferenceDTOValidator")
	private Validator notificationPreferenceDTOValidator;
	

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Gets all notification preferences of the current customer", notes = "Returns the notification preferences of the current customer.")
	@ApiBaseSiteIdAndUserIdParam
	public NotificationPreferenceListWsDTO getNotificationPreferences()
	{

		final NotificationPreferenceDataList preferenceDataList = notificationPreferenceFacade
				.getNotificationPreferences((notificationPreferenceFacade.getValidNotificationPreferences()));

		return dataMapper.map(preferenceDataList, NotificationPreferenceListWsDTO.class);
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(method = RequestMethod.PATCH, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Updates the notification preferences of the current customer", notes = "Updates the notification preference of the current customer.")
	@ApiBaseSiteIdAndUserIdParam
	public void updateNotificationPreferences(
			@ApiParam(value = "Notification preference list", required = true) @RequestBody final BasicNotificationPreferenceListWsDTO notificationPreferences)
	{

		final List<NotificationPreferenceData> currentPreferences = notificationPreferenceFacade.getNotificationPreferences();
		if (Objects.nonNull(notificationPreferences) && Objects.nonNull(notificationPreferences.getPreferences()))
		{
			notificationPreferences.getPreferences().forEach(preference -> {
				validate(preference, "BasicNotificationPreference", getNotificationPreferenceDTOValidator());
				updatePreferences(dataMapper.map(preference, NotificationPreferenceData.class), currentPreferences);

			});
		}

		notificationPreferenceFacade.updateNotificationPreference(currentPreferences);
	}


	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}

	protected Validator getNotificationPreferenceDTOValidator()
	{
		return notificationPreferenceDTOValidator;
	}

	protected void validate(final Object object, final String objectName, final Validator validator)
	{
		final Errors errors = new BeanPropertyBindingResult(object, objectName);
		validator.validate(object, errors);
		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}

	}

	protected void updatePreferences(final NotificationPreferenceData changedData,
			final List<NotificationPreferenceData> preferences)
	{
		for (final NotificationPreferenceData originalData : preferences)
		{
			if (changedData.getChannel().equals(originalData.getChannel()))
			{
				originalData.setEnabled(changedData.isEnabled());
			}
		}
	}

}
