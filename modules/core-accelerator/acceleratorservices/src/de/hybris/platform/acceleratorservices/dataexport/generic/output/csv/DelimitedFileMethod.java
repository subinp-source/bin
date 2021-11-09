/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.dataexport.generic.output.csv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(
{ ElementType.METHOD })
public @interface DelimitedFileMethod
{
	/**
	 * The column position of the attribute
	 */
	int position();

	/**
	 * Specify a value if the getter returns null. default is an empty string.
	 */
	String nullValue() default "";

	/**
	 * name of the property if not the name itself
	 */
	String name() default "";
}
