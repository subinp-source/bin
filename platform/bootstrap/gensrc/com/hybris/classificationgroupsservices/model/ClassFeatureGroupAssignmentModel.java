/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 */
package com.hybris.classificationgroupsservices.model;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupModel;
import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ClassFeatureGroupAssignment first defined at extension classificationgroupsservices.
 */
@SuppressWarnings("all")
public class ClassFeatureGroupAssignmentModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ClassFeatureGroupAssignment";
	
	/**<i>Generated relation code constant for relation <code>ClassFeatureGroup2ClassFeatureGroupAssignmentRelation</code> defining source attribute <code>classFeatureGroup</code> in extension <code>classificationgroupsservices</code>.</i>*/
	public static final String _CLASSFEATUREGROUP2CLASSFEATUREGROUPASSIGNMENTRELATION = "ClassFeatureGroup2ClassFeatureGroupAssignmentRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassFeatureGroupAssignment.index</code> attribute defined at extension <code>classificationgroupsservices</code>. */
	public static final String INDEX = "index";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassFeatureGroupAssignment.fullQualifier</code> attribute defined at extension <code>classificationgroupsservices</code>. */
	public static final String FULLQUALIFIER = "fullQualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassFeatureGroupAssignment.classAttributeAssignment</code> attribute defined at extension <code>classificationgroupsservices</code>. */
	public static final String CLASSATTRIBUTEASSIGNMENT = "classAttributeAssignment";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassFeatureGroupAssignment.classificationClass</code> attribute defined at extension <code>classificationgroupsservices</code>. */
	public static final String CLASSIFICATIONCLASS = "classificationClass";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassFeatureGroupAssignment.classFeatureGroupPOS</code> attribute defined at extension <code>classificationgroupsservices</code>. */
	public static final String CLASSFEATUREGROUPPOS = "classFeatureGroupPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassFeatureGroupAssignment.classFeatureGroup</code> attribute defined at extension <code>classificationgroupsservices</code>. */
	public static final String CLASSFEATUREGROUP = "classFeatureGroup";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ClassFeatureGroupAssignmentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ClassFeatureGroupAssignmentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _classAttributeAssignment initial attribute declared by type <code>ClassFeatureGroupAssignment</code> at extension <code>classificationgroupsservices</code>
	 * @param _classificationClass initial attribute declared by type <code>ClassFeatureGroupAssignment</code> at extension <code>classificationgroupsservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ClassFeatureGroupAssignmentModel(final ClassAttributeAssignmentModel _classAttributeAssignment, final ClassificationClassModel _classificationClass)
	{
		super();
		setClassAttributeAssignment(_classAttributeAssignment);
		setClassificationClass(_classificationClass);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _classAttributeAssignment initial attribute declared by type <code>ClassFeatureGroupAssignment</code> at extension <code>classificationgroupsservices</code>
	 * @param _classificationClass initial attribute declared by type <code>ClassFeatureGroupAssignment</code> at extension <code>classificationgroupsservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ClassFeatureGroupAssignmentModel(final ClassAttributeAssignmentModel _classAttributeAssignment, final ClassificationClassModel _classificationClass, final ItemModel _owner)
	{
		super();
		setClassAttributeAssignment(_classAttributeAssignment);
		setClassificationClass(_classificationClass);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassFeatureGroupAssignment.classAttributeAssignment</code> attribute defined at extension <code>classificationgroupsservices</code>. 
	 * @return the classAttributeAssignment - ClassAttributeAssignment which is wrapped by ClassFeatureGroupAssignment
	 */
	@Accessor(qualifier = "classAttributeAssignment", type = Accessor.Type.GETTER)
	public ClassAttributeAssignmentModel getClassAttributeAssignment()
	{
		return getPersistenceContext().getPropertyValue(CLASSATTRIBUTEASSIGNMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassFeatureGroupAssignment.classFeatureGroup</code> attribute defined at extension <code>classificationgroupsservices</code>. 
	 * @return the classFeatureGroup
	 */
	@Accessor(qualifier = "classFeatureGroup", type = Accessor.Type.GETTER)
	public ClassFeatureGroupModel getClassFeatureGroup()
	{
		return getPersistenceContext().getPropertyValue(CLASSFEATUREGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassFeatureGroupAssignment.classificationClass</code> attribute defined at extension <code>classificationgroupsservices</code>. 
	 * @return the classificationClass - ClassificationClass of ClassFeatureGroupAssignment
	 */
	@Accessor(qualifier = "classificationClass", type = Accessor.Type.GETTER)
	public ClassificationClassModel getClassificationClass()
	{
		return getPersistenceContext().getPropertyValue(CLASSIFICATIONCLASS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassFeatureGroupAssignment.fullQualifier</code> dynamic attribute defined at extension <code>classificationgroupsservices</code>. 
	 * @return the fullQualifier - Full qualifier of ClassAttributeAssignment
	 */
	@Accessor(qualifier = "fullQualifier", type = Accessor.Type.GETTER)
	public String getFullQualifier()
	{
		return getPersistenceContext().getDynamicValue(this,FULLQUALIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassFeatureGroupAssignment.index</code> dynamic attribute defined at extension <code>classificationgroupsservices</code>. 
	 * @return the index - Index of ClassFeatureGroupAssignment in ClassFeatureGroup
	 */
	@Accessor(qualifier = "index", type = Accessor.Type.GETTER)
	public Integer getIndex()
	{
		return getPersistenceContext().getDynamicValue(this,INDEX);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassFeatureGroupAssignment.classAttributeAssignment</code> attribute defined at extension <code>classificationgroupsservices</code>. 
	 *  
	 * @param value the classAttributeAssignment - ClassAttributeAssignment which is wrapped by ClassFeatureGroupAssignment
	 */
	@Accessor(qualifier = "classAttributeAssignment", type = Accessor.Type.SETTER)
	public void setClassAttributeAssignment(final ClassAttributeAssignmentModel value)
	{
		getPersistenceContext().setPropertyValue(CLASSATTRIBUTEASSIGNMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassFeatureGroupAssignment.classFeatureGroup</code> attribute defined at extension <code>classificationgroupsservices</code>. 
	 *  
	 * @param value the classFeatureGroup
	 */
	@Accessor(qualifier = "classFeatureGroup", type = Accessor.Type.SETTER)
	public void setClassFeatureGroup(final ClassFeatureGroupModel value)
	{
		getPersistenceContext().setPropertyValue(CLASSFEATUREGROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassFeatureGroupAssignment.classificationClass</code> attribute defined at extension <code>classificationgroupsservices</code>. 
	 *  
	 * @param value the classificationClass - ClassificationClass of ClassFeatureGroupAssignment
	 */
	@Accessor(qualifier = "classificationClass", type = Accessor.Type.SETTER)
	public void setClassificationClass(final ClassificationClassModel value)
	{
		getPersistenceContext().setPropertyValue(CLASSIFICATIONCLASS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassFeatureGroupAssignment.fullQualifier</code> dynamic attribute defined at extension <code>classificationgroupsservices</code>. 
	 *  
	 * @param value the fullQualifier - Full qualifier of ClassAttributeAssignment
	 */
	@Accessor(qualifier = "fullQualifier", type = Accessor.Type.SETTER)
	public void setFullQualifier(final String value)
	{
		getPersistenceContext().setDynamicValue(this,FULLQUALIFIER, value);
	}
	
}
