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
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.searchservices.jalo.AbstractSnIndexerCronJob;
import de.hybris.platform.searchservices.jalo.SnIndexerItemSourceOperation;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cronjob.jalo.CronJob IncrementalSnIndexerCronJob}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedIncrementalSnIndexerCronJob extends AbstractSnIndexerCronJob
{
	/** Qualifier of the <code>IncrementalSnIndexerCronJob.indexerItemSourceOperations</code> attribute **/
	public static final String INDEXERITEMSOURCEOPERATIONS = "indexerItemSourceOperations";
	/**
	* {@link OneToManyHandler} for handling 1:n INDEXERITEMSOURCEOPERATIONS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<SnIndexerItemSourceOperation> INDEXERITEMSOURCEOPERATIONSHANDLER = new OneToManyHandler<SnIndexerItemSourceOperation>(
	SearchservicesConstants.TC.SNINDEXERITEMSOURCEOPERATION,
	true,
	"cronJob",
	"cronJobPOS",
	true,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(AbstractSnIndexerCronJob.DEFAULT_INITIAL_ATTRIBUTES);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IncrementalSnIndexerCronJob.indexerItemSourceOperations</code> attribute.
	 * @return the indexerItemSourceOperations
	 */
	public List<SnIndexerItemSourceOperation> getIndexerItemSourceOperations(final SessionContext ctx)
	{
		return (List<SnIndexerItemSourceOperation>)INDEXERITEMSOURCEOPERATIONSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IncrementalSnIndexerCronJob.indexerItemSourceOperations</code> attribute.
	 * @return the indexerItemSourceOperations
	 */
	public List<SnIndexerItemSourceOperation> getIndexerItemSourceOperations()
	{
		return getIndexerItemSourceOperations( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IncrementalSnIndexerCronJob.indexerItemSourceOperations</code> attribute. 
	 * @param value the indexerItemSourceOperations
	 */
	public void setIndexerItemSourceOperations(final SessionContext ctx, final List<SnIndexerItemSourceOperation> value)
	{
		INDEXERITEMSOURCEOPERATIONSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IncrementalSnIndexerCronJob.indexerItemSourceOperations</code> attribute. 
	 * @param value the indexerItemSourceOperations
	 */
	public void setIndexerItemSourceOperations(final List<SnIndexerItemSourceOperation> value)
	{
		setIndexerItemSourceOperations( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to indexerItemSourceOperations. 
	 * @param value the item to add to indexerItemSourceOperations
	 */
	public void addToIndexerItemSourceOperations(final SessionContext ctx, final SnIndexerItemSourceOperation value)
	{
		INDEXERITEMSOURCEOPERATIONSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to indexerItemSourceOperations. 
	 * @param value the item to add to indexerItemSourceOperations
	 */
	public void addToIndexerItemSourceOperations(final SnIndexerItemSourceOperation value)
	{
		addToIndexerItemSourceOperations( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from indexerItemSourceOperations. 
	 * @param value the item to remove from indexerItemSourceOperations
	 */
	public void removeFromIndexerItemSourceOperations(final SessionContext ctx, final SnIndexerItemSourceOperation value)
	{
		INDEXERITEMSOURCEOPERATIONSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from indexerItemSourceOperations. 
	 * @param value the item to remove from indexerItemSourceOperations
	 */
	public void removeFromIndexerItemSourceOperations(final SnIndexerItemSourceOperation value)
	{
		removeFromIndexerItemSourceOperations( getSession().getSessionContext(), value );
	}
	
}
