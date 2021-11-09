/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';
import {
    stringUtils,
    GatewayProxied,
    GenericEditorStackService,
    IRenderService,
    Payload,
    SeInjectable
} from 'smarteditcommons';
import { IComponent, IEditorModalService } from 'cmscommons/services/IEditorModalService';
import { IGenericEditorModalServiceComponent } from 'cmscommons/services/IGenericEditorModalServiceComponent';
import { ContextAwareEditableItemService } from 'cmssmarteditcontainer/services/contextAwareEditableItem/ContextAwareEditableItemServiceOuter';
import { ComponentService } from 'cmscommons';

@GatewayProxied('open', 'openAndRerenderSlot')
@SeInjectable()
export class EditorModalService extends IEditorModalService {
    constructor(
        private isBlank: (value: any) => boolean,
        private genericEditorModalService: any,
        private componentService: ComponentService,
        private renderService: IRenderService,
        private contextAwareEditableItemService: ContextAwareEditableItemService,
        private cmsitemsRestService: any,
        private $q: angular.IQService,
        private $translate: angular.translate.ITranslateService,
        private lodash: lo.LoDashStatic,
        private genericEditorStackService: GenericEditorStackService
    ) {
        super();
    }

    openAndRerenderSlot(
        componentType: string,
        componentUuid: string,
        targetedQualifier: string,
        saveCallback?: (item: any) => void,
        editorStackId?: string
    ): angular.IPromise<void> {
        const componentAttributes: IComponent = {
            smarteditComponentType: componentType,
            smarteditComponentUuid: componentUuid
        };

        return this._preloadContent(componentUuid, componentAttributes).then((attributes) => {
            const componentData = this._createComponentData(
                attributes,
                {
                    targetedQualifier
                },
                editorStackId || stringUtils.generateIdentifier()
            );
            return this._openGenericEditor(componentData, saveCallback);
        });
    }

    open(
        componentAttributes: IComponent,
        targetSlotId?: string,
        position?: number,
        targetedQualifier?: string,
        saveCallback?: (item: any) => void,
        editorStackId?: string
    ): angular.IPromise<void> {
        return this._preloadContent(
            componentAttributes.smarteditComponentUuid,
            componentAttributes
        ).then((attributes) => {
            const componentData = this._createComponentData(
                attributes,
                {
                    slotId: targetSlotId,
                    position,
                    targetedQualifier
                },
                editorStackId || stringUtils.generateIdentifier()
            );

            return this._openGenericEditor(componentData, saveCallback);
        });
    }

    /**
     * Loads content of the item by uuid and populates the content attribute of componentAttributes object only if it's not already provided.
     */
    private _preloadContent(
        uuid: string,
        componentAttributes: IComponent
    ): angular.IPromise<IComponent> {
        return this._getContentByUuid(uuid).then((component) => {
            if (component !== null && this.lodash.isNil(componentAttributes.content)) {
                componentAttributes.content = component;
            }
            return this.$q.resolve(componentAttributes);
        });
    }

    /**
     * Opens generic editor.
     *
     * @param {IGenericEditorModalServiceComponent} componentData Object that contains all parameters for generic editor.
     * Note: if the componentUuid is not provided the generic editor will be opened for creation.
     *
     * @param {Function} saveCallback the save callback that is triggered after submit.
     */
    private _openGenericEditor(
        componentData: IGenericEditorModalServiceComponent,
        saveCallback?: (item: any) => void
    ): angular.IPromise<void> {
        return this._markComponentAsShared(componentData.content, componentData).then(() => {
            return this.genericEditorModalService.open(componentData, (item: any) => {
                if (saveCallback) {
                    saveCallback(item);
                }

                if (componentData.editorStackId) {
                    const [topEditor] = this.genericEditorStackService.getEditorsStack(
                        componentData.editorStackId
                    );
                    const {
                        component: { uuid }
                    } = topEditor;

                    this.componentService
                        .getSlotsForComponent(uuid as string)
                        .then((slotIds: string[]) => {
                            this.renderService.renderSlots(slotIds);
                        });
                }
            });
        });
    }

    /**
     * Retrieves a content object by its uuid. If the uuid is undefined, null is returned.
     */
    private _getContentByUuid(uuid: string): angular.IPromise<Payload> {
        if (!this.lodash.isNil(uuid)) {
            return this.cmsitemsRestService.getById(uuid);
        } else {
            return this.$q.resolve(null);
        }
    }

    /**
     * Verifies whether the component is shared in workflow context. If yes then makes the component read only and adds a message that the component
     * is used in different workflow. If no, checks whether the component is shared in page context and adds a message about it.
     */
    private _markComponentAsShared(
        componentContent: Payload,
        modelServiceParameters: IGenericEditorModalServiceComponent
    ): angular.IPromise<void> {
        if (componentContent && componentContent.uid) {
            return this.contextAwareEditableItemService
                .isItemEditable(componentContent.uid as string)
                .then((componentIsEditable: boolean) => {
                    modelServiceParameters.readOnlyMode = !componentIsEditable;
                    if (modelServiceParameters.readOnlyMode) {
                        this._addComponentInfoMessage(
                            'se.cms.component.workflow.shared.component',
                            modelServiceParameters
                        );
                    } else {
                        if (this._componentIsShared(componentContent)) {
                            this._addComponentInfoMessage(
                                'se.cms.component.shared.component',
                                modelServiceParameters
                            );
                        }
                    }
                    return this.$q.resolve();
                });
        }
        return this.$q.resolve();
    }

    /**
     * Verifies whether the component is shared or not by checking the slots attribute of the component payload.
     */
    private _componentIsShared(componentContent: Payload): boolean {
        return componentContent.slots && (componentContent.slots as Payload[]).length > 1;
    }

    /**
     * Adds a message to a messages attribute of IGenericEditorModalServiceComponent. If the attribute is undefined then the new one is created.
     */
    private _addComponentInfoMessage(
        message: string,
        modelServiceParameters: IGenericEditorModalServiceComponent
    ): void {
        modelServiceParameters.messages = modelServiceParameters.messages || [];
        modelServiceParameters.messages.push({
            type: 'info',
            message: this.$translate.instant(message)
        });
    }

    private _prepareContentForCreate(
        content: Payload,
        componentType: string,
        catalogVersionUuid: string,
        slotId: string,
        position: number
    ): Payload {
        const preparedContent = content ? Object.assign({}, content) : {};
        preparedContent.position = !this.isBlank(preparedContent.position)
            ? preparedContent.position
            : position;
        preparedContent.slotId = preparedContent.slotId || slotId;
        preparedContent.typeCode = preparedContent.typeCode || componentType;
        preparedContent.itemtype = preparedContent.itemtype || componentType;
        preparedContent.catalogVersion = preparedContent.catalogVersion || catalogVersionUuid;
        preparedContent.visible = !this.isBlank(preparedContent.visible)
            ? preparedContent.visible
            : true;
        return preparedContent;
    }

    private _createComponentData(
        componentAttributes: IComponent,
        params: IComponentAttributes,
        editorStackId?: string
    ): IGenericEditorModalServiceComponent {
        let type;
        try {
            type = componentAttributes.smarteditComponentType.toLowerCase();
        } catch (e) {
            throw new Error(
                `editorModalService._createComponentData - invalid component type in componentAttributes. ${e}`
            );
        }

        const isCreateOperation = this.lodash.isNil(componentAttributes.smarteditComponentUuid);
        let content: Payload;
        if (isCreateOperation) {
            content = this._prepareContentForCreate(
                componentAttributes.content,
                componentAttributes.smarteditComponentType,
                componentAttributes.catalogVersionUuid,
                params.slotId,
                params.position
            );
        } else {
            content = componentAttributes.content;
        }
        return {
            componentUuid: componentAttributes.smarteditComponentUuid,
            componentType: componentAttributes.smarteditComponentType,
            title: 'type.' + type + '.name',
            targetedQualifier: params.targetedQualifier,
            initialDirty: componentAttributes.initialDirty,
            content,
            editorStackId
        };
    }
}

interface IComponentAttributes {
    position?: number;
    slotId?: string;
    targetedQualifier?: string;
}
