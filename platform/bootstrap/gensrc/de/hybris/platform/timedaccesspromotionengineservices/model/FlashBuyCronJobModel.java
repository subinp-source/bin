/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.timedaccesspromotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.timedaccesspromotionengineservices.model.FlashBuyCouponModel;

/**
 * Generated model class for type FlashBuyCronJob first defined at extension timedaccesspromotionengineservices.
 */
@SuppressWarnings("all")
public class FlashBuyCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "FlashBuyCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlashBuyCronJob.flashBuyCoupon</code> attribute defined at extension <code>timedaccesspromotionengineservices</code>. */
	public static final String FLASHBUYCOUPON = "flashBuyCoupon";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public FlashBuyCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public FlashBuyCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public FlashBuyCronJobModel(final JobModel _job)
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
	public FlashBuyCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashBuyCronJob.flashBuyCoupon</code> attribute defined at extension <code>timedaccesspromotionengineservices</code>. 
	 * @return the flashBuyCoupon
	 */
	@Accessor(qualifier = "flashBuyCoupon", type = Accessor.Type.GETTER)
	public FlashBuyCouponModel getFlashBuyCoupon()
	{
		return getPersistenceContext().getPropertyValue(FLASHBUYCOUPON);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlashBuyCronJob.flashBuyCoupon</code> attribute defined at extension <code>timedaccesspromotionengineservices</code>. 
	 *  
	 * @param value the flashBuyCoupon
	 */
	@Accessor(qualifier = "flashBuyCoupon", type = Accessor.Type.SETTER)
	public void setFlashBuyCoupon(final FlashBuyCouponModel value)
	{
		getPersistenceContext().setPropertyValue(FLASHBUYCOUPON, value);
	}
	
}
