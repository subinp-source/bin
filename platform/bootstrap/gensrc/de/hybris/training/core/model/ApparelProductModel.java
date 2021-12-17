/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 15-Dec-2021, 3:07:44 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.training.core.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type ApparelProduct first defined at extension trainingcore.
 * <p>
 * Base apparel product extension that contains additional attributes.
 */
@SuppressWarnings("all")
public class ApparelProductModel extends ProductModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ApparelProduct";
	
	/** <i>Generated constant</i> - Attribute key of <code>ApparelProduct.genders</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String GENDERS = "genders";
	
	/** <i>Generated constant</i> - Attribute key of <code>ApparelProduct.testAttribute</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String TESTATTRIBUTE = "testAttribute";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ApparelProductModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ApparelProductModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Product</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Product</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ApparelProductModel(final CatalogVersionModel _catalogVersion, final String _code)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Product</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Product</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ApparelProductModel(final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ApparelProduct.genders</code> attribute defined at extension <code>trainingcore</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the genders - List of genders that the ApparelProduct is designed for
	 */
	@Accessor(qualifier = "genders", type = Accessor.Type.GETTER)
	public List<Gender> getGenders()
	{
		return getPersistenceContext().getPropertyValue(GENDERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ApparelProduct.testAttribute</code> attribute defined at extension <code>trainingcore</code>. 
	 * @return the testAttribute - labelTestAttribute
	 */
	@Accessor(qualifier = "testAttribute", type = Accessor.Type.GETTER)
	public String getTestAttribute()
	{
		return getTestAttribute(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ApparelProduct.testAttribute</code> attribute defined at extension <code>trainingcore</code>. 
	 * @param loc the value localization key 
	 * @return the testAttribute - labelTestAttribute
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "testAttribute", type = Accessor.Type.GETTER)
	public String getTestAttribute(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(TESTATTRIBUTE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ApparelProduct.genders</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the genders - List of genders that the ApparelProduct is designed for
	 */
	@Accessor(qualifier = "genders", type = Accessor.Type.SETTER)
	public void setGenders(final List<Gender> value)
	{
		getPersistenceContext().setPropertyValue(GENDERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ApparelProduct.testAttribute</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the testAttribute - labelTestAttribute
	 */
	@Accessor(qualifier = "testAttribute", type = Accessor.Type.SETTER)
	public void setTestAttribute(final String value)
	{
		setTestAttribute(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ApparelProduct.testAttribute</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the testAttribute - labelTestAttribute
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "testAttribute", type = Accessor.Type.SETTER)
	public void setTestAttribute(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(TESTATTRIBUTE, loc, value);
	}
	
}
