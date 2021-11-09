/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponservices.enums.CouponNotificationStatus;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CouponNotification first defined at extension customercouponservices.
 */
@SuppressWarnings("all")
public class CouponNotificationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CouponNotification";
	
	/** <i>Generated constant</i> - Attribute key of <code>CouponNotification.customerCoupon</code> attribute defined at extension <code>customercouponservices</code>. */
	public static final String CUSTOMERCOUPON = "customerCoupon";
	
	/** <i>Generated constant</i> - Attribute key of <code>CouponNotification.customer</code> attribute defined at extension <code>customercouponservices</code>. */
	public static final String CUSTOMER = "customer";
	
	/** <i>Generated constant</i> - Attribute key of <code>CouponNotification.status</code> attribute defined at extension <code>customercouponservices</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>CouponNotification.baseSite</code> attribute defined at extension <code>customercouponservices</code>. */
	public static final String BASESITE = "baseSite";
	
	/** <i>Generated constant</i> - Attribute key of <code>CouponNotification.language</code> attribute defined at extension <code>customercouponservices</code>. */
	public static final String LANGUAGE = "language";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CouponNotificationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CouponNotificationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseSite initial attribute declared by type <code>CouponNotification</code> at extension <code>customercouponservices</code>
	 * @param _customer initial attribute declared by type <code>CouponNotification</code> at extension <code>customercouponservices</code>
	 * @param _customerCoupon initial attribute declared by type <code>CouponNotification</code> at extension <code>customercouponservices</code>
	 * @param _language initial attribute declared by type <code>CouponNotification</code> at extension <code>customercouponservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public CouponNotificationModel(final BaseSiteModel _baseSite, final CustomerModel _customer, final CustomerCouponModel _customerCoupon, final LanguageModel _language)
	{
		super();
		setBaseSite(_baseSite);
		setCustomer(_customer);
		setCustomerCoupon(_customerCoupon);
		setLanguage(_language);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseSite initial attribute declared by type <code>CouponNotification</code> at extension <code>customercouponservices</code>
	 * @param _customer initial attribute declared by type <code>CouponNotification</code> at extension <code>customercouponservices</code>
	 * @param _customerCoupon initial attribute declared by type <code>CouponNotification</code> at extension <code>customercouponservices</code>
	 * @param _language initial attribute declared by type <code>CouponNotification</code> at extension <code>customercouponservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public CouponNotificationModel(final BaseSiteModel _baseSite, final CustomerModel _customer, final CustomerCouponModel _customerCoupon, final LanguageModel _language, final ItemModel _owner)
	{
		super();
		setBaseSite(_baseSite);
		setCustomer(_customer);
		setCustomerCoupon(_customerCoupon);
		setLanguage(_language);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponNotification.baseSite</code> attribute defined at extension <code>customercouponservices</code>. 
	 * @return the baseSite - Attribute contains base site object that will be used in the process.
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.GETTER)
	public BaseSiteModel getBaseSite()
	{
		return getPersistenceContext().getPropertyValue(BASESITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponNotification.customer</code> attribute defined at extension <code>customercouponservices</code>. 
	 * @return the customer
	 */
	@Accessor(qualifier = "customer", type = Accessor.Type.GETTER)
	public CustomerModel getCustomer()
	{
		return getPersistenceContext().getPropertyValue(CUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponNotification.customerCoupon</code> attribute defined at extension <code>customercouponservices</code>. 
	 * @return the customerCoupon
	 */
	@Accessor(qualifier = "customerCoupon", type = Accessor.Type.GETTER)
	public CustomerCouponModel getCustomerCoupon()
	{
		return getPersistenceContext().getPropertyValue(CUSTOMERCOUPON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponNotification.language</code> attribute defined at extension <code>customercouponservices</code>. 
	 * @return the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public LanguageModel getLanguage()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponNotification.status</code> attribute defined at extension <code>customercouponservices</code>. 
	 * @return the status - Coupon notification status.
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public CouponNotificationStatus getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CouponNotification.baseSite</code> attribute defined at extension <code>customercouponservices</code>. 
	 *  
	 * @param value the baseSite - Attribute contains base site object that will be used in the process.
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.SETTER)
	public void setBaseSite(final BaseSiteModel value)
	{
		getPersistenceContext().setPropertyValue(BASESITE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CouponNotification.customer</code> attribute defined at extension <code>customercouponservices</code>. 
	 *  
	 * @param value the customer
	 */
	@Accessor(qualifier = "customer", type = Accessor.Type.SETTER)
	public void setCustomer(final CustomerModel value)
	{
		getPersistenceContext().setPropertyValue(CUSTOMER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CouponNotification.customerCoupon</code> attribute defined at extension <code>customercouponservices</code>. 
	 *  
	 * @param value the customerCoupon
	 */
	@Accessor(qualifier = "customerCoupon", type = Accessor.Type.SETTER)
	public void setCustomerCoupon(final CustomerCouponModel value)
	{
		getPersistenceContext().setPropertyValue(CUSTOMERCOUPON, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CouponNotification.language</code> attribute defined at extension <code>customercouponservices</code>. 
	 *  
	 * @param value the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CouponNotification.status</code> attribute defined at extension <code>customercouponservices</code>. 
	 *  
	 * @param value the status - Coupon notification status.
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final CouponNotificationStatus value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
}
