/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SynchronizationStatus } from './synchronizationConstants';
import { ISyncStatus } from '../..';

export interface ISynchronizationPanelApi {
    selectAll: () => void;
    displayItemList: (visible: boolean) => void;
    disableItemList: (disable: boolean) => void;
    setMessage: (msg: { type: string; description: string }) => void;
    disableItem: (item: ISyncStatusItem) => void;
}

export interface ISyncStatusItem extends ISyncStatus {
    isExternal: boolean;
    selected: boolean;
    status: SynchronizationStatus;
    i18nKey: string;
}
