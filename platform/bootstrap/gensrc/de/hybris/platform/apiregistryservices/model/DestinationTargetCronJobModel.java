/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type DestinationTargetCronJob first defined at extension apiregistryservices.
 * <p>
 * DestinationTarget cronjob.
 */
@SuppressWarnings("all")
public class DestinationTargetCronJobModel extends ServicelayerJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DestinationTargetCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>DestinationTargetCronJob.destinationTargetId</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String DESTINATIONTARGETID = "destinationTargetId";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DestinationTargetCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DestinationTargetCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _destinationTargetId initial attribute declared by type <code>DestinationTargetCronJob</code> at extension <code>apiregistryservices</code>
	 * @param _springId initial attribute declared by type <code>ServicelayerJob</code> at extension <code>processing</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public DestinationTargetCronJobModel(final String _code, final String _destinationTargetId, final String _springId)
	{
		super();
		setCode(_code);
		setDestinationTargetId(_destinationTargetId);
		setSpringId(_springId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _destinationTargetId initial attribute declared by type <code>DestinationTargetCronJob</code> at extension <code>apiregistryservices</code>
	 * @param _nodeID initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _springId initial attribute declared by type <code>ServicelayerJob</code> at extension <code>processing</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public DestinationTargetCronJobModel(final String _code, final String _destinationTargetId, final Integer _nodeID, final ItemModel _owner, final String _springId)
	{
		super();
		setCode(_code);
		setDestinationTargetId(_destinationTargetId);
		setNodeID(_nodeID);
		setOwner(_owner);
		setSpringId(_springId);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DestinationTargetCronJob.destinationTargetId</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the destinationTargetId - ID of DestinationTarget to be processed
	 */
	@Accessor(qualifier = "destinationTargetId", type = Accessor.Type.GETTER)
	public String getDestinationTargetId()
	{
		return getPersistenceContext().getPropertyValue(DESTINATIONTARGETID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DestinationTargetCronJob.destinationTargetId</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the destinationTargetId - ID of DestinationTarget to be processed
	 */
	@Accessor(qualifier = "destinationTargetId", type = Accessor.Type.SETTER)
	public void setDestinationTargetId(final String value)
	{
		getPersistenceContext().setPropertyValue(DESTINATIONTARGETID, value);
	}
	
}
