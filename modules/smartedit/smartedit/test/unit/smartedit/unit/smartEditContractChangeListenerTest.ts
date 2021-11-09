/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    promiseUtils,
    ComponentEntry,
    COMPONENT_CLASS,
    CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS,
    CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS,
    ID_ATTRIBUTE,
    IPageInfoService,
    IPositionRegistry,
    IResizeListener,
    JQueryUtilsService,
    LogService,
    PolyfillService,
    SystemEventService,
    TestModeService,
    TypedMap,
    UUID_ATTRIBUTE
} from 'smarteditcommons';
import { ComponentHandlerService } from 'smartedit/services';
import { SmartEditContractChangeListener } from 'smartedit/services/SmartEditContractChangeListener';

import * as angular from 'angular';

describe('smartEditContractChangeListener in polyfill mode', () => {
    const logService = new LogService();
    let systemEventService: SystemEventService;
    let componentHandlerService: jasmine.SpyObj<ComponentHandlerService>;
    let pageinfoService: jasmine.SpyObj<IPageInfoService>;
    let yjQuery: any;
    const SMARTEDIT_COMPONENT_PROCESS_STATUS = 'smartEditComponentProcessStatus';
    let isInExtendedViewPort: jasmine.Spy;
    let smartEditContractChangeListener: SmartEditContractChangeListener;
    let testModeService: jasmine.SpyObj<TestModeService>;
    let mutationObserverMock: jasmine.SpyObj<MutationObserver>;
    let intersectionObserverMock: jasmine.SpyObj<IntersectionObserver>;
    let mutationObserverCallback: any;
    let intersectionObserverCallback: any;
    let onComponentRepositionedCallback: jasmine.Spy;
    let onComponentResizedCallback: (component: HTMLElement) => void;
    let onComponentsAddedCallback: jasmine.Spy;
    let onComponentsRemovedCallback: jasmine.Spy;
    let onComponentChangedCallback: jasmine.Spy;
    let resizeListener: jasmine.SpyObj<IResizeListener>;
    let positionRegistry: jasmine.SpyObj<IPositionRegistry>;
    let runIntersectionObserver: (queue: IntersectionObserverEntry[]) => void;
    let parent: jasmine.SpyObj<HTMLElement>;
    let directParent: jasmine.SpyObj<HTMLElement>;
    let component0: jasmine.SpyObj<HTMLElement>;
    let component1: jasmine.SpyObj<HTMLElement>;
    let component2: jasmine.SpyObj<HTMLElement>;
    let component21: jasmine.SpyObj<HTMLElement>;
    let component3: jasmine.SpyObj<HTMLElement>;
    let invisibleComponent: jasmine.SpyObj<HTMLElement>;
    let nonProcessableComponent: jasmine.SpyObj<HTMLElement>;
    let detachedComponent: jasmine.SpyObj<HTMLElement>;
    let polyfillService: jasmine.SpyObj<PolyfillService>;
    let yjQueryUtilsService: jasmine.SpyObj<JQueryUtilsService>;

    const holder: any = {};

    let SECOND_LEVEL_CHILDREN: jasmine.SpyObj<HTMLElement>[];
    let INTERSECTIONS_MAPPING: IntersectionObserverEntry[];

    const INITIAL_PAGE_UUID = 'INITIAL_PAGE_UUID';
    const ANY_PAGE_UUID = 'ANY_PAGE_UUID';
    const BODY_TAG = 'body';
    const BODY = {};

    async function playMutationObserverCallback(mutations: any) {
        mutationObserverCallback(mutations);

        await promiseUtils.sleep(4);
    }

    const getComponents = () => {
        return Array.from(
            (smartEditContractChangeListener as any).componentsQueue.values(),
            ({ component }) => component
        );
    };

    beforeEach(() => {
        systemEventService = new SystemEventService(logService, promiseUtils);

        (window as any).elementResizeDetectorMaker = () => {
            return {
                uninstall: angular.noop
            };
        };

        testModeService = jasmine.createSpyObj('testModeService', ['isE2EMode']);
        testModeService.isE2EMode.and.returnValue(true);

        polyfillService = jasmine.createSpyObj('polyfillService', ['isEligibleForExtendedView']);
        polyfillService.isEligibleForExtendedView.and.returnValue(true);

        isInExtendedViewPort = jasmine.createSpy('isInExtendedViewPort');

        // we here give isInExtendedViewPort the same beahviour as isIntersecting
        isInExtendedViewPort.and.callFake((element: HTMLElement) => {
            const obj = INTERSECTIONS_MAPPING.find((object) => {
                return object.target === element;
            });
            return obj ? obj.isIntersecting : false;
        });

        componentHandlerService = jasmine.createSpyObj('componentHandlerService', [
            'getClosestSmartEditComponent',
            'isSmartEditComponent',
            'getFirstSmartEditComponentChildren',
            'getParent'
        ]);
        pageinfoService = jasmine.createSpyObj('pageinfoService', ['getPageUUID']);

        resizeListener = jasmine.createSpyObj('resizeListener', [
            'register',
            'unregister',
            'fix',
            'dispose',
            'init'
        ]);
        positionRegistry = jasmine.createSpyObj('positionRegistry', [
            'register',
            'unregister',
            'getRepositionedComponents',
            'dispose'
        ]);

        positionRegistry.getRepositionedComponents.and.returnValue([]);

        const contains = jasmine.createSpy('contains');
        yjQuery = jasmine.createSpy('yjQuery');
        yjQuery.contains = contains;

        yjQuery.contains.and.callFake((container: any, element: any) => {
            return element.name !== (detachedComponent as any).name;
        });

        yjQuery.fn = {
            extend() {
                return;
            }
        };

        yjQueryUtilsService = jasmine.createSpyObj('yjQueryUtilsService', [
            'isInExtendedViewPort',
            'isInDOM'
        ]);

        yjQueryUtilsService.isInExtendedViewPort.and.callFake((element: HTMLElement) => {
            const obj = INTERSECTIONS_MAPPING.find((object) => {
                return object.target === element;
            });
            return obj ? obj.isIntersecting : false;
        });

        yjQueryUtilsService.isInDOM.and.callFake((element: any) => {
            return element.name !== (detachedComponent as any).name;
        });
    });

    beforeEach(() => {
        smartEditContractChangeListener = new SmartEditContractChangeListener(
            yjQueryUtilsService,
            componentHandlerService,
            pageinfoService,
            resizeListener,
            positionRegistry,
            logService,
            yjQuery,
            systemEventService,
            polyfillService,
            testModeService
        );

        mutationObserverMock = jasmine.createSpyObj('MutationObserver', ['observe', 'disconnect']);
        spyOn(smartEditContractChangeListener, '_newMutationObserver').and.callFake(function(
            callback: jasmine.SpyObj<MutationCallback>
        ) {
            mutationObserverCallback = callback;
            this.observe = angular.noop;
            this.disconnect = angular.noop;
            return mutationObserverMock;
        });

        intersectionObserverMock = jasmine.createSpyObj('IntersectionObserver', [
            'observe',
            'unobserve',
            'disconnect'
        ]);
        spyOn(smartEditContractChangeListener, '_newIntersectionObserver').and.callFake(
            (callback: jasmine.SpyObj<IntersectionObserverCallback>) => {
                intersectionObserverCallback = callback;
                return intersectionObserverMock;
            }
        );

        intersectionObserverMock.observe.and.callFake((comp: HTMLElement) => {
            // run time intersectionObserver would indeed trigger a callback immediately after observing
            intersectionObserverCallback(
                INTERSECTIONS_MAPPING.filter((intersection) => {
                    return intersection.target === comp;
                })
            );
        });

        runIntersectionObserver = async (queue) => {
            intersectionObserverCallback(queue);

            await promiseUtils.sleep(4);
        };

        onComponentRepositionedCallback = jasmine.createSpy('onComponentRepositioned');
        smartEditContractChangeListener.onComponentRepositioned(onComponentRepositionedCallback);

        onComponentResizedCallback = angular.noop;
        smartEditContractChangeListener.onComponentResized(onComponentResizedCallback);

        onComponentsAddedCallback = jasmine.createSpy('onComponentsAdded');
        smartEditContractChangeListener.onComponentsAdded(onComponentsAddedCallback);

        onComponentsRemovedCallback = jasmine.createSpy('onComponentsRemoved');
        smartEditContractChangeListener.onComponentsRemoved(onComponentsRemovedCallback);

        onComponentChangedCallback = jasmine.createSpy('onComponentChangedCallback');
        smartEditContractChangeListener.onComponentChanged(onComponentChangedCallback);
    });

    beforeEach(() => {
        parent = jasmine.createSpyObj('parent', ['attr']);
        (parent as any).outerHTML = '<div></div>';
        (parent as any).tagName = 'PARENT';
        (parent as any).nodeType = 1;
        (parent as any).className = COMPONENT_CLASS;
        (parent as any).attr.and.returnValue('parent');
        (parent as any).name = 'parent';
        (parent as any).contains = () => {
            return true;
        };
        (parent as any).sourceIndex = 0;
        (parent as any).dataset = {};

        directParent = jasmine.createSpyObj('directParent', ['attr']);
        (directParent as any).nodeType = 1;
        (directParent as any).attr.and.returnValue('directParent');
        (directParent as any).name = 'directParent';
        (directParent as any).contains = () => {
            return false;
        };
        (directParent as any).sourceIndex = 1;
        (directParent as any).dataset = {};
        (directParent as any).hasAttribute = () => {
            return true;
        };

        component0 = jasmine.createSpyObj('component0', ['attr']);
        (component0 as any).outerHTML = '<div></div>';
        (component0 as any).tagName = 'DIV';
        (component0 as any).nodeType = 1;
        (component0 as any).className = 'nonSmartEditComponent';
        (component0 as any).attr.and.returnValue('component0');
        (component0 as any).name = 'component0';
        (component0 as any).contains = () => {
            return false;
        };
        (component0 as any).sourceIndex = 2;
        (component0 as any).dataset = {};
        (component0 as any).hasAttribute = () => {
            return true;
        };

        component1 = jasmine.createSpyObj('component1', ['attr']);
        (component1 as any).outerHTML = '<div></div>';
        (component1 as any).tagName = 'DIV';
        (component1 as any).nodeType = 1;
        (component1 as any).className = COMPONENT_CLASS;
        (component1 as any).attr.and.returnValue('component1');
        (component1 as any).name = 'component1';
        (component1 as any).contains = () => {
            return true;
        };
        (component1 as any).sourceIndex = 3;
        (component1 as any).dataset = {};
        (component1 as any).hasAttribute = () => {
            return true;
        };

        component21 = jasmine.createSpyObj('component2_1', ['attr']);
        (component21 as any).outerHTML = '<div></div>';
        (component21 as any).tagName = 'DIV';
        (component21 as any).nodeType = 1;
        (component21 as any).className = COMPONENT_CLASS;
        (component21 as any).attr.and.returnValue('component2_1');
        (component21 as any).name = 'component2_1';
        (component21 as any).contains = () => {
            return false;
        };
        (component21 as any).sourceIndex = 5;
        (component21 as any).dataset = {};
        (component21 as any).hasAttribute = () => {
            return true;
        };

        component2 = jasmine.createSpyObj('component2', ['attr']);
        (component2 as any).outerHTML = '<div></div>';
        (component2 as any).tagName = 'DIV';
        (component2 as any).nodeType = 1;
        (component2 as any).className = COMPONENT_CLASS;
        (component2 as any).attr.and.returnValue('component2');
        (component2 as any).name = 'component2';
        (component2 as any).contains = (node: HTMLElement) => {
            return node === component21;
        };
        (component2 as any).sourceIndex = 4;
        (component2 as any).dataset = {};
        (component2 as any).hasAttribute = () => {
            return true;
        };

        component3 = jasmine.createSpyObj('component3', ['attr']);
        (component3 as any).outerHTML = '<div></div>';
        (component3 as any).tagName = 'DIV';
        (component3 as any).nodeType = 1;
        (component3 as any).className = COMPONENT_CLASS;
        (component3 as any).attr.and.returnValue('component3');
        (component3 as any).name = 'component3';
        (component3 as any).contains = () => {
            return false;
        };
        (component3 as any).sourceIndex = 6;
        (component3 as any).dataset = {};
        (component3 as any).hasAttribute = () => {
            return true;
        };

        invisibleComponent = jasmine.createSpyObj('invisibleComponent', ['attr']);
        (invisibleComponent as any).nodeType = 1;
        (invisibleComponent as any).className = COMPONENT_CLASS;
        (invisibleComponent as any).attr.and.returnValue('invisibleComponent');
        (invisibleComponent as any).name = 'invisibleComponent';
        (invisibleComponent as any).contains = () => {
            return false;
        };
        (invisibleComponent as any).sourceIndex = 8;
        (invisibleComponent as any).dataset = {};
        (invisibleComponent as any).hasAttribute = () => {
            return true;
        };

        nonProcessableComponent = jasmine.createSpyObj('nonProcessableComponent', ['attr']);
        (nonProcessableComponent as any).nodeType = 1;
        (nonProcessableComponent as any).className = COMPONENT_CLASS;
        (nonProcessableComponent as any).attr.and.returnValue('nonProcessableComponent');
        (nonProcessableComponent as any).name = 'nonProcessableComponent';
        (nonProcessableComponent as any).contains = () => {
            return false;
        };
        (nonProcessableComponent as any).sourceIndex = 8;
        (nonProcessableComponent as any).dataset = {};
        (nonProcessableComponent as any).hasAttribute = () => {
            return true;
        };

        detachedComponent = jasmine.createSpyObj('detachedComponent', ['attr']);
        (detachedComponent as any).nodeType = 1;
        (detachedComponent as any).className = COMPONENT_CLASS;
        (detachedComponent as any).attr.and.returnValue('detachedComponent');
        (detachedComponent as any).name = 'detachedComponent';
        (detachedComponent as any).contains = () => {
            return false;
        };
        (detachedComponent as any).sourceIndex = 9;
        (detachedComponent as any).dataset = {};
        (detachedComponent as any).hasAttribute = () => {
            return true;
        };

        let pageUUIDCounter = 0;
        pageinfoService.getPageUUID.and.callFake(() => {
            pageUUIDCounter++;
            if (pageUUIDCounter === 1) {
                return Promise.resolve(INITIAL_PAGE_UUID);
            } else if (pageUUIDCounter === 2) {
                return Promise.resolve(ANY_PAGE_UUID);
            }
            return Promise.resolve(null);
        });

        yjQuery.and.callFake((arg: string) => {
            if (arg === BODY_TAG) {
                return BODY;
            }
            return null;
        });

        componentHandlerService.isSmartEditComponent.and.callFake((node: HTMLElement) => {
            return node.className && node.className.split(/[\s]+/).indexOf(COMPONENT_CLASS) > -1;
        });

        componentHandlerService.getClosestSmartEditComponent.and.callFake((node: HTMLElement) => {
            if (
                node === parent ||
                node === component1 ||
                node === component2 ||
                node === component21 ||
                node === component3
            ) {
                return [node];
            } else if (node === component0) {
                return [parent];
            } else {
                return [];
            }
        });

        componentHandlerService.getParent.and.callFake((node: HTMLElement) => {
            if (node === component21) {
                return [component2];
            } else if (node === component1 || node === component2 || node === component3) {
                return [parent];
            } else {
                return [];
            }
        });

        SECOND_LEVEL_CHILDREN = [component1];
        INTERSECTIONS_MAPPING = [
            {
                isIntersecting: true,
                target: component1 // child before 'parent'
            },
            {
                isIntersecting: true,
                target: parent
            },
            {
                isIntersecting: false,
                target: invisibleComponent
            },
            {
                isIntersecting: true,
                target: nonProcessableComponent
            }
        ] as any;

        componentHandlerService.getFirstSmartEditComponentChildren.and.callFake(
            (node: HTMLElement) => {
                if (node === BODY) {
                    return [parent];
                } else if (node === parent) {
                    return SECOND_LEVEL_CHILDREN;
                } else if (node === component2) {
                    return [component21];
                } else if (node === component0) {
                    return [component2]; // ok to just return array, slice is applied on it
                } else {
                    return [];
                }
            }
        );

        holder.canProcess = (comp: HTMLElement) => {
            return comp !== nonProcessableComponent;
        };

        systemEventService.subscribe(
            CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS.PROCESS_COMPONENTS,
            (eventId, components) => {
                const result = components.map((component: HTMLElement) => {
                    component.dataset[SMARTEDIT_COMPONENT_PROCESS_STATUS] = holder.canProcess(
                        component
                    )
                        ? CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS.PROCESS
                        : CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS.REMOVE;
                    return component;
                });
                return Promise.resolve(result);
            }
        );
    });

    beforeEach(async () => {
        smartEditContractChangeListener.initListener();

        await promiseUtils.sleep(4);
    });

    describe('DOM intersections', () => {
        it('should register resize and position listeners on existing visible smartedit components that are processable', () => {
            expect(resizeListener.register.calls.count()).toEqual(2);
            expect(resizeListener.register.calls.argsFor(0)[0]).toBe(parent);
            expect(resizeListener.register.calls.argsFor(1)[0]).toBe(component1);

            expect(positionRegistry.register.calls.count()).toEqual(2);
            expect(positionRegistry.register.calls.argsFor(0)[0]).toBe(parent);
            expect(positionRegistry.register.calls.argsFor(1)[0]).toBe(component1);

            expect(onComponentsAddedCallback.calls.count()).toBe(2);
            expect(onComponentsAddedCallback.calls.argsFor(0)[0]).toEqual([parent]);
            expect(onComponentsAddedCallback.calls.argsFor(1)[0]).toEqual([component1]);
        });

        it('event with same intersections for components should not retrigger anything', async (done) => {
            await runIntersectionObserver(INTERSECTIONS_MAPPING);

            resizeListener.register.calls.reset();
            positionRegistry.register.calls.reset();
            onComponentsAddedCallback.calls.reset();
            onComponentsRemovedCallback.calls.reset();

            await runIntersectionObserver(INTERSECTIONS_MAPPING);

            expect(resizeListener.register).not.toHaveBeenCalled();
            expect(positionRegistry.register).not.toHaveBeenCalled();
            expect(onComponentsAddedCallback).not.toHaveBeenCalled();
            expect(onComponentsRemovedCallback).not.toHaveBeenCalled();

            done();
        });

        it('when components are no longer visible, they are destroyed', async (done) => {
            await runIntersectionObserver(INTERSECTIONS_MAPPING);

            resizeListener.register.calls.reset();
            resizeListener.unregister.calls.reset();
            positionRegistry.register.calls.reset();
            positionRegistry.unregister.calls.reset();
            onComponentsAddedCallback.calls.reset();
            onComponentsRemovedCallback.calls.reset();

            INTERSECTIONS_MAPPING.forEach((element: IntersectionObserverEntry) => {
                (element as any).isIntersecting = false;
            });

            await runIntersectionObserver(INTERSECTIONS_MAPPING);

            expect(resizeListener.register).not.toHaveBeenCalled();
            expect(resizeListener.unregister).not.toHaveBeenCalled();

            expect(positionRegistry.register).not.toHaveBeenCalled();
            expect(positionRegistry.unregister).not.toHaveBeenCalled();

            expect(onComponentsAddedCallback).not.toHaveBeenCalled();
            expect(onComponentsRemovedCallback.calls.count()).toBe(1);
            expect(onComponentsRemovedCallback.calls.argsFor(0)[0]).toEqual([
                {
                    component: parent,
                    parent: undefined
                },
                {
                    component: component1,
                    parent
                }
            ]);

            done();
        });
    });

    describe('DOM mutations', () => {
        beforeEach(() => {
            resizeListener.unregister.calls.reset();
            resizeListener.register.calls.reset();
            positionRegistry.unregister.calls.reset();
            positionRegistry.register.calls.reset();
            onComponentsAddedCallback.calls.reset();
        });

        it('should init the Mutation Observer and observe on body element with the expected configuration', () => {
            const expectedConfig = {
                attributes: true,
                attributeOldValue: true,
                childList: true,
                characterData: false,
                subtree: true
            };
            expect(mutationObserverMock.observe).toHaveBeenCalledWith(
                document.getElementsByTagName('body')[0],
                expectedConfig
            );
        });

        it('should be able to observe a page change and execute a registered page change callback', async () => {
            // GIVEN
            const pageChangedCallback = jasmine.createSpy('callback');
            smartEditContractChangeListener.onPageChanged(pageChangedCallback);
            pageChangedCallback.calls.reset();

            // WHEN
            const mutations = [
                {
                    attributeName: 'class',
                    type: 'attributes',
                    target: {
                        tagName: 'BODY'
                    }
                }
            ];
            await playMutationObserverCallback(mutations);

            // THEN
            expect(pageChangedCallback.calls.argsFor(0)[0]).toEqual(ANY_PAGE_UUID);

            expect(pageChangedCallback.calls.count()).toBe(1);
        });

        it('when a parent and a child are in the same operation (can occur), the child is NOT ignored but is process AFTER the parent', async () => {
            // WHEN
            Array.prototype.push.apply(INTERSECTIONS_MAPPING, [
                {
                    isIntersecting: true,
                    target: component21 // child before parent component2
                },
                {
                    isIntersecting: true,
                    target: component2
                },
                {
                    isIntersecting: true,
                    target: component3
                },
                {
                    isIntersecting: true,
                    target: detachedComponent
                }
            ]);

            SECOND_LEVEL_CHILDREN = [component1, component2, component3, invisibleComponent];
            const mutations = [
                {
                    type: 'childList',
                    addedNodes: [component21, component2, invisibleComponent, component3]
                }
            ];
            await playMutationObserverCallback(mutations);

            // THEN
            expect(onComponentsAddedCallback.calls.count()).toBe(3);
            expect(onComponentsAddedCallback.calls.argsFor(0)[0]).toEqual([component2]);
            expect(onComponentsAddedCallback.calls.argsFor(1)[0]).toEqual([component21]);
            expect(onComponentsAddedCallback.calls.argsFor(2)[0]).toEqual([component3]);
        });

        it('should be able to observe sub tree of smartEditComponent component added', async () => {
            // WHEN
            Array.prototype.push.apply(INTERSECTIONS_MAPPING, [
                {
                    isIntersecting: true,
                    target: component2
                },
                {
                    isIntersecting: true,
                    target: component21
                },
                {
                    isIntersecting: true,
                    target: component3
                }
            ]);

            SECOND_LEVEL_CHILDREN = [component1, component2, component3, invisibleComponent];
            const mutations = [
                {
                    type: 'childList',
                    addedNodes: [component2, component3, invisibleComponent]
                }
            ];
            await playMutationObserverCallback(mutations);

            // THEN
            expect(resizeListener.unregister.calls.count()).toEqual(0);

            expect(resizeListener.register.calls.count()).toEqual(3);
            expect(resizeListener.register.calls.argsFor(0)[0]).toBe(component2);
            expect(resizeListener.register.calls.argsFor(1)[0]).toBe(component21);
            expect(resizeListener.register.calls.argsFor(2)[0]).toBe(component3);

            expect(positionRegistry.register.calls.count()).toEqual(3);
            expect(positionRegistry.register.calls.argsFor(0)[0]).toBe(component2);
            expect(positionRegistry.register.calls.argsFor(1)[0]).toBe(component21);
            expect(positionRegistry.register.calls.argsFor(2)[0]).toBe(component3);

            expect(onComponentsAddedCallback.calls.count()).toBe(3);
            expect(onComponentsAddedCallback.calls.argsFor(0)[0]).toEqual([component2]);
            expect(onComponentsAddedCallback.calls.argsFor(1)[0]).toEqual([component21]);
            expect(onComponentsAddedCallback.calls.argsFor(2)[0]).toEqual([component3]);
        });

        it('should be able to observe sub tree of non smartEditComponent component added', async () => {
            // WHEN
            Array.prototype.push.apply(INTERSECTIONS_MAPPING, [
                {
                    isIntersecting: true,
                    target: component2
                },
                {
                    isIntersecting: true,
                    target: component21
                },
                {
                    isIntersecting: true,
                    target: component3
                }
            ]);

            SECOND_LEVEL_CHILDREN = [component1, component3, invisibleComponent];
            const mutations = [
                {
                    type: 'childList',
                    addedNodes: [component0, component3, invisibleComponent]
                }
            ];
            await playMutationObserverCallback(mutations);

            // THEN

            expect(resizeListener.unregister.calls.count()).toEqual(0);

            expect(resizeListener.register.calls.count()).toEqual(3);
            expect(resizeListener.register.calls.argsFor(0)[0]).toBe(component2);
            expect(resizeListener.register.calls.argsFor(1)[0]).toBe(component21);
            expect(resizeListener.register.calls.argsFor(2)[0]).toBe(component3);

            expect(positionRegistry.register.calls.count()).toEqual(3);
            expect(positionRegistry.register.calls.argsFor(0)[0]).toBe(component2);
            expect(positionRegistry.register.calls.argsFor(1)[0]).toBe(component21);
            expect(positionRegistry.register.calls.argsFor(2)[0]).toBe(component3);

            expect(onComponentsAddedCallback.calls.count()).toBe(3);
            expect(onComponentsAddedCallback.calls.argsFor(0)[0]).toEqual([component2]);
            expect(onComponentsAddedCallback.calls.argsFor(1)[0]).toEqual([component21]);
            expect(onComponentsAddedCallback.calls.argsFor(2)[0]).toEqual([component3]);
        });

        it('should be able to observe sub tree of smartEditComponent (and parent) removed', async () => {
            (smartEditContractChangeListener as any).componentsQueue.set(0, {
                component: component2,
                isIntersecting: true,
                processed: 'added',
                parent
            } as any);
            (smartEditContractChangeListener as any).componentsQueue.set(1, {
                component: component21,
                isIntersecting: true,
                processed: 'added',
                parent: component2
            } as any);
            (smartEditContractChangeListener as any).componentsQueue.set(2, {
                component: component3,
                isIntersecting: true,
                processed: 'added',
                parent
            } as any);

            // WHEN
            Array.prototype.push.apply(INTERSECTIONS_MAPPING, [
                {
                    isIntersecting: false,
                    target: component2
                },
                {
                    isIntersecting: false,
                    target: component21
                },
                {
                    isIntersecting: false,
                    target: component3
                }
            ]);
            SECOND_LEVEL_CHILDREN = [component1, component2, component3];

            await runIntersectionObserver(INTERSECTIONS_MAPPING);

            // THEN
            expect(resizeListener.register).not.toHaveBeenCalled();
            expect(resizeListener.unregister).not.toHaveBeenCalled();
            expect(positionRegistry.unregister).not.toHaveBeenCalled();

            expect(onComponentsRemovedCallback.calls.count()).toBe(1);
            expect(onComponentsRemovedCallback.calls.argsFor(0)[0]).toEqualData([
                {
                    component: component2,
                    parent
                },
                {
                    component: component21,
                    parent: component2
                },
                {
                    component: component3,
                    parent
                }
            ]);
        });

        it('should be able to stop all the listeners', () => {
            smartEditContractChangeListener.stopListener();

            expect(mutationObserverMock.disconnect).toHaveBeenCalled();
            expect(intersectionObserverMock.disconnect).toHaveBeenCalled();
            expect(resizeListener.dispose).toHaveBeenCalled();
            expect(positionRegistry.dispose).toHaveBeenCalled();
        });

        it('should call the componentRepositionedCallback when a component is repositioned after updating the registry', async () => {
            positionRegistry.getRepositionedComponents.and.returnValue([component1]);

            await promiseUtils.sleep(100);

            expect(onComponentRepositionedCallback.calls.count()).toBe(1);
            expect(onComponentRepositionedCallback).toHaveBeenCalledWith(component1);
        });

        it('should cancel the repositionListener interval when calling stopListener', () => {
            positionRegistry.getRepositionedComponents.and.returnValue([]);

            smartEditContractChangeListener.stopListener();

            expect((smartEditContractChangeListener as any).repositionListener).toBe(null);
        });

        it('should be able to observe a component change', async () => {
            // WHEN
            const mutations = [
                {
                    type: 'attributes',
                    attributeName: UUID_ATTRIBUTE,
                    target: component1,
                    oldValue: 'random_uuid'
                },
                {
                    type: 'attributes',
                    attributeName: ID_ATTRIBUTE,
                    target: component1,
                    oldValue: 'random_id'
                }
            ];
            await playMutationObserverCallback(mutations);

            // THEN
            const expectedOldAttributes: TypedMap<string> = {};
            expectedOldAttributes[UUID_ATTRIBUTE] = 'random_uuid';
            expectedOldAttributes[ID_ATTRIBUTE] = 'random_id';
            expect(onComponentChangedCallback.calls.argsFor(0)[0]).toEqual(
                component1,
                expectedOldAttributes
            );
        });

        it('should be able to observe a component remove', async () => {
            // GIVEN
            const mutations = [
                {
                    type: 'childList',
                    removedNodes: [component1],
                    target: parent
                }
            ];

            // WHEN
            await playMutationObserverCallback(mutations);

            // THEN
            expect(onComponentsRemovedCallback.calls.count()).toBe(1);
            expect(onComponentsRemovedCallback.calls.argsFor(0)[0]).toEqual([
                {
                    component: component1,
                    parent
                }
            ]);
        });

        it('should add a component to the components queue if it was generated from a simple div by adding smartedit attributes (change operation)', async () => {
            // GIVEN
            (smartEditContractChangeListener as any).componentsQueue = new Map<
                Element,
                ComponentEntry
            >();

            const mutations: any = [
                {
                    type: 'attributes',
                    attributeName: UUID_ATTRIBUTE,
                    target: component1,
                    oldValue: null
                }
            ];

            // WHEN
            await playMutationObserverCallback(mutations);

            // THEN
            const componentsInQueue = getComponents();
            expect(componentsInQueue).toContain(component1);
        });

        it('should remove component from the queue if smartedit attributes were removed from it (change operation)', async () => {
            // GIVEN
            (component1 as any).hasAttribute = () => {
                return false;
            };
            const mutations: any = [
                {
                    type: 'attributes',
                    attributeName: UUID_ATTRIBUTE,
                    target: component1,
                    oldValue: null
                }
            ];
            let componentsInQueue = getComponents();
            expect(componentsInQueue).toContain(component1);

            // WHEN
            await playMutationObserverCallback(mutations);

            // THEN
            componentsInQueue = getComponents();
            expect(componentsInQueue).not.toContain(component1);
            expect(intersectionObserverMock.unobserve).toHaveBeenCalledWith(component1);
            expect(onComponentsRemovedCallback).toHaveBeenCalled();
        });
    });
});
