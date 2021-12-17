/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.jalo;

import de.hybris.platform.apiregistryservices.constants.ApiregistryservicesConstants;
import de.hybris.platform.apiregistryservices.jalo.AbstractCredential;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.webservicescommons.jalo.OAuthClientDetails;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.apiregistryservices.jalo.ExposedOAuthCredential ExposedOAuthCredential}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedExposedOAuthCredential extends AbstractCredential
{
	/** Qualifier of the <code>ExposedOAuthCredential.oAuthClientDetails</code> attribute **/
	public static final String OAUTHCLIENTDETAILS = "oAuthClientDetails";
	/** Qualifier of the <code>ExposedOAuthCredential.password</code> attribute **/
	public static final String PASSWORD = "password";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(AbstractCredential.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(OAUTHCLIENTDETAILS, AttributeMode.INITIAL);
		tmp.put(PASSWORD, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExposedOAuthCredential.oAuthClientDetails</code> attribute.
	 * @return the oAuthClientDetails - OAuth Details
	 */
	public OAuthClientDetails getOAuthClientDetails(final SessionContext ctx)
	{
		return (OAuthClientDetails)getProperty( ctx, OAUTHCLIENTDETAILS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExposedOAuthCredential.oAuthClientDetails</code> attribute.
	 * @return the oAuthClientDetails - OAuth Details
	 */
	public OAuthClientDetails getOAuthClientDetails()
	{
		return getOAuthClientDetails( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ExposedOAuthCredential.oAuthClientDetails</code> attribute. 
	 * @param value the oAuthClientDetails - OAuth Details
	 */
	public void setOAuthClientDetails(final SessionContext ctx, final OAuthClientDetails value)
	{
		setProperty(ctx, OAUTHCLIENTDETAILS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ExposedOAuthCredential.oAuthClientDetails</code> attribute. 
	 * @param value the oAuthClientDetails - OAuth Details
	 */
	public void setOAuthClientDetails(final OAuthClientDetails value)
	{
		setOAuthClientDetails( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExposedOAuthCredential.password</code> attribute.
	 * @return the password - OAuth Password
	 */
	public String getPassword(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PASSWORD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExposedOAuthCredential.password</code> attribute.
	 * @return the password - OAuth Password
	 */
	public String getPassword()
	{
		return getPassword( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ExposedOAuthCredential.password</code> attribute. 
	 * @param value the password - OAuth Password
	 */
	public void setPassword(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PASSWORD,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ExposedOAuthCredential.password</code> attribute. 
	 * @param value the password - OAuth Password
	 */
	public void setPassword(final String value)
	{
		setPassword( getSession().getSessionContext(), value );
	}
	
}
