/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.subscriptionservices.constants.SubscriptionservicesConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.subscriptionservices.jalo.BillingTime BillingTime}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedBillingTime extends GenericItem
{
	/** Qualifier of the <code>BillingTime.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>BillingTime.nameInCart</code> attribute **/
	public static final String NAMEINCART = "nameInCart";
	/** Qualifier of the <code>BillingTime.nameInOrder</code> attribute **/
	public static final String NAMEINORDER = "nameInOrder";
	/** Qualifier of the <code>BillingTime.description</code> attribute **/
	public static final String DESCRIPTION = "description";
	/** Qualifier of the <code>BillingTime.order</code> attribute **/
	public static final String ORDER = "order";
	/** Qualifier of the <code>BillingTime.cartAware</code> attribute **/
	public static final String CARTAWARE = "cartAware";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(NAMEINCART, AttributeMode.INITIAL);
		tmp.put(NAMEINORDER, AttributeMode.INITIAL);
		tmp.put(DESCRIPTION, AttributeMode.INITIAL);
		tmp.put(ORDER, AttributeMode.INITIAL);
		tmp.put(CARTAWARE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.cartAware</code> attribute.
	 * @return the cartAware - Cart Aware
	 */
	public Boolean isCartAware(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, CARTAWARE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.cartAware</code> attribute.
	 * @return the cartAware - Cart Aware
	 */
	public Boolean isCartAware()
	{
		return isCartAware( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.cartAware</code> attribute. 
	 * @return the cartAware - Cart Aware
	 */
	public boolean isCartAwareAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isCartAware( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.cartAware</code> attribute. 
	 * @return the cartAware - Cart Aware
	 */
	public boolean isCartAwareAsPrimitive()
	{
		return isCartAwareAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.cartAware</code> attribute. 
	 * @param value the cartAware - Cart Aware
	 */
	public void setCartAware(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, CARTAWARE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.cartAware</code> attribute. 
	 * @param value the cartAware - Cart Aware
	 */
	public void setCartAware(final Boolean value)
	{
		setCartAware( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.cartAware</code> attribute. 
	 * @param value the cartAware - Cart Aware
	 */
	public void setCartAware(final SessionContext ctx, final boolean value)
	{
		setCartAware( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.cartAware</code> attribute. 
	 * @param value the cartAware - Cart Aware
	 */
	public void setCartAware(final boolean value)
	{
		setCartAware( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.code</code> attribute.
	 * @return the code - Code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.code</code> attribute.
	 * @return the code - Code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.code</code> attribute. 
	 * @param value the code - Code
	 */
	protected void setCode(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+CODE+"' is not changeable", 0 );
		}
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.code</code> attribute. 
	 * @param value the code - Code
	 */
	protected void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.description</code> attribute.
	 * @return the description - Description
	 */
	public String getDescription(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedBillingTime.getDescription requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.description</code> attribute.
	 * @return the description - Description
	 */
	public String getDescription()
	{
		return getDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.description</code> attribute. 
	 * @return the localized description - Description
	 */
	public Map<Language,String> getAllDescription(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,DESCRIPTION,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.description</code> attribute. 
	 * @return the localized description - Description
	 */
	public Map<Language,String> getAllDescription()
	{
		return getAllDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.description</code> attribute. 
	 * @param value the description - Description
	 */
	public void setDescription(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedBillingTime.setDescription requires a session language", 0 );
		}
		setLocalizedProperty(ctx, DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.description</code> attribute. 
	 * @param value the description - Description
	 */
	public void setDescription(final String value)
	{
		setDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.description</code> attribute. 
	 * @param value the description - Description
	 */
	public void setAllDescription(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.description</code> attribute. 
	 * @param value the description - Description
	 */
	public void setAllDescription(final Map<Language,String> value)
	{
		setAllDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.nameInCart</code> attribute.
	 * @return the nameInCart - Name of frequency in cart
	 */
	public String getNameInCart(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedBillingTime.getNameInCart requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAMEINCART);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.nameInCart</code> attribute.
	 * @return the nameInCart - Name of frequency in cart
	 */
	public String getNameInCart()
	{
		return getNameInCart( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.nameInCart</code> attribute. 
	 * @return the localized nameInCart - Name of frequency in cart
	 */
	public Map<Language,String> getAllNameInCart(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAMEINCART,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.nameInCart</code> attribute. 
	 * @return the localized nameInCart - Name of frequency in cart
	 */
	public Map<Language,String> getAllNameInCart()
	{
		return getAllNameInCart( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.nameInCart</code> attribute. 
	 * @param value the nameInCart - Name of frequency in cart
	 */
	public void setNameInCart(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedBillingTime.setNameInCart requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAMEINCART,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.nameInCart</code> attribute. 
	 * @param value the nameInCart - Name of frequency in cart
	 */
	public void setNameInCart(final String value)
	{
		setNameInCart( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.nameInCart</code> attribute. 
	 * @param value the nameInCart - Name of frequency in cart
	 */
	public void setAllNameInCart(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAMEINCART,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.nameInCart</code> attribute. 
	 * @param value the nameInCart - Name of frequency in cart
	 */
	public void setAllNameInCart(final Map<Language,String> value)
	{
		setAllNameInCart( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.nameInOrder</code> attribute.
	 * @return the nameInOrder - Name of frequency in order
	 */
	public String getNameInOrder(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedBillingTime.getNameInOrder requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAMEINORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.nameInOrder</code> attribute.
	 * @return the nameInOrder - Name of frequency in order
	 */
	public String getNameInOrder()
	{
		return getNameInOrder( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.nameInOrder</code> attribute. 
	 * @return the localized nameInOrder - Name of frequency in order
	 */
	public Map<Language,String> getAllNameInOrder(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAMEINORDER,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.nameInOrder</code> attribute. 
	 * @return the localized nameInOrder - Name of frequency in order
	 */
	public Map<Language,String> getAllNameInOrder()
	{
		return getAllNameInOrder( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.nameInOrder</code> attribute. 
	 * @param value the nameInOrder - Name of frequency in order
	 */
	public void setNameInOrder(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedBillingTime.setNameInOrder requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAMEINORDER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.nameInOrder</code> attribute. 
	 * @param value the nameInOrder - Name of frequency in order
	 */
	public void setNameInOrder(final String value)
	{
		setNameInOrder( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.nameInOrder</code> attribute. 
	 * @param value the nameInOrder - Name of frequency in order
	 */
	public void setAllNameInOrder(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAMEINORDER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.nameInOrder</code> attribute. 
	 * @param value the nameInOrder - Name of frequency in order
	 */
	public void setAllNameInOrder(final Map<Language,String> value)
	{
		setAllNameInOrder( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.order</code> attribute.
	 * @return the order - Sequence
	 */
	public Integer getOrder(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, ORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.order</code> attribute.
	 * @return the order - Sequence
	 */
	public Integer getOrder()
	{
		return getOrder( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.order</code> attribute. 
	 * @return the order - Sequence
	 */
	public int getOrderAsPrimitive(final SessionContext ctx)
	{
		Integer value = getOrder( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingTime.order</code> attribute. 
	 * @return the order - Sequence
	 */
	public int getOrderAsPrimitive()
	{
		return getOrderAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.order</code> attribute. 
	 * @param value the order - Sequence
	 */
	public void setOrder(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, ORDER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.order</code> attribute. 
	 * @param value the order - Sequence
	 */
	public void setOrder(final Integer value)
	{
		setOrder( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.order</code> attribute. 
	 * @param value the order - Sequence
	 */
	public void setOrder(final SessionContext ctx, final int value)
	{
		setOrder( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingTime.order</code> attribute. 
	 * @param value the order - Sequence
	 */
	public void setOrder(final int value)
	{
		setOrder( getSession().getSessionContext(), value );
	}
	
}
