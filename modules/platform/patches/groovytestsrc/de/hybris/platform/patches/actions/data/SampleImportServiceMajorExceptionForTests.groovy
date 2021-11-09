/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.patches.actions.data

import de.hybris.platform.servicelayer.impex.ImportConfig
import de.hybris.platform.servicelayer.impex.ImportResult
import de.hybris.platform.servicelayer.impex.impl.DefaultImportService

/**
 * Sample impex import service which throws major exception during importing impex data, created for {@link ImportPatchActionErrorHandlingTest} purposes.
 */
class SampleImportServiceMajorExceptionForTests extends DefaultImportService {

    ImportResult importData(final ImportConfig config) {
        throw new UnsupportedOperationException("major exception")
    }
}
