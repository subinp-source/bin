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
import de.hybris.platform.b2b.jalo.B2BMerchantCheck;
import de.hybris.platform.b2b.jalo.B2BUnit;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.StandardDateRange;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.b2b.jalo.B2BCreditLimit B2BCreditLimit}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BCreditLimit extends B2BMerchantCheck
{
	/** Qualifier of the <code>B2BCreditLimit.currency</code> attribute **/
	public static final String CURRENCY = "currency";
	/** Qualifier of the <code>B2BCreditLimit.amount</code> attribute **/
	public static final String AMOUNT = "amount";
	/** Qualifier of the <code>B2BCreditLimit.datePeriod</code> attribute **/
	public static final String DATEPERIOD = "datePeriod";
	/** Qualifier of the <code>B2BCreditLimit.dateRange</code> attribute **/
	public static final String DATERANGE = "dateRange";
	/** Qualifier of the <code>B2BCreditLimit.alertThreshold</code> attribute **/
	public static final String ALERTTHRESHOLD = "alertThreshold";
	/** Qualifier of the <code>B2BCreditLimit.alertRateType</code> attribute **/
	public static final String ALERTRATETYPE = "alertRateType";
	/** Qualifier of the <code>B2BCreditLimit.alertSentDate</code> attribute **/
	public static final String ALERTSENTDATE = "alertSentDate";
	/** Qualifier of the <code>B2BCreditLimit.Unit</code> attribute **/
	public static final String UNIT = "Unit";
	/**
	* {@link OneToManyHandler} for handling 1:n UNIT's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<B2BUnit> UNITHANDLER = new OneToManyHandler<B2BUnit>(
	B2BCommerceConstants.TC.B2BUNIT,
	false,
	"CreditLimit",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(B2BMerchantCheck.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(CURRENCY, AttributeMode.INITIAL);
		tmp.put(AMOUNT, AttributeMode.INITIAL);
		tmp.put(DATEPERIOD, AttributeMode.INITIAL);
		tmp.put(DATERANGE, AttributeMode.INITIAL);
		tmp.put(ALERTTHRESHOLD, AttributeMode.INITIAL);
		tmp.put(ALERTRATETYPE, AttributeMode.INITIAL);
		tmp.put(ALERTSENTDATE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditLimit.alertRateType</code> attribute.
	 * @return the alertRateType
	 */
	public EnumerationValue getAlertRateType(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, ALERTRATETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditLimit.alertRateType</code> attribute.
	 * @return the alertRateType
	 */
	public EnumerationValue getAlertRateType()
	{
		return getAlertRateType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditLimit.alertRateType</code> attribute. 
	 * @param value the alertRateType
	 */
	public void setAlertRateType(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, ALERTRATETYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditLimit.alertRateType</code> attribute. 
	 * @param value the alertRateType
	 */
	public void setAlertRateType(final EnumerationValue value)
	{
		setAlertRateType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditLimit.alertSentDate</code> attribute.
	 * @return the alertSentDate
	 */
	public Date getAlertSentDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, ALERTSENTDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditLimit.alertSentDate</code> attribute.
	 * @return the alertSentDate
	 */
	public Date getAlertSentDate()
	{
		return getAlertSentDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditLimit.alertSentDate</code> attribute. 
	 * @param value the alertSentDate
	 */
	public void setAlertSentDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, ALERTSENTDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditLimit.alertSentDate</code> attribute. 
	 * @param value the alertSentDate
	 */
	public void setAlertSentDate(final Date value)
	{
		setAlertSentDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditLimit.alertThreshold</code> attribute.
	 * @return the alertThreshold
	 */
	public BigDecimal getAlertThreshold(final SessionContext ctx)
	{
		return (BigDecimal)getProperty( ctx, ALERTTHRESHOLD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditLimit.alertThreshold</code> attribute.
	 * @return the alertThreshold
	 */
	public BigDecimal getAlertThreshold()
	{
		return getAlertThreshold( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditLimit.alertThreshold</code> attribute. 
	 * @param value the alertThreshold
	 */
	public void setAlertThreshold(final SessionContext ctx, final BigDecimal value)
	{
		setProperty(ctx, ALERTTHRESHOLD,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditLimit.alertThreshold</code> attribute. 
	 * @param value the alertThreshold
	 */
	public void setAlertThreshold(final BigDecimal value)
	{
		setAlertThreshold( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditLimit.amount</code> attribute.
	 * @return the amount
	 */
	public BigDecimal getAmount(final SessionContext ctx)
	{
		return (BigDecimal)getProperty( ctx, AMOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditLimit.amount</code> attribute.
	 * @return the amount
	 */
	public BigDecimal getAmount()
	{
		return getAmount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditLimit.amount</code> attribute. 
	 * @param value the amount
	 */
	public void setAmount(final SessionContext ctx, final BigDecimal value)
	{
		setProperty(ctx, AMOUNT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditLimit.amount</code> attribute. 
	 * @param value the amount
	 */
	public void setAmount(final BigDecimal value)
	{
		setAmount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditLimit.currency</code> attribute.
	 * @return the currency
	 */
	public Currency getCurrency(final SessionContext ctx)
	{
		return (Currency)getProperty( ctx, CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditLimit.currency</code> attribute.
	 * @return the currency
	 */
	public Currency getCurrency()
	{
		return getCurrency( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditLimit.currency</code> attribute. 
	 * @param value the currency
	 */
	public void setCurrency(final SessionContext ctx, final Currency value)
	{
		setProperty(ctx, CURRENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditLimit.currency</code> attribute. 
	 * @param value the currency
	 */
	public void setCurrency(final Currency value)
	{
		setCurrency( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditLimit.datePeriod</code> attribute.
	 * @return the datePeriod - date range the credit limit is active
	 */
	public StandardDateRange getDatePeriod(final SessionContext ctx)
	{
		return (StandardDateRange)getProperty( ctx, DATEPERIOD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditLimit.datePeriod</code> attribute.
	 * @return the datePeriod - date range the credit limit is active
	 */
	public StandardDateRange getDatePeriod()
	{
		return getDatePeriod( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditLimit.datePeriod</code> attribute. 
	 * @param value the datePeriod - date range the credit limit is active
	 */
	public void setDatePeriod(final SessionContext ctx, final StandardDateRange value)
	{
		setProperty(ctx, DATEPERIOD,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditLimit.datePeriod</code> attribute. 
	 * @param value the datePeriod - date range the credit limit is active
	 */
	public void setDatePeriod(final StandardDateRange value)
	{
		setDatePeriod( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditLimit.dateRange</code> attribute.
	 * @return the dateRange
	 */
	public EnumerationValue getDateRange(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, DATERANGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditLimit.dateRange</code> attribute.
	 * @return the dateRange
	 */
	public EnumerationValue getDateRange()
	{
		return getDateRange( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditLimit.dateRange</code> attribute. 
	 * @param value the dateRange
	 */
	public void setDateRange(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, DATERANGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditLimit.dateRange</code> attribute. 
	 * @param value the dateRange
	 */
	public void setDateRange(final EnumerationValue value)
	{
		setDateRange( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditLimit.Unit</code> attribute.
	 * @return the Unit
	 */
	public Collection<B2BUnit> getUnit(final SessionContext ctx)
	{
		return UNITHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditLimit.Unit</code> attribute.
	 * @return the Unit
	 */
	public Collection<B2BUnit> getUnit()
	{
		return getUnit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditLimit.Unit</code> attribute. 
	 * @param value the Unit
	 */
	public void setUnit(final SessionContext ctx, final Collection<B2BUnit> value)
	{
		UNITHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditLimit.Unit</code> attribute. 
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
