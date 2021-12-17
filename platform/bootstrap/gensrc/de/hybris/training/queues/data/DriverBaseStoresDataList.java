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
import de.hybris.platform.commercefacades.product.data.DriverBaseData;
import java.util.List;


import java.util.Objects;
public  class DriverBaseStoresDataList  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>DriverBaseStoresDataList.driverbasestores</code> property defined at extension <code>Hybriswebservices</code>. */
		
	private List<DriverBaseData> driverbasestores;
	
	public DriverBaseStoresDataList()
	{
		// default constructor
	}
	
	public void setDriverbasestores(final List<DriverBaseData> driverbasestores)
	{
		this.driverbasestores = driverbasestores;
	}

	public List<DriverBaseData> getDriverbasestores() 
	{
		return driverbasestores;
	}
	

}