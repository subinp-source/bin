/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.training.core.jalo;

import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.training.core.constants.TrainingCoreConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.training.core.jalo.ApparelProduct ApparelProduct}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedApparelProduct extends Product
{
	/** Qualifier of the <code>ApparelProduct.genders</code> attribute **/
	public static final String GENDERS = "genders";
	/** Qualifier of the <code>ApparelProduct.testAttribute</code> attribute **/
	public static final String TESTATTRIBUTE = "testAttribute";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(Product.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(GENDERS, AttributeMode.INITIAL);
		tmp.put(TESTATTRIBUTE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ApparelProduct.genders</code> attribute.
	 * @return the genders - List of genders that the ApparelProduct is designed for
	 */
	public List<EnumerationValue> getGenders(final SessionContext ctx)
	{
		List<EnumerationValue> coll = (List<EnumerationValue>)getProperty( ctx, GENDERS);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ApparelProduct.genders</code> attribute.
	 * @return the genders - List of genders that the ApparelProduct is designed for
	 */
	public List<EnumerationValue> getGenders()
	{
		return getGenders( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ApparelProduct.genders</code> attribute. 
	 * @param value the genders - List of genders that the ApparelProduct is designed for
	 */
	public void setGenders(final SessionContext ctx, final List<EnumerationValue> value)
	{
		setProperty(ctx, GENDERS,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ApparelProduct.genders</code> attribute. 
	 * @param value the genders - List of genders that the ApparelProduct is designed for
	 */
	public void setGenders(final List<EnumerationValue> value)
	{
		setGenders( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ApparelProduct.testAttribute</code> attribute.
	 * @return the testAttribute - labelTestAttribute
	 */
	public String getTestAttribute(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedApparelProduct.getTestAttribute requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, TESTATTRIBUTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ApparelProduct.testAttribute</code> attribute.
	 * @return the testAttribute - labelTestAttribute
	 */
	public String getTestAttribute()
	{
		return getTestAttribute( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ApparelProduct.testAttribute</code> attribute. 
	 * @return the localized testAttribute - labelTestAttribute
	 */
	public Map<Language,String> getAllTestAttribute(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,TESTATTRIBUTE,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ApparelProduct.testAttribute</code> attribute. 
	 * @return the localized testAttribute - labelTestAttribute
	 */
	public Map<Language,String> getAllTestAttribute()
	{
		return getAllTestAttribute( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ApparelProduct.testAttribute</code> attribute. 
	 * @param value the testAttribute - labelTestAttribute
	 */
	public void setTestAttribute(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedApparelProduct.setTestAttribute requires a session language", 0 );
		}
		setLocalizedProperty(ctx, TESTATTRIBUTE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ApparelProduct.testAttribute</code> attribute. 
	 * @param value the testAttribute - labelTestAttribute
	 */
	public void setTestAttribute(final String value)
	{
		setTestAttribute( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ApparelProduct.testAttribute</code> attribute. 
	 * @param value the testAttribute - labelTestAttribute
	 */
	public void setAllTestAttribute(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,TESTATTRIBUTE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ApparelProduct.testAttribute</code> attribute. 
	 * @param value the testAttribute - labelTestAttribute
	 */
	public void setAllTestAttribute(final Map<Language,String> value)
	{
		setAllTestAttribute( getSession().getSessionContext(), value );
	}
	
}
