/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { Injector } from '@angular/core';
import { UpgradeModule } from '@angular/upgrade/static';
import {
    promiseUtils,
    stringUtils,
    GenericEditorField,
    IGenericEditor,
    LegacyGEWidgetToCustomElementConverter,
    TypedMap,
    WindowUtils
} from 'smarteditcommons';
import { GenericEditorFieldComponentScope } from '../components/GenericEditorFieldComponent';

/*
 * Semi integration test:
 * pure black box testing of real custom elements
 * but injected with mocks of AngularJS $compile and $rootscope
 */
describe('LegacyGEWidgetToCustomElementConverter test', () => {
    let $compile: jasmine.Spy;
    let $rootScope: jasmine.SpyObj<angular.IRootScopeService>;
    let upgrade: jasmine.SpyObj<UpgradeModule>;
    let injector: jasmine.SpyObj<Injector>;
    const windowUtils = jasmine.createSpyObj<WindowUtils>('windowUtils', ['isIframe']);

    let converter: LegacyGEWidgetToCustomElementConverter;

    let genericEditorField: HTMLElement & Partial<GenericEditorFieldComponentScope>;
    // parent of a se-template-ge-widget that has a generic-editor-field as ancestor
    let expectedParent: HTMLElement;
    // parent of a se-template-ge-widget that does not have a generic-editor-field as ancestor
    let unexpectedParent: HTMLElement;

    const templateGeWidget = document.createElement('se-template-ge-widget');

    const editor = jasmine.createSpyObj<IGenericEditor>('editor', ['submit']);
    const model: TypedMap<any> = {};
    const field = jasmine.createSpyObj<GenericEditorField>('field', ['options']);
    const qualifier = 'thequalifier';
    const id = 'theid';
    const editorStackId = 'theeditorStackId';
    const isFieldDisabled = () => false;

    const newScope: jasmine.SpyObj<
        angular.IScope & Partial<GenericEditorFieldComponentScope>
    > = jasmine.createSpyObj<angular.IScope>('newScope', ['$destroy']);

    beforeEach(() => {
        templateGeWidget.appendChild(document.createElement('noise'));

        upgrade = jasmine.createSpyObj<UpgradeModule>('UpgradeModule', ['bootstrap']);
        injector = jasmine.createSpyObj<Injector>('Injector', ['get']);
        (upgrade as any).injector = injector;

        $rootScope = jasmine.createSpyObj<angular.IRootScopeService>('$rootScope', ['$new']);
        $rootScope.$new.and.returnValue(newScope);

        $compile = jasmine.createSpy('$compile');
        $compile.and.callFake((element: HTMLElement) => {
            if (element === templateGeWidget) {
                return ($scope: angular.IScope) => {
                    if ($scope === newScope) {
                        element.appendChild(document.createElement('sometemplatecontent'));
                        return [element];
                    } else {
                        throw new Error(
                            `unexpected $scope passed to $compile: ${JSON.stringify($scope)}`
                        );
                    }
                };
            } else {
                throw new Error(`unexpected node passed to $compile: ${element.tagName}`);
            }
        });

        const mocksMap = {
            $compile,
            $rootScope
        } as TypedMap<any>;

        injector.get.and.callFake((name: string) => {
            return mocksMap[name];
        });

        windowUtils.isIframe.and.returnValue(false);

        converter = new LegacyGEWidgetToCustomElementConverter(upgrade, windowUtils);

        /*
         * custom element will only be declared once for all tests while the mock instances
         * are different each time, so test of scope and templateGeWidget in $compile must assess on deep equality
         * OR reuse same mock instances across all tests instead (and reset mocks)
         * OR use a beforeAll instead of beforeEach (and reset mocks)
         */
        converter.convert();

        genericEditorField = document.createElement('generic-editor-field');
        document.body.appendChild(genericEditorField);
        const child1 = document.createElement('child1');
        genericEditorField.appendChild(child1);
        expectedParent = document.createElement('expectedParent');
        genericEditorField.appendChild(expectedParent);

        unexpectedParent = document.createElement('unexpectedParent');
        document.body.appendChild(unexpectedParent);

        genericEditorField.editor = editor;
        genericEditorField.model = model;
        genericEditorField.field = field;
        genericEditorField.qualifier = qualifier;
        genericEditorField.id = id;
        genericEditorField.editorStackId = editorStackId;
        genericEditorField.isFieldDisabled = isFieldDisabled;
    });

    afterEach(() => {
        newScope.$destroy.calls.reset();
        templateGeWidget.removeAttribute('processed');
        while (templateGeWidget.firstChild) {
            templateGeWidget.removeChild(templateGeWidget.firstChild);
        }
        while (document.body.firstChild) {
            document.body.removeChild(document.body.firstChild);
        }
    });

    it(`se-template-ge-widget child of an unexpected parent does not compile`, () => {
        unexpectedParent.appendChild(templateGeWidget);

        expect(stringUtils.formatHTML(templateGeWidget.outerHTML)).toEqual(
            stringUtils.formatHTML('<se-template-ge-widget><noise></noise></se-template-ge-widget>')
        );
    });

    it(`se-template-ge-widget child of an expected parent 
        does compile after emptying its content and ng-include template is injected,
        scope is initialised with GenericEditorFieldComponentScope contract from parent generic-field-editor,
        AND when removed, scope is destroyed`, async () => {
        expectedParent.appendChild(templateGeWidget);

        const outerHTML = await promiseUtils.resolveToCallbackWhenCondition(
            () => !!templateGeWidget.querySelector('sometemplatecontent'),
            () => templateGeWidget.outerHTML
        );

        expect(stringUtils.formatHTML(outerHTML)).toEqual(
            stringUtils.formatHTML(
                `<se-template-ge-widget processed="true">
                    <div class="ySEGenericEditorField ySEGenericEditorField__input-page-name--color" data-ng-include="field.template"></div>
                    <sometemplatecontent></sometemplatecontent>
                </se-template-ge-widget>`
            )
        );

        expect(newScope.editor).toBe(editor);
        expect(newScope.model).toBe(model);
        expect(newScope.field).toBe(field);
        expect(newScope.qualifier).toBe(qualifier);
        expect(newScope.id).toBe(id);
        expect(newScope.editorStackId).toBe(editorStackId);
        expect(newScope.isFieldDisabled).toBe(isFieldDisabled);

        expectedParent.removeChild(templateGeWidget);

        expect(newScope.$destroy).toHaveBeenCalled();
    });
});
