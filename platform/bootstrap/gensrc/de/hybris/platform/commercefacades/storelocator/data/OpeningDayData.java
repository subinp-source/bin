/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.storelocator.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.storelocator.data.TimeData;


import java.util.Objects;
public  class OpeningDayData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OpeningDayData.openingTime</code> property defined at extension <code>commercefacades</code>. */
		
	private TimeData openingTime;

	/** <i>Generated property</i> for <code>OpeningDayData.closingTime</code> property defined at extension <code>commercefacades</code>. */
		
	private TimeData closingTime;
	
	public OpeningDayData()
	{
		// default constructor
	}
	
	public void setOpeningTime(final TimeData openingTime)
	{
		this.openingTime = openingTime;
	}

	public TimeData getOpeningTime() 
	{
		return openingTime;
	}
	
	public void setClosingTime(final TimeData closingTime)
	{
		this.closingTime = closingTime;
	}

	public TimeData getClosingTime() 
	{
		return closingTime;
	}
	

}