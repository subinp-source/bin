/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.setup.data;

import java.io.Serializable;


import java.util.Objects;
public  class EditSyncAttributeDescriptorData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>EditSyncAttributeDescriptorData.includeInSync</code> property defined at extension <code>commerceservices</code>. */
		
	private Boolean includeInSync;

	/** <i>Generated property</i> for <code>EditSyncAttributeDescriptorData.copyByValue</code> property defined at extension <code>commerceservices</code>. */
		
	private Boolean copyByValue;

	/** <i>Generated property</i> for <code>EditSyncAttributeDescriptorData.untranslatable</code> property defined at extension <code>commerceservices</code>. */
		
	private Boolean untranslatable;

	/** <i>Generated property</i> for <code>EditSyncAttributeDescriptorData.qualifier</code> property defined at extension <code>commerceservices</code>. */
		
	private String qualifier;
	
	public EditSyncAttributeDescriptorData()
	{
		// default constructor
	}
	
	public void setIncludeInSync(final Boolean includeInSync)
	{
		this.includeInSync = includeInSync;
	}

	public Boolean getIncludeInSync() 
	{
		return includeInSync;
	}
	
	public void setCopyByValue(final Boolean copyByValue)
	{
		this.copyByValue = copyByValue;
	}

	public Boolean getCopyByValue() 
	{
		return copyByValue;
	}
	
	public void setUntranslatable(final Boolean untranslatable)
	{
		this.untranslatable = untranslatable;
	}

	public Boolean getUntranslatable() 
	{
		return untranslatable;
	}
	
	public void setQualifier(final String qualifier)
	{
		this.qualifier = qualifier;
	}

	public String getQualifier() 
	{
		return qualifier;
	}
	

}