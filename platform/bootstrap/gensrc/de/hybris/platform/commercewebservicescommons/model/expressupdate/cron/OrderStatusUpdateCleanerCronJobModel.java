/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.model.expressupdate.cron;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type OrderStatusUpdateCleanerCronJob first defined at extension commercewebservicescommons.
 */
@SuppressWarnings("all")
public class OrderStatusUpdateCleanerCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OrderStatusUpdateCleanerCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderStatusUpdateCleanerCronJob.queueTimeLimit</code> attribute defined at extension <code>commercewebservicescommons</code>. */
	public static final String QUEUETIMELIMIT = "queueTimeLimit";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OrderStatusUpdateCleanerCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OrderStatusUpdateCleanerCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public OrderStatusUpdateCleanerCronJobModel(final JobModel _job)
	{
		super();
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public OrderStatusUpdateCleanerCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderStatusUpdateCleanerCronJob.queueTimeLimit</code> attribute defined at extension <code>commercewebservicescommons</code>. 
	 * @return the queueTimeLimit - Only elements older than specified value (in minutes) will be removed from the queue
	 */
	@Accessor(qualifier = "queueTimeLimit", type = Accessor.Type.GETTER)
	public Integer getQueueTimeLimit()
	{
		return getPersistenceContext().getPropertyValue(QUEUETIMELIMIT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderStatusUpdateCleanerCronJob.queueTimeLimit</code> attribute defined at extension <code>commercewebservicescommons</code>. 
	 *  
	 * @param value the queueTimeLimit - Only elements older than specified value (in minutes) will be removed from the queue
	 */
	@Accessor(qualifier = "queueTimeLimit", type = Accessor.Type.SETTER)
	public void setQueueTimeLimit(final Integer value)
	{
		getPersistenceContext().setPropertyValue(QUEUETIMELIMIT, value);
	}
	
}
