/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 */
package com.hybris.merchandising.jalo;

import com.hybris.merchandising.constants.MerchandisingservicesConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrIndexedProperty;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem AbstractMerchProperty}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedAbstractMerchProperty extends GenericItem
{
	/** Qualifier of the <code>AbstractMerchProperty.indexedProperty</code> attribute **/
	public static final String INDEXEDPROPERTY = "indexedProperty";
	/** Qualifier of the <code>AbstractMerchProperty.merchMappedName</code> attribute **/
	public static final String MERCHMAPPEDNAME = "merchMappedName";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(INDEXEDPROPERTY, AttributeMode.INITIAL);
		tmp.put(MERCHMAPPEDNAME, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractMerchProperty.indexedProperty</code> attribute.
	 * @return the indexedProperty - Unique identifier
	 */
	public SolrIndexedProperty getIndexedProperty(final SessionContext ctx)
	{
		return (SolrIndexedProperty)getProperty( ctx, INDEXEDPROPERTY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractMerchProperty.indexedProperty</code> attribute.
	 * @return the indexedProperty - Unique identifier
	 */
	public SolrIndexedProperty getIndexedProperty()
	{
		return getIndexedProperty( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractMerchProperty.indexedProperty</code> attribute. 
	 * @param value the indexedProperty - Unique identifier
	 */
	public void setIndexedProperty(final SessionContext ctx, final SolrIndexedProperty value)
	{
		setProperty(ctx, INDEXEDPROPERTY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractMerchProperty.indexedProperty</code> attribute. 
	 * @param value the indexedProperty - Unique identifier
	 */
	public void setIndexedProperty(final SolrIndexedProperty value)
	{
		setIndexedProperty( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractMerchProperty.merchMappedName</code> attribute.
	 * @return the merchMappedName - Indexed type
	 */
	public String getMerchMappedName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, MERCHMAPPEDNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractMerchProperty.merchMappedName</code> attribute.
	 * @return the merchMappedName - Indexed type
	 */
	public String getMerchMappedName()
	{
		return getMerchMappedName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractMerchProperty.merchMappedName</code> attribute. 
	 * @param value the merchMappedName - Indexed type
	 */
	public void setMerchMappedName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, MERCHMAPPEDNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractMerchProperty.merchMappedName</code> attribute. 
	 * @param value the merchMappedName - Indexed type
	 */
	public void setMerchMappedName(final String value)
	{
		setMerchMappedName( getSession().getSessionContext(), value );
	}
	
}
