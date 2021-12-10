/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type AbstractSnIndexerCronJob first defined at extension searchservices.
 */
@SuppressWarnings("all")
public class AbstractSnIndexerCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractSnIndexerCronJob";
	
	/**<i>Generated relation code constant for relation <code>SnIndexType2IndexerCronJob</code> defining source attribute <code>indexType</code> in extension <code>searchservices</code>.</i>*/
	public static final String _SNINDEXTYPE2INDEXERCRONJOB = "SnIndexType2IndexerCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractSnIndexerCronJob.lastSuccessfulStartTime</code> attribute defined at extension <code>searchservices</code>. */
	public static final String LASTSUCCESSFULSTARTTIME = "lastSuccessfulStartTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractSnIndexerCronJob.indexTypePOS</code> attribute defined at extension <code>searchservices</code>. */
	public static final String INDEXTYPEPOS = "indexTypePOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractSnIndexerCronJob.indexType</code> attribute defined at extension <code>searchservices</code>. */
	public static final String INDEXTYPE = "indexType";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractSnIndexerCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractSnIndexerCronJobModel(final ItemModelContext ctx)
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
	public AbstractSnIndexerCronJobModel(final SnIndexTypeModel _indexType, final JobModel _job)
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
	public AbstractSnIndexerCronJobModel(final SnIndexTypeModel _indexType, final JobModel _job, final ItemModel _owner)
	{
		super();
		setIndexType(_indexType);
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnIndexerCronJob.indexType</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the indexType
	 */
	@Accessor(qualifier = "indexType", type = Accessor.Type.GETTER)
	public SnIndexTypeModel getIndexType()
	{
		return getPersistenceContext().getPropertyValue(INDEXTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnIndexerCronJob.lastSuccessfulStartTime</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the lastSuccessfulStartTime
	 */
	@Accessor(qualifier = "lastSuccessfulStartTime", type = Accessor.Type.GETTER)
	public Date getLastSuccessfulStartTime()
	{
		return getPersistenceContext().getPropertyValue(LASTSUCCESSFULSTARTTIME);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractSnIndexerCronJob.indexType</code> attribute defined at extension <code>searchservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the indexType
	 */
	@Accessor(qualifier = "indexType", type = Accessor.Type.SETTER)
	public void setIndexType(final SnIndexTypeModel value)
	{
		getPersistenceContext().setPropertyValue(INDEXTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractSnIndexerCronJob.lastSuccessfulStartTime</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the lastSuccessfulStartTime
	 */
	@Accessor(qualifier = "lastSuccessfulStartTime", type = Accessor.Type.SETTER)
	public void setLastSuccessfulStartTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(LASTSUCCESSFULSTARTTIME, value);
	}
	
}
