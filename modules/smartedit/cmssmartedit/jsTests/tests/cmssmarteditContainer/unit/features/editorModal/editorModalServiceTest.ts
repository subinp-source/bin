/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';
import { EditorModalService } from 'cmssmarteditcontainer/services';
import { promiseHelper, PromiseType } from 'testhelpers';
import { IGenericEditorModalServiceComponent } from 'cmscommons/services/IGenericEditorModalServiceComponent';
import { IComponent } from 'cmscommons/services/IEditorModalService';
import { ContextAwareEditableItemService } from 'cmssmarteditcontainer/services/contextAwareEditableItem/ContextAwareEditableItemServiceOuter';
import { GenericEditorStackService } from 'smarteditcommons';

describe('editorModalService', () => {
    let editorModalService: EditorModalService;
    let genericEditorModalServiceMock: jasmine.SpyObj<any>;
    let renderServiceMock: jasmine.SpyObj<any>;
    let contextAwareEditableItemService: jasmine.SpyObj<ContextAwareEditableItemService>;
    let cmsitemsRestService: jasmine.SpyObj<any>;
    let $translate: jasmine.SpyObj<any>;
    let genericEditorStackService: jasmine.SpyObj<GenericEditorStackService>;
    const componentService = jasmine.createSpyObj('ComponentService', ['getSlotsForComponent']);

    const $q = promiseHelper.$q();

    let COMPONENT_SERVICE_SLOTS_FOR_COMPONENT: string[];
    const COMPONENT_UID = 'componentUid';
    const COMPONENT_SHARED_IN_PAGE_CONTEXT_MESSAGE = 'se.cms.component.shared.component';
    const COMPONENT_SHARED_IN_WORKFLOW_CONTEXT_MESSAGE =
        'se.cms.component.workflow.shared.component';

    beforeEach(() => {
        COMPONENT_SERVICE_SLOTS_FOR_COMPONENT = ['some-slot-id'];
        renderServiceMock = jasmine.createSpyObj('renderService', ['renderSlots']);
        genericEditorModalServiceMock = jasmine.createSpyObj('genericEditorModalServiceMock', [
            'open'
        ]);
        contextAwareEditableItemService = jasmine.createSpyObj('contextAwareEditableItemService', [
            'isItemEditable'
        ]);
        cmsitemsRestService = jasmine.createSpyObj('cmsitemsRestService', ['getById']);
        $translate = jasmine.createSpyObj<angular.translate.ITranslateService>('$translate', [
            'instant'
        ]);
        const isBlankStub = () => true;
        componentService.getSlotsForComponent.and.returnValue(
            promiseHelper.buildPromise<string[]>(
                'openGenericModalPromise',
                PromiseType.RESOLVES,
                COMPONENT_SERVICE_SLOTS_FOR_COMPONENT
            )
        );

        cmsitemsRestService.getById.and.returnValue(
            $q.resolve({
                uid: COMPONENT_UID
            })
        );

        genericEditorStackService = jasmine.createSpyObj('genericEditorStackService', [
            'getEditorsStack'
        ]);

        genericEditorStackService.getEditorsStack.and.returnValue([
            { component: { uuid: 'uuid ' } }
        ]);
        contextAwareEditableItemService.isItemEditable.and.returnValue($q.resolve(true));

        genericEditorModalServiceMock.open.and.callFake(
            (componentData: IGenericEditorModalServiceComponent) => {
                const resolvedValue = (componentData && componentData.content
                    ? componentData.content.slotId
                    : undefined) as string;
                return promiseHelper.buildPromise<string>(
                    'genericEditorModalServiceMock',
                    PromiseType.RESOLVES,
                    resolvedValue
                );
            }
        );
        editorModalService = new EditorModalService(
            isBlankStub,
            genericEditorModalServiceMock,
            componentService,
            renderServiceMock,
            contextAwareEditableItemService,
            cmsitemsRestService,
            $q,
            $translate,
            lo,
            genericEditorStackService
        );
    });

    it('open will delegate to genericEditorModalService.open and invoke a rerendering upon closing', () => {
        // GIVEN
        const editorStackId = 'editorStackId';
        const editorModalServiceEditComponentMock: IComponent = {
            smarteditComponentUuid: 'smarteditComponentUuid',
            smarteditComponentType: 'smarteditComponentType',
            catalogVersionUuid: 'catalogVersionUuid',
            initialDirty: false
        };

        const genericEditorModalServiceEditComponentMock: IGenericEditorModalServiceComponent = {
            componentUuid: editorModalServiceEditComponentMock.smarteditComponentUuid,
            componentType: editorModalServiceEditComponentMock.smarteditComponentType,
            title:
                'type.' +
                editorModalServiceEditComponentMock.smarteditComponentType.toLowerCase() +
                '.name',
            targetedQualifier: undefined,
            content: {
                uid: COMPONENT_UID
            },
            initialDirty: false,
            readOnlyMode: false,
            editorStackId
        };

        // WHEN
        editorModalService.open(
            editorModalServiceEditComponentMock,
            undefined,
            undefined,
            undefined,
            undefined,
            editorStackId
        );

        // THEN
        expect(genericEditorModalServiceMock.open.calls.count()).toBe(1);
        expect(genericEditorModalServiceMock.open.calls.argsFor(0).length).toBe(2);

        expect(genericEditorModalServiceMock.open.calls.argsFor(0)[0]).toEqual(
            genericEditorModalServiceEditComponentMock
        );

        expect(renderServiceMock.renderSlots).not.toHaveBeenCalled();
        const callback = genericEditorModalServiceMock.open.calls.argsFor(0)[1];
        callback();
        expect(genericEditorStackService.getEditorsStack).toHaveBeenCalledWith(editorStackId);
        expect(renderServiceMock.renderSlots).toHaveBeenCalledWith(
            COMPONENT_SERVICE_SLOTS_FOR_COMPONENT
        );
    });

    it('GIVEN creating a new component THEN renderService.renderComponent is not called', () => {
        // GIVEN
        const editorStackId = 'editorStackId';
        const editorModalServiceCreateComponentMock: IComponent = {
            smarteditComponentType: 'smarteditComponentType',
            catalogVersionUuid: 'catalogVersionUuid',
            initialDirty: false
        };

        const genericEditorModalServiceCreateComponentMock: IGenericEditorModalServiceComponent = {
            componentUuid: undefined,
            componentType: editorModalServiceCreateComponentMock.smarteditComponentType,
            title:
                'type.' +
                editorModalServiceCreateComponentMock.smarteditComponentType.toLowerCase() +
                '.name',
            targetedQualifier: undefined,
            content: {
                slotId: undefined,
                typeCode: editorModalServiceCreateComponentMock.smarteditComponentType,
                itemtype: editorModalServiceCreateComponentMock.smarteditComponentType,
                catalogVersion: editorModalServiceCreateComponentMock.catalogVersionUuid,
                visible: true,
                position: undefined
            },
            initialDirty: false,
            editorStackId
        };

        componentService.getSlotsForComponent.and.returnValue(Promise.resolve([]));

        // WHEN
        editorModalService.open(
            editorModalServiceCreateComponentMock,
            undefined,
            undefined,
            undefined,
            undefined,
            editorStackId
        );

        // THEN
        expect(renderServiceMock.renderSlots).not.toHaveBeenCalled();
        expect(genericEditorModalServiceMock.open.calls.argsFor(0)[0]).toEqual(
            genericEditorModalServiceCreateComponentMock
        );
        const callback = genericEditorModalServiceMock.open.calls.argsFor(0)[1];
        callback();
        expect(renderServiceMock.renderSlots).not.toHaveBeenCalled();
    });

    it('should mark component read only if it is not editable in current context', () => {
        // GIVEM
        const editorModalServiceEditComponentMock: IComponent = {
            smarteditComponentUuid: 'smarteditComponentUuid',
            smarteditComponentType: 'smarteditComponentType',
            catalogVersionUuid: 'catalogVersionUuid',
            initialDirty: false
        };
        contextAwareEditableItemService.isItemEditable.and.returnValue($q.resolve(false));

        // WHEN
        editorModalService.open(editorModalServiceEditComponentMock);

        // THEN
        const genericEditorParams = genericEditorModalServiceMock.open.calls.argsFor(0)[0];
        expect(genericEditorParams.readOnlyMode).toEqual(true);
    });

    it('should display message if component is shared and is editable in current context', () => {
        // GIVEM
        const editorModalServiceEditComponentMock: IComponent = {
            smarteditComponentUuid: 'smarteditComponentUuid',
            smarteditComponentType: 'smarteditComponentType',
            catalogVersionUuid: 'catalogVersionUuid',
            initialDirty: false
        };
        cmsitemsRestService.getById.and.returnValue(
            $q.resolve({
                uid: COMPONENT_UID,
                slots: ['a', 'b']
            })
        );
        $translate.instant.and.returnValue(COMPONENT_SHARED_IN_PAGE_CONTEXT_MESSAGE);

        // WHEN
        editorModalService.open(editorModalServiceEditComponentMock);

        // THEN
        expect($translate.instant).toHaveBeenCalledWith(COMPONENT_SHARED_IN_PAGE_CONTEXT_MESSAGE);
        const genericEditorParams = genericEditorModalServiceMock.open.calls.argsFor(0)[0];
        expect(genericEditorParams.messages).toContain({
            type: 'info',
            message: COMPONENT_SHARED_IN_PAGE_CONTEXT_MESSAGE
        });
    });

    it('should display message if component is editable in current context', () => {
        // GIVEM
        const editorModalServiceEditComponentMock: IComponent = {
            smarteditComponentUuid: 'smarteditComponentUuid',
            smarteditComponentType: 'smarteditComponentType',
            catalogVersionUuid: 'catalogVersionUuid',
            initialDirty: false
        };
        contextAwareEditableItemService.isItemEditable.and.returnValue($q.resolve(false));
        $translate.instant.and.returnValue(COMPONENT_SHARED_IN_WORKFLOW_CONTEXT_MESSAGE);

        // WHEN
        editorModalService.open(editorModalServiceEditComponentMock);

        // THEN
        expect($translate.instant).toHaveBeenCalledWith(
            COMPONENT_SHARED_IN_WORKFLOW_CONTEXT_MESSAGE
        );
        const genericEditorParams = genericEditorModalServiceMock.open.calls.argsFor(0)[0];
        expect(genericEditorParams.messages).toContain({
            type: 'info',
            message: COMPONENT_SHARED_IN_WORKFLOW_CONTEXT_MESSAGE
        });
    });

    it('should display only one message if component is editable and is shared in current context at the same time', () => {
        // GIVEN
        const editorModalServiceEditComponentMock: IComponent = {
            smarteditComponentUuid: 'smarteditComponentUuid',
            smarteditComponentType: 'smarteditComponentType',
            catalogVersionUuid: 'catalogVersionUuid',
            initialDirty: false
        };
        contextAwareEditableItemService.isItemEditable.and.returnValue($q.resolve(false));
        cmsitemsRestService.getById.and.returnValue(
            $q.resolve({
                uid: COMPONENT_UID,
                slots: ['a', 'b']
            })
        );
        $translate.instant
            .withArgs(COMPONENT_SHARED_IN_WORKFLOW_CONTEXT_MESSAGE)
            .and.returnValue(COMPONENT_SHARED_IN_WORKFLOW_CONTEXT_MESSAGE);
        $translate.instant
            .withArgs(COMPONENT_SHARED_IN_PAGE_CONTEXT_MESSAGE)
            .and.returnValue(COMPONENT_SHARED_IN_PAGE_CONTEXT_MESSAGE);

        // WHEN
        editorModalService.open(editorModalServiceEditComponentMock);

        // THEN
        const genericEditorParams = genericEditorModalServiceMock.open.calls.argsFor(0)[0];
        expect(genericEditorParams.messages).toContain({
            type: 'info',
            message: COMPONENT_SHARED_IN_WORKFLOW_CONTEXT_MESSAGE
        });
        expect(genericEditorParams.messages).not.toContain({
            type: 'info',
            message: COMPONENT_SHARED_IN_PAGE_CONTEXT_MESSAGE
        });
    });
});
