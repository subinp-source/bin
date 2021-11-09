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
import de.hybris.platform.b2b.jalo.B2BCostCenter;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.OrderEntry;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.b2b.jalo.B2BBookingLineEntry B2BBookingLineEntry}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BBookingLineEntry extends GenericItem
{
	/** Qualifier of the <code>B2BBookingLineEntry.bookingStatus</code> attribute **/
	public static final String BOOKINGSTATUS = "bookingStatus";
	/** Qualifier of the <code>B2BBookingLineEntry.costCenter</code> attribute **/
	public static final String COSTCENTER = "costCenter";
	/** Qualifier of the <code>B2BBookingLineEntry.amount</code> attribute **/
	public static final String AMOUNT = "amount";
	/** Qualifier of the <code>B2BBookingLineEntry.currency</code> attribute **/
	public static final String CURRENCY = "currency";
	/** Qualifier of the <code>B2BBookingLineEntry.product</code> attribute **/
	public static final String PRODUCT = "product";
	/** Qualifier of the <code>B2BBookingLineEntry.quantity</code> attribute **/
	public static final String QUANTITY = "quantity";
	/** Qualifier of the <code>B2BBookingLineEntry.orderID</code> attribute **/
	public static final String ORDERID = "orderID";
	/** Qualifier of the <code>B2BBookingLineEntry.orderEntryNr</code> attribute **/
	public static final String ORDERENTRYNR = "orderEntryNr";
	/** Qualifier of the <code>B2BBookingLineEntry.orderEntry</code> attribute **/
	public static final String ORDERENTRY = "orderEntry";
	/** Qualifier of the <code>B2BBookingLineEntry.bookingDate</code> attribute **/
	public static final String BOOKINGDATE = "bookingDate";
	/** Qualifier of the <code>B2BBookingLineEntry.bookingType</code> attribute **/
	public static final String BOOKINGTYPE = "bookingType";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(BOOKINGSTATUS, AttributeMode.INITIAL);
		tmp.put(COSTCENTER, AttributeMode.INITIAL);
		tmp.put(AMOUNT, AttributeMode.INITIAL);
		tmp.put(CURRENCY, AttributeMode.INITIAL);
		tmp.put(PRODUCT, AttributeMode.INITIAL);
		tmp.put(QUANTITY, AttributeMode.INITIAL);
		tmp.put(ORDERID, AttributeMode.INITIAL);
		tmp.put(ORDERENTRYNR, AttributeMode.INITIAL);
		tmp.put(ORDERENTRY, AttributeMode.INITIAL);
		tmp.put(BOOKINGDATE, AttributeMode.INITIAL);
		tmp.put(BOOKINGTYPE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.amount</code> attribute.
	 * @return the amount
	 */
	public BigDecimal getAmount(final SessionContext ctx)
	{
		return (BigDecimal)getProperty( ctx, AMOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.amount</code> attribute.
	 * @return the amount
	 */
	public BigDecimal getAmount()
	{
		return getAmount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.amount</code> attribute. 
	 * @param value the amount
	 */
	public void setAmount(final SessionContext ctx, final BigDecimal value)
	{
		setProperty(ctx, AMOUNT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.amount</code> attribute. 
	 * @param value the amount
	 */
	public void setAmount(final BigDecimal value)
	{
		setAmount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.bookingDate</code> attribute.
	 * @return the bookingDate
	 */
	public Date getBookingDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, BOOKINGDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.bookingDate</code> attribute.
	 * @return the bookingDate
	 */
	public Date getBookingDate()
	{
		return getBookingDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.bookingDate</code> attribute. 
	 * @param value the bookingDate
	 */
	public void setBookingDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, BOOKINGDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.bookingDate</code> attribute. 
	 * @param value the bookingDate
	 */
	public void setBookingDate(final Date value)
	{
		setBookingDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.bookingStatus</code> attribute.
	 * @return the bookingStatus
	 */
	public EnumerationValue getBookingStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, BOOKINGSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.bookingStatus</code> attribute.
	 * @return the bookingStatus
	 */
	public EnumerationValue getBookingStatus()
	{
		return getBookingStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.bookingStatus</code> attribute. 
	 * @param value the bookingStatus
	 */
	public void setBookingStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, BOOKINGSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.bookingStatus</code> attribute. 
	 * @param value the bookingStatus
	 */
	public void setBookingStatus(final EnumerationValue value)
	{
		setBookingStatus( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.bookingType</code> attribute.
	 * @return the bookingType
	 */
	public EnumerationValue getBookingType(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, BOOKINGTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.bookingType</code> attribute.
	 * @return the bookingType
	 */
	public EnumerationValue getBookingType()
	{
		return getBookingType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.bookingType</code> attribute. 
	 * @param value the bookingType
	 */
	public void setBookingType(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, BOOKINGTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.bookingType</code> attribute. 
	 * @param value the bookingType
	 */
	public void setBookingType(final EnumerationValue value)
	{
		setBookingType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.costCenter</code> attribute.
	 * @return the costCenter
	 */
	public B2BCostCenter getCostCenter(final SessionContext ctx)
	{
		return (B2BCostCenter)getProperty( ctx, COSTCENTER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.costCenter</code> attribute.
	 * @return the costCenter
	 */
	public B2BCostCenter getCostCenter()
	{
		return getCostCenter( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.costCenter</code> attribute. 
	 * @param value the costCenter
	 */
	public void setCostCenter(final SessionContext ctx, final B2BCostCenter value)
	{
		setProperty(ctx, COSTCENTER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.costCenter</code> attribute. 
	 * @param value the costCenter
	 */
	public void setCostCenter(final B2BCostCenter value)
	{
		setCostCenter( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.currency</code> attribute.
	 * @return the currency
	 */
	public Currency getCurrency(final SessionContext ctx)
	{
		return (Currency)getProperty( ctx, CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.currency</code> attribute.
	 * @return the currency
	 */
	public Currency getCurrency()
	{
		return getCurrency( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.currency</code> attribute. 
	 * @param value the currency
	 */
	public void setCurrency(final SessionContext ctx, final Currency value)
	{
		setProperty(ctx, CURRENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.currency</code> attribute. 
	 * @param value the currency
	 */
	public void setCurrency(final Currency value)
	{
		setCurrency( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.orderEntry</code> attribute.
	 * @return the orderEntry
	 */
	public OrderEntry getOrderEntry(final SessionContext ctx)
	{
		return (OrderEntry)getProperty( ctx, ORDERENTRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.orderEntry</code> attribute.
	 * @return the orderEntry
	 */
	public OrderEntry getOrderEntry()
	{
		return getOrderEntry( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.orderEntry</code> attribute. 
	 * @param value the orderEntry
	 */
	public void setOrderEntry(final SessionContext ctx, final OrderEntry value)
	{
		setProperty(ctx, ORDERENTRY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.orderEntry</code> attribute. 
	 * @param value the orderEntry
	 */
	public void setOrderEntry(final OrderEntry value)
	{
		setOrderEntry( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.orderEntryNr</code> attribute.
	 * @return the orderEntryNr
	 */
	public Integer getOrderEntryNr(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, ORDERENTRYNR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.orderEntryNr</code> attribute.
	 * @return the orderEntryNr
	 */
	public Integer getOrderEntryNr()
	{
		return getOrderEntryNr( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.orderEntryNr</code> attribute. 
	 * @return the orderEntryNr
	 */
	public int getOrderEntryNrAsPrimitive(final SessionContext ctx)
	{
		Integer value = getOrderEntryNr( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.orderEntryNr</code> attribute. 
	 * @return the orderEntryNr
	 */
	public int getOrderEntryNrAsPrimitive()
	{
		return getOrderEntryNrAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.orderEntryNr</code> attribute. 
	 * @param value the orderEntryNr
	 */
	public void setOrderEntryNr(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, ORDERENTRYNR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.orderEntryNr</code> attribute. 
	 * @param value the orderEntryNr
	 */
	public void setOrderEntryNr(final Integer value)
	{
		setOrderEntryNr( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.orderEntryNr</code> attribute. 
	 * @param value the orderEntryNr
	 */
	public void setOrderEntryNr(final SessionContext ctx, final int value)
	{
		setOrderEntryNr( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.orderEntryNr</code> attribute. 
	 * @param value the orderEntryNr
	 */
	public void setOrderEntryNr(final int value)
	{
		setOrderEntryNr( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.orderID</code> attribute.
	 * @return the orderID
	 */
	public String getOrderID(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ORDERID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.orderID</code> attribute.
	 * @return the orderID
	 */
	public String getOrderID()
	{
		return getOrderID( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.orderID</code> attribute. 
	 * @param value the orderID
	 */
	public void setOrderID(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ORDERID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.orderID</code> attribute. 
	 * @param value the orderID
	 */
	public void setOrderID(final String value)
	{
		setOrderID( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.product</code> attribute.
	 * @return the product
	 */
	public String getProduct(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.product</code> attribute.
	 * @return the product
	 */
	public String getProduct()
	{
		return getProduct( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.product</code> attribute. 
	 * @param value the product
	 */
	public void setProduct(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PRODUCT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.product</code> attribute. 
	 * @param value the product
	 */
	public void setProduct(final String value)
	{
		setProduct( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.quantity</code> attribute.
	 * @return the quantity
	 */
	public Long getQuantity(final SessionContext ctx)
	{
		return (Long)getProperty( ctx, QUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.quantity</code> attribute.
	 * @return the quantity
	 */
	public Long getQuantity()
	{
		return getQuantity( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.quantity</code> attribute. 
	 * @return the quantity
	 */
	public long getQuantityAsPrimitive(final SessionContext ctx)
	{
		Long value = getQuantity( ctx );
		return value != null ? value.longValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBookingLineEntry.quantity</code> attribute. 
	 * @return the quantity
	 */
	public long getQuantityAsPrimitive()
	{
		return getQuantityAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.quantity</code> attribute. 
	 * @param value the quantity
	 */
	public void setQuantity(final SessionContext ctx, final Long value)
	{
		setProperty(ctx, QUANTITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.quantity</code> attribute. 
	 * @param value the quantity
	 */
	public void setQuantity(final Long value)
	{
		setQuantity( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.quantity</code> attribute. 
	 * @param value the quantity
	 */
	public void setQuantity(final SessionContext ctx, final long value)
	{
		setQuantity( ctx,Long.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBookingLineEntry.quantity</code> attribute. 
	 * @param value the quantity
	 */
	public void setQuantity(final long value)
	{
		setQuantity( getSession().getSessionContext(), value );
	}
	
}
