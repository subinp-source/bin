/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { HttpClient } from '@angular/common/http';
import { of, throwError } from 'rxjs';
import { ComponentHandlerService, RenderService, SeNamespaceService } from 'smartedit/services';
import {
    annotationService,
    CrossFrameEventService,
    GatewayProxied,
    ID_ATTRIBUTE,
    IExperienceService,
    INotificationService,
    IPageInfoService,
    IPerspectiveService,
    JQueryUtilsService,
    MessageGateway,
    ModalService,
    SmarteditBootstrapGateway,
    SystemEventService,
    TYPE_ATTRIBUTE,
    WindowUtils
} from 'smarteditcommons';
import { jQueryHelper, promiseHelper, ElementForJQuery } from 'testhelpers';

describe('renderServiceInner', () => {
    let renderService: RenderService;
    let jQueryUtilsService: jasmine.SpyObj<JQueryUtilsService>;
    let smarteditBootstrapGateway: jasmine.SpyObj<SmarteditBootstrapGateway>;
    let messageGateway: jasmine.SpyObj<MessageGateway>;
    let componentHandlerService: jasmine.SpyObj<ComponentHandlerService>;
    let httpClient: jasmine.SpyObj<HttpClient>;
    const $log = jasmine.createSpyObj('$log', ['error']);
    const $q = promiseHelper.$q();
    let systemEventService: jasmine.SpyObj<SystemEventService>;
    let notificationService: jasmine.SpyObj<INotificationService>;
    let pageInfoService: jasmine.SpyObj<IPageInfoService>;
    let perspectiveService: jasmine.SpyObj<IPerspectiveService>;
    let crossFrameEventService: jasmine.SpyObj<CrossFrameEventService>;
    let experienceService: jasmine.SpyObj<IExperienceService>;
    let seNamespaceService: jasmine.SpyObj<SeNamespaceService>;
    let modalService: jasmine.SpyObj<ModalService>;

    const windowUtils = new WindowUtils();
    const alertService = jasmine.createSpyObj('alertService', ['showDanger']);

    const CATALOG_VERSION_UUID_ATTRIBUTE = 'data-smartedit-catalog-version-uuid';
    const CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS = {
        PROCESS_COMPONENTS: 'contractChangeListenerProcessComponents',
        RESTART_PROCESS: 'contractChangeListenerRestartProcess'
    };

    const UUID_ATTRIBUTE = 'data-smartedit-component-uuid';

    const MOCK_COMPONENT_ID = 'someComponentId';
    const MOCK_COMPONENT_TYPE = 'someComponentType';
    const SLOT_ID_1 = 'slot1';
    const SLOT_ID_2 = 'slot2';
    const MOCK_STOREFRONT_PREVIEW_URL = 'someMockPreviewStorefronUrl';

    let reprocessPageSpy: jasmine.Spy;

    beforeEach(() => {
        renderService = null;

        httpClient = jasmine.createSpyObj<HttpClient>('httpClient', ['get']);

        messageGateway = jasmine.createSpyObj('messageGateway', ['publish']);
        smarteditBootstrapGateway = jasmine.createSpyObj('smarteditBootstrapGateway', [
            'getInstance'
        ]);
        smarteditBootstrapGateway.getInstance.and.returnValue(messageGateway);

        componentHandlerService = jasmine.createSpyObj('componentHandlerService', [
            'getOverlay',
            'isOverlayOn',
            'getParent',
            'getComponentUnderSlot',
            'getComponent',
            'getOriginalComponent',
            'getComponentInOverlay',
            'getParentSlotForComponent',
            'getOverlayComponentWithinSlot',
            'getOverlayComponent',
            'isSmartEditComponent',
            'getFirstSmartEditComponentChildren',
            'getComponentCloneInOverlay'
        ]);
        systemEventService = jasmine.createSpyObj('systemEventService', ['publish', 'subscribe']);
        notificationService = jasmine.createSpyObj('notificationService', [
            'pushNotification',
            'removeNotification'
        ]);
        pageInfoService = jasmine.createSpyObj('pageInfoService', ['getPageUUID']);
        perspectiveService = jasmine.createSpyObj('perspectiveService', [
            'isHotkeyEnabledForActivePerspective'
        ]);
        crossFrameEventService = jasmine.createSpyObj('crossFrameEventService', [
            'publish',
            'subscribe'
        ]);
        experienceService = jasmine.createSpyObj<IExperienceService>('experienceService', [
            'buildRefreshedPreviewUrl'
        ]);
        experienceService.buildRefreshedPreviewUrl.and.callFake(function() {
            return $q.when(MOCK_STOREFRONT_PREVIEW_URL);
        });
        seNamespaceService = jasmine.createSpyObj<SeNamespaceService>('seNamespaceService', [
            'renderComponent',
            'reprocessPage'
        ]);

        modalService = jasmine.createSpyObj('modalService', ['hasOpenModals']);
        modalService.hasOpenModals.and.returnValue(false);
    });

    function initService(jq: JQueryStatic = null) {
        jq = jq || jQueryHelper.jQuery();
        renderService = new RenderService(
            smarteditBootstrapGateway,
            httpClient,
            $log,
            jq,
            alertService,
            componentHandlerService,
            crossFrameEventService,
            jQueryUtilsService,
            experienceService,
            seNamespaceService,
            systemEventService,
            notificationService,
            pageInfoService,
            perspectiveService,
            windowUtils,
            modalService
        );

        reprocessPageSpy = spyOn(renderService as any, '_reprocessPage');
        ((jq as any) as jasmine.Spy).calls.reset();
        httpClient.get.calls.reset();
    }

    describe('init', () => {
        beforeEach(() => {
            initService();
        });

        it('should be decorated with GatewayProxied', () => {
            const gatewayProxiedAnnotation = annotationService.getClassAnnotation(
                RenderService,
                GatewayProxied
            );
            expect(gatewayProxiedAnnotation).toEqual([
                'blockRendering',
                'isRenderingBlocked',
                'renderSlots',
                'renderComponent',
                'renderRemoval',
                'toggleOverlay',
                'refreshOverlayDimensions',
                'renderPage'
            ]);
        });

        it('extends IRenderService', () => {
            expect(renderService.renderSlots).toBeDefined();
            expect(renderService.renderComponent).toBeDefined();
            expect(renderService.renderRemoval).toBeDefined();
            expect(renderService.renderPage).toBeDefined();
            expect(renderService.toggleOverlay).toBeDefined();
            expect(renderService.refreshOverlayDimensions).toBeDefined();

            expect(renderService.blockRendering).toBeEmptyFunction();
            expect(renderService.isRenderingBlocked).toBeEmptyFunction();
        });
    });

    describe('renderRemoval', () => {
        let element: jasmine.SpyObj<JQuery>;

        beforeEach(() => {
            element = jasmine.createSpyObj('element', ['remove']);
            initService();
        });

        beforeEach(() => {
            componentHandlerService.getComponentUnderSlot.and.returnValue(element);
            spyOn(renderService, 'refreshOverlayDimensions');
        });

        beforeEach(() => {
            renderService.renderRemoval(MOCK_COMPONENT_ID, MOCK_COMPONENT_TYPE, SLOT_ID_1);
        });

        it('should remove the element', () => {
            expect(componentHandlerService.getComponentUnderSlot).toHaveBeenCalledWith(
                MOCK_COMPONENT_ID,
                MOCK_COMPONENT_TYPE,
                SLOT_ID_1
            );
            expect(element.remove).toHaveBeenCalled();
        });

        it('should refresh overlay dimensions', () => {
            expect(renderService.refreshOverlayDimensions).toHaveBeenCalled();
        });
    });

    describe('renderComponent', () => {
        let slotElement: jasmine.SpyObj<JQuery>;
        let componentElement: JQuery;
        let renderSlotsPromise: any;

        beforeEach(() => {
            slotElement = jasmine.createSpyObj('slotElement', ['attr']);
            slotElement.attr.and.returnValue(SLOT_ID_1);

            componentElement = jasmine.createSpyObj('component1', ['attr']);
            renderSlotsPromise = {};

            initService();

            spyOn(renderService, 'renderSlots').and.returnValue(renderSlotsPromise);
            componentHandlerService.getComponent.and.returnValue(componentElement);
            componentHandlerService.getParent.and.returnValue(slotElement);
        });

        it('extracts the slot ID for the given component ID and type', () => {
            renderService.renderComponent(MOCK_COMPONENT_ID, MOCK_COMPONENT_TYPE);
            expect(componentHandlerService.getComponent).toHaveBeenCalledWith(
                MOCK_COMPONENT_ID,
                MOCK_COMPONENT_TYPE
            );
            expect(componentHandlerService.getParent).toHaveBeenCalledWith(componentElement);
            expect(slotElement.attr).toHaveBeenCalledWith('data-smartedit-component-id');
        });

        it('delegates to renderSlots', () => {
            expect(renderService.renderComponent(MOCK_COMPONENT_ID, MOCK_COMPONENT_TYPE)).toBe(
                renderSlotsPromise
            );
            expect(renderService.renderSlots).toHaveBeenCalledWith(SLOT_ID_1);
        });
    });

    describe('renderSlots', () => {
        const EXPECTED_EXCEPTION_NO_SLOT_IDS = 'renderService.renderSlots.slotIds.required';
        let actual: Promise<any>;

        describe('when no slot ids are provided', () => {
            beforeEach((done) => {
                initService();
                spyOn(renderService, 'renderPage');
                actual = renderService.renderSlots();
                waitForPromiseToReject(actual, done);
            });

            it('returns a rejected promise with an error message', () => {
                expect(actual).toBeRejectedWithData(EXPECTED_EXCEPTION_NO_SLOT_IDS);
            });

            it('should not build refreshed preview url', () => {
                expect(experienceService.buildRefreshedPreviewUrl).not.toHaveBeenCalled();
            });

            it('should not fetch the page', () => {
                expect(httpClient.get).not.toHaveBeenCalled();
            });

            it('should not reprocess and render the page', () => {
                expect((renderService as any)._reprocessPage).not.toHaveBeenCalled();
                expect(renderService.renderPage).not.toHaveBeenCalled();
            });
        });

        describe('when an empty array of slot ids is provided', () => {
            beforeEach((done) => {
                initService();
                spyOn(renderService, 'renderPage');
                actual = renderService.renderSlots([]);
                waitForPromiseToReject(actual, done);
            });

            it('returns a rejected promise with an error message', () => {
                expect(actual).toBeRejectedWithData(EXPECTED_EXCEPTION_NO_SLOT_IDS);
            });

            it('should not build refreshed preview url when an empty array of slot ids is provided', () => {
                expect(experienceService.buildRefreshedPreviewUrl).not.toHaveBeenCalled();
            });

            it('should not fetch the page when an empty array of slot ids is provided', () => {
                expect(httpClient.get).not.toHaveBeenCalled();
            });

            it('should not reprocess and render the page when an empty array of slot ids is provided', () => {
                expect((renderService as any)._reprocessPage).not.toHaveBeenCalled();
                expect(renderService.renderPage).not.toHaveBeenCalled();
            });
        });

        describe('when http request fails', () => {
            const EXPECTED_HTML_ERROR_MESSAGE = 'ExpectedHTMLErrorMessage';

            beforeEach((done) => {
                httpClient.get.and.returnValue(throwError(EXPECTED_HTML_ERROR_MESSAGE));
                initService();
                spyOn(renderService, 'renderPage');
                actual = renderService.renderSlots(SLOT_ID_1);
                waitForPromiseToReject(actual, done);
            });

            it('should spawn an alert', () => {
                expect(alertService.showDanger).toHaveBeenCalledWith({
                    message: EXPECTED_HTML_ERROR_MESSAGE
                });
            });

            it('should not reprocess and render the page', () => {
                expect((renderService as any)._reprocessPage).not.toHaveBeenCalled();
                expect(renderService.renderPage).not.toHaveBeenCalled();
            });

            it('should return a rejected promise with the message in the error response', (done) => {
                actual.then(
                    (error: any) => {
                        fail('should have rejected');
                    },
                    (result) => {
                        expect(result).toBe(EXPECTED_HTML_ERROR_MESSAGE);
                        done();
                    }
                );
            });
        });

        describe('when multiple slot IDs are provided and http request succeeds', () => {
            const EXPECTED_SLOT_1_SELECTOR =
                '.smartEditComponent' +
                "[data-smartedit-component-type='ContentSlot']" +
                "[data-smartedit-component-id='" +
                SLOT_ID_1 +
                "']";
            const EXPECTED_SLOT_2_SELECTOR =
                '.smartEditComponent' +
                "[data-smartedit-component-type='ContentSlot']" +
                "[data-smartedit-component-id='" +
                SLOT_ID_2 +
                "']";
            const EXPECTED_FETCHED_SLOT_1_HTML = 'html1';
            const EXPECTED_FETCHED_SLOT_2_HTML = 'html2';
            const EXPECTED_HTTP_REQUEST_OBJECT = {
                headers: {
                    Accept: 'text/html',
                    Pragma: 'no-cache'
                },
                responseType: 'text'
            };

            let successHtmlResponse: any;
            let root: any;
            let originalSlotToReplace1: any;
            let originalSlotToReplace2: any;
            let fetchedSlotToRender1: any;
            let fetchedSlotToRender2: any;
            let jQuery: JQueryStatic;

            beforeEach(() => {
                originalSlotToReplace1 = jasmine.createSpyObj('originalSlotToReplace1', [
                    'html',
                    'css',
                    'data'
                ]);
                originalSlotToReplace2 = jasmine.createSpyObj('originalSlotToReplace2', [
                    'html',
                    'css',
                    'data'
                ]);
                fetchedSlotToRender1 = jasmine.createSpyObj('fetchedSlotToRender1', [
                    'html',
                    'css',
                    'data'
                ]);
                fetchedSlotToRender2 = jasmine.createSpyObj('fetchedSlotToRender2', [
                    'html',
                    'css',
                    'data'
                ]);
            });

            beforeEach((done) => {
                successHtmlResponse = '<html>some html</html>';
                root = {};
                httpClient.get.and.returnValue(of(successHtmlResponse));

                jQueryUtilsService = jasmine.createSpyObj('JQueryUtilsService', [
                    'extractFromElement',
                    'unsafeParseHTML'
                ]);
                jQueryUtilsService.unsafeParseHTML.and.returnValue(root);
                jQueryUtilsService.extractFromElement.and.callFake((parent: any, selector: any) => {
                    if (parent !== root) {
                        throw new Error(
                            `extractFromElement called with wrong 1st argument: ${parent}. Expected: ${root}`
                        );
                    }
                    if (selector === EXPECTED_SLOT_1_SELECTOR) {
                        return fetchedSlotToRender1;
                    } else if (selector === EXPECTED_SLOT_2_SELECTOR) {
                        return fetchedSlotToRender2;
                    }
                });

                const slot1: jasmine.SpyObj<JQuery<ElementForJQuery>> = jQueryHelper.wrap(
                    EXPECTED_SLOT_1_SELECTOR
                );
                slot1.html.and.returnValue(originalSlotToReplace1);

                const slot2: jasmine.SpyObj<JQuery<ElementForJQuery>> = jQueryHelper.wrap(
                    EXPECTED_SLOT_2_SELECTOR
                );
                slot2.html.and.returnValue(originalSlotToReplace2);

                fetchedSlotToRender1.html.and.returnValue(EXPECTED_FETCHED_SLOT_1_HTML);
                fetchedSlotToRender2.html.and.returnValue(EXPECTED_FETCHED_SLOT_2_HTML);

                jQuery = jQueryHelper.jQuery((selector) => {
                    if (selector === EXPECTED_SLOT_1_SELECTOR) {
                        return originalSlotToReplace1;
                    } else if (selector === EXPECTED_SLOT_2_SELECTOR) {
                        return originalSlotToReplace2;
                    }
                    throw new Error(`unexpected string selector: ${selector}`);
                });

                initService(jQuery);

                actual = renderService.renderSlots([SLOT_ID_1, SLOT_ID_2]);

                waitForPromiseToResolve(actual, done);
            });

            it('should build refreshed preview url', () => {
                expect(experienceService.buildRefreshedPreviewUrl).toHaveBeenCalled();
            });

            it('should call the httpClient.get with the current page URL', () => {
                expect(httpClient.get).toHaveBeenCalledWith(
                    MOCK_STOREFRONT_PREVIEW_URL,
                    EXPECTED_HTTP_REQUEST_OBJECT
                );
            });

            it('should parse the response html data', () => {
                expect(jQueryUtilsService.unsafeParseHTML.calls.count()).toBe(1);
                expect(jQueryUtilsService.unsafeParseHTML).toHaveBeenCalledWith(
                    successHtmlResponse
                );
            });

            it('should extract the slots to re-render from the DOM of the fetched page', () => {
                expect(jQueryUtilsService.extractFromElement.calls.count()).toBe(2);
                expect(jQueryUtilsService.extractFromElement.calls.argsFor(0)[0]).toBe(root);
                expect(jQueryUtilsService.extractFromElement.calls.argsFor(0)[1]).toBe(
                    EXPECTED_SLOT_1_SELECTOR
                );
                expect(jQueryUtilsService.extractFromElement.calls.argsFor(1)[0]).toBe(root);
                expect(jQueryUtilsService.extractFromElement.calls.argsFor(1)[1]).toBe(
                    EXPECTED_SLOT_2_SELECTOR
                );
            });

            it('should get the existing slots elements to replace from the current storefront', () => {
                expect(((jQuery as any) as jasmine.Spy).calls.count()).toBe(2);
                expect(((jQuery as any) as jasmine.Spy).calls.argsFor(0)[0]).toBe(
                    EXPECTED_SLOT_1_SELECTOR
                );
                expect(((jQuery as any) as jasmine.Spy).calls.argsFor(1)[0]).toBe(
                    EXPECTED_SLOT_2_SELECTOR
                );
            });

            it('should fetch the html of the new slot elements', () => {
                expect(fetchedSlotToRender1.html).toHaveBeenCalled();
                expect(fetchedSlotToRender2.html).toHaveBeenCalled();
            });

            it('should replace the html of the existing slot elements with that of the new slot elements', () => {
                expect(originalSlotToReplace1.html).toHaveBeenCalledWith(
                    EXPECTED_FETCHED_SLOT_1_HTML
                );
                expect(originalSlotToReplace2.html).toHaveBeenCalledWith(
                    EXPECTED_FETCHED_SLOT_2_HTML
                );
            });

            it('should re-process the page responsiveness', () => {
                expect(reprocessPageSpy.calls.count()).toBe(1);
            });
        });
    });

    describe('_markSmartEditAsReady', () => {
        beforeEach(() => {
            initService();
        });

        it('should publish smartEditReady event on the smartEditBootstrap gateway', () => {
            (renderService as any)._markSmartEditAsReady();
            expect(messageGateway.publish).toHaveBeenCalledWith('smartEditReady', {});
        });
    });

    describe('toggleOverlay', () => {
        let overlay: JQuery;

        beforeEach(() => {
            initService();
        });

        beforeEach(() => {
            overlay = jasmine.createSpyObj('overlay', ['css']);
            componentHandlerService.getOverlay.and.returnValue(overlay);
        });

        it('should make the SmartEdit overlay visible when passed true', () => {
            renderService.toggleOverlay(true);
            expect(overlay.css).toHaveBeenCalledWith('visibility', 'visible');
        });

        it('should make the SmartEdit overlay hidden when passed false', () => {
            renderService.toggleOverlay(false);
            expect(overlay.css).toHaveBeenCalledWith('visibility', 'hidden');
        });
    });

    describe('refreshOverlayDimensions', () => {
        let element: any;
        const wrappedElement: any = {};
        let parentOverlay: any;
        let originalComponents: any;
        let jQuery: JQueryStatic;

        beforeEach(() => {
            element = {};
            parentOverlay = {};
            originalComponents = jasmine.createSpyObj('originalComponents', ['each']);

            jQuery = jQueryHelper.jQuery((selector) => {
                if (selector === 'body') {
                    return element;
                } else if (selector === element) {
                    return wrappedElement;
                }
                throw new Error(`unexpected string selector: ${selector}`);
            });
            componentHandlerService.getFirstSmartEditComponentChildren.and.returnValue(
                originalComponents
            );
            initService(jQuery);

            spyOn(renderService, 'updateComponentSizeAndPosition');
            spyOn(renderService as any, '_getParentInOverlay').and.returnValue(parentOverlay);
        });

        it('should fetch the body as the element when given no parameters', () => {
            renderService.refreshOverlayDimensions();
            expect(((jQuery as any) as jasmine.Spy).calls.count()).toBe(1);
            expect(((jQuery as any) as jasmine.Spy).calls.argsFor(0)[0]).toBe('body');
        });

        it('should use the element provided instead of the body when provided with an element', () => {
            renderService.refreshOverlayDimensions(element);
            expect(((jQuery as any) as jasmine.Spy).calls.count()).toBe(0);
        });

        it('refreshOverlayDimensions should update the overlay dimensions for each child and call itself on each child', () => {
            renderService.refreshOverlayDimensions(element);
            spyOn(renderService, 'refreshOverlayDimensions');
            const eachCallback = originalComponents.each.calls.argsFor(0)[0];
            eachCallback(0, element);
            expect(renderService.updateComponentSizeAndPosition).toHaveBeenCalledWith(element);
            expect(renderService.refreshOverlayDimensions).toHaveBeenCalledWith(element);
        });
    });

    describe('updateComponentSizeAndPosition', () => {
        let shallowCopy: any;
        let componentInOverlay: any;
        let componentInOverlayList: any;
        let originalElement: jasmine.SpyObj<JQuery<ElementForJQuery>>;
        let parentOverlay: any;
        let unwrappedOriginalElement: any;
        let unwrappedParentOverlay: any;

        beforeEach(() => {
            shallowCopy = jasmine.createSpyObj('shallowCopy', ['width', 'height', 'css']);
            componentInOverlay = jasmine.createSpyObj('componentInOverlay', ['find']);
            componentInOverlay.style = {};
            componentInOverlayList = jasmine.createSpyObj('componentInOverlayList', [
                'get',
                'find'
            ]);
            originalElement = jasmine.createSpyObj('originalElement', ['attr', 'get', 'find']);

            parentOverlay = jasmine.createSpyObj('parentOverlay', ['get']);
            parentOverlay.length = 1;
            unwrappedOriginalElement = jasmine.createSpyObj('unwrappedOriginalElement', [
                'getBoundingClientRect'
            ]);
            unwrappedParentOverlay = jasmine.createSpyObj('unwrappedParentOverlay', [
                'getBoundingClientRect'
            ]);

            const jQuery: any = jasmine.createSpy('yjQuery');
            jQuery.and.callFake((arg: any) => {
                arg.on = (event: string): void => null;
                return arg;
            });
            initService(jQuery);

            spyOn(renderService as any, '_getParentInOverlay').and.callFake((element: any) => {
                if (element === originalElement) {
                    return parentOverlay;
                }
                return null;
            });
        });

        const ORIGINAL_ELEMENT_BOUNDING_CLIENT_RECT = {
            top: 123,
            left: 456
        };
        const PARENT_ELEMENT_BOUNDING_CLIENT_RECT = {
            top: 5,
            left: 10
        };

        beforeEach(() => {
            unwrappedOriginalElement.getBoundingClientRect.and.returnValue(
                ORIGINAL_ELEMENT_BOUNDING_CLIENT_RECT
            );
            unwrappedOriginalElement.offsetHeight = 789;
            unwrappedOriginalElement.offsetWidth = 890;

            originalElement.attr.and.callFake((attribute: any) => {
                if (attribute === ID_ATTRIBUTE) {
                    return MOCK_COMPONENT_ID;
                } else if (attribute === TYPE_ATTRIBUTE) {
                    return MOCK_COMPONENT_TYPE;
                }
                return null;
            });
            originalElement.get.and.returnValue(unwrappedOriginalElement);
            originalElement.find.and.returnValue(unwrappedOriginalElement);
            unwrappedParentOverlay.getBoundingClientRect.and.returnValue(
                PARENT_ELEMENT_BOUNDING_CLIENT_RECT
            );
            parentOverlay.get.and.returnValue(unwrappedParentOverlay);
            componentInOverlayList.get.and.returnValue(componentInOverlay);
            componentInOverlay.find.and.returnValue(shallowCopy);
            shallowCopy.width.and.returnValue(() => 890);
            shallowCopy.height.and.returnValue(() => 789);
            shallowCopy.css.and.returnValue(null);
            componentHandlerService.getComponentCloneInOverlay.and.returnValue(
                componentInOverlayList
            );
        });

        it('updateComponentSizeAndPosition should fetch the component in overlay', () => {
            renderService.updateComponentSizeAndPosition(originalElement as any);
            expect(componentHandlerService.getComponentCloneInOverlay).toHaveBeenCalledWith(
                originalElement
            );
            expect(componentInOverlayList.get).toHaveBeenCalledWith(0);
        });

        it('should not fetch the component in overlay if it is provided', () => {
            renderService.updateComponentSizeAndPosition(
                originalElement as any,
                componentInOverlay
            );
            expect(componentHandlerService.getComponentCloneInOverlay).not.toHaveBeenCalled();
            expect(componentInOverlayList.get).not.toHaveBeenCalled();
        });

        it('should update position and dimensions of the given overlay clone from the original component', () => {
            renderService.updateComponentSizeAndPosition(
                originalElement as any,
                componentInOverlay
            );
            expect(componentInOverlay.find).toHaveBeenCalledWith(
                '[id="someComponentId_someComponentType_overlay"]'
            );
            expect(componentInOverlay.style).toEqual({
                position: 'absolute',
                top: '118px',
                left: '446px',
                width: '890px',
                height: '789px',
                minWidth: '51px',
                minHeight: '48px'
            });

            expect(shallowCopy.width).toHaveBeenCalledWith(890);
            expect(shallowCopy.height).toHaveBeenCalledWith(789);
            expect(shallowCopy.css.calls.allArgs()).toEqual([
                ['min-height', 49],
                ['min-width', 51]
            ]);
        });
    });

    describe('_cloneComponent', () => {
        const MOCK_COMPONENT_CATALOG_VERSION = 'someComponentCatalogVersion';

        let element: any;
        let parentOverlay: any;
        let document: any;
        let shallowCopy: any;
        let smartEditWrapper: any;
        let componentDecorator: any;
        const attrMap: any = {};
        let jQuery: any;

        beforeEach(() => {
            element = jasmine.createSpyObj('element', ['attr', 'get', 'parent', 'is']);
            element.is.and.returnValue(true);

            attrMap[ID_ATTRIBUTE] = MOCK_COMPONENT_ID;
            attrMap[UUID_ATTRIBUTE] = MOCK_COMPONENT_ID;
            attrMap[TYPE_ATTRIBUTE] = MOCK_COMPONENT_TYPE;
            attrMap[CATALOG_VERSION_UUID_ATTRIBUTE] = MOCK_COMPONENT_CATALOG_VERSION;

            element.attr.and.callFake((attribute: any) => {
                return attrMap[attribute];
            });
            element.get.and.returnValue({
                attributes: [
                    {
                        nodeName: 'nonsmarteditattribute',
                        nodeValue: 'somevalue'
                    },
                    {
                        nodeName: 'data-smartedit-component-id',
                        nodeValue: MOCK_COMPONENT_ID
                    },
                    {
                        nodeName: 'data-smartedit-component-type',
                        nodeValue: MOCK_COMPONENT_TYPE
                    }
                ]
            });
            element.on = (event: string): void => null;

            parentOverlay = jasmine.createSpyObj('parentOverlay', ['append']);
            parentOverlay.length = 1;
            document = jasmine.createSpyObj('document', ['createElement']);
            shallowCopy = {};
            smartEditWrapper = {};
            smartEditWrapper.style = {};
            componentDecorator = jasmine.createSpyObj('componentDecorator', [
                'addClass',
                'attr',
                'append'
            ]);
            componentDecorator.on = (event: string): void => null;

            let callCount = 0;
            document.createElement.and.callFake(() => {
                callCount++;
                return callCount === 1 ? shallowCopy : smartEditWrapper;
            });

            jQuery = jasmine.createSpy('yjQuery');
            jQuery.and.callFake((el: any) => {
                if (el === smartEditWrapper) {
                    return componentDecorator;
                }
                return element; // to ease testing
            });

            componentHandlerService.getParent.and.callFake((el: any) => {
                if (el === element) {
                    return parentOverlay;
                }
                return null;
            });
            componentHandlerService.getOverlayComponent.and.returnValue(parentOverlay);

            initService(jQuery);

            spyOn(renderService as any, '_getDocument').and.returnValue(document);
            spyOn(renderService, 'updateComponentSizeAndPosition');

            (renderService as any)._cloneComponent(element);
        });

        it('should create a shallow copy of the component and a SmartEdit decorator wrapper', () => {
            expect(((renderService as any)._getDocument as jasmine.Spy).calls.count()).toBe(2);
            expect(document.createElement.calls.count()).toBe(2);
            expect(document.createElement.calls.argsFor(0)[0]).toBe('div');
            expect(document.createElement.calls.argsFor(1)[0]).toBe('smartedit-element');
        });

        it('should add an overlay identifier to the shallow copy element', () => {
            expect(shallowCopy.id).toBe('someComponentId_someComponentType_overlay');
        });

        it('should update the overlay dimensions for the newly created SmartEdit decorator wrapper', () => {
            expect(renderService.updateComponentSizeAndPosition).toHaveBeenCalledWith(
                element.get(0),
                smartEditWrapper
            );
        });

        it('should fetch a wrapped SmartEdit decorator wrapper', () => {
            expect(jQuery).toHaveBeenCalledWith(smartEditWrapper);
        });

        it('should add the overlay class to the wrapped SmartEdit decorator wrapper', () => {
            expect(componentDecorator.addClass).toHaveBeenCalledWith('smartEditComponentX');
        });

        it('should copy attributes with "data-smartedit" prefix from the element to the wrapped SmartEdit decorator wrapper', () => {
            expect(element.get).toHaveBeenCalledWith(0);
            expect(componentDecorator.attr.calls.count()).toBe(2);
            expect(componentDecorator.attr.calls.argsFor(0)).toEqual([
                'data-smartedit-component-id',
                MOCK_COMPONENT_ID
            ]);
            expect(componentDecorator.attr.calls.argsFor(1)).toEqual([
                'data-smartedit-component-type',
                MOCK_COMPONENT_TYPE
            ]);
        });

        it('should append the shallow copy onto the SmartEdit decorator wrapper', () => {
            expect(componentDecorator.append).toHaveBeenCalledWith(shallowCopy);
        });

        it('should append the compiled element on the parent overlay', () => {
            expect(parentOverlay.append).toHaveBeenCalledWith(smartEditWrapper);
        });

        describe('when component is a NavigationBarCollectionComponent', () => {
            beforeEach(() => {
                attrMap[TYPE_ATTRIBUTE] = 'NavigationBarCollectionComponent';
                (renderService as any)._cloneComponent(element);
            });

            it('should add a specific z-index to the styling of the SmartEdit decorator wrapper', () => {
                const EXPECTED_Z_INDEX_FOR_NAVIGATION = '7';
                expect(smartEditWrapper.style.zIndex).toBe(EXPECTED_Z_INDEX_FOR_NAVIGATION);
            });
        });
    });

    describe('_getParentInOverlay', () => {
        let element: any;
        let parent: any;

        beforeEach(() => {
            element = jasmine.createSpyObj('element', ['attr']);
            parent = {
                length: 1
            };
        });

        describe('when called with a SmartEdit component', () => {
            let expectedOverlay = {};
            let actualOverlay = {};
            const overlay = {};
            beforeEach(() => {
                expectedOverlay = {};
                componentHandlerService.getOverlayComponent.and.returnValue(expectedOverlay);
                componentHandlerService.getOverlay.and.returnValue(overlay);
                initService();
            });

            it('should fetch the component in the overlay', () => {
                componentHandlerService.getParent.and.returnValue(parent);

                actualOverlay = (renderService as any)._getParentInOverlay(element);

                expect(componentHandlerService.getParent).toHaveBeenCalledWith(element);
                expect(componentHandlerService.getOverlayComponent).toHaveBeenCalledWith(parent);
                expect(actualOverlay).toBe(expectedOverlay);
            });

            it('should fetch the overlay', () => {
                componentHandlerService.getParent.and.returnValue({});

                actualOverlay = (renderService as any)._getParentInOverlay(element);

                expect(componentHandlerService.getParent).toHaveBeenCalledWith(element);
                expect(componentHandlerService.getOverlay).toHaveBeenCalled();
                expect(actualOverlay).toBe(overlay);
            });
        });
    });

    describe('createComponent', () => {
        let element: any;
        let parentOverlay: any;
        let elements: any;
        let children: any;
        let emptyChildren: any;
        let jQuery: any;

        beforeEach(() => {
            element = {};
            parentOverlay = {};
            elements = ['child1', 'child2'];
            children = jasmine.createSpyObj('children', ['each']);
            children.each.and.callFake((callback: any) => {
                elements.forEach((child: any) => {
                    callback(0, child);
                });
            });

            emptyChildren = jasmine.createSpyObj('emptyChildren', ['each']);
            emptyChildren.each.and.returnValue();

            componentHandlerService.getFirstSmartEditComponentChildren.and.callFake((arg: any) => {
                return arg === element ? children : emptyChildren;
            });

            componentHandlerService.isOverlayOn.and.returnValue(true);

            jQuery = jasmine.createSpy('yjQuery');
            jQuery.and.callFake((arg: any) => {
                arg.on = (event: string): void => null;
                return arg;
            });
            initService(jQuery);

            spyOn(renderService as any, '_getParentInOverlay').and.returnValue(parentOverlay);
        });

        it('should clone and compile the targeted element only', () => {
            // Arrange
            const isComponentVisibleSpy: jasmine.Spy = spyOn(
                renderService as any,
                '_isComponentVisible'
            ).and.callThrough();
            element.offsetParent = 1;
            const cloneComponentSpy: jasmine.Spy = spyOn(renderService as any, '_cloneComponent');

            // Act
            renderService.createComponent(element);

            // Assert
            expect(isComponentVisibleSpy.calls.count()).toBe(1);
            expect(componentHandlerService.isOverlayOn).toHaveBeenCalledTimes(1);
            expect(cloneComponentSpy).toHaveBeenCalledWith(element);
        });

        it('should not clone if element is not visible', () => {
            // Arrange
            const isComponentVisibleSpy: jasmine.Spy = spyOn(
                renderService as any,
                '_isComponentVisible'
            );
            isComponentVisibleSpy.and.callFake((el: any) => {
                return element !== el;
            });
            const cloneComponentSpy: jasmine.Spy = spyOn(renderService as any, '_cloneComponent');

            // Act
            renderService.createComponent(element);

            // Assert
            expect(isComponentVisibleSpy.calls.count()).toBe(1);
            expect(cloneComponentSpy).not.toHaveBeenCalled();
        });
    });

    describe('renderPage', () => {
        let overlay: JQuery;

        beforeEach(() => {
            overlay = jasmine.createSpyObj('overlay', ['hide', 'show']);
            componentHandlerService.getOverlay.and.returnValue(overlay);
            initService();
            spyOn(renderService, 'resizeSlots');
            spyOn(renderService as any, '_markSmartEditAsReady');
            spyOn(renderService, 'isRenderingBlocked').and.returnValue($q.when(true));
        });

        it('will publish overlay rerendered event', () => {
            renderService.renderPage(false);
            expect(crossFrameEventService.publish).toHaveBeenCalledWith('overlayRerendered');
        });

        it('call with isRerender:false will publish restartProcess', () => {
            renderService.renderPage(false);
            expect(systemEventService.publish).toHaveBeenCalledWith(
                CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS.RESTART_PROCESS
            );
        });

        it('call with isRerender:true will publish restartProcess', () => {
            renderService.renderPage(true);
            expect(systemEventService.publish).toHaveBeenCalledWith(
                CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS.RESTART_PROCESS
            );
        });

        it('should remove the overlay', () => {
            renderService.renderPage(false);
            expect(overlay.hide).toHaveBeenCalled();
        });

        it('when current perspective is "Preview" should not mark SmartEdit as ready', () => {
            renderService.renderPage(false);
            expect((renderService as any)._markSmartEditAsReady).toHaveBeenCalled();
        });

        it('should resize the slots', () => {
            renderService.renderPage(false);
            expect(renderService.resizeSlots).toHaveBeenCalled();
        });
    });

    describe('destroyComponent', () => {
        let elements: any;
        let child1: any;
        let child2: any;
        let component: any;
        let parent: any;
        let componentInOverlay: any;
        const slotId = 'someSlotId';
        let jQuery: any;

        beforeEach(() => {
            parent = jasmine.createSpyObj('parent', ['attr']);
            component = jasmine.createSpyObj('component', ['attr']);
            child1 = jasmine.createSpyObj('child1', ['attr']);
            child2 = jasmine.createSpyObj('child2', ['attr']);
            elements = [child1, child2];
            componentInOverlay = jasmine.createSpyObj('componentInOverlay', ['remove']);

            component.attr.and.callFake((element: any) => {
                if (element === ID_ATTRIBUTE) {
                    return MOCK_COMPONENT_ID;
                } else if (element === TYPE_ATTRIBUTE) {
                    return MOCK_COMPONENT_TYPE;
                }
                return null;
            });

            componentHandlerService.getOverlayComponentWithinSlot.and.returnValue(
                componentInOverlay
            );
            componentHandlerService.getComponentInOverlay.and.returnValue(componentInOverlay);

            const children = jasmine.createSpyObj('children', ['each']);
            children.each.and.callFake((callback: any) => {
                elements.forEach((arg1: any) => {
                    callback(0, arg1);
                });
            });
            const emptyChildren = jasmine.createSpyObj('emptyChildren', ['each']);
            emptyChildren.each.and.returnValue();

            componentHandlerService.getFirstSmartEditComponentChildren.and.callFake((arg: any) => {
                return arg === component ? children : emptyChildren;
            });

            jQuery = jasmine.createSpy('yjQuery');
            jQuery.and.callFake((arg: any) => {
                arg.on = (event: string): void => null;
                return arg;
            });
            initService(jQuery);

            spyOn(renderService, 'destroyComponent').and.callThrough();
        });

        it('will destroy overlay component with id and type from component', () => {
            parent.attr.and.returnValue(slotId);

            renderService.destroyComponent(component, parent);

            expect(componentHandlerService.getOverlayComponentWithinSlot).toHaveBeenCalledWith(
                MOCK_COMPONENT_ID,
                MOCK_COMPONENT_TYPE,
                slotId
            );

            expect(componentInOverlay.remove).toHaveBeenCalled();
            expect(parent.attr).toHaveBeenCalledWith(ID_ATTRIBUTE);
        });

        it('will destroy overlay component with id and type from slot', () => {
            parent.attr.and.returnValue(undefined);

            renderService.destroyComponent(component, parent);

            expect(componentHandlerService.getComponentInOverlay).toHaveBeenCalledWith(
                MOCK_COMPONENT_ID,
                MOCK_COMPONENT_TYPE
            );

            expect(componentInOverlay.remove).toHaveBeenCalled();
            expect(parent.attr).toHaveBeenCalledWith(ID_ATTRIBUTE);
        });

        it('will destroy overlay component with id and type from component from old attributes', () => {
            parent.attr.and.returnValue(slotId);

            const oldAttributes: any = {};
            oldAttributes[ID_ATTRIBUTE] = 'oldId';
            oldAttributes[TYPE_ATTRIBUTE] = 'oldType';

            renderService.destroyComponent(component, parent, oldAttributes);

            expect(componentHandlerService.getOverlayComponentWithinSlot).toHaveBeenCalledWith(
                'oldId',
                'oldType',
                slotId
            );

            expect(componentInOverlay.remove).toHaveBeenCalled();
            expect(parent.attr).toHaveBeenCalledWith(ID_ATTRIBUTE);
        });
    });

    function waitForPromiseToResolve(actual: Promise<any>, done: () => void): void {
        actual.then(
            (error: any) => {
                done();
            },
            (result) => {
                fail('should have resolved');
            }
        );
    }

    function waitForPromiseToReject(actual: Promise<any>, done: () => void): void {
        actual.then(
            (error: any) => {
                fail('should have rejected');
            },
            (result) => {
                done();
            }
        );
    }
});
