/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.training.validation.data;

import java.io.Serializable;
import de.hybris.training.validation.data.CartVoucherValidationData;
import java.util.List;


import java.util.Objects;
public  class CartVoucherValidationDataList  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CartVoucherValidationDataList.CartVoucherValidationDataList</code> property defined at extension <code>Hybriswebservices</code>. */
		
	private List<CartVoucherValidationData> CartVoucherValidationDataList;
	
	public CartVoucherValidationDataList()
	{
		// default constructor
	}
	
	public void setCartVoucherValidationDataList(final List<CartVoucherValidationData> CartVoucherValidationDataList)
	{
		this.CartVoucherValidationDataList = CartVoucherValidationDataList;
	}

	public List<CartVoucherValidationData> getCartVoucherValidationDataList() 
	{
		return CartVoucherValidationDataList;
	}
	

}