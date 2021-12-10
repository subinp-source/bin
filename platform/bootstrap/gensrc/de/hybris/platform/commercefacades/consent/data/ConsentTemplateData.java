/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:32:59 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.consent.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.consent.data.ConsentData;


import java.util.Objects;
public  class ConsentTemplateData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ConsentTemplateData.id</code> property defined at extension <code>commercefacades</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>ConsentTemplateData.name</code> property defined at extension <code>commercefacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>ConsentTemplateData.description</code> property defined at extension <code>commercefacades</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>ConsentTemplateData.version</code> property defined at extension <code>commercefacades</code>. */
		
	private Integer version;

	/** <i>Generated property</i> for <code>ConsentTemplateData.exposed</code> property defined at extension <code>commercefacades</code>. */
		
	private boolean exposed;

	/** <i>Generated property</i> for <code>ConsentTemplateData.consentData</code> property defined at extension <code>commercefacades</code>. */
		
	private ConsentData consentData;
	
	public ConsentTemplateData()
	{
		// default constructor
	}
	
	public void setId(final String id)
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getDescription() 
	{
		return description;
	}
	
	public void setVersion(final Integer version)
	{
		this.version = version;
	}

	public Integer getVersion() 
	{
		return version;
	}
	
	public void setExposed(final boolean exposed)
	{
		this.exposed = exposed;
	}

	public boolean isExposed() 
	{
		return exposed;
	}
	
	public void setConsentData(final ConsentData consentData)
	{
		this.consentData = consentData;
	}

	public ConsentData getConsentData() 
	{
		return consentData;
	}
	

}