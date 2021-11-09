/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.voucher;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.storesession.CurrencyWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a Voucher
 */
@ApiModel(value="Voucher", description="Representation of a Voucher")
public  class VoucherWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** The identifier of the Voucher. This is the first part of voucher code which holds first 3 letters, like: 123<br/><br/><i>Generated property</i> for <code>VoucherWsDTO.code</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="code", value="The identifier of the Voucher. This is the first part of voucher code which holds first 3 letters, like: 123") 	
	private String code;

	/** Voucher code, is the holder for keeping specific occasional voucher related to business usage. It can be generated and looks like: 123-H8BC-Y3D5-34AL<br/><br/><i>Generated property</i> for <code>VoucherWsDTO.voucherCode</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="voucherCode", value="Voucher code, is the holder for keeping specific occasional voucher related to business usage. It can be generated and looks like: 123-H8BC-Y3D5-34AL") 	
	private String voucherCode;

	/** Name of the voucher<br/><br/><i>Generated property</i> for <code>VoucherWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="name", value="Name of the voucher") 	
	private String name;

	/** Description of the voucher<br/><br/><i>Generated property</i> for <code>VoucherWsDTO.description</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="description", value="Description of the voucher") 	
	private String description;

	/** Value of the voucher. Example of such value is: 15.0d<br/><br/><i>Generated property</i> for <code>VoucherWsDTO.value</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="value", value="Value of the voucher. Example of such value is: 15.0d") 	
	private Double value;

	/** Formatted value of the voucher<br/><br/><i>Generated property</i> for <code>VoucherWsDTO.valueFormatted</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="valueFormatted", value="Formatted value of the voucher") 	
	private String valueFormatted;

	/** The value of the voucher to display. Example: 15.0%<br/><br/><i>Generated property</i> for <code>VoucherWsDTO.valueString</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="valueString", value="The value of the voucher to display. Example: 15.0%") 	
	private String valueString;

	/** Specifies if the order this voucher is applied to is shipped for free (true) or not (false). Defaults to false.<br/><br/><i>Generated property</i> for <code>VoucherWsDTO.freeShipping</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="freeShipping", value="Specifies if the order this voucher is applied to is shipped for free (true) or not (false). Defaults to false.") 	
	private Boolean freeShipping;

	/** Currency of the voucher<br/><br/><i>Generated property</i> for <code>VoucherWsDTO.currency</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="currency", value="Currency of the voucher") 	
	private CurrencyWsDTO currency;

	/** Applied value when using this voucher<br/><br/><i>Generated property</i> for <code>VoucherWsDTO.appliedValue</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="appliedValue", value="Applied value when using this voucher") 	
	private PriceWsDTO appliedValue;
	
	public VoucherWsDTO()
	{
		// default constructor
	}
	
	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	
	public void setVoucherCode(final String voucherCode)
	{
		this.voucherCode = voucherCode;
	}

	public String getVoucherCode() 
	{
		return voucherCode;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getDescription() 
	{
		return description;
	}
	
	public void setValue(final Double value)
	{
		this.value = value;
	}

	public Double getValue() 
	{
		return value;
	}
	
	public void setValueFormatted(final String valueFormatted)
	{
		this.valueFormatted = valueFormatted;
	}

	public String getValueFormatted() 
	{
		return valueFormatted;
	}
	
	public void setValueString(final String valueString)
	{
		this.valueString = valueString;
	}

	public String getValueString() 
	{
		return valueString;
	}
	
	public void setFreeShipping(final Boolean freeShipping)
	{
		this.freeShipping = freeShipping;
	}

	public Boolean getFreeShipping() 
	{
		return freeShipping;
	}
	
	public void setCurrency(final CurrencyWsDTO currency)
	{
		this.currency = currency;
	}

	public CurrencyWsDTO getCurrency() 
	{
		return currency;
	}
	
	public void setAppliedValue(final PriceWsDTO appliedValue)
	{
		this.appliedValue = appliedValue;
	}

	public PriceWsDTO getAppliedValue() 
	{
		return appliedValue;
	}
	

}