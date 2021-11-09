/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('nestedComponentManagementServiceTest', function() {
    // ---------------------------------------------------------------
    // Constants
    // ---------------------------------------------------------------
    var EXCEPTED_RESULT = 'some result';
    var STACK_ID = 'some stack id';

    // ---------------------------------------------------------------
    // Variables
    // ---------------------------------------------------------------
    var nestedComponentManagementService, mocks;

    // ---------------------------------------------------------------
    // Set Up
    // ---------------------------------------------------------------
    beforeEach(function() {
        var harness = AngularUnitTestHelper.prepareModule('nestedComponentManagementServiceModule')
            .mock('editorModalService', 'open')
            .and.returnValue(EXCEPTED_RESULT)
            .service('nestedComponentManagementService');

        nestedComponentManagementService = harness.service;
        mocks = harness.mocks;
    });

    // ---------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------
    it('WHEN openNestedComponentEditor is called with visibility as false THEN the editor is properly opened', function() {
        // GIVEN
        var COMPONENT_TYPE = 'Some Component Type';
        var COMPONENT_UUID = 'some component uuid';
        var CATALOG_VERSION = 'some catalog version uuid';

        var componentInfo = {
            componentType: COMPONENT_TYPE,
            componentUuid: COMPONENT_UUID,
            content: {
                visible: false,
                catalogVersion: CATALOG_VERSION
            }
        };

        var expectedComponentData = {
            smarteditComponentUuid: COMPONENT_UUID,
            smarteditComponentType: COMPONENT_TYPE,
            content: {
                typeCode: COMPONENT_TYPE,
                itemtype: COMPONENT_TYPE,
                catalogVersion: CATALOG_VERSION,
                visible: false
            }
        };

        // WHEN
        var result = nestedComponentManagementService.openNestedComponentEditor(
            componentInfo,
            STACK_ID
        );

        // THEN
        expect(result).toBe(EXCEPTED_RESULT);
        expect(mocks.editorModalService.open).toHaveBeenCalledWith(
            expectedComponentData,
            null,
            null,
            null,
            undefined,
            STACK_ID
        );
    });

    it('WHEN openNestedComponentEditor is called THEN the editor is properly opened with the default values', function() {
        // GIVEN
        var COMPONENT_TYPE = 'Some Component Type';
        var COMPONENT_UUID = 'some component uuid';
        var CATALOG_VERSION = 'some catalog version uuid';

        var componentInfo = {
            componentType: COMPONENT_TYPE,
            componentUuid: COMPONENT_UUID,
            content: {
                catalogVersion: CATALOG_VERSION
            }
        };

        var expectedComponentData = {
            smarteditComponentUuid: COMPONENT_UUID,
            smarteditComponentType: COMPONENT_TYPE,
            content: {
                typeCode: COMPONENT_TYPE,
                itemtype: COMPONENT_TYPE,
                catalogVersion: CATALOG_VERSION,
                visible: true
            }
        };

        // WHEN
        var result = nestedComponentManagementService.openNestedComponentEditor(
            componentInfo,
            STACK_ID
        );

        // THEN
        expect(result).toBe(EXCEPTED_RESULT);
        expect(mocks.editorModalService.open).toHaveBeenCalledWith(
            expectedComponentData,
            null,
            null,
            null,
            undefined,
            STACK_ID
        );
    });
});
