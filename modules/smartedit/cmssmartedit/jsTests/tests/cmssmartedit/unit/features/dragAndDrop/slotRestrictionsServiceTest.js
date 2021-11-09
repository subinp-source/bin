/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
describe('slotRestrictionsService', function() {
    var fixture;
    var slotRestrictionsService;
    var $rootScope;
    var mockComponentHandlerService;
    var mockPageInfoService;
    var mockRestServiceFactory;
    var mockSlotsRestrictionsRestService;
    var mockPageContentSlotsComponentsRestService;
    var mockTypePermissionsRestService;
    var crossFrameEventService;
    var pageChangeListener;
    var MOCK_SLOTS_RESTRICTIONS;
    var MOCK_PAGE_UID;
    var COMPONENT_IN_SLOT_STATUS = {
        ALLOWED: 'allowed',
        DISALLOWED: 'disallowed',
        MAYBEALLOWED: 'mayBeAllowed'
    };

    beforeEach(angular.mock.module('functionsModule'));

    beforeEach(function() {
        fixture = AngularUnitTestHelper.prepareModule('slotRestrictionsServiceModule')
            .mockConstant(
                'CONTENT_SLOTS_TYPE_RESTRICTION_RESOURCE_URI',
                'CONTENT_SLOTS_TYPE_RESTRICTION_RESOURCE_URI'
            )
            .mockConstant('CONTENT_SLOTS_TYPE_RESTRICTION_FETCH_LIMIT', 2)
            .mockConstant('COMPONENT_IN_SLOT_STATUS', COMPONENT_IN_SLOT_STATUS)
            .mockConstant('CONTENT_SLOT_TYPE', 'ContentSlot')
            .mock('gatewayProxy', 'initForService')
            .mock('pageInfoService', 'getPageUID')
            .mock('componentHandlerService', 'isExternalComponent')
            .mock('componentHandlerService', 'getAllSlotUids')
            .mock('slotSharedService', 'isSlotShared')
            .mock('slotSharedService', 'areSharedSlotsDisabled')
            .mock('restServiceFactory', 'get')
            .mock('crossFrameEventService', 'subscribe')
            .mock('pageContentSlotsComponentsRestService', 'getComponentsForSlot')
            .mock('typePermissionsRestService', 'hasUpdatePermissionForTypes')
            .mockConstant('EVENTS', {
                PAGE_CHANGE: 'PAGE_CHANGE'
            })
            .service('slotRestrictionsService');

        slotRestrictionsService = fixture.service;
        $q = fixture.injected.$q;
        $rootScope = fixture.injected.$rootScope;
        mockRestServiceFactory = fixture.mocks.restServiceFactory;
        mockComponentHandlerService = fixture.mocks.componentHandlerService;
        mockPageInfoService = fixture.mocks.pageInfoService;
        mockTypePermissionsRestService = fixture.mocks.typePermissionsRestService;
        mockPageContentSlotsComponentsRestService =
            fixture.mocks.pageContentSlotsComponentsRestService;

        crossFrameEventService = fixture.mocks.crossFrameEventService;

        expect(crossFrameEventService.subscribe).toHaveBeenCalledWith(
            'PAGE_CHANGE',
            jasmine.any(Function)
        );
        pageChangeListener = crossFrameEventService.subscribe.calls.argsFor(0)[1];

        spyOn(slotRestrictionsService, '_emptyCache').and.callThrough();
    });

    beforeEach(function() {
        mockSlotsRestrictionsRestService = jasmine.createSpyObj(
            'mockSlotsRestrictionsRestService',
            ['save']
        );
        mockRestServiceFactory.get.and.returnValue(mockSlotsRestrictionsRestService);
    });

    beforeEach(function() {
        MOCK_PAGE_UID = 'SomePageUID';
        MOCK_SLOTS_RESTRICTIONS = [
            {
                contentSlotUid: 'SomeSlotUID',
                validComponentTypes: [
                    'SomeComponentType1',
                    'SomeComponentType2',
                    'SomeComponentType3'
                ]
            },
            {
                contentSlotUid: 'SomeOtherSlotUID',
                validComponentTypes: ['SomeComponentType1', 'SomeComponentType2']
            },
            {
                contentSlotUid: 'SomeAnotherSlotUID',
                validComponentTypes: ['SomeComponentType1']
            }
        ];
    });

    describe('getSlotRestrictions', function() {
        it('initialises with gatewayProxy', function() {
            expect(fixture.mocks.gatewayProxy.initForService).toHaveBeenCalledWith(
                slotRestrictionsService,
                ['getAllComponentTypesSupportedOnPage', 'getSlotRestrictions'],
                'SLOT_RESTRICTIONS'
            );
        });

        it('should cache the page ID', function(done) {
            // Arrange
            mockPageInfoService.getPageUID.and.returnValue(Promise.resolve(MOCK_PAGE_UID));
            mockComponentHandlerService.isExternalComponent.and.returnValue(false);

            // Act
            slotRestrictionsService.getSlotRestrictions('SomeSlotUID').then(function() {
                slotRestrictionsService.getSlotRestrictions('SomeSlotUID').then(function() {
                    // Assert
                    expect(mockPageInfoService.getPageUID.calls.count()).toBe(
                        1,
                        'Expected pageInfoService.getPageUID() to have been called only once'
                    );
                    done();
                });
            });
        });

        it('should fetch slots restrictions and cache them in-memory', function(done) {
            // Arrange
            spyOn(slotRestrictionsService, '_fetchSlotsRestrictions').and.callThrough();
            mockSlotsRestrictionsRestService.save.and.returnValue(
                Promise.resolve(MOCK_SLOTS_RESTRICTIONS)
            );
            mockPageInfoService.getPageUID.and.returnValue(Promise.resolve(MOCK_PAGE_UID));

            // Act - Before caching
            var promise = slotRestrictionsService.getSlotRestrictions('SomeSlotUID');
            fixture.detectChanges();

            // Assert - undefined
            promise.then(function(value) {
                expect(value).toBe(undefined);

                // Act
                var savePromise = slotRestrictionsService._fetchSlotsRestrictions(['SomeSlotUID']);
                fixture.detectChanges();

                savePromise.then(function() {
                    // Assert
                    expect(mockSlotsRestrictionsRestService.save).toHaveBeenCalledWith({
                        slotIds: ['SomeSlotUID'],
                        pageUid: 'SomePageUID'
                    });

                    // Act - After caching
                    promise = slotRestrictionsService.getSlotRestrictions('SomeSlotUID');
                    fixture.detectChanges();

                    // Assert - data
                    promise.then(function(value) {
                        expect(value).toEqual([
                            'SomeComponentType1',
                            'SomeComponentType2',
                            'SomeComponentType3'
                        ]);
                        done();
                    });
                });
            });
        });

        it('should return no valid component types for external slots', function(done) {
            // Arrange
            mockPageInfoService.getPageUID.and.returnValue(Promise.resolve(MOCK_PAGE_UID));
            mockComponentHandlerService.isExternalComponent.and.returnValue(true);

            // Act
            slotRestrictionsService.getSlotRestrictions('SomeSlotUID').then(function(value) {
                // Assert
                expect(value).toEqual([]);
                done();
            });
        });

        it('should recursively call slot restriction REST service depending on the number of slots on the page and also on fetch limit', function(done) {
            // Arrange
            mockComponentHandlerService.getAllSlotUids.and.returnValue([
                'SomeSlotUID',
                'SomeOtherSlotUID',
                'SomeAnotherSlotUID'
            ]);
            mockPageInfoService.getPageUID.and.returnValue(Promise.resolve(MOCK_PAGE_UID));
            mockComponentHandlerService.isExternalComponent.and.returnValue(false);
            mockSlotsRestrictionsRestService.save.and.callFake(function(obj) {
                if (obj.slotIds.length === 2) {
                    return Promise.resolve([
                        {
                            contentSlotUid: 'SomeSlotUID',
                            validComponentTypes: [
                                'SomeComponentType1',
                                'SomeComponentType2',
                                'SomeComponentType3'
                            ]
                        },
                        {
                            contentSlotUid: 'SomeOtherSlotUID',
                            validComponentTypes: ['SomeComponentType1', 'SomeComponentType2']
                        }
                    ]);
                } else {
                    return Promise.resolve([
                        {
                            contentSlotUid: 'SomeAnotherSlotUID',
                            validComponentTypes: ['SomeComponentType1']
                        }
                    ]);
                }
            });

            // Act
            slotRestrictionsService._cacheSlotsRestrictions().then(function() {
                // Assert
                expect(mockSlotsRestrictionsRestService.save.calls.count()).toBe(
                    2,
                    'Expected slot restrictions save REST service to have been called twice'
                );

                expect(mockSlotsRestrictionsRestService.save).toHaveBeenCalledWith({
                    slotIds: ['SomeSlotUID', 'SomeOtherSlotUID'],
                    pageUid: 'SomePageUID'
                });
                expect(mockSlotsRestrictionsRestService.save).toHaveBeenCalledWith({
                    slotIds: ['SomeAnotherSlotUID'],
                    pageUid: 'SomePageUID'
                });
                done();
            });
        });

        it('should not retrieve slot restrictions for external slot ids when _cacheSlotsRestrictions is called with slotIds', function(done) {
            // Arrange
            mockComponentHandlerService.getAllSlotUids.and.returnValue([
                'SomeSlotUID',
                'SomeOtherSlotUID',
                'SomeAnotherSlotUID'
            ]);
            mockPageInfoService.getPageUID.and.returnValue(Promise.resolve(MOCK_PAGE_UID));
            // if all slots are external slots
            mockComponentHandlerService.isExternalComponent.and.returnValue(true);

            // Act
            slotRestrictionsService._cacheSlotsRestrictions().then(function() {
                // Assert
                expect(mockSlotsRestrictionsRestService.save).not.toHaveBeenCalled();
                done();
            });
        });
    });

    describe('isComponentAllowedInSlot', function() {
        it('should return a promise resolving to ALLOWED if the component type is allowed in the given slot AND source and target slots are the same AND the target slot already contains the component', function() {
            // Arrange
            mockPageInfoService.getPageUID.and.returnValue(Promise.resolve(MOCK_PAGE_UID));

            var slotRestriction = MOCK_SLOTS_RESTRICTIONS.find(function(slotRestriction) {
                return slotRestriction.contentSlotUid === 'SomeSlotUID';
            });

            spyOn(slotRestrictionsService, 'getSlotRestrictions').and.returnValue(
                Promise.resolve(slotRestriction.validComponentTypes)
            );

            mockPageContentSlotsComponentsRestService.getComponentsForSlot.and.returnValue(
                Promise.resolve([
                    {
                        uid: 'something'
                    }
                ])
            );
            var slot = {
                id: 'SomeSlotUID',
                components: [
                    {
                        id: 'something'
                    }
                ]
            };
            var dragInfo = {
                slotId: 'SomeSlotUID',
                componentType: 'SomeComponentType1',
                componentId: 'something'
            };

            // Act
            slotRestrictionsService.isComponentAllowedInSlot(slot, dragInfo).then(function(value) {
                // Assert
                expect(value).toBe(COMPONENT_IN_SLOT_STATUS.ALLOWED);
            });
        });

        it('should return a promise resolving to ALLOWED if the component type is allowed in the given slot AND the slot does not already contain the component', function() {
            // Arrange
            mockPageInfoService.getPageUID.and.returnValue(Promise.resolve(MOCK_PAGE_UID));

            var slotRestriction = MOCK_SLOTS_RESTRICTIONS.find(function(slotRestriction) {
                return slotRestriction.contentSlotUid === 'SomeSlotUID';
            });

            spyOn(slotRestrictionsService, 'getSlotRestrictions').and.returnValue(
                Promise.resolve(slotRestriction.validComponentTypes)
            );

            mockPageContentSlotsComponentsRestService.getComponentsForSlot.and.returnValue(
                Promise.resolve([
                    {
                        uid: 'something'
                    }
                ])
            );
            var slot = {
                id: 'SomeSlotUID',
                components: [
                    {
                        id: 'something'
                    }
                ]
            };
            var dragInfo = {
                slotId: 'SomeOtherSlotUID',
                componentType: 'SomeComponentType1',
                componentId: 'SomeComponentId1'
            };
            // Act
            slotRestrictionsService.isComponentAllowedInSlot(slot, dragInfo).then(function(value) {
                // Assert
                expect(value).toBe(COMPONENT_IN_SLOT_STATUS.ALLOWED);
            });
        });

        it('should return a promise resolving to DISALLOWED if the component type is allowed in the given slot AND source and target slots are different AND the slot already contains the component', function() {
            // Arrange
            mockPageInfoService.getPageUID.and.returnValue(Promise.resolve(MOCK_PAGE_UID));

            var slotRestriction = MOCK_SLOTS_RESTRICTIONS.find(function(slotRestriction) {
                return slotRestriction.contentSlotUid === 'SomeSlotUID';
            });

            spyOn(slotRestrictionsService, 'getSlotRestrictions').and.returnValue(
                Promise.resolve(slotRestriction.validComponentTypes)
            );

            mockPageContentSlotsComponentsRestService.getComponentsForSlot.and.returnValue(
                Promise.resolve([
                    {
                        uid: 'SomeComponentId1'
                    }
                ])
            );
            var slot = {
                id: 'SomeSlotUID',
                components: [
                    {
                        id: 'SomeComponentId1'
                    }
                ]
            };
            var dragInfo = {
                slotId: 'SomeOtherSlotUID',
                componentType: 'SomeComponentType1',
                componentId: 'SomeComponentId1'
            };
            // Act
            slotRestrictionsService.isComponentAllowedInSlot(slot, dragInfo).then(function(value) {
                // Assert
                expect(value).toBe(COMPONENT_IN_SLOT_STATUS.DISALLOWED);
            });
        });

        it('should return a promise resolving to DISALLOWED if the component type is not allowed in the given slot', function() {
            // Arrange
            mockPageInfoService.getPageUID.and.returnValue(Promise.resolve(MOCK_PAGE_UID));

            var slotRestriction = MOCK_SLOTS_RESTRICTIONS.find(function(slotRestriction) {
                return slotRestriction.contentSlotUid === 'SomeSlotUID';
            });

            spyOn(slotRestrictionsService, 'getSlotRestrictions').and.returnValue(
                Promise.resolve(slotRestriction.validComponentTypes)
            );

            mockPageContentSlotsComponentsRestService.getComponentsForSlot.and.returnValue(
                Promise.resolve([
                    {
                        uid: 'something'
                    }
                ])
            );
            var slot = {
                id: 'SomeSlotUID',
                components: [
                    {
                        id: 'something'
                    }
                ]
            };
            var dragInfo = {
                slotId: 'SomeOtherSlotUID',
                componentType: 'SomeComponentType4',
                componentId: 'SomeComponentId4'
            };

            // Act
            slotRestrictionsService.isComponentAllowedInSlot(slot, dragInfo).then(function(value) {
                // Assert
                expect(value).toBe(COMPONENT_IN_SLOT_STATUS.DISALLOWED);
            });
        });

        it('should return a promise resolving to MAYBEALLOWED if source and target slots are the same AND the target slot already contains the component and the slot restriction for a given slot is not cached', function() {
            mockPageInfoService.getPageUID.and.returnValue(Promise.resolve(MOCK_PAGE_UID));

            spyOn(slotRestrictionsService, 'getSlotRestrictions').and.returnValue(
                Promise.resolve()
            );

            mockPageContentSlotsComponentsRestService.getComponentsForSlot.and.returnValue(
                Promise.resolve([
                    {
                        uid: 'something'
                    }
                ])
            );
            var slot = {
                id: 'SomeSlotUID',
                components: [
                    {
                        id: 'something'
                    }
                ]
            };
            var dragInfo = {
                slotId: 'SomeSlotUID',
                componentType: 'SomeComponentType1',
                componentId: 'something'
            };
            // Act
            slotRestrictionsService.isComponentAllowedInSlot(slot, dragInfo).then(function(value) {
                // Assert
                expect(value).toBe(COMPONENT_IN_SLOT_STATUS.MAYBEALLOWED);
            });
        });

        it('should return a promise resolving to MAYBEALLOWED if the slot does not already contain the component and the slot restriction for a given slot is not cached', function() {
            // Arrange
            mockPageInfoService.getPageUID.and.returnValue(Promise.resolve(MOCK_PAGE_UID));

            spyOn(slotRestrictionsService, 'getSlotRestrictions').and.returnValue(
                Promise.resolve()
            );

            mockPageContentSlotsComponentsRestService.getComponentsForSlot.and.returnValue(
                Promise.resolve([
                    {
                        uid: 'something'
                    }
                ])
            );
            var slot = {
                id: 'SomeSlotUID',
                components: [
                    {
                        id: 'something'
                    }
                ]
            };
            var dragInfo = {
                slotId: 'SomeOtherSlotUID',
                componentType: 'SomeComponentType1',
                componentId: 'SomeComponentId1'
            };
            // Act
            slotRestrictionsService.isComponentAllowedInSlot(slot, dragInfo).then(function(value) {
                // Assert
                expect(value).toBe(COMPONENT_IN_SLOT_STATUS.MAYBEALLOWED);
            });
        });
    });

    describe('_emptyCache', function() {
        beforeEach(function() {
            mockPageInfoService.getPageUID.and.returnValue(Promise.resolve(MOCK_PAGE_UID));
        });

        it('when page changes _emptyCache is called', function() {
            expect(slotRestrictionsService._emptyCache).not.toHaveBeenCalled();
            pageChangeListener();
            expect(slotRestrictionsService._emptyCache).toHaveBeenCalled();
        });

        it('when page changes _cacheSlotsRestrictions is called', function() {
            spyOn(slotRestrictionsService, '_cacheSlotsRestrictions').and.callFake(angular.noop);
            expect(slotRestrictionsService._cacheSlotsRestrictions).not.toHaveBeenCalled();

            pageChangeListener();
            expect(slotRestrictionsService._cacheSlotsRestrictions).toHaveBeenCalled();
        });

        it('should invalidate the cache when _emptyCache is called', function(done) {
            // Arrange
            mockSlotsRestrictionsRestService.save.and.returnValue(
                Promise.resolve(MOCK_SLOTS_RESTRICTIONS)
            );
            mockPageInfoService.getPageUID.and.returnValue(Promise.resolve(MOCK_PAGE_UID));
            mockComponentHandlerService.isExternalComponent.and.returnValue(false);

            var cachePromise = slotRestrictionsService._fetchSlotsRestrictions(['SomeSlotUID']);
            cachePromise.then(function() {
                var getPromise = slotRestrictionsService.getSlotRestrictions('SomeSlotUID');

                // Assert - before empty cache
                getPromise.then(function(value) {
                    expect(value).toEqual([
                        'SomeComponentType1',
                        'SomeComponentType2',
                        'SomeComponentType3'
                    ]);

                    // Act
                    slotRestrictionsService._emptyCache();

                    // Assert - after empty cache
                    slotRestrictionsService
                        .getSlotRestrictions('SomeSlotUID')
                        .then(function(result) {
                            expect(result).toBe(undefined);
                            done();
                        });
                });
            });
        });
    });

    describe('slotEditable - ', function() {
        var slotId = 'some slot';
        var areSharedSlotsDisabled, isSlotShared, isExternalComponent;

        beforeEach(function() {
            fixture.mocks.slotSharedService.isSlotShared.and.callFake(function() {
                return $q.when(isSlotShared);
            });

            fixture.mocks.componentHandlerService.isExternalComponent.and.callFake(function() {
                return isExternalComponent;
            });

            fixture.mocks.slotSharedService.areSharedSlotsDisabled.and.callFake(function() {
                return areSharedSlotsDisabled;
            });
        });

        describe('with CHANGE permissions ', function() {
            beforeEach(function() {
                mockTypePermissionsRestService.hasUpdatePermissionForTypes.and.callFake(function() {
                    return $q.when({
                        ContentSlot: true
                    });
                });
            });

            it('GIVEN shared slots are disabled WHEN isSlotEditable is called AND the slot is shared THEN it returns false', function() {
                // GIVEN
                areSharedSlotsDisabled = true;
                isSlotShared = true;
                isExternalComponent = false;

                // WHEN
                var promise = slotRestrictionsService.isSlotEditable(slotId);

                // THEN
                promise.then(function(result) {
                    expect(result).toBe(false);
                });
                $rootScope.$digest();
            });

            it('GIVEN shared slots are disabled WHEN isSlotEditable is called AND the slot is not shared THEN it returns true', function() {
                // GIVEN
                areSharedSlotsDisabled = true;
                isSlotShared = false;
                isExternalComponent = false;

                // WHEN
                var promise = slotRestrictionsService.isSlotEditable(slotId);

                // THEN
                promise.then(function(result) {
                    expect(result).toBe(true);
                });
                $rootScope.$digest();
            });

            it('GIVEN shared slots are enabled WHEN isSlotEditable is called AND the slot is shared THEN it returns true', function() {
                // GIVEN
                areSharedSlotsDisabled = false;
                isSlotShared = true;
                isExternalComponent = false;

                // WHEN
                var promise = slotRestrictionsService.isSlotEditable(slotId);

                // THEN
                promise.then(function(result) {
                    expect(result).toBe(true);
                });
                $rootScope.$digest();
            });

            it('GIVEN shared slots are enabled WHEN isSlotEditable is called AND the slot is external THEN it returns false', function() {
                // GIVEN
                areSharedSlotsDisabled = false;
                isSlotShared = true;
                isExternalComponent = true;

                // WHEN
                var promise = slotRestrictionsService.isSlotEditable(slotId);

                // THEN
                promise.then(function(result) {
                    expect(result).toBe(false);
                });
                $rootScope.$digest();
            });
        });

        describe('without CHANGE permissions ', function() {
            it('GIVEN slot without CHANGE permission WHEN isSlotEditable is called THEN it returns false', function() {
                // GIVEN
                mockTypePermissionsRestService.hasUpdatePermissionForTypes.and.callFake(function() {
                    return $q.when({
                        ContentSlot: false
                    });
                });

                // WHEN
                var promise = slotRestrictionsService.isSlotEditable(slotId);

                // THEN
                promise.then(function(result) {
                    expect(result).toBe(false);
                });
                $rootScope.$digest();
            });
        });
    });
});
