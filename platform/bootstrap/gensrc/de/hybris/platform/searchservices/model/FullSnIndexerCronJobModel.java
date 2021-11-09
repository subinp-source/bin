/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.searchservices.model.AbstractSnIndexerCronJobModel;
import de.hybris.platform.searchservices.model.AbstractSnIndexerItemSourceModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type FullSnIndexerCronJob first defined at extension searchservices.
 */
@SuppressWarnings("all")
public class FullSnIndexerCronJobModel extends AbstractSnIndexerCronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "FullSnIndexerCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>FullSnIndexerCronJob.indexerItemSource</code> attribute defined at extension <code>searchservices</code>. */
	public static final String INDEXERITEMSOURCE = "indexerItemSource";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public FullSnIndexerCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public FullSnIndexerCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _indexType initial attribute declared by type <code>AbstractSnIndexerCronJob</code> at extension <code>searchservices</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public FullSnIndexerCronJobModel(final SnIndexTypeModel _indexType, final JobModel _job)
	{
		super();
		setIndexType(_indexType);
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _indexType initial attribute declared by type <code>AbstractSnIndexerCronJob</code> at extension <code>searchservices</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public FullSnIndexerCronJobModel(final SnIndexTypeModel _indexType, final JobModel _job, final ItemModel _owner)
	{
		super();
		setIndexType(_indexType);
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FullSnIndexerCronJob.indexerItemSource</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the indexerItemSource
	 */
	@Accessor(qualifier = "indexerItemSource", type = Accessor.Type.GETTER)
	public AbstractSnIndexerItemSourceModel getIndexerItemSource()
	{
		return getPersistenceContext().getPropertyValue(INDEXERITEMSOURCE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FullSnIndexerCronJob.indexerItemSource</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the indexerItemSource
	 */
	@Accessor(qualifier = "indexerItemSource", type = Accessor.Type.SETTER)
	public void setIndexerItemSource(final AbstractSnIndexerItemSourceModel value)
	{
		getPersistenceContext().setPropertyValue(INDEXERITEMSOURCE, value);
	}
	
}
