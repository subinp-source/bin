/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
describe('restrictionsEditor', function() {
    var scope, element, uiSelect;
    var template =
        '<restrictions-editor data-editable="editable" data-item="item" data-restrictions="restrictions" data-on-criteria-selected="onCriteriaSelected"></restrictions-editor>';

    var MOCK_RESTRICITON_1 = {
        uid: 'SampleUid',
        name: 'Sample Name',
        typeCode: 'SampleRestriction',
        typeName: {
            en: 'Sample Restriction'
        },
        description: 'Sample Description'
    };

    var MOCK_RESTRICTION_2 = {
        uid: 'SampleUid2',
        name: 'Sample Name 2',
        typeCode: 'SampleRestriction2',
        typeName: {
            en: 'Sample Restriction 2'
        },
        description: 'Sample Description 2'
    };

    beforeEach(function() {
        var harness = AngularUnitTestHelper.prepareModule('restrictionsEditorModule')
            .withTranslations({
                'se.cms.restrictions.criteria': 'Criteria:',
                'se.cms.restrictions.criteria.all': 'Match all',
                'se.cms.restrictions.criteria.any': 'Match any',
                'se.cms.restrictions.editor.button.add.new': 'Create New',
                'se.cms.restrictions.list.clear.all': 'Clear All',
                'page.restrictions.list.empty': 'This page has no restrictions'
            })
            .mock('restrictionsCriteriaService', 'getRestrictionCriteriaOptions')
            .and.returnValue([
                {
                    id: 'all',
                    label: 'se.cms.restrictions.criteria.all',
                    editLabel: 'all',
                    value: false
                },
                {
                    id: 'any',
                    label: 'se.cms.restrictions.criteria.any',
                    editLabel: 'any',
                    value: true
                }
            ])
            .mock('GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT')
            .mock('GENERIC_EDITOR_LOADED_EVENT')
            .mock('ITEM_MANAGEMENT_EDITOR_ID')
            .mock('systemEventService')
            .mock('restrictionPickerConfig')
            .mock('contextAwareEditableItemService', 'isItemEditable')
            .and.returnValue(
                new Promise(function(resolve, reject) {
                    resolve(false);
                })
            )
            .mock('isBlank')
            .component(template, {
                editable: false,
                onCriteriaSelected: jasmine.createSpy('onCriteriaSelected'),
                restrictions: [MOCK_RESTRICITON_1],
                item: {
                    onlyOneRestrictionMustApply: true
                }
            });

        element = harness.element;
        uiSelect = new UiSelectPageObject(element);
        scope = harness.scope;
    });

    describe('restrictions list', function() {
        it('should render exactly one restrictions', function() {
            expect(element.find('.se-restriction__item').length).toBe(1);
        });

        it('should render the name, type, and description of each restriction', function() {
            expect(
                element
                    .find('#restriction-1 .se-restriction__item-name')
                    .text()
                    .trim()
            ).toBe('Sample Name');
            expect(
                element.find('#restriction-1 .se-restriction__item-type-and-id').text()
            ).toContain('SampleRestriction');
            expect(
                element
                    .find('#restriction-1 .se-restriction__item-description')
                    .text()
                    .trim()
            ).toBe('Sample Description');
        });
    });

    describe('no restrictions', function() {
        beforeEach(function() {
            scope.restrictions = [];
            scope.$digest();
        });

        it('should not render the restrictions list', function() {
            expect(element.find('.ySERestrictionsListContainer').length).toBe(0);
        });
    });

    describe('read-only criteria', function() {
        beforeEach(function() {
            scope.restrictions = [MOCK_RESTRICITON_1, MOCK_RESTRICTION_2];
            scope.criteria = {};
            scope.$digest();
        });

        it('should render [Match all] criteria by default', function() {
            scope.criteria.label = 'se.cms.restrictions.criteria.all';
            scope.$digest();

            expect(element.find('.se-restriction-criteria').text()).toContain(
                'Criteria: Match all'
            );
        });
    });

    describe('editable criteria', function() {
        beforeEach(function() {
            scope.restrictions = [MOCK_RESTRICITON_1, MOCK_RESTRICTION_2];
            scope.editable = true;
            scope.$digest();
        });

        it('should render a selectable criteria list', function() {
            uiSelect.getSelectToggle().click();
            expect(
                uiSelect
                    .getSelectedElement()
                    .text()
                    .trim()
            ).toContain('all');
            expect(
                uiSelect
                    .getSelectElement(0)
                    .text()
                    .trim()
            ).toBe('all');
            expect(
                uiSelect
                    .getSelectElement(1)
                    .text()
                    .trim()
            ).toBe('any');
        });

        it('should update the selected criteria on select of "all"', function() {
            uiSelect.clickSelectToggle();
            uiSelect.clickSelectElement(0);
            expect(
                uiSelect
                    .getSelectedElement()
                    .text()
                    .trim()
            ).toContain('all');
        });

        it('should update the selected criteria on select of "any"', function() {
            uiSelect.clickSelectToggle();
            uiSelect.clickSelectElement(1);
            expect(
                uiSelect
                    .getSelectedElement()
                    .text()
                    .trim()
            ).toContain('any');
        });

        it('should reset from "any" to unselected when restrictions are removed', function() {
            // GIVEN
            uiSelect.clickSelectToggle();
            uiSelect.clickSelectElement(1);
            expect(
                uiSelect
                    .getSelectedElement()
                    .text()
                    .trim()
            ).toContain('any');
            scope.$digest();

            // WHEN
            scope.restrictions = [];
            scope.$digest();

            // THEN
            expect(uiSelect.getSelectedElement().length).toBe(0);
        });

        it('should reset to "all" when user removes restrictions, then adds at least two restrictions', function() {
            // GIVEN
            uiSelect.clickSelectToggle();
            uiSelect.clickSelectElement(1);
            expect(
                uiSelect
                    .getSelectedElement()
                    .text()
                    .trim()
            ).toContain('any');
            scope.$digest();

            // WHEN
            scope.restrictions = [];
            scope.$digest();

            scope.restrictions = [MOCK_RESTRICITON_1, MOCK_RESTRICTION_2];
            scope.$digest();

            // THEN
            uiSelect.clickSelectToggle();
            expect(
                uiSelect
                    .getSelectedElement()
                    .text()
                    .trim()
            ).toContain('all');
        });
    });

    describe('no criteria', function() {
        it('should not render criteria', function() {
            expect(element.find('.se-restriction-criteria').length).toBe(0);
        });
    });
});
