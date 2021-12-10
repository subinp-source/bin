/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.TypeConstraintModel;

/**
 * Generated model class for type AbstractBundleRuleTargetProductsAssignedConstraint first defined at extension configurablebundleservices.
 * <p>
 * Custom constraint which checks that at least 1 target product is assigned to a bundle rule.
 */
@SuppressWarnings("all")
public class AbstractBundleRuleTargetProductsAssignedConstraintModel extends TypeConstraintModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractBundleRuleTargetProductsAssignedConstraint";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractBundleRuleTargetProductsAssignedConstraintModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractBundleRuleTargetProductsAssignedConstraintModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _annotation initial attribute declared by type <code>AbstractBundleRuleTargetProductsAssignedConstraint</code> at extension <code>configurablebundleservices</code>
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public AbstractBundleRuleTargetProductsAssignedConstraintModel(final Class _annotation, final String _id)
	{
		super();
		setAnnotation(_annotation);
		setId(_id);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _annotation initial attribute declared by type <code>AbstractBundleRuleTargetProductsAssignedConstraint</code> at extension <code>configurablebundleservices</code>
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public AbstractBundleRuleTargetProductsAssignedConstraintModel(final Class _annotation, final String _id, final ItemModel _owner)
	{
		super();
		setAnnotation(_annotation);
		setId(_id);
		setOwner(_owner);
	}
	
	
}
