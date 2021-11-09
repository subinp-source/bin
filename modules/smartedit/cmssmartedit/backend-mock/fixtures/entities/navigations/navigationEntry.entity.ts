/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
export interface INavigationEntry {
    name: string;
    itemId: string;
    itemType: string;
    itemSuperType: string;
    navigationNodeUid?: string;
    uid?: string;
    catalogVersion?: string;
}
