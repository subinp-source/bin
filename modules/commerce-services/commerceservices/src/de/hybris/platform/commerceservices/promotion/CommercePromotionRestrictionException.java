/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.promotion;

import de.hybris.platform.servicelayer.exceptions.BusinessException;


public class CommercePromotionRestrictionException extends BusinessException
{
	public CommercePromotionRestrictionException(final String message)
	{
		super(message);
	}

	public CommercePromotionRestrictionException(final Throwable cause)
	{
		super(cause);
	}

	public CommercePromotionRestrictionException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
