/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.xstream.alias;

/**
 * Abstraction for the type aliasing. Provides a mapping which is used for marshaling/unmarshaling a type of data
 * object.
 * 
 * <pre>
 * {@code}
 * final XStream xstream  = .... ;
 * xstream.alias({@link #getAlias()},{@link #getAliasedClass()});
 * {@code}
 * </pre>
 * 
 * Such spring configuration
 * 
 * <pre>
 * {@code
 * <bean class="de.hybris.platform.commercefacades.xstream.alias.TypeAliasMapping">
 * <property name="alias"  value="someAlias" />
 * <property name="aliasedClass"  value="de.hybris.platform.commercefacades.product.data.SomeData" />
 * </bean>
 * }
 * </pre>
 * 
 * ,results which such response
 * 
 * <pre>
 * {@code
 * <someAlias>
 * ...
 * </someAlias>
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
 * </de.hybris.platform.commercefacades.product.data.SomeData>
 * }
 * </pre>
 * 
 * 
 */
public class TypeAliasMapping
{
	private String alias;

	private Class aliasedClass;


	public void setAlias(final String alias)
	{
		this.alias = alias;
	}


	public String getAlias()
	{
		return alias;
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
