/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.device.data;

import java.io.Serializable;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;


import java.util.Objects;
public  class UiExperienceData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>UiExperienceData.level</code> property defined at extension <code>acceleratorfacades</code>. */
		
	private UiExperienceLevel level;
	
	public UiExperienceData()
	{
		// default constructor
	}
	
	public void setLevel(final UiExperienceLevel level)
	{
		this.level = level;
	}

	public UiExperienceLevel getLevel() 
	{
		return level;
	}
	

}