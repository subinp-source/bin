/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 */
package com.hybris.classificationgroupsservices.model;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type ClassFeatureGroup first defined at extension classificationgroupsservices.
 */
@SuppressWarnings("all")
public class ClassFeatureGroupModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ClassFeatureGroup";
	
	/**<i>Generated relation code constant for relation <code>ClassificationClass2ClassFeatureGroupRelation</code> defining source attribute <code>classificationClass</code> in extension <code>classificationgroupsservices</code>.</i>*/
	public static final String _CLASSIFICATIONCLASS2CLASSFEATUREGROUPRELATION = "ClassificationClass2ClassFeatureGroupRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassFeatureGroup.code</code> attribute defined at extension <code>classificationgroupsservices</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassFeatureGroup.name</code> attribute defined at extension <code>classificationgroupsservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassFeatureGroup.index</code> attribute defined at extension <code>classificationgroupsservices</code>. */
	public static final String INDEX = "index";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassFeatureGroup.classFeatureGroupAssignments</code> attribute defined at extension <code>classificationgroupsservices</code>. */
	public static final String CLASSFEATUREGROUPASSIGNMENTS = "classFeatureGroupAssignments";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassFeatureGroup.classificationClassPOS</code> attribute defined at extension <code>classificationgroupsservices</code>. */
	public static final String CLASSIFICATIONCLASSPOS = "classificationClassPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassFeatureGroup.classificationClass</code> attribute defined at extension <code>classificationgroupsservices</code>. */
	public static final String CLASSIFICATIONCLASS = "classificationClass";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ClassFeatureGroupModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ClassFeatureGroupModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _classificationClass initial attribute declared by type <code>ClassFeatureGroup</code> at extension <code>classificationgroupsservices</code>
	 * @param _code initial attribute declared by type <code>ClassFeatureGroup</code> at extension <code>classificationgroupsservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ClassFeatureGroupModel(final ClassificationClassModel _classificationClass, final String _code)
	{
		super();
		setClassificationClass(_classificationClass);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _classificationClass initial attribute declared by type <code>ClassFeatureGroup</code> at extension <code>classificationgroupsservices</code>
	 * @param _code initial attribute declared by type <code>ClassFeatureGroup</code> at extension <code>classificationgroupsservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ClassFeatureGroupModel(final ClassificationClassModel _classificationClass, final String _code, final ItemModel _owner)
	{
		super();
		setClassificationClass(_classificationClass);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassFeatureGroup.classFeatureGroupAssignments</code> attribute defined at extension <code>classificationgroupsservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the classFeatureGroupAssignments
	 */
	@Accessor(qualifier = "classFeatureGroupAssignments", type = Accessor.Type.GETTER)
	public List<ClassFeatureGroupAssignmentModel> getClassFeatureGroupAssignments()
	{
		return getPersistenceContext().getPropertyValue(CLASSFEATUREGROUPASSIGNMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassFeatureGroup.classificationClass</code> attribute defined at extension <code>classificationgroupsservices</code>. 
	 * @return the classificationClass
	 */
	@Accessor(qualifier = "classificationClass", type = Accessor.Type.GETTER)
	public ClassificationClassModel getClassificationClass()
	{
		return getPersistenceContext().getPropertyValue(CLASSIFICATIONCLASS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassFeatureGroup.code</code> attribute defined at extension <code>classificationgroupsservices</code>. 
	 * @return the code - Code of ClassFeatureGroup
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassFeatureGroup.index</code> dynamic attribute defined at extension <code>classificationgroupsservices</code>. 
	 * @return the index - Index of group in ClassificationClass
	 */
	@Accessor(qualifier = "index", type = Accessor.Type.GETTER)
	public Integer getIndex()
	{
		return getPersistenceContext().getDynamicValue(this,INDEX);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassFeatureGroup.name</code> attribute defined at extension <code>classificationgroupsservices</code>. 
	 * @return the name - Localized name of ClassFeatureGroup
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassFeatureGroup.name</code> attribute defined at extension <code>classificationgroupsservices</code>. 
	 * @param loc the value localization key 
	 * @return the name - Localized name of ClassFeatureGroup
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassFeatureGroup.classFeatureGroupAssignments</code> attribute defined at extension <code>classificationgroupsservices</code>. 
	 *  
	 * @param value the classFeatureGroupAssignments
	 */
	@Accessor(qualifier = "classFeatureGroupAssignments", type = Accessor.Type.SETTER)
	public void setClassFeatureGroupAssignments(final List<ClassFeatureGroupAssignmentModel> value)
	{
		getPersistenceContext().setPropertyValue(CLASSFEATUREGROUPASSIGNMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassFeatureGroup.classificationClass</code> attribute defined at extension <code>classificationgroupsservices</code>. 
	 *  
	 * @param value the classificationClass
	 */
	@Accessor(qualifier = "classificationClass", type = Accessor.Type.SETTER)
	public void setClassificationClass(final ClassificationClassModel value)
	{
		getPersistenceContext().setPropertyValue(CLASSIFICATIONCLASS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassFeatureGroup.code</code> attribute defined at extension <code>classificationgroupsservices</code>. 
	 *  
	 * @param value the code - Code of ClassFeatureGroup
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassFeatureGroup.name</code> attribute defined at extension <code>classificationgroupsservices</code>. 
	 *  
	 * @param value the name - Localized name of ClassFeatureGroup
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ClassFeatureGroup.name</code> attribute defined at extension <code>classificationgroupsservices</code>. 
	 *  
	 * @param value the name - Localized name of ClassFeatureGroup
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
}
