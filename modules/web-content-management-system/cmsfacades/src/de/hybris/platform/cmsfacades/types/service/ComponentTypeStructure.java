/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.service;

import de.hybris.platform.cmsfacades.data.ComponentTypeData;
import de.hybris.platform.cmsfacades.data.StructureTypeCategory;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.ComposedTypeModel;

import java.util.List;
import java.util.Set;


/**
 * Represents meta-information about a <code>ComposedTypeModel</code> and the populators required to convert this
 * information to a <code>ComponentTypeData</code>.
 */
public interface ComponentTypeStructure
{
	/**
	 * Get the typecode identifying the <code>ComposedTypeModel</code>.
	 *
	 * @return the typecode
	 */
	String getTypecode();

	/**
	 * Sets the typecode identifying the <code>ComposedTypeModel</code>.
	 *
	 * @param typecode
	 *           - the typecode
	 */
	void setTypecode(String typecode);

	/**
	 * Get the category in which this type belongs to. By default and for backwards compatibility, all categories are set
	 * to COMPONENT enum type.
	 *
	 * @return the category
	 */
	StructureTypeCategory getCategory();

	/**
	 * Sets the category for this component type. By default, if the category is not set, the category is of the
	 * COMPONENT enum type.
	 *
	 * @param category
	 */
	void setCategory(StructureTypeCategory category);

	/**
	 * Get the concrete Type Data class representing this type
	 *
	 * @return the concrete class that represents the Type Structure
	 * @deprecated since 1811, no longer needed
	 */
	@Deprecated(since = "1811", forRemoval = true)
	Class getTypeDataClass();

	/**
	 * Optional. Sets the concrete Type Data class representing this type.
	 *
	 * @param typeDataClass
	 *           The concrete class that represents the Type Structure
	 * @deprecated since 1811, no longer needed
	 */
	@Deprecated(since = "1811", forRemoval = true)
	void setTypeDataClass(Class typeDataClass);

	/**
	 * Get the attributes that should be considered by the ComponentTypeStructure.
	 *
	 * @return the attributes or an empty set
	 */
	Set<ComponentTypeAttributeStructure> getAttributes();

	/**
	 * Get the populators to be used when converting the representing <code>ComposedTypeModel</code> to a
	 * <code>ComponentTypeData</code>.
	 *
	 * @return the populators
	 */
	List<Populator<ComposedTypeModel, ComponentTypeData>> getPopulators();

	/**
	 * Sets the populators to be used when converting the representing <code>ComposedTypeModel</code> to a
	 * <code>ComponentTypeData</code>.
	 *
	 * @param populators
	 *           - the populators
	 */
	void setPopulators(List<Populator<ComposedTypeModel, ComponentTypeData>> populators);

}
