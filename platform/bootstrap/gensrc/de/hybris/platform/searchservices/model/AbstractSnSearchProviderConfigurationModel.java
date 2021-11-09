/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type AbstractSnSearchProviderConfiguration first defined at extension searchservices.
 */
@SuppressWarnings("all")
public class AbstractSnSearchProviderConfigurationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractSnSearchProviderConfiguration";
	
	/**<i>Generated relation code constant for relation <code>SnIndexConfiguration2SearchProviderConfiguration</code> defining source attribute <code>indexConfigurations</code> in extension <code>searchservices</code>.</i>*/
	public static final String _SNINDEXCONFIGURATION2SEARCHPROVIDERCONFIGURATION = "SnIndexConfiguration2SearchProviderConfiguration";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractSnSearchProviderConfiguration.id</code> attribute defined at extension <code>searchservices</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractSnSearchProviderConfiguration.name</code> attribute defined at extension <code>searchservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractSnSearchProviderConfiguration.listeners</code> attribute defined at extension <code>searchservices</code>. */
	public static final String LISTENERS = "listeners";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractSnSearchProviderConfiguration.indexConfigurations</code> attribute defined at extension <code>searchservices</code>. */
	public static final String INDEXCONFIGURATIONS = "indexConfigurations";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractSnSearchProviderConfigurationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractSnSearchProviderConfigurationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>AbstractSnSearchProviderConfiguration</code> at extension <code>searchservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public AbstractSnSearchProviderConfigurationModel(final String _id)
	{
		super();
		setId(_id);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>AbstractSnSearchProviderConfiguration</code> at extension <code>searchservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public AbstractSnSearchProviderConfigurationModel(final String _id, final ItemModel _owner)
	{
		super();
		setId(_id);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnSearchProviderConfiguration.id</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnSearchProviderConfiguration.indexConfigurations</code> attribute defined at extension <code>searchservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the indexConfigurations
	 */
	@Accessor(qualifier = "indexConfigurations", type = Accessor.Type.GETTER)
	public List<SnIndexConfigurationModel> getIndexConfigurations()
	{
		return getPersistenceContext().getPropertyValue(INDEXCONFIGURATIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnSearchProviderConfiguration.listeners</code> attribute defined at extension <code>searchservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the listeners
	 */
	@Accessor(qualifier = "listeners", type = Accessor.Type.GETTER)
	public List<String> getListeners()
	{
		return getPersistenceContext().getPropertyValue(LISTENERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnSearchProviderConfiguration.name</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnSearchProviderConfiguration.name</code> attribute defined at extension <code>searchservices</code>. 
	 * @param loc the value localization key 
	 * @return the name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractSnSearchProviderConfiguration.id</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractSnSearchProviderConfiguration.indexConfigurations</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the indexConfigurations
	 */
	@Accessor(qualifier = "indexConfigurations", type = Accessor.Type.SETTER)
	public void setIndexConfigurations(final List<SnIndexConfigurationModel> value)
	{
		getPersistenceContext().setPropertyValue(INDEXCONFIGURATIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractSnSearchProviderConfiguration.listeners</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the listeners
	 */
	@Accessor(qualifier = "listeners", type = Accessor.Type.SETTER)
	public void setListeners(final List<String> value)
	{
		getPersistenceContext().setPropertyValue(LISTENERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractSnSearchProviderConfiguration.name</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractSnSearchProviderConfiguration.name</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
}
