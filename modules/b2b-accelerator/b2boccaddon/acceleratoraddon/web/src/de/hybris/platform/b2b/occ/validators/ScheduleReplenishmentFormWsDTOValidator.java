/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.validators;

import de.hybris.platform.b2bacceleratorfacades.order.data.B2BReplenishmentRecurrenceEnum;
import de.hybris.platform.b2bwebservicescommons.dto.order.ScheduleReplenishmentFormWsDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class ScheduleReplenishmentFormWsDTOValidator implements Validator
{
	@Override
	public boolean supports(final Class<?> clazz)
	{
		return ScheduleReplenishmentFormWsDTO.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final ScheduleReplenishmentFormWsDTO scheduleReplenishmentForm = (ScheduleReplenishmentFormWsDTO) target;

		if (scheduleReplenishmentForm == null)
		{
			errors.reject("error.scheduleReplenishmentForm.notFound");
		}
		else
		{
			try
			{
				if (scheduleReplenishmentForm.getRecurrencePeriod() != null)
				{
					Enum.valueOf(B2BReplenishmentRecurrenceEnum.class, scheduleReplenishmentForm.getRecurrencePeriod());
				}
				else
				{
					errors.reject("error.scheduleReplenishmentForm.recurrencePeriod.notFound");
				}
			}
			catch (IllegalArgumentException e)
			{
				errors.reject("error.scheduleReplenishmentForm.recurrencePeriod.illegalArgument");
			}
		}
	}

}
