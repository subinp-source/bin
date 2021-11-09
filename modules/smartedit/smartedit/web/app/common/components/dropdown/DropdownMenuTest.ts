/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component } from '@angular/core';

import { DropdownMenuComponent, IDropdownMenuItem } from './dropdownMenu';
import { stringUtils } from '../../utils';
import { TypedMap } from 'smarteditcommons/dtos';

describe('DropdownMenu', () => {
    // component
    let dropDownMenuComponent: DropdownMenuComponent;

    // mocks
    let templateCache: jasmine.SpyObj<angular.ITemplateCacheService>;

    @Component({
        selector: '',
        template: ''
    })
    class MockDropdownMenuItemComponent {}

    const mockDropdownItems: TypedMap<IDropdownMenuItem[]> = {
        callbackAndTemplate: [
            {
                callback: null,
                template: null
            }
        ],
        callbackAndTemplateUrl: [
            {
                callback: null,
                templateUrl: null
            }
        ],
        templateAndTemplateUrl: [
            {
                template: null,
                templateUrl: null
            }
        ],
        componentAndTemplateUrl: [
            {
                component: null,
                templateUrl: null
            }
        ],
        callback: [
            {
                callback: () => {
                    //
                }
            }
        ],
        template: [
            {
                template: 'MOCKED_TEMPLATE'
            }
        ],
        templateUrl: [
            {
                templateUrl: 'MOCKED_TEMPLATE_URL'
            }
        ],
        component: [
            {
                component: MockDropdownMenuItemComponent
            }
        ]
    };

    const mockedEncodedString = 'mockedEncodedString';
    const mockedEncodedTemplateUrl = 'yDropdownItem_' + mockedEncodedString + '_Template.html';

    beforeEach(() => {
        spyOn(stringUtils, 'getEncodedString').and.returnValue(mockedEncodedString);

        templateCache = jasmine.createSpyObj('templateCache', ['get', 'put']);
        templateCache.get.and.returnValue('<div></div>');

        dropDownMenuComponent = new DropdownMenuComponent(templateCache, null);
    });

    describe('ngOnChanges', () => {
        describe('throws an error if more than one type of template is provided', () => {
            const errorMessage =
                'DropdownMenuComponent.validateDropdownItem - Dropdown Item must contain at least a callback, template, templateUrl or component';
            it('throws for callback and template', () => {
                dropDownMenuComponent.dropdownItems = mockDropdownItems.callbackAndTemplate;
                expect(() => {
                    dropDownMenuComponent.ngOnChanges({
                        dropdownItems: {} as any
                    });
                }).toThrow(new Error(errorMessage));
            });

            it('throws for callback and templateUrl', () => {
                dropDownMenuComponent.dropdownItems = mockDropdownItems.callbackAndTemplateUrl;
                expect(() => {
                    dropDownMenuComponent.ngOnChanges({
                        dropdownItems: {} as any
                    });
                }).toThrow(new Error(errorMessage));
            });

            it('throws for template and templateUrl', () => {
                dropDownMenuComponent.dropdownItems = mockDropdownItems.templateAndTemplateUrl;
                expect(() => {
                    dropDownMenuComponent.ngOnChanges({
                        dropdownItems: {} as any
                    });
                }).toThrow(new Error(errorMessage));
            });

            it('throws for component and templateUrl', () => {
                dropDownMenuComponent.dropdownItems = mockDropdownItems.componentAndTemplateUrl;
                expect(() => {
                    dropDownMenuComponent.ngOnChanges({
                        dropdownItems: {} as any
                    });
                }).toThrow(new Error(errorMessage));
            });
        });

        describe('sets component or templateUrl on clonedDropdownItems', () => {
            it('GIVEN callback WHEN ngOnChanges is called THEN default component will be set', () => {
                // Given
                dropDownMenuComponent.dropdownItems = mockDropdownItems.callback;

                // When
                dropDownMenuComponent.ngOnChanges({
                    dropdownItems: {} as any
                });

                // Assert
                expect(dropDownMenuComponent.clonedDropdownItems[0].component).toBeDefined();
            });

            it('GIVEN template WHEN ngOnChanges is called THEN templateUrl will be set', () => {
                // GIVEN
                dropDownMenuComponent.dropdownItems = mockDropdownItems.template;

                // WHEN
                dropDownMenuComponent.ngOnChanges({
                    dropdownItems: {} as any
                });

                // THEN
                expect(dropDownMenuComponent.clonedDropdownItems[0].templateUrl).toBe(
                    mockedEncodedTemplateUrl
                );
                expect(dropDownMenuComponent.clonedDropdownItems[0].template).toBe(null);
            });

            it('GVEN templateUrl WHEN ngOnChanges is called THEN templateUrl property should not be affected', () => {
                // GIVEN
                dropDownMenuComponent.dropdownItems = mockDropdownItems.templateUrl;

                // WHEN
                dropDownMenuComponent.ngOnChanges({
                    dropdownItems: {} as any
                });

                // THEN
                expect(dropDownMenuComponent.clonedDropdownItems[0].templateUrl).toBe(
                    'MOCKED_TEMPLATE_URL'
                );
            });

            it('GIVEN component WHEN ngOnChanges is called THEN component property should not be affected', () => {
                // GIVEN
                dropDownMenuComponent.dropdownItems = mockDropdownItems.component;

                // WHEN
                dropDownMenuComponent.ngOnChanges({
                    dropdownItems: {} as any
                });

                // THEN
                expect(dropDownMenuComponent.clonedDropdownItems[0].component).toBe(
                    MockDropdownMenuItemComponent
                );
            });
        });
    });
});
