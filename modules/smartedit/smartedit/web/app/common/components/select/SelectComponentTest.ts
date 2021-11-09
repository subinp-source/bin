/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
// tslint:disable:max-classes-per-file
import { Component } from '@angular/core';
import { CdkDragDrop } from '@angular/cdk/drag-drop';

import { SelectComponent } from './SelectComponent';
import { LogService } from '../../services';

describe('SelectComponent Test', () => {
    @Component({
        selector: 'mock-results-header'
    })
    class MockResultsHeaderComponent {}

    const mockFetchAllStrategy = () => Promise.resolve([]);
    const mockFetchPageStrategy = () => Promise.resolve({} as any);

    const logService = jasmine.createSpyObj<LogService>('logService', ['debug']);
    const createComponent = () => new SelectComponent(null, logService, null);

    let cmp: SelectComponent<any>;
    beforeEach(() => {
        cmp = createComponent();
    });

    it('GIVEN "onChange" WHEN drag & drop of selected option has changed its sequence THEN "onChange" function should be called', () => {
        cmp.selected = ['0', '1'];
        cmp.onChange = jasmine.createSpy('onChange');

        const mockDragDropEvent = {
            currentIndex: 1,
            previousIndex: 0
        } as CdkDragDrop<string[]>;
        cmp.onDrop(mockDragDropEvent);

        expect(cmp.onChange).toHaveBeenCalled();
    });

    describe('isValidConfiguration', () => {
        describe('Results Header', () => {
            const expectedError =
                'Only one of "resultsHeaderComponent" or "resultsHeaderLabel" must be specified';
            it('should throw error for invalid custom Results Header Template', () => {
                cmp.resultsHeaderComponent = MockResultsHeaderComponent;
                cmp.resultsHeaderLabel = 'label';
                expect(() => {
                    (cmp as any).isValidConfiguration();
                }).toThrowError(expectedError);

                cmp.resultsHeaderComponent = MockResultsHeaderComponent;
                cmp.resultsHeaderLabel = undefined;
                expect(() => {
                    (cmp as any).isValidConfiguration();
                }).not.toThrowError(expectedError);

                cmp.resultsHeaderComponent = undefined;
                cmp.resultsHeaderLabel = 'label';
                expect(() => {
                    (cmp as any).isValidConfiguration();
                }).not.toThrowError(expectedError);
            });
        });

        describe('Fetch Strategy', () => {
            it('should throw error when none of the Fetch Strategy are specified', () => {
                const expectedError =
                    'Neither fetchAll nor fetchPage have been specified in fetchStrategy';
                cmp.fetchStrategy = {};
                expect(() => {
                    (cmp as any).isValidConfiguration();
                }).toThrowError(expectedError);
            });

            it('should throw error when none of the Fetch Strategy are specified', () => {
                const expectedError =
                    'Only one of either fetchAll or fetchPage must be specified in fetchStrategy';

                cmp.fetchStrategy = {
                    fetchAll: mockFetchAllStrategy,
                    fetchPage: mockFetchPageStrategy
                };
                expect(() => {
                    (cmp as any).isValidConfiguration();
                }).toThrowError(expectedError);
            });

            it('should throw error when Fetch Page is specified without Fetch Entity nor Fetch Entities', () => {
                cmp.model = '1';
                const expectedError = `fetchPage has been specified in fetchStrategy but neither fetchEntity nor fetchEntities are available to load item identified by ${
                    cmp.model
                }`;

                cmp.fetchStrategy = {
                    fetchPage: mockFetchPageStrategy
                };
                expect(() => {
                    (cmp as any).isValidConfiguration();
                }).toThrowError(expectedError);
            });

            it('should log a warning message for Paged mode when "keepModelOnReset" is set to false', () => {
                cmp.fetchStrategy = {
                    fetchPage: mockFetchPageStrategy
                };

                (cmp as any).isValidConfiguration();

                expect(logService.debug).toHaveBeenCalledTimes(1);
            });
        });
    });
});
