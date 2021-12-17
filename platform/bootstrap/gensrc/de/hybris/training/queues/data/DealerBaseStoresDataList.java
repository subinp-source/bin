/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.training.queues.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.DealerFolder.data.DealerBaseData;
import java.util.List;


import java.util.Objects;
public  class DealerBaseStoresDataList  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>DealerBaseStoresDataList.dealerbasestores</code> property defined at extension <code>Hybriswebservices</code>. */
		
	private List<DealerBaseData> dealerbasestores;
	
	public DealerBaseStoresDataList()
	{
		// default constructor
	}
	
	public void setDealerbasestores(final List<DealerBaseData> dealerbasestores)
	{
		this.dealerbasestores = dealerbasestores;
	}

	public List<DealerBaseData> getDealerbasestores() 
	{
		return dealerbasestores;
	}
	

}