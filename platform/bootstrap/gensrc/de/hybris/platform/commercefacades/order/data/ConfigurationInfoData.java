/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;
import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.catalog.enums.ProductInfoStatus;


import java.util.Objects;
public  class ConfigurationInfoData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ConfigurationInfoData.configuratorType</code> property defined at extension <code>commercefacades</code>. */
		
	private ConfiguratorType configuratorType;

	/** <i>Generated property</i> for <code>ConfigurationInfoData.status</code> property defined at extension <code>commercefacades</code>. */
		
	private ProductInfoStatus status;

	/** <i>Generated property</i> for <code>ConfigurationInfoData.configurationLabel</code> property defined at extension <code>commercefacades</code>. */
		
	private String configurationLabel;

	/** <i>Generated property</i> for <code>ConfigurationInfoData.configurationValue</code> property defined at extension <code>commercefacades</code>. */
		
	private String configurationValue;

	/** <i>Generated property</i> for <code>ConfigurationInfoData.configId</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String configId;
	
	public ConfigurationInfoData()
	{
		// default constructor
	}
	
	public void setConfiguratorType(final ConfiguratorType configuratorType)
	{
		this.configuratorType = configuratorType;
	}

	public ConfiguratorType getConfiguratorType() 
	{
		return configuratorType;
	}
	
	public void setStatus(final ProductInfoStatus status)
	{
		this.status = status;
	}

	public ProductInfoStatus getStatus() 
	{
		return status;
	}
	
	public void setConfigurationLabel(final String configurationLabel)
	{
		this.configurationLabel = configurationLabel;
	}

	public String getConfigurationLabel() 
	{
		return configurationLabel;
	}
	
	public void setConfigurationValue(final String configurationValue)
	{
		this.configurationValue = configurationValue;
	}

	public String getConfigurationValue() 
	{
		return configurationValue;
	}
	
	public void setConfigId(final String configId)
	{
		this.configId = configId;
	}

	public String getConfigId() 
	{
		return configId;
	}
	

}