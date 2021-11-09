/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pagescontentslotscomponents.validator.predicate;


import de.hybris.platform.cmsfacades.dto.ComponentAndContentSlotValidationDto;

import java.util.Objects;
import java.util.function.Predicate;


/**
 * Predicate to test if a component is already in the given slot.
 * <p>
 * Returns <tt>TRUE</tt> if the component is already in the slot; <tt>FALSE</tt> otherwise.
 * </p>
 */
public class ComponentAlreadyInContentSlotPredicate implements Predicate<ComponentAndContentSlotValidationDto>
{

	@Override
	public boolean test(final ComponentAndContentSlotValidationDto target)
	{
		if (Objects.isNull(target) || Objects.isNull(target.getContentSlot())
				|| Objects.isNull(target.getContentSlot().getCmsComponents()))
		{
			return Boolean.FALSE;
		}
		else
		{
			return target.getContentSlot().getCmsComponents().contains(target.getComponent());
		}
	}

}
