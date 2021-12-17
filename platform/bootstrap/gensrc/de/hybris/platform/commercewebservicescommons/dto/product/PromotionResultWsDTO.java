/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.product;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.order.PromotionOrderEntryConsumedWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PromotionWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of a Promotion result
 */
@ApiModel(value="PromotionResult", description="Representation of a Promotion result")
public  class PromotionResultWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Description of promotion result<br/><br/><i>Generated property</i> for <code>PromotionResultWsDTO.description</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="description", value="Description of promotion result") 	
	private String description;

	/** Promotion information for given promotion result<br/><br/><i>Generated property</i> for <code>PromotionResultWsDTO.promotion</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="promotion", value="Promotion information for given promotion result") 	
	private PromotionWsDTO promotion;

	/** List of promotion order entries consumed<br/><br/><i>Generated property</i> for <code>PromotionResultWsDTO.consumedEntries</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="consumedEntries", value="List of promotion order entries consumed") 	
	private List<PromotionOrderEntryConsumedWsDTO> consumedEntries;
	
	public PromotionResultWsDTO()
	{
		// default constructor
	}
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getDescription() 
	{
		return description;
	}
	
	public void setPromotion(final PromotionWsDTO promotion)
	{
		this.promotion = promotion;
	}

	public PromotionWsDTO getPromotion() 
	{
		return promotion;
	}
	
	public void setConsumedEntries(final List<PromotionOrderEntryConsumedWsDTO> consumedEntries)
	{
		this.consumedEntries = consumedEntries;
	}

	public List<PromotionOrderEntryConsumedWsDTO> getConsumedEntries() 
	{
		return consumedEntries;
	}
	

}