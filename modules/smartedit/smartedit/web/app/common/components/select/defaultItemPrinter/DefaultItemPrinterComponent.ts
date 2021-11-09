/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ChangeDetectionStrategy, Component, Inject } from '@angular/core';

import { ItemComponentData, ITEM_COMPONENT_DATA_TOKEN } from '../itemPrinter';

/**
 * Component represents the default Item of Select Component.
 * Displayed by Item Printer Component.
 *
 * @internal
 */
@Component({
    selector: 'se-default-item-printer',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './DefaultItemPrinterComponent.html'
})
export class DefaultItemPrinterComponent {
    constructor(@Inject(ITEM_COMPONENT_DATA_TOKEN) public data: ItemComponentData) {}
}
