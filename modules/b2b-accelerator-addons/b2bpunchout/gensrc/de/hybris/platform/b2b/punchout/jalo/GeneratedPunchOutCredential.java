/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout.jalo;

import de.hybris.platform.b2b.punchout.constants.B2bpunchoutConstants;
import de.hybris.platform.b2b.punchout.jalo.B2BCustomerPunchOutCredentialMapping;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem PunchOutCredential}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedPunchOutCredential extends GenericItem
{
	/** Qualifier of the <code>PunchOutCredential.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>PunchOutCredential.identity</code> attribute **/
	public static final String IDENTITY = "identity";
	/** Qualifier of the <code>PunchOutCredential.domain</code> attribute **/
	public static final String DOMAIN = "domain";
	/** Qualifier of the <code>PunchOutCredential.sharedsecret</code> attribute **/
	public static final String SHAREDSECRET = "sharedsecret";
	/** Qualifier of the <code>PunchOutCredential.B2BCustomerPunchOutCredentialMapping</code> attribute **/
	public static final String B2BCUSTOMERPUNCHOUTCREDENTIALMAPPING = "B2BCustomerPunchOutCredentialMapping";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n B2BCUSTOMERPUNCHOUTCREDENTIALMAPPING's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedPunchOutCredential> B2BCUSTOMERPUNCHOUTCREDENTIALMAPPINGHANDLER = new BidirectionalOneToManyHandler<GeneratedPunchOutCredential>(
	B2bpunchoutConstants.TC.PUNCHOUTCREDENTIAL,
	false,
	"B2BCustomerPunchOutCredentialMapping",
	null,
	false,
	true,
	CollectionType.SET
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(IDENTITY, AttributeMode.INITIAL);
		tmp.put(DOMAIN, AttributeMode.INITIAL);
		tmp.put(SHAREDSECRET, AttributeMode.INITIAL);
		tmp.put(B2BCUSTOMERPUNCHOUTCREDENTIALMAPPING, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PunchOutCredential.B2BCustomerPunchOutCredentialMapping</code> attribute.
	 * @return the B2BCustomerPunchOutCredentialMapping
	 */
	public B2BCustomerPunchOutCredentialMapping getB2BCustomerPunchOutCredentialMapping(final SessionContext ctx)
	{
		return (B2BCustomerPunchOutCredentialMapping)getProperty( ctx, B2BCUSTOMERPUNCHOUTCREDENTIALMAPPING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PunchOutCredential.B2BCustomerPunchOutCredentialMapping</code> attribute.
	 * @return the B2BCustomerPunchOutCredentialMapping
	 */
	public B2BCustomerPunchOutCredentialMapping getB2BCustomerPunchOutCredentialMapping()
	{
		return getB2BCustomerPunchOutCredentialMapping( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PunchOutCredential.B2BCustomerPunchOutCredentialMapping</code> attribute. 
	 * @param value the B2BCustomerPunchOutCredentialMapping
	 */
	public void setB2BCustomerPunchOutCredentialMapping(final SessionContext ctx, final B2BCustomerPunchOutCredentialMapping value)
	{
		B2BCUSTOMERPUNCHOUTCREDENTIALMAPPINGHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PunchOutCredential.B2BCustomerPunchOutCredentialMapping</code> attribute. 
	 * @param value the B2BCustomerPunchOutCredentialMapping
	 */
	public void setB2BCustomerPunchOutCredentialMapping(final B2BCustomerPunchOutCredentialMapping value)
	{
		setB2BCustomerPunchOutCredentialMapping( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PunchOutCredential.code</code> attribute.
	 * @return the code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PunchOutCredential.code</code> attribute.
	 * @return the code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PunchOutCredential.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PunchOutCredential.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		B2BCUSTOMERPUNCHOUTCREDENTIALMAPPINGHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PunchOutCredential.domain</code> attribute.
	 * @return the domain
	 */
	public String getDomain(final SessionContext ctx)
	{
		return (String)getProperty( ctx, DOMAIN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PunchOutCredential.domain</code> attribute.
	 * @return the domain
	 */
	public String getDomain()
	{
		return getDomain( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PunchOutCredential.domain</code> attribute. 
	 * @param value the domain
	 */
	public void setDomain(final SessionContext ctx, final String value)
	{
		setProperty(ctx, DOMAIN,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PunchOutCredential.domain</code> attribute. 
	 * @param value the domain
	 */
	public void setDomain(final String value)
	{
		setDomain( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PunchOutCredential.identity</code> attribute.
	 * @return the identity
	 */
	public String getIdentity(final SessionContext ctx)
	{
		return (String)getProperty( ctx, IDENTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PunchOutCredential.identity</code> attribute.
	 * @return the identity
	 */
	public String getIdentity()
	{
		return getIdentity( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PunchOutCredential.identity</code> attribute. 
	 * @param value the identity
	 */
	public void setIdentity(final SessionContext ctx, final String value)
	{
		setProperty(ctx, IDENTITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PunchOutCredential.identity</code> attribute. 
	 * @param value the identity
	 */
	public void setIdentity(final String value)
	{
		setIdentity( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PunchOutCredential.sharedsecret</code> attribute.
	 * @return the sharedsecret
	 */
	public String getSharedsecret(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SHAREDSECRET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PunchOutCredential.sharedsecret</code> attribute.
	 * @return the sharedsecret
	 */
	public String getSharedsecret()
	{
		return getSharedsecret( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PunchOutCredential.sharedsecret</code> attribute. 
	 * @param value the sharedsecret
	 */
	public void setSharedsecret(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SHAREDSECRET,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PunchOutCredential.sharedsecret</code> attribute. 
	 * @param value the sharedsecret
	 */
	public void setSharedsecret(final String value)
	{
		setSharedsecret( getSession().getSessionContext(), value );
	}
	
}
