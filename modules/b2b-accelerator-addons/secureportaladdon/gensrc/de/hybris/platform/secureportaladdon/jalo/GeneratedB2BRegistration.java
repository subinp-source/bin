/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.secureportaladdon.jalo;

import de.hybris.platform.b2b.jalo.B2BUnit;
import de.hybris.platform.cms2.jalo.site.CMSSite;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.c2l.Region;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.secureportaladdon.constants.SecureportaladdonConstants;
import de.hybris.platform.store.BaseStore;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem B2BRegistration}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BRegistration extends GenericItem
{
	/** Qualifier of the <code>B2BRegistration.cmsSite</code> attribute **/
	public static final String CMSSITE = "cmsSite";
	/** Qualifier of the <code>B2BRegistration.language</code> attribute **/
	public static final String LANGUAGE = "language";
	/** Qualifier of the <code>B2BRegistration.currency</code> attribute **/
	public static final String CURRENCY = "currency";
	/** Qualifier of the <code>B2BRegistration.baseStore</code> attribute **/
	public static final String BASESTORE = "baseStore";
	/** Qualifier of the <code>B2BRegistration.title</code> attribute **/
	public static final String TITLE = "title";
	/** Qualifier of the <code>B2BRegistration.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>B2BRegistration.email</code> attribute **/
	public static final String EMAIL = "email";
	/** Qualifier of the <code>B2BRegistration.position</code> attribute **/
	public static final String POSITION = "position";
	/** Qualifier of the <code>B2BRegistration.telephone</code> attribute **/
	public static final String TELEPHONE = "telephone";
	/** Qualifier of the <code>B2BRegistration.telephoneExtension</code> attribute **/
	public static final String TELEPHONEEXTENSION = "telephoneExtension";
	/** Qualifier of the <code>B2BRegistration.companyName</code> attribute **/
	public static final String COMPANYNAME = "companyName";
	/** Qualifier of the <code>B2BRegistration.companyAddressStreet</code> attribute **/
	public static final String COMPANYADDRESSSTREET = "companyAddressStreet";
	/** Qualifier of the <code>B2BRegistration.companyAddressStreetLine2</code> attribute **/
	public static final String COMPANYADDRESSSTREETLINE2 = "companyAddressStreetLine2";
	/** Qualifier of the <code>B2BRegistration.companyAddressCity</code> attribute **/
	public static final String COMPANYADDRESSCITY = "companyAddressCity";
	/** Qualifier of the <code>B2BRegistration.companyAddressPostalCode</code> attribute **/
	public static final String COMPANYADDRESSPOSTALCODE = "companyAddressPostalCode";
	/** Qualifier of the <code>B2BRegistration.companyAddressRegion</code> attribute **/
	public static final String COMPANYADDRESSREGION = "companyAddressRegion";
	/** Qualifier of the <code>B2BRegistration.companyAddressCountry</code> attribute **/
	public static final String COMPANYADDRESSCOUNTRY = "companyAddressCountry";
	/** Qualifier of the <code>B2BRegistration.message</code> attribute **/
	public static final String MESSAGE = "message";
	/** Qualifier of the <code>B2BRegistration.rejectReason</code> attribute **/
	public static final String REJECTREASON = "rejectReason";
	/** Qualifier of the <code>B2BRegistration.defaultB2BUnit</code> attribute **/
	public static final String DEFAULTB2BUNIT = "defaultB2BUnit";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CMSSITE, AttributeMode.INITIAL);
		tmp.put(LANGUAGE, AttributeMode.INITIAL);
		tmp.put(CURRENCY, AttributeMode.INITIAL);
		tmp.put(BASESTORE, AttributeMode.INITIAL);
		tmp.put(TITLE, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(EMAIL, AttributeMode.INITIAL);
		tmp.put(POSITION, AttributeMode.INITIAL);
		tmp.put(TELEPHONE, AttributeMode.INITIAL);
		tmp.put(TELEPHONEEXTENSION, AttributeMode.INITIAL);
		tmp.put(COMPANYNAME, AttributeMode.INITIAL);
		tmp.put(COMPANYADDRESSSTREET, AttributeMode.INITIAL);
		tmp.put(COMPANYADDRESSSTREETLINE2, AttributeMode.INITIAL);
		tmp.put(COMPANYADDRESSCITY, AttributeMode.INITIAL);
		tmp.put(COMPANYADDRESSPOSTALCODE, AttributeMode.INITIAL);
		tmp.put(COMPANYADDRESSREGION, AttributeMode.INITIAL);
		tmp.put(COMPANYADDRESSCOUNTRY, AttributeMode.INITIAL);
		tmp.put(MESSAGE, AttributeMode.INITIAL);
		tmp.put(REJECTREASON, AttributeMode.INITIAL);
		tmp.put(DEFAULTB2BUNIT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.baseStore</code> attribute.
	 * @return the baseStore
	 */
	public BaseStore getBaseStore(final SessionContext ctx)
	{
		return (BaseStore)getProperty( ctx, BASESTORE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.baseStore</code> attribute.
	 * @return the baseStore
	 */
	public BaseStore getBaseStore()
	{
		return getBaseStore( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.baseStore</code> attribute. 
	 * @param value the baseStore
	 */
	public void setBaseStore(final SessionContext ctx, final BaseStore value)
	{
		setProperty(ctx, BASESTORE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.baseStore</code> attribute. 
	 * @param value the baseStore
	 */
	public void setBaseStore(final BaseStore value)
	{
		setBaseStore( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.cmsSite</code> attribute.
	 * @return the cmsSite
	 */
	public CMSSite getCmsSite(final SessionContext ctx)
	{
		return (CMSSite)getProperty( ctx, CMSSITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.cmsSite</code> attribute.
	 * @return the cmsSite
	 */
	public CMSSite getCmsSite()
	{
		return getCmsSite( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.cmsSite</code> attribute. 
	 * @param value the cmsSite
	 */
	public void setCmsSite(final SessionContext ctx, final CMSSite value)
	{
		setProperty(ctx, CMSSITE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.cmsSite</code> attribute. 
	 * @param value the cmsSite
	 */
	public void setCmsSite(final CMSSite value)
	{
		setCmsSite( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.companyAddressCity</code> attribute.
	 * @return the companyAddressCity
	 */
	public String getCompanyAddressCity(final SessionContext ctx)
	{
		return (String)getProperty( ctx, COMPANYADDRESSCITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.companyAddressCity</code> attribute.
	 * @return the companyAddressCity
	 */
	public String getCompanyAddressCity()
	{
		return getCompanyAddressCity( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.companyAddressCity</code> attribute. 
	 * @param value the companyAddressCity
	 */
	public void setCompanyAddressCity(final SessionContext ctx, final String value)
	{
		setProperty(ctx, COMPANYADDRESSCITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.companyAddressCity</code> attribute. 
	 * @param value the companyAddressCity
	 */
	public void setCompanyAddressCity(final String value)
	{
		setCompanyAddressCity( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.companyAddressCountry</code> attribute.
	 * @return the companyAddressCountry
	 */
	public Country getCompanyAddressCountry(final SessionContext ctx)
	{
		return (Country)getProperty( ctx, COMPANYADDRESSCOUNTRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.companyAddressCountry</code> attribute.
	 * @return the companyAddressCountry
	 */
	public Country getCompanyAddressCountry()
	{
		return getCompanyAddressCountry( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.companyAddressCountry</code> attribute. 
	 * @param value the companyAddressCountry
	 */
	public void setCompanyAddressCountry(final SessionContext ctx, final Country value)
	{
		setProperty(ctx, COMPANYADDRESSCOUNTRY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.companyAddressCountry</code> attribute. 
	 * @param value the companyAddressCountry
	 */
	public void setCompanyAddressCountry(final Country value)
	{
		setCompanyAddressCountry( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.companyAddressPostalCode</code> attribute.
	 * @return the companyAddressPostalCode
	 */
	public String getCompanyAddressPostalCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, COMPANYADDRESSPOSTALCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.companyAddressPostalCode</code> attribute.
	 * @return the companyAddressPostalCode
	 */
	public String getCompanyAddressPostalCode()
	{
		return getCompanyAddressPostalCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.companyAddressPostalCode</code> attribute. 
	 * @param value the companyAddressPostalCode
	 */
	public void setCompanyAddressPostalCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, COMPANYADDRESSPOSTALCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.companyAddressPostalCode</code> attribute. 
	 * @param value the companyAddressPostalCode
	 */
	public void setCompanyAddressPostalCode(final String value)
	{
		setCompanyAddressPostalCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.companyAddressRegion</code> attribute.
	 * @return the companyAddressRegion
	 */
	public Region getCompanyAddressRegion(final SessionContext ctx)
	{
		return (Region)getProperty( ctx, COMPANYADDRESSREGION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.companyAddressRegion</code> attribute.
	 * @return the companyAddressRegion
	 */
	public Region getCompanyAddressRegion()
	{
		return getCompanyAddressRegion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.companyAddressRegion</code> attribute. 
	 * @param value the companyAddressRegion
	 */
	public void setCompanyAddressRegion(final SessionContext ctx, final Region value)
	{
		setProperty(ctx, COMPANYADDRESSREGION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.companyAddressRegion</code> attribute. 
	 * @param value the companyAddressRegion
	 */
	public void setCompanyAddressRegion(final Region value)
	{
		setCompanyAddressRegion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.companyAddressStreet</code> attribute.
	 * @return the companyAddressStreet
	 */
	public String getCompanyAddressStreet(final SessionContext ctx)
	{
		return (String)getProperty( ctx, COMPANYADDRESSSTREET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.companyAddressStreet</code> attribute.
	 * @return the companyAddressStreet
	 */
	public String getCompanyAddressStreet()
	{
		return getCompanyAddressStreet( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.companyAddressStreet</code> attribute. 
	 * @param value the companyAddressStreet
	 */
	public void setCompanyAddressStreet(final SessionContext ctx, final String value)
	{
		setProperty(ctx, COMPANYADDRESSSTREET,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.companyAddressStreet</code> attribute. 
	 * @param value the companyAddressStreet
	 */
	public void setCompanyAddressStreet(final String value)
	{
		setCompanyAddressStreet( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.companyAddressStreetLine2</code> attribute.
	 * @return the companyAddressStreetLine2
	 */
	public String getCompanyAddressStreetLine2(final SessionContext ctx)
	{
		return (String)getProperty( ctx, COMPANYADDRESSSTREETLINE2);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.companyAddressStreetLine2</code> attribute.
	 * @return the companyAddressStreetLine2
	 */
	public String getCompanyAddressStreetLine2()
	{
		return getCompanyAddressStreetLine2( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.companyAddressStreetLine2</code> attribute. 
	 * @param value the companyAddressStreetLine2
	 */
	public void setCompanyAddressStreetLine2(final SessionContext ctx, final String value)
	{
		setProperty(ctx, COMPANYADDRESSSTREETLINE2,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.companyAddressStreetLine2</code> attribute. 
	 * @param value the companyAddressStreetLine2
	 */
	public void setCompanyAddressStreetLine2(final String value)
	{
		setCompanyAddressStreetLine2( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.companyName</code> attribute.
	 * @return the companyName
	 */
	public String getCompanyName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, COMPANYNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.companyName</code> attribute.
	 * @return the companyName
	 */
	public String getCompanyName()
	{
		return getCompanyName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.companyName</code> attribute. 
	 * @param value the companyName
	 */
	public void setCompanyName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, COMPANYNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.companyName</code> attribute. 
	 * @param value the companyName
	 */
	public void setCompanyName(final String value)
	{
		setCompanyName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.currency</code> attribute.
	 * @return the currency
	 */
	public Currency getCurrency(final SessionContext ctx)
	{
		return (Currency)getProperty( ctx, CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.currency</code> attribute.
	 * @return the currency
	 */
	public Currency getCurrency()
	{
		return getCurrency( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.currency</code> attribute. 
	 * @param value the currency
	 */
	public void setCurrency(final SessionContext ctx, final Currency value)
	{
		setProperty(ctx, CURRENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.currency</code> attribute. 
	 * @param value the currency
	 */
	public void setCurrency(final Currency value)
	{
		setCurrency( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.defaultB2BUnit</code> attribute.
	 * @return the defaultB2BUnit
	 */
	public B2BUnit getDefaultB2BUnit(final SessionContext ctx)
	{
		return (B2BUnit)getProperty( ctx, DEFAULTB2BUNIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.defaultB2BUnit</code> attribute.
	 * @return the defaultB2BUnit
	 */
	public B2BUnit getDefaultB2BUnit()
	{
		return getDefaultB2BUnit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.defaultB2BUnit</code> attribute. 
	 * @param value the defaultB2BUnit
	 */
	public void setDefaultB2BUnit(final SessionContext ctx, final B2BUnit value)
	{
		setProperty(ctx, DEFAULTB2BUNIT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.defaultB2BUnit</code> attribute. 
	 * @param value the defaultB2BUnit
	 */
	public void setDefaultB2BUnit(final B2BUnit value)
	{
		setDefaultB2BUnit( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.email</code> attribute.
	 * @return the email
	 */
	public String getEmail(final SessionContext ctx)
	{
		return (String)getProperty( ctx, EMAIL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.email</code> attribute.
	 * @return the email
	 */
	public String getEmail()
	{
		return getEmail( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.email</code> attribute. 
	 * @param value the email
	 */
	public void setEmail(final SessionContext ctx, final String value)
	{
		setProperty(ctx, EMAIL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.email</code> attribute. 
	 * @param value the email
	 */
	public void setEmail(final String value)
	{
		setEmail( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.language</code> attribute.
	 * @return the language
	 */
	public Language getLanguage(final SessionContext ctx)
	{
		return (Language)getProperty( ctx, LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.language</code> attribute.
	 * @return the language
	 */
	public Language getLanguage()
	{
		return getLanguage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.language</code> attribute. 
	 * @param value the language
	 */
	public void setLanguage(final SessionContext ctx, final Language value)
	{
		setProperty(ctx, LANGUAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.language</code> attribute. 
	 * @param value the language
	 */
	public void setLanguage(final Language value)
	{
		setLanguage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.message</code> attribute.
	 * @return the message
	 */
	public String getMessage(final SessionContext ctx)
	{
		return (String)getProperty( ctx, MESSAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.message</code> attribute.
	 * @return the message
	 */
	public String getMessage()
	{
		return getMessage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.message</code> attribute. 
	 * @param value the message
	 */
	public void setMessage(final SessionContext ctx, final String value)
	{
		setProperty(ctx, MESSAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.message</code> attribute. 
	 * @param value the message
	 */
	public void setMessage(final String value)
	{
		setMessage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.name</code> attribute.
	 * @return the name
	 */
	public String getName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.name</code> attribute.
	 * @return the name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.position</code> attribute.
	 * @return the position
	 */
	public String getPosition(final SessionContext ctx)
	{
		return (String)getProperty( ctx, POSITION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.position</code> attribute.
	 * @return the position
	 */
	public String getPosition()
	{
		return getPosition( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.position</code> attribute. 
	 * @param value the position
	 */
	public void setPosition(final SessionContext ctx, final String value)
	{
		setProperty(ctx, POSITION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.position</code> attribute. 
	 * @param value the position
	 */
	public void setPosition(final String value)
	{
		setPosition( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.rejectReason</code> attribute.
	 * @return the rejectReason
	 */
	public String getRejectReason(final SessionContext ctx)
	{
		return (String)getProperty( ctx, REJECTREASON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.rejectReason</code> attribute.
	 * @return the rejectReason
	 */
	public String getRejectReason()
	{
		return getRejectReason( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.rejectReason</code> attribute. 
	 * @param value the rejectReason
	 */
	public void setRejectReason(final SessionContext ctx, final String value)
	{
		setProperty(ctx, REJECTREASON,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.rejectReason</code> attribute. 
	 * @param value the rejectReason
	 */
	public void setRejectReason(final String value)
	{
		setRejectReason( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.telephone</code> attribute.
	 * @return the telephone
	 */
	public String getTelephone(final SessionContext ctx)
	{
		return (String)getProperty( ctx, TELEPHONE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.telephone</code> attribute.
	 * @return the telephone
	 */
	public String getTelephone()
	{
		return getTelephone( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.telephone</code> attribute. 
	 * @param value the telephone
	 */
	public void setTelephone(final SessionContext ctx, final String value)
	{
		setProperty(ctx, TELEPHONE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.telephone</code> attribute. 
	 * @param value the telephone
	 */
	public void setTelephone(final String value)
	{
		setTelephone( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.telephoneExtension</code> attribute.
	 * @return the telephoneExtension
	 */
	public String getTelephoneExtension(final SessionContext ctx)
	{
		return (String)getProperty( ctx, TELEPHONEEXTENSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.telephoneExtension</code> attribute.
	 * @return the telephoneExtension
	 */
	public String getTelephoneExtension()
	{
		return getTelephoneExtension( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.telephoneExtension</code> attribute. 
	 * @param value the telephoneExtension
	 */
	public void setTelephoneExtension(final SessionContext ctx, final String value)
	{
		setProperty(ctx, TELEPHONEEXTENSION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.telephoneExtension</code> attribute. 
	 * @param value the telephoneExtension
	 */
	public void setTelephoneExtension(final String value)
	{
		setTelephoneExtension( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.title</code> attribute.
	 * @return the title
	 */
	public Title getTitle(final SessionContext ctx)
	{
		return (Title)getProperty( ctx, TITLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BRegistration.title</code> attribute.
	 * @return the title
	 */
	public Title getTitle()
	{
		return getTitle( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.title</code> attribute. 
	 * @param value the title
	 */
	public void setTitle(final SessionContext ctx, final Title value)
	{
		setProperty(ctx, TITLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BRegistration.title</code> attribute. 
	 * @param value the title
	 */
	public void setTitle(final Title value)
	{
		setTitle( getSession().getSessionContext(), value );
	}
	
}
