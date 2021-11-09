/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedserviceyprofilefacades.data;

import java.io.Serializable;


import java.util.Objects;
public  class TechnologyUsedData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>TechnologyUsedData.device</code> property defined at extension <code>assistedserviceyprofilefacades</code>. */
		
	private String device;

	/** <i>Generated property</i> for <code>TechnologyUsedData.browser</code> property defined at extension <code>assistedserviceyprofilefacades</code>. */
		
	private String browser;

	/** <i>Generated property</i> for <code>TechnologyUsedData.operatingSystem</code> property defined at extension <code>assistedserviceyprofilefacades</code>. */
		
	private String operatingSystem;
	
	public TechnologyUsedData()
	{
		// default constructor
	}
	
	public void setDevice(final String device)
	{
		this.device = device;
	}

	public String getDevice() 
	{
		return device;
	}
	
	public void setBrowser(final String browser)
	{
		this.browser = browser;
	}

	public String getBrowser() 
	{
		return browser;
	}
	
	public void setOperatingSystem(final String operatingSystem)
	{
		this.operatingSystem = operatingSystem;
	}

	public String getOperatingSystem() 
	{
		return operatingSystem;
	}
	

}