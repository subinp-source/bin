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
import de.hybris.platform.searchservices.model.SnSynonymDictionaryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

/**
 * Generated model class for type SnSynonymEntry first defined at extension searchservices.
 */
@SuppressWarnings("all")
public class SnSynonymEntryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SnSynonymEntry";
	
	/**<i>Generated relation code constant for relation <code>SnSynonymDictionary2SynonymEntry</code> defining source attribute <code>synonymDictionary</code> in extension <code>searchservices</code>.</i>*/
	public static final String _SNSYNONYMDICTIONARY2SYNONYMENTRY = "SnSynonymDictionary2SynonymEntry";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnSynonymEntry.id</code> attribute defined at extension <code>searchservices</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnSynonymEntry.input</code> attribute defined at extension <code>searchservices</code>. */
	public static final String INPUT = "input";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnSynonymEntry.synonyms</code> attribute defined at extension <code>searchservices</code>. */
	public static final String SYNONYMS = "synonyms";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnSynonymEntry.synonymDictionaryPOS</code> attribute defined at extension <code>searchservices</code>. */
	public static final String SYNONYMDICTIONARYPOS = "synonymDictionaryPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnSynonymEntry.synonymDictionary</code> attribute defined at extension <code>searchservices</code>. */
	public static final String SYNONYMDICTIONARY = "synonymDictionary";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SnSynonymEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SnSynonymEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>SnSynonymEntry</code> at extension <code>searchservices</code>
	 * @param _synonymDictionary initial attribute declared by type <code>SnSynonymEntry</code> at extension <code>searchservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public SnSynonymEntryModel(final String _id, final SnSynonymDictionaryModel _synonymDictionary)
	{
		super();
		setId(_id);
		setSynonymDictionary(_synonymDictionary);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>SnSynonymEntry</code> at extension <code>searchservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _synonymDictionary initial attribute declared by type <code>SnSynonymEntry</code> at extension <code>searchservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public SnSynonymEntryModel(final String _id, final ItemModel _owner, final SnSynonymDictionaryModel _synonymDictionary)
	{
		super();
		setId(_id);
		setOwner(_owner);
		setSynonymDictionary(_synonymDictionary);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymEntry.id</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymEntry.input</code> attribute defined at extension <code>searchservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the input
	 */
	@Accessor(qualifier = "input", type = Accessor.Type.GETTER)
	public List<String> getInput()
	{
		return getPersistenceContext().getPropertyValue(INPUT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymEntry.synonymDictionary</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the synonymDictionary
	 */
	@Accessor(qualifier = "synonymDictionary", type = Accessor.Type.GETTER)
	public SnSynonymDictionaryModel getSynonymDictionary()
	{
		return getPersistenceContext().getPropertyValue(SYNONYMDICTIONARY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymEntry.synonyms</code> attribute defined at extension <code>searchservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the synonyms
	 */
	@Accessor(qualifier = "synonyms", type = Accessor.Type.GETTER)
	public List<String> getSynonyms()
	{
		return getPersistenceContext().getPropertyValue(SYNONYMS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnSynonymEntry.id</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnSynonymEntry.input</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the input
	 */
	@Accessor(qualifier = "input", type = Accessor.Type.SETTER)
	public void setInput(final List<String> value)
	{
		getPersistenceContext().setPropertyValue(INPUT, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SnSynonymEntry.synonymDictionary</code> attribute defined at extension <code>searchservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the synonymDictionary
	 */
	@Accessor(qualifier = "synonymDictionary", type = Accessor.Type.SETTER)
	public void setSynonymDictionary(final SnSynonymDictionaryModel value)
	{
		getPersistenceContext().setPropertyValue(SYNONYMDICTIONARY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnSynonymEntry.synonyms</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the synonyms
	 */
	@Accessor(qualifier = "synonyms", type = Accessor.Type.SETTER)
	public void setSynonyms(final List<String> value)
	{
		getPersistenceContext().setPropertyValue(SYNONYMS, value);
	}
	
}
