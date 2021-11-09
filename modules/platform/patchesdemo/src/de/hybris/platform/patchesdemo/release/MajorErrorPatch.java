/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.patchesdemo.release;

import de.hybris.platform.patches.Patch;
import de.hybris.platform.patches.Rerunnable;
import de.hybris.platform.patchesdemo.structure.Release;
import de.hybris.platform.patchesdemo.structure.StructureState;

import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;


/**
 * Example patch which is switched off by default as it throws major exception, which stops whole init/update process.
 * Should be switched on to demonstrate error handling or tracking of problems.
 */
public class MajorErrorPatch extends AbstractDemoPatch implements SimpleDemoPatch, Rerunnable
{

	public MajorErrorPatch()
	{
		super("major_error_patch", "patch_with_major_error", Release.E1, StructureState.LAST);
	}

	/**
	 * Creates project data. See {@link Patch#createProjectData(de.hybris.platform.patches.organisation.StructureState)}.
	 */
	@Override
	public void createProjectData(final de.hybris.platform.patches.organisation.StructureState structureState)
	{
		throw new DataSourceLookupFailureException("Data source is not accessible - major error");
	}
}
