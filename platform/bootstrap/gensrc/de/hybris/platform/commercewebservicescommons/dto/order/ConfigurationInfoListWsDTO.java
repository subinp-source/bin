/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.order;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.order.ConfigurationInfoWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of a Configuration Info List
 */
@ApiModel(value="ConfigurationInfoList", description="Representation of a Configuration Info List")
public  class ConfigurationInfoListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** List of configuration info<br/><br/><i>Generated property</i> for <code>ConfigurationInfoListWsDTO.configurationInfos</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="configurationInfos", value="List of configuration info") 	
	private List<ConfigurationInfoWsDTO> configurationInfos;
	
	public ConfigurationInfoListWsDTO()
	{
		// default constructor
	}
	
	public void setConfigurationInfos(final List<ConfigurationInfoWsDTO> configurationInfos)
	{
		this.configurationInfos = configurationInfos;
	}

	public List<ConfigurationInfoWsDTO> getConfigurationInfos() 
	{
		return configurationInfos;
	}
	

}