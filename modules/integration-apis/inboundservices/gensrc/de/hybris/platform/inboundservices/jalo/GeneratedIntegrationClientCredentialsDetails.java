/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.jalo;

import de.hybris.platform.inboundservices.constants.InboundservicesConstants;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.webservicescommons.jalo.OAuthClientDetails;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.webservicescommons.jalo.OAuthClientDetails IntegrationClientCredentialsDetails}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedIntegrationClientCredentialsDetails extends OAuthClientDetails
{
	/** Qualifier of the <code>IntegrationClientCredentialsDetails.user</code> attribute **/
	public static final String USER = "user";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(OAuthClientDetails.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(USER, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IntegrationClientCredentialsDetails.user</code> attribute.
	 * @return the user
	 */
	public Employee getUser(final SessionContext ctx)
	{
		return (Employee)getProperty( ctx, USER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IntegrationClientCredentialsDetails.user</code> attribute.
	 * @return the user
	 */
	public Employee getUser()
	{
		return getUser( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IntegrationClientCredentialsDetails.user</code> attribute. 
	 * @param value the user
	 */
	protected void setUser(final SessionContext ctx, final Employee value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+USER+"' is not changeable", 0 );
		}
		setProperty(ctx, USER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IntegrationClientCredentialsDetails.user</code> attribute. 
	 * @param value the user
	 */
	protected void setUser(final Employee value)
	{
		setUser( getSession().getSessionContext(), value );
	}
	
}
