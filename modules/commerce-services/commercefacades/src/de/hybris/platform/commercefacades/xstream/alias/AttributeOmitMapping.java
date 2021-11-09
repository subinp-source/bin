/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.xstream.alias;

/**
 * Abstraction for the omitting specified field. Provides a mapping which is used for marshaling/unmarshaling a field of
 * type for data object.
 * 
 * <pre>
 * {@code}
 * final XStream xstream  = .... ;
 * xstream.omitField({@link #getAliasedClass()},{@link #getAttributeName()});
 * {@code}
 * </pre>
 * 
 * Such spring configuration
 * 
 * <pre>
 * {@code
 * <bean class="de.hybris.platform.commercefacades.xstream.alias.AttributeOmitMapping">
 * <property name="attributeName"  value="someAttribute" />
 * <property name="aliasedClass"  value="de.hybris.platform.commercefacades.product.data.SomeData" />
 * </bean>
 * }
 * </pre>
 * 
 * ,results which such response
 * 
 * <pre>
 * {@code
 * <de.hybris.platform.commercefacades.product.data.SomeData>
 * ...
 * </de.hybris.platform.commercefacades.product.data.SomeData>
 * }
 * </pre>
 * 
 * instead of
 * 
 * ,
 * 
 * <pre>
 * {@code
 * <de.hybris.platform.commercefacades.product.data.SomeData>
 * ...
 * 	<someAttribute>
 *  	....
 * 	</someAttribute> 
 * </de.hybris.platform.commercefacades.product.data.SomeData>
 * }
 * </pre>
 * 
 * 
 */
public class AttributeOmitMapping
{
	private String attributeName;

	private Class aliasedClass;

	public String getAttributeName()
	{
		return attributeName;
	}

	public void setAttributeName(final String attributeName)
	{
		this.attributeName = attributeName;
	}

	public void setAliasedClass(final Class aliasedClass)
	{
		this.aliasedClass = aliasedClass;
	}

	public Class getAliasedClass()
	{
		return aliasedClass;
	}
}
