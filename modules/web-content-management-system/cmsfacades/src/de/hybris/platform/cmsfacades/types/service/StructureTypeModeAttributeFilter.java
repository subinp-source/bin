/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.service;

import de.hybris.platform.cmsfacades.data.StructureTypeMode;

import java.util.List;
import java.util.function.BiPredicate;

/**
 * This interface defines the inclusions, exclusions and order for a given Structure Type and Structure Mode.  
 */
public interface StructureTypeModeAttributeFilter
{

	/**
	 * The BiPredicate that constrains the usage of this Mode.
	 * First argument of the BiPredicate is the TypeCode and the second is the structure mode. 
	 * @return a bi-predicate 
	 */
	BiPredicate<String, StructureTypeMode> getConstrainedBy();

	/**
	 * Returns the list of attributes that must be included as part of this mode and typeCode.   
	 * @return list of included attributes
	 */
	List<String> getIncludes();

	/**
	 * Returns the list of attributes that must be excluded as part of this mode and typeCode.   
	 * @return list of excluded attributes
	 */
	List<String> getExcludes();

	/**
	 * Returns the list of attributes that must be ordered.   
	 * @return list of attributes in order. 
	 */
	List<String> getOrder();
}
