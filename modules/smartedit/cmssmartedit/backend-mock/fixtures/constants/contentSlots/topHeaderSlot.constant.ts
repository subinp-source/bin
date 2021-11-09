/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IContentSlot } from '../../entities/contentSlots';

export const topHeaderSlot: IContentSlot = {
    contentSlotName: 'topHeaderSlot',
    contentSlotUid: 'topHeaderSlot',
    validComponentTypes: [
        'componentType1',
        'componentType2',
        'componentType3',
        'CMSParagraphComponent',
        'AbstractCMSComponent'
    ]
};
