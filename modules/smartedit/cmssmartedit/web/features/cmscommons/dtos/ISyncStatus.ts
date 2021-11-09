/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TypedMap } from 'smarteditcommons';

/**
 * @description
 * Interface for synchronization information
 */
export interface ISyncStatus {
    catalogVersionUuid: string;
    catalogVersionNameTemplate: string;
    dependentItemTypesOutOfSync: TypedMap<string>[];
    itemId: string;
    itemType: string;
    name: string;
    status: string;
    lastSyncStatus: number;
    lastModifiedDate: number;
    selectedDependencies: ISyncStatus[];
    sharedDependencies: ISyncStatus[];
    unavailableDependencies: ISyncStatus[];
}
