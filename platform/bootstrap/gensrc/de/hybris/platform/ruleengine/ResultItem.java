/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ruleengine;

import java.io.Serializable;
import de.hybris.platform.ruleengine.MessageLevel;


import java.util.Objects;
public  class ResultItem  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ResultItem.message</code> property defined at extension <code>ruleengine</code>. */
		
	private String message;

	/** <i>Generated property</i> for <code>ResultItem.level</code> property defined at extension <code>ruleengine</code>. */
		
	private MessageLevel level;

	/** <i>Generated property</i> for <code>ResultItem.path</code> property defined at extension <code>ruleengine</code>. */
		
	private String path;

	/** <i>Generated property</i> for <code>ResultItem.line</code> property defined at extension <code>ruleengine</code>. */
		
	private int line;
	
	public ResultItem()
	{
		// default constructor
	}
	
	public void setMessage(final String message)
	{
		this.message = message;
	}

	public String getMessage() 
	{
		return message;
	}
	
	public void setLevel(final MessageLevel level)
	{
		this.level = level;
	}

	public MessageLevel getLevel() 
	{
		return level;
	}
	
	public void setPath(final String path)
	{
		this.path = path;
	}

	public String getPath() 
	{
		return path;
	}
	
	public void setLine(final int line)
	{
		this.line = line;
	}

	public int getLine() 
	{
		return line;
	}
	

}