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
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.enums.RegexpFlag;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;
import java.util.Set;

/**
 * Generated model class for type SnCollectionPatternConstraint first defined at extension searchservices.
 */
@SuppressWarnings("all")
public class SnCollectionPatternConstraintModel extends AttributeConstraintModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SnCollectionPatternConstraint";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnCollectionPatternConstraint.regexp</code> attribute defined at extension <code>searchservices</code>. */
	public static final String REGEXP = "regexp";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnCollectionPatternConstraint.flags</code> attribute defined at extension <code>searchservices</code>. */
	public static final String FLAGS = "flags";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SnCollectionPatternConstraintModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SnCollectionPatternConstraintModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _annotation initial attribute declared by type <code>SnCollectionPatternConstraint</code> at extension <code>searchservices</code>
	 * @param _flags initial attribute declared by type <code>SnCollectionPatternConstraint</code> at extension <code>searchservices</code>
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _regexp initial attribute declared by type <code>SnCollectionPatternConstraint</code> at extension <code>searchservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public SnCollectionPatternConstraintModel(final Class _annotation, final Set<RegexpFlag> _flags, final String _id, final String _regexp)
	{
		super();
		setAnnotation(_annotation);
		setFlags(_flags);
		setId(_id);
		setRegexp(_regexp);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _annotation initial attribute declared by type <code>SnCollectionPatternConstraint</code> at extension <code>searchservices</code>
	 * @param _flags initial attribute declared by type <code>SnCollectionPatternConstraint</code> at extension <code>searchservices</code>
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _regexp initial attribute declared by type <code>SnCollectionPatternConstraint</code> at extension <code>searchservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public SnCollectionPatternConstraintModel(final Class _annotation, final Set<RegexpFlag> _flags, final String _id, final ItemModel _owner, final String _regexp)
	{
		super();
		setAnnotation(_annotation);
		setFlags(_flags);
		setId(_id);
		setOwner(_owner);
		setRegexp(_regexp);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnCollectionPatternConstraint.flags</code> attribute defined at extension <code>searchservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the flags
	 */
	@Accessor(qualifier = "flags", type = Accessor.Type.GETTER)
	public Set<RegexpFlag> getFlags()
	{
		return getPersistenceContext().getPropertyValue(FLAGS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnCollectionPatternConstraint.regexp</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the regexp
	 */
	@Accessor(qualifier = "regexp", type = Accessor.Type.GETTER)
	public String getRegexp()
	{
		return getPersistenceContext().getPropertyValue(REGEXP);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnCollectionPatternConstraint.flags</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the flags
	 */
	@Accessor(qualifier = "flags", type = Accessor.Type.SETTER)
	public void setFlags(final Set<RegexpFlag> value)
	{
		getPersistenceContext().setPropertyValue(FLAGS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnCollectionPatternConstraint.regexp</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the regexp
	 */
	@Accessor(qualifier = "regexp", type = Accessor.Type.SETTER)
	public void setRegexp(final String value)
	{
		getPersistenceContext().setPropertyValue(REGEXP, value);
	}
	
}
