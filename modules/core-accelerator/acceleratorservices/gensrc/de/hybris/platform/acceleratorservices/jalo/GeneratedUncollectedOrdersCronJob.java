/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.jalo;

import de.hybris.platform.acceleratorservices.constants.AcceleratorServicesConstants;
import de.hybris.platform.basecommerce.constants.BasecommerceConstants;
import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.acceleratorservices.jalo.UncollectedOrdersCronJob UncollectedOrdersCronJob}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedUncollectedOrdersCronJob extends CronJob
{
	/** Qualifier of the <code>UncollectedOrdersCronJob.sites</code> attribute **/
	public static final String SITES = "sites";
	/**
	* {@link OneToManyHandler} for handling 1:n SITES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<BaseSite> SITESHANDLER = new OneToManyHandler<BaseSite>(
	BasecommerceConstants.TC.BASESITE,
	false,
	"uncollectedOrdersCronJob",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UncollectedOrdersCronJob.sites</code> attribute.
	 * @return the sites
	 */
	public Collection<BaseSite> getSites(final SessionContext ctx)
	{
		return SITESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UncollectedOrdersCronJob.sites</code> attribute.
	 * @return the sites
	 */
	public Collection<BaseSite> getSites()
	{
		return getSites( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UncollectedOrdersCronJob.sites</code> attribute. 
	 * @param value the sites
	 */
	public void setSites(final SessionContext ctx, final Collection<BaseSite> value)
	{
		SITESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UncollectedOrdersCronJob.sites</code> attribute. 
	 * @param value the sites
	 */
	public void setSites(final Collection<BaseSite> value)
	{
		setSites( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to sites. 
	 * @param value the item to add to sites
	 */
	public void addToSites(final SessionContext ctx, final BaseSite value)
	{
		SITESHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to sites. 
	 * @param value the item to add to sites
	 */
	public void addToSites(final BaseSite value)
	{
		addToSites( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from sites. 
	 * @param value the item to remove from sites
	 */
	public void removeFromSites(final SessionContext ctx, final BaseSite value)
	{
		SITESHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from sites. 
	 * @param value the item to remove from sites
	 */
	public void removeFromSites(final BaseSite value)
	{
		removeFromSites( getSession().getSessionContext(), value );
	}
	
}
