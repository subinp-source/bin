/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.voucher;

import de.hybris.platform.commercefacades.voucher.data.VoucherData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;

import java.util.List;


/**
 * Voucher facade interface. Manages applying vouchers to cart and releasing it.
 */
public interface VoucherFacade
{
	/**
	 * Check if voucher code is valid
	 * 
	 * @param voucherCode
	 *           voucher code
	 * @return true when voucher code is valid, false when voucher code is invalid
	 */
	boolean checkVoucherCode(String voucherCode);

	/**
	 * Get voucher base on its code
	 * 
	 * @param voucherCode
	 *           voucher identifier
	 * @return the {@link VoucherData}
	 * 
	 * @throws VoucherOperationException
	 *            if no voucher with the specified code is found
	 * @throws IllegalArgumentException
	 *            if parameter code is <code>null</code> or empty
	 */
	VoucherData getVoucher(String voucherCode) throws VoucherOperationException;

	/**
	 * Apply voucher to current session cart
	 * 
	 * @param voucherCode
	 *           voucher identifier
	 * 
	 * @throws VoucherOperationException
	 *            if voucher wasn't applied due to some problems
	 * @throws IllegalArgumentException
	 *            if parameter code is <code>null</code> or empty
	 * 
	 */
	void applyVoucher(String voucherCode) throws VoucherOperationException;

	/**
	 * Remove voucher from current session cart
	 * 
	 * @param voucherCode
	 *           voucher identifier
	 * 
	 * @throws VoucherOperationException
	 *            if voucher wasn't released due to some problems
	 * @throws IllegalArgumentException
	 *            if parameter code is <code>null</code> or empty
	 */
	void releaseVoucher(String voucherCode) throws VoucherOperationException;

	/**
	 * Get vouchers applied for current session cart
	 * 
	 * @return list of vouchers applied for current session cart
	 */
	List<VoucherData> getVouchersForCart();

}
