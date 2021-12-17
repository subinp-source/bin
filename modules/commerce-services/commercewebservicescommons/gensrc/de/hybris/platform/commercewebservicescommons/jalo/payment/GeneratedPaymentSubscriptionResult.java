/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.jalo.payment;

import de.hybris.platform.commercewebservicescommons.constants.CommercewebservicescommonsConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commercewebservicescommons.jalo.payment.PaymentSubscriptionResult PaymentSubscriptionResult}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedPaymentSubscriptionResult extends GenericItem
{
	/** Qualifier of the <code>PaymentSubscriptionResult.cartId</code> attribute **/
	public static final String CARTID = "cartId";
	/** Qualifier of the <code>PaymentSubscriptionResult.success</code> attribute **/
	public static final String SUCCESS = "success";
	/** Qualifier of the <code>PaymentSubscriptionResult.result</code> attribute **/
	public static final String RESULT = "result";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CARTID, AttributeMode.INITIAL);
		tmp.put(SUCCESS, AttributeMode.INITIAL);
		tmp.put(RESULT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentSubscriptionResult.cartId</code> attribute.
	 * @return the cartId
	 */
	public String getCartId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CARTID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentSubscriptionResult.cartId</code> attribute.
	 * @return the cartId
	 */
	public String getCartId()
	{
		return getCartId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentSubscriptionResult.cartId</code> attribute. 
	 * @param value the cartId
	 */
	public void setCartId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CARTID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentSubscriptionResult.cartId</code> attribute. 
	 * @param value the cartId
	 */
	public void setCartId(final String value)
	{
		setCartId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentSubscriptionResult.result</code> attribute.
	 * @return the result - Serialized result object
	 */
	public Object getResult(final SessionContext ctx)
	{
		return getProperty( ctx, RESULT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentSubscriptionResult.result</code> attribute.
	 * @return the result - Serialized result object
	 */
	public Object getResult()
	{
		return getResult( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentSubscriptionResult.result</code> attribute. 
	 * @param value the result - Serialized result object
	 */
	public void setResult(final SessionContext ctx, final Object value)
	{
		setProperty(ctx, RESULT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentSubscriptionResult.result</code> attribute. 
	 * @param value the result - Serialized result object
	 */
	public void setResult(final Object value)
	{
		setResult( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentSubscriptionResult.success</code> attribute.
	 * @return the success - Define if subscription was successful.
	 */
	public Boolean isSuccess(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, SUCCESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentSubscriptionResult.success</code> attribute.
	 * @return the success - Define if subscription was successful.
	 */
	public Boolean isSuccess()
	{
		return isSuccess( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentSubscriptionResult.success</code> attribute. 
	 * @return the success - Define if subscription was successful.
	 */
	public boolean isSuccessAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isSuccess( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentSubscriptionResult.success</code> attribute. 
	 * @return the success - Define if subscription was successful.
	 */
	public boolean isSuccessAsPrimitive()
	{
		return isSuccessAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentSubscriptionResult.success</code> attribute. 
	 * @param value the success - Define if subscription was successful.
	 */
	public void setSuccess(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, SUCCESS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentSubscriptionResult.success</code> attribute. 
	 * @param value the success - Define if subscription was successful.
	 */
	public void setSuccess(final Boolean value)
	{
		setSuccess( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentSubscriptionResult.success</code> attribute. 
	 * @param value the success - Define if subscription was successful.
	 */
	public void setSuccess(final SessionContext ctx, final boolean value)
	{
		setSuccess( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentSubscriptionResult.success</code> attribute. 
	 * @param value the success - Define if subscription was successful.
	 */
	public void setSuccess(final boolean value)
	{
		setSuccess( getSession().getSessionContext(), value );
	}
	
}
