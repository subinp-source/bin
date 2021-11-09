/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.webservicescommons.dto.error;

import java.io.Serializable;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * List of errors
 */
@ApiModel(value="errorList", description="List of errors")
public  class ErrorListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ErrorListWsDTO.errors</code> property defined at extension <code>webservicescommons</code>. */
	@ApiModelProperty(name="errors") 	
	private List<ErrorWsDTO> errors;
	
	public ErrorListWsDTO()
	{
		// default constructor
	}
	
	public void setErrors(final List<ErrorWsDTO> errors)
	{
		this.errors = errors;
	}

	public List<ErrorWsDTO> getErrors() 
	{
		return errors;
	}
	

}