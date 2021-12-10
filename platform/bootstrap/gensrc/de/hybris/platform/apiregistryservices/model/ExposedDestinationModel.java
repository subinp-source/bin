/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.EndpointModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ExposedDestination first defined at extension apiregistryservices.
 * <p>
 * Exposed service that can be used by an external system.
 */
@SuppressWarnings("all")
public class ExposedDestinationModel extends AbstractDestinationModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ExposedDestination";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExposedDestination.targetId</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String TARGETID = "targetId";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ExposedDestinationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ExposedDestinationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _destinationTarget initial attribute declared by type <code>AbstractDestination</code> at extension <code>apiregistryservices</code>
	 * @param _endpoint initial attribute declared by type <code>AbstractDestination</code> at extension <code>apiregistryservices</code>
	 * @param _id initial attribute declared by type <code>AbstractDestination</code> at extension <code>apiregistryservices</code>
	 * @param _url initial attribute declared by type <code>AbstractDestination</code> at extension <code>apiregistryservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ExposedDestinationModel(final DestinationTargetModel _destinationTarget, final EndpointModel _endpoint, final String _id, final String _url)
	{
		super();
		setDestinationTarget(_destinationTarget);
		setEndpoint(_endpoint);
		setId(_id);
		setUrl(_url);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _destinationTarget initial attribute declared by type <code>AbstractDestination</code> at extension <code>apiregistryservices</code>
	 * @param _endpoint initial attribute declared by type <code>AbstractDestination</code> at extension <code>apiregistryservices</code>
	 * @param _id initial attribute declared by type <code>AbstractDestination</code> at extension <code>apiregistryservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _url initial attribute declared by type <code>AbstractDestination</code> at extension <code>apiregistryservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ExposedDestinationModel(final DestinationTargetModel _destinationTarget, final EndpointModel _endpoint, final String _id, final ItemModel _owner, final String _url)
	{
		super();
		setDestinationTarget(_destinationTarget);
		setEndpoint(_endpoint);
		setId(_id);
		setOwner(_owner);
		setUrl(_url);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExposedDestination.targetId</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the targetId - Unique Id of destination in the target system
	 */
	@Accessor(qualifier = "targetId", type = Accessor.Type.GETTER)
	public String getTargetId()
	{
		return getPersistenceContext().getPropertyValue(TARGETID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExposedDestination.targetId</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the targetId - Unique Id of destination in the target system
	 */
	@Accessor(qualifier = "targetId", type = Accessor.Type.SETTER)
	public void setTargetId(final String value)
	{
		getPersistenceContext().setPropertyValue(TARGETID, value);
	}
	
}
