/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IPageRestriction } from './pages';

export interface ICommonSlot {
    uid: string;
    uuid: string;
    id?: string;
    name?: string | { en: string };
    headline?: string;
    visible?: boolean;
    active?: boolean;
    typeCode?: string;
    catalogVersion?: string;
    slots?: string[];
    creationtime?: string | number;
    modifiedtime?: string | number;
    activationDate?: string | number;
    itemtype?: string;
    cloneable?: boolean;
    external?: boolean;
    media?: { en: string | { widescreen: string; desktop: string }; hi?: string } | string;
    restrictions?: string[] | IPageRestriction[];
    onlyOneRestrictionMustApply?: boolean;
    content?:
        | {
              en: string;
              fr?: string;
              pl?: string;
              it?: string;
              hi?: string;
          }
        | string;
    orientation?: string;
    linkToggle?: {
        external: boolean;
        urlLink: string;
    };
    navigationComponent?: string;
    linkName?: { en: string };
    product?: string;
    type?: string;
    defaultPage?: boolean;
    masterTemplate?: string;
    title?: {
        de?: string;
        en?: string;
    };
    approvalStatus?: string;
    displayStatus?: string;
    pageStatus?: string;
    label?: string;
    template?: string;
    copyToCatalogsDisabled?: boolean;
    pk?: string;
}
