/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.data;

import java.io.Serializable;


import java.util.Objects;
public  class AsSearchConfigurationInfoData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AsSearchConfigurationInfoData.type</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String type;

	/** <i>Generated property</i> for <code>AsSearchConfigurationInfoData.contextType</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String contextType;

	/** <i>Generated property</i> for <code>AsSearchConfigurationInfoData.contextLabel</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String contextLabel;

	/** <i>Generated property</i> for <code>AsSearchConfigurationInfoData.contextDescription</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String contextDescription;
	
	public AsSearchConfigurationInfoData()
	{
		// default constructor
	}
	
	public void setType(final String type)
	{
		this.type = type;
	}

	public String getType() 
	{
		return type;
	}
	
	public void setContextType(final String contextType)
	{
		this.contextType = contextType;
	}

	public String getContextType() 
	{
		return contextType;
	}
	
	public void setContextLabel(final String contextLabel)
	{
		this.contextLabel = contextLabel;
	}

	public String getContextLabel() 
	{
		return contextLabel;
	}
	
	public void setContextDescription(final String contextDescription)
	{
		this.contextDescription = contextDescription;
	}

	public String getContextDescription() 
	{
		return contextDescription;
	}
	

}