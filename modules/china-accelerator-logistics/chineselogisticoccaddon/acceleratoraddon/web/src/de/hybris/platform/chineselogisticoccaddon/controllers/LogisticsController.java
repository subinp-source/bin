/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineselogisticoccaddon.controllers;

import de.hybris.platform.chineselogisticfacades.delivery.DeliveryTimeSlotFacade;
import de.hybris.platform.chineselogisticfacades.delivery.data.DeliveryTimeSlotDataList;
import de.hybris.platform.chinesecommercewebservicescommons.dto.DeliveryTimeSlotListWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdUserIdAndCartIdParam;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * APIs for Chinese logistic.
 */
@Controller
@RequestMapping(value = "/{baseSiteId}")
@Api(tags = "Logistics")
public class LogisticsController
{
	@Resource(name = "deliveryTimeSlotFacade")
	private DeliveryTimeSlotFacade deliveryTimeSlotFacade;

	@Resource(name = "dataMapper")
	private DataMapper dataMapper;

	@Resource(name = "deliveryTimeSlotCodeValidator")
	private Validator deliveryTimeSlotCodeValidator;

	protected static final String DELIVERY_TIME_SLOT_OBJECT = "deliveryTimeSlotCode";

	@ResponseBody
	@RequestMapping(value = "/deliverytimeslots", method = RequestMethod.GET)
	@ApiOperation(value = "Gets delivery timeslots.", notes = "Gets delivery timeslots.")
	@ApiBaseSiteIdParam
	public DeliveryTimeSlotListWsDTO getDeliveryTimeSlot()
	{
		final DeliveryTimeSlotDataList deliveryTimeSlots = new DeliveryTimeSlotDataList();
		deliveryTimeSlots.setDeliveryTimeSlots(deliveryTimeSlotFacade.getAllDeliveryTimeSlots());
		return dataMapper.map(deliveryTimeSlots, DeliveryTimeSlotListWsDTO.class);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_GUEST" })
	@RequestMapping(value = "/users/{userId}/carts/{cartId}/deliverytimeslot", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Updates delivery timeslot for a cart.", notes = "Updates delivery timeslot for a cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void replaceDeliveryTimeSlot(
			@ApiParam(value = "Delivery time slot code", required = true)
			@RequestParam
			final String deliveryTimeSlotCode)
	{
		validate(deliveryTimeSlotCode, DELIVERY_TIME_SLOT_OBJECT, deliveryTimeSlotCodeValidator);
		deliveryTimeSlotFacade.setDeliveryTimeSlot(deliveryTimeSlotCode);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_GUEST" })
	@RequestMapping(value = "/users/{userId}/carts/{cartId}/deliverytimeslot", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Deletes delivery timeslot from the cart.", notes = "Deletes delivery timeslot from the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void removeDeliveryTimeSlot()
	{
		deliveryTimeSlotFacade.removeDeliveryTimeSlot();
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

}
