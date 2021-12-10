/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.deltadetection.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

/**
 * Generated model class for type StreamConfigurationContainer first defined at extension deltadetection.
 */
@SuppressWarnings("all")
public class StreamConfigurationContainerModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "StreamConfigurationContainer";
	
	/** <i>Generated constant</i> - Attribute key of <code>StreamConfigurationContainer.id</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>StreamConfigurationContainer.configurations</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String CONFIGURATIONS = "configurations";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public StreamConfigurationContainerModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public StreamConfigurationContainerModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>StreamConfigurationContainer</code> at extension <code>deltadetection</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public StreamConfigurationContainerModel(final String _id)
	{
		super();
		setId(_id);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>StreamConfigurationContainer</code> at extension <code>deltadetection</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public StreamConfigurationContainerModel(final String _id, final ItemModel _owner)
	{
		super();
		setId(_id);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StreamConfigurationContainer.configurations</code> attribute defined at extension <code>deltadetection</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the configurations
	 */
	@Accessor(qualifier = "configurations", type = Accessor.Type.GETTER)
	public Set<StreamConfigurationModel> getConfigurations()
	{
		return getPersistenceContext().getPropertyValue(CONFIGURATIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StreamConfigurationContainer.id</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StreamConfigurationContainer.configurations</code> attribute defined at extension <code>deltadetection</code>. 
	 *  
	 * @param value the configurations
	 */
	@Accessor(qualifier = "configurations", type = Accessor.Type.SETTER)
	public void setConfigurations(final Set<StreamConfigurationModel> value)
	{
		getPersistenceContext().setPropertyValue(CONFIGURATIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>StreamConfigurationContainer.id</code> attribute defined at extension <code>deltadetection</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
}
