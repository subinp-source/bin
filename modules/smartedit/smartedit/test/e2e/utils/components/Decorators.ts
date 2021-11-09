/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { by, ElementFinder } from 'protractor';

import { Storefront } from './Storefront';

export namespace Decorators {
    export const Elements = {
        async renderDecorator(componentId: string): Promise<ElementFinder> {
            const el = await Storefront.Elements.getComponentInOverlayById(componentId);
            return el.element(by.id(componentId + '-render-button-inner'));
        },
        async renderSlotDecorator(componentId: string): Promise<ElementFinder> {
            const el = await Storefront.Elements.getComponentInOverlayById(componentId);
            return el.element(by.id(componentId + '-render-slot-button-inner'));
        },
        async dirtyContentDecorator(componentId: string): Promise<ElementFinder> {
            const el = await Storefront.Elements.getComponentInOverlayById(componentId);
            return el.element(by.id(componentId + '-dirty-content-button'));
        }
    };
}
