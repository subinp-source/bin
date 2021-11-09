/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout.jalo;

import de.hybris.platform.b2b.punchout.constants.B2bpunchoutConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.Cart;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem StoredPunchOutSession}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedStoredPunchOutSession extends GenericItem
{
	/** Qualifier of the <code>StoredPunchOutSession.sid</code> attribute **/
	public static final String SID = "sid";
	/** Qualifier of the <code>StoredPunchOutSession.punchOutSession</code> attribute **/
	public static final String PUNCHOUTSESSION = "punchOutSession";
	/** Qualifier of the <code>StoredPunchOutSession.cart</code> attribute **/
	public static final String CART = "cart";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(SID, AttributeMode.INITIAL);
		tmp.put(PUNCHOUTSESSION, AttributeMode.INITIAL);
		tmp.put(CART, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoredPunchOutSession.cart</code> attribute.
	 * @return the cart
	 */
	public Cart getCart(final SessionContext ctx)
	{
		return (Cart)getProperty( ctx, CART);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoredPunchOutSession.cart</code> attribute.
	 * @return the cart
	 */
	public Cart getCart()
	{
		return getCart( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoredPunchOutSession.cart</code> attribute. 
	 * @param value the cart
	 */
	protected void setCart(final SessionContext ctx, final Cart value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+CART+"' is not changeable", 0 );
		}
		setProperty(ctx, CART,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoredPunchOutSession.cart</code> attribute. 
	 * @param value the cart
	 */
	protected void setCart(final Cart value)
	{
		setCart( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoredPunchOutSession.punchOutSession</code> attribute.
	 * @return the punchOutSession - Should be instance of type PunchOutSession
	 */
	public Object getPunchOutSession(final SessionContext ctx)
	{
		return getProperty( ctx, PUNCHOUTSESSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoredPunchOutSession.punchOutSession</code> attribute.
	 * @return the punchOutSession - Should be instance of type PunchOutSession
	 */
	public Object getPunchOutSession()
	{
		return getPunchOutSession( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoredPunchOutSession.punchOutSession</code> attribute. 
	 * @param value the punchOutSession - Should be instance of type PunchOutSession
	 */
	public void setPunchOutSession(final SessionContext ctx, final Object value)
	{
		setProperty(ctx, PUNCHOUTSESSION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoredPunchOutSession.punchOutSession</code> attribute. 
	 * @param value the punchOutSession - Should be instance of type PunchOutSession
	 */
	public void setPunchOutSession(final Object value)
	{
		setPunchOutSession( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoredPunchOutSession.sid</code> attribute.
	 * @return the sid
	 */
	public String getSid(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoredPunchOutSession.sid</code> attribute.
	 * @return the sid
	 */
	public String getSid()
	{
		return getSid( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoredPunchOutSession.sid</code> attribute. 
	 * @param value the sid
	 */
	protected void setSid(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+SID+"' is not changeable", 0 );
		}
		setProperty(ctx, SID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoredPunchOutSession.sid</code> attribute. 
	 * @param value the sid
	 */
	protected void setSid(final String value)
	{
		setSid( getSession().getSessionContext(), value );
	}
	
}
