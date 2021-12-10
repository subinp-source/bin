/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.Date;


import java.util.Objects;
public  class CMSCommentData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CMSCommentData.text</code> property defined at extension <code>cmsfacades</code>. */
		
	private String text;

	/** <i>Generated property</i> for <code>CMSCommentData.code</code> property defined at extension <code>cmsfacades</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>CMSCommentData.creationtime</code> property defined at extension <code>cmsfacades</code>. */
		
	private Date creationtime;

	/** <i>Generated property</i> for <code>CMSCommentData.authorName</code> property defined at extension <code>cmsfacades</code>. */
		
	private String authorName;

	/** <i>Generated property</i> for <code>CMSCommentData.decisionName</code> property defined at extension <code>cmsfacades</code>. */
		
	private String decisionName;

	/** <i>Generated property</i> for <code>CMSCommentData.decisionCode</code> property defined at extension <code>cmsfacades</code>. */
		
	private String decisionCode;

	/** <i>Generated property</i> for <code>CMSCommentData.originalActionCode</code> property defined at extension <code>cmsfacades</code>. */
		
	private String originalActionCode;

	/** <i>Generated property</i> for <code>CMSCommentData.createdAgoInMillis</code> property defined at extension <code>cmsfacades</code>. */
		
	private Long createdAgoInMillis;
	
	public CMSCommentData()
	{
		// default constructor
	}
	
	public void setText(final String text)
	{
		this.text = text;
	}

	public String getText() 
	{
		return text;
	}
	
	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	
	public void setCreationtime(final Date creationtime)
	{
		this.creationtime = creationtime;
	}

	public Date getCreationtime() 
	{
		return creationtime;
	}
	
	public void setAuthorName(final String authorName)
	{
		this.authorName = authorName;
	}

	public String getAuthorName() 
	{
		return authorName;
	}
	
	public void setDecisionName(final String decisionName)
	{
		this.decisionName = decisionName;
	}

	public String getDecisionName() 
	{
		return decisionName;
	}
	
	public void setDecisionCode(final String decisionCode)
	{
		this.decisionCode = decisionCode;
	}

	public String getDecisionCode() 
	{
		return decisionCode;
	}
	
	public void setOriginalActionCode(final String originalActionCode)
	{
		this.originalActionCode = originalActionCode;
	}

	public String getOriginalActionCode() 
	{
		return originalActionCode;
	}
	
	public void setCreatedAgoInMillis(final Long createdAgoInMillis)
	{
		this.createdAgoInMillis = createdAgoInMillis;
	}

	public Long getCreatedAgoInMillis() 
	{
		return createdAgoInMillis;
	}
	

}