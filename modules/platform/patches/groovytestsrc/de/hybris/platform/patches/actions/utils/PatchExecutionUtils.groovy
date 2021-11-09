/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.patches.actions.utils

import de.hybris.platform.core.Registry
import de.hybris.platform.patches.model.PatchExecutionModel
import de.hybris.platform.patches.model.PatchExecutionUnitModel
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery
import de.hybris.platform.servicelayer.search.SearchResult

/**
 * Utils class with methods needed for proper patch execution testing.
 * Use it only for testing purposes.
 */
public final class PatchExecutionUtils {

    /**
     * Returns the list of {@link PatchExecutionUnitModel}
     * @param patchExecution {@link PatchExecutionModel}
     * @return List of related {@link PatchExecutionUnitModel}
     */
    public static List<PatchExecutionUnitModel> getPatchExecutionUnits(final PatchExecutionModel patchExecution) {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(
                "select {" + PatchExecutionUnitModel.PK + "} from {" + PatchExecutionUnitModel._TYPECODE
                        + "} where {" + PatchExecutionUnitModel.PATCH + "} = ?patch")
        query.addQueryParameter(PatchExecutionUnitModel.PATCH, patchExecution)
        final SearchResult<PatchExecutionUnitModel> result = Registry.getApplicationContext().getBean("flexibleSearchService").search(query)
        final List<PatchExecutionUnitModel> results = result.getResult()
        return results.isEmpty() ? Collections.emptyList() : results
    }
}
