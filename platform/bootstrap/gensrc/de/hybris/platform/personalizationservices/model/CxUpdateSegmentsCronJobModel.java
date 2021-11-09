/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

/**
 * Generated model class for type CxUpdateSegmentsCronJob first defined at extension personalizationservices.
 */
@SuppressWarnings("all")
public class CxUpdateSegmentsCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CxUpdateSegmentsCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxUpdateSegmentsCronJob.allProviders</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String ALLPROVIDERS = "allProviders";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxUpdateSegmentsCronJob.providers</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String PROVIDERS = "providers";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxUpdateSegmentsCronJob.fullUpdate</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String FULLUPDATE = "fullUpdate";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxUpdateSegmentsCronJob.allBaseSites</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String ALLBASESITES = "allBaseSites";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxUpdateSegmentsCronJob.baseSites</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String BASESITES = "baseSites";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CxUpdateSegmentsCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CxUpdateSegmentsCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public CxUpdateSegmentsCronJobModel(final JobModel _job)
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
	public CxUpdateSegmentsCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxUpdateSegmentsCronJob.baseSites</code> attribute defined at extension <code>personalizationservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the baseSites - BaseSites which will be set in session for update segment process
	 */
	@Accessor(qualifier = "baseSites", type = Accessor.Type.GETTER)
	public Set<BaseSiteModel> getBaseSites()
	{
		return getPersistenceContext().getPropertyValue(BASESITES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxUpdateSegmentsCronJob.providers</code> attribute defined at extension <code>personalizationservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the providers - Segment providers
	 */
	@Accessor(qualifier = "providers", type = Accessor.Type.GETTER)
	public Set<String> getProviders()
	{
		return getPersistenceContext().getPropertyValue(PROVIDERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxUpdateSegmentsCronJob.allBaseSites</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the allBaseSites
	 */
	@Accessor(qualifier = "allBaseSites", type = Accessor.Type.GETTER)
	public boolean isAllBaseSites()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(ALLBASESITES));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxUpdateSegmentsCronJob.allProviders</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the allProviders
	 */
	@Accessor(qualifier = "allProviders", type = Accessor.Type.GETTER)
	public boolean isAllProviders()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(ALLPROVIDERS));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxUpdateSegmentsCronJob.fullUpdate</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the fullUpdate
	 */
	@Accessor(qualifier = "fullUpdate", type = Accessor.Type.GETTER)
	public boolean isFullUpdate()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(FULLUPDATE));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxUpdateSegmentsCronJob.allBaseSites</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the allBaseSites
	 */
	@Accessor(qualifier = "allBaseSites", type = Accessor.Type.SETTER)
	public void setAllBaseSites(final boolean value)
	{
		getPersistenceContext().setPropertyValue(ALLBASESITES, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxUpdateSegmentsCronJob.allProviders</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the allProviders
	 */
	@Accessor(qualifier = "allProviders", type = Accessor.Type.SETTER)
	public void setAllProviders(final boolean value)
	{
		getPersistenceContext().setPropertyValue(ALLPROVIDERS, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxUpdateSegmentsCronJob.baseSites</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the baseSites - BaseSites which will be set in session for update segment process
	 */
	@Accessor(qualifier = "baseSites", type = Accessor.Type.SETTER)
	public void setBaseSites(final Set<BaseSiteModel> value)
	{
		getPersistenceContext().setPropertyValue(BASESITES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxUpdateSegmentsCronJob.fullUpdate</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the fullUpdate
	 */
	@Accessor(qualifier = "fullUpdate", type = Accessor.Type.SETTER)
	public void setFullUpdate(final boolean value)
	{
		getPersistenceContext().setPropertyValue(FULLUPDATE, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxUpdateSegmentsCronJob.providers</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the providers - Segment providers
	 */
	@Accessor(qualifier = "providers", type = Accessor.Type.SETTER)
	public void setProviders(final Set<String> value)
	{
		getPersistenceContext().setPropertyValue(PROVIDERS, value);
	}
	
}
