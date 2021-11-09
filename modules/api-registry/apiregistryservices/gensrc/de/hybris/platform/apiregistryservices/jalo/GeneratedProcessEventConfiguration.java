/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.jalo;

import de.hybris.platform.apiregistryservices.constants.ApiregistryservicesConstants;
import de.hybris.platform.apiregistryservices.jalo.events.EventConfiguration;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.apiregistryservices.jalo.ProcessEventConfiguration ProcessEventConfiguration}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedProcessEventConfiguration extends EventConfiguration
{
	/** Qualifier of the <code>ProcessEventConfiguration.process</code> attribute **/
	public static final String PROCESS = "process";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(EventConfiguration.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(PROCESS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProcessEventConfiguration.process</code> attribute.
	 * @return the process - Fully qualified classname of business process
	 */
	public String getProcess(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PROCESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProcessEventConfiguration.process</code> attribute.
	 * @return the process - Fully qualified classname of business process
	 */
	public String getProcess()
	{
		return getProcess( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProcessEventConfiguration.process</code> attribute. 
	 * @param value the process - Fully qualified classname of business process
	 */
	public void setProcess(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PROCESS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProcessEventConfiguration.process</code> attribute. 
	 * @param value the process - Fully qualified classname of business process
	 */
	public void setProcess(final String value)
	{
		setProcess( getSession().getSessionContext(), value );
	}
	
}
