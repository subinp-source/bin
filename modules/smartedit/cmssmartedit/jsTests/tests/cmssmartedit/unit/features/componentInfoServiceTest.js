/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('componentInfoService', function() {
    var harness;
    var $q;
    var $rootScope;
    var componentInfoService;
    var mockCmsitemsRestService;
    var crossFrameEventService;
    var systemEventService;
    var yjQuery;

    var COMPONENT1 = {
        id: 1,
        uuid: 'uuid-001',
        attr: function() {
            return 'uuid-001';
        }
    };
    var COMPONENT2 = {
        id: 2,
        uuid: 'uuid-002',
        attr: function() {
            return 'uuid-002';
        }
    };
    var MOCK_COMPONENTS = [COMPONENT1, COMPONENT2];

    var COMPONENTS_DATA = [
        {
            uuid: COMPONENT1.uuid
        },
        {
            uuid: COMPONENT2.uuid
        }
    ];

    beforeEach(function() {
        harness = AngularUnitTestHelper.prepareModule('componentInfoServiceModule')
            .mock('cmsitemsRestService', 'getById')
            .mock('cmsitemsRestService', 'getByIds')
            .mock('crossFrameEventService', 'subscribe')
            .mockConstant('OVERLAY_RERENDERED_EVENT', 'OVERLAY_RERENDERED_EVENT')
            .mockConstant('COMPONENT_CREATED_EVENT', 'COMPONENT_CREATED_EVENT')
            .mockConstant('COMPONENT_UPDATED_EVENT', 'COMPONENT_UPDATED_EVENT')
            .mockConstant('COMPONENT_REMOVED_EVENT', 'COMPONENT_REMOVED_EVENT')
            .mockConstant('UUID_ATTRIBUTE', 'data-smartedit-component-uuid')
            .mockConstant('EVENTS', {
                PAGE_CHANGE: 'PAGE_CHANGE',
                USER_HAS_CHANGED: 'USER_HAS_CHANGED'
            })
            .service('componentInfoService');

        $q = harness.injected.$q;
        $rootScope = harness.injected.$rootScope;
        yjQuery = harness.injected.yjQuery;

        componentInfoService = harness.service;
        crossFrameEventService = harness.mocks.crossFrameEventService;
        systemEventService = harness.mocks.systemEventService;

        mockCmsitemsRestService = harness.mocks.cmsitemsRestService;
        mockCmsitemsRestService.getByIds.and.callFake(function() {
            return $q.when({
                response: COMPONENTS_DATA
            });
        });

        spyOn(yjQuery, 'attr').and.callFake(function(param) {
            return param.attr();
        });
    });

    it('WHEN components are added it should call _getComponentsDataByUUIDs', function() {
        spyOn(componentInfoService, '_getComponentsDataByUUIDs');

        componentInfoService._onComponentsAddedToOverlay(MOCK_COMPONENTS);

        expect(componentInfoService._getComponentsDataByUUIDs).toHaveBeenCalledWith([
            COMPONENT1.uuid,
            COMPONENT2.uuid
        ]);
    });

    it('WHEN components are added THEN getById should return the component data', function() {
        componentInfoService._onComponentsAddedToOverlay(MOCK_COMPONENTS);
        $rootScope.$digest();

        expect(componentInfoService.getById(COMPONENT1.uuid)).toBeResolvedWithData(
            getExpectedComponentData(COMPONENT1.uuid)
        );
    });

    it('WHEN getById is called and the component data is not cached, it should resolve when the component data is ready', function() {
        componentInfoService._getComponentsDataByUUIDs([COMPONENT2.uuid]);
        $rootScope.$digest();
        expect(componentInfoService.getById(COMPONENT2.uuid)).toBeResolvedWithData(
            getExpectedComponentData(COMPONENT2.uuid)
        );
    });

    it('WHEN getById is called and the component data fetch failed, it should reject the getById promise', function(done) {
        mockCmsitemsRestService.getById.and.callFake(function() {
            return $q.reject({
                message: 'error while retrieving cmsitems'
            });
        });

        componentInfoService.getById(COMPONENT2.uuid).then(
            function() {},
            function(e) {
                expect(e.message).toEqual('error while retrieving cmsitems');
                done();
            }
        );
        componentInfoService._getComponentsDataByUUIDs([COMPONENT2.uuid]);
        $rootScope.$digest();
    });

    it('GIVEN value is cached WHEN getById is called THEN it should be retrieved from the cache', function() {
        // GIVEN
        componentInfoService._forceAddComponent(COMPONENT1);

        // WHEN
        var promise = componentInfoService.getById(COMPONENT1.uuid);

        // THEN
        promise.then(function(componentRetrieved) {
            expect(componentRetrieved).toBe(COMPONENT1);
            expect(mockCmsitemsRestService.getById.calls.count()).toBe(0);
        });
        $rootScope.$digest();
    });

    it('WHEN getById is called with the forceImmediateResult enabled THEN it should query the backend right away AND return the component data', function() {
        // WHEN

        mockCmsitemsRestService.getById.and.callFake(function(uuid) {
            if (uuid === COMPONENT1.uuid) {
                return $q.when(COMPONENT1);
            }
        });

        // THEN
        expect(componentInfoService.getById(COMPONENT1.uuid, true)).toBeResolvedWithData(
            COMPONENT1
        );
        expect(mockCmsitemsRestService.getById.calls.count()).toBe(1);
    });

    it('WHEN getById is called with the forceImmediateResult enabled AND the call fails THEN it should reject the promise', function() {
        // GIVEN
        mockCmsitemsRestService.getById.and.callFake(function() {
            return $q.reject({
                message: 'error while retrieving cmsitems'
            });
        });

        // WHEN
        var promise = componentInfoService.getById(COMPONENT1.uuid, true);

        // THEN
        promise.then(
            function() {},
            function(e) {
                expect(e.message).toEqual('error while retrieving cmsitems');
            }
        );
        $rootScope.$digest();
    });

    it('GIVEN value is cached WHEN getById is called with the forceImmediateResult enabled THEN it should be retrieved from the cache', function() {
        // GIVEN
        componentInfoService._forceAddComponent(COMPONENT1);

        // WHEN
        var promise = componentInfoService.getById(COMPONENT1.uuid, true);

        // THEN
        promise.then(function(componentRetrieved) {
            expect(componentRetrieved).toBe(COMPONENT1);
            expect(mockCmsitemsRestService.getById.calls.count()).toBe(0);
        });
        $rootScope.$digest();
    });

    it('WHEN component is added to page THEN the cache must be updated', function() {
        mockCmsitemsRestService.getById.and.callFake(function(uuid) {
            if (uuid === COMPONENT1.uuid) {
                return $q.when(COMPONENT1);
            }
        });
        var promise = componentInfoService.getById(COMPONENT1.uuid);

        // THEN
        expect(promise).toBeResolvedWithData(COMPONENT1);
    });

    it('WHEN component is removed by force THEN it must be removed from the cache', function() {
        // GIVEN
        var componentToRemove = {
            uuid: COMPONENT1.uuid,
            slots: ['some other uuid']
        };
        componentInfoService._forceAddComponent(componentToRemove);

        // WHEN
        componentInfoService._forceRemoveComponent(componentToRemove);

        // THEN
        expect(componentInfoService._isComponentCached(COMPONENT1.uuid)).toBeFalsy();
    });

    it('should subscribe on PAGE_CHANGE event', function() {
        expect(crossFrameEventService.subscribe).toHaveBeenCalledWith(
            'PAGE_CHANGE',
            jasmine.any(Function)
        );
    });

    it('should subscribe on OVERLAY_RERENDERED_EVENT event', function() {
        expect(crossFrameEventService.subscribe).toHaveBeenCalledWith(
            'OVERLAY_RERENDERED_EVENT',
            jasmine.any(Function)
        );
    });

    it('should subscribe on USER_HAS_CHANGED event', function() {
        expect(crossFrameEventService.subscribe).toHaveBeenCalledWith(
            'USER_HAS_CHANGED',
            jasmine.any(Function)
        );
    });

    it('should subscribe to COMPONENT_CREATED_EVENT', function() {
        expect(crossFrameEventService.subscribe).toHaveBeenCalledWith(
            'COMPONENT_CREATED_EVENT',
            jasmine.any(Function)
        );
    });

    it('should subscribe to COMPONENT_UPDATED_EVENT', function() {
        expect(crossFrameEventService.subscribe).toHaveBeenCalledWith(
            'COMPONENT_UPDATED_EVENT',
            jasmine.any(Function)
        );
    });

    it('should subscribe to COMPONENT_REMOVED_EVENT', function() {
        expect(crossFrameEventService.subscribe).toHaveBeenCalledWith(
            'COMPONENT_REMOVED_EVENT',
            jasmine.any(Function)
        );
    });

    function getExpectedComponentData(componentUuid) {
        return COMPONENTS_DATA.filter(function(component) {
            return component.uuid === componentUuid;
        })[0];
    }
});
