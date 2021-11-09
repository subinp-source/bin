/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';

import {
    moduleUtils,
    IContextualMenuButton,
    IContextualMenuConfiguration,
    IContextualMenuService,
    IDecoratorService,
    L10nPipeModule,
    L10nService,
    SeCustomComponent,
    SeDowngradeComponent,
    SeEntryModule,
    SeTranslationModule
} from 'smarteditcommons';

// tslint:disable:max-classes-per-file

@SeDowngradeComponent()
@Component({
    selector: 'dummy',
    template: `
        <div>{{ dummyText | seL10n | async }}</div>
    `
})
export class DummyComponent {
    dummyText = {
        en: 'dummyText in english',
        fr: 'dummyText in french'
    };
}

@SeCustomComponent()
@Component({
    selector: 'dummy-angular',
    template: `
        <div>{{ 'dummyKey2' | translate }}</div>
    `
})
export class Dummy2Component {
    value: Observable<string>;
}

document.body.append(document.createElement('dummy-angular'));

@SeEntryModule('FakeModule')
@NgModule({
    imports: [CommonModule, SeTranslationModule.forChild(), L10nPipeModule],
    declarations: [DummyComponent, Dummy2Component],
    entryComponents: [DummyComponent, Dummy2Component],
    providers: [
        moduleUtils.bootstrap(
            (
                decoratorService: IDecoratorService,
                contextualMenuService: IContextualMenuService,
                l10nService: L10nService
            ) => {
                decoratorService.addMappings({
                    '^((?!Slot).)*$': ['contextualMenu']
                });

                decoratorService.enable('contextualMenu');

                contextualMenuService.addItems({
                    componentType1: [
                        {
                            key: 'templateString2',
                            i18nKey: 'TEMPLATEURL',
                            condition(configuration: IContextualMenuConfiguration) {
                                (configuration.element as any).addClass('conditionClass1');
                                return true;
                            },
                            action: {
                                template: '<dummy></dummy>'
                            },
                            displayClass: 'editbutton',
                            iconIdle: '../contextualMenu/icons/contextualmenu_edit_off.png',
                            iconNonIdle:
                                '../test/e2e/contextualMenu/icons/contextualmenu_edit_on.png'
                        } as IContextualMenuButton
                    ]
                });

                return l10nService.resolveLanguage();
            },
            [IDecoratorService, IContextualMenuService, L10nService]
        )
    ]
})
export class FakeModule {}
