/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 15-Dec-2021, 3:07:44 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.services.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.sap.productconfig.services.enums.ProductConfigurationPersistenceCleanUpMode;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ProductConfigurationPersistenceCleanUpCronJob first defined at extension sapproductconfigservices.
 */
@SuppressWarnings("all")
public class ProductConfigurationPersistenceCleanUpCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductConfigurationPersistenceCleanUpCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductConfigurationPersistenceCleanUpCronJob.thresholdDays</code> attribute defined at extension <code>sapproductconfigservices</code>. */
	public static final String THRESHOLDDAYS = "thresholdDays";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductConfigurationPersistenceCleanUpCronJob.cleanUpMode</code> attribute defined at extension <code>sapproductconfigservices</code>. */
	public static final String CLEANUPMODE = "cleanUpMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductConfigurationPersistenceCleanUpCronJob.baseSite</code> attribute defined at extension <code>sapproductconfigservices</code>. */
	public static final String BASESITE = "baseSite";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductConfigurationPersistenceCleanUpCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductConfigurationPersistenceCleanUpCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ProductConfigurationPersistenceCleanUpCronJobModel(final JobModel _job)
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
	public ProductConfigurationPersistenceCleanUpCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationPersistenceCleanUpCronJob.baseSite</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 * @return the baseSite
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.GETTER)
	public BaseSiteModel getBaseSite()
	{
		return getPersistenceContext().getPropertyValue(BASESITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationPersistenceCleanUpCronJob.cleanUpMode</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 * @return the cleanUpMode
	 */
	@Accessor(qualifier = "cleanUpMode", type = Accessor.Type.GETTER)
	public ProductConfigurationPersistenceCleanUpMode getCleanUpMode()
	{
		return getPersistenceContext().getPropertyValue(CLEANUPMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationPersistenceCleanUpCronJob.thresholdDays</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 * @return the thresholdDays
	 */
	@Accessor(qualifier = "thresholdDays", type = Accessor.Type.GETTER)
	public Integer getThresholdDays()
	{
		return getPersistenceContext().getPropertyValue(THRESHOLDDAYS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductConfigurationPersistenceCleanUpCronJob.baseSite</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 *  
	 * @param value the baseSite
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.SETTER)
	public void setBaseSite(final BaseSiteModel value)
	{
		getPersistenceContext().setPropertyValue(BASESITE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductConfigurationPersistenceCleanUpCronJob.cleanUpMode</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 *  
	 * @param value the cleanUpMode
	 */
	@Accessor(qualifier = "cleanUpMode", type = Accessor.Type.SETTER)
	public void setCleanUpMode(final ProductConfigurationPersistenceCleanUpMode value)
	{
		getPersistenceContext().setPropertyValue(CLEANUPMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductConfigurationPersistenceCleanUpCronJob.thresholdDays</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 *  
	 * @param value the thresholdDays
	 */
	@Accessor(qualifier = "thresholdDays", type = Accessor.Type.SETTER)
	public void setThresholdDays(final Integer value)
	{
		getPersistenceContext().setPropertyValue(THRESHOLDDAYS, value);
	}
	
}
