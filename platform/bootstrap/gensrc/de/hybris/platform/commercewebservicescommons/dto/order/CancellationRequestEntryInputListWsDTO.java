/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.order;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.order.CancellationRequestEntryInputWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of a cancellation request entry input list for an order
 */
@ApiModel(value="CancellationRequestEntryInputList", description="Representation of a cancellation request entry input list for an order")
public  class CancellationRequestEntryInputListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Cancellation request entry inputs which contain information about the order entries which are requested to be cancelled<br/><br/><i>Generated property</i> for <code>CancellationRequestEntryInputListWsDTO.cancellationRequestEntryInputs</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="cancellationRequestEntryInputs", value="Cancellation request entry inputs which contain information about the order entries which are requested to be cancelled", required=true) 	
	private List<CancellationRequestEntryInputWsDTO> cancellationRequestEntryInputs;
	
	public CancellationRequestEntryInputListWsDTO()
	{
		// default constructor
	}
	
	public void setCancellationRequestEntryInputs(final List<CancellationRequestEntryInputWsDTO> cancellationRequestEntryInputs)
	{
		this.cancellationRequestEntryInputs = cancellationRequestEntryInputs;
	}

	public List<CancellationRequestEntryInputWsDTO> getCancellationRequestEntryInputs() 
	{
		return cancellationRequestEntryInputs;
	}
	

}