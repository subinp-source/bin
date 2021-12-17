/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 15-Dec-2021, 3:07:44 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.deltadetection.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AbstractChangeProcessorJob first defined at extension deltadetection.
 */
@SuppressWarnings("all")
public class AbstractChangeProcessorJobModel extends ServicelayerJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractChangeProcessorJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractChangeProcessorJob.input</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String INPUT = "input";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractChangeProcessorJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractChangeProcessorJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _input initial attribute declared by type <code>AbstractChangeProcessorJob</code> at extension <code>deltadetection</code>
	 * @param _springId initial attribute declared by type <code>ServicelayerJob</code> at extension <code>processing</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public AbstractChangeProcessorJobModel(final String _code, final MediaModel _input, final String _springId)
	{
		super();
		setCode(_code);
		setInput(_input);
		setSpringId(_springId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _input initial attribute declared by type <code>AbstractChangeProcessorJob</code> at extension <code>deltadetection</code>
	 * @param _nodeID initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _springId initial attribute declared by type <code>ServicelayerJob</code> at extension <code>processing</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public AbstractChangeProcessorJobModel(final String _code, final MediaModel _input, final Integer _nodeID, final ItemModel _owner, final String _springId)
	{
		super();
		setCode(_code);
		setInput(_input);
		setNodeID(_nodeID);
		setOwner(_owner);
		setSpringId(_springId);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractChangeProcessorJob.input</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the input
	 */
	@Accessor(qualifier = "input", type = Accessor.Type.GETTER)
	public MediaModel getInput()
	{
		return getPersistenceContext().getPropertyValue(INPUT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractChangeProcessorJob.input</code> attribute defined at extension <code>deltadetection</code>. 
	 *  
	 * @param value the input
	 */
	@Accessor(qualifier = "input", type = Accessor.Type.SETTER)
	public void setInput(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(INPUT, value);
	}
	
}
