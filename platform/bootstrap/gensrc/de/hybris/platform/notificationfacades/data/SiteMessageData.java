/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationfacades.data;

import java.io.Serializable;
import de.hybris.platform.notificationservices.enums.NotificationType;
import java.util.Date;


import java.util.Objects;
public  class SiteMessageData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SiteMessageData.uid</code> property defined at extension <code>notificationfacades</code>. */
		
	private String uid;

	/** <i>Generated property</i> for <code>SiteMessageData.title</code> property defined at extension <code>notificationfacades</code>. */
		
	private String title;

	/** <i>Generated property</i> for <code>SiteMessageData.content</code> property defined at extension <code>notificationfacades</code>. */
		
	private String content;

	/** <i>Generated property</i> for <code>SiteMessageData.subject</code> property defined at extension <code>notificationfacades</code>. */
		
	private String subject;

	/** <i>Generated property</i> for <code>SiteMessageData.body</code> property defined at extension <code>notificationfacades</code>. */
		
	private String body;

	/** <i>Generated property</i> for <code>SiteMessageData.link</code> property defined at extension <code>notificationfacades</code>. */
		
	private String link;

	/** <i>Generated property</i> for <code>SiteMessageData.notificationType</code> property defined at extension <code>notificationfacades</code>. */
		
	private NotificationType notificationType;

	/** <i>Generated property</i> for <code>SiteMessageData.sentDate</code> property defined at extension <code>notificationfacades</code>. */
		
	private Date sentDate;
	
	public SiteMessageData()
	{
		// default constructor
	}
	
	public void setUid(final String uid)
	{
		this.uid = uid;
	}

	public String getUid() 
	{
		return uid;
	}
	
	/**
	 * @deprecated true
	 */
	@Deprecated(since = "1905", forRemoval = true)
	public void setTitle(final String title)
	{
		this.title = title;
	}

	/**
	 * @deprecated true
	 */
	@Deprecated(since = "1905", forRemoval = true)
	public String getTitle() 
	{
		return title;
	}
	
	/**
	 * @deprecated true
	 */
	@Deprecated(since = "1905", forRemoval = true)
	public void setContent(final String content)
	{
		this.content = content;
	}

	/**
	 * @deprecated true
	 */
	@Deprecated(since = "1905", forRemoval = true)
	public String getContent() 
	{
		return content;
	}
	
	public void setSubject(final String subject)
	{
		this.subject = subject;
	}

	public String getSubject() 
	{
		return subject;
	}
	
	public void setBody(final String body)
	{
		this.body = body;
	}

	public String getBody() 
	{
		return body;
	}
	
	public void setLink(final String link)
	{
		this.link = link;
	}

	public String getLink() 
	{
		return link;
	}
	
	public void setNotificationType(final NotificationType notificationType)
	{
		this.notificationType = notificationType;
	}

	public NotificationType getNotificationType() 
	{
		return notificationType;
	}
	
	public void setSentDate(final Date sentDate)
	{
		this.sentDate = sentDate;
	}

	public Date getSentDate() 
	{
		return sentDate;
	}
	

}