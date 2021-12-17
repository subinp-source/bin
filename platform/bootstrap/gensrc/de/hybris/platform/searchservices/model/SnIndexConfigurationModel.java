/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 15-Dec-2021, 3:07:44 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.searchservices.model.AbstractSnSearchProviderConfigurationModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.searchservices.model.SnSynonymDictionaryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type SnIndexConfiguration first defined at extension searchservices.
 */
@SuppressWarnings("all")
public class SnIndexConfigurationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SnIndexConfiguration";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexConfiguration.id</code> attribute defined at extension <code>searchservices</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexConfiguration.name</code> attribute defined at extension <code>searchservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexConfiguration.user</code> attribute defined at extension <code>searchservices</code>. */
	public static final String USER = "user";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexConfiguration.listeners</code> attribute defined at extension <code>searchservices</code>. */
	public static final String LISTENERS = "listeners";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexConfiguration.languages</code> attribute defined at extension <code>searchservices</code>. */
	public static final String LANGUAGES = "languages";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexConfiguration.currencies</code> attribute defined at extension <code>searchservices</code>. */
	public static final String CURRENCIES = "currencies";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexConfiguration.searchProviderConfiguration</code> attribute defined at extension <code>searchservices</code>. */
	public static final String SEARCHPROVIDERCONFIGURATION = "searchProviderConfiguration";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexConfiguration.indexTypes</code> attribute defined at extension <code>searchservices</code>. */
	public static final String INDEXTYPES = "indexTypes";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexConfiguration.synonymDictionaries</code> attribute defined at extension <code>searchservices</code>. */
	public static final String SYNONYMDICTIONARIES = "synonymDictionaries";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SnIndexConfigurationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SnIndexConfigurationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>SnIndexConfiguration</code> at extension <code>searchservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public SnIndexConfigurationModel(final String _id)
	{
		super();
		setId(_id);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>SnIndexConfiguration</code> at extension <code>searchservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public SnIndexConfigurationModel(final String _id, final ItemModel _owner)
	{
		super();
		setId(_id);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.currencies</code> attribute defined at extension <code>searchservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the currencies
	 */
	@Accessor(qualifier = "currencies", type = Accessor.Type.GETTER)
	public List<CurrencyModel> getCurrencies()
	{
		return getPersistenceContext().getPropertyValue(CURRENCIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.id</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.indexTypes</code> attribute defined at extension <code>searchservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the indexTypes
	 */
	@Accessor(qualifier = "indexTypes", type = Accessor.Type.GETTER)
	public List<SnIndexTypeModel> getIndexTypes()
	{
		return getPersistenceContext().getPropertyValue(INDEXTYPES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.languages</code> attribute defined at extension <code>searchservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the languages
	 */
	@Accessor(qualifier = "languages", type = Accessor.Type.GETTER)
	public List<LanguageModel> getLanguages()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.listeners</code> attribute defined at extension <code>searchservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the listeners
	 */
	@Accessor(qualifier = "listeners", type = Accessor.Type.GETTER)
	public List<String> getListeners()
	{
		return getPersistenceContext().getPropertyValue(LISTENERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.name</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.name</code> attribute defined at extension <code>searchservices</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.searchProviderConfiguration</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the searchProviderConfiguration
	 */
	@Accessor(qualifier = "searchProviderConfiguration", type = Accessor.Type.GETTER)
	public AbstractSnSearchProviderConfigurationModel getSearchProviderConfiguration()
	{
		return getPersistenceContext().getPropertyValue(SEARCHPROVIDERCONFIGURATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.synonymDictionaries</code> attribute defined at extension <code>searchservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the synonymDictionaries
	 */
	@Accessor(qualifier = "synonymDictionaries", type = Accessor.Type.GETTER)
	public List<SnSynonymDictionaryModel> getSynonymDictionaries()
	{
		return getPersistenceContext().getPropertyValue(SYNONYMDICTIONARIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.user</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexConfiguration.currencies</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the currencies
	 */
	@Accessor(qualifier = "currencies", type = Accessor.Type.SETTER)
	public void setCurrencies(final List<CurrencyModel> value)
	{
		getPersistenceContext().setPropertyValue(CURRENCIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexConfiguration.id</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexConfiguration.indexTypes</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the indexTypes
	 */
	@Accessor(qualifier = "indexTypes", type = Accessor.Type.SETTER)
	public void setIndexTypes(final List<SnIndexTypeModel> value)
	{
		getPersistenceContext().setPropertyValue(INDEXTYPES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexConfiguration.languages</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the languages
	 */
	@Accessor(qualifier = "languages", type = Accessor.Type.SETTER)
	public void setLanguages(final List<LanguageModel> value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexConfiguration.listeners</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the listeners
	 */
	@Accessor(qualifier = "listeners", type = Accessor.Type.SETTER)
	public void setListeners(final List<String> value)
	{
		getPersistenceContext().setPropertyValue(LISTENERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexConfiguration.name</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexConfiguration.name</code> attribute defined at extension <code>searchservices</code>. 
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
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexConfiguration.searchProviderConfiguration</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the searchProviderConfiguration
	 */
	@Accessor(qualifier = "searchProviderConfiguration", type = Accessor.Type.SETTER)
	public void setSearchProviderConfiguration(final AbstractSnSearchProviderConfigurationModel value)
	{
		getPersistenceContext().setPropertyValue(SEARCHPROVIDERCONFIGURATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexConfiguration.synonymDictionaries</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the synonymDictionaries
	 */
	@Accessor(qualifier = "synonymDictionaries", type = Accessor.Type.SETTER)
	public void setSynonymDictionaries(final List<SnSynonymDictionaryModel> value)
	{
		getPersistenceContext().setPropertyValue(SYNONYMDICTIONARIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexConfiguration.user</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
}
