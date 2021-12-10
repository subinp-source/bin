/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.model.SnIndexModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SnIndexerOperation first defined at extension searchservices.
 */
@SuppressWarnings("all")
public class SnIndexerOperationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SnIndexerOperation";
	
	/**<i>Generated relation code constant for relation <code>SnIndex2IndexerOperation</code> defining source attribute <code>index</code> in extension <code>searchservices</code>.</i>*/
	public static final String _SNINDEX2INDEXEROPERATION = "SnIndex2IndexerOperation";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexerOperation.id</code> attribute defined at extension <code>searchservices</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexerOperation.index</code> attribute defined at extension <code>searchservices</code>. */
	public static final String INDEX = "index";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SnIndexerOperationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SnIndexerOperationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>SnIndexerOperation</code> at extension <code>searchservices</code>
	 * @param _index initial attribute declared by type <code>SnIndexerOperation</code> at extension <code>searchservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public SnIndexerOperationModel(final String _id, final SnIndexModel _index)
	{
		super();
		setId(_id);
		setIndex(_index);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>SnIndexerOperation</code> at extension <code>searchservices</code>
	 * @param _index initial attribute declared by type <code>SnIndexerOperation</code> at extension <code>searchservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public SnIndexerOperationModel(final String _id, final SnIndexModel _index, final ItemModel _owner)
	{
		super();
		setId(_id);
		setIndex(_index);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexerOperation.id</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexerOperation.index</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the index
	 */
	@Accessor(qualifier = "index", type = Accessor.Type.GETTER)
	public SnIndexModel getIndex()
	{
		return getPersistenceContext().getPropertyValue(INDEX);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexerOperation.id</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SnIndexerOperation.index</code> attribute defined at extension <code>searchservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the index
	 */
	@Accessor(qualifier = "index", type = Accessor.Type.SETTER)
	public void setIndex(final SnIndexModel value)
	{
		getPersistenceContext().setPropertyValue(INDEX, value);
	}
	
}
