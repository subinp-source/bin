/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* tslint:disable:max-classes-per-file */

import { Injectable } from '@angular/core';
import { UpgradeModule } from '@angular/upgrade/static';
import {
    AbstractAngularJSBasedCustomElement,
    CustomElementConstructor,
    WindowUtils
} from 'smarteditcommons/utils';
import { GenericEditorFieldComponentScope } from '../components/GenericEditorFieldComponent';

/*
 * Custom Web Element class Factory aimed at hiding behing a framework agnostic <se-template-ge-widget> tag
 * the legacy AngularJS / ng-include / template based implementations of old Generic editor custom widgets.
 *
 */
function genericEditorAngularJSTemplateBasedCustomElementClassFactory(
    upgrade: UpgradeModule
): CustomElementConstructor {
    return class extends AbstractAngularJSBasedCustomElement {
        private angularJSBody = `
            <div
                class="ySEGenericEditorField ySEGenericEditorField__input-page-name--color"
                data-ng-include="field.template"
            ></div>
        `;

        constructor() {
            super(upgrade);
        }

        internalConnectedCallback(): void {
            const parent =
                this.parentElement.closest(`se-generic-editor-field`) ||
                (this.parentElement.closest(`generic-editor-field`) as any);

            if (!parent || !parent.field) {
                return;
            }

            this.markAsProcessed();

            const {
                editor,
                model,
                field,
                qualifier,
                id,
                editorStackId,
                isFieldDisabled,
                $ctrl
            }: GenericEditorFieldComponentScope = parent;

            while (this.firstChild) {
                this.removeChild(this.firstChild);
            }

            this.appendChild(window.smarteditJQuery(this.angularJSBody)[0]);

            this.scope = this.$rootScope.$new(false);

            const scope: Partial<GenericEditorFieldComponentScope> = (this
                .scope as unknown) as Partial<GenericEditorFieldComponentScope>;

            scope.editor = editor;
            scope.model = model;
            scope.field = field;
            scope.qualifier = qualifier;
            scope.id = id;
            scope.editorStackId = editorStackId;
            scope.isFieldDisabled = isFieldDisabled;
            scope.$ctrl = $ctrl;

            this.$compile(this)(this.scope)[0];
        }
    };
}

@Injectable()
export class LegacyGEWidgetToCustomElementConverter {
    static readonly TEMPLATE_WIDGET_NAME = `se-template-ge-widget`;

    constructor(private upgrade: UpgradeModule, private windowUtils: WindowUtils) {}

    /*
     * must only happen in the container, and only once, where generic editor is eligible.
     * If it happens first in smarteditloader or smartedit then the injection of those
     * will be used and the app will miss dependencies and template cache.
     */
    convert(): void {
        if (
            !this.windowUtils.isIframe() &&
            !customElements.get(LegacyGEWidgetToCustomElementConverter.TEMPLATE_WIDGET_NAME)
        ) {
            const CustomComponentClass = genericEditorAngularJSTemplateBasedCustomElementClassFactory(
                this.upgrade
            );
            customElements.define(
                LegacyGEWidgetToCustomElementConverter.TEMPLATE_WIDGET_NAME,
                CustomComponentClass
            );
        }
    }
}
