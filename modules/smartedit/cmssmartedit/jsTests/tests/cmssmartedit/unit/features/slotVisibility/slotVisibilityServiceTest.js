/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('slotVisibilityService', function() {
    // Service under test
    var slotVisibilityService;

    // Mock dependencies
    var pageContentSlotsComponentsRestService;
    var componentHandlerService;
    var pageInfoService;
    var crossFrameEventService;

    // Util
    var $q;
    var $rootScope;

    var PAGE_UID = 'homepage';

    beforeEach(function() {
        var harness = AngularUnitTestHelper.prepareModule('slotVisibilityServiceModule')
            .mock('renderService', 'renderSlots')
            .and.returnValue()
            .mock('componentHandlerService', 'getId')
            .mock('componentHandlerService', 'getOriginalComponentsWithinSlot')
            .mock('pageContentSlotsComponentsRestService', 'clearCache')
            .mock('pageContentSlotsComponentsRestService', 'getSlotsToComponentsMapForPageUid')
            .mock('pageInfoService', 'getPageUID')
            .mock('crossFrameEventService', 'subscribe')
            .mockConstant('EVENTS', {
                PAGE_CHANGE: 'PAGE_CHANGE'
            })
            .mockConstant('COMPONENT_CREATED_EVENT', 'COMPONENT_CREATED_EVENT')
            .mockConstant('COMPONENT_UPDATED_EVENT', 'COMPONENT_UPDATED_EVENT')
            .mockConstant('COMPONENT_REMOVED_EVENT', 'COMPONENT_REMOVED_EVENT')
            .service('slotVisibilityService');

        $q = harness.injected.$q;
        $rootScope = harness.injected.$rootScope;

        slotVisibilityService = harness.service;
        componentHandlerService = harness.mocks.componentHandlerService;
        pageInfoService = harness.mocks.pageInfoService;
        pageContentSlotsComponentsRestService = harness.mocks.pageContentSlotsComponentsRestService;
        crossFrameEventService = harness.mocks.crossFrameEventService;

        pageInfoService.getPageUID.and.returnValue($q.when(PAGE_UID));
    });

    describe('reloadSlotsInfo', function() {
        it('WHEN reloadSlotsInfo is called, it will clear cache and reload SlotsToComponentsMap', function() {
            slotVisibilityService.reloadSlotsInfo();
            $rootScope.$digest();

            expect(pageContentSlotsComponentsRestService.clearCache).toHaveBeenCalled();
            expect(
                pageContentSlotsComponentsRestService.getSlotsToComponentsMapForPageUid
            ).toHaveBeenCalledWith(PAGE_UID);
        });
    });

    describe('content slots per page is empty', function() {
        it('should have an empty hidden component list.', function() {
            var SLOT = 'some-slot-id';
            pageContentSlotsComponentsRestService.getSlotsToComponentsMapForPageUid.and.returnValue(
                $q.when('some_data')
            );

            var promise = slotVisibilityService.getHiddenComponents(SLOT);
            $rootScope.$digest();

            expect(promise).toBeResolvedWithData([]);
        });
    });

    describe('content slots per page is not empty', function() {
        var SLOT1 = 'some-slot-id-1';

        var COMPONENT1 = {
            visible: false,
            uid: 10
        };
        var COMPONENT2 = {
            visible: true,
            uid: 20
        };
        var COMPONENT3 = {
            visible: true,
            uid: 30
        };

        beforeEach(function() {
            pageContentSlotsComponentsRestService.getSlotsToComponentsMapForPageUid.and.returnValue(
                $q.when({
                    'some-slot-id-1': [COMPONENT1, COMPONENT2],
                    'some-slot-id-2': [COMPONENT3]
                })
            );

            var yjQueryObject = {
                get: function() {
                    return [
                        {
                            visible: true,
                            uuid: 20
                        }
                    ];
                }
            };

            componentHandlerService.getOriginalComponentsWithinSlot.and.returnValue(yjQueryObject);
            componentHandlerService.getId.and.returnValue(20);
        });

        it('should return a non-empty the hidden component list', function() {
            var promise = slotVisibilityService.getHiddenComponents(SLOT1);
            $rootScope.$digest();

            expect(promise).toBeResolvedWithData([COMPONENT1]);
        });

        it('should subscribe to COMPONENT_CREATED_EVENT and clear component cache on trigger', function() {
            expect(crossFrameEventService.subscribe).toHaveBeenCalledWith(
                'COMPONENT_CREATED_EVENT',
                jasmine.any(Function)
            );

            var componentEventListener = crossFrameEventService.subscribe.calls.argsFor(0)[1];
            componentEventListener();

            expect(pageContentSlotsComponentsRestService.clearCache).toHaveBeenCalled();
        });

        it('should subscribe to COMPONENT_UPDATED_EVENT and clear component cache on trigger', function() {
            expect(crossFrameEventService.subscribe).toHaveBeenCalledWith(
                'COMPONENT_UPDATED_EVENT',
                jasmine.any(Function)
            );

            var componentEventListener = crossFrameEventService.subscribe.calls.argsFor(1)[1];
            componentEventListener();

            expect(pageContentSlotsComponentsRestService.clearCache).toHaveBeenCalled();
        });

        it('should subscribe to COMPONENT_REMOVED_EVENT and clear component cache on trigger', function() {
            expect(crossFrameEventService.subscribe).toHaveBeenCalledWith(
                'COMPONENT_REMOVED_EVENT',
                jasmine.any(Function)
            );

            var componentEventListener = crossFrameEventService.subscribe.calls.argsFor(2)[1];
            componentEventListener();

            expect(pageContentSlotsComponentsRestService.clearCache).toHaveBeenCalled();
        });
    });
});
