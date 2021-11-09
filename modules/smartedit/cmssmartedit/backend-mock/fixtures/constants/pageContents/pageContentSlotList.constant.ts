/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IPageContentSlot } from '../../entities/pageContents';

export const pageContentSlotList: IPageContentSlot[] = [
    {
        pageId: 'homepage',
        position: 'topHeader',
        slotId: 'topHeaderSlot',
        slotShared: false,
        slotStatus: 'PAGE'
    },
    {
        pageId: 'homepage',
        position: 'bottomHeader',
        slotId: 'bottomHeaderSlot',
        slotShared: false,
        slotStatus: 'PAGE'
    },
    {
        pageId: 'homepage',
        position: 'footer',
        slotId: 'footerSlot',
        slotShared: false,
        slotStatus: 'PAGE'
    },
    {
        pageId: 'homepage',
        position: 'other',
        slotId: 'otherSlot',
        slotShared: false,
        slotStatus: 'PAGE'
    }
];
