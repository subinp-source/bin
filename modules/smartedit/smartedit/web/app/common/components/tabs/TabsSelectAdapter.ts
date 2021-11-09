/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ISelectAdapter, ISelectItem } from '@smart/utils';
import { Tab } from './TabsComponent';

export class TabsSelectAdapter implements ISelectAdapter {
    static transform(item: Tab, id: number): ISelectItem<Tab> {
        return {
            id,
            label: item.title,
            value: item,
            listItemClassName: item.hasErrors && 'sm-tab-error'
        };
    }
}
