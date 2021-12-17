/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 15-Dec-2021, 3:07:44 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsItemConfigurationModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type AsFacetRange first defined at extension adaptivesearch.
 */
@SuppressWarnings("all")
public class AsFacetRangeModel extends AbstractAsItemConfigurationModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AsFacetRange";
	
	/**<i>Generated relation code constant for relation <code>AbstractAsFacetConfiguration2AsFacetRange</code> defining source attribute <code>facetConfiguration</code> in extension <code>adaptivesearch</code>.</i>*/
	public static final String _ABSTRACTASFACETCONFIGURATION2ASFACETRANGE = "AbstractAsFacetConfiguration2AsFacetRange";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsFacetRange.id</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsFacetRange.name</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsFacetRange.qualifier</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String QUALIFIER = "qualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsFacetRange.from</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String FROM = "from";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsFacetRange.to</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String TO = "to";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsFacetRange.uniqueIdx</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String UNIQUEIDX = "uniqueIdx";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsFacetRange.facetConfigurationPOS</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String FACETCONFIGURATIONPOS = "facetConfigurationPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsFacetRange.facetConfiguration</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String FACETCONFIGURATION = "facetConfiguration";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AsFacetRangeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AsFacetRangeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _facetConfiguration initial attribute declared by type <code>AsFacetRange</code> at extension <code>adaptivesearch</code>
	 * @param _from initial attribute declared by type <code>AsFacetRange</code> at extension <code>adaptivesearch</code>
	 * @param _id initial attribute declared by type <code>AsFacetRange</code> at extension <code>adaptivesearch</code>
	 * @param _qualifier initial attribute declared by type <code>AsFacetRange</code> at extension <code>adaptivesearch</code>
	 * @param _to initial attribute declared by type <code>AsFacetRange</code> at extension <code>adaptivesearch</code>
	 * @param _uid initial attribute declared by type <code>AbstractAsConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _uniqueIdx initial attribute declared by type <code>AsFacetRange</code> at extension <code>adaptivesearch</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public AsFacetRangeModel(final AbstractAsFacetConfigurationModel _facetConfiguration, final String _from, final String _id, final String _qualifier, final String _to, final String _uid, final String _uniqueIdx)
	{
		super();
		setFacetConfiguration(_facetConfiguration);
		setFrom(_from);
		setId(_id);
		setQualifier(_qualifier);
		setTo(_to);
		setUid(_uid);
		setUniqueIdx(_uniqueIdx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>AbstractAsConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _facetConfiguration initial attribute declared by type <code>AsFacetRange</code> at extension <code>adaptivesearch</code>
	 * @param _from initial attribute declared by type <code>AsFacetRange</code> at extension <code>adaptivesearch</code>
	 * @param _id initial attribute declared by type <code>AsFacetRange</code> at extension <code>adaptivesearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>AsFacetRange</code> at extension <code>adaptivesearch</code>
	 * @param _to initial attribute declared by type <code>AsFacetRange</code> at extension <code>adaptivesearch</code>
	 * @param _uid initial attribute declared by type <code>AbstractAsConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _uniqueIdx initial attribute declared by type <code>AsFacetRange</code> at extension <code>adaptivesearch</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public AsFacetRangeModel(final CatalogVersionModel _catalogVersion, final AbstractAsFacetConfigurationModel _facetConfiguration, final String _from, final String _id, final ItemModel _owner, final String _qualifier, final String _to, final String _uid, final String _uniqueIdx)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setFacetConfiguration(_facetConfiguration);
		setFrom(_from);
		setId(_id);
		setOwner(_owner);
		setQualifier(_qualifier);
		setTo(_to);
		setUid(_uid);
		setUniqueIdx(_uniqueIdx);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AsFacetRange.facetConfiguration</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the facetConfiguration
	 */
	@Accessor(qualifier = "facetConfiguration", type = Accessor.Type.GETTER)
	public AbstractAsFacetConfigurationModel getFacetConfiguration()
	{
		return getPersistenceContext().getPropertyValue(FACETCONFIGURATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AsFacetRange.from</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the from
	 */
	@Accessor(qualifier = "from", type = Accessor.Type.GETTER)
	public String getFrom()
	{
		return getPersistenceContext().getPropertyValue(FROM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AsFacetRange.id</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AsFacetRange.name</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AsFacetRange.name</code> attribute defined at extension <code>adaptivesearch</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>AsFacetRange.qualifier</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the qualifier
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
	public String getQualifier()
	{
		return getPersistenceContext().getPropertyValue(QUALIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AsFacetRange.to</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the to
	 */
	@Accessor(qualifier = "to", type = Accessor.Type.GETTER)
	public String getTo()
	{
		return getPersistenceContext().getPropertyValue(TO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AsFacetRange.uniqueIdx</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the uniqueIdx
	 */
	@Accessor(qualifier = "uniqueIdx", type = Accessor.Type.GETTER)
	public String getUniqueIdx()
	{
		return getPersistenceContext().getPropertyValue(UNIQUEIDX);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AsFacetRange.facetConfiguration</code> attribute defined at extension <code>adaptivesearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the facetConfiguration
	 */
	@Accessor(qualifier = "facetConfiguration", type = Accessor.Type.SETTER)
	public void setFacetConfiguration(final AbstractAsFacetConfigurationModel value)
	{
		getPersistenceContext().setPropertyValue(FACETCONFIGURATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AsFacetRange.from</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the from
	 */
	@Accessor(qualifier = "from", type = Accessor.Type.SETTER)
	public void setFrom(final String value)
	{
		getPersistenceContext().setPropertyValue(FROM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AsFacetRange.id</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AsFacetRange.name</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AsFacetRange.name</code> attribute defined at extension <code>adaptivesearch</code>. 
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
	 * <i>Generated method</i> - Setter of <code>AsFacetRange.qualifier</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the qualifier
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
	public void setQualifier(final String value)
	{
		getPersistenceContext().setPropertyValue(QUALIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AsFacetRange.to</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the to
	 */
	@Accessor(qualifier = "to", type = Accessor.Type.SETTER)
	public void setTo(final String value)
	{
		getPersistenceContext().setPropertyValue(TO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AsFacetRange.uniqueIdx</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the uniqueIdx
	 */
	@Accessor(qualifier = "uniqueIdx", type = Accessor.Type.SETTER)
	public void setUniqueIdx(final String value)
	{
		getPersistenceContext().setPropertyValue(UNIQUEIDX, value);
	}
	
}
