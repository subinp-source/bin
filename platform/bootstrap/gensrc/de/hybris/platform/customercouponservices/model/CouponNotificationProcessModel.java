/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.customercouponservices.model.CouponNotificationModel;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type couponNotificationProcess first defined at extension customercouponservices.
 * <p>
 * Represents The coupon notification Process.
 */
@SuppressWarnings("all")
public class CouponNotificationProcessModel extends BusinessProcessModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "couponNotificationProcess";
	
	/** <i>Generated constant</i> - Attribute key of <code>couponNotificationProcess.language</code> attribute defined at extension <code>customercouponservices</code>. */
	public static final String LANGUAGE = "language";
	
	/** <i>Generated constant</i> - Attribute key of <code>couponNotificationProcess.couponNotification</code> attribute defined at extension <code>customercouponservices</code>. */
	public static final String COUPONNOTIFICATION = "couponNotification";
	
	/** <i>Generated constant</i> - Attribute key of <code>couponNotificationProcess.notificationType</code> attribute defined at extension <code>customercouponservices</code>. */
	public static final String NOTIFICATIONTYPE = "notificationType";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CouponNotificationProcessModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CouponNotificationProcessModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 * @param _processDefinitionName initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public CouponNotificationProcessModel(final String _code, final String _processDefinitionName)
	{
		super();
		setCode(_code);
		setProcessDefinitionName(_processDefinitionName);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _processDefinitionName initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public CouponNotificationProcessModel(final String _code, final ItemModel _owner, final String _processDefinitionName)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setProcessDefinitionName(_processDefinitionName);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>couponNotificationProcess.couponNotification</code> attribute defined at extension <code>customercouponservices</code>. 
	 * @return the couponNotification - Attribute contains the coupon notification
	 */
	@Accessor(qualifier = "couponNotification", type = Accessor.Type.GETTER)
	public CouponNotificationModel getCouponNotification()
	{
		return getPersistenceContext().getPropertyValue(COUPONNOTIFICATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>couponNotificationProcess.language</code> attribute defined at extension <code>customercouponservices</code>. 
	 * @return the language - Attribute contains language that will be used in the process.
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public LanguageModel getLanguage()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>couponNotificationProcess.notificationType</code> attribute defined at extension <code>customercouponservices</code>. 
	 * @return the notificationType - Notification type
	 */
	@Accessor(qualifier = "notificationType", type = Accessor.Type.GETTER)
	public NotificationType getNotificationType()
	{
		return getPersistenceContext().getPropertyValue(NOTIFICATIONTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>couponNotificationProcess.couponNotification</code> attribute defined at extension <code>customercouponservices</code>. 
	 *  
	 * @param value the couponNotification - Attribute contains the coupon notification
	 */
	@Accessor(qualifier = "couponNotification", type = Accessor.Type.SETTER)
	public void setCouponNotification(final CouponNotificationModel value)
	{
		getPersistenceContext().setPropertyValue(COUPONNOTIFICATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>couponNotificationProcess.language</code> attribute defined at extension <code>customercouponservices</code>. 
	 *  
	 * @param value the language - Attribute contains language that will be used in the process.
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>couponNotificationProcess.notificationType</code> attribute defined at extension <code>customercouponservices</code>. 
	 *  
	 * @param value the notificationType - Notification type
	 */
	@Accessor(qualifier = "notificationType", type = Accessor.Type.SETTER)
	public void setNotificationType(final NotificationType value)
	{
		getPersistenceContext().setPropertyValue(NOTIFICATIONTYPE, value);
	}
	
}
