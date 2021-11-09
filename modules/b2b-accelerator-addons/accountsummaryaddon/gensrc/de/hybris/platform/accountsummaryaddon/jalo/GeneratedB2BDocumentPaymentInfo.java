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
import de.hybris.platform.accountsummaryaddon.jalo.B2BDocument;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem B2BDocumentPaymentInfo}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BDocumentPaymentInfo extends GenericItem
{
	/** Qualifier of the <code>B2BDocumentPaymentInfo.external</code> attribute **/
	public static final String EXTERNAL = "external";
	/** Qualifier of the <code>B2BDocumentPaymentInfo.amount</code> attribute **/
	public static final String AMOUNT = "amount";
	/** Qualifier of the <code>B2BDocumentPaymentInfo.date</code> attribute **/
	public static final String DATE = "date";
	/** Qualifier of the <code>B2BDocumentPaymentInfo.ccTransactionNumber</code> attribute **/
	public static final String CCTRANSACTIONNUMBER = "ccTransactionNumber";
	/** Qualifier of the <code>B2BDocumentPaymentInfo.payDocument</code> attribute **/
	public static final String PAYDOCUMENT = "payDocument";
	/** Qualifier of the <code>B2BDocumentPaymentInfo.useDocument</code> attribute **/
	public static final String USEDOCUMENT = "useDocument";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n PAYDOCUMENT's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedB2BDocumentPaymentInfo> PAYDOCUMENTHANDLER = new BidirectionalOneToManyHandler<GeneratedB2BDocumentPaymentInfo>(
	AccountsummaryaddonConstants.TC.B2BDOCUMENTPAYMENTINFO,
	false,
	"payDocument",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n USEDOCUMENT's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedB2BDocumentPaymentInfo> USEDOCUMENTHANDLER = new BidirectionalOneToManyHandler<GeneratedB2BDocumentPaymentInfo>(
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
		tmp.put(EXTERNAL, AttributeMode.INITIAL);
		tmp.put(AMOUNT, AttributeMode.INITIAL);
		tmp.put(DATE, AttributeMode.INITIAL);
		tmp.put(CCTRANSACTIONNUMBER, AttributeMode.INITIAL);
		tmp.put(PAYDOCUMENT, AttributeMode.INITIAL);
		tmp.put(USEDOCUMENT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentPaymentInfo.amount</code> attribute.
	 * @return the amount - amount
	 */
	public BigDecimal getAmount(final SessionContext ctx)
	{
		return (BigDecimal)getProperty( ctx, AMOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentPaymentInfo.amount</code> attribute.
	 * @return the amount - amount
	 */
	public BigDecimal getAmount()
	{
		return getAmount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentPaymentInfo.amount</code> attribute. 
	 * @param value the amount - amount
	 */
	public void setAmount(final SessionContext ctx, final BigDecimal value)
	{
		setProperty(ctx, AMOUNT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentPaymentInfo.amount</code> attribute. 
	 * @param value the amount - amount
	 */
	public void setAmount(final BigDecimal value)
	{
		setAmount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentPaymentInfo.ccTransactionNumber</code> attribute.
	 * @return the ccTransactionNumber
	 */
	public String getCcTransactionNumber(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CCTRANSACTIONNUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentPaymentInfo.ccTransactionNumber</code> attribute.
	 * @return the ccTransactionNumber
	 */
	public String getCcTransactionNumber()
	{
		return getCcTransactionNumber( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentPaymentInfo.ccTransactionNumber</code> attribute. 
	 * @param value the ccTransactionNumber
	 */
	public void setCcTransactionNumber(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CCTRANSACTIONNUMBER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentPaymentInfo.ccTransactionNumber</code> attribute. 
	 * @param value the ccTransactionNumber
	 */
	public void setCcTransactionNumber(final String value)
	{
		setCcTransactionNumber( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		PAYDOCUMENTHANDLER.newInstance(ctx, allAttributes);
		USEDOCUMENTHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentPaymentInfo.date</code> attribute.
	 * @return the date - date
	 */
	public Date getDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, DATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentPaymentInfo.date</code> attribute.
	 * @return the date - date
	 */
	public Date getDate()
	{
		return getDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentPaymentInfo.date</code> attribute. 
	 * @param value the date - date
	 */
	public void setDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, DATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentPaymentInfo.date</code> attribute. 
	 * @param value the date - date
	 */
	public void setDate(final Date value)
	{
		setDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentPaymentInfo.external</code> attribute.
	 * @return the external
	 */
	public String getExternal(final SessionContext ctx)
	{
		return (String)getProperty( ctx, EXTERNAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentPaymentInfo.external</code> attribute.
	 * @return the external
	 */
	public String getExternal()
	{
		return getExternal( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentPaymentInfo.external</code> attribute. 
	 * @param value the external
	 */
	public void setExternal(final SessionContext ctx, final String value)
	{
		setProperty(ctx, EXTERNAL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentPaymentInfo.external</code> attribute. 
	 * @param value the external
	 */
	public void setExternal(final String value)
	{
		setExternal( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentPaymentInfo.payDocument</code> attribute.
	 * @return the payDocument
	 */
	public B2BDocument getPayDocument(final SessionContext ctx)
	{
		return (B2BDocument)getProperty( ctx, PAYDOCUMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentPaymentInfo.payDocument</code> attribute.
	 * @return the payDocument
	 */
	public B2BDocument getPayDocument()
	{
		return getPayDocument( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentPaymentInfo.payDocument</code> attribute. 
	 * @param value the payDocument
	 */
	public void setPayDocument(final SessionContext ctx, final B2BDocument value)
	{
		PAYDOCUMENTHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentPaymentInfo.payDocument</code> attribute. 
	 * @param value the payDocument
	 */
	public void setPayDocument(final B2BDocument value)
	{
		setPayDocument( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentPaymentInfo.useDocument</code> attribute.
	 * @return the useDocument
	 */
	public B2BDocument getUseDocument(final SessionContext ctx)
	{
		return (B2BDocument)getProperty( ctx, USEDOCUMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentPaymentInfo.useDocument</code> attribute.
	 * @return the useDocument
	 */
	public B2BDocument getUseDocument()
	{
		return getUseDocument( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentPaymentInfo.useDocument</code> attribute. 
	 * @param value the useDocument
	 */
	public void setUseDocument(final SessionContext ctx, final B2BDocument value)
	{
		USEDOCUMENTHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentPaymentInfo.useDocument</code> attribute. 
	 * @param value the useDocument
	 */
	public void setUseDocument(final B2BDocument value)
	{
		setUseDocument( getSession().getSessionContext(), value );
	}
	
}
