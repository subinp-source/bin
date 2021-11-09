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
import de.hybris.deltadetection.enums.ItemVersionMarkerStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type ItemVersionMarker first defined at extension deltadetection.
 */
@SuppressWarnings("all")
public class ItemVersionMarkerModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ItemVersionMarker";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemVersionMarker.itemPK</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String ITEMPK = "itemPK";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemVersionMarker.itemComposedType</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String ITEMCOMPOSEDTYPE = "itemComposedType";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemVersionMarker.versionTS</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String VERSIONTS = "versionTS";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemVersionMarker.versionValue</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String VERSIONVALUE = "versionValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemVersionMarker.lastVersionValue</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String LASTVERSIONVALUE = "lastVersionValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemVersionMarker.info</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String INFO = "info";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemVersionMarker.streamId</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String STREAMID = "streamId";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemVersionMarker.status</code> attribute defined at extension <code>deltadetection</code>. */
	public static final String STATUS = "status";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ItemVersionMarkerModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ItemVersionMarkerModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _itemComposedType initial attribute declared by type <code>ItemVersionMarker</code> at extension <code>deltadetection</code>
	 * @param _itemPK initial attribute declared by type <code>ItemVersionMarker</code> at extension <code>deltadetection</code>
	 * @param _status initial attribute declared by type <code>ItemVersionMarker</code> at extension <code>deltadetection</code>
	 * @param _streamId initial attribute declared by type <code>ItemVersionMarker</code> at extension <code>deltadetection</code>
	 * @param _versionTS initial attribute declared by type <code>ItemVersionMarker</code> at extension <code>deltadetection</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ItemVersionMarkerModel(final ComposedTypeModel _itemComposedType, final Long _itemPK, final ItemVersionMarkerStatus _status, final String _streamId, final Date _versionTS)
	{
		super();
		setItemComposedType(_itemComposedType);
		setItemPK(_itemPK);
		setStatus(_status);
		setStreamId(_streamId);
		setVersionTS(_versionTS);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _itemComposedType initial attribute declared by type <code>ItemVersionMarker</code> at extension <code>deltadetection</code>
	 * @param _itemPK initial attribute declared by type <code>ItemVersionMarker</code> at extension <code>deltadetection</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _status initial attribute declared by type <code>ItemVersionMarker</code> at extension <code>deltadetection</code>
	 * @param _streamId initial attribute declared by type <code>ItemVersionMarker</code> at extension <code>deltadetection</code>
	 * @param _versionTS initial attribute declared by type <code>ItemVersionMarker</code> at extension <code>deltadetection</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ItemVersionMarkerModel(final ComposedTypeModel _itemComposedType, final Long _itemPK, final ItemModel _owner, final ItemVersionMarkerStatus _status, final String _streamId, final Date _versionTS)
	{
		super();
		setItemComposedType(_itemComposedType);
		setItemPK(_itemPK);
		setOwner(_owner);
		setStatus(_status);
		setStreamId(_streamId);
		setVersionTS(_versionTS);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemVersionMarker.info</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the info
	 */
	@Accessor(qualifier = "info", type = Accessor.Type.GETTER)
	public String getInfo()
	{
		return getPersistenceContext().getPropertyValue(INFO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemVersionMarker.itemComposedType</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the itemComposedType
	 */
	@Accessor(qualifier = "itemComposedType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getItemComposedType()
	{
		return getPersistenceContext().getPropertyValue(ITEMCOMPOSEDTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemVersionMarker.itemPK</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the itemPK
	 */
	@Accessor(qualifier = "itemPK", type = Accessor.Type.GETTER)
	public Long getItemPK()
	{
		return getPersistenceContext().getPropertyValue(ITEMPK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemVersionMarker.lastVersionValue</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the lastVersionValue
	 */
	@Accessor(qualifier = "lastVersionValue", type = Accessor.Type.GETTER)
	public String getLastVersionValue()
	{
		return getPersistenceContext().getPropertyValue(LASTVERSIONVALUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemVersionMarker.status</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public ItemVersionMarkerStatus getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemVersionMarker.streamId</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the streamId
	 */
	@Accessor(qualifier = "streamId", type = Accessor.Type.GETTER)
	public String getStreamId()
	{
		return getPersistenceContext().getPropertyValue(STREAMID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemVersionMarker.versionTS</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the versionTS
	 */
	@Accessor(qualifier = "versionTS", type = Accessor.Type.GETTER)
	public Date getVersionTS()
	{
		return getPersistenceContext().getPropertyValue(VERSIONTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemVersionMarker.versionValue</code> attribute defined at extension <code>deltadetection</code>. 
	 * @return the versionValue
	 */
	@Accessor(qualifier = "versionValue", type = Accessor.Type.GETTER)
	public String getVersionValue()
	{
		return getPersistenceContext().getPropertyValue(VERSIONVALUE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemVersionMarker.info</code> attribute defined at extension <code>deltadetection</code>. 
	 *  
	 * @param value the info
	 */
	@Accessor(qualifier = "info", type = Accessor.Type.SETTER)
	public void setInfo(final String value)
	{
		getPersistenceContext().setPropertyValue(INFO, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ItemVersionMarker.itemComposedType</code> attribute defined at extension <code>deltadetection</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the itemComposedType
	 */
	@Accessor(qualifier = "itemComposedType", type = Accessor.Type.SETTER)
	public void setItemComposedType(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(ITEMCOMPOSEDTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ItemVersionMarker.itemPK</code> attribute defined at extension <code>deltadetection</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the itemPK
	 */
	@Accessor(qualifier = "itemPK", type = Accessor.Type.SETTER)
	public void setItemPK(final Long value)
	{
		getPersistenceContext().setPropertyValue(ITEMPK, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemVersionMarker.lastVersionValue</code> attribute defined at extension <code>deltadetection</code>. 
	 *  
	 * @param value the lastVersionValue
	 */
	@Accessor(qualifier = "lastVersionValue", type = Accessor.Type.SETTER)
	public void setLastVersionValue(final String value)
	{
		getPersistenceContext().setPropertyValue(LASTVERSIONVALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemVersionMarker.status</code> attribute defined at extension <code>deltadetection</code>. 
	 *  
	 * @param value the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final ItemVersionMarkerStatus value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ItemVersionMarker.streamId</code> attribute defined at extension <code>deltadetection</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the streamId
	 */
	@Accessor(qualifier = "streamId", type = Accessor.Type.SETTER)
	public void setStreamId(final String value)
	{
		getPersistenceContext().setPropertyValue(STREAMID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemVersionMarker.versionTS</code> attribute defined at extension <code>deltadetection</code>. 
	 *  
	 * @param value the versionTS
	 */
	@Accessor(qualifier = "versionTS", type = Accessor.Type.SETTER)
	public void setVersionTS(final Date value)
	{
		getPersistenceContext().setPropertyValue(VERSIONTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemVersionMarker.versionValue</code> attribute defined at extension <code>deltadetection</code>. 
	 *  
	 * @param value the versionValue
	 */
	@Accessor(qualifier = "versionValue", type = Accessor.Type.SETTER)
	public void setVersionValue(final String value)
	{
		getPersistenceContext().setPropertyValue(VERSIONVALUE, value);
	}
	
}
