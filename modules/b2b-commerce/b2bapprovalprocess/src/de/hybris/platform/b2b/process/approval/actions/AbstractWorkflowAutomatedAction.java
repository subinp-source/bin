/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.process.approval.actions;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.jalo.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.jalo.WorkflowAction;
import de.hybris.platform.workflow.jalo.WorkflowDecision;

import java.util.List;


/**
 * This automated action depends on Jalo functionlity and is currently required for workflows managed via HMC.
 *
 * @deprecated Since 4.4. Will be migrated to service layer in version 4.5.
 */
@Deprecated(since = "4.4", forRemoval = true)
public class AbstractWorkflowAutomatedAction implements AutomatedWorkflowTemplateJob {

    @Override
    public final WorkflowDecision perform(final WorkflowAction action) {

        performAction(action);

        final List<WorkflowDecision> workflowDecisionList = (List<WorkflowDecision>) action.getDecisions();
        if (workflowDecisionList != null && !workflowDecisionList.isEmpty()) {
            return workflowDecisionList.get(0);
        }

        return null;
    }

    public void performAction(final WorkflowAction action) {
        //default implementation does nothing
    }

    /**
     * @deprecated Since 5.0.
     */
    @Deprecated(since = "5.0", forRemoval = true)
    public ModelService getModelService() {
        return Registry.getApplicationContext().getBean("modelService", ModelService.class);
    }

    /**
     * @deprecated Since 5.0.
     */
    @Deprecated(since = "5.0", forRemoval = true)
    public EventService getEventService() {
        return Registry.getApplicationContext().getBean("eventService", EventService.class);
    }
}
