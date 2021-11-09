/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentoccaddon.exceptions;

import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceException;

import java.util.ArrayList;
import java.util.List;


/**
 * Thrown when coupons applied on cart expires
 */
public class CartVoucherExpiredException extends WebserviceException
{
	private static final String TYPE = "cartVoucherError";
	private static final String SUBJECT_TYPE = "voucher";
	private static final String REASON_INVALID = "expired";
	private final List<String> invalidVouchers;


	public CartVoucherExpiredException(final String message)
	{
		super(message);
		invalidVouchers = new ArrayList();
	}


	@Override
	public String getSubjectType()
	{
		return SUBJECT_TYPE;
	}

	@Override
	public String getType()
	{
		return TYPE;
	}

	@Override
	public String getReason()
	{
		return REASON_INVALID;
	}

	public List<String> getInvalidVouchers()
	{
		return invalidVouchers;
	}

	public void addInvalidVoucher(final String voucheCode)
	{
		invalidVouchers.add(voucheCode);
	}


}
