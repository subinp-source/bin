/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.voucher.exceptions;


import de.hybris.platform.servicelayer.exceptions.BusinessException;


public class VoucherOperationException extends BusinessException
{
	public VoucherOperationException(final String message)
	{
		super(message);
	}

	public VoucherOperationException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
