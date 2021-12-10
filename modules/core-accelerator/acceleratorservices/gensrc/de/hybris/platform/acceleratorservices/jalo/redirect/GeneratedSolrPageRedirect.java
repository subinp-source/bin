/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.jalo.redirect;

import de.hybris.platform.acceleratorservices.constants.AcceleratorServicesConstants;
import de.hybris.platform.cms2.jalo.pages.AbstractPage;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.solrfacetsearch.jalo.redirect.SolrAbstractKeywordRedirect;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.acceleratorservices.jalo.redirect.SolrPageRedirect SolrPageRedirect}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSolrPageRedirect extends SolrAbstractKeywordRedirect
{
	/** Qualifier of the <code>SolrPageRedirect.redirectItem</code> attribute **/
	public static final String REDIRECTITEM = "redirectItem";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SolrAbstractKeywordRedirect.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(REDIRECTITEM, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrPageRedirect.redirectItem</code> attribute.
	 * @return the redirectItem
	 */
	public AbstractPage getRedirectItem(final SessionContext ctx)
	{
		return (AbstractPage)getProperty( ctx, REDIRECTITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrPageRedirect.redirectItem</code> attribute.
	 * @return the redirectItem
	 */
	public AbstractPage getRedirectItem()
	{
		return getRedirectItem( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrPageRedirect.redirectItem</code> attribute. 
	 * @param value the redirectItem
	 */
	protected void setRedirectItem(final SessionContext ctx, final AbstractPage value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+REDIRECTITEM+"' is not changeable", 0 );
		}
		setProperty(ctx, REDIRECTITEM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrPageRedirect.redirectItem</code> attribute. 
	 * @param value the redirectItem
	 */
	protected void setRedirectItem(final AbstractPage value)
	{
		setRedirectItem( getSession().getSessionContext(), value );
	}
	
}
