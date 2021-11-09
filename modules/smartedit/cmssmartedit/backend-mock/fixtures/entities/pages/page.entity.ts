/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
export interface IPage {
    creationtime: string;
    modifiedtime: string;
    pk: string;
    masterTemplate: string;
    name: string;
    typeCode: string;
    uid: string;
    label?: string;
    type?: string;
    defaultPage?: boolean;
    onlyOneRestrictionMustApply?: boolean;
    title?: { en: string; de?: string };
    itemtype?: string;
}
