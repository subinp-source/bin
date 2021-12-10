/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.searchservices.jalo.SnIndexConfiguration;
import de.hybris.platform.searchservices.jalo.SnSynonymEntry;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem SnSynonymDictionary}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSnSynonymDictionary extends GenericItem
{
	/** Qualifier of the <code>SnSynonymDictionary.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>SnSynonymDictionary.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>SnSynonymDictionary.languages</code> attribute **/
	public static final String LANGUAGES = "languages";
	/** Qualifier of the <code>SnSynonymDictionary.indexConfigurations</code> attribute **/
	public static final String INDEXCONFIGURATIONS = "indexConfigurations";
	/** Relation ordering override parameter constants for SnIndexConfiguration2SynonymDictionary from ((searchservices))*/
	protected static String SNINDEXCONFIGURATION2SYNONYMDICTIONARY_SRC_ORDERED = "relation.SnIndexConfiguration2SynonymDictionary.source.ordered";
	protected static String SNINDEXCONFIGURATION2SYNONYMDICTIONARY_TGT_ORDERED = "relation.SnIndexConfiguration2SynonymDictionary.target.ordered";
	/** Relation disable markmodifed parameter constants for SnIndexConfiguration2SynonymDictionary from ((searchservices))*/
	protected static String SNINDEXCONFIGURATION2SYNONYMDICTIONARY_MARKMODIFIED = "relation.SnIndexConfiguration2SynonymDictionary.markmodified";
	/** Qualifier of the <code>SnSynonymDictionary.entries</code> attribute **/
	public static final String ENTRIES = "entries";
	/**
	* {@link OneToManyHandler} for handling 1:n ENTRIES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<SnSynonymEntry> ENTRIESHANDLER = new OneToManyHandler<SnSynonymEntry>(
	SearchservicesConstants.TC.SNSYNONYMENTRY,
	true,
	"synonymDictionary",
	"synonymDictionaryPOS",
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
		tmp.put(LANGUAGES, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymDictionary.entries</code> attribute.
	 * @return the entries
	 */
	public List<SnSynonymEntry> getEntries(final SessionContext ctx)
	{
		return (List<SnSynonymEntry>)ENTRIESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymDictionary.entries</code> attribute.
	 * @return the entries
	 */
	public List<SnSynonymEntry> getEntries()
	{
		return getEntries( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymDictionary.entries</code> attribute. 
	 * @param value the entries
	 */
	public void setEntries(final SessionContext ctx, final List<SnSynonymEntry> value)
	{
		ENTRIESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymDictionary.entries</code> attribute. 
	 * @param value the entries
	 */
	public void setEntries(final List<SnSynonymEntry> value)
	{
		setEntries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to entries. 
	 * @param value the item to add to entries
	 */
	public void addToEntries(final SessionContext ctx, final SnSynonymEntry value)
	{
		ENTRIESHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to entries. 
	 * @param value the item to add to entries
	 */
	public void addToEntries(final SnSynonymEntry value)
	{
		addToEntries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from entries. 
	 * @param value the item to remove from entries
	 */
	public void removeFromEntries(final SessionContext ctx, final SnSynonymEntry value)
	{
		ENTRIESHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from entries. 
	 * @param value the item to remove from entries
	 */
	public void removeFromEntries(final SnSynonymEntry value)
	{
		removeFromEntries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymDictionary.id</code> attribute.
	 * @return the id
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymDictionary.id</code> attribute.
	 * @return the id
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymDictionary.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymDictionary.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymDictionary.indexConfigurations</code> attribute.
	 * @return the indexConfigurations
	 */
	public List<SnIndexConfiguration> getIndexConfigurations(final SessionContext ctx)
	{
		final List<SnIndexConfiguration> items = getLinkedItems( 
			ctx,
			false,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2SYNONYMDICTIONARY,
			"SnIndexConfiguration",
			null,
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_TGT_ORDERED, true)
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymDictionary.indexConfigurations</code> attribute.
	 * @return the indexConfigurations
	 */
	public List<SnIndexConfiguration> getIndexConfigurations()
	{
		return getIndexConfigurations( getSession().getSessionContext() );
	}
	
	public long getIndexConfigurationsCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2SYNONYMDICTIONARY,
			"SnIndexConfiguration",
			null
		);
	}
	
	public long getIndexConfigurationsCount()
	{
		return getIndexConfigurationsCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymDictionary.indexConfigurations</code> attribute. 
	 * @param value the indexConfigurations
	 */
	public void setIndexConfigurations(final SessionContext ctx, final List<SnIndexConfiguration> value)
	{
		setLinkedItems( 
			ctx,
			false,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2SYNONYMDICTIONARY,
			null,
			value,
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymDictionary.indexConfigurations</code> attribute. 
	 * @param value the indexConfigurations
	 */
	public void setIndexConfigurations(final List<SnIndexConfiguration> value)
	{
		setIndexConfigurations( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to indexConfigurations. 
	 * @param value the item to add to indexConfigurations
	 */
	public void addToIndexConfigurations(final SessionContext ctx, final SnIndexConfiguration value)
	{
		addLinkedItems( 
			ctx,
			false,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2SYNONYMDICTIONARY,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to indexConfigurations. 
	 * @param value the item to add to indexConfigurations
	 */
	public void addToIndexConfigurations(final SnIndexConfiguration value)
	{
		addToIndexConfigurations( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from indexConfigurations. 
	 * @param value the item to remove from indexConfigurations
	 */
	public void removeFromIndexConfigurations(final SessionContext ctx, final SnIndexConfiguration value)
	{
		removeLinkedItems( 
			ctx,
			false,
			SearchservicesConstants.Relations.SNINDEXCONFIGURATION2SYNONYMDICTIONARY,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from indexConfigurations. 
	 * @param value the item to remove from indexConfigurations
	 */
	public void removeFromIndexConfigurations(final SnIndexConfiguration value)
	{
		removeFromIndexConfigurations( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("SnIndexConfiguration");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(SNINDEXCONFIGURATION2SYNONYMDICTIONARY_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymDictionary.languages</code> attribute.
	 * @return the languages
	 */
	public List<Language> getLanguages(final SessionContext ctx)
	{
		List<Language> coll = (List<Language>)getProperty( ctx, LANGUAGES);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymDictionary.languages</code> attribute.
	 * @return the languages
	 */
	public List<Language> getLanguages()
	{
		return getLanguages( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymDictionary.languages</code> attribute. 
	 * @param value the languages
	 */
	public void setLanguages(final SessionContext ctx, final List<Language> value)
	{
		setProperty(ctx, LANGUAGES,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymDictionary.languages</code> attribute. 
	 * @param value the languages
	 */
	public void setLanguages(final List<Language> value)
	{
		setLanguages( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymDictionary.name</code> attribute.
	 * @return the name
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedSnSynonymDictionary.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymDictionary.name</code> attribute.
	 * @return the name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymDictionary.name</code> attribute. 
	 * @return the localized name
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymDictionary.name</code> attribute. 
	 * @return the localized name
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymDictionary.name</code> attribute. 
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
			throw new JaloInvalidParameterException("GeneratedSnSynonymDictionary.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymDictionary.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymDictionary.name</code> attribute. 
	 * @param value the name
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymDictionary.name</code> attribute. 
	 * @param value the name
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
}
