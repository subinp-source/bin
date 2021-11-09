/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.model.payment;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type OldPaymentSubscriptionResultRemovalCronJob first defined at extension commercewebservicescommons.
 */
@SuppressWarnings("all")
public class OldPaymentSubscriptionResultRemovalCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OldPaymentSubscriptionResultRemovalCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>OldPaymentSubscriptionResultRemovalCronJob.age</code> attribute defined at extension <code>commercewebservicescommons</code>. */
	public static final String AGE = "age";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OldPaymentSubscriptionResultRemovalCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OldPaymentSubscriptionResultRemovalCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public OldPaymentSubscriptionResultRemovalCronJobModel(final JobModel _job)
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
	public OldPaymentSubscriptionResultRemovalCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OldPaymentSubscriptionResultRemovalCronJob.age</code> attribute defined at extension <code>commercewebservicescommons</code>. 
	 * @return the age - After specified number of seconds payment subscription result will be cleaned up. Default is 24 hour.
	 */
	@Accessor(qualifier = "age", type = Accessor.Type.GETTER)
	public Integer getAge()
	{
		return getPersistenceContext().getPropertyValue(AGE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OldPaymentSubscriptionResultRemovalCronJob.age</code> attribute defined at extension <code>commercewebservicescommons</code>. 
	 *  
	 * @param value the age - After specified number of seconds payment subscription result will be cleaned up. Default is 24 hour.
	 */
	@Accessor(qualifier = "age", type = Accessor.Type.SETTER)
	public void setAge(final Integer value)
	{
		getPersistenceContext().setPropertyValue(AGE, value);
	}
	
}
