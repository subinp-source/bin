/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';
import { GenericEditorField, IGenericEditor, ISeComponent, SeComponent } from 'smarteditcommons';
import { WorkflowService } from 'cmssmarteditcontainer/components/workflow/services/WorkflowService';
import {
    WorkflowAction,
    WorkflowDecision,
    WorkflowTemplate
} from 'cmssmarteditcontainer/components/workflow/dtos';

@SeComponent({
    templateUrl: 'workflowCreateVersionFieldTemplate.html',
    inputs: ['model', 'editor', 'field']
})
export class WorkflowCreateVersionFieldComponent implements ISeComponent {
    public field: GenericEditorField;
    private model: any;
    private editor: IGenericEditor;
    private unregisterOnChangeEvent: any;

    constructor(private workflowService: WorkflowService, private lodash: lo.LoDashStatic) {}

    public showVersionLabel(): boolean {
        return this.model.createVersion;
    }

    public onCreateVersionChange(): void {
        if (this.model.createVersion === false) {
            this.model.versionLabel = null;
        } else {
            // Create version label generation while making decisions
            if (this.model.decisionCode && this.lodash.isEmpty(this.model.versionLabel)) {
                this.workflowService
                    .getAllActionsForWorkflowCode(this.model.workflowCode)
                    .then((workflowActions: WorkflowAction[]) => {
                        const workflowAction = workflowActions.find((action: WorkflowAction) => {
                            return action.code === this.model.actionCode;
                        });
                        const workflowDecision = workflowAction.decisions.find(
                            (decision: WorkflowDecision) => {
                                return decision.code === this.model.decisionCode;
                            }
                        );
                        this.model.versionLabel =
                            workflowDecision.name + ' for ' + workflowAction.name;
                    });
                // Create version label generation while creating workflows
            } else {
                this.workflowService
                    .getWorkflowTemplateByCode(this.model.templateCode)
                    .then((workflow: WorkflowTemplate) => {
                        if (workflow && this.lodash.isEmpty(this.model.versionLabel)) {
                            this.model.versionLabel = workflow.name + ' workflow started';
                        }
                    });
            }
        }
    }

    $onInit(): void {
        this.unregisterOnChangeEvent = this.editor.api.addContentChangeEvent(
            this.onCreateVersionChange.bind(this)
        );
    }

    $onDestroy(): void {
        this.unregisterOnChangeEvent();
    }
}
