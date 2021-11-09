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
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.b2b.jalo.B2BMerchantCheckResult B2BMerchantCheckResult}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BMerchantCheckResult extends GenericItem
{
	/** Qualifier of the <code>B2BMerchantCheckResult.merchantcheck</code> attribute **/
	public static final String MERCHANTCHECK = "merchantcheck";
	/** Qualifier of the <code>B2BMerchantCheckResult.merchantCheckTypeCode</code> attribute **/
	public static final String MERCHANTCHECKTYPECODE = "merchantCheckTypeCode";
	/** Qualifier of the <code>B2BMerchantCheckResult.status</code> attribute **/
	public static final String STATUS = "status";
	/** Qualifier of the <code>B2BMerchantCheckResult.statusEmail</code> attribute **/
	public static final String STATUSEMAIL = "statusEmail";
	/** Qualifier of the <code>B2BMerchantCheckResult.note</code> attribute **/
	public static final String NOTE = "note";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(MERCHANTCHECK, AttributeMode.INITIAL);
		tmp.put(MERCHANTCHECKTYPECODE, AttributeMode.INITIAL);
		tmp.put(STATUS, AttributeMode.INITIAL);
		tmp.put(STATUSEMAIL, AttributeMode.INITIAL);
		tmp.put(NOTE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BMerchantCheckResult.merchantcheck</code> attribute.
	 * @return the merchantcheck
	 */
	public B2BMerchantCheck getMerchantcheck(final SessionContext ctx)
	{
		return (B2BMerchantCheck)getProperty( ctx, MERCHANTCHECK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BMerchantCheckResult.merchantcheck</code> attribute.
	 * @return the merchantcheck
	 */
	public B2BMerchantCheck getMerchantcheck()
	{
		return getMerchantcheck( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BMerchantCheckResult.merchantcheck</code> attribute. 
	 * @param value the merchantcheck
	 */
	public void setMerchantcheck(final SessionContext ctx, final B2BMerchantCheck value)
	{
		setProperty(ctx, MERCHANTCHECK,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BMerchantCheckResult.merchantcheck</code> attribute. 
	 * @param value the merchantcheck
	 */
	public void setMerchantcheck(final B2BMerchantCheck value)
	{
		setMerchantcheck( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BMerchantCheckResult.merchantCheckTypeCode</code> attribute.
	 * @return the merchantCheckTypeCode - the item type code for the merchant check, the result is
	 * 						holding as defined in items.xml
	 */
	public String getMerchantCheckTypeCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, MERCHANTCHECKTYPECODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BMerchantCheckResult.merchantCheckTypeCode</code> attribute.
	 * @return the merchantCheckTypeCode - the item type code for the merchant check, the result is
	 * 						holding as defined in items.xml
	 */
	public String getMerchantCheckTypeCode()
	{
		return getMerchantCheckTypeCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BMerchantCheckResult.merchantCheckTypeCode</code> attribute. 
	 * @param value the merchantCheckTypeCode - the item type code for the merchant check, the result is
	 * 						holding as defined in items.xml
	 */
	public void setMerchantCheckTypeCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, MERCHANTCHECKTYPECODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BMerchantCheckResult.merchantCheckTypeCode</code> attribute. 
	 * @param value the merchantCheckTypeCode - the item type code for the merchant check, the result is
	 * 						holding as defined in items.xml
	 */
	public void setMerchantCheckTypeCode(final String value)
	{
		setMerchantCheckTypeCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BMerchantCheckResult.note</code> attribute.
	 * @return the note
	 */
	public String getNote(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedB2BMerchantCheckResult.getNote requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NOTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BMerchantCheckResult.note</code> attribute.
	 * @return the note
	 */
	public String getNote()
	{
		return getNote( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BMerchantCheckResult.note</code> attribute. 
	 * @return the localized note
	 */
	public Map<Language,String> getAllNote(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NOTE,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BMerchantCheckResult.note</code> attribute. 
	 * @return the localized note
	 */
	public Map<Language,String> getAllNote()
	{
		return getAllNote( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BMerchantCheckResult.note</code> attribute. 
	 * @param value the note
	 */
	public void setNote(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedB2BMerchantCheckResult.setNote requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NOTE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BMerchantCheckResult.note</code> attribute. 
	 * @param value the note
	 */
	public void setNote(final String value)
	{
		setNote( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BMerchantCheckResult.note</code> attribute. 
	 * @param value the note
	 */
	public void setAllNote(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NOTE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BMerchantCheckResult.note</code> attribute. 
	 * @param value the note
	 */
	public void setAllNote(final Map<Language,String> value)
	{
		setAllNote( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BMerchantCheckResult.status</code> attribute.
	 * @return the status
	 */
	public EnumerationValue getStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BMerchantCheckResult.status</code> attribute.
	 * @return the status
	 */
	public EnumerationValue getStatus()
	{
		return getStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BMerchantCheckResult.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, STATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BMerchantCheckResult.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final EnumerationValue value)
	{
		setStatus( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BMerchantCheckResult.statusEmail</code> attribute.
	 * @return the statusEmail
	 */
	public EnumerationValue getStatusEmail(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, STATUSEMAIL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BMerchantCheckResult.statusEmail</code> attribute.
	 * @return the statusEmail
	 */
	public EnumerationValue getStatusEmail()
	{
		return getStatusEmail( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BMerchantCheckResult.statusEmail</code> attribute. 
	 * @param value the statusEmail
	 */
	public void setStatusEmail(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, STATUSEMAIL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BMerchantCheckResult.statusEmail</code> attribute. 
	 * @param value the statusEmail
	 */
	public void setStatusEmail(final EnumerationValue value)
	{
		setStatusEmail( getSession().getSessionContext(), value );
	}
	
}
