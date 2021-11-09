/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
    moduleUtils,
    IContextualMenuService,
    IDecoratorService,
    SeDowngradeComponent,
    SeEntryModule
} from 'smarteditcommons';

// tslint:disable:max-classes-per-file

const smarteditroot = 'web/webroot';

@SeDowngradeComponent()
@Component({
    selector: 'dummy',
    template: `
        <div>
            ..... Dummy Template
            <img src="/static-resources/images/slot_contextualmenu_placeholder_off.png" /> .....
        </div>
    `
})
export class DummyComponent {}

@SeEntryModule('Innerapp')
@NgModule({
    imports: [CommonModule],
    declarations: [DummyComponent],
    entryComponents: [DummyComponent],
    providers: [
        moduleUtils.bootstrap(
            (
                contextualMenuService: IContextualMenuService,
                decoratorService: IDecoratorService
            ) => {
                const addContextualMenuItem = (key: string) => {
                    contextualMenuService.addItems({
                        '^.*Slot$': [
                            {
                                key: 'slot.context.menu.title' + key,
                                displayClass: 'editbutton se-ctx-menu-element__btn',
                                i18nKey: 'slot.context.menu.title.' + key,
                                iconIdle:
                                    smarteditroot +
                                    '/static-resources/images/slot_contextualmenu_placeholder_off.png',
                                iconNonIdle:
                                    smarteditroot +
                                    '/static-resources/images/slot_contextualmenu_placeholder_on.png',

                                action: {
                                    callback() {
                                        addContextualMenuItem('dummy5');
                                    }
                                }
                            }
                        ]
                    });
                };

                decoratorService.addMappings({
                    '^.*Slot$': ['se.slotContextualMenu']
                });

                decoratorService.enable('se.slotContextualMenu');

                contextualMenuService.addItems({
                    '^.*Slot$': [
                        {
                            key: 'slot.context.menu.title.dummy1',
                            displayClass: 'editbutton se-ctx-menu-element__btn',
                            i18nKey: 'slot.context.menu.title.dummy1',
                            iconIdle:
                                smarteditroot +
                                '/static-resources/images/slot_contextualmenu_placeholder_off.png',
                            iconNonIdle:
                                smarteditroot +
                                '/static-resources/images/slot_contextualmenu_placeholder_on.png',

                            action: {
                                callback() {
                                    addContextualMenuItem('dummy3');
                                }
                            }
                        },
                        {
                            key: 'slot.context.menu.title.dummy2',
                            displayClass: 'editbutton se-ctx-menu-element__btn',
                            i18nKey: 'slot.context.menu.title.dummy2',
                            iconIdle:
                                smarteditroot +
                                '/static-resources/images/slot_contextualmenu_placeholder_off.png',
                            iconNonIdle:
                                smarteditroot +
                                '/static-resources/images/slot_contextualmenu_placeholder_on.png',

                            action: {
                                callback() {
                                    addContextualMenuItem('dummy4');
                                }
                            }
                        },
                        {
                            key: 'slot.context.menu.title.dummy6',
                            action: {
                                component: DummyComponent,
                                callback() {
                                    addContextualMenuItem('dummy6');
                                }
                            }
                        }
                    ]
                });
            },
            [IContextualMenuService, IDecoratorService]
        )
    ]
})
export class Innerapp {}
