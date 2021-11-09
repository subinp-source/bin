/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationfacades.data;

import java.io.Serializable;
import de.hybris.platform.notificationservices.enums.NotificationChannel;


import java.util.Objects;
public  class NotificationPreferenceData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>NotificationPreferenceData.emailEnabled</code> property defined at extension <code>notificationfacades</code>. */
		
	private boolean emailEnabled;

	/** <i>Generated property</i> for <code>NotificationPreferenceData.smsEnabled</code> property defined at extension <code>notificationfacades</code>. */
		
	private boolean smsEnabled;

	/** <i>Generated property</i> for <code>NotificationPreferenceData.emailAddress</code> property defined at extension <code>notificationfacades</code>. */
		
	private String emailAddress;

	/** <i>Generated property</i> for <code>NotificationPreferenceData.mobileNumber</code> property defined at extension <code>notificationfacades</code>. */
		
	private String mobileNumber;

	/** <i>Generated property</i> for <code>NotificationPreferenceData.channel</code> property defined at extension <code>notificationfacades</code>. */
		
	private NotificationChannel channel;

	/** <i>Generated property</i> for <code>NotificationPreferenceData.value</code> property defined at extension <code>notificationfacades</code>. */
		
	private String value;

	/** <i>Generated property</i> for <code>NotificationPreferenceData.enabled</code> property defined at extension <code>notificationfacades</code>. */
		
	private boolean enabled;

	/** <i>Generated property</i> for <code>NotificationPreferenceData.visible</code> property defined at extension <code>notificationfacades</code>. */
		
	private boolean visible;
	
	public NotificationPreferenceData()
	{
		// default constructor
	}
	
	/**
	 * @deprecated true
	 */
	@Deprecated(since = "1905", forRemoval = true)
	public void setEmailEnabled(final boolean emailEnabled)
	{
		this.emailEnabled = emailEnabled;
	}

	/**
	 * @deprecated true
	 */
	@Deprecated(since = "1905", forRemoval = true)
	public boolean isEmailEnabled() 
	{
		return emailEnabled;
	}
	
	/**
	 * @deprecated true
	 */
	@Deprecated(since = "1905", forRemoval = true)
	public void setSmsEnabled(final boolean smsEnabled)
	{
		this.smsEnabled = smsEnabled;
	}

	/**
	 * @deprecated true
	 */
	@Deprecated(since = "1905", forRemoval = true)
	public boolean isSmsEnabled() 
	{
		return smsEnabled;
	}
	
	/**
	 * @deprecated true
	 */
	@Deprecated(since = "1905", forRemoval = true)
	public void setEmailAddress(final String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	/**
	 * @deprecated true
	 */
	@Deprecated(since = "1905", forRemoval = true)
	public String getEmailAddress() 
	{
		return emailAddress;
	}
	
	/**
	 * @deprecated true
	 */
	@Deprecated(since = "1905", forRemoval = true)
	public void setMobileNumber(final String mobileNumber)
	{
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @deprecated true
	 */
	@Deprecated(since = "1905", forRemoval = true)
	public String getMobileNumber() 
	{
		return mobileNumber;
	}
	
	public void setChannel(final NotificationChannel channel)
	{
		this.channel = channel;
	}

	public NotificationChannel getChannel() 
	{
		return channel;
	}
	
	public void setValue(final String value)
	{
		this.value = value;
	}

	public String getValue() 
	{
		return value;
	}
	
	public void setEnabled(final boolean enabled)
	{
		this.enabled = enabled;
	}

	public boolean isEnabled() 
	{
		return enabled;
	}
	
	public void setVisible(final boolean visible)
	{
		this.visible = visible;
	}

	public boolean isVisible() 
	{
		return visible;
	}
	

}