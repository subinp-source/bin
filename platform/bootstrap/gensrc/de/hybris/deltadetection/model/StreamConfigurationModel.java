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
import de.hybris.deltadetection.model.StreamConfigurationContainerModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

/**
 * Generated model class for type StreamConfiguration first defined at extension deltadetection.
 */
@SuppressWarnings("all")
public class StreamConfigurationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "StreamConfiguration";
	
	/**<i>Generated relation code constant for relation <code>StreamConfigurationContainer2StreamConfiguration</code> defining source attribute <code>container</code> in extension <code>deltadetection</code>.</i>*/
	public static final String _STREAMCONFIGURATIONCONTAINER2STREAMCONFIGURATION = "StreamConfigurationContainer2StreamConfiguration";
	
	/** <i>Generated constant</i> - Attribute key of <code>StreamConfiguration.streamId</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String STREAMID = "streamId";
	
	/** <i>Generated constant</i> - Attribute key of <code>StreamConfiguration.itemTypeForStream</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String ITEMTYPEFORSTREAM = "itemTypeForStream";
	
	/** <i>Generated constant</i> - Attribute key of <code>StreamConfiguration.whereClause</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String WHERECLAUSE = "whereClause";
	
	/** <i>Generated constant</i> - Attribute key of <code>StreamConfiguration.versionSelectClause</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String VERSIONSELECTCLAUSE = "versionSelectClause";
	
	/** <i>Generated constant</i> - Attribute key of <code>StreamConfiguration.active</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>StreamConfiguration.infoExpression</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String INFOEXPRESSION = "infoExpression";
	
	/** <i>Generated constant</i> - Attribute key of <code>StreamConfiguration.container</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String CONTAINER = "container";
	
	/** <i>Generated constant</i> - Attribute key of <code>StreamConfiguration.excludedTypes</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String EXCLUDEDTYPES = "excludedTypes";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public StreamConfigurationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public StreamConfigurationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _container initial attribute declared by type <code>StreamConfiguration</code> at extension <code>deltadetection</code>
	 * @param _itemTypeForStream initial attribute declared by type <code>StreamConfiguration</code> at extension <code>deltadetection</code>
	 * @param _streamId initial attribute declared by type <code>StreamConfiguration</code> at extension <code>deltadetection</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public StreamConfigurationModel(final StreamConfigurationContainerModel _container, final ComposedTypeModel _itemTypeForStream, final String _streamId)
	{
		super();
		setContainer(_container);
		setItemTypeForStream(_itemTypeForStream);
		setStreamId(_streamId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _container initial attribute declared by type <code>StreamConfiguration</code> at extension <code>deltadetection</code>
	 * @param _itemTypeForStream initial attribute declared by type <code>StreamConfiguration</code> at extension <code>deltadetection</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _streamId initial attribute declared by type <code>StreamConfiguration</code> at extension <code>deltadetection</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public StreamConfigurationModel(final StreamConfigurationContainerModel _container, final ComposedTypeModel _itemTypeForStream, final ItemModel _owner, final String _streamId)
	{
		super();
		setContainer(_container);
		setItemTypeForStream(_itemTypeForStream);
		setOwner(_owner);
		setStreamId(_streamId);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StreamConfiguration.active</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public Boolean getActive()
	{
		return getPersistenceContext().getPropertyValue(ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StreamConfiguration.container</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the container
	 */
	@Accessor(qualifier = "container", type = Accessor.Type.GETTER)
	public StreamConfigurationContainerModel getContainer()
	{
		return getPersistenceContext().getPropertyValue(CONTAINER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StreamConfiguration.excludedTypes</code> attribute defined at extension <code>deltadetection</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the excludedTypes
	 */
	@Accessor(qualifier = "excludedTypes", type = Accessor.Type.GETTER)
	public Set<ComposedTypeModel> getExcludedTypes()
	{
		return getPersistenceContext().getPropertyValue(EXCLUDEDTYPES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StreamConfiguration.infoExpression</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the infoExpression
	 */
	@Accessor(qualifier = "infoExpression", type = Accessor.Type.GETTER)
	public String getInfoExpression()
	{
		return getPersistenceContext().getPropertyValue(INFOEXPRESSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StreamConfiguration.itemTypeForStream</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the itemTypeForStream
	 */
	@Accessor(qualifier = "itemTypeForStream", type = Accessor.Type.GETTER)
	public ComposedTypeModel getItemTypeForStream()
	{
		return getPersistenceContext().getPropertyValue(ITEMTYPEFORSTREAM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StreamConfiguration.streamId</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the streamId
	 */
	@Accessor(qualifier = "streamId", type = Accessor.Type.GETTER)
	public String getStreamId()
	{
		return getPersistenceContext().getPropertyValue(STREAMID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StreamConfiguration.versionSelectClause</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the versionSelectClause
	 */
	@Accessor(qualifier = "versionSelectClause", type = Accessor.Type.GETTER)
	public String getVersionSelectClause()
	{
		return getPersistenceContext().getPropertyValue(VERSIONSELECTCLAUSE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StreamConfiguration.whereClause</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the whereClause
	 */
	@Accessor(qualifier = "whereClause", type = Accessor.Type.GETTER)
	public String getWhereClause()
	{
		return getPersistenceContext().getPropertyValue(WHERECLAUSE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StreamConfiguration.active</code> attribute defined at extension <code>deltadetection</code>. 
	 *  
	 * @param value the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>StreamConfiguration.container</code> attribute defined at extension <code>deltadetection</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the container
	 */
	@Accessor(qualifier = "container", type = Accessor.Type.SETTER)
	public void setContainer(final StreamConfigurationContainerModel value)
	{
		getPersistenceContext().setPropertyValue(CONTAINER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StreamConfiguration.excludedTypes</code> attribute defined at extension <code>deltadetection</code>. 
	 *  
	 * @param value the excludedTypes
	 */
	@Accessor(qualifier = "excludedTypes", type = Accessor.Type.SETTER)
	public void setExcludedTypes(final Set<ComposedTypeModel> value)
	{
		getPersistenceContext().setPropertyValue(EXCLUDEDTYPES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StreamConfiguration.infoExpression</code> attribute defined at extension <code>deltadetection</code>. 
	 *  
	 * @param value the infoExpression
	 */
	@Accessor(qualifier = "infoExpression", type = Accessor.Type.SETTER)
	public void setInfoExpression(final String value)
	{
		getPersistenceContext().setPropertyValue(INFOEXPRESSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StreamConfiguration.itemTypeForStream</code> attribute defined at extension <code>deltadetection</code>. 
	 *  
	 * @param value the itemTypeForStream
	 */
	@Accessor(qualifier = "itemTypeForStream", type = Accessor.Type.SETTER)
	public void setItemTypeForStream(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(ITEMTYPEFORSTREAM, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>StreamConfiguration.streamId</code> attribute defined at extension <code>deltadetection</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the streamId
	 */
	@Accessor(qualifier = "streamId", type = Accessor.Type.SETTER)
	public void setStreamId(final String value)
	{
		getPersistenceContext().setPropertyValue(STREAMID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StreamConfiguration.versionSelectClause</code> attribute defined at extension <code>deltadetection</code>. 
	 *  
	 * @param value the versionSelectClause
	 */
	@Accessor(qualifier = "versionSelectClause", type = Accessor.Type.SETTER)
	public void setVersionSelectClause(final String value)
	{
		getPersistenceContext().setPropertyValue(VERSIONSELECTCLAUSE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StreamConfiguration.whereClause</code> attribute defined at extension <code>deltadetection</code>. 
	 *  
	 * @param value the whereClause
	 */
	@Accessor(qualifier = "whereClause", type = Accessor.Type.SETTER)
	public void setWhereClause(final String value)
	{
		getPersistenceContext().setPropertyValue(WHERECLAUSE, value);
	}
	
}
