/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.patches.actions.data

import de.hybris.platform.patches.actions.PatchAction

/**
 * Sample patch action for {@link PatchActionPerformErrorHandlingTest} purposes
 */
class SamplePatchActionForTests implements PatchAction {
    @Override
    void perform(final PatchActionData data) {
        data.getOption(PatchActionDataOption.Impex.RUN_AGAIN) // do nothing or throw exception
    }
}
