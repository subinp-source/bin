/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.data;

import de.hybris.platform.adaptivesearch.data.AbstractAsItemConfiguration;
import de.hybris.platform.core.PK;


import java.util.Objects;
public abstract  class AbstractAsBoostItemConfiguration extends AbstractAsItemConfiguration 
{

 

	/** <i>Generated property</i> for <code>AbstractAsBoostItemConfiguration.itemPk</code> property defined at extension <code>adaptivesearch</code>. */
		
	private PK itemPk;
	
	public AbstractAsBoostItemConfiguration()
	{
		// default constructor
	}
	
	public void setItemPk(final PK itemPk)
	{
		this.itemPk = itemPk;
	}

	public PK getItemPk() 
	{
		return itemPk;
	}
	

}