/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.jalo;

import de.hybris.platform.accountsummaryaddon.constants.AccountsummaryaddonConstants;
import de.hybris.platform.accountsummaryaddon.jalo.B2BDocumentPaymentInfo;
import de.hybris.platform.accountsummaryaddon.jalo.B2BDocumentType;
import de.hybris.platform.accountsummaryaddon.jalo.DocumentMedia;
import de.hybris.platform.b2b.jalo.B2BUnit;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem B2BDocument}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BDocument extends GenericItem
{
	/** Qualifier of the <code>B2BDocument.documentNumber</code> attribute **/
	public static final String DOCUMENTNUMBER = "documentNumber";
	/** Qualifier of the <code>B2BDocument.status</code> attribute **/
	public static final String STATUS = "status";
	/** Qualifier of the <code>B2BDocument.date</code> attribute **/
	public static final String DATE = "date";
	/** Qualifier of the <code>B2BDocument.dueDate</code> attribute **/
	public static final String DUEDATE = "dueDate";
	/** Qualifier of the <code>B2BDocument.amount</code> attribute **/
	public static final String AMOUNT = "amount";
	/** Qualifier of the <code>B2BDocument.openAmount</code> attribute **/
	public static final String OPENAMOUNT = "openAmount";
	/** Qualifier of the <code>B2BDocument.currency</code> attribute **/
	public static final String CURRENCY = "currency";
	/** Qualifier of the <code>B2BDocument.documentMedia</code> attribute **/
	public static final String DOCUMENTMEDIA = "documentMedia";
	/** Qualifier of the <code>B2BDocument.documentType</code> attribute **/
	public static final String DOCUMENTTYPE = "documentType";
	/** Qualifier of the <code>B2BDocument.unit</code> attribute **/
	public static final String UNIT = "unit";
	/** Qualifier of the <code>B2BDocument.order</code> attribute **/
	public static final String ORDER = "order";
	/** Qualifier of the <code>B2BDocument.payDocumentPaymentInfo</code> attribute **/
	public static final String PAYDOCUMENTPAYMENTINFO = "payDocumentPaymentInfo";
	/** Qualifier of the <code>B2BDocument.useDocumentPaymentInfo</code> attribute **/
	public static final String USEDOCUMENTPAYMENTINFO = "useDocumentPaymentInfo";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n DOCUMENTTYPE's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedB2BDocument> DOCUMENTTYPEHANDLER = new BidirectionalOneToManyHandler<GeneratedB2BDocument>(
	AccountsummaryaddonConstants.TC.B2BDOCUMENT,
	false,
	"documentType",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n UNIT's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedB2BDocument> UNITHANDLER = new BidirectionalOneToManyHandler<GeneratedB2BDocument>(
	AccountsummaryaddonConstants.TC.B2BDOCUMENT,
	false,
	"unit",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n ORDER's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedB2BDocument> ORDERHANDLER = new BidirectionalOneToManyHandler<GeneratedB2BDocument>(
	AccountsummaryaddonConstants.TC.B2BDOCUMENT,
	false,
	"order",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link OneToManyHandler} for handling 1:n PAYDOCUMENTPAYMENTINFO's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<B2BDocumentPaymentInfo> PAYDOCUMENTPAYMENTINFOHANDLER = new OneToManyHandler<B2BDocumentPaymentInfo>(
	AccountsummaryaddonConstants.TC.B2BDOCUMENTPAYMENTINFO,
	false,
	"payDocument",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link OneToManyHandler} for handling 1:n USEDOCUMENTPAYMENTINFO's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<B2BDocumentPaymentInfo> USEDOCUMENTPAYMENTINFOHANDLER = new OneToManyHandler<B2BDocumentPaymentInfo>(
	AccountsummaryaddonConstants.TC.B2BDOCUMENTPAYMENTINFO,
	false,
	"useDocument",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(DOCUMENTNUMBER, AttributeMode.INITIAL);
		tmp.put(STATUS, AttributeMode.INITIAL);
		tmp.put(DATE, AttributeMode.INITIAL);
		tmp.put(DUEDATE, AttributeMode.INITIAL);
		tmp.put(AMOUNT, AttributeMode.INITIAL);
		tmp.put(OPENAMOUNT, AttributeMode.INITIAL);
		tmp.put(CURRENCY, AttributeMode.INITIAL);
		tmp.put(DOCUMENTMEDIA, AttributeMode.INITIAL);
		tmp.put(DOCUMENTTYPE, AttributeMode.INITIAL);
		tmp.put(UNIT, AttributeMode.INITIAL);
		tmp.put(ORDER, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.amount</code> attribute.
	 * @return the amount - amount
	 */
	public BigDecimal getAmount(final SessionContext ctx)
	{
		return (BigDecimal)getProperty( ctx, AMOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.amount</code> attribute.
	 * @return the amount - amount
	 */
	public BigDecimal getAmount()
	{
		return getAmount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.amount</code> attribute. 
	 * @param value the amount - amount
	 */
	public void setAmount(final SessionContext ctx, final BigDecimal value)
	{
		setProperty(ctx, AMOUNT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.amount</code> attribute. 
	 * @param value the amount - amount
	 */
	public void setAmount(final BigDecimal value)
	{
		setAmount( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		DOCUMENTTYPEHANDLER.newInstance(ctx, allAttributes);
		UNITHANDLER.newInstance(ctx, allAttributes);
		ORDERHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.currency</code> attribute.
	 * @return the currency
	 */
	public Currency getCurrency(final SessionContext ctx)
	{
		return (Currency)getProperty( ctx, CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.currency</code> attribute.
	 * @return the currency
	 */
	public Currency getCurrency()
	{
		return getCurrency( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.currency</code> attribute. 
	 * @param value the currency
	 */
	public void setCurrency(final SessionContext ctx, final Currency value)
	{
		setProperty(ctx, CURRENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.currency</code> attribute. 
	 * @param value the currency
	 */
	public void setCurrency(final Currency value)
	{
		setCurrency( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.date</code> attribute.
	 * @return the date - date
	 */
	public Date getDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, DATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.date</code> attribute.
	 * @return the date - date
	 */
	public Date getDate()
	{
		return getDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.date</code> attribute. 
	 * @param value the date - date
	 */
	public void setDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, DATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.date</code> attribute. 
	 * @param value the date - date
	 */
	public void setDate(final Date value)
	{
		setDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.documentMedia</code> attribute.
	 * @return the documentMedia
	 */
	public DocumentMedia getDocumentMedia(final SessionContext ctx)
	{
		return (DocumentMedia)getProperty( ctx, DOCUMENTMEDIA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.documentMedia</code> attribute.
	 * @return the documentMedia
	 */
	public DocumentMedia getDocumentMedia()
	{
		return getDocumentMedia( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.documentMedia</code> attribute. 
	 * @param value the documentMedia
	 */
	public void setDocumentMedia(final SessionContext ctx, final DocumentMedia value)
	{
		setProperty(ctx, DOCUMENTMEDIA,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.documentMedia</code> attribute. 
	 * @param value the documentMedia
	 */
	public void setDocumentMedia(final DocumentMedia value)
	{
		setDocumentMedia( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.documentNumber</code> attribute.
	 * @return the documentNumber
	 */
	public String getDocumentNumber(final SessionContext ctx)
	{
		return (String)getProperty( ctx, DOCUMENTNUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.documentNumber</code> attribute.
	 * @return the documentNumber
	 */
	public String getDocumentNumber()
	{
		return getDocumentNumber( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.documentNumber</code> attribute. 
	 * @param value the documentNumber
	 */
	public void setDocumentNumber(final SessionContext ctx, final String value)
	{
		setProperty(ctx, DOCUMENTNUMBER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.documentNumber</code> attribute. 
	 * @param value the documentNumber
	 */
	public void setDocumentNumber(final String value)
	{
		setDocumentNumber( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.documentType</code> attribute.
	 * @return the documentType
	 */
	public B2BDocumentType getDocumentType(final SessionContext ctx)
	{
		return (B2BDocumentType)getProperty( ctx, DOCUMENTTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.documentType</code> attribute.
	 * @return the documentType
	 */
	public B2BDocumentType getDocumentType()
	{
		return getDocumentType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.documentType</code> attribute. 
	 * @param value the documentType
	 */
	public void setDocumentType(final SessionContext ctx, final B2BDocumentType value)
	{
		DOCUMENTTYPEHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.documentType</code> attribute. 
	 * @param value the documentType
	 */
	public void setDocumentType(final B2BDocumentType value)
	{
		setDocumentType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.dueDate</code> attribute.
	 * @return the dueDate - dueDate
	 */
	public Date getDueDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, DUEDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.dueDate</code> attribute.
	 * @return the dueDate - dueDate
	 */
	public Date getDueDate()
	{
		return getDueDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.dueDate</code> attribute. 
	 * @param value the dueDate - dueDate
	 */
	public void setDueDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, DUEDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.dueDate</code> attribute. 
	 * @param value the dueDate - dueDate
	 */
	public void setDueDate(final Date value)
	{
		setDueDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.openAmount</code> attribute.
	 * @return the openAmount - openAmount
	 */
	public BigDecimal getOpenAmount(final SessionContext ctx)
	{
		return (BigDecimal)getProperty( ctx, OPENAMOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.openAmount</code> attribute.
	 * @return the openAmount - openAmount
	 */
	public BigDecimal getOpenAmount()
	{
		return getOpenAmount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.openAmount</code> attribute. 
	 * @param value the openAmount - openAmount
	 */
	public void setOpenAmount(final SessionContext ctx, final BigDecimal value)
	{
		setProperty(ctx, OPENAMOUNT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.openAmount</code> attribute. 
	 * @param value the openAmount - openAmount
	 */
	public void setOpenAmount(final BigDecimal value)
	{
		setOpenAmount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.order</code> attribute.
	 * @return the order
	 */
	public AbstractOrder getOrder(final SessionContext ctx)
	{
		return (AbstractOrder)getProperty( ctx, ORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.order</code> attribute.
	 * @return the order
	 */
	public AbstractOrder getOrder()
	{
		return getOrder( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.order</code> attribute. 
	 * @param value the order
	 */
	public void setOrder(final SessionContext ctx, final AbstractOrder value)
	{
		ORDERHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.order</code> attribute. 
	 * @param value the order
	 */
	public void setOrder(final AbstractOrder value)
	{
		setOrder( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.payDocumentPaymentInfo</code> attribute.
	 * @return the payDocumentPaymentInfo
	 */
	public Collection<B2BDocumentPaymentInfo> getPayDocumentPaymentInfo(final SessionContext ctx)
	{
		return PAYDOCUMENTPAYMENTINFOHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.payDocumentPaymentInfo</code> attribute.
	 * @return the payDocumentPaymentInfo
	 */
	public Collection<B2BDocumentPaymentInfo> getPayDocumentPaymentInfo()
	{
		return getPayDocumentPaymentInfo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.payDocumentPaymentInfo</code> attribute. 
	 * @param value the payDocumentPaymentInfo
	 */
	public void setPayDocumentPaymentInfo(final SessionContext ctx, final Collection<B2BDocumentPaymentInfo> value)
	{
		PAYDOCUMENTPAYMENTINFOHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.payDocumentPaymentInfo</code> attribute. 
	 * @param value the payDocumentPaymentInfo
	 */
	public void setPayDocumentPaymentInfo(final Collection<B2BDocumentPaymentInfo> value)
	{
		setPayDocumentPaymentInfo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to payDocumentPaymentInfo. 
	 * @param value the item to add to payDocumentPaymentInfo
	 */
	public void addToPayDocumentPaymentInfo(final SessionContext ctx, final B2BDocumentPaymentInfo value)
	{
		PAYDOCUMENTPAYMENTINFOHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to payDocumentPaymentInfo. 
	 * @param value the item to add to payDocumentPaymentInfo
	 */
	public void addToPayDocumentPaymentInfo(final B2BDocumentPaymentInfo value)
	{
		addToPayDocumentPaymentInfo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from payDocumentPaymentInfo. 
	 * @param value the item to remove from payDocumentPaymentInfo
	 */
	public void removeFromPayDocumentPaymentInfo(final SessionContext ctx, final B2BDocumentPaymentInfo value)
	{
		PAYDOCUMENTPAYMENTINFOHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from payDocumentPaymentInfo. 
	 * @param value the item to remove from payDocumentPaymentInfo
	 */
	public void removeFromPayDocumentPaymentInfo(final B2BDocumentPaymentInfo value)
	{
		removeFromPayDocumentPaymentInfo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.status</code> attribute.
	 * @return the status
	 */
	public EnumerationValue getStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.status</code> attribute.
	 * @return the status
	 */
	public EnumerationValue getStatus()
	{
		return getStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, STATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final EnumerationValue value)
	{
		setStatus( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.unit</code> attribute.
	 * @return the unit
	 */
	public B2BUnit getUnit(final SessionContext ctx)
	{
		return (B2BUnit)getProperty( ctx, UNIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.unit</code> attribute.
	 * @return the unit
	 */
	public B2BUnit getUnit()
	{
		return getUnit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.unit</code> attribute. 
	 * @param value the unit
	 */
	public void setUnit(final SessionContext ctx, final B2BUnit value)
	{
		UNITHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.unit</code> attribute. 
	 * @param value the unit
	 */
	public void setUnit(final B2BUnit value)
	{
		setUnit( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.useDocumentPaymentInfo</code> attribute.
	 * @return the useDocumentPaymentInfo
	 */
	public Collection<B2BDocumentPaymentInfo> getUseDocumentPaymentInfo(final SessionContext ctx)
	{
		return USEDOCUMENTPAYMENTINFOHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocument.useDocumentPaymentInfo</code> attribute.
	 * @return the useDocumentPaymentInfo
	 */
	public Collection<B2BDocumentPaymentInfo> getUseDocumentPaymentInfo()
	{
		return getUseDocumentPaymentInfo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.useDocumentPaymentInfo</code> attribute. 
	 * @param value the useDocumentPaymentInfo
	 */
	public void setUseDocumentPaymentInfo(final SessionContext ctx, final Collection<B2BDocumentPaymentInfo> value)
	{
		USEDOCUMENTPAYMENTINFOHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocument.useDocumentPaymentInfo</code> attribute. 
	 * @param value the useDocumentPaymentInfo
	 */
	public void setUseDocumentPaymentInfo(final Collection<B2BDocumentPaymentInfo> value)
	{
		setUseDocumentPaymentInfo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to useDocumentPaymentInfo. 
	 * @param value the item to add to useDocumentPaymentInfo
	 */
	public void addToUseDocumentPaymentInfo(final SessionContext ctx, final B2BDocumentPaymentInfo value)
	{
		USEDOCUMENTPAYMENTINFOHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to useDocumentPaymentInfo. 
	 * @param value the item to add to useDocumentPaymentInfo
	 */
	public void addToUseDocumentPaymentInfo(final B2BDocumentPaymentInfo value)
	{
		addToUseDocumentPaymentInfo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from useDocumentPaymentInfo. 
	 * @param value the item to remove from useDocumentPaymentInfo
	 */
	public void removeFromUseDocumentPaymentInfo(final SessionContext ctx, final B2BDocumentPaymentInfo value)
	{
		USEDOCUMENTPAYMENTINFOHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from useDocumentPaymentInfo. 
	 * @param value the item to remove from useDocumentPaymentInfo
	 */
	public void removeFromUseDocumentPaymentInfo(final B2BDocumentPaymentInfo value)
	{
		removeFromUseDocumentPaymentInfo( getSession().getSessionContext(), value );
	}
	
}
