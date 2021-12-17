/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 15-Dec-2021, 3:07:44 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.enums.SnDocumentOperationType;
import de.hybris.platform.searchservices.model.AbstractSnIndexerItemSourceModel;
import de.hybris.platform.searchservices.model.IncrementalSnIndexerCronJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SnIndexerItemSourceOperation first defined at extension searchservices.
 */
@SuppressWarnings("all")
public class SnIndexerItemSourceOperationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SnIndexerItemSourceOperation";
	
	/**<i>Generated relation code constant for relation <code>SnIncrementalIndexerCronJob2IndexerItemSourceOperation</code> defining source attribute <code>cronJob</code> in extension <code>searchservices</code>.</i>*/
	public static final String _SNINCREMENTALINDEXERCRONJOB2INDEXERITEMSOURCEOPERATION = "SnIncrementalIndexerCronJob2IndexerItemSourceOperation";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexerItemSourceOperation.documentOperationType</code> attribute defined at extension <code>searchservices</code>. */
	public static final String DOCUMENTOPERATIONTYPE = "documentOperationType";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexerItemSourceOperation.indexerItemSource</code> attribute defined at extension <code>searchservices</code>. */
	public static final String INDEXERITEMSOURCE = "indexerItemSource";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexerItemSourceOperation.cronJobPOS</code> attribute defined at extension <code>searchservices</code>. */
	public static final String CRONJOBPOS = "cronJobPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexerItemSourceOperation.cronJob</code> attribute defined at extension <code>searchservices</code>. */
	public static final String CRONJOB = "cronJob";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SnIndexerItemSourceOperationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SnIndexerItemSourceOperationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public SnIndexerItemSourceOperationModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexerItemSourceOperation.cronJob</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the cronJob
	 */
	@Accessor(qualifier = "cronJob", type = Accessor.Type.GETTER)
	public IncrementalSnIndexerCronJobModel getCronJob()
	{
		return getPersistenceContext().getPropertyValue(CRONJOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexerItemSourceOperation.documentOperationType</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the documentOperationType
	 */
	@Accessor(qualifier = "documentOperationType", type = Accessor.Type.GETTER)
	public SnDocumentOperationType getDocumentOperationType()
	{
		return getPersistenceContext().getPropertyValue(DOCUMENTOPERATIONTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexerItemSourceOperation.indexerItemSource</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the indexerItemSource
	 */
	@Accessor(qualifier = "indexerItemSource", type = Accessor.Type.GETTER)
	public AbstractSnIndexerItemSourceModel getIndexerItemSource()
	{
		return getPersistenceContext().getPropertyValue(INDEXERITEMSOURCE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexerItemSourceOperation.cronJob</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the cronJob
	 */
	@Accessor(qualifier = "cronJob", type = Accessor.Type.SETTER)
	public void setCronJob(final IncrementalSnIndexerCronJobModel value)
	{
		getPersistenceContext().setPropertyValue(CRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexerItemSourceOperation.documentOperationType</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the documentOperationType
	 */
	@Accessor(qualifier = "documentOperationType", type = Accessor.Type.SETTER)
	public void setDocumentOperationType(final SnDocumentOperationType value)
	{
		getPersistenceContext().setPropertyValue(DOCUMENTOPERATIONTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexerItemSourceOperation.indexerItemSource</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the indexerItemSource
	 */
	@Accessor(qualifier = "indexerItemSource", type = Accessor.Type.SETTER)
	public void setIndexerItemSource(final AbstractSnIndexerItemSourceModel value)
	{
		getPersistenceContext().setPropertyValue(INDEXERITEMSOURCE, value);
	}
	
}
