/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.modepredicates;

import de.hybris.platform.cmsfacades.data.StructureTypeMode;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.function.BiPredicate;

import org.springframework.beans.factory.annotation.Required;

/**
 * BiPredicate that tests if the {@link StructureTypeMode} is the same as defined for this instance of predicate. 
 */
public class EqualsModeAttributeBiPredicate implements BiPredicate<AttributeDescriptorModel, StructureTypeMode>
{
	private StructureTypeMode mode;
	
	@Override
	public boolean test(final AttributeDescriptorModel attributeDescriptorModel, final StructureTypeMode structureTypeMode)
	{
		return structureTypeMode == mode;
	}

	protected StructureTypeMode getMode()
	{
		return mode;
	}

	@Required
	public void setMode(final StructureTypeMode mode)
	{
		this.mode = mode;
	}
}
