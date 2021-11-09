/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchprovidercssearchservices.jalo;

import de.hybris.platform.apiregistryservices.jalo.ConsumedDestination;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.searchprovidercssearchservices.constants.SearchprovidercssearchservicesConstants;
import de.hybris.platform.searchservices.jalo.AbstractSnSearchProviderConfiguration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem CSSearchSnSearchProviderConfiguration}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedCSSearchSnSearchProviderConfiguration extends AbstractSnSearchProviderConfiguration
{
	/** Qualifier of the <code>CSSearchSnSearchProviderConfiguration.consumedDestination</code> attribute **/
	public static final String CONSUMEDDESTINATION = "consumedDestination";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(AbstractSnSearchProviderConfiguration.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(CONSUMEDDESTINATION, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CSSearchSnSearchProviderConfiguration.consumedDestination</code> attribute.
	 * @return the consumedDestination
	 */
	public ConsumedDestination getConsumedDestination(final SessionContext ctx)
	{
		return (ConsumedDestination)getProperty( ctx, CONSUMEDDESTINATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CSSearchSnSearchProviderConfiguration.consumedDestination</code> attribute.
	 * @return the consumedDestination
	 */
	public ConsumedDestination getConsumedDestination()
	{
		return getConsumedDestination( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CSSearchSnSearchProviderConfiguration.consumedDestination</code> attribute. 
	 * @param value the consumedDestination
	 */
	public void setConsumedDestination(final SessionContext ctx, final ConsumedDestination value)
	{
		setProperty(ctx, CONSUMEDDESTINATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CSSearchSnSearchProviderConfiguration.consumedDestination</code> attribute. 
	 * @param value the consumedDestination
	 */
	public void setConsumedDestination(final ConsumedDestination value)
	{
		setConsumedDestination( getSession().getSessionContext(), value );
	}
	
}
