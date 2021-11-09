/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.patchesdemo.release;

import de.hybris.platform.patches.Rerunnable;
import de.hybris.platform.patchesdemo.structure.Release;
import de.hybris.platform.patchesdemo.structure.StructureState;


/**
 * Example patch doing nothing at all.
 */
public class Patch2x1 extends AbstractDemoPatch implements SimpleDemoPatch, Rerunnable
{

	public Patch2x1()
	{
		super("2_1", "02_01", Release.R2, StructureState.V3);
	}
}
