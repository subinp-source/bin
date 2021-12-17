/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.jalo;

import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.searchservices.jalo.AbstractSnIndexerItemSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem FlexibleSearchSnIndexerItemSource}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedFlexibleSearchSnIndexerItemSource extends AbstractSnIndexerItemSource
{
	/** Qualifier of the <code>FlexibleSearchSnIndexerItemSource.query</code> attribute **/
	public static final String QUERY = "query";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(AbstractSnIndexerItemSource.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(QUERY, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlexibleSearchSnIndexerItemSource.query</code> attribute.
	 * @return the query
	 */
	public String getQuery(final SessionContext ctx)
	{
		return (String)getProperty( ctx, QUERY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlexibleSearchSnIndexerItemSource.query</code> attribute.
	 * @return the query
	 */
	public String getQuery()
	{
		return getQuery( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FlexibleSearchSnIndexerItemSource.query</code> attribute. 
	 * @param value the query
	 */
	public void setQuery(final SessionContext ctx, final String value)
	{
		setProperty(ctx, QUERY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FlexibleSearchSnIndexerItemSource.query</code> attribute. 
	 * @param value the query
	 */
	public void setQuery(final String value)
	{
		setQuery( getSession().getSessionContext(), value );
	}
	
}
