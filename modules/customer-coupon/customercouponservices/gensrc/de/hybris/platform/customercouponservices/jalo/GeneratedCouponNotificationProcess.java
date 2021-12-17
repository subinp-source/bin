/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponservices.jalo;

import de.hybris.platform.customercouponservices.constants.CustomercouponservicesConstants;
import de.hybris.platform.customercouponservices.jalo.CouponNotification;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.processengine.jalo.BusinessProcess;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.processengine.jalo.BusinessProcess couponNotificationProcess}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedCouponNotificationProcess extends BusinessProcess
{
	/** Qualifier of the <code>couponNotificationProcess.language</code> attribute **/
	public static final String LANGUAGE = "language";
	/** Qualifier of the <code>couponNotificationProcess.couponNotification</code> attribute **/
	public static final String COUPONNOTIFICATION = "couponNotification";
	/** Qualifier of the <code>couponNotificationProcess.notificationType</code> attribute **/
	public static final String NOTIFICATIONTYPE = "notificationType";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(BusinessProcess.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(LANGUAGE, AttributeMode.INITIAL);
		tmp.put(COUPONNOTIFICATION, AttributeMode.INITIAL);
		tmp.put(NOTIFICATIONTYPE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>couponNotificationProcess.couponNotification</code> attribute.
	 * @return the couponNotification - Attribute contains the coupon notification
	 */
	public CouponNotification getCouponNotification(final SessionContext ctx)
	{
		return (CouponNotification)getProperty( ctx, COUPONNOTIFICATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>couponNotificationProcess.couponNotification</code> attribute.
	 * @return the couponNotification - Attribute contains the coupon notification
	 */
	public CouponNotification getCouponNotification()
	{
		return getCouponNotification( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>couponNotificationProcess.couponNotification</code> attribute. 
	 * @param value the couponNotification - Attribute contains the coupon notification
	 */
	public void setCouponNotification(final SessionContext ctx, final CouponNotification value)
	{
		setProperty(ctx, COUPONNOTIFICATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>couponNotificationProcess.couponNotification</code> attribute. 
	 * @param value the couponNotification - Attribute contains the coupon notification
	 */
	public void setCouponNotification(final CouponNotification value)
	{
		setCouponNotification( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>couponNotificationProcess.language</code> attribute.
	 * @return the language - Attribute contains language that will be used in the process.
	 */
	public Language getLanguage(final SessionContext ctx)
	{
		return (Language)getProperty( ctx, LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>couponNotificationProcess.language</code> attribute.
	 * @return the language - Attribute contains language that will be used in the process.
	 */
	public Language getLanguage()
	{
		return getLanguage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>couponNotificationProcess.language</code> attribute. 
	 * @param value the language - Attribute contains language that will be used in the process.
	 */
	public void setLanguage(final SessionContext ctx, final Language value)
	{
		setProperty(ctx, LANGUAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>couponNotificationProcess.language</code> attribute. 
	 * @param value the language - Attribute contains language that will be used in the process.
	 */
	public void setLanguage(final Language value)
	{
		setLanguage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>couponNotificationProcess.notificationType</code> attribute.
	 * @return the notificationType - Notification type
	 */
	public EnumerationValue getNotificationType(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, NOTIFICATIONTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>couponNotificationProcess.notificationType</code> attribute.
	 * @return the notificationType - Notification type
	 */
	public EnumerationValue getNotificationType()
	{
		return getNotificationType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>couponNotificationProcess.notificationType</code> attribute. 
	 * @param value the notificationType - Notification type
	 */
	public void setNotificationType(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, NOTIFICATIONTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>couponNotificationProcess.notificationType</code> attribute. 
	 * @param value the notificationType - Notification type
	 */
	public void setNotificationType(final EnumerationValue value)
	{
		setNotificationType( getSession().getSessionContext(), value );
	}
	
}
