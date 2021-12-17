/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.order;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a Configuration Info
 */
@ApiModel(value="ConfigurationInfo", description="Representation of a Configuration Info")
public  class ConfigurationInfoWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Type of configuration info<br/><br/><i>Generated property</i> for <code>ConfigurationInfoWsDTO.configuratorType</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="configuratorType", value="Type of configuration info") 	
	private String configuratorType;

	/** Status of configuration info<br/><br/><i>Generated property</i> for <code>ConfigurationInfoWsDTO.status</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="status", value="Status of configuration info") 	
	private String status;

	/** Label of configuration info<br/><br/><i>Generated property</i> for <code>ConfigurationInfoWsDTO.configurationLabel</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="configurationLabel", value="Label of configuration info") 	
	private String configurationLabel;

	/** Value of configuration info<br/><br/><i>Generated property</i> for <code>ConfigurationInfoWsDTO.configurationValue</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="configurationValue", value="Value of configuration info") 	
	private String configurationValue;
	
	public ConfigurationInfoWsDTO()
	{
		// default constructor
	}
	
	public void setConfiguratorType(final String configuratorType)
	{
		this.configuratorType = configuratorType;
	}

	public String getConfiguratorType() 
	{
		return configuratorType;
	}
	
	public void setStatus(final String status)
	{
		this.status = status;
	}

	public String getStatus() 
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
	

}