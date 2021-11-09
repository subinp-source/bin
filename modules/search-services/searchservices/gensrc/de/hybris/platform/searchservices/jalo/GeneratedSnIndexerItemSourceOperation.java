/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.searchservices.jalo.AbstractSnIndexerItemSource;
import de.hybris.platform.searchservices.jalo.IncrementalSnIndexerCronJob;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.PartOfHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem SnIndexerItemSourceOperation}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSnIndexerItemSourceOperation extends GenericItem
{
	/** Qualifier of the <code>SnIndexerItemSourceOperation.documentOperationType</code> attribute **/
	public static final String DOCUMENTOPERATIONTYPE = "documentOperationType";
	/** Qualifier of the <code>SnIndexerItemSourceOperation.indexerItemSource</code> attribute **/
	public static final String INDEXERITEMSOURCE = "indexerItemSource";
	/** Qualifier of the <code>SnIndexerItemSourceOperation.cronJobPOS</code> attribute **/
	public static final String CRONJOBPOS = "cronJobPOS";
	/** Qualifier of the <code>SnIndexerItemSourceOperation.cronJob</code> attribute **/
	public static final String CRONJOB = "cronJob";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n CRONJOB's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedSnIndexerItemSourceOperation> CRONJOBHANDLER = new BidirectionalOneToManyHandler<GeneratedSnIndexerItemSourceOperation>(
	SearchservicesConstants.TC.SNINDEXERITEMSOURCEOPERATION,
	false,
	"cronJob",
	"cronJobPOS",
	true,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(DOCUMENTOPERATIONTYPE, AttributeMode.INITIAL);
		tmp.put(INDEXERITEMSOURCE, AttributeMode.INITIAL);
		tmp.put(CRONJOBPOS, AttributeMode.INITIAL);
		tmp.put(CRONJOB, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		CRONJOBHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexerItemSourceOperation.cronJob</code> attribute.
	 * @return the cronJob
	 */
	public IncrementalSnIndexerCronJob getCronJob(final SessionContext ctx)
	{
		return (IncrementalSnIndexerCronJob)getProperty( ctx, CRONJOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexerItemSourceOperation.cronJob</code> attribute.
	 * @return the cronJob
	 */
	public IncrementalSnIndexerCronJob getCronJob()
	{
		return getCronJob( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexerItemSourceOperation.cronJob</code> attribute. 
	 * @param value the cronJob
	 */
	public void setCronJob(final SessionContext ctx, final IncrementalSnIndexerCronJob value)
	{
		CRONJOBHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexerItemSourceOperation.cronJob</code> attribute. 
	 * @param value the cronJob
	 */
	public void setCronJob(final IncrementalSnIndexerCronJob value)
	{
		setCronJob( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexerItemSourceOperation.cronJobPOS</code> attribute.
	 * @return the cronJobPOS
	 */
	 Integer getCronJobPOS(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CRONJOBPOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexerItemSourceOperation.cronJobPOS</code> attribute.
	 * @return the cronJobPOS
	 */
	 Integer getCronJobPOS()
	{
		return getCronJobPOS( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexerItemSourceOperation.cronJobPOS</code> attribute. 
	 * @return the cronJobPOS
	 */
	 int getCronJobPOSAsPrimitive(final SessionContext ctx)
	{
		Integer value = getCronJobPOS( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexerItemSourceOperation.cronJobPOS</code> attribute. 
	 * @return the cronJobPOS
	 */
	 int getCronJobPOSAsPrimitive()
	{
		return getCronJobPOSAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexerItemSourceOperation.cronJobPOS</code> attribute. 
	 * @param value the cronJobPOS
	 */
	 void setCronJobPOS(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CRONJOBPOS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexerItemSourceOperation.cronJobPOS</code> attribute. 
	 * @param value the cronJobPOS
	 */
	 void setCronJobPOS(final Integer value)
	{
		setCronJobPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexerItemSourceOperation.cronJobPOS</code> attribute. 
	 * @param value the cronJobPOS
	 */
	 void setCronJobPOS(final SessionContext ctx, final int value)
	{
		setCronJobPOS( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexerItemSourceOperation.cronJobPOS</code> attribute. 
	 * @param value the cronJobPOS
	 */
	 void setCronJobPOS(final int value)
	{
		setCronJobPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexerItemSourceOperation.documentOperationType</code> attribute.
	 * @return the documentOperationType
	 */
	public EnumerationValue getDocumentOperationType(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, DOCUMENTOPERATIONTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexerItemSourceOperation.documentOperationType</code> attribute.
	 * @return the documentOperationType
	 */
	public EnumerationValue getDocumentOperationType()
	{
		return getDocumentOperationType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexerItemSourceOperation.documentOperationType</code> attribute. 
	 * @param value the documentOperationType
	 */
	public void setDocumentOperationType(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, DOCUMENTOPERATIONTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexerItemSourceOperation.documentOperationType</code> attribute. 
	 * @param value the documentOperationType
	 */
	public void setDocumentOperationType(final EnumerationValue value)
	{
		setDocumentOperationType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexerItemSourceOperation.indexerItemSource</code> attribute.
	 * @return the indexerItemSource
	 */
	public AbstractSnIndexerItemSource getIndexerItemSource(final SessionContext ctx)
	{
		return (AbstractSnIndexerItemSource)getProperty( ctx, INDEXERITEMSOURCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexerItemSourceOperation.indexerItemSource</code> attribute.
	 * @return the indexerItemSource
	 */
	public AbstractSnIndexerItemSource getIndexerItemSource()
	{
		return getIndexerItemSource( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexerItemSourceOperation.indexerItemSource</code> attribute. 
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
	 * <i>Generated method</i> - Setter of the <code>SnIndexerItemSourceOperation.indexerItemSource</code> attribute. 
	 * @param value the indexerItemSource
	 */
	public void setIndexerItemSource(final AbstractSnIndexerItemSource value)
	{
		setIndexerItemSource( getSession().getSessionContext(), value );
	}
	
}
