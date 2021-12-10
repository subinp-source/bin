/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.product;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.StockWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.VariantOptionQualifierWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Collection;


import java.util.Objects;
/**
 * Representation of a Variant Option
 */
@ApiModel(value="VariantOption", description="Representation of a Variant Option")
public  class VariantOptionWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Code of the variant option<br/><br/><i>Generated property</i> for <code>VariantOptionWsDTO.code</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="code", value="Code of the variant option") 	
	private String code;

	/** Stock value of the variant option<br/><br/><i>Generated property</i> for <code>VariantOptionWsDTO.stock</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="stock", value="Stock value of the variant option") 	
	private StockWsDTO stock;

	/** Url address of the variant option<br/><br/><i>Generated property</i> for <code>VariantOptionWsDTO.url</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="url", value="Url address of the variant option") 	
	private String url;

	/** Price data information of the variant option<br/><br/><i>Generated property</i> for <code>VariantOptionWsDTO.priceData</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="priceData", value="Price data information of the variant option") 	
	private PriceWsDTO priceData;

	/** List of variant option qualifiers<br/><br/><i>Generated property</i> for <code>VariantOptionWsDTO.variantOptionQualifiers</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="variantOptionQualifiers", value="List of variant option qualifiers") 	
	private Collection<VariantOptionQualifierWsDTO> variantOptionQualifiers;
	
	public VariantOptionWsDTO()
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
	
	public void setStock(final StockWsDTO stock)
	{
		this.stock = stock;
	}

	public StockWsDTO getStock() 
	{
		return stock;
	}
	
	public void setUrl(final String url)
	{
		this.url = url;
	}

	public String getUrl() 
	{
		return url;
	}
	
	public void setPriceData(final PriceWsDTO priceData)
	{
		this.priceData = priceData;
	}

	public PriceWsDTO getPriceData() 
	{
		return priceData;
	}
	
	public void setVariantOptionQualifiers(final Collection<VariantOptionQualifierWsDTO> variantOptionQualifiers)
	{
		this.variantOptionQualifiers = variantOptionQualifiers;
	}

	public Collection<VariantOptionQualifierWsDTO> getVariantOptionQualifiers() 
	{
		return variantOptionQualifiers;
	}
	

}