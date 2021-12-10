/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsfacades.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.notificationfacades.data.NotificationPreferenceData;
import de.hybris.platform.notificationservices.enums.NotificationType;
import java.util.Date;
import java.util.List;


import java.util.Objects;
public  class ProductInterestData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ProductInterestData.expiryDate</code> property defined at extension <code>customerinterestsfacades</code>. */
		
	private Date expiryDate;

	/** <i>Generated property</i> for <code>ProductInterestData.creationDate</code> property defined at extension <code>customerinterestsfacades</code>. */
		
	private Date creationDate;

	/** <i>Generated property</i> for <code>ProductInterestData.siteMsgNotificationEnabled</code> property defined at extension <code>customerinterestsfacades</code>. */
		
	private boolean siteMsgNotificationEnabled;

	/** <i>Generated property</i> for <code>ProductInterestData.emailNotificationEnabled</code> property defined at extension <code>customerinterestsfacades</code>. */
		
	private boolean emailNotificationEnabled;

	/** <i>Generated property</i> for <code>ProductInterestData.smsNotificationEnabled</code> property defined at extension <code>customerinterestsfacades</code>. */
		
	private boolean smsNotificationEnabled;

	/** <i>Generated property</i> for <code>ProductInterestData.emailAddress</code> property defined at extension <code>customerinterestsfacades</code>. */
		
	private String emailAddress;

	/** <i>Generated property</i> for <code>ProductInterestData.mobileNumber</code> property defined at extension <code>customerinterestsfacades</code>. */
		
	private String mobileNumber;

	/** <i>Generated property</i> for <code>ProductInterestData.notificationType</code> property defined at extension <code>customerinterestsfacades</code>. */
		
	private NotificationType notificationType;

	/** <i>Generated property</i> for <code>ProductInterestData.product</code> property defined at extension <code>customerinterestsfacades</code>. */
		
	private ProductData product;

	/** <i>Generated property</i> for <code>ProductInterestData.notificationChannels</code> property defined at extension <code>customerinterestsfacades</code>. */
		
	private List<NotificationPreferenceData> notificationChannels;
	
	public ProductInterestData()
	{
		// default constructor
	}
	
	public void setExpiryDate(final Date expiryDate)
	{
		this.expiryDate = expiryDate;
	}

	public Date getExpiryDate() 
	{
		return expiryDate;
	}
	
	public void setCreationDate(final Date creationDate)
	{
		this.creationDate = creationDate;
	}

	public Date getCreationDate() 
	{
		return creationDate;
	}
	
	public void setSiteMsgNotificationEnabled(final boolean siteMsgNotificationEnabled)
	{
		this.siteMsgNotificationEnabled = siteMsgNotificationEnabled;
	}

	public boolean isSiteMsgNotificationEnabled() 
	{
		return siteMsgNotificationEnabled;
	}
	
	/**
	 * @deprecated true
	 */
	@Deprecated(since = "6.7", forRemoval = true)
	public void setEmailNotificationEnabled(final boolean emailNotificationEnabled)
	{
		this.emailNotificationEnabled = emailNotificationEnabled;
	}

	/**
	 * @deprecated true
	 */
	@Deprecated(since = "6.7", forRemoval = true)
	public boolean isEmailNotificationEnabled() 
	{
		return emailNotificationEnabled;
	}
	
	/**
	 * @deprecated true
	 */
	@Deprecated(since = "6.7", forRemoval = true)
	public void setSmsNotificationEnabled(final boolean smsNotificationEnabled)
	{
		this.smsNotificationEnabled = smsNotificationEnabled;
	}

	/**
	 * @deprecated true
	 */
	@Deprecated(since = "6.7", forRemoval = true)
	public boolean isSmsNotificationEnabled() 
	{
		return smsNotificationEnabled;
	}
	
	/**
	 * @deprecated true
	 */
	@Deprecated(since = "6.7", forRemoval = true)
	public void setEmailAddress(final String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	/**
	 * @deprecated true
	 */
	@Deprecated(since = "6.7", forRemoval = true)
	public String getEmailAddress() 
	{
		return emailAddress;
	}
	
	/**
	 * @deprecated true
	 */
	@Deprecated(since = "6.7", forRemoval = true)
	public void setMobileNumber(final String mobileNumber)
	{
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @deprecated true
	 */
	@Deprecated(since = "6.7", forRemoval = true)
	public String getMobileNumber() 
	{
		return mobileNumber;
	}
	
	public void setNotificationType(final NotificationType notificationType)
	{
		this.notificationType = notificationType;
	}

	public NotificationType getNotificationType() 
	{
		return notificationType;
	}
	
	public void setProduct(final ProductData product)
	{
		this.product = product;
	}

	public ProductData getProduct() 
	{
		return product;
	}
	
	public void setNotificationChannels(final List<NotificationPreferenceData> notificationChannels)
	{
		this.notificationChannels = notificationChannels;
	}

	public List<NotificationPreferenceData> getNotificationChannels() 
	{
		return notificationChannels;
	}
	

}