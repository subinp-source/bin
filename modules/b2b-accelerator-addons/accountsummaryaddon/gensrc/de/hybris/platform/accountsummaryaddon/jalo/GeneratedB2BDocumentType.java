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
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem B2BDocumentType}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BDocumentType extends GenericItem
{
	/** Qualifier of the <code>B2BDocumentType.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>B2BDocumentType.includeInOpenBalance</code> attribute **/
	public static final String INCLUDEINOPENBALANCE = "includeInOpenBalance";
	/** Qualifier of the <code>B2BDocumentType.displayInAllList</code> attribute **/
	public static final String DISPLAYINALLLIST = "displayInAllList";
	/** Qualifier of the <code>B2BDocumentType.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>B2BDocumentType.payableOrUsable</code> attribute **/
	public static final String PAYABLEORUSABLE = "payableOrUsable";
	/** Qualifier of the <code>B2BDocumentType.document</code> attribute **/
	public static final String DOCUMENT = "document";
	/**
	* {@link OneToManyHandler} for handling 1:n DOCUMENT's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<B2BDocument> DOCUMENTHANDLER = new OneToManyHandler<B2BDocument>(
	AccountsummaryaddonConstants.TC.B2BDOCUMENT,
	false,
	"documentType",
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
		tmp.put(INCLUDEINOPENBALANCE, AttributeMode.INITIAL);
		tmp.put(DISPLAYINALLLIST, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(PAYABLEORUSABLE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentType.code</code> attribute.
	 * @return the code - code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentType.code</code> attribute.
	 * @return the code - code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentType.code</code> attribute. 
	 * @param value the code - code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentType.code</code> attribute. 
	 * @param value the code - code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentType.displayInAllList</code> attribute.
	 * @return the displayInAllList - displayInAllList
	 */
	public Boolean isDisplayInAllList(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, DISPLAYINALLLIST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentType.displayInAllList</code> attribute.
	 * @return the displayInAllList - displayInAllList
	 */
	public Boolean isDisplayInAllList()
	{
		return isDisplayInAllList( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentType.displayInAllList</code> attribute. 
	 * @return the displayInAllList - displayInAllList
	 */
	public boolean isDisplayInAllListAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isDisplayInAllList( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentType.displayInAllList</code> attribute. 
	 * @return the displayInAllList - displayInAllList
	 */
	public boolean isDisplayInAllListAsPrimitive()
	{
		return isDisplayInAllListAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentType.displayInAllList</code> attribute. 
	 * @param value the displayInAllList - displayInAllList
	 */
	public void setDisplayInAllList(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, DISPLAYINALLLIST,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentType.displayInAllList</code> attribute. 
	 * @param value the displayInAllList - displayInAllList
	 */
	public void setDisplayInAllList(final Boolean value)
	{
		setDisplayInAllList( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentType.displayInAllList</code> attribute. 
	 * @param value the displayInAllList - displayInAllList
	 */
	public void setDisplayInAllList(final SessionContext ctx, final boolean value)
	{
		setDisplayInAllList( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentType.displayInAllList</code> attribute. 
	 * @param value the displayInAllList - displayInAllList
	 */
	public void setDisplayInAllList(final boolean value)
	{
		setDisplayInAllList( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentType.document</code> attribute.
	 * @return the document
	 */
	public Collection<B2BDocument> getDocument(final SessionContext ctx)
	{
		return DOCUMENTHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentType.document</code> attribute.
	 * @return the document
	 */
	public Collection<B2BDocument> getDocument()
	{
		return getDocument( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentType.document</code> attribute. 
	 * @param value the document
	 */
	public void setDocument(final SessionContext ctx, final Collection<B2BDocument> value)
	{
		DOCUMENTHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentType.document</code> attribute. 
	 * @param value the document
	 */
	public void setDocument(final Collection<B2BDocument> value)
	{
		setDocument( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to document. 
	 * @param value the item to add to document
	 */
	public void addToDocument(final SessionContext ctx, final B2BDocument value)
	{
		DOCUMENTHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to document. 
	 * @param value the item to add to document
	 */
	public void addToDocument(final B2BDocument value)
	{
		addToDocument( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from document. 
	 * @param value the item to remove from document
	 */
	public void removeFromDocument(final SessionContext ctx, final B2BDocument value)
	{
		DOCUMENTHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from document. 
	 * @param value the item to remove from document
	 */
	public void removeFromDocument(final B2BDocument value)
	{
		removeFromDocument( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentType.includeInOpenBalance</code> attribute.
	 * @return the includeInOpenBalance - includeInOpenBalance
	 */
	public Boolean isIncludeInOpenBalance(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, INCLUDEINOPENBALANCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentType.includeInOpenBalance</code> attribute.
	 * @return the includeInOpenBalance - includeInOpenBalance
	 */
	public Boolean isIncludeInOpenBalance()
	{
		return isIncludeInOpenBalance( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentType.includeInOpenBalance</code> attribute. 
	 * @return the includeInOpenBalance - includeInOpenBalance
	 */
	public boolean isIncludeInOpenBalanceAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIncludeInOpenBalance( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentType.includeInOpenBalance</code> attribute. 
	 * @return the includeInOpenBalance - includeInOpenBalance
	 */
	public boolean isIncludeInOpenBalanceAsPrimitive()
	{
		return isIncludeInOpenBalanceAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentType.includeInOpenBalance</code> attribute. 
	 * @param value the includeInOpenBalance - includeInOpenBalance
	 */
	public void setIncludeInOpenBalance(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, INCLUDEINOPENBALANCE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentType.includeInOpenBalance</code> attribute. 
	 * @param value the includeInOpenBalance - includeInOpenBalance
	 */
	public void setIncludeInOpenBalance(final Boolean value)
	{
		setIncludeInOpenBalance( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentType.includeInOpenBalance</code> attribute. 
	 * @param value the includeInOpenBalance - includeInOpenBalance
	 */
	public void setIncludeInOpenBalance(final SessionContext ctx, final boolean value)
	{
		setIncludeInOpenBalance( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentType.includeInOpenBalance</code> attribute. 
	 * @param value the includeInOpenBalance - includeInOpenBalance
	 */
	public void setIncludeInOpenBalance(final boolean value)
	{
		setIncludeInOpenBalance( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentType.name</code> attribute.
	 * @return the name - name
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedB2BDocumentType.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentType.name</code> attribute.
	 * @return the name - name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentType.name</code> attribute. 
	 * @return the localized name - name
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentType.name</code> attribute. 
	 * @return the localized name - name
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentType.name</code> attribute. 
	 * @param value the name - name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedB2BDocumentType.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentType.name</code> attribute. 
	 * @param value the name - name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentType.name</code> attribute. 
	 * @param value the name - name
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentType.name</code> attribute. 
	 * @param value the name - name
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentType.payableOrUsable</code> attribute.
	 * @return the payableOrUsable - payableOrUsable
	 */
	public EnumerationValue getPayableOrUsable(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, PAYABLEORUSABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BDocumentType.payableOrUsable</code> attribute.
	 * @return the payableOrUsable - payableOrUsable
	 */
	public EnumerationValue getPayableOrUsable()
	{
		return getPayableOrUsable( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentType.payableOrUsable</code> attribute. 
	 * @param value the payableOrUsable - payableOrUsable
	 */
	public void setPayableOrUsable(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, PAYABLEORUSABLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BDocumentType.payableOrUsable</code> attribute. 
	 * @param value the payableOrUsable - payableOrUsable
	 */
	public void setPayableOrUsable(final EnumerationValue value)
	{
		setPayableOrUsable( getSession().getSessionContext(), value );
	}
	
}
