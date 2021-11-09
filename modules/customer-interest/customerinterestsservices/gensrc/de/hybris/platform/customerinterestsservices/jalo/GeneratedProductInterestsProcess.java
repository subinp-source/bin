/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsservices.jalo;

import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.customerinterestsservices.constants.CustomerinterestsservicesConstants;
import de.hybris.platform.customerinterestsservices.jalo.ProductInterest;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.processengine.jalo.BusinessProcess;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.processengine.jalo.BusinessProcess ProductInterestsProcess}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedProductInterestsProcess extends BusinessProcess
{
	/** Qualifier of the <code>ProductInterestsProcess.language</code> attribute **/
	public static final String LANGUAGE = "language";
	/** Qualifier of the <code>ProductInterestsProcess.productInterest</code> attribute **/
	public static final String PRODUCTINTEREST = "productInterest";
	/** Qualifier of the <code>ProductInterestsProcess.Customer</code> attribute **/
	public static final String CUSTOMER = "Customer";
	/** Qualifier of the <code>ProductInterestsProcess.product</code> attribute **/
	public static final String PRODUCT = "product";
	/** Qualifier of the <code>ProductInterestsProcess.baseSite</code> attribute **/
	public static final String BASESITE = "baseSite";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(BusinessProcess.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(LANGUAGE, AttributeMode.INITIAL);
		tmp.put(PRODUCTINTEREST, AttributeMode.INITIAL);
		tmp.put(CUSTOMER, AttributeMode.INITIAL);
		tmp.put(PRODUCT, AttributeMode.INITIAL);
		tmp.put(BASESITE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterestsProcess.baseSite</code> attribute.
	 * @return the baseSite - Attribute contains the base site that will be used in the process
	 */
	public BaseSite getBaseSite(final SessionContext ctx)
	{
		return (BaseSite)getProperty( ctx, BASESITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterestsProcess.baseSite</code> attribute.
	 * @return the baseSite - Attribute contains the base site that will be used in the process
	 */
	public BaseSite getBaseSite()
	{
		return getBaseSite( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterestsProcess.baseSite</code> attribute. 
	 * @param value the baseSite - Attribute contains the base site that will be used in the process
	 */
	public void setBaseSite(final SessionContext ctx, final BaseSite value)
	{
		setProperty(ctx, BASESITE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterestsProcess.baseSite</code> attribute. 
	 * @param value the baseSite - Attribute contains the base site that will be used in the process
	 */
	public void setBaseSite(final BaseSite value)
	{
		setBaseSite( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterestsProcess.Customer</code> attribute.
	 * @return the Customer - Attribute contains customer will be notified in the process.
	 */
	public Customer getCustomer(final SessionContext ctx)
	{
		return (Customer)getProperty( ctx, CUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterestsProcess.Customer</code> attribute.
	 * @return the Customer - Attribute contains customer will be notified in the process.
	 */
	public Customer getCustomer()
	{
		return getCustomer( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterestsProcess.Customer</code> attribute. 
	 * @param value the Customer - Attribute contains customer will be notified in the process.
	 */
	public void setCustomer(final SessionContext ctx, final Customer value)
	{
		setProperty(ctx, CUSTOMER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterestsProcess.Customer</code> attribute. 
	 * @param value the Customer - Attribute contains customer will be notified in the process.
	 */
	public void setCustomer(final Customer value)
	{
		setCustomer( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterestsProcess.language</code> attribute.
	 * @return the language - Attribute contains language that will be used in the process.
	 */
	public Language getLanguage(final SessionContext ctx)
	{
		return (Language)getProperty( ctx, LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterestsProcess.language</code> attribute.
	 * @return the language - Attribute contains language that will be used in the process.
	 */
	public Language getLanguage()
	{
		return getLanguage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterestsProcess.language</code> attribute. 
	 * @param value the language - Attribute contains language that will be used in the process.
	 */
	public void setLanguage(final SessionContext ctx, final Language value)
	{
		setProperty(ctx, LANGUAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterestsProcess.language</code> attribute. 
	 * @param value the language - Attribute contains language that will be used in the process.
	 */
	public void setLanguage(final Language value)
	{
		setLanguage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterestsProcess.product</code> attribute.
	 * @return the product - Attribute contains the product that will be used in the process
	 */
	public Product getProduct(final SessionContext ctx)
	{
		return (Product)getProperty( ctx, PRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterestsProcess.product</code> attribute.
	 * @return the product - Attribute contains the product that will be used in the process
	 */
	public Product getProduct()
	{
		return getProduct( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterestsProcess.product</code> attribute. 
	 * @param value the product - Attribute contains the product that will be used in the process
	 */
	public void setProduct(final SessionContext ctx, final Product value)
	{
		setProperty(ctx, PRODUCT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterestsProcess.product</code> attribute. 
	 * @param value the product - Attribute contains the product that will be used in the process
	 */
	public void setProduct(final Product value)
	{
		setProduct( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterestsProcess.productInterest</code> attribute.
	 * @return the productInterest - Attribute contains the product back in stock interests. Deprecated since 1905.
	 */
	public ProductInterest getProductInterest(final SessionContext ctx)
	{
		return (ProductInterest)getProperty( ctx, PRODUCTINTEREST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterestsProcess.productInterest</code> attribute.
	 * @return the productInterest - Attribute contains the product back in stock interests. Deprecated since 1905.
	 */
	public ProductInterest getProductInterest()
	{
		return getProductInterest( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterestsProcess.productInterest</code> attribute. 
	 * @param value the productInterest - Attribute contains the product back in stock interests. Deprecated since 1905.
	 */
	public void setProductInterest(final SessionContext ctx, final ProductInterest value)
	{
		setProperty(ctx, PRODUCTINTEREST,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductInterestsProcess.productInterest</code> attribute. 
	 * @param value the productInterest - Attribute contains the product back in stock interests. Deprecated since 1905.
	 */
	public void setProductInterest(final ProductInterest value)
	{
		setProductInterest( getSession().getSessionContext(), value );
	}
	
}
