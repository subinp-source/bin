/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.dealerbasestores;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.dealerbasestores.DealerBaseStoresWSDTO;
import java.util.List;


import java.util.Objects;
public  class DealerBaseStoresAllDataListWSDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>DealerBaseStoresAllDataListWSDTO.dealerbasestores</code> property defined at extension <code>Hybriswebservices</code>. */
		
	private List<DealerBaseStoresWSDTO> dealerbasestores;
	
	public DealerBaseStoresAllDataListWSDTO()
	{
		// default constructor
	}
	
	public void setDealerbasestores(final List<DealerBaseStoresWSDTO> dealerbasestores)
	{
		this.dealerbasestores = dealerbasestores;
	}

	public List<DealerBaseStoresWSDTO> getDealerbasestores() 
	{
		return dealerbasestores;
	}
	

}