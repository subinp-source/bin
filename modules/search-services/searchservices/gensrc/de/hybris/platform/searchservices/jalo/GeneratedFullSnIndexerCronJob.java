/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.jalo;

import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.searchservices.jalo.AbstractSnIndexerCronJob;
import de.hybris.platform.searchservices.jalo.AbstractSnIndexerItemSource;
import de.hybris.platform.util.PartOfHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cronjob.jalo.CronJob FullSnIndexerCronJob}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedFullSnIndexerCronJob extends AbstractSnIndexerCronJob
{
	/** Qualifier of the <code>FullSnIndexerCronJob.indexerItemSource</code> attribute **/
	public static final String INDEXERITEMSOURCE = "indexerItemSource";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(AbstractSnIndexerCronJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(INDEXERITEMSOURCE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FullSnIndexerCronJob.indexerItemSource</code> attribute.
	 * @return the indexerItemSource
	 */
	public AbstractSnIndexerItemSource getIndexerItemSource(final SessionContext ctx)
	{
		return (AbstractSnIndexerItemSource)getProperty( ctx, INDEXERITEMSOURCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FullSnIndexerCronJob.indexerItemSource</code> attribute.
	 * @return the indexerItemSource
	 */
	public AbstractSnIndexerItemSource getIndexerItemSource()
	{
		return getIndexerItemSource( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FullSnIndexerCronJob.indexerItemSource</code> attribute. 
	 * @param value the indexerItemSource
	 */
	public void setIndexerItemSource(final SessionContext ctx, final AbstractSnIndexerItemSource value)
	{
		new PartOfHandler<AbstractSnIndexerItemSource>()
		{
			@Override
			protected AbstractSnIndexerItemSource doGetValue(final SessionContext ctx)
			{
				return getIndexerItemSource( ctx );
			}
			@Override
			protected void doSetValue(final SessionContext ctx, final AbstractSnIndexerItemSource _value)
			{
				final AbstractSnIndexerItemSource value = _value;
				setProperty(ctx, INDEXERITEMSOURCE,value);
			}
		}.setValue( ctx, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FullSnIndexerCronJob.indexerItemSource</code> attribute. 
	 * @param value the indexerItemSource
	 */
	public void setIndexerItemSource(final AbstractSnIndexerItemSource value)
	{
		setIndexerItemSource( getSession().getSessionContext(), value );
	}
	
}
