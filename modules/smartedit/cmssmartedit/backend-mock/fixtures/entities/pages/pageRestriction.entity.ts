/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
export interface IPageRestriction {
    uid: string;
    uuid: string;
    name: string;
    typeCode: string;
    itemtype: string;
    description: string;
    categories?: string[];
}
