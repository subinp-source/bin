/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.data;

import java.io.Serializable;
import de.hybris.platform.core.PK;


import java.util.Objects;
public  class AsReference  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AsReference.pk</code> property defined at extension <code>adaptivesearch</code>. */
		
	private PK pk;

	/** <i>Generated property</i> for <code>AsReference.version</code> property defined at extension <code>adaptivesearch</code>. */
		
	private long version;
	
	public AsReference()
	{
		// default constructor
	}
	
	public void setPk(final PK pk)
	{
		this.pk = pk;
	}

	public PK getPk() 
	{
		return pk;
	}
	
	public void setVersion(final long version)
	{
		this.version = version;
	}

	public long getVersion() 
	{
		return version;
	}
	

}