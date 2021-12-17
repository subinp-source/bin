/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 15-Dec-2021, 3:07:44 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type CustomerCoupon first defined at extension customercouponservices.
 */
@SuppressWarnings("all")
public class CustomerCouponModel extends AbstractCouponModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CustomerCoupon";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerCoupon.assignable</code> attribute defined at extension <code>customercouponservices</code>. */
	public static final String ASSIGNABLE = "assignable";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerCoupon.description</code> attribute defined at extension <code>customercouponservices</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerCoupon.customers</code> attribute defined at extension <code>customercouponservices</code>. */
	public static final String CUSTOMERS = "customers";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CustomerCouponModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CustomerCouponModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _couponId initial attribute declared by type <code>AbstractCoupon</code> at extension <code>couponservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public CustomerCouponModel(final String _couponId)
	{
		super();
		setCouponId(_couponId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _couponId initial attribute declared by type <code>AbstractCoupon</code> at extension <code>couponservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public CustomerCouponModel(final String _couponId, final ItemModel _owner)
	{
		super();
		setCouponId(_couponId);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCoupon.assignable</code> attribute defined at extension <code>customercouponservices</code>. 
	 * @return the assignable
	 */
	@Accessor(qualifier = "assignable", type = Accessor.Type.GETTER)
	public Boolean getAssignable()
	{
		return getPersistenceContext().getPropertyValue(ASSIGNABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCoupon.customers</code> attribute defined at extension <code>customercouponservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the customers - Customers
	 */
	@Accessor(qualifier = "customers", type = Accessor.Type.GETTER)
	public Collection<CustomerModel> getCustomers()
	{
		return getPersistenceContext().getPropertyValue(CUSTOMERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCoupon.description</code> attribute defined at extension <code>customercouponservices</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCoupon.description</code> attribute defined at extension <code>customercouponservices</code>. 
	 * @param loc the value localization key 
	 * @return the description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CustomerCoupon.assignable</code> attribute defined at extension <code>customercouponservices</code>. 
	 *  
	 * @param value the assignable
	 */
	@Accessor(qualifier = "assignable", type = Accessor.Type.SETTER)
	public void setAssignable(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ASSIGNABLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CustomerCoupon.customers</code> attribute defined at extension <code>customercouponservices</code>. 
	 *  
	 * @param value the customers - Customers
	 */
	@Accessor(qualifier = "customers", type = Accessor.Type.SETTER)
	public void setCustomers(final Collection<CustomerModel> value)
	{
		getPersistenceContext().setPropertyValue(CUSTOMERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CustomerCoupon.description</code> attribute defined at extension <code>customercouponservices</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>CustomerCoupon.description</code> attribute defined at extension <code>customercouponservices</code>. 
	 *  
	 * @param value the description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
}
