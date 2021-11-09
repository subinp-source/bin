/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.storelocator.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.storelocator.data.SpecialOpeningDayData;
import de.hybris.platform.commercefacades.storelocator.data.WeekdayOpeningDayData;
import java.util.List;


import java.util.Objects;
public  class OpeningScheduleData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OpeningScheduleData.name</code> property defined at extension <code>commercefacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>OpeningScheduleData.code</code> property defined at extension <code>commercefacades</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>OpeningScheduleData.weekDayOpeningList</code> property defined at extension <code>commercefacades</code>. */
		
	private List<WeekdayOpeningDayData> weekDayOpeningList;

	/** <i>Generated property</i> for <code>OpeningScheduleData.specialDayOpeningList</code> property defined at extension <code>commercefacades</code>. */
		
	private List<SpecialOpeningDayData> specialDayOpeningList;
	
	public OpeningScheduleData()
	{
		// default constructor
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	
	public void setWeekDayOpeningList(final List<WeekdayOpeningDayData> weekDayOpeningList)
	{
		this.weekDayOpeningList = weekDayOpeningList;
	}

	public List<WeekdayOpeningDayData> getWeekDayOpeningList() 
	{
		return weekDayOpeningList;
	}
	
	public void setSpecialDayOpeningList(final List<SpecialOpeningDayData> specialDayOpeningList)
	{
		this.specialDayOpeningList = specialDayOpeningList;
	}

	public List<SpecialOpeningDayData> getSpecialDayOpeningList() 
	{
		return specialDayOpeningList;
	}
	

}