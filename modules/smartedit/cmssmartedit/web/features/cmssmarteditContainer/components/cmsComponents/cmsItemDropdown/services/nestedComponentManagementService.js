/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('nestedComponentManagementServiceModule', [
        'yLoDashModule',
        'cmsSmarteditServicesModule'
    ])
    .service('nestedComponentManagementService', function(lodash, editorModalService) {
        // ------------------------------------------------------------------------
        // Public API
        // ------------------------------------------------------------------------
        this.openNestedComponentEditor = function(componentInfo, editorStackId, saveCallback) {
            var componentData = prepareComponentData(componentInfo);
            return editorModalService.open(
                componentData,
                null,
                null,
                null,
                saveCallback,
                editorStackId
            );
        };

        // ------------------------------------------------------------------------
        // Helper Methods
        // ------------------------------------------------------------------------
        var prepareComponentData = function(componentInfo) {
            return {
                smarteditComponentUuid: componentInfo.componentUuid,
                smarteditComponentType: componentInfo.componentType,
                content: lodash.defaultsDeep({}, componentInfo.content, {
                    typeCode: componentInfo.componentType,
                    itemtype: componentInfo.componentType,
                    visible: true
                })
            };
        };
    });
