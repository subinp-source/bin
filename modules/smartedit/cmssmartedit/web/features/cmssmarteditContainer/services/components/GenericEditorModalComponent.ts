/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { from as fromPromise } from 'rxjs';

import {
    stringUtils,
    CrossFrameEventService,
    CONTEXT_SITE_ID,
    FundamentalModalButtonAction,
    FundamentalModalButtonStyle,
    FundamentalModalManagerService,
    GenericEditorAPI,
    GenericEditorOnSubmitResponse,
    GenericEditorStructure,
    GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT,
    IConfirmationModalService,
    Payload,
    SeDowngradeComponent,
    SystemEventService
} from 'smarteditcommons';
import { Component, HostListener } from '@angular/core';
import { IGenericEditorModalServiceComponent, TYPES_RESOURCE_URI } from 'cmscommons';
import { IGenericEditorModalComponentData } from '../GenericEditorModalService';

export const CMS_LINK_TO_RELOAD_STRUCTURE_EVENT_ID = 'cms-link-to-reload-structure';

export interface GenericEditorModalComponentControls {
    isDirty: () => boolean;
    reset: () => Promise<void>;
    submit: () => Promise<GenericEditorOnSubmitResponse>;
    genericEditorAPI: GenericEditorAPI;
}

export interface GenericEditorUnrelatedErrorEvent {
    sourceGenericEditorId: string;
    messages: string[];
}
export interface StructureChangeEvent {
    content: Payload;
    structureApiMode: string;
    structure: GenericEditorStructure;
    editorId: string;
}
@SeDowngradeComponent()
@Component({
    selector: 'se-generic-editor-modal',
    template: `
        <se-message
            class="se-ge-modal--messages"
            *ngIf="showDisclaimer"
            type="info"
            messageId="VisibilityTab.DisplayComponentOffDisclaimer"
        >
            <ng-container se-message-description>
                {{ 'se.cms.editortabset.visibilitytab.disclaimer' | translate }}
            </ng-container>
        </se-message>

        <ng-container *ngIf="data && data.messages">
            <se-message
                class="se-ge-modal--messages"
                *ngFor="let msg of data.messages; let i = index"
                [type]="msg.type"
                messageId="generic-editor-model-message-{{ i }}"
            >
                <ng-container se-message-description> {{ msg.message }} </ng-container>
            </se-message>
        </ng-container>

        <se-generic-editor
            [id]="genericEditorId"
            [smarteditComponentId]="data.componentUuid"
            [smarteditComponentType]="data.componentType"
            [structure]="structure"
            [structureApi]="structureApi"
            [content]="data.content"
            [contentApi]="contentApi"
            [(submit)]="controls.submit"
            [(reset)]="controls.reset"
            [(isDirty)]="controls.isDirty"
            [editorStackId]="editorStackId"
            (getApi)="getApi($event)"
        >
        </se-generic-editor>
    `
})
export class GenericEditorModalComponent {
    public data: IGenericEditorModalServiceComponent = {} as IGenericEditorModalServiceComponent;
    public controls: GenericEditorModalComponentControls = {} as GenericEditorModalComponentControls;
    public editorStackId: string;
    public genericEditorId = stringUtils.generateIdentifier();
    public structure: GenericEditorStructure;
    public structureApi: string;
    public contentApi: string;
    public showDisclaimer: boolean = false;

    private readonly STRUCTURE_API_BASE_URL: string = `${TYPES_RESOURCE_URI}?code=:smarteditComponentType&mode=:structureApiMode`;

    private saveCallback: (item: GenericEditorOnSubmitResponse) => void;
    private errorCallback: (messages: string[], instance: GenericEditorModalComponent) => void;
    private changeStructureEventListener: () => void;
    private unrelatedValidationErrorsEvent: () => void;
    private isSetupCompleted: boolean = false;
    private isReset: boolean = false;
    private isEscapeInProgress: boolean = false;

    constructor(
        private modalManager: FundamentalModalManagerService,
        private confirmationModalService: IConfirmationModalService,
        private crossFrameEventService: CrossFrameEventService,
        private systemEventService: SystemEventService
    ) {}

    @HostListener('document:keyup.escape') onEscapeDown(): void {
        if (this.isEscapeInProgress) {
            return;
        }

        this.onEscapeClicked();
    }
    public getApi(genericEditorAPI: GenericEditorAPI): void {
        this.controls.genericEditorAPI = genericEditorAPI;

        if (this.data.targetedQualifier) {
            genericEditorAPI.switchToTabContainingQualifier(this.data.targetedQualifier);
        }
        if (this.data.initialDirty) {
            genericEditorAPI.considerFormDirty();
        }
    }

    public onSave(): Promise<GenericEditorOnSubmitResponse> {
        return this.controls.submit().then((item) => {
            this.crossFrameEventService.publish('CMSITEMS_UPDATE');

            if (this.saveCallback) {
                this.saveCallback(item);
            }

            this.removeEventListeners();
            return item;
        });
    }

    public onCancel(): Promise<void> {
        return new Promise((resolve, reject) => {
            if (this.genericEditorIsDirty() && !this.isReset) {
                (this.confirmationModalService.confirm({
                    description: 'se.editor.cancel.confirm'
                }) as Promise<any>).then(
                    () => {
                        this.controls.reset().then(
                            () => {
                                this.markAsReset();
                                this.removeEventListeners();
                                resolve();
                            },
                            () => {
                                reject();
                            }
                        );
                        this.crossFrameEventService.publish('GENERIC_EDITOR_CANCEL', {
                            confirmed: true
                        });
                    },
                    () => {
                        reject();
                        this.crossFrameEventService.publish('GENERIC_EDITOR_CANCEL', {
                            confirmed: false
                        });
                    }
                );
            } else {
                this.removeEventListeners();
                resolve();
            }
        });
    }

    public setup() {
        this.modalManager.setDismissCallback(() => this.onCancel());

        this.modalManager.addButton({
            id: 'cancel',
            label: this.data.cancelLabel || 'se.cms.component.confirmation.modal.cancel',
            style: FundamentalModalButtonStyle.Default,
            action: FundamentalModalButtonAction.Dismiss,
            callback: () => fromPromise(this.onCancel())
        });

        this.modalManager.addButton({
            id: 'save',
            label: this.data.saveLabel || 'se.cms.component.confirmation.modal.save',
            style: FundamentalModalButtonStyle.Primary,
            action: FundamentalModalButtonAction.Close,
            callback: () => fromPromise(this.onSave()),
            disabledFn: () =>
                this.controls.genericEditorAPI.isSubmitDisabled() ||
                this.isGenericEditorInReadOnlyMode()
        });

        this.structure = this.data.structure;

        if (!this.structure) {
            this.structureApi = this.getStructureApiByMode('DEFAULT');

            if (this.isGenericEditorInReadOnlyMode()) {
                this.structureApi = this.applyReadOnlyModeToStructureApiUrl(this.structureApi);
            }
        } else if (this.isGenericEditorInReadOnlyMode()) {
            this.structure = this.makeStructureReadOnly(this.structure);
        }

        this.changeStructureEventListener = this.systemEventService.subscribe(
            CMS_LINK_TO_RELOAD_STRUCTURE_EVENT_ID,
            (event: string, data: StructureChangeEvent) => this.onChangeStructureEvent(event, data)
        );

        this.unrelatedValidationErrorsEvent = this.systemEventService.subscribe(
            GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT,
            (event: string, data: GenericEditorUnrelatedErrorEvent) =>
                this.onUnrelatedErrors(event, data)
        );

        this.contentApi =
            this.data.contentApi || `/cmswebservices/v1/sites/${CONTEXT_SITE_ID}/cmsitems`;

        this.isSetupCompleted = true;
    }

    /**
     * Method makes each attribute of the structure non editable.
     */
    public makeStructureReadOnly(structure: GenericEditorStructure): GenericEditorStructure {
        structure.attributes = structure.attributes || [];
        structure.attributes.forEach(function(element) {
            element.editable = false;
        });
        return structure;
    }

    /**
     * Method verifies whether the generic editor is in read only mode or not.
     * Returns TRUE if the generic editor is in read only mode, FALSE otherwise.
     */
    public isGenericEditorInReadOnlyMode(): boolean {
        return !!this.data.readOnlyMode;
    }

    /**
     * Method returns generic editor content object.
     */
    public getGenericEditorContent(): Payload {
        return this.controls.genericEditorAPI && this.controls.genericEditorAPI.getContent();
    }

    /**
     * Method verifies whether the generic editor is in dirty state or not.
     * Returns TRUE if the generic editor is in dirty state, FALSE otherwise.
     */
    public genericEditorIsDirty(): boolean {
        return this.controls.isDirty && this.controls.isDirty();
    }

    public onChangeStructureEvent(eventId: string, payload: StructureChangeEvent) {
        // Structure is reloaded only for the Generic Editor that owns the component where this event came from.
        // This is done to avoid contaminating editors in a nested set-up.
        if (this.genericEditorId === payload.editorId) {
            if (payload.structureApiMode) {
                this.structure = null;
                this.structureApi = this.getStructureApiByMode(payload.structureApiMode);
            } else if (payload.structure) {
                this.structureApi = null;
                this.structure = payload.structure;
            }
            this.data.content = payload.content;
        }
    }

    public onUnrelatedErrors(eventId: string, eventData: GenericEditorUnrelatedErrorEvent) {
        if (eventData.sourceGenericEditorId === this.data.componentUuid && this.errorCallback) {
            this.errorCallback(eventData.messages, this);
        }
    }

    /**
     * Converts the structure api url to read only mode. All fields return in read only mode will not be editable.
     */
    public applyReadOnlyModeToStructureApiUrl(structureApiUrl: string): string {
        return `${structureApiUrl}&readOnly=true`;
    }

    public getStructureApiByMode(structureApiMode: string): string {
        return this.STRUCTURE_API_BASE_URL.replace(/:structureApiMode/gi, structureApiMode);
    }

    public removeEventListeners(): void {
        this.unrelatedValidationErrorsEvent();
        this.changeStructureEventListener();
    }

    ngOnInit() {
        this.modalManager.getModalData().subscribe((config: IGenericEditorModalComponentData) => {
            this.data = config.data;
            this.saveCallback = config.saveCallback;
            this.errorCallback = config.errorCallback;
            this.editorStackId = config.data.editorStackId;

            if (!this.isSetupCompleted) {
                this.setup();
            }
        });
    }

    private markAsReset(): void {
        this.isReset = true;
    }

    private onEscapeClicked(): void {
        this.isEscapeInProgress = true;

        this.onCancel()
            .then(() => this.modalManager.dismiss())
            .finally(() => {
                this.isEscapeInProgress = false;
            });
    }
}
