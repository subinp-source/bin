/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.driverbasestores;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.driverbasestores.DriverBaseStoresWSDTO;
import java.util.List;


import java.util.Objects;
public  class DriverBaseStoresDataListWSDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>DriverBaseStoresDataListWSDTO.driverbasestores</code> property defined at extension <code>Hybriswebservices</code>. */
		
	private List<DriverBaseStoresWSDTO> driverbasestores;
	
	public DriverBaseStoresDataListWSDTO()
	{
		// default constructor
	}
	
	public void setDriverbasestores(final List<DriverBaseStoresWSDTO> driverbasestores)
	{
		this.driverbasestores = driverbasestores;
	}

	public List<DriverBaseStoresWSDTO> getDriverbasestores() 
	{
		return driverbasestores;
	}
	

}