/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponservices.jalo;

import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.customercouponservices.constants.CustomercouponservicesConstants;
import de.hybris.platform.customercouponservices.jalo.CustomerCoupon;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.user.Customer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem CouponNotification}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedCouponNotification extends GenericItem
{
	/** Qualifier of the <code>CouponNotification.customerCoupon</code> attribute **/
	public static final String CUSTOMERCOUPON = "customerCoupon";
	/** Qualifier of the <code>CouponNotification.customer</code> attribute **/
	public static final String CUSTOMER = "customer";
	/** Qualifier of the <code>CouponNotification.status</code> attribute **/
	public static final String STATUS = "status";
	/** Qualifier of the <code>CouponNotification.baseSite</code> attribute **/
	public static final String BASESITE = "baseSite";
	/** Qualifier of the <code>CouponNotification.language</code> attribute **/
	public static final String LANGUAGE = "language";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CUSTOMERCOUPON, AttributeMode.INITIAL);
		tmp.put(CUSTOMER, AttributeMode.INITIAL);
		tmp.put(STATUS, AttributeMode.INITIAL);
		tmp.put(BASESITE, AttributeMode.INITIAL);
		tmp.put(LANGUAGE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponNotification.baseSite</code> attribute.
	 * @return the baseSite - Attribute contains base site object that will be used in the process.
	 */
	public BaseSite getBaseSite(final SessionContext ctx)
	{
		return (BaseSite)getProperty( ctx, BASESITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponNotification.baseSite</code> attribute.
	 * @return the baseSite - Attribute contains base site object that will be used in the process.
	 */
	public BaseSite getBaseSite()
	{
		return getBaseSite( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CouponNotification.baseSite</code> attribute. 
	 * @param value the baseSite - Attribute contains base site object that will be used in the process.
	 */
	public void setBaseSite(final SessionContext ctx, final BaseSite value)
	{
		setProperty(ctx, BASESITE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CouponNotification.baseSite</code> attribute. 
	 * @param value the baseSite - Attribute contains base site object that will be used in the process.
	 */
	public void setBaseSite(final BaseSite value)
	{
		setBaseSite( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponNotification.customer</code> attribute.
	 * @return the customer
	 */
	public Customer getCustomer(final SessionContext ctx)
	{
		return (Customer)getProperty( ctx, CUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponNotification.customer</code> attribute.
	 * @return the customer
	 */
	public Customer getCustomer()
	{
		return getCustomer( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CouponNotification.customer</code> attribute. 
	 * @param value the customer
	 */
	public void setCustomer(final SessionContext ctx, final Customer value)
	{
		setProperty(ctx, CUSTOMER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CouponNotification.customer</code> attribute. 
	 * @param value the customer
	 */
	public void setCustomer(final Customer value)
	{
		setCustomer( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponNotification.customerCoupon</code> attribute.
	 * @return the customerCoupon
	 */
	public CustomerCoupon getCustomerCoupon(final SessionContext ctx)
	{
		return (CustomerCoupon)getProperty( ctx, CUSTOMERCOUPON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponNotification.customerCoupon</code> attribute.
	 * @return the customerCoupon
	 */
	public CustomerCoupon getCustomerCoupon()
	{
		return getCustomerCoupon( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CouponNotification.customerCoupon</code> attribute. 
	 * @param value the customerCoupon
	 */
	public void setCustomerCoupon(final SessionContext ctx, final CustomerCoupon value)
	{
		setProperty(ctx, CUSTOMERCOUPON,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CouponNotification.customerCoupon</code> attribute. 
	 * @param value the customerCoupon
	 */
	public void setCustomerCoupon(final CustomerCoupon value)
	{
		setCustomerCoupon( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponNotification.language</code> attribute.
	 * @return the language
	 */
	public Language getLanguage(final SessionContext ctx)
	{
		return (Language)getProperty( ctx, LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponNotification.language</code> attribute.
	 * @return the language
	 */
	public Language getLanguage()
	{
		return getLanguage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CouponNotification.language</code> attribute. 
	 * @param value the language
	 */
	public void setLanguage(final SessionContext ctx, final Language value)
	{
		setProperty(ctx, LANGUAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CouponNotification.language</code> attribute. 
	 * @param value the language
	 */
	public void setLanguage(final Language value)
	{
		setLanguage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponNotification.status</code> attribute.
	 * @return the status - Coupon notification status.
	 */
	public EnumerationValue getStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponNotification.status</code> attribute.
	 * @return the status - Coupon notification status.
	 */
	public EnumerationValue getStatus()
	{
		return getStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CouponNotification.status</code> attribute. 
	 * @param value the status - Coupon notification status.
	 */
	public void setStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, STATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CouponNotification.status</code> attribute. 
	 * @param value the status - Coupon notification status.
	 */
	public void setStatus(final EnumerationValue value)
	{
		setStatus( getSession().getSessionContext(), value );
	}
	
}
