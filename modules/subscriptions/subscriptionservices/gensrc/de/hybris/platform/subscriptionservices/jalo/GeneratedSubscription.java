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
import de.hybris.platform.subscriptionservices.constants.SubscriptionservicesConstants;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.subscriptionservices.jalo.Subscription Subscription}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSubscription extends GenericItem
{
	/** Qualifier of the <code>Subscription.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>Subscription.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>Subscription.description</code> attribute **/
	public static final String DESCRIPTION = "description";
	/** Qualifier of the <code>Subscription.productCode</code> attribute **/
	public static final String PRODUCTCODE = "productCode";
	/** Qualifier of the <code>Subscription.orderNumber</code> attribute **/
	public static final String ORDERNUMBER = "orderNumber";
	/** Qualifier of the <code>Subscription.orderEntryNumber</code> attribute **/
	public static final String ORDERENTRYNUMBER = "orderEntryNumber";
	/** Qualifier of the <code>Subscription.billingsystemId</code> attribute **/
	public static final String BILLINGSYSTEMID = "billingsystemId";
	/** Qualifier of the <code>Subscription.startDate</code> attribute **/
	public static final String STARTDATE = "startDate";
	/** Qualifier of the <code>Subscription.endDate</code> attribute **/
	public static final String ENDDATE = "endDate";
	/** Qualifier of the <code>Subscription.renewalType</code> attribute **/
	public static final String RENEWALTYPE = "renewalType";
	/** Qualifier of the <code>Subscription.subscriptionStatus</code> attribute **/
	public static final String SUBSCRIPTIONSTATUS = "subscriptionStatus";
	/** Qualifier of the <code>Subscription.cancellable</code> attribute **/
	public static final String CANCELLABLE = "cancellable";
	/** Qualifier of the <code>Subscription.billingFrequency</code> attribute **/
	public static final String BILLINGFREQUENCY = "billingFrequency";
	/** Qualifier of the <code>Subscription.contractDuration</code> attribute **/
	public static final String CONTRACTDURATION = "contractDuration";
	/** Qualifier of the <code>Subscription.contractFrequency</code> attribute **/
	public static final String CONTRACTFREQUENCY = "contractFrequency";
	/** Qualifier of the <code>Subscription.placedOn</code> attribute **/
	public static final String PLACEDON = "placedOn";
	/** Qualifier of the <code>Subscription.cancelledDate</code> attribute **/
	public static final String CANCELLEDDATE = "cancelledDate";
	/** Qualifier of the <code>Subscription.customerId</code> attribute **/
	public static final String CUSTOMERID = "customerId";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ID, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(DESCRIPTION, AttributeMode.INITIAL);
		tmp.put(PRODUCTCODE, AttributeMode.INITIAL);
		tmp.put(ORDERNUMBER, AttributeMode.INITIAL);
		tmp.put(ORDERENTRYNUMBER, AttributeMode.INITIAL);
		tmp.put(BILLINGSYSTEMID, AttributeMode.INITIAL);
		tmp.put(STARTDATE, AttributeMode.INITIAL);
		tmp.put(ENDDATE, AttributeMode.INITIAL);
		tmp.put(RENEWALTYPE, AttributeMode.INITIAL);
		tmp.put(SUBSCRIPTIONSTATUS, AttributeMode.INITIAL);
		tmp.put(CANCELLABLE, AttributeMode.INITIAL);
		tmp.put(BILLINGFREQUENCY, AttributeMode.INITIAL);
		tmp.put(CONTRACTDURATION, AttributeMode.INITIAL);
		tmp.put(CONTRACTFREQUENCY, AttributeMode.INITIAL);
		tmp.put(PLACEDON, AttributeMode.INITIAL);
		tmp.put(CANCELLEDDATE, AttributeMode.INITIAL);
		tmp.put(CUSTOMERID, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.billingFrequency</code> attribute.
	 * @return the billingFrequency - Billing Frequency
	 */
	public String getBillingFrequency(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BILLINGFREQUENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.billingFrequency</code> attribute.
	 * @return the billingFrequency - Billing Frequency
	 */
	public String getBillingFrequency()
	{
		return getBillingFrequency( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.billingFrequency</code> attribute. 
	 * @param value the billingFrequency - Billing Frequency
	 */
	public void setBillingFrequency(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BILLINGFREQUENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.billingFrequency</code> attribute. 
	 * @param value the billingFrequency - Billing Frequency
	 */
	public void setBillingFrequency(final String value)
	{
		setBillingFrequency( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.billingsystemId</code> attribute.
	 * @return the billingsystemId - Billingsystem Identifier
	 */
	public String getBillingsystemId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BILLINGSYSTEMID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.billingsystemId</code> attribute.
	 * @return the billingsystemId - Billingsystem Identifier
	 */
	public String getBillingsystemId()
	{
		return getBillingsystemId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.billingsystemId</code> attribute. 
	 * @param value the billingsystemId - Billingsystem Identifier
	 */
	public void setBillingsystemId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BILLINGSYSTEMID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.billingsystemId</code> attribute. 
	 * @param value the billingsystemId - Billingsystem Identifier
	 */
	public void setBillingsystemId(final String value)
	{
		setBillingsystemId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.cancellable</code> attribute.
	 * @return the cancellable - Cancellable
	 */
	public Boolean isCancellable(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, CANCELLABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.cancellable</code> attribute.
	 * @return the cancellable - Cancellable
	 */
	public Boolean isCancellable()
	{
		return isCancellable( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.cancellable</code> attribute. 
	 * @return the cancellable - Cancellable
	 */
	public boolean isCancellableAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isCancellable( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.cancellable</code> attribute. 
	 * @return the cancellable - Cancellable
	 */
	public boolean isCancellableAsPrimitive()
	{
		return isCancellableAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.cancellable</code> attribute. 
	 * @param value the cancellable - Cancellable
	 */
	public void setCancellable(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, CANCELLABLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.cancellable</code> attribute. 
	 * @param value the cancellable - Cancellable
	 */
	public void setCancellable(final Boolean value)
	{
		setCancellable( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.cancellable</code> attribute. 
	 * @param value the cancellable - Cancellable
	 */
	public void setCancellable(final SessionContext ctx, final boolean value)
	{
		setCancellable( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.cancellable</code> attribute. 
	 * @param value the cancellable - Cancellable
	 */
	public void setCancellable(final boolean value)
	{
		setCancellable( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.cancelledDate</code> attribute.
	 * @return the cancelledDate - Cancellation Date
	 */
	public Date getCancelledDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, CANCELLEDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.cancelledDate</code> attribute.
	 * @return the cancelledDate - Cancellation Date
	 */
	public Date getCancelledDate()
	{
		return getCancelledDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.cancelledDate</code> attribute. 
	 * @param value the cancelledDate - Cancellation Date
	 */
	public void setCancelledDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, CANCELLEDDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.cancelledDate</code> attribute. 
	 * @param value the cancelledDate - Cancellation Date
	 */
	public void setCancelledDate(final Date value)
	{
		setCancelledDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.contractDuration</code> attribute.
	 * @return the contractDuration - Contract Duration
	 */
	public Integer getContractDuration(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CONTRACTDURATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.contractDuration</code> attribute.
	 * @return the contractDuration - Contract Duration
	 */
	public Integer getContractDuration()
	{
		return getContractDuration( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.contractDuration</code> attribute. 
	 * @return the contractDuration - Contract Duration
	 */
	public int getContractDurationAsPrimitive(final SessionContext ctx)
	{
		Integer value = getContractDuration( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.contractDuration</code> attribute. 
	 * @return the contractDuration - Contract Duration
	 */
	public int getContractDurationAsPrimitive()
	{
		return getContractDurationAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.contractDuration</code> attribute. 
	 * @param value the contractDuration - Contract Duration
	 */
	public void setContractDuration(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CONTRACTDURATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.contractDuration</code> attribute. 
	 * @param value the contractDuration - Contract Duration
	 */
	public void setContractDuration(final Integer value)
	{
		setContractDuration( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.contractDuration</code> attribute. 
	 * @param value the contractDuration - Contract Duration
	 */
	public void setContractDuration(final SessionContext ctx, final int value)
	{
		setContractDuration( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.contractDuration</code> attribute. 
	 * @param value the contractDuration - Contract Duration
	 */
	public void setContractDuration(final int value)
	{
		setContractDuration( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.contractFrequency</code> attribute.
	 * @return the contractFrequency - Contract Frequency
	 */
	public String getContractFrequency(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CONTRACTFREQUENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.contractFrequency</code> attribute.
	 * @return the contractFrequency - Contract Frequency
	 */
	public String getContractFrequency()
	{
		return getContractFrequency( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.contractFrequency</code> attribute. 
	 * @param value the contractFrequency - Contract Frequency
	 */
	public void setContractFrequency(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CONTRACTFREQUENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.contractFrequency</code> attribute. 
	 * @param value the contractFrequency - Contract Frequency
	 */
	public void setContractFrequency(final String value)
	{
		setContractFrequency( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.customerId</code> attribute.
	 * @return the customerId - Customer Id
	 */
	public String getCustomerId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CUSTOMERID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.customerId</code> attribute.
	 * @return the customerId - Customer Id
	 */
	public String getCustomerId()
	{
		return getCustomerId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.customerId</code> attribute. 
	 * @param value the customerId - Customer Id
	 */
	public void setCustomerId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CUSTOMERID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.customerId</code> attribute. 
	 * @param value the customerId - Customer Id
	 */
	public void setCustomerId(final String value)
	{
		setCustomerId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.description</code> attribute.
	 * @return the description - Description
	 */
	public String getDescription(final SessionContext ctx)
	{
		return (String)getProperty( ctx, DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.description</code> attribute.
	 * @return the description - Description
	 */
	public String getDescription()
	{
		return getDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.description</code> attribute. 
	 * @param value the description - Description
	 */
	public void setDescription(final SessionContext ctx, final String value)
	{
		setProperty(ctx, DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.description</code> attribute. 
	 * @param value the description - Description
	 */
	public void setDescription(final String value)
	{
		setDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.endDate</code> attribute.
	 * @return the endDate - Enddate
	 */
	public Date getEndDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, ENDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.endDate</code> attribute.
	 * @return the endDate - Enddate
	 */
	public Date getEndDate()
	{
		return getEndDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.endDate</code> attribute. 
	 * @param value the endDate - Enddate
	 */
	public void setEndDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, ENDDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.endDate</code> attribute. 
	 * @param value the endDate - Enddate
	 */
	public void setEndDate(final Date value)
	{
		setEndDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.id</code> attribute. 
	 * @param value the id - Identifier
	 */
	protected void setId(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+ID+"' is not changeable", 0 );
		}
		setProperty(ctx, ID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.id</code> attribute. 
	 * @param value the id - Identifier
	 */
	protected void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.name</code> attribute.
	 * @return the name - Name
	 */
	public String getName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.name</code> attribute.
	 * @return the name - Name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.orderEntryNumber</code> attribute.
	 * @return the orderEntryNumber - Order Entry Identifier
	 */
	public Integer getOrderEntryNumber(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, ORDERENTRYNUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.orderEntryNumber</code> attribute.
	 * @return the orderEntryNumber - Order Entry Identifier
	 */
	public Integer getOrderEntryNumber()
	{
		return getOrderEntryNumber( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.orderEntryNumber</code> attribute. 
	 * @return the orderEntryNumber - Order Entry Identifier
	 */
	public int getOrderEntryNumberAsPrimitive(final SessionContext ctx)
	{
		Integer value = getOrderEntryNumber( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.orderEntryNumber</code> attribute. 
	 * @return the orderEntryNumber - Order Entry Identifier
	 */
	public int getOrderEntryNumberAsPrimitive()
	{
		return getOrderEntryNumberAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.orderEntryNumber</code> attribute. 
	 * @param value the orderEntryNumber - Order Entry Identifier
	 */
	public void setOrderEntryNumber(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, ORDERENTRYNUMBER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.orderEntryNumber</code> attribute. 
	 * @param value the orderEntryNumber - Order Entry Identifier
	 */
	public void setOrderEntryNumber(final Integer value)
	{
		setOrderEntryNumber( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.orderEntryNumber</code> attribute. 
	 * @param value the orderEntryNumber - Order Entry Identifier
	 */
	public void setOrderEntryNumber(final SessionContext ctx, final int value)
	{
		setOrderEntryNumber( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.orderEntryNumber</code> attribute. 
	 * @param value the orderEntryNumber - Order Entry Identifier
	 */
	public void setOrderEntryNumber(final int value)
	{
		setOrderEntryNumber( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.orderNumber</code> attribute.
	 * @return the orderNumber - Order Identifier
	 */
	public String getOrderNumber(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ORDERNUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.orderNumber</code> attribute.
	 * @return the orderNumber - Order Identifier
	 */
	public String getOrderNumber()
	{
		return getOrderNumber( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.orderNumber</code> attribute. 
	 * @param value the orderNumber - Order Identifier
	 */
	public void setOrderNumber(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ORDERNUMBER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.orderNumber</code> attribute. 
	 * @param value the orderNumber - Order Identifier
	 */
	public void setOrderNumber(final String value)
	{
		setOrderNumber( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.placedOn</code> attribute.
	 * @return the placedOn - Placed on
	 */
	public Date getPlacedOn(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, PLACEDON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.placedOn</code> attribute.
	 * @return the placedOn - Placed on
	 */
	public Date getPlacedOn()
	{
		return getPlacedOn( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.placedOn</code> attribute. 
	 * @param value the placedOn - Placed on
	 */
	public void setPlacedOn(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, PLACEDON,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.placedOn</code> attribute. 
	 * @param value the placedOn - Placed on
	 */
	public void setPlacedOn(final Date value)
	{
		setPlacedOn( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.productCode</code> attribute.
	 * @return the productCode - Product Code
	 */
	public String getProductCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PRODUCTCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.productCode</code> attribute.
	 * @return the productCode - Product Code
	 */
	public String getProductCode()
	{
		return getProductCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.productCode</code> attribute. 
	 * @param value the productCode - Product Code
	 */
	public void setProductCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PRODUCTCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.productCode</code> attribute. 
	 * @param value the productCode - Product Code
	 */
	public void setProductCode(final String value)
	{
		setProductCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.renewalType</code> attribute.
	 * @return the renewalType - Renewal Type
	 */
	public String getRenewalType(final SessionContext ctx)
	{
		return (String)getProperty( ctx, RENEWALTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.renewalType</code> attribute.
	 * @return the renewalType - Renewal Type
	 */
	public String getRenewalType()
	{
		return getRenewalType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.renewalType</code> attribute. 
	 * @param value the renewalType - Renewal Type
	 */
	public void setRenewalType(final SessionContext ctx, final String value)
	{
		setProperty(ctx, RENEWALTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.renewalType</code> attribute. 
	 * @param value the renewalType - Renewal Type
	 */
	public void setRenewalType(final String value)
	{
		setRenewalType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.startDate</code> attribute.
	 * @return the startDate - Startdate
	 */
	public Date getStartDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, STARTDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.startDate</code> attribute.
	 * @return the startDate - Startdate
	 */
	public Date getStartDate()
	{
		return getStartDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.startDate</code> attribute. 
	 * @param value the startDate - Startdate
	 */
	public void setStartDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, STARTDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.startDate</code> attribute. 
	 * @param value the startDate - Startdate
	 */
	public void setStartDate(final Date value)
	{
		setStartDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.subscriptionStatus</code> attribute.
	 * @return the subscriptionStatus - Status
	 */
	public String getSubscriptionStatus(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SUBSCRIPTIONSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Subscription.subscriptionStatus</code> attribute.
	 * @return the subscriptionStatus - Status
	 */
	public String getSubscriptionStatus()
	{
		return getSubscriptionStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.subscriptionStatus</code> attribute. 
	 * @param value the subscriptionStatus - Status
	 */
	public void setSubscriptionStatus(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SUBSCRIPTIONSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Subscription.subscriptionStatus</code> attribute. 
	 * @param value the subscriptionStatus - Status
	 */
	public void setSubscriptionStatus(final String value)
	{
		setSubscriptionStatus( getSession().getSessionContext(), value );
	}
	
}
