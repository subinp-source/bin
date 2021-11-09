/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.xstream.conv;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterMatcher;


/**
 * Abstraction for the type converter mapping. Mapping entry which is being used to find a {@link Converter} while
 * marshaling/unmarshaling of type {@link #getAliasedClass()}.
 * 
 * <pre>
 * {@code}
 * final XStream xstream  = .... ;
 * xstream.registerLocalConverter({@link #getAliasedClass()},{@link #getConverter()});
 * {@code}
 * </pre>
 * 
 * Such spring configuration
 * 
 * <pre>
 * {@code
 * 
 * 		<bean class="de.hybris.platform.commercefacades.xstream.conv.TypeConverterMapping" >
 *         <property name="aliasedClass" value="de.hybris.platform.commercefacades.product.data.SomeData" />
 *         <property name="converter" >
 *             <bean class="de.hybris.platform.commercewebservices.conv.SomeConverter" />
 *         </property>
 * 	</bean>
 * }
 * </pre>
 * 
 * , determines that a de.hybris.platform.commercewebservices.conv.SomeConverter will be used for
 * marshaling/unmarshaling SomeData's object.
 * 
 * 
 */
public class TypeConverterMapping
{
	private ConverterMatcher converter;

	private Class aliasedClass;


	public void setAliasedClass(final Class aliasedClass)
	{
		this.aliasedClass = aliasedClass;
	}

	public Class getAliasedClass()
	{
		return aliasedClass;
	}


	public void setConverter(final ConverterMatcher converter)
	{
		this.converter = converter;
	}


	public ConverterMatcher getConverter()
	{
		return converter;
	}
}
