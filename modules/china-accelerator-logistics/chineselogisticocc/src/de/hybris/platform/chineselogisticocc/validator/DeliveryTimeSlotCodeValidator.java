/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.chineselogisticocc.validator;

import de.hybris.platform.chineselogisticfacades.delivery.DeliveryTimeSlotFacade;

import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class DeliveryTimeSlotCodeValidator implements Validator
{

	@Resource(name = "deliveryTimeSlotFacade")
	private DeliveryTimeSlotFacade deliveryTimeSlotFacade;

	private static final int MAX_LENGTH = 128;

	@Override
	public boolean supports(final Class clazz)
	{
		return String.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final String deliveryTimeSlotCode = (String) target;
		if (StringUtils.isBlank(deliveryTimeSlotCode) || StringUtils.length(deliveryTimeSlotCode) > MAX_LENGTH)
		{
			errors.reject(deliveryTimeSlotCode, new String[]
			{ String.valueOf(MAX_LENGTH) }, "The deliveryTimeSlotCode is required and must be between 1 and {0} characters long.");
		}
		else if (Objects.isNull(deliveryTimeSlotFacade.getDeliveryTimeSlotByCode(deliveryTimeSlotCode)))
		{
			errors.reject(deliveryTimeSlotCode, new String[]
			{ deliveryTimeSlotCode }, "The deliveryTimeSlotCode is not valid.");
		}

	}

}
