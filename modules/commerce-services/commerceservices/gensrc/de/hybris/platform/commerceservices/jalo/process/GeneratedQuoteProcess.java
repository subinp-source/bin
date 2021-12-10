/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.jalo.process;

import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.processengine.jalo.BusinessProcess;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.process.QuoteProcess QuoteProcess}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedQuoteProcess extends BusinessProcess
{
	/** Qualifier of the <code>QuoteProcess.quoteCode</code> attribute **/
	public static final String QUOTECODE = "quoteCode";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(BusinessProcess.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(QUOTECODE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>QuoteProcess.quoteCode</code> attribute.
	 * @return the quoteCode - Quote model submitted by the buyer/sales rep.
	 */
	public String getQuoteCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, QUOTECODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>QuoteProcess.quoteCode</code> attribute.
	 * @return the quoteCode - Quote model submitted by the buyer/sales rep.
	 */
	public String getQuoteCode()
	{
		return getQuoteCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>QuoteProcess.quoteCode</code> attribute. 
	 * @param value the quoteCode - Quote model submitted by the buyer/sales rep.
	 */
	public void setQuoteCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, QUOTECODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>QuoteProcess.quoteCode</code> attribute. 
	 * @param value the quoteCode - Quote model submitted by the buyer/sales rep.
	 */
	public void setQuoteCode(final String value)
	{
		setQuoteCode( getSession().getSessionContext(), value );
	}
	
}
