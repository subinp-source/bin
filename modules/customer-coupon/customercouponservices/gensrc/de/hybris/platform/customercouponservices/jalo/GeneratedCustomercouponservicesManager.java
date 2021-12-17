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
import de.hybris.platform.customercouponservices.jalo.CouponNotificationProcess;
import de.hybris.platform.customercouponservices.jalo.CustomerCoupon;
import de.hybris.platform.customercouponservices.jalo.CustomerCouponForPromotionSourceRule;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type <code>CustomercouponservicesManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedCustomercouponservicesManager extends Extension
{
	/** Relation ordering override parameter constants for CustomerCoupon2Customer from ((customercouponservices))*/
	protected static String CUSTOMERCOUPON2CUSTOMER_SRC_ORDERED = "relation.CustomerCoupon2Customer.source.ordered";
	protected static String CUSTOMERCOUPON2CUSTOMER_TGT_ORDERED = "relation.CustomerCoupon2Customer.target.ordered";
	/** Relation disable markmodifed parameter constants for CustomerCoupon2Customer from ((customercouponservices))*/
	protected static String CUSTOMERCOUPON2CUSTOMER_MARKMODIFIED = "relation.CustomerCoupon2Customer.markmodified";
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	public CouponNotification createCouponNotification(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CustomercouponservicesConstants.TC.COUPONNOTIFICATION );
			return (CouponNotification)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating CouponNotification : "+e.getMessage(), 0 );
		}
	}
	
	public CouponNotification createCouponNotification(final Map attributeValues)
	{
		return createCouponNotification( getSession().getSessionContext(), attributeValues );
	}
	
	public CouponNotificationProcess createCouponNotificationProcess(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CustomercouponservicesConstants.TC.COUPONNOTIFICATIONPROCESS );
			return (CouponNotificationProcess)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating couponNotificationProcess : "+e.getMessage(), 0 );
		}
	}
	
	public CouponNotificationProcess createCouponNotificationProcess(final Map attributeValues)
	{
		return createCouponNotificationProcess( getSession().getSessionContext(), attributeValues );
	}
	
	public CustomerCoupon createCustomerCoupon(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CustomercouponservicesConstants.TC.CUSTOMERCOUPON );
			return (CustomerCoupon)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating CustomerCoupon : "+e.getMessage(), 0 );
		}
	}
	
	public CustomerCoupon createCustomerCoupon(final Map attributeValues)
	{
		return createCustomerCoupon( getSession().getSessionContext(), attributeValues );
	}
	
	public CustomerCouponForPromotionSourceRule createCustomerCouponForPromotionSourceRule(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CustomercouponservicesConstants.TC.CUSTOMERCOUPONFORPROMOTIONSOURCERULE );
			return (CustomerCouponForPromotionSourceRule)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating CustomerCouponForPromotionSourceRule : "+e.getMessage(), 0 );
		}
	}
	
	public CustomerCouponForPromotionSourceRule createCustomerCouponForPromotionSourceRule(final Map attributeValues)
	{
		return createCustomerCouponForPromotionSourceRule( getSession().getSessionContext(), attributeValues );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.customerCoupons</code> attribute.
	 * @return the customerCoupons - Customer Coupons
	 */
	public Collection<CustomerCoupon> getCustomerCoupons(final SessionContext ctx, final Customer item)
	{
		final List<CustomerCoupon> items = item.getLinkedItems( 
			ctx,
			false,
			CustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER,
			"CustomerCoupon",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.customerCoupons</code> attribute.
	 * @return the customerCoupons - Customer Coupons
	 */
	public Collection<CustomerCoupon> getCustomerCoupons(final Customer item)
	{
		return getCustomerCoupons( getSession().getSessionContext(), item );
	}
	
	public long getCustomerCouponsCount(final SessionContext ctx, final Customer item)
	{
		return item.getLinkedItemsCount(
			ctx,
			false,
			CustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER,
			"CustomerCoupon",
			null
		);
	}
	
	public long getCustomerCouponsCount(final Customer item)
	{
		return getCustomerCouponsCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.customerCoupons</code> attribute. 
	 * @param value the customerCoupons - Customer Coupons
	 */
	public void setCustomerCoupons(final SessionContext ctx, final Customer item, final Collection<CustomerCoupon> value)
	{
		item.setLinkedItems( 
			ctx,
			false,
			CustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(CUSTOMERCOUPON2CUSTOMER_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.customerCoupons</code> attribute. 
	 * @param value the customerCoupons - Customer Coupons
	 */
	public void setCustomerCoupons(final Customer item, final Collection<CustomerCoupon> value)
	{
		setCustomerCoupons( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to customerCoupons. 
	 * @param value the item to add to customerCoupons - Customer Coupons
	 */
	public void addToCustomerCoupons(final SessionContext ctx, final Customer item, final CustomerCoupon value)
	{
		item.addLinkedItems( 
			ctx,
			false,
			CustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(CUSTOMERCOUPON2CUSTOMER_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to customerCoupons. 
	 * @param value the item to add to customerCoupons - Customer Coupons
	 */
	public void addToCustomerCoupons(final Customer item, final CustomerCoupon value)
	{
		addToCustomerCoupons( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from customerCoupons. 
	 * @param value the item to remove from customerCoupons - Customer Coupons
	 */
	public void removeFromCustomerCoupons(final SessionContext ctx, final Customer item, final CustomerCoupon value)
	{
		item.removeLinkedItems( 
			ctx,
			false,
			CustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(CUSTOMERCOUPON2CUSTOMER_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from customerCoupons. 
	 * @param value the item to remove from customerCoupons - Customer Coupons
	 */
	public void removeFromCustomerCoupons(final Customer item, final CustomerCoupon value)
	{
		removeFromCustomerCoupons( getSession().getSessionContext(), item, value );
	}
	
	@Override
	public String getName()
	{
		return CustomercouponservicesConstants.EXTENSIONNAME;
	}
	
}
