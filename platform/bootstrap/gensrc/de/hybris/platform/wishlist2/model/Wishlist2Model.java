/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.wishlist2.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import java.util.List;

/**
 * Generated model class for type Wishlist2 first defined at extension wishlist.
 */
@SuppressWarnings("all")
public class Wishlist2Model extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Wishlist2";
	
	/**<i>Generated relation code constant for relation <code>User2Wishlist2</code> defining source attribute <code>user</code> in extension <code>wishlist</code>.</i>*/
	public static final String _USER2WISHLIST2 = "User2Wishlist2";
	
	/** <i>Generated constant</i> - Attribute key of <code>Wishlist2.name</code> attribute defined at extension <code>wishlist</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>Wishlist2.description</code> attribute defined at extension <code>wishlist</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>Wishlist2.default</code> attribute defined at extension <code>wishlist</code>. */
	public static final String DEFAULT = "default";
	
	/** <i>Generated constant</i> - Attribute key of <code>Wishlist2.user</code> attribute defined at extension <code>wishlist</code>. */
	public static final String USER = "user";
	
	/** <i>Generated constant</i> - Attribute key of <code>Wishlist2.entries</code> attribute defined at extension <code>wishlist</code>. */
	public static final String ENTRIES = "entries";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public Wishlist2Model()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public Wishlist2Model(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>Wishlist2</code> at extension <code>wishlist</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public Wishlist2Model(final String _name)
	{
		super();
		setName(_name);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>Wishlist2</code> at extension <code>wishlist</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public Wishlist2Model(final String _name, final ItemModel _owner)
	{
		super();
		setName(_name);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2.default</code> attribute defined at extension <code>wishlist</code>. 
	 * @return the default
	 */
	@Accessor(qualifier = "default", type = Accessor.Type.GETTER)
	public Boolean getDefault()
	{
		return getPersistenceContext().getPropertyValue(DEFAULT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2.description</code> attribute defined at extension <code>wishlist</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getPersistenceContext().getPropertyValue(DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2.entries</code> attribute defined at extension <code>wishlist</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the entries
	 */
	@Accessor(qualifier = "entries", type = Accessor.Type.GETTER)
	public List<Wishlist2EntryModel> getEntries()
	{
		return getPersistenceContext().getPropertyValue(ENTRIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2.name</code> attribute defined at extension <code>wishlist</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2.user</code> attribute defined at extension <code>wishlist</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Wishlist2.default</code> attribute defined at extension <code>wishlist</code>. 
	 *  
	 * @param value the default
	 */
	@Accessor(qualifier = "default", type = Accessor.Type.SETTER)
	public void setDefault(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(DEFAULT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Wishlist2.description</code> attribute defined at extension <code>wishlist</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		getPersistenceContext().setPropertyValue(DESCRIPTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Wishlist2.entries</code> attribute defined at extension <code>wishlist</code>. 
	 *  
	 * @param value the entries
	 */
	@Accessor(qualifier = "entries", type = Accessor.Type.SETTER)
	public void setEntries(final List<Wishlist2EntryModel> value)
	{
		getPersistenceContext().setPropertyValue(ENTRIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Wishlist2.name</code> attribute defined at extension <code>wishlist</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Wishlist2.user</code> attribute defined at extension <code>wishlist</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
}
