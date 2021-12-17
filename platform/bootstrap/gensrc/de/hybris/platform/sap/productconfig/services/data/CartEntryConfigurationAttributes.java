/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.services.data;

import java.io.Serializable;


import java.util.Objects;
public  class CartEntryConfigurationAttributes  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Configuration is complete and consistent?<br/><br/><i>Generated property</i> for <code>CartEntryConfigurationAttributes.configurationConsistent</code> property defined at extension <code>sapproductconfigservices</code>. */
		
	private Boolean configurationConsistent;

	/** Number of errors in the configuration attached to the entry<br/><br/><i>Generated property</i> for <code>CartEntryConfigurationAttributes.numberOfErrors</code> property defined at extension <code>sapproductconfigservices</code>. */
		
	private Integer numberOfErrors;
	
	public CartEntryConfigurationAttributes()
	{
		// default constructor
	}
	
	public void setConfigurationConsistent(final Boolean configurationConsistent)
	{
		this.configurationConsistent = configurationConsistent;
	}

	public Boolean getConfigurationConsistent() 
	{
		return configurationConsistent;
	}
	
	public void setNumberOfErrors(final Integer numberOfErrors)
	{
		this.numberOfErrors = numberOfErrors;
	}

	public Integer getNumberOfErrors() 
	{
		return numberOfErrors;
	}
	

}