/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.frontend.util.impl;

import java.io.Serializable;
import de.hybris.platform.sap.productconfig.frontend.util.impl.ErrorType;


import java.util.Objects;
public  class ErrorMessage  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ErrorMessage.path</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private String path;

	/** <i>Generated property</i> for <code>ErrorMessage.code</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>ErrorMessage.message</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private String message;

	/** <i>Generated property</i> for <code>ErrorMessage.args</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private Object[] args;

	/** <i>Generated property</i> for <code>ErrorMessage.type</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private ErrorType type;
	
	public ErrorMessage()
	{
		// default constructor
	}
	
	public void setPath(final String path)
	{
		this.path = path;
	}

	public String getPath() 
	{
		return path;
	}
	
	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	
	public void setMessage(final String message)
	{
		this.message = message;
	}

	public String getMessage() 
	{
		return message;
	}
	
	public void setArgs(final Object[] args)
	{
		this.args = args;
	}

	public Object[] getArgs() 
	{
		return args;
	}
	
	public void setType(final ErrorType type)
	{
		this.type = type;
	}

	public ErrorType getType() 
	{
		return type;
	}
	

}