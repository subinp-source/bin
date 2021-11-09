/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GenericEditorTabService } from './GenericEditorTabService';
import { GenericEditorTab, GenericEditorTabConfiguration } from '../types';

describe('genericEditorTabService', () => {
    // --------------------------------------------------------------------------------------
    // Constants
    // --------------------------------------------------------------------------------------
    const TAB_ID = 'someTabId';
    const TAB_CONFIGURATION: GenericEditorTabConfiguration = {
        priority: 0
    };

    // --------------------------------------------------------------------------------------
    // Variables
    // --------------------------------------------------------------------------------------
    let genericEditorTabService: GenericEditorTabService;

    // --------------------------------------------------------------------------------------
    // Before Each
    // --------------------------------------------------------------------------------------

    beforeEach(() => {
        genericEditorTabService = new GenericEditorTabService();
    });

    // --------------------------------------------------------------------------------------
    // Tests
    // --------------------------------------------------------------------------------------
    it('WHEN a tab configuration is added THEN the service stores it', () => {
        // GIVEN
        expect(Object.keys((genericEditorTabService as any)._tabsConfiguration).length).toBe(0);

        // WHEN
        genericEditorTabService.configureTab(TAB_ID, TAB_CONFIGURATION);

        // THEN
        expect(Object.keys((genericEditorTabService as any)._tabsConfiguration).length).toBe(1);
        expect((genericEditorTabService as any)._tabsConfiguration[TAB_ID]).toBe(TAB_CONFIGURATION);
    });

    it('GIVEN the tab configuration does not exist WHEN a tab configuration is retrieved THEN the service returns null', () => {
        // GIVEN

        // WHEN
        const result = genericEditorTabService.getTabConfiguration(TAB_ID);

        // THEN
        expect(result).toBe(null);
    });

    it('GIVEN the tab configuration exists WHEN a tab configuration is retrieved THEN the service returns the config object', () => {
        // GIVEN
        genericEditorTabService.configureTab(TAB_ID, TAB_CONFIGURATION);

        // WHEN
        const result = genericEditorTabService.getTabConfiguration(TAB_ID);

        // THEN
        expect(result).toBe(TAB_CONFIGURATION);
    });

    it('WHEN a tab configuration is added AND it collides with an existing one THEN the existing one is overwritten', () => {
        // GIVEN
        genericEditorTabService.configureTab(TAB_ID, TAB_CONFIGURATION);
        expect(genericEditorTabService.getTabConfiguration(TAB_ID)).toBe(TAB_CONFIGURATION);

        const newConfiguration = {
            priority: 1
        };

        // WHEN
        genericEditorTabService.configureTab(TAB_ID, newConfiguration);
        const result = genericEditorTabService.getTabConfiguration(TAB_ID);

        // THEN
        expect(result).toBe(newConfiguration);
    });

    describe('Tab sorting -', () => {
        const TAB_ID_1 = 'ABC';
        const TAB_ID_2 = 'DEF';
        const TAB_ID_3 = 'GHI';
        const TAB_ID_4 = 'JKL';

        let tabsList: GenericEditorTab[];

        beforeEach(() => {
            tabsList = [
                {
                    id: TAB_ID_2,
                    title: 'title'
                },
                {
                    id: TAB_ID_4,
                    title: 'title'
                },
                {
                    id: TAB_ID_3,
                    title: 'title'
                },
                {
                    id: TAB_ID_1,
                    title: 'title'
                }
            ];
        });

        it('GIVEN no priority has been given to any tab THEN the tabs must be sorted alphabetically', () => {
            // GIVEN

            // WHEN
            genericEditorTabService.sortTabs(tabsList);

            // THEN
            expect(tabsList[0].id).toBe(TAB_ID_1);
            expect(tabsList[1].id).toBe(TAB_ID_2);
            expect(tabsList[2].id).toBe(TAB_ID_3);
            expect(tabsList[3].id).toBe(TAB_ID_4);
        });

        it('GIVEN some tabs have been given priority THEN the tabs must be sorted by priority first AND then aphabetically', () => {
            // GIVEN
            genericEditorTabService.configureTab(TAB_ID_3, {
                priority: 10
            });
            genericEditorTabService.configureTab(TAB_ID_4, {
                priority: 12
            });

            // WHEN
            genericEditorTabService.sortTabs(tabsList);

            // THEN
            expect(tabsList[0].id).toBe(TAB_ID_4);
            expect(tabsList[1].id).toBe(TAB_ID_3);
            expect(tabsList[2].id).toBe(TAB_ID_1);
            expect(tabsList[3].id).toBe(TAB_ID_2);
        });
    });
});
