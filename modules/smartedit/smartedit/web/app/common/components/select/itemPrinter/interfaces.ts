/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { InjectionToken } from '@angular/core';

import { SelectItem } from '../interfaces';
import { SelectComponent } from '../SelectComponent';

export interface ItemComponentData<T extends SelectItem = any> {
    item: T;
    selected: boolean;
    select: SelectComponent<T>;
}

export const ITEM_COMPONENT_DATA_TOKEN = new InjectionToken<ItemComponentData>(
    'ITEM_COMPONENT_DATA'
);
