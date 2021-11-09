/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.searchservices.jalo.AbstractSnSearchProviderConfiguration;
import de.hybris.platform.searchservices.jalo.SnIndexType;
import de.hybris.platform.searchservices.jalo.SnSynonymDictionary;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem SnIndexConfiguration}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSnIndexConfiguration extends GenericItem
{
	/** Qualifier of the <code>SnIndexConfiguration.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>SnIndexConfiguration.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>SnIndexConfiguration.user</code> attribute **/
	public static final String USER = "user";
	/** Qualifier of the <code>SnIndexConfiguration.listeners</code> attribute **/
	public static final String LISTENERS = "listeners";
	/** Qualifier of the <code>SnIndexConfiguration.languages</code> attribute **/
	public static final String LANGUAGES = "languages";
	/** Relation ordering override parameter constants for SnIndexConfiguration2Language from ((searchservices))*/
	protected static String SNINDEXCONFIGURATION2LANGUAGE_SRC_ORDERED = "relation.SnIndexConfiguration2Language.source.ordered";
	protected static String SNINDEXCONFIGURATION2LANGUAGE_TGT_ORDERED = "relation.SnIndexConfiguration2Language.target.ordered";
	/** Relation disable markmodifed parameter constants for SnIndexConfiguration2Language from ((searchservices))*/
	protected static String SNINDEXCONFIGURATION2LANGUAGE_MARKMODIFIED = "relation.SnIndexConfiguration2Language.markmodified";
	/** Qualifier of the <code>SnIndexConfiguration.currencies</code> attribute **/
	public static final String CURRENCIES = "currencies";
	/** Relation ordering override parameter constants for SnIndexConfiguration2Currency from ((searchservices))*/
	protected static String SNINDEXCONFIGURATION2CURRENCY_SRC_ORDERED = "relation.SnIndexConfiguration2Currency.source.ordered";
	protected static String SNINDEXCONFIGURATION2CURRENCY_TGT_ORDERED = "relation.SnIndexConfiguration2Currency.target.ordered";
	/** Relation disable markmodifed parameter constants for SnIndexConfiguration2Currency from ((searchservices))*/
	protected static String SNINDEXCONFIGURATION2CURRENCY_MARKMODIFIED = "relation.SnIndexConfiguration2Currency.markmodified";
	/** Qualifier of the <code>SnIndexConfiguration.searchProviderConfiguration</code> attribute **/
	public static final String SEARCHPROVIDERCONFIGURATION = "searchProviderConfiguration";
	/** Qualifier of the <code>SnIndexConfiguration.indexTypes</code> attribute **/
	public static final String INDEXTYPES = "indexTypes";
	/** Qualifier of the <code>SnIndexConfiguration.synonymDictionaries</code> attribute **/
	public static final String SYNONYMDICTIONARIES = "synonymDictionaries";
	/** Relation ordering override parameter constants for SnIndexConfiguration2SynonymDictionary from ((searchservices))*/
	protected static String SNINDEXCONFIGURATION2SYNONYMDICTIONARY_SRC_ORDERED = "relation.SnIndexConfiguration2SynonymDictionary.source.ordered";
	protected static String SNINDEXCONFIGURATION2SYNONYMDICTIONARY_TGT_ORDERED = "relation.SnIndexConfiguration2SynonymDictionary.target.ordered";
	/** Relation disable markmodifed parameter constants for SnIndexConfiguration2SynonymDictionary from ((searchservices))*/
	protected static String SNINDEXCONFIGURATION2SYNONYMDICTIONARY_MARKMODIFIED = "relation.SnIndexConfiguration2SynonymDictionary.markmodified";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n SEARCHPROVIDERCONFIGURATION's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedSnIndexConfiguration> SEARCHPROVIDERCONFIGURATIONHANDLER = new BidirectionalOneToManyHandler<GeneratedSnIndexConfiguration>(
	SearchservicesConstants.TC.SNINDEXCONFIGURATION,
	false,
	"searchProviderConfiguration",
	null,
	false,
	true,
	CollectionType.LIST
	);
	/**
	* {@link OneToManyHandler} for handling 1:n INDEXTYPES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<SnIndexType> INDEXTYPESHANDLER = new OneToManyHandler<SnIndexType>(
	SearchservicesConstants.TC.SNINDEXTYPE,
	false,
	"indexConfiguration",
	"indexConfigurationPOS",
	true,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ID, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(USER, AttributeMode.INITIAL);
		tmp.put(LISTENERS, AttributeMode.INITIAL);
		tmp.put(SEARCHPROVIDERCONFIGURATION, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		SEARCHPROVIDERCONFIGURATIONHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.currencies</code> attribute.
	 * @return the currencies
	 */
	public List<Currency> getCurrencies(final SessionContext ctx)
	{
		final List<Currency> items = getLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2CURRENCY,
			"Currency",
			null,
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2CURRENCY_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.currencies</code> attribute.
	 * @return the currencies
	 */
	public List<Currency> getCurrencies()
	{
		return getCurrencies( getSession().getSessionContext() );
	}
	
	public long getCurrenciesCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2CURRENCY,
			"Currency",
			null
		);
	}
	
	public long getCurrenciesCount()
	{
		return getCurrenciesCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.currencies</code> attribute. 
	 * @param value the currencies
	 */
	public void setCurrencies(final SessionContext ctx, final List<Currency> value)
	{
		setLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2CURRENCY,
			null,
			value,
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2CURRENCY_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(SNINDEXCONFIGURATION2CURRENCY_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.currencies</code> attribute. 
	 * @param value the currencies
	 */
	public void setCurrencies(final List<Currency> value)
	{
		setCurrencies( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to currencies. 
	 * @param value the item to add to currencies
	 */
	public void addToCurrencies(final SessionContext ctx, final Currency value)
	{
		addLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2CURRENCY,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2CURRENCY_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(SNINDEXCONFIGURATION2CURRENCY_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to currencies. 
	 * @param value the item to add to currencies
	 */
	public void addToCurrencies(final Currency value)
	{
		addToCurrencies( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from currencies. 
	 * @param value the item to remove from currencies
	 */
	public void removeFromCurrencies(final SessionContext ctx, final Currency value)
	{
		removeLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2CURRENCY,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2CURRENCY_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(SNINDEXCONFIGURATION2CURRENCY_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from currencies. 
	 * @param value the item to remove from currencies
	 */
	public void removeFromCurrencies(final Currency value)
	{
		removeFromCurrencies( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.id</code> attribute.
	 * @return the id
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.id</code> attribute.
	 * @return the id
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.indexTypes</code> attribute.
	 * @return the indexTypes
	 */
	public List<SnIndexType> getIndexTypes(final SessionContext ctx)
	{
		return (List<SnIndexType>)INDEXTYPESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.indexTypes</code> attribute.
	 * @return the indexTypes
	 */
	public List<SnIndexType> getIndexTypes()
	{
		return getIndexTypes( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.indexTypes</code> attribute. 
	 * @param value the indexTypes
	 */
	public void setIndexTypes(final SessionContext ctx, final List<SnIndexType> value)
	{
		INDEXTYPESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.indexTypes</code> attribute. 
	 * @param value the indexTypes
	 */
	public void setIndexTypes(final List<SnIndexType> value)
	{
		setIndexTypes( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to indexTypes. 
	 * @param value the item to add to indexTypes
	 */
	public void addToIndexTypes(final SessionContext ctx, final SnIndexType value)
	{
		INDEXTYPESHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to indexTypes. 
	 * @param value the item to add to indexTypes
	 */
	public void addToIndexTypes(final SnIndexType value)
	{
		addToIndexTypes( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from indexTypes. 
	 * @param value the item to remove from indexTypes
	 */
	public void removeFromIndexTypes(final SessionContext ctx, final SnIndexType value)
	{
		INDEXTYPESHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from indexTypes. 
	 * @param value the item to remove from indexTypes
	 */
	public void removeFromIndexTypes(final SnIndexType value)
	{
		removeFromIndexTypes( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Language");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(SNINDEXCONFIGURATION2LANGUAGE_MARKMODIFIED);
		}
		ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("Currency");
		if(relationSecondEnd1.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(SNINDEXCONFIGURATION2CURRENCY_MARKMODIFIED);
		}
		ComposedType relationSecondEnd2 = TypeManager.getInstance().getComposedType("SnSynonymDictionary");
		if(relationSecondEnd2.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.languages</code> attribute.
	 * @return the languages
	 */
	public List<Language> getLanguages(final SessionContext ctx)
	{
		final List<Language> items = getLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2LANGUAGE,
			"Language",
			null,
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2LANGUAGE_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.languages</code> attribute.
	 * @return the languages
	 */
	public List<Language> getLanguages()
	{
		return getLanguages( getSession().getSessionContext() );
	}
	
	public long getLanguagesCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2LANGUAGE,
			"Language",
			null
		);
	}
	
	public long getLanguagesCount()
	{
		return getLanguagesCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.languages</code> attribute. 
	 * @param value the languages
	 */
	public void setLanguages(final SessionContext ctx, final List<Language> value)
	{
		setLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2LANGUAGE,
			null,
			value,
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2LANGUAGE_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(SNINDEXCONFIGURATION2LANGUAGE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.languages</code> attribute. 
	 * @param value the languages
	 */
	public void setLanguages(final List<Language> value)
	{
		setLanguages( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to languages. 
	 * @param value the item to add to languages
	 */
	public void addToLanguages(final SessionContext ctx, final Language value)
	{
		addLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2LANGUAGE,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2LANGUAGE_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(SNINDEXCONFIGURATION2LANGUAGE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to languages. 
	 * @param value the item to add to languages
	 */
	public void addToLanguages(final Language value)
	{
		addToLanguages( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from languages. 
	 * @param value the item to remove from languages
	 */
	public void removeFromLanguages(final SessionContext ctx, final Language value)
	{
		removeLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2LANGUAGE,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2LANGUAGE_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(SNINDEXCONFIGURATION2LANGUAGE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from languages. 
	 * @param value the item to remove from languages
	 */
	public void removeFromLanguages(final Language value)
	{
		removeFromLanguages( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.listeners</code> attribute.
	 * @return the listeners
	 */
	public List<String> getListeners(final SessionContext ctx)
	{
		List<String> coll = (List<String>)getProperty( ctx, LISTENERS);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.listeners</code> attribute.
	 * @return the listeners
	 */
	public List<String> getListeners()
	{
		return getListeners( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.listeners</code> attribute. 
	 * @param value the listeners
	 */
	public void setListeners(final SessionContext ctx, final List<String> value)
	{
		setProperty(ctx, LISTENERS,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.listeners</code> attribute. 
	 * @param value the listeners
	 */
	public void setListeners(final List<String> value)
	{
		setListeners( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.name</code> attribute.
	 * @return the name
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedSnIndexConfiguration.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.name</code> attribute.
	 * @return the name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.name</code> attribute. 
	 * @return the localized name
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.name</code> attribute. 
	 * @return the localized name
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedSnIndexConfiguration.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.name</code> attribute. 
	 * @param value the name
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.name</code> attribute. 
	 * @param value the name
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.searchProviderConfiguration</code> attribute.
	 * @return the searchProviderConfiguration
	 */
	public AbstractSnSearchProviderConfiguration getSearchProviderConfiguration(final SessionContext ctx)
	{
		return (AbstractSnSearchProviderConfiguration)getProperty( ctx, SEARCHPROVIDERCONFIGURATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.searchProviderConfiguration</code> attribute.
	 * @return the searchProviderConfiguration
	 */
	public AbstractSnSearchProviderConfiguration getSearchProviderConfiguration()
	{
		return getSearchProviderConfiguration( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.searchProviderConfiguration</code> attribute. 
	 * @param value the searchProviderConfiguration
	 */
	public void setSearchProviderConfiguration(final SessionContext ctx, final AbstractSnSearchProviderConfiguration value)
	{
		SEARCHPROVIDERCONFIGURATIONHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.searchProviderConfiguration</code> attribute. 
	 * @param value the searchProviderConfiguration
	 */
	public void setSearchProviderConfiguration(final AbstractSnSearchProviderConfiguration value)
	{
		setSearchProviderConfiguration( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.synonymDictionaries</code> attribute.
	 * @return the synonymDictionaries
	 */
	public List<SnSynonymDictionary> getSynonymDictionaries(final SessionContext ctx)
	{
		final List<SnSynonymDictionary> items = getLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2SYNONYMDICTIONARY,
			"SnSynonymDictionary",
			null,
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_TGT_ORDERED, true)
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.synonymDictionaries</code> attribute.
	 * @return the synonymDictionaries
	 */
	public List<SnSynonymDictionary> getSynonymDictionaries()
	{
		return getSynonymDictionaries( getSession().getSessionContext() );
	}
	
	public long getSynonymDictionariesCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2SYNONYMDICTIONARY,
			"SnSynonymDictionary",
			null
		);
	}
	
	public long getSynonymDictionariesCount()
	{
		return getSynonymDictionariesCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.synonymDictionaries</code> attribute. 
	 * @param value the synonymDictionaries
	 */
	public void setSynonymDictionaries(final SessionContext ctx, final List<SnSynonymDictionary> value)
	{
		setLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2SYNONYMDICTIONARY,
			null,
			value,
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.synonymDictionaries</code> attribute. 
	 * @param value the synonymDictionaries
	 */
	public void setSynonymDictionaries(final List<SnSynonymDictionary> value)
	{
		setSynonymDictionaries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to synonymDictionaries. 
	 * @param value the item to add to synonymDictionaries
	 */
	public void addToSynonymDictionaries(final SessionContext ctx, final SnSynonymDictionary value)
	{
		addLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2SYNONYMDICTIONARY,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to synonymDictionaries. 
	 * @param value the item to add to synonymDictionaries
	 */
	public void addToSynonymDictionaries(final SnSynonymDictionary value)
	{
		addToSynonymDictionaries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from synonymDictionaries. 
	 * @param value the item to remove from synonymDictionaries
	 */
	public void removeFromSynonymDictionaries(final SessionContext ctx, final SnSynonymDictionary value)
	{
		removeLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2SYNONYMDICTIONARY,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from synonymDictionaries. 
	 * @param value the item to remove from synonymDictionaries
	 */
	public void removeFromSynonymDictionaries(final SnSynonymDictionary value)
	{
		removeFromSynonymDictionaries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.user</code> attribute.
	 * @return the user
	 */
	public User getUser(final SessionContext ctx)
	{
		return (User)getProperty( ctx, USER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexConfiguration.user</code> attribute.
	 * @return the user
	 */
	public User getUser()
	{
		return getUser( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.user</code> attribute. 
	 * @param value the user
	 */
	public void setUser(final SessionContext ctx, final User value)
	{
		setProperty(ctx, USER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexConfiguration.user</code> attribute. 
	 * @param value the user
	 */
	public void setUser(final User value)
	{
		setUser( getSession().getSessionContext(), value );
	}
	
}
