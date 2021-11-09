/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
describe('test removeComponentService class', function() {
    var restServiceFactory,
        removeComponentService,
        restServiceForRemoveComponent,
        renderService,
        sharedDataService,
        systemEventService;
    var componentInfoService;
    var COMPONENT_REMOVED_EVENT = 'some value';

    beforeEach(function() {
        window.addModulesIfNotDeclared(['smarteditServicesModule', 'legacySmarteditCommonsModule']);
    });

    beforeEach(angular.mock.module('functionsModule'));
    beforeEach(
        angular.mock.module('removeComponentServiceModule', function($provide) {
            systemEventService = jasmine.createSpyObj('systemEventService', ['publish']);

            $provide.value('systemEventService', systemEventService);
        })
    );

    beforeEach(
        angular.mock.module('resourceLocationsModule', function($provide) {
            $provide.constant('CONTEXT_CATALOG', 'CURRENT_CONTEXT_CATALOG');
            $provide.constant('CONTEXT_CATALOG_VERSION', 'CURRENT_CONTEXT_CATALOG_VERSION');
            $provide.constant('CONTEXT_SITE_ID', 'CURRENT_CONTEXT_SITE_ID');
            $provide.constant(
                'PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI',
                '/cmswebservices/v1/sites/CURRENT_CONTEXT_SITE_ID/catalogs/CURRENT_CONTEXT_CATALOG/versions/CURRENT_CONTEXT_CATALOG_VERSION/pagescontentslotscomponents'
            );
            $provide.constant('COMPONENT_REMOVED_EVENT', COMPONENT_REMOVED_EVENT);
        })
    );

    beforeEach(
        angular.mock.module('smarteditServicesModule', function($provide) {
            restServiceForRemoveComponent = jasmine.createSpyObj('restServiceForRemoveComponent', [
                'remove'
            ]);

            restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
            restServiceFactory.get.and.callFake(function() {
                return restServiceForRemoveComponent;
            });
            $provide.value('restServiceFactory', restServiceFactory);

            gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
            $provide.value('gatewayProxy', gatewayProxy);

            componentInfoService = jasmine.createSpyObj('componentInfoService', ['getById']);
            componentInfoService.getById.and.callFake(function(componentUuid, forceRetrieval) {
                var returnValue = forceRetrieval
                    ? COMPONENT_TO_REMOVE_UPDATED
                    : COMPONENT_TO_REMOVE;
                return $q.when(returnValue);
            });

            $provide.value('componentInfoService', componentInfoService);

            renderService = jasmine.createSpyObj('renderService', ['renderSlots']);
            renderService.renderSlots.and.returnValue();
            $provide.value('renderService', renderService);

            sharedDataService = jasmine.createSpyObj('sharedDataService', ['get']);
            sharedDataService.get.and.returnValue();
            $provide.value('sharedDataService', sharedDataService);
        })
    );

    beforeEach(inject(function(_$rootScope_, _$q_) {
        $rootScope = _$rootScope_;
        $q = _$q_;
    })); //includes $rootScope and $q
    beforeEach(inject(function(_removeComponentService_) {
        removeComponentService = _removeComponentService_;
    }));

    var payload = {
        slotId: 'testSlotId',
        componentId: 'testContainerId'
    };

    var COMPONENT_ID = 'testComponentId';
    var COMPONENT_UUID = 'testComponentUuid';
    var COMPONENT_TYPE = 'componentType';
    var SLOT_ID = 'testSlotId';
    var SLOT_UUID = 'testSlotUuid';
    var COMPONENT_TO_REMOVE_INFO = {
        slotId: SLOT_ID,
        slotUuid: SLOT_UUID,
        componentId: COMPONENT_ID,
        componentType: COMPONENT_TYPE,
        componentUuid: COMPONENT_UUID,
        slotOperationRelatedId: 'testContainerId',
        slotOperationRelatedType: 'testContainerType'
    };

    var COMPONENT_TO_REMOVE = {
        someProperty: 'some value',
        slots: [SLOT_UUID]
    };

    var COMPONENT_TO_REMOVE_UPDATED = {
        someProperty: 'some value',
        slots: []
    };

    it('should remove a component from a slot AND return the removed component', function() {
        // GIVEN
        restServiceForRemoveComponent.remove.and.returnValue($q.when());

        // WHEN
        var promise = removeComponentService.removeComponent(COMPONENT_TO_REMOVE_INFO);

        // THEN
        promise.then(function(component) {
            expect(restServiceForRemoveComponent.remove).toHaveBeenCalledWith(payload);
            expect(renderService.renderSlots).toHaveBeenCalledWith(SLOT_ID);
            expect(component).toBe(COMPONENT_TO_REMOVE_UPDATED);
        });
        $rootScope.$digest();
    });

    it('should not remove a component from a slot', function() {
        // GIVEN
        restServiceForRemoveComponent.remove.and.returnValue($q.reject());

        // WHEN
        removeComponentService.removeComponent(COMPONENT_TO_REMOVE_INFO);
        $rootScope.$digest();

        // THEN
        expect(restServiceForRemoveComponent.remove).toHaveBeenCalledWith(payload);
        expect(renderService.renderSlots).not.toHaveBeenCalled();
    });

    it('GIVEN component can be removed WHEN removeComponent is called THEN it must send a COMPONENT_REMOVED_EVENT event', function() {
        // GIVEN
        restServiceForRemoveComponent.remove.and.returnValue($q.when());

        // WHEN
        removeComponentService.removeComponent(COMPONENT_TO_REMOVE_INFO);
        $rootScope.$digest();

        // THEN
        expect(systemEventService.publish).toHaveBeenCalledWith(
            COMPONENT_REMOVED_EVENT,
            COMPONENT_TO_REMOVE
        );
    });

    it('GIVEN component cannot be removed WHEN removeComponent is called THEN it must not send a COMPONENT_REMOVED_EVENT event', function() {
        // GIVEN
        restServiceForRemoveComponent.remove.and.returnValue($q.reject());

        // WHEN
        removeComponentService.removeComponent(COMPONENT_TO_REMOVE_INFO);
        $rootScope.$digest();

        // THEN
        expect(systemEventService.publish).not.toHaveBeenCalled();
    });
});
