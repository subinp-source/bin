/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponservices.jalo;

import de.hybris.platform.couponservices.jalo.AbstractCoupon;
import de.hybris.platform.customercouponservices.constants.CustomercouponservicesConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.customercouponservices.jalo.CustomerCoupon CustomerCoupon}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedCustomerCoupon extends AbstractCoupon
{
	/** Qualifier of the <code>CustomerCoupon.assignable</code> attribute **/
	public static final String ASSIGNABLE = "assignable";
	/** Qualifier of the <code>CustomerCoupon.description</code> attribute **/
	public static final String DESCRIPTION = "description";
	/** Qualifier of the <code>CustomerCoupon.customers</code> attribute **/
	public static final String CUSTOMERS = "customers";
	/** Relation ordering override parameter constants for CustomerCoupon2Customer from ((customercouponservices))*/
	protected static String CUSTOMERCOUPON2CUSTOMER_SRC_ORDERED = "relation.CustomerCoupon2Customer.source.ordered";
	protected static String CUSTOMERCOUPON2CUSTOMER_TGT_ORDERED = "relation.CustomerCoupon2Customer.target.ordered";
	/** Relation disable markmodifed parameter constants for CustomerCoupon2Customer from ((customercouponservices))*/
	protected static String CUSTOMERCOUPON2CUSTOMER_MARKMODIFIED = "relation.CustomerCoupon2Customer.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(AbstractCoupon.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(ASSIGNABLE, AttributeMode.INITIAL);
		tmp.put(DESCRIPTION, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCoupon.assignable</code> attribute.
	 * @return the assignable
	 */
	public Boolean isAssignable(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ASSIGNABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCoupon.assignable</code> attribute.
	 * @return the assignable
	 */
	public Boolean isAssignable()
	{
		return isAssignable( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCoupon.assignable</code> attribute. 
	 * @return the assignable
	 */
	public boolean isAssignableAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isAssignable( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCoupon.assignable</code> attribute. 
	 * @return the assignable
	 */
	public boolean isAssignableAsPrimitive()
	{
		return isAssignableAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerCoupon.assignable</code> attribute. 
	 * @param value the assignable
	 */
	public void setAssignable(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ASSIGNABLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerCoupon.assignable</code> attribute. 
	 * @param value the assignable
	 */
	public void setAssignable(final Boolean value)
	{
		setAssignable( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerCoupon.assignable</code> attribute. 
	 * @param value the assignable
	 */
	public void setAssignable(final SessionContext ctx, final boolean value)
	{
		setAssignable( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerCoupon.assignable</code> attribute. 
	 * @param value the assignable
	 */
	public void setAssignable(final boolean value)
	{
		setAssignable( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCoupon.customers</code> attribute.
	 * @return the customers - Customers
	 */
	public Collection<Customer> getCustomers(final SessionContext ctx)
	{
		final List<Customer> items = getLinkedItems( 
			ctx,
			true,
			CustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER,
			"Customer",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCoupon.customers</code> attribute.
	 * @return the customers - Customers
	 */
	public Collection<Customer> getCustomers()
	{
		return getCustomers( getSession().getSessionContext() );
	}
	
	public long getCustomersCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			CustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER,
			"Customer",
			null
		);
	}
	
	public long getCustomersCount()
	{
		return getCustomersCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerCoupon.customers</code> attribute. 
	 * @param value the customers - Customers
	 */
	public void setCustomers(final SessionContext ctx, final Collection<Customer> value)
	{
		setLinkedItems( 
			ctx,
			true,
			CustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(CUSTOMERCOUPON2CUSTOMER_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerCoupon.customers</code> attribute. 
	 * @param value the customers - Customers
	 */
	public void setCustomers(final Collection<Customer> value)
	{
		setCustomers( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to customers. 
	 * @param value the item to add to customers - Customers
	 */
	public void addToCustomers(final SessionContext ctx, final Customer value)
	{
		addLinkedItems( 
			ctx,
			true,
			CustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(CUSTOMERCOUPON2CUSTOMER_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to customers. 
	 * @param value the item to add to customers - Customers
	 */
	public void addToCustomers(final Customer value)
	{
		addToCustomers( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from customers. 
	 * @param value the item to remove from customers - Customers
	 */
	public void removeFromCustomers(final SessionContext ctx, final Customer value)
	{
		removeLinkedItems( 
			ctx,
			true,
			CustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(CUSTOMERCOUPON2CUSTOMER_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from customers. 
	 * @param value the item to remove from customers - Customers
	 */
	public void removeFromCustomers(final Customer value)
	{
		removeFromCustomers( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCoupon.description</code> attribute.
	 * @return the description
	 */
	public String getDescription(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedCustomerCoupon.getDescription requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCoupon.description</code> attribute.
	 * @return the description
	 */
	public String getDescription()
	{
		return getDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCoupon.description</code> attribute. 
	 * @return the localized description
	 */
	public Map<Language,String> getAllDescription(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,DESCRIPTION,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCoupon.description</code> attribute. 
	 * @return the localized description
	 */
	public Map<Language,String> getAllDescription()
	{
		return getAllDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerCoupon.description</code> attribute. 
	 * @param value the description
	 */
	public void setDescription(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedCustomerCoupon.setDescription requires a session language", 0 );
		}
		setLocalizedProperty(ctx, DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerCoupon.description</code> attribute. 
	 * @param value the description
	 */
	public void setDescription(final String value)
	{
		setDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerCoupon.description</code> attribute. 
	 * @param value the description
	 */
	public void setAllDescription(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerCoupon.description</code> attribute. 
	 * @param value the description
	 */
	public void setAllDescription(final Map<Language,String> value)
	{
		setAllDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Customer");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(CUSTOMERCOUPON2CUSTOMER_MARKMODIFIED);
		}
		return true;
	}
	
}
