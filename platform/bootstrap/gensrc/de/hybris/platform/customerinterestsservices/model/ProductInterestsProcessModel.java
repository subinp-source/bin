/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customerinterestsservices.model.ProductInterestModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ProductInterestsProcess first defined at extension customerinterestsservices.
 * <p>
 * Represents The Customer Interests Process.
 */
@SuppressWarnings("all")
public class ProductInterestsProcessModel extends BusinessProcessModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductInterestsProcess";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductInterestsProcess.language</code> attribute defined at extension <code>customerinterestsservices</code>. */
	public static final String LANGUAGE = "language";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductInterestsProcess.productInterest</code> attribute defined at extension <code>customerinterestsservices</code>. */
	public static final String PRODUCTINTEREST = "productInterest";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductInterestsProcess.Customer</code> attribute defined at extension <code>customerinterestsservices</code>. */
	public static final String CUSTOMER = "Customer";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductInterestsProcess.product</code> attribute defined at extension <code>customerinterestsservices</code>. */
	public static final String PRODUCT = "product";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductInterestsProcess.baseSite</code> attribute defined at extension <code>customerinterestsservices</code>. */
	public static final String BASESITE = "baseSite";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductInterestsProcessModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductInterestsProcessModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 * @param _processDefinitionName initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ProductInterestsProcessModel(final String _code, final String _processDefinitionName)
	{
		super();
		setCode(_code);
		setProcessDefinitionName(_processDefinitionName);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _processDefinitionName initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ProductInterestsProcessModel(final String _code, final ItemModel _owner, final String _processDefinitionName)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setProcessDefinitionName(_processDefinitionName);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterestsProcess.baseSite</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 * @return the baseSite - Attribute contains the base site that will be used in the process
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.GETTER)
	public BaseSiteModel getBaseSite()
	{
		return getPersistenceContext().getPropertyValue(BASESITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterestsProcess.Customer</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 * @return the Customer - Attribute contains customer will be notified in the process.
	 */
	@Accessor(qualifier = "Customer", type = Accessor.Type.GETTER)
	public CustomerModel getCustomer()
	{
		return getPersistenceContext().getPropertyValue(CUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterestsProcess.language</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 * @return the language - Attribute contains language that will be used in the process.
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public LanguageModel getLanguage()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterestsProcess.product</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 * @return the product - Attribute contains the product that will be used in the process
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.GETTER)
	public ProductModel getProduct()
	{
		return getPersistenceContext().getPropertyValue(PRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterestsProcess.productInterest</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 * @return the productInterest - Attribute contains the product back in stock interests. Deprecated since 1905.
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval = true)
	@Accessor(qualifier = "productInterest", type = Accessor.Type.GETTER)
	public ProductInterestModel getProductInterest()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTINTEREST);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductInterestsProcess.baseSite</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 *  
	 * @param value the baseSite - Attribute contains the base site that will be used in the process
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.SETTER)
	public void setBaseSite(final BaseSiteModel value)
	{
		getPersistenceContext().setPropertyValue(BASESITE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductInterestsProcess.Customer</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 *  
	 * @param value the Customer - Attribute contains customer will be notified in the process.
	 */
	@Accessor(qualifier = "Customer", type = Accessor.Type.SETTER)
	public void setCustomer(final CustomerModel value)
	{
		getPersistenceContext().setPropertyValue(CUSTOMER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductInterestsProcess.language</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 *  
	 * @param value the language - Attribute contains language that will be used in the process.
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductInterestsProcess.product</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 *  
	 * @param value the product - Attribute contains the product that will be used in the process
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.SETTER)
	public void setProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(PRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductInterestsProcess.productInterest</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 *  
	 * @param value the productInterest - Attribute contains the product back in stock interests. Deprecated since 1905.
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval = true)
	@Accessor(qualifier = "productInterest", type = Accessor.Type.SETTER)
	public void setProductInterest(final ProductInterestModel value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTINTEREST, value);
	}
	
}
