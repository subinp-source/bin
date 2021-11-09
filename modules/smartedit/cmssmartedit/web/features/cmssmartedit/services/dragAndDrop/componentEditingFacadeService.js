/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc overview
 * @name componentEditingFacadeModule
 * @description
 * # The componentEditingFacadeModule
 *
 * The componentEditingFacadeModule contains a service with methods that allow adding or removing components in the page.
 *
 */
angular
    .module('componentEditingFacadeModule', [
        'alertServiceModule',
        'componentVisibilityAlertServiceModule',
        'cmsSmarteditServicesModule',
        'smarteditServicesModule',
        'slotVisibilityServiceModule',
        'translationServiceModule',
        'functionsModule'
    ])
    .constant('COMPONENT_CREATED_EVENT', 'COMPONENT_CREATED_EVENT')
    .constant('COMPONENT_UPDATED_EVENT', 'COMPONENT_UPDATED_EVENT')
    /**
     * @ngdoc service
     * @name componentEditingFacadeModule.service:componentEditingFacade
     *
     * @description
     * This service provides methods that allow adding or removing components in the page.
     */
    .service('componentEditingFacade', function(
        $log,
        $q,
        $translate,
        copy,
        componentService,
        systemEventService,
        crossFrameEventService,
        pageInfoService,
        restServiceFactory,
        editorModalService,
        renderService,
        alertService,
        PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI,
        componentVisibilityAlertService,
        slotVisibilityService,
        sharedDataService,
        COMPONENT_CREATED_EVENT,
        COMPONENT_UPDATED_EVENT,
        EVENT_SMARTEDIT_COMPONENT_UPDATED
    ) {
        var _contentSlotComponentsRestService;

        function _generateAndAlertSuccessMessage(sourceComponentId, targetSlotId) {
            alertService.showSuccess({
                message: 'se.cms.draganddrop.success',
                messagePlaceholders: {
                    sourceComponentId: sourceComponentId,
                    targetSlotId: targetSlotId
                }
            });
        }

        function _generateAndAlertErrorMessage(
            sourceComponentId,
            targetSlotId,
            requestResponse,
            alertConf
        ) {
            if (
                requestResponse &&
                requestResponse.error &&
                requestResponse.error.errors &&
                requestResponse.error.errors.length > 0
            ) {
                alertService.showDanger({
                    message: 'se.cms.draganddrop.error',
                    messagePlaceholders: {
                        sourceComponentId: sourceComponentId,
                        targetSlotId: targetSlotId,
                        detailedError: requestResponse.error.errors[0].message
                    }
                });
            } else if (alertConf) {
                alertService.showDanger(alertConf);
            }
        }

        function _renderSlots(slots, sourceComponentId, targetSlotId, showSuccess) {
            return renderService.renderSlots(slots).then(
                function() {
                    return slotVisibilityService.reloadSlotsInfo().then(
                        function() {
                            if (showSuccess) {
                                _generateAndAlertSuccessMessage(sourceComponentId, targetSlotId);
                            }
                            return $q.when();
                        },
                        function(e) {
                            $log.error(
                                'componentEditingFacadeModule._renderSlots::slotVisibilityService.reloadSlotsInfo'
                            );
                            $log.error(e);
                            return $q.reject(e);
                        }
                    );
                },
                function(e) {
                    $log.error(
                        'componentEditingFacadeModule._renderSlots::renderService.renderSlots - targetSlotId:',
                        targetSlotId
                    );
                    $log.error(e);
                    _generateAndAlertErrorMessage(sourceComponentId, targetSlotId, e);
                    return $q.reject(e);
                }
            );
        }

        /**
         * @ngdoc method
         * @name componentEditingFacadeModule.service:componentEditingFacade#addNewComponentToSlot
         * @methodOf componentEditingFacadeModule.service:componentEditingFacade
         *
         * @description
         * This methods adds a new component to the slot and opens a component modal to edit its properties.
         *
         * @param {Object} slotInfo The target slot for the new component
         * @param {Object} slotInfo.targetSlotId The Uid of the slot where to drop the new component.
         * @param {Object} slotInfo.targetSlotUUId The UUid of the slot where to drop the new component.
         * @param {String} catalogVersionUuid The catalog version on which to create the new component
         * @param {String} componentType The type of the new component to add.
         * @param {Number} position The position in the slot where to add the new component.
         *
         */
        this.addNewComponentToSlot = function(
            slotInfo,
            catalogVersionUuid,
            componentType,
            position
        ) {
            var componentAttributes = {
                smarteditComponentType: componentType,
                catalogVersionUuid: catalogVersionUuid
            };
            return editorModalService
                .open(componentAttributes, slotInfo.targetSlotUUId, position)
                .then(function(response) {
                    componentVisibilityAlertService.checkAndAlertOnComponentVisibility({
                        itemId: response.uuid,
                        itemType: response.itemtype,
                        catalogVersion: response.catalogVersion,
                        restricted: response.restricted,
                        slotId: slotInfo.targetSlotId,
                        visible: response.visible
                    });

                    crossFrameEventService.publish(COMPONENT_CREATED_EVENT, response);
                    return _renderSlots(
                        [slotInfo.targetSlotId],
                        response.uid,
                        slotInfo.targetSlotId,
                        true
                    );
                });
        };

        /**
         * @ngdoc method
         * @name componentEditingFacadeModule.service:componentEditingFacade#addExistingComponentToSlot
         * @methodOf componentEditingFacadeModule.service:componentEditingFacade
         *
         * @description
         * This methods adds an existing component to the slot and display an Alert whenever the component is either hidden or restricted.
         *
         * @param {String} targetSlotId The ID of the slot where to drop the component.
         * @param {Object} dragInfo The dragInfo object containing the componentId, componentUuid and componentType.
         * @param {Number} position The position in the slot where to add the component.
         *
         */
        this.addExistingComponentToSlot = function(targetSlotId, dragInfo, position) {
            return pageInfoService.getPageUID().then(function(pageId) {
                return componentService
                    .addExistingComponent(pageId, dragInfo.componentId, targetSlotId, position)
                    .then(
                        function() {
                            return componentService.loadComponentItem(dragInfo.componentUuid).then(
                                function(item) {
                                    componentVisibilityAlertService.checkAndAlertOnComponentVisibility(
                                        {
                                            itemId: dragInfo.componentUuid,
                                            itemType: dragInfo.componentType,
                                            catalogVersion: item.catalogVersion,
                                            restricted: item.restricted,
                                            slotId: targetSlotId,
                                            visible: item.visible
                                        }
                                    );

                                    // 1. First update the cache.
                                    systemEventService.publish(COMPONENT_UPDATED_EVENT, item);

                                    // 2. Then replay decorators (via EVENT_SMARTEDIT_COMPONENT_UPDATED).
                                    // This is important because there might be existing instances of the component in the page that need to
                                    // be updated. For example, if the component was not shared, it would not show the SharedComponent contextual button.
                                    // However, if a user adds another instance into the page then the component becomes shared. Both instances of the
                                    // component must show that the component is shared now. Thus, the first instance needs to be updated too.
                                    crossFrameEventService.publish(
                                        EVENT_SMARTEDIT_COMPONENT_UPDATED,
                                        {
                                            componentId: dragInfo.componentId,
                                            componentType: dragInfo.componentType,
                                            componentUuid: dragInfo.componentUuid,
                                            requiresReplayingDecorators: true
                                        }
                                    );

                                    return _renderSlots(
                                        targetSlotId,
                                        dragInfo.componentId,
                                        targetSlotId,
                                        true
                                    );
                                },
                                function(response) {
                                    _generateAndAlertErrorMessage(
                                        dragInfo.componentId,
                                        targetSlotId,
                                        response
                                    );
                                    return $q.reject();
                                }
                            );
                        },
                        function(response) {
                            _generateAndAlertErrorMessage(
                                dragInfo.componentId,
                                targetSlotId,
                                response
                            );
                            return $q.reject();
                        }
                    );
            });
        };

        /**
         * @ngdoc method
         * @name componentEditingFacadeModule.service:componentEditingFacade#cloneExistingComponentToSlot
         * @methodOf componentEditingFacadeModule.service:componentEditingFacade
         *
         * @description
         * This methods clones an existing component to the slot by opening a component modal to edit its properties.
         *
         * @param {Object} componentProperties The properties of the component required to create a clone
         * @param {String} componentProperties.targetSlotId The ID of the slot where to drop the component.
         * @param {Object} componentProperties.dragInfo The dragInfo object containing the componentId, componentUuid and componentType.
         * @param {Number} componentProperties.position The position in the slot where to add the component.
         *
         */
        this.cloneExistingComponentToSlot = function(componentProperties) {
            return componentService
                .loadComponentItem(componentProperties.dragInfo.componentUuid)
                .then(
                    function(_component) {
                        return sharedDataService.get('experience').then(function(experience) {
                            var component = copy(_component);
                            // while cloning an existing components, remove some parameters, reset catalogVersion to the version of the page.
                            // if cloning an existing component, prefix na me and drop restrictions - doing this here will make generic editor dirty and enable save by default
                            component.componentUuid = component.uuid;
                            component.cloneComponent = true;
                            component.catalogVersion = experience.pageContext.catalogVersionUuid;
                            component.name =
                                $translate.instant('se.cms.component.name.clone.of.prefix') +
                                ' ' +
                                component.name;

                            delete component.uuid;
                            delete component.uid;
                            delete component.slots;
                            delete component.restrictions;
                            delete component.creationtime;
                            delete component.modifiedtime;

                            var componentAttributes = {
                                smarteditComponentType: componentProperties.dragInfo.componentType,
                                catalogVersionUuid: experience.pageContext.catalogVersionUuid,
                                content: copy(component),
                                initialDirty: true
                            };
                            return editorModalService
                                .open(
                                    componentAttributes,
                                    componentProperties.targetSlotId,
                                    componentProperties.position
                                )
                                .then(function(item) {
                                    componentVisibilityAlertService.checkAndAlertOnComponentVisibility(
                                        {
                                            itemId: item.uuid,
                                            itemType: item.itemtype,
                                            catalogVersion: item.catalogVersion,
                                            restricted: item.restricted,
                                            slotId: componentProperties.targetSlotId,
                                            visible: item.visible
                                        }
                                    );

                                    crossFrameEventService.publish(COMPONENT_CREATED_EVENT, item);
                                    return _renderSlots(
                                        componentProperties.targetSlotId,
                                        item.uid,
                                        componentProperties.targetSlotId,
                                        true
                                    );
                                });
                        });
                    },
                    function(response) {
                        _generateAndAlertErrorMessage(
                            componentProperties.dragInfo.componentId,
                            componentProperties.targetSlotId,
                            response
                        );
                        return $q.reject();
                    }
                );
        };

        /**
         * @ngdoc method
         * @name componentEditingFacadeModule.service:componentEditingFacade#moveComponent
         * @methodOf componentEditingFacadeModule.service:componentEditingFacade
         *
         * @description
         * This methods moves a component from two slots in a page.
         *
         * @param {String} sourceSlotId The ID of the slot where the component is initially located.
         * @param {String} targetSlotId The ID of the slot where to drop the component.
         * @param {String} componentId The ID of the component to add into the slot.
         * @param {Number} position The position in the slot where to add the component.
         *
         */
        this.moveComponent = function(sourceSlotId, targetSlotId, componentId, position) {
            var contentSlotComponentsResourceLocation =
                PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI +
                '/pages/:pageId/contentslots/:currentSlotId/components/:componentId';
            _contentSlotComponentsRestService =
                _contentSlotComponentsRestService ||
                restServiceFactory.get(contentSlotComponentsResourceLocation, 'componentId');

            return pageInfoService.getPageUID().then(function(pageId) {
                return _contentSlotComponentsRestService
                    .update({
                        pageId: pageId,
                        currentSlotId: sourceSlotId,
                        componentId: componentId,
                        slotId: targetSlotId,
                        position: position
                    })
                    .then(
                        function() {
                            return _renderSlots(
                                [sourceSlotId, targetSlotId],
                                componentId,
                                targetSlotId
                            );
                        },
                        function(response) {
                            _generateAndAlertErrorMessage(componentId, targetSlotId, response, {
                                message: 'se.cms.draganddrop.move.failed',
                                messagePlaceholders: {
                                    slotID: targetSlotId,
                                    componentID: componentId
                                }
                            });
                            return $q.reject();
                        }
                    );
            });
        };
    });
