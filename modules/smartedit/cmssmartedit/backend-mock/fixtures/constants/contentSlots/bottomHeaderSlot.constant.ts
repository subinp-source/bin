/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IContentSlot } from '../../entities/contentSlots';

export const bottomHeaderSlot: IContentSlot = {
    contentSlotName: 'bottomHeaderSlot',
    contentSlotUid: 'bottomHeaderSlot',
    validComponentTypes: [
        'componentType4',
        'CMSParagraphComponent',
        'AbstractCMSComponent',
        'SimpleBannerComponent'
    ]
};
