/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.jalo.process;

import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.process.ForgottenPasswordProcess ForgottenPasswordProcess}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedForgottenPasswordProcess extends StoreFrontCustomerProcess
{
	/** Qualifier of the <code>ForgottenPasswordProcess.token</code> attribute **/
	public static final String TOKEN = "token";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(StoreFrontCustomerProcess.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(TOKEN, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ForgottenPasswordProcess.token</code> attribute.
	 * @return the token - Attribute contains token that is used in this process.
	 */
	public String getToken(final SessionContext ctx)
	{
		return (String)getProperty( ctx, TOKEN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ForgottenPasswordProcess.token</code> attribute.
	 * @return the token - Attribute contains token that is used in this process.
	 */
	public String getToken()
	{
		return getToken( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ForgottenPasswordProcess.token</code> attribute. 
	 * @param value the token - Attribute contains token that is used in this process.
	 */
	public void setToken(final SessionContext ctx, final String value)
	{
		setProperty(ctx, TOKEN,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ForgottenPasswordProcess.token</code> attribute. 
	 * @param value the token - Attribute contains token that is used in this process.
	 */
	public void setToken(final String value)
	{
		setToken( getSession().getSessionContext(), value );
	}
	
}
