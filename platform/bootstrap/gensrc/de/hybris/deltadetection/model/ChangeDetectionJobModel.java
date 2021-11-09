/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.deltadetection.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ChangeDetectionJob first defined at extension deltadetection.
 */
@SuppressWarnings("all")
public class ChangeDetectionJobModel extends ServicelayerJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ChangeDetectionJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>ChangeDetectionJob.typePK</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String TYPEPK = "typePK";
	
	/** <i>Generated constant</i> - Attribute key of <code>ChangeDetectionJob.streamId</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String STREAMID = "streamId";
	
	/** <i>Generated constant</i> - Attribute key of <code>ChangeDetectionJob.output</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String OUTPUT = "output";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ChangeDetectionJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ChangeDetectionJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _springId initial attribute declared by type <code>ChangeDetectionJob</code> at extension <code>deltadetection</code>
	 * @param _streamId initial attribute declared by type <code>ChangeDetectionJob</code> at extension <code>deltadetection</code>
	 * @param _typePK initial attribute declared by type <code>ChangeDetectionJob</code> at extension <code>deltadetection</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ChangeDetectionJobModel(final String _code, final String _springId, final String _streamId, final ComposedTypeModel _typePK)
	{
		super();
		setCode(_code);
		setSpringId(_springId);
		setStreamId(_streamId);
		setTypePK(_typePK);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _nodeID initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _springId initial attribute declared by type <code>ChangeDetectionJob</code> at extension <code>deltadetection</code>
	 * @param _streamId initial attribute declared by type <code>ChangeDetectionJob</code> at extension <code>deltadetection</code>
	 * @param _typePK initial attribute declared by type <code>ChangeDetectionJob</code> at extension <code>deltadetection</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ChangeDetectionJobModel(final String _code, final Integer _nodeID, final ItemModel _owner, final String _springId, final String _streamId, final ComposedTypeModel _typePK)
	{
		super();
		setCode(_code);
		setNodeID(_nodeID);
		setOwner(_owner);
		setSpringId(_springId);
		setStreamId(_streamId);
		setTypePK(_typePK);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDetectionJob.output</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the output
	 */
	@Accessor(qualifier = "output", type = Accessor.Type.GETTER)
	public MediaModel getOutput()
	{
		return getPersistenceContext().getPropertyValue(OUTPUT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDetectionJob.streamId</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the streamId
	 */
	@Accessor(qualifier = "streamId", type = Accessor.Type.GETTER)
	public String getStreamId()
	{
		return getPersistenceContext().getPropertyValue(STREAMID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDetectionJob.typePK</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the typePK
	 */
	@Accessor(qualifier = "typePK", type = Accessor.Type.GETTER)
	public ComposedTypeModel getTypePK()
	{
		return getPersistenceContext().getPropertyValue(TYPEPK);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ChangeDetectionJob.output</code> attribute defined at extension <code>deltadetection</code>. 
	 *  
	 * @param value the output
	 */
	@Accessor(qualifier = "output", type = Accessor.Type.SETTER)
	public void setOutput(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(OUTPUT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ChangeDetectionJob.streamId</code> attribute defined at extension <code>deltadetection</code>. 
	 *  
	 * @param value the streamId
	 */
	@Accessor(qualifier = "streamId", type = Accessor.Type.SETTER)
	public void setStreamId(final String value)
	{
		getPersistenceContext().setPropertyValue(STREAMID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ChangeDetectionJob.typePK</code> attribute defined at extension <code>deltadetection</code>. 
	 *  
	 * @param value the typePK
	 */
	@Accessor(qualifier = "typePK", type = Accessor.Type.SETTER)
	public void setTypePK(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(TYPEPK, value);
	}
	
}
