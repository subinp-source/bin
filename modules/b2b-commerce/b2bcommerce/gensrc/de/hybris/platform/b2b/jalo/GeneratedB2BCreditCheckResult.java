/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.jalo;

import de.hybris.platform.b2b.constants.B2BCommerceConstants;
import de.hybris.platform.b2b.jalo.B2BMerchantCheckResult;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.b2b.jalo.B2BCreditCheckResult B2BCreditCheckResult}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BCreditCheckResult extends B2BMerchantCheckResult
{
	/** Qualifier of the <code>B2BCreditCheckResult.currency</code> attribute **/
	public static final String CURRENCY = "currency";
	/** Qualifier of the <code>B2BCreditCheckResult.creditLimit</code> attribute **/
	public static final String CREDITLIMIT = "creditLimit";
	/** Qualifier of the <code>B2BCreditCheckResult.amountUtilised</code> attribute **/
	public static final String AMOUNTUTILISED = "amountUtilised";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(B2BMerchantCheckResult.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(CURRENCY, AttributeMode.INITIAL);
		tmp.put(CREDITLIMIT, AttributeMode.INITIAL);
		tmp.put(AMOUNTUTILISED, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditCheckResult.amountUtilised</code> attribute.
	 * @return the amountUtilised
	 */
	public BigDecimal getAmountUtilised(final SessionContext ctx)
	{
		return (BigDecimal)getProperty( ctx, AMOUNTUTILISED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditCheckResult.amountUtilised</code> attribute.
	 * @return the amountUtilised
	 */
	public BigDecimal getAmountUtilised()
	{
		return getAmountUtilised( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditCheckResult.amountUtilised</code> attribute. 
	 * @param value the amountUtilised
	 */
	public void setAmountUtilised(final SessionContext ctx, final BigDecimal value)
	{
		setProperty(ctx, AMOUNTUTILISED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditCheckResult.amountUtilised</code> attribute. 
	 * @param value the amountUtilised
	 */
	public void setAmountUtilised(final BigDecimal value)
	{
		setAmountUtilised( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditCheckResult.creditLimit</code> attribute.
	 * @return the creditLimit
	 */
	public BigDecimal getCreditLimit(final SessionContext ctx)
	{
		return (BigDecimal)getProperty( ctx, CREDITLIMIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditCheckResult.creditLimit</code> attribute.
	 * @return the creditLimit
	 */
	public BigDecimal getCreditLimit()
	{
		return getCreditLimit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditCheckResult.creditLimit</code> attribute. 
	 * @param value the creditLimit
	 */
	public void setCreditLimit(final SessionContext ctx, final BigDecimal value)
	{
		setProperty(ctx, CREDITLIMIT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditCheckResult.creditLimit</code> attribute. 
	 * @param value the creditLimit
	 */
	public void setCreditLimit(final BigDecimal value)
	{
		setCreditLimit( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditCheckResult.currency</code> attribute.
	 * @return the currency
	 */
	public Currency getCurrency(final SessionContext ctx)
	{
		return (Currency)getProperty( ctx, CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditCheckResult.currency</code> attribute.
	 * @return the currency
	 */
	public Currency getCurrency()
	{
		return getCurrency( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditCheckResult.currency</code> attribute. 
	 * @param value the currency
	 */
	public void setCurrency(final SessionContext ctx, final Currency value)
	{
		setProperty(ctx, CURRENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditCheckResult.currency</code> attribute. 
	 * @param value the currency
	 */
	public void setCurrency(final Currency value)
	{
		setCurrency( getSession().getSessionContext(), value );
	}
	
}
