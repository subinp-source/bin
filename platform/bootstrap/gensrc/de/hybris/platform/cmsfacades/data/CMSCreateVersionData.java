/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;


import java.util.Objects;
public abstract  class CMSCreateVersionData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CMSCreateVersionData.createVersion</code> property defined at extension <code>cmsfacades</code>. */
		
	private Boolean createVersion;

	/** <i>Generated property</i> for <code>CMSCreateVersionData.versionLabel</code> property defined at extension <code>cmsfacades</code>. */
		
	private String versionLabel;
	
	public CMSCreateVersionData()
	{
		// default constructor
	}
	
	public void setCreateVersion(final Boolean createVersion)
	{
		this.createVersion = createVersion;
	}

	public Boolean getCreateVersion() 
	{
		return createVersion;
	}
	
	public void setVersionLabel(final String versionLabel)
	{
		this.versionLabel = versionLabel;
	}

	public String getVersionLabel() 
	{
		return versionLabel;
	}
	

}