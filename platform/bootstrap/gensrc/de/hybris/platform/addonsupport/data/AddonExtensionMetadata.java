/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addonsupport.data;

import java.io.Serializable;


import java.util.Objects;
public  class AddonExtensionMetadata  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AddonExtensionMetadata.baseExtensionName</code> property defined at extension <code>addonsupport</code>. */
		
	private String baseExtensionName;

	/** <i>Generated property</i> for <code>AddonExtensionMetadata.suffixChannel</code> property defined at extension <code>addonsupport</code>. */
		
	private boolean suffixChannel;
	
	public AddonExtensionMetadata()
	{
		// default constructor
	}
	
	public void setBaseExtensionName(final String baseExtensionName)
	{
		this.baseExtensionName = baseExtensionName;
	}

	public String getBaseExtensionName() 
	{
		return baseExtensionName;
	}
	
	public void setSuffixChannel(final boolean suffixChannel)
	{
		this.suffixChannel = suffixChannel;
	}

	public boolean isSuffixChannel() 
	{
		return suffixChannel;
	}
	

}