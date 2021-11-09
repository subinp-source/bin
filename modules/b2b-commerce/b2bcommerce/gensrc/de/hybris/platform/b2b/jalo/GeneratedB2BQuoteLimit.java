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
import de.hybris.platform.b2b.jalo.B2BUnit;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.util.OneToManyHandler;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.b2b.jalo.B2BQuoteLimit B2BQuoteLimit}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BQuoteLimit extends GenericItem
{
	/** Qualifier of the <code>B2BQuoteLimit.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>B2BQuoteLimit.amount</code> attribute **/
	public static final String AMOUNT = "amount";
	/** Qualifier of the <code>B2BQuoteLimit.currency</code> attribute **/
	public static final String CURRENCY = "currency";
	/** Qualifier of the <code>B2BQuoteLimit.Unit</code> attribute **/
	public static final String UNIT = "Unit";
	/**
	* {@link OneToManyHandler} for handling 1:n UNIT's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<B2BUnit> UNITHANDLER = new OneToManyHandler<B2BUnit>(
	B2BCommerceConstants.TC.B2BUNIT,
	false,
	"QuoteLimit",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(AMOUNT, AttributeMode.INITIAL);
		tmp.put(CURRENCY, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BQuoteLimit.amount</code> attribute.
	 * @return the amount
	 */
	public BigDecimal getAmount(final SessionContext ctx)
	{
		return (BigDecimal)getProperty( ctx, AMOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BQuoteLimit.amount</code> attribute.
	 * @return the amount
	 */
	public BigDecimal getAmount()
	{
		return getAmount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BQuoteLimit.amount</code> attribute. 
	 * @param value the amount
	 */
	public void setAmount(final SessionContext ctx, final BigDecimal value)
	{
		setProperty(ctx, AMOUNT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BQuoteLimit.amount</code> attribute. 
	 * @param value the amount
	 */
	public void setAmount(final BigDecimal value)
	{
		setAmount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BQuoteLimit.code</code> attribute.
	 * @return the code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BQuoteLimit.code</code> attribute.
	 * @return the code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BQuoteLimit.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BQuoteLimit.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BQuoteLimit.currency</code> attribute.
	 * @return the currency
	 */
	public Currency getCurrency(final SessionContext ctx)
	{
		return (Currency)getProperty( ctx, CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BQuoteLimit.currency</code> attribute.
	 * @return the currency
	 */
	public Currency getCurrency()
	{
		return getCurrency( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BQuoteLimit.currency</code> attribute. 
	 * @param value the currency
	 */
	public void setCurrency(final SessionContext ctx, final Currency value)
	{
		setProperty(ctx, CURRENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BQuoteLimit.currency</code> attribute. 
	 * @param value the currency
	 */
	public void setCurrency(final Currency value)
	{
		setCurrency( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BQuoteLimit.Unit</code> attribute.
	 * @return the Unit
	 */
	public Collection<B2BUnit> getUnit(final SessionContext ctx)
	{
		return UNITHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BQuoteLimit.Unit</code> attribute.
	 * @return the Unit
	 */
	public Collection<B2BUnit> getUnit()
	{
		return getUnit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BQuoteLimit.Unit</code> attribute. 
	 * @param value the Unit
	 */
	public void setUnit(final SessionContext ctx, final Collection<B2BUnit> value)
	{
		UNITHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BQuoteLimit.Unit</code> attribute. 
	 * @param value the Unit
	 */
	public void setUnit(final Collection<B2BUnit> value)
	{
		setUnit( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to Unit. 
	 * @param value the item to add to Unit
	 */
	public void addToUnit(final SessionContext ctx, final B2BUnit value)
	{
		UNITHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to Unit. 
	 * @param value the item to add to Unit
	 */
	public void addToUnit(final B2BUnit value)
	{
		addToUnit( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from Unit. 
	 * @param value the item to remove from Unit
	 */
	public void removeFromUnit(final SessionContext ctx, final B2BUnit value)
	{
		UNITHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from Unit. 
	 * @param value the item to remove from Unit
	 */
	public void removeFromUnit(final B2BUnit value)
	{
		removeFromUnit( getSession().getSessionContext(), value );
	}
	
}
